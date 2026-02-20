CREATE TABLE equipment_borrow
(
    id                   VARCHAR(255) NOT NULL,
    org_id               VARCHAR(20)  NOT NULL,
    item_id              VARCHAR(255) NOT NULL,
    customer_id          VARCHAR(255) NOT NULL,
    quantity             DECIMAL      NOT NULL,
    borrow_date          datetime     NOT NULL,
    expected_return_date datetime NULL,
    actual_return_date   datetime NULL,
    status               SMALLINT NULL,
    CONSTRAINT pk_equipmentborrow PRIMARY KEY (id)
);

CREATE TABLE inventory_item
(
    id               VARCHAR(255) NOT NULL,
    org_id           VARCHAR(20)  NOT NULL,
    code             VARCHAR(255) NOT NULL,
    name             VARCHAR(255) NOT NULL,
    type             SMALLINT     NOT NULL,
    unit_id          VARCHAR(255) NULL,
    quantity_on_hand DECIMAL      NOT NULL,
    alert_threshold  DECIMAL NULL,
    CONSTRAINT pk_inventoryitem PRIMARY KEY (id)
);

CREATE TABLE stock_transaction
(
    id                  VARCHAR(255) NOT NULL,
    org_id              VARCHAR(20)  NOT NULL,
    item_id             VARCHAR(255) NOT NULL,
    quantity            DECIMAL      NOT NULL,
    type                SMALLINT     NOT NULL,
    reason              SMALLINT     NOT NULL,
    memo                VARCHAR(255) NULL,
    equipment_borrow_id VARCHAR(255) NULL,
    created_at          datetime     NOT NULL,
    created_by_id       VARCHAR(255) NOT NULL,
    CONSTRAINT pk_stocktransaction PRIMARY KEY (id)
);

CREATE INDEX org_index ON stock_transaction (org_id);

CREATE INDEX org_index ON inventory_item (org_id);

CREATE INDEX org_index ON equipment_borrow (org_id);

ALTER TABLE equipment_borrow
    ADD CONSTRAINT FK_EQUIPMENTBORROW_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);

ALTER TABLE equipment_borrow
    ADD CONSTRAINT FK_EQUIPMENTBORROW_ON_ITEM FOREIGN KEY (item_id) REFERENCES inventory_item (id);

ALTER TABLE inventory_item
    ADD CONSTRAINT FK_INVENTORYITEM_ON_UNIT FOREIGN KEY (unit_id) REFERENCES unit (id);

ALTER TABLE stock_transaction
    ADD CONSTRAINT FK_STOCKTRANSACTION_ON_CREATEDBY FOREIGN KEY (created_by_id) REFERENCES user (id);

ALTER TABLE stock_transaction
    ADD CONSTRAINT FK_STOCKTRANSACTION_ON_EQUIPMENTBORROW FOREIGN KEY (equipment_borrow_id) REFERENCES equipment_borrow (id);

ALTER TABLE stock_transaction
    ADD CONSTRAINT FK_STOCKTRANSACTION_ON_ITEM FOREIGN KEY (item_id) REFERENCES inventory_item (id);