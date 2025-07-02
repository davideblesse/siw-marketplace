-- immagini
INSERT INTO image_entity (id, name) VALUES (1, 'Amore_Ai_Tempi_1.jpg');
INSERT INTO image_entity (id, name) VALUES (2, 'Amore_Ai_Tempi_2.jpg');
INSERT INTO image_entity (id, name) VALUES (3, 'Cent_Anni_1.jpg');
INSERT INTO image_entity (id, name) VALUES (4, 'Cent_Anni_2.jpg');
INSERT INTO image_entity (id, name) VALUES (5, 'maglietta_1.jpg');
INSERT INTO image_entity (id, name) VALUES (6, 'maglietta_2.jpg');
INSERT INTO image_entity (id, name) VALUES (7, 'Speaker_1.jpg');
INSERT INTO image_entity (id, name) VALUES (8, 'Speaker_2.jpg');
INSERT INTO image_entity (id, name) VALUES (9, 'powerbank_1.jpg');
INSERT INTO image_entity (id, name) VALUES (10, 'powerbank_2.jpg');

-- utenti
INSERT INTO users (id, name, surname, email) VALUES (1, 'Mario', 'Rossi', 'mariorossi@example.com');
INSERT INTO users (id, name, surname, email) VALUES (2, 'Luigi', 'Bianchi', 'luigibianchi@example.com');

-- annunci
INSERT INTO annuncio (id, title, price, categoria, owner_id) VALUES (4, 'Amore ai Tempi del Colera', 20, 'LIBRI', 2);
INSERT INTO annuncio (id, title, price, categoria, owner_id) VALUES (5, 'Cent''Anni di Solitudine', 18, 'LIBRI', 2);
INSERT INTO annuncio (id, title, price, categoria, owner_id) VALUES (6, 'Maglietta Vintage Taglia M', 10, 'ABBIGLIAMENTO', 1);
INSERT INTO annuncio (id, title, price, categoria, owner_id) VALUES (7, 'Speaker Bluetooth Portatile', 25, 'ELETTRONICA', 1);
INSERT INTO annuncio (id, title, price, categoria, owner_id) VALUES (8, 'Powerbank 10000mAh', 30, 'ELETTRONICA', 2);


-- commenti
INSERT INTO commento (id, user_id, annuncio_id, text, offer) VALUES (3, 1, 8, 'Sto cercando un powerbank affidabile per i miei viaggi: questo ha una buona capacità?', 30);
INSERT INTO commento (id, user_id, annuncio_id, text, offer) VALUES (4, 2, 6, 'La maglietta mi piace molto, è disponibile in taglia M o L?', 20);



-- credenziali
INSERT INTO credentials (id, user_id, username, password, role) VALUES (1, 1, 'user', '$2a$12$5prWEhDvUP7OdU9cFa2/quN2spDBCBiKXyWTXXQyA1N2wkHZAh32S', 'DEFAULT');
INSERT INTO credentials (id, user_id, username, password, role) VALUES (2, 2, 'admin', '$2a$12$r5E6fDdqaIIz2KxEMIQaF.vBZrC3WHZTK6F/51HdClPXJcUdM914W', 'ADMIN');

-- relazioni many-to-many
INSERT INTO annuncio_images (annuncio_id, images_id) VALUES (4, 1);
INSERT INTO annuncio_images (annuncio_id, images_id) VALUES (4, 2);
INSERT INTO annuncio_images (annuncio_id, images_id) VALUES (5, 3);
INSERT INTO annuncio_images (annuncio_id, images_id) VALUES (5, 4);
INSERT INTO annuncio_images (annuncio_id, images_id) VALUES (6, 5);
INSERT INTO annuncio_images (annuncio_id, images_id) VALUES (6, 6);
INSERT INTO annuncio_images (annuncio_id, images_id) VALUES (7, 7);
INSERT INTO annuncio_images (annuncio_id, images_id) VALUES (7, 8);
INSERT INTO annuncio_images (annuncio_id, images_id) VALUES (8, 9);
INSERT INTO annuncio_images (annuncio_id, images_id) VALUES (8, 10);


-- riallineamento delle sequence
SELECT setval('image_entity_seq',   (SELECT MAX(id) FROM image_entity));
SELECT setval('users_seq',          (SELECT MAX(id) FROM users));
SELECT setval('annuncio_seq',       (SELECT MAX(id) FROM annuncio));
SELECT setval('commento_seq',       (SELECT MAX(id) FROM commento));
SELECT setval('credentials_seq',    (SELECT MAX(id) FROM credentials));
