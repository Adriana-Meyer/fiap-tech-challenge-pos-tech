-- ============================================================
-- Workshop Management System — Initial Schema
-- ============================================================

CREATE TABLE customers (
    id             CHAR(36)      NOT NULL,
    document_value VARCHAR(14)   NOT NULL,
    document_type  VARCHAR(10)   NOT NULL,
    name           VARCHAR(255)  NOT NULL,
    phone          VARCHAR(20),
    email          VARCHAR(255),
    created_at     DATETIME      NOT NULL,
    CONSTRAINT pk_customers PRIMARY KEY (id),
    CONSTRAINT uq_customers_document UNIQUE (document_value)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE vehicles (
    id          CHAR(36)      NOT NULL,
    plate_value VARCHAR(8)    NOT NULL,
    brand       VARCHAR(100)  NOT NULL,
    model       VARCHAR(100)  NOT NULL,
    year        INT           NOT NULL,
    color       VARCHAR(50),
    customer_id CHAR(36)      NOT NULL,
    CONSTRAINT pk_vehicles PRIMARY KEY (id),
    CONSTRAINT uq_vehicles_plate UNIQUE (plate_value),
    CONSTRAINT fk_vehicles_customer FOREIGN KEY (customer_id) REFERENCES customers (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE service_catalog_items (
    id          CHAR(36)      NOT NULL,
    name        VARCHAR(255)  NOT NULL,
    description TEXT,
    type        VARCHAR(50)   NOT NULL,
    base_price  DECIMAL(15,2) NOT NULL,
    active      BOOLEAN       NOT NULL DEFAULT TRUE,
    CONSTRAINT pk_service_catalog_items PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE supplies (
    id             CHAR(36)      NOT NULL,
    code           VARCHAR(50)   NOT NULL,
    name           VARCHAR(255)  NOT NULL,
    description    TEXT,
    type           VARCHAR(20)   NOT NULL,
    unit_price     DECIMAL(15,2) NOT NULL,
    stock_quantity INT           NOT NULL DEFAULT 0,
    minimum_stock  INT           NOT NULL DEFAULT 0,
    CONSTRAINT pk_supplies PRIMARY KEY (id),
    CONSTRAINT uq_supplies_code UNIQUE (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE service_orders (
    id                    CHAR(36)      NOT NULL,
    os_code               VARCHAR(20)   NOT NULL,
    status                VARCHAR(30)   NOT NULL,
    customer_id           CHAR(36)      NOT NULL,
    vehicle_id            CHAR(36)      NOT NULL,
    total_amount          DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    diagnosis_notes       TEXT,
    customer_comments     TEXT,
    received_at           DATETIME      NOT NULL,
    diagnosis_started_at  DATETIME,
    waiting_approval_at   DATETIME,
    execution_started_at  DATETIME,
    execution_finished_at DATETIME,
    delivered_at          DATETIME,
    created_at            DATETIME      NOT NULL,
    updated_at            DATETIME      NOT NULL,
    CONSTRAINT pk_service_orders PRIMARY KEY (id),
    CONSTRAINT uq_service_orders_os_code UNIQUE (os_code),
    CONSTRAINT fk_service_orders_customer FOREIGN KEY (customer_id) REFERENCES customers (id),
    CONSTRAINT fk_service_orders_vehicle  FOREIGN KEY (vehicle_id)  REFERENCES vehicles (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE service_order_items (
    id                      CHAR(36)      NOT NULL,
    service_order_id        CHAR(36)      NOT NULL,
    service_catalog_item_id CHAR(36),
    supply_id               CHAR(36),
    quantity                INT           NOT NULL DEFAULT 1,
    unit_price              DECIMAL(15,2) NOT NULL,
    subtotal                DECIMAL(15,2) NOT NULL,
    execution_started_at    DATETIME,
    execution_finished_at   DATETIME,
    CONSTRAINT pk_service_order_items PRIMARY KEY (id),
    CONSTRAINT fk_items_service_order  FOREIGN KEY (service_order_id)        REFERENCES service_orders (id),
    CONSTRAINT fk_items_catalog_item   FOREIGN KEY (service_catalog_item_id) REFERENCES service_catalog_items (id),
    CONSTRAINT fk_items_supply         FOREIGN KEY (supply_id)               REFERENCES supplies (id),
    CONSTRAINT chk_items_type          CHECK (service_catalog_item_id IS NOT NULL OR supply_id IS NOT NULL)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE users (
    id            CHAR(36)      NOT NULL,
    email         VARCHAR(255)  NOT NULL,
    password_hash VARCHAR(255)  NOT NULL,
    role          VARCHAR(20)   NOT NULL,
    active        BOOLEAN       NOT NULL DEFAULT TRUE,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
