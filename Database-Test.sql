 DROP DATABASE IF EXISTS InvestmentManagementDBTest;

-- Create the database
CREATE DATABASE InvestmentManagementDBTest;

-- Use the database
USE InvestmentManagementDBTest;

--  select * from Transaction;
--  select * from Portfolio_Asset;
--  select * from Asset;
--  select * from Portfolio;
--  select * from Account;
--  select * from User;

-- Create the User table
CREATE TABLE User (
    UserID INT NOT NULL AUTO_INCREMENT,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    Email VARCHAR(100) NOT NULL,
    Phone VARCHAR(12) NOT NULL,
    PRIMARY KEY (UserID)
);


-- Create the Account table
CREATE TABLE Account (
    AccountID INT NOT NULL AUTO_INCREMENT,
    AccountName VARCHAR(100) NOT NULL,
    AccountType VARCHAR(50) NOT NULL,
    UserID INT NOT NULL,
    PRIMARY KEY (AccountID),
    FOREIGN KEY (UserID) REFERENCES User(UserID)
);

-- Create the Portfolio table
CREATE TABLE Portfolio (
    PortfolioID INT NOT NULL AUTO_INCREMENT,
    PortfolioName VARCHAR(100) NOT NULL,
    AccountID INT NOT NULL,
    PRIMARY KEY (PortfolioID),
    FOREIGN KEY (AccountID) REFERENCES Account(AccountID)
);

-- Create the Asset table
CREATE TABLE Asset (
    AssetID INT NOT NULL AUTO_INCREMENT,
    AssetName VARCHAR(100) NOT NULL,
    AssetType VARCHAR(50) NOT NULL,
    PRIMARY KEY (AssetID)
);

-- Create the Portfolio_Asset table
CREATE TABLE Portfolio_Asset (
    PortfolioID INT NOT NULL,
    AssetID INT NOT NULL,
    Quantity DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (PortfolioID, AssetID),
    FOREIGN KEY (PortfolioID) REFERENCES Portfolio(PortfolioID),
    FOREIGN KEY (AssetID) REFERENCES Asset(AssetID)
);

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

-- Verify the data in the Transaction table
SELECT * FROM Transaction;
