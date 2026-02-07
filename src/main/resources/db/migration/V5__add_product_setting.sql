CREATE TABLE product_setting
(
    org_id      VARCHAR(20)  NOT NULL,
    product_id  VARCHAR(255) NOT NULL,
    customer_id VARCHAR(255) NOT NULL
);

ALTER TABLE product_setting
    ADD PRIMARY KEY (customer_id, product_id);

CREATE INDEX org_index ON product_setting (org_id);

ALTER TABLE product_setting
    ADD CONSTRAINT FK_PRODUCTSETTING_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);

ALTER TABLE product_setting
    ADD CONSTRAINT FK_PRODUCTSETTING_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id);