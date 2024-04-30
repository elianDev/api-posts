INSERT INTO tb_user(name, email, password) VALUES ('José Rocha', 'jose@gmail.com', '$2a$10$eDKQQCSVwERFyi5xTlpPKudmw8P/Q4Dum7b4bdh4WnIA3xtB6zEzy');
INSERT INTO tb_user(name, email, password) VALUES ('Maria Sousa', 'maria@gmail.com', '$2a$10$eDKQQCSVwERFyi5xTlpPKudmw8P/Q4Dum7b4bdh4WnIA3xtB6zEzy');

INSERT INTO tb_role(authority) VALUES ('ROLE_USER');
INSERT INTO tb_role(authority) VALUES ('ROLE_ADMIN');

INSERT INTO tb_user_role(user_id, role_id) VALUES (1, 1);
INSERT INTO tb_user_role(user_id, role_id) VALUES (2, 1);
INSERT INTO tb_user_role(user_id, role_id) VALUES (2, 2);

INSERT INTO tb_post(text, created_at, user_id) VALUES ('Eai galera!', '2023-07-27 15:31:22', 2);
INSERT INTO tb_post(text, created_at, user_id) VALUES ('Bom dia!', '2023-07-27 09:31:22', 2);
INSERT INTO tb_post(text, created_at, user_id) VALUES ('Só testando aqui :D', '2023-07-27 11:31:22', 1);

INSERT INTO tb_comment(text, created_at, user_id, post_id) VALUES ('Opa, tudo bom?', '2023-07-28 15:31:22', 1, 1);
