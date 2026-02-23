CREATE TABLE payment
(
    id           VARCHAR(255)   NOT NULL,
    org_id       VARCHAR(20)    NOT NULL,
    cycle_id     VARCHAR(255)   NOT NULL,
    payment_date datetime NULL,
    amount       DECIMAL(19, 5) NOT NULL,
    CONSTRAINT pk_payment PRIMARY KEY (id)
);

CREATE INDEX org_index ON payment (org_id);

ALTER TABLE payment
    ADD CONSTRAINT FK_PAYMENT_ON_CYCLE FOREIGN KEY (cycle_id) REFERENCES payment_term_cycle (id);