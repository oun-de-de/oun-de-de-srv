ALTER TABLE weight_record
    ADD quantity_per_product DECIMAL NULL;

ALTER TABLE weight_record
DROP
COLUMN weight_per_product;