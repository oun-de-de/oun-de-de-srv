CREATE TABLE payment_term_cycle
(
    id          VARCHAR(255) NOT NULL,
    org_id      VARCHAR(20)  NOT NULL,
    customer_id VARCHAR(255) NOT NULL,
    start_date  datetime     NOT NULL,
    end_date    datetime     NOT NULL,
    CONSTRAINT pk_paymenttermcycle PRIMARY KEY (id)
);

CREATE INDEX org_index ON payment_term_cycle (org_id);

ALTER TABLE payment_term_cycle
    ADD CONSTRAINT FK_PAYMENTTERMCYCLE_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);