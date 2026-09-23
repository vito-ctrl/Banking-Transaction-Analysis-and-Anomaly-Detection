CREATE DATABASE IF NOT EXISTS banking_analysis;

USE banking_analysis;

CREATE TABLE clients (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE
) ENGINE = InnoDB;

CREATE TABLE accounts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    number VARCHAR(30) NOT NULL UNIQUE,
    balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    client_id BIGINT NOT NULL,

    account_type ENUM('CURRENT', 'SAVINGS') NOT NULL,

    authorized_overdraft DECIMAL(15, 2) DEFAULT NULL,
    interest_rate DECIMAL(5, 2) DEFAULT NULL,

    CONSTRAINT fk_account_client
        FOREIGN KEY (client_id)
        REFERENCES clients(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE = InnoDB;

CREATE TABLE transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transaction_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(15, 2) NOT NULL,

    transaction_type ENUM(
        'DEPOSIT',
        'WITHDRAWAL',
        'TRANSFER'
    ) NOT NULL,

    location VARCHAR(150) NOT NULL,

    account_id BIGINT NOT NULL,

    CONSTRAINT fk_transaction_account
        FOREIGN KEY (account_id)
        REFERENCES accounts(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE = InnoDB;