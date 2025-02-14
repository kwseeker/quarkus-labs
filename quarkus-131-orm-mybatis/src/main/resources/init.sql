DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS books;

CREATE TABLE users
(
    id         INT         NOT NULL PRIMARY KEY,
    name       VARCHAR(80) NOT NULL,
    externalId CHAR(36)    NOT NULL
);

CREATE TABLE books
(
    id        INT         NOT NULL PRIMARY KEY,
    title     VARCHAR(80) NOT NULL,
    author_id INT         NOT NULL
#     FOREIGN KEY (author_id) REFERENCES users (id)
);

DELETE FROM users where id > 0;
INSERT INTO users (id, name, externalId)
VALUES (1, 'Test User1', 'ccb16b65-8924-4c3f-8c55-681d85a16e79');
INSERT INTO users (id, name, externalId)
VALUES (2, 'Test User2', 'ae43f233-0b69-4c4e-bfa9-656c475150ad');
INSERT INTO users (id, name, externalId)
VALUES (3, 'Test User3', '5640e179-466c-427e-9747-4cfac09a2f9a');

DELETE FROM books where id > 0;
INSERT INTO books(id, title, author_id)
VALUES (1, 'Test Title', 1);