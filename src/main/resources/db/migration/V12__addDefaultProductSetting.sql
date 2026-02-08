CREATE TABLE default_product_setting
(
    id       VARCHAR(255) NOT NULL,
    org_id   VARCHAR(20)  NOT NULL,
    price    DECIMAL NULL,
    quantity DECIMAL NULL,
    CONSTRAINT pk_defaultproductsetting PRIMARY KEY (id)
);

ALTER TABLE product
    ADD default_product_setting_id VARCHAR(255) NULL;

ALTER TABLE product_setting
    ADD quantity DECIMAL NULL;

CREATE INDEX org_index ON default_product_setting (org_id);

ALTER TABLE product
    ADD CONSTRAINT FK_PRODUCT_ON_DEFAULTPRODUCTSETTING FOREIGN KEY (default_product_setting_id) REFERENCES default_product_setting (id);

ALTER TABLE product_setting
DROP
COLUMN weight;