DROP DATABASE IF EXISTS BankDB;
CREATE DATABASE BankDB;
USE BankDB;

CREATE TABLE account_registry (
	AccountNumber VARCHAR(50) NOT NULL,
	LastName VARCHAR(50) NOT NULL,
	FirstName VARCHAR(50) NOT NULL,
	UserName VARCHAR(50) NOT NULL UNIQUE,
	DOB DATE NOT NULL,
	EmailAddress VARCHAR(50) NOT NULL UNIQUE,
	PasswordHash VARCHAR(255) NOT NULL,
	CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

	CONSTRAINT pk_account_registry PRIMARY KEY (AccountNumber)
);

CREATE TABLE account_checking (
	AccountNumber VARCHAR(50) NOT NULL,
	CheckingBalance DECIMAL(10,2) NOT NULL,
	MinimumBalance DECIMAL(10,2) AS (CheckingBalance * 0.25) STORED,
	MaxCheckingWithdrawal DECIMAL(10,2) AS (CheckingBalance * 0.4) STORED,

	CONSTRAINT pk_account_checking PRIMARY KEY (AccountNumber),
	CONSTRAINT fk_account_checking_account_registry FOREIGN KEY (AccountNumber) 
	REFERENCES account_registry (AccountNumber) 
	ON DELETE CASCADE
	ON UPDATE CASCADE
);

CREATE TABLE address (
	AddressID INT AUTO_INCREMENT NOT NULL,
	AccountNumber VARCHAR(50) NOT NULL,
	StreetAddress VARCHAR(255) NOT NULL,
	StreetAddress2 VARCHAR(255),
	ZipCode VARCHAR(10) NOT NULL,
	City VARCHAR(100) NOT NULL,
	StateCode CHAR(2) NOT NULL,

	CONSTRAINT pk_address PRIMARY KEY (AddressID),
	CONSTRAINT fk_address_account_number FOREIGN KEY (AccountNumber) 
	REFERENCES account_registry(AccountNumber) 
	ON DELETE CASCADE
	ON UPDATE CASCADE
);

CREATE TABLE transactions (
	TransactionID INT AUTO_INCREMENT NOT NULL,
	AccountNumber VARCHAR(50) NOT NULL,
	TransactionType ENUM('Deposit', 'Withdraw') NOT NULL,
	Amount DECIMAL(10, 2) NOT NULL,
	TransactionTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

	CONSTRAINT pk_transactions PRIMARY KEY (TransactionID),
	CONSTRAINT fk_transactions_account_number FOREIGN KEY (AccountNumber) 
	REFERENCES account_registry(AccountNumber)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);

INSERT INTO account_registry (AccountNumber, LastName, FirstName, UserName, DOB, EmailAddress, PasswordHash) values (
	'A0001', 'Nerne', 'Sig', 'snerne0', '1944-06-29', 'snerne0@freewebs.com', 'SAF'
	);
INSERT INTO account_registry (AccountNumber, LastName, FirstName, UserName, DOB, EmailAddress, PasswordHash) values (
	'A0002', 'Preddy', 'Hazel', 'hpreddy1', '1993-07-16', 'hpreddy1@bbb.org', 'PasswFGAG2ord'
	);
INSERT INTO account_registry (AccountNumber, LastName, FirstName, UserName, DOB, EmailAddress, PasswordHash) values (
	'A0003', 'Dykes', 'Lana', 'ldykes2', '1957-01-08', 'ldykes2@acquirethisname.com', 'PasswASDFASDF2ord'
	);
INSERT INTO account_registry (AccountNumber, LastName, FirstName, UserName, DOB, EmailAddress, PasswordHash) values (
	'A0004', 'Menhci', 'Leelah', 'lmenhci3', '1993-08-06', 'lmenhci3@techcrunch.com', 'PassASFAS46word'
	);
INSERT INTO account_registry (AccountNumber, LastName, FirstName, UserName, DOB, EmailAddress, PasswordHash) values (
	'A0005', 'Hasell', 'Mimi', 'mhasell4', '2001-11-20', 'mhasell4@un.org', '64SADF12!'
	);

INSERT INTO account_checking (AccountNumber, CheckingBalance) values ('A0001', 578877);
INSERT INTO account_checking (AccountNumber, CheckingBalance) values ('A0002', 131740);
INSERT INTO account_checking (AccountNumber, CheckingBalance) values ('A0003', 766489);
INSERT INTO account_checking (AccountNumber, CheckingBalance) values ('A0004', 161419);
INSERT INTO account_checking (AccountNumber, CheckingBalance) values ('A0005', 140553);

INSERT INTO address (AccountNumber, StreetAddress, StreetAddress2, ZipCode, City, StateCode) VALUES (
	'A0001', '123 Main Street', NULL, '12345', 'New York', 'NY'
);
INSERT INTO address (AccountNumber, StreetAddress, StreetAddress2, ZipCode, City, StateCode) VALUES (
	'A0002', '456 Main Street', NULL, '12345', 'New York', 'NY'
);
INSERT INTO address (AccountNumber, StreetAddress, StreetAddress2, ZipCode, City, StateCode) VALUES (
	'A0003', '789 Main Street', NULL, '12345', 'New York', 'NY'
);
INSERT INTO address (AccountNumber, StreetAddress, StreetAddress2, ZipCode, City, StateCode) VALUES (
	'A0004', '321 Main Street', NULL, '12345', 'New York', 'NY'
);
INSERT INTO address (AccountNumber, StreetAddress, StreetAddress2, ZipCode, City, StateCode) VALUES (
	'A0005', '654 Main Street', NULL, '12345', 'New York', 'NY'
);