package it.uniroma3.marketplace.authentication;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static it.uniroma3.marketplace.model.Credentials.ADMIN_ROLE;
import static it.uniroma3.marketplace.model.Credentials.DEFAULT_ROLE;
import it.uniroma3.marketplace.service.CredentialsService;

@Configuration
@EnableWebSecurity
public class AuthConfiguration {
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.authoritiesByUsernameQuery("SELECT username, role from credentials WHERE username=?")
				.usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
	}


	@Bean
	protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(withDefaults()).cors(cors -> cors.disable()).authorizeHttpRequests(requests -> requests
                // Consentiti a tutti (occasionali)
                .requestMatchers(HttpMethod.GET, "/", "/index", "/annunci/**",
                        "/login", "/register", "/css/**", "/images/**", "favicon.ico")
                .permitAll().requestMatchers(HttpMethod.POST, "/register", "/login", "/images/**")
                .permitAll()

                // Solo ADMIN_ROLE
                .requestMatchers("/admin/**").hasAuthority(ADMIN_ROLE)

                // Solo DEFAULT_ROLE
                .requestMatchers("/user/**").hasAuthority(DEFAULT_ROLE)

                // Qualunque altra richiesta: autenticazione
                .anyRequest().authenticated()).formLogin(login -> login.loginPage("/login") // Pagina di login di default
                // per tutti
                .loginProcessingUrl("/login") // URL di submit form login user
                .usernameParameter("username").passwordParameter("pwd")
                .successHandler((request, response, authentication) -> {
                    // Success handler custom: redirect in base al ruolo
                    var principal = authentication.getPrincipal();
                    // Recupero id utente dal Principal
                    Long idUtente = null;
                    String username = null;
                    if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
                        // Ottieni ID utente qui secondo la tua implementazione
                        // Esempio: CredentialsService -> trova utente per username
                        username = userDetails.getUsername();
                        idUtente = this.credentialsService.findByUsername(username).getUser().getId();

                    }
                    boolean isAdmin = this.credentialsService.findByUsername(username).getRole().equals(ADMIN_ROLE);
                    if (isAdmin) {
                        // Se ADMIN, redirect operatore (sostituisci idUtente)
                        response.sendRedirect(idUtente != null ? "/admin/annunci" : "/login");
                    } else {
                        // Se Utente, redirect utente (sostituisci idUtente)
                        response.sendRedirect(idUtente != null ? "/user/annunci" : "/login");
                    }
                }).failureUrl("/login?error=true").permitAll()).logout(logout -> logout.logoutUrl("/logout")
                .logoutSuccessUrl("/").invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .clearAuthentication(true).permitAll());
		return httpSecurity.build();
	}
}
