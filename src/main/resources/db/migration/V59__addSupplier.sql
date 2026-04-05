CREATE TABLE supplier
(
    id        VARCHAR(255) NOT NULL,
    name      VARCHAR(255) NULL,
    descr     VARCHAR(1000) NULL,
    org_id    VARCHAR(20)  NOT NULL,
    address   VARCHAR(255) NULL,
    telephone VARCHAR(255) NULL,
    CONSTRAINT pk_supplier PRIMARY KEY (id)
);

ALTER TABLE inventory_item
    ADD supplier_id VARCHAR(255) NULL;

CREATE INDEX org_index ON supplier (org_id);

ALTER TABLE inventory_item
    ADD CONSTRAINT FK_INVENTORYITEM_ON_SUPPLIER FOREIGN KEY (supplier_id) REFERENCES supplier (id);