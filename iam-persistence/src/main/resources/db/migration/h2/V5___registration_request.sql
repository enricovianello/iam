CREATE TABLE iam_reg_request (ID BIGINT IDENTITY NOT NULL, UUID VARCHAR(36) NOT NULL UNIQUE, CREATIONTIME TIMESTAMP NOT NULL, ACCOUNT_ID BIGINT, STATUS VARCHAR(50), LASTUPDATETIME TIMESTAMP, PRIMARY KEY (ID));
ALTER TABLE iam_reg_request ADD CONSTRAINT FK_iam_reg_request_account_id FOREIGN KEY (ACCOUNT_ID) REFERENCES iam_account (ID);
ALTER TABLE iam_account ADD COLUMN (confirmation_key VARCHAR(36), reset_key VARCHAR(36));

