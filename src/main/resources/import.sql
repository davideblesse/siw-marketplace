INSERT INTO image_entity (id, name) VALUES (nextval('image_entity_seq'), 'iphone.jpg');
INSERT INTO image_entity (id, name) VALUES (nextval('image_entity_seq'), 'iphone_2.jpg');
INSERT INTO image_entity (id, name) VALUES (nextval('image_entity_seq'), 'ipad.jpg');
INSERT INTO image_entity (id, name) VALUES (nextval('image_entity_seq'), 'macbook.jpg');

INSERT INTO users (id, name, surname, email) VALUES (nextval('users_seq'), 'Mario', 'Rossi', 'mariorossi@example.com');
INSERT INTO users (id, name, surname, email) VALUES (nextval('users_seq'), 'Luigi', 'Bianchi', 'luigibianchi@example.com');

INSERT INTO annuncio (id, title, price, categoria, owner_id) VALUES (nextval('annuncio_seq'),'IPHONE', 100, 'ELETTRONICA', (SELECT id FROM users WHERE email = 'mariorossi@example.com'));
INSERT INTO annuncio (id, title, price, categoria, owner_id) VALUES (nextval('annuncio_seq'), 'IPAD', 200, 'ELETTRONICA', (SELECT id FROM users WHERE email = 'mariorossi@example.com'));
INSERT INTO annuncio (id, title, price, categoria, owner_id) VALUES (nextval('annuncio_seq'), 'MACBOOK', 300, 'ELETTRONICA', (SELECT id FROM users WHERE email = 'mariorossi@example.com'));

INSERT INTO commento (id, user_id, annuncio_id, comment, offer) VALUES (nextval('commento_seq'), (SELECT id FROM users WHERE email = 'mariorossi@example.com'), (SELECT id FROM annuncio WHERE title = 'IPHONE'), 'Sono super interessato', 1100);
INSERT INTO commento (id, user_id, annuncio_id, comment, offer) VALUES (nextval('commento_seq'), (SELECT id FROM users WHERE email = 'luigibianchi@example.com'),(SELECT id FROM annuncio WHERE title = 'IPHONE'), 'Sono interessato', 1000);

INSERT INTO credentials (id, user_id, username, password, role) VALUES (nextval('commento_seq'), (SELECT id FROM users WHERE email = 'mariorossi@example.com'), 'user', '$2a$12$5prWEhDvUP7OdU9cFa2/quN2spDBCBiKXyWTXXQyA1N2wkHZAh32S', 'DEFAULT');

INSERT INTO annuncio_images (annuncio_id, images_id) VALUES ((SELECT id FROM annuncio WHERE title = 'IPHONE'), (SELECT id FROM image_entity WHERE name = 'iphone.jpg'));
INSERT INTO annuncio_images (annuncio_id, images_id) VALUES ((SELECT id FROM annuncio WHERE title = 'IPHONE'), (SELECT id FROM image_entity WHERE name = 'iphone_2.jpg'));
INSERT INTO annuncio_images (annuncio_id, images_id) VALUES ((SELECT id FROM annuncio WHERE title = 'IPAD'), (SELECT id FROM image_entity WHERE name = 'ipad.jpg'));
INSERT INTO annuncio_images (annuncio_id, images_id) VALUES ((SELECT id FROM annuncio WHERE title = 'MACBOOK'), (SELECT id FROM image_entity WHERE name = 'macbook.jpg'));