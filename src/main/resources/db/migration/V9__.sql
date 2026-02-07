ALTER TABLE customer
    ADD warehouse_id VARCHAR(255) NULL;

ALTER TABLE customer
    ADD CONSTRAINT FK_CUSTOMER_ON_WAREHOUSE FOREIGN KEY (warehouse_id) REFERENCES warehouse (id);

ALTER TABLE customer
DROP
COLUMN warehouse;