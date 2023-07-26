 DROP DATABASE IF EXISTS InvestmentManagementDB;

-- Create the database
CREATE DATABASE InvestmentManagementDB;

-- Use the database
USE InvestmentManagementDB;

 select * from Transaction;
 select * from Portfolio_Asset;
 select * from Asset;
 select * from Portfolio;
 select * from Account;
 select * from User;

-- Create the User table
CREATE TABLE User (
    UserID INT NOT NULL AUTO_INCREMENT,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    Email VARCHAR(100) NOT NULL,
    Phone VARCHAR(12) NOT NULL,
    PRIMARY KEY (UserID)
);

-- Insert sample data into User table
INSERT INTO User (FirstName, LastName, Email, Phone)
VALUES
    ('John', 'Doe', 'john@example.com', '111-111-2233'),
    ('Jane', 'Smith', 'jane@example.com', '111-111-2233'),
    ('Mike', 'Johnson', 'mike@example.com', '111-111-2233'),
    ('Sarah', 'Williams', 'sarah@example.com', '111-111-2233'),
    ('David', 'Lee', 'david@example.com', '111-111-2233'),
    ('Emily', 'Chen', 'emily@example.com', '111-111-2233');

-- Create the Account table
CREATE TABLE Account (
    AccountID INT NOT NULL AUTO_INCREMENT,
    AccountName VARCHAR(100) NOT NULL,
    AccountType VARCHAR(50) NOT NULL,
    UserID INT NOT NULL,
    PRIMARY KEY (AccountID),
    FOREIGN KEY (UserID) REFERENCES User(UserID)
);

-- Insert sample data into Account table
INSERT INTO Account (AccountName, AccountType, UserID)
VALUES
    ('Savings Account', 'Savings', 1),
    ('Checking Account', 'Checking', 2),
    ('Investment Account', 'Investment', 3),
    ('Retirement Account', 'Retirement', 4),
    ('Joint Account', 'Joint', 5),
    ('Business Account', 'Business', 6);

-- Create the Portfolio table
CREATE TABLE Portfolio (
    PortfolioID INT NOT NULL AUTO_INCREMENT,
    PortfolioName VARCHAR(100) NOT NULL,
    AccountID INT NOT NULL,
    PRIMARY KEY (PortfolioID),
    FOREIGN KEY (AccountID) REFERENCES Account(AccountID)
);

-- Insert sample data into Portfolio table
INSERT INTO Portfolio (PortfolioName, AccountID)
VALUES
    ('John\'s Portfolio', 1),
    ('Jane\'s Portfolio', 2),
    ('Mike\'s Portfolio', 3),
    ('Sarah\'s Portfolio', 4),
    ('David\'s Portfolio', 5),
    ('Emily\'s Portfolio', 6);

-- Create the Asset table
CREATE TABLE Asset (
    AssetID INT NOT NULL AUTO_INCREMENT,
    AssetName VARCHAR(100) NOT NULL,
    AssetType VARCHAR(50) NOT NULL,
    PRIMARY KEY (AssetID)
);

-- Insert sample data into Asset table
INSERT INTO Asset (AssetName, AssetType)
VALUES
    ('Dogecoin', 'Cryptocurrency'),
    ('Tesla Inc', 'Stock'),
    ('Apple Inc', 'Stock'),
    ('Bitcoin', 'Cryptocurrency'),
    ('Amazon.com Inc', 'Stock'),
    ('Cash', 'Currency'),
    ('Microsoft Corp', 'Stock');

-- Create the Portfolio_Asset table
CREATE TABLE Portfolio_Asset (
    PortfolioID INT NOT NULL,
    AssetID INT NOT NULL,
    PRIMARY KEY (PortfolioID, AssetID),
    FOREIGN KEY (PortfolioID) REFERENCES Portfolio(PortfolioID),
    FOREIGN KEY (AssetID) REFERENCES Asset(AssetID)
);

-- Insert sample data into Portfolio_Asset table
INSERT INTO Portfolio_Asset (PortfolioID, AssetID)
VALUES
    (1, 1),
    (1, 2),
    (2, 3),
    (3, 1),
    (4, 2),
    (5, 3),
    (6, 4),
    (1, 5),
    (2, 6),
    (3, 4),
    (4, 5),
    (5, 6);

-- Create the Transaction table
CREATE TABLE Transaction (
    TransactionID INT NOT NULL AUTO_INCREMENT,
    TransactionDate DATE NOT NULL,
    Amount DECIMAL(10, 2) NOT NULL,
    TransactionType VARCHAR(50) NOT NULL,
    Description VARCHAR(100) NOT NULL,
    PortfolioID INT NOT NULL,
    AssetID INT NOT NULL,
    PRIMARY KEY (TransactionID),
    FOREIGN KEY (PortfolioID) REFERENCES Portfolio(PortfolioID),
    FOREIGN KEY (AssetID) REFERENCES Asset(AssetID)
);

-- Insert sample data into Transaction table (continued)
INSERT INTO Transaction (TransactionDate, Amount, TransactionType, Description, PortfolioID, AssetID)
VALUES
	('2023-07-19', 50.00, 'buy', 'Buying Dogecoin', 1, 1),
    ('2023-07-20', 20.00, 'sell', 'Selling Dogecoin', 1, 1),
	('2023-07-15', 1000.00, 'deposit', 'Initial deposit', 1, 6),
    ('2023-07-16', 500.00, 'deposit', 'Additional deposit', 1, 6),
    ('2023-07-17', 300.00, 'deposit', 'Cash inflow', 2, 6),
    ('2023-07-18', 200.00, 'withdrawal', 'Cash outflow', 2, 6);

-- Verify the data in the Transaction table
SELECT * FROM Transaction;
