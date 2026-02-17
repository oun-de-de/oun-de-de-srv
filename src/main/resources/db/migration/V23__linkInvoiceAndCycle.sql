ALTER TABLE invoice
    ADD cycle_id VARCHAR(255) NULL;

ALTER TABLE invoice
    ADD CONSTRAINT FK_INVOICE_ON_CYCLE FOREIGN KEY (cycle_id) REFERENCES payment_term_cycle (id);
