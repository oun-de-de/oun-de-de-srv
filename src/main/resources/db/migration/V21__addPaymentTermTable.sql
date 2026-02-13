CREATE TABLE payment_term
(
    id          VARCHAR(255) NOT NULL,
    org_id      VARCHAR(20)  NOT NULL,
    customer_id VARCHAR(255) NOT NULL,
    duration    INT NULL,
    start_date  datetime NULL,
    CONSTRAINT pk_paymentterm PRIMARY KEY (id)
);

ALTER TABLE customer
    ADD payment_term_id VARCHAR(255) NULL;

CREATE INDEX org_index ON payment_term (org_id);

ALTER TABLE customer
    ADD CONSTRAINT FK_CUSTOMER_ON_PAYMENTTERM FOREIGN KEY (payment_term_id) REFERENCES payment_term (id);

ALTER TABLE payment_term
    ADD CONSTRAINT FK_PAYMENTTERM_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);

ALTER TABLE customer
DROP
COLUMN payment_term;

ALTER TABLE product
    MODIFY name VARCHAR (255) NOT NULL;

ALTER TABLE contact
    MODIFY org_id VARCHAR (20) NOT NULL;

ALTER TABLE coupon
    MODIFY org_id VARCHAR (20) NOT NULL;

ALTER TABLE customer
    MODIFY org_id VARCHAR (20) NOT NULL;

ALTER TABLE product
    MODIFY org_id VARCHAR (20) NOT NULL;

ALTER TABLE user
    MODIFY org_id VARCHAR (20) NOT NULL;

ALTER TABLE vehicle
    MODIFY org_id VARCHAR (20) NOT NULL;

ALTER TABLE weight_record
    MODIFY org_id VARCHAR (20) NOT NULL;