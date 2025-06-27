package it.uniroma3.marketplace.authentication;

import static it.uniroma3.marketplace.model.Credentials.ADMIN_ROLE;
import static it.uniroma3.marketplace.model.Credentials.DEFAULT_ROLE;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import it.uniroma3.marketplace.service.CredentialsService;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

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
		httpSecurity.csrf().and().cors().disable().authorizeHttpRequests()
				// Consentiti a tutti (occasionali)
				.requestMatchers(HttpMethod.GET, "/", "/index", "/annunci/**", "/authors/**",
						"/login", "/register", "/css/**", "/images/**", "favicon.ico")
				.permitAll().requestMatchers(HttpMethod.POST, "/register", "/login",  "/images/**")
				.permitAll()

				// Solo ADMIN_ROLE
				.requestMatchers("/admin/**").hasAuthority(ADMIN_ROLE)

				// Solo DEFAULT_ROLE
				.requestMatchers("/user/**").hasAuthority(DEFAULT_ROLE)

				// Qualunque altra richiesta: autenticazione
				.anyRequest().authenticated().and().formLogin().loginPage("/login") // Pagina di login di default
																							// per tutti
				.loginProcessingUrl("/login") // URL di submit form login user
				.usernameParameter("username").passwordParameter("password")
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
						response.sendRedirect(idUtente != null ? "/admin/annunci": "/login");
					} else {
						// Se Utente, redirect utente (sostituisci idUtente)
						response.sendRedirect(idUtente != null ? "/user/annunci" : "/login");
					}
				}).failureUrl("/login?error=true").permitAll().and().logout().logoutUrl("/logout")
				.logoutSuccessUrl("/").invalidateHttpSession(true).deleteCookies("JSESSIONID")
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).clearAuthentication(true).permitAll();
		return httpSecurity.build();
	}

}