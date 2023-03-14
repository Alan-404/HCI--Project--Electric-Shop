CREATE TABLE USER_SYSTEM(
	ID VARCHAR(21) PRIMARY KEY,
	FIRST_NAME VARCHAR(11),
	LAST_NAME VARCHAR(11),
	EMAIL VARCHAR(51),
	PHONE VARCHAR(12),
	BIRTH_DATE DATE,
	GENDER VARCHAR(7),
	ADDRESS VARCHAR(51),
	CREATED_AT TIMESTAMP WITH TIME ZONE,
	MODIFIED_AT TIMESTAMP WITH TIME ZONE
)

CREATE TABLE ACCOUNT(
	ID VARCHAR(21) PRIMARY KEY,
	USER_ID VARCHAR(21) REFERENCES USER_SYSTEM(ID),
	PASSWORD TEXT,
	STATUS BOOL,
	ROLE VARCHAR(11),
	MODIFIED_AT TIMESTAMP WITH TIME ZONE
)

CREATE TABLE DISTRIBUTOR(
	ID VARCHAR(21) PRIMARY KEY,
	NAME VARCHAR(11),
	CREATED_AT TIMESTAMP WITH TIME ZONE,
	MODIFIED_AT TIMESTAMP WITH TIME ZONE
)

CREATE TABLE PRODUCT(
	ID VARCHAR(21) PRIMARY KEY,
	NAME VARCHAR(101),
	DESCRIPTION TEXT,
	PRICE FLOAT,
	STATUS VARCHAR(11),
	INFORMATION TEXT,
	CREATED_AT TIMESTAMP WITH TIME ZONE,
	MODIFIED_AT TIMESTAMP WITH TIME ZONE
)


CREATE TABLE PRODUCT_DISTRIBUTOR(
	ID SERIAL PRIMARY KEY,
	PRODUCT_ID VARCHAR(21) REFERENCES PRODUCT(ID),
	DISTRIBUTOR_ID VARCHAR(21) REFERENCES DISTRIBUTOR(ID)
)

CREATE TABLE CATEGORY(
	ID VARCHAR(21) PRIMARY KEY,
	NAME VARCHAR(11),
	STATUS BOOL,
	CREATED_AT TIMESTAMP WITH TIME ZONE,
	MODIFIED_AT TIMESTAMP WITH TIME ZONE
)


CREATE TABLE PRODUCT_CATEGORY(
	ID SERIAL PRIMARY KEY,
	PRODUCT_ID VARCHAR(21) REFERENCES PRODUCT(ID),
	CATEGORY_ID VARCHAR(21) REFERENCES CATEGORY(ID)
)

CREATE TABLE DISCOUNT(
	ID SERIAL PRIMARY KEY,
	PRODUCT_ID VARCHAR(21) REFERENCES PRODUCT(ID),
	VALUE FLOAT,
	MODIFIED_AT TIMESTAMP WITH TIME ZONE
)

CREATE TABLE WAREHOUSE(
	ID SERIAL PRIMARY KEY,
	PRODUCT_ID VARCHAR(21) REFERENCES PRODUCT(ID),
	QUANTITY INT
)

CREATE TABLE WAREHOUSE_HISTORY(
	ID SERIAL PRIMARY KEY,
	WAREHOUSE_ID INT REFERENCES WAREHOUSE(ID),
	QUANTITY INT,
	TYPE VARCHAR(8),
	CREATED_AT TIMESTAMP WITH TIME ZONE
)

CREATE TABLE CART(
	ID VARCHAR()
)

DROP TABLE WAREHOUSE_HISTORY


SELECT * FROM ACCOUNT
SELECT * FROM USER_SYSTEM
SELECT * FROM PRODUCT
SELECT * FROM DISCOUNT
SELECT * FROM WAREHOUSE
SELECT * FROM WAREHOUSE_HISTORY
SELECT * FROM CATEGORY

DELETE FROM ACCOUNT
DELETE FROM USER_SYSTEM

DROP TABLE ACCOUNT
DROP TABLE USER_SYSTEM

ALTER TABLE CATEGORY ALTER COLUMN NAME TYPE VARCHAR(31)