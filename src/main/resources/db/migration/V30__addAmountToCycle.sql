ALTER TABLE payment_term_cycle
    ADD total_amount DECIMAL(19, 5) NULL;

ALTER TABLE payment_term_cycle
    ADD total_paid_amount DECIMAL(19, 5) NULL;

ALTER TABLE payment_term_cycle
    MODIFY total_amount DECIMAL (19, 5) NOT NULL;

ALTER TABLE payment_term_cycle
    MODIFY total_paid_amount DECIMAL (19, 5) NOT NULL;