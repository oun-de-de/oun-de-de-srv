CREATE TABLE contact
(
    id               VARCHAR(255) NOT NULL,
    org_id           VARCHAR(255) NOT NULL,
    telephone        VARCHAR(255) NULL,
    email            VARCHAR(255) NULL,
    geography        VARCHAR(255) NULL,
    address          VARCHAR(1000) NULL,
    location         VARCHAR(255) NULL,
    map              VARCHAR(255) NULL,
    billing_address  VARCHAR(1000) NULL,
    delivery_address VARCHAR(1000) NULL,
    CONSTRAINT pk_contact PRIMARY KEY (id)
);

CREATE TABLE coupon
(
    id          VARCHAR(255) NOT NULL,
    org_id      VARCHAR(255) NOT NULL,
    date        datetime NULL,
    vehicle_id  VARCHAR(255) NOT NULL,
    driver_name VARCHAR(255) NULL,
    employee_id VARCHAR(255) NOT NULL,
    remark      VARCHAR(255) NULL,
    coupon_no   BIGINT NULL,
    coupon_id   BIGINT NULL,
    acc_no      VARCHAR(20) NULL,
    del_acc_no  VARCHAR(20) NULL,
    del_date    datetime NULL,
    CONSTRAINT pk_coupon PRIMARY KEY (id)
);

CREATE TABLE customer
(
    id              VARCHAR(255) NOT NULL,
    org_id          VARCHAR(255) NOT NULL,
    register_date   datetime     NOT NULL,
    code            VARCHAR(255) NOT NULL,
    name            VARCHAR(255) NOT NULL,
    status          BIT(1)       NOT NULL,
    referred_by_id  VARCHAR(255) NULL,
    default_price   VARCHAR(255) NULL,
    warehouse       VARCHAR(255) NULL,
    memo            VARCHAR(1000) NULL,
    employee_id     VARCHAR(255) NOT NULL,
    contact_id      VARCHAR(255) NULL,
    profile_url     VARCHAR(2000) NULL,
    shop_banner_url VARCHAR(2000) NULL,
    CONSTRAINT pk_customer PRIMARY KEY (id)
);

CREATE TABLE product
(
    id       VARCHAR(255) NOT NULL,
    org_id   VARCHAR(255) NOT NULL,
    name     VARCHAR(255) NULL,
    date     datetime NULL,
    ref_no   VARCHAR(255) NULL,
    quantity DECIMAL NULL,
    cost     DECIMAL NULL,
    price    DECIMAL NULL,
    CONSTRAINT pk_product PRIMARY KEY (id)
);

CREATE TABLE refresh_token
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    user_id     VARCHAR(255) NOT NULL,
    token       VARCHAR(255) NOT NULL,
    expiry_date datetime     NOT NULL,
    CONSTRAINT pk_refreshtoken PRIMARY KEY (id)
);

CREATE TABLE user
(
    id            VARCHAR(255) NOT NULL,
    org_id        VARCHAR(255) NOT NULL,
    username      VARCHAR(255) NOT NULL,
    password      VARCHAR(255) NOT NULL,
    first_name    VARCHAR(255) NULL,
    last_name     VARCHAR(255) NULL,
    created_by_id VARCHAR(255) NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE vehicle
(
    id            VARCHAR(255) NOT NULL,
    org_id        VARCHAR(255) NOT NULL,
    vehicle_type  SMALLINT NULL,
    license_plate VARCHAR(30) NULL,
    customer_id   VARCHAR(255) NOT NULL,
    CONSTRAINT pk_vehicle PRIMARY KEY (id)
);

CREATE TABLE weight_record
(
    id                 VARCHAR(255) NOT NULL,
    org_id             VARCHAR(255) NOT NULL,
    price_per_product  DECIMAL NULL,
    weight_per_product DECIMAL NULL,
    weight             DECIMAL NULL,
    quantity           DECIMAL NULL,
    out_time           datetime NULL,
    is_manual          BIT(1)       NOT NULL,
    coupon_id          VARCHAR(255) NULL,
    CONSTRAINT pk_weightrecord PRIMARY KEY (id)
);

ALTER TABLE refresh_token
    ADD CONSTRAINT uc_refreshtoken_token UNIQUE (token);

ALTER TABLE user
    ADD CONSTRAINT uc_user_username UNIQUE (username);

CREATE INDEX org_index ON contact (org_id);

CREATE INDEX org_index ON coupon (org_id);

CREATE INDEX org_index ON customer (org_id);

CREATE INDEX org_index ON product (org_id);

CREATE INDEX org_index ON user (org_id);

CREATE INDEX org_index ON vehicle (org_id);

CREATE INDEX org_index ON weight_record (org_id);

ALTER TABLE coupon
    ADD CONSTRAINT FK_COUPON_ON_EMPLOYEE FOREIGN KEY (employee_id) REFERENCES user (id);

ALTER TABLE coupon
    ADD CONSTRAINT FK_COUPON_ON_VEHICLE FOREIGN KEY (vehicle_id) REFERENCES vehicle (id);

ALTER TABLE customer
    ADD CONSTRAINT FK_CUSTOMER_ON_CONTACT FOREIGN KEY (contact_id) REFERENCES contact (id);

ALTER TABLE customer
    ADD CONSTRAINT FK_CUSTOMER_ON_EMPLOYEE FOREIGN KEY (employee_id) REFERENCES user (id);

ALTER TABLE customer
    ADD CONSTRAINT FK_CUSTOMER_ON_REFERREDBY FOREIGN KEY (referred_by_id) REFERENCES customer (id);

ALTER TABLE refresh_token
    ADD CONSTRAINT FK_REFRESHTOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE user
    ADD CONSTRAINT FK_USER_ON_CREATEDBY FOREIGN KEY (created_by_id) REFERENCES user (id);

ALTER TABLE vehicle
    ADD CONSTRAINT FK_VEHICLE_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);

ALTER TABLE weight_record
    ADD CONSTRAINT FK_WEIGHTRECORD_ON_COUPON FOREIGN KEY (coupon_id) REFERENCES coupon (id);