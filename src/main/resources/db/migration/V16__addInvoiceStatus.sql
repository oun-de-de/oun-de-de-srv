ALTER TABLE invoice
    ADD customer_name VARCHAR(255) NULL;

ALTER TABLE invoice
    ADD status SMALLINT NULL;

ALTER TABLE invoice
    MODIFY customer_name VARCHAR (255) NOT NULL;

ALTER TABLE invoice
    MODIFY status SMALLINT NOT NULL;