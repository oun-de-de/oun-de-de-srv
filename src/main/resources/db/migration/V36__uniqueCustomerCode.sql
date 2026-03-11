ALTER TABLE customer
    ADD CONSTRAINT uc_customer_code UNIQUE (code);