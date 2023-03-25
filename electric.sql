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
	USER_ID VARCHAR(21) REFERENCES USER_SYSTEM(ID),
	NAME VARCHAR(11),
	PHONE VARCHAR(13),
	CREATED_AT TIMESTAMP WITH TIME ZONE,
	MODIFIED_AT TIMESTAMP WITH TIME ZONE
)

CREATE TABLE PRODUCT(
	ID VARCHAR(21) PRIMARY KEY,
	NAME VARCHAR(101),
	DESCRIPTION TEXT,
	INFORMATION TEXT,
	DISTRIBUTOR_ID VARCHAR(21) REFERENCES DISTRIBUTOR(ID),
	CREATED_AT TIMESTAMP WITH TIME ZONE,
	MODIFIED_AT TIMESTAMP WITH TIME ZONE
)


CREATE TABLE PRODUCT_DETAIL(
	ID VARCHAR(21) PRIMARY KEY,
	PRODUCT_ID VARCHAR(21) REFERENCES PRODUCT(ID),
	COLOR INT,
	SPECIFICATIONS TEXT,
	PRICE REAL,
	STATUS BOOL,
	MODIFIED_AT TIMESTAMP WITH TIME ZONE
)

CREATE TABLE PRODUCT_IMAGE(
	ID SERIAL PRIMARY KEY,
	PRODUCT_ID VARCHAR(21) REFERENCES PRODUCT_DETAIL(ID),
	LINK TEXT,
	MAIN BOOLEAN
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
	PRODUCT_ID VARCHAR(21) REFERENCES PRODUCT_DETAIL(ID),
	VALUE REAL,
	MODIFIED_AT TIMESTAMP WITH TIME ZONE
)


CREATE TABLE WAREHOUSE(
	ID SERIAL PRIMARY KEY,
	PRODUCT_ID VARCHAR(21) REFERENCES PRODUCT_DETAIL(ID),
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
	ID VARCHAR(21) PRIMARY KEY,
	USER_ID VARCHAR(21) REFERENCES USER_SYSTEM(ID),
	PRODUCT_ID VARCHAR(21) REFERENCES PRODUCT_DETAIL(ID),
	QUANTITY INT,
	STATUS BOOLEAN,
	CREATED_AT TIMESTAMP WITH TIME ZONE,
	MODIFIED_AT TIMESTAMP WITH TIME ZONE
)




CREATE TABLE BILL(
	ID VARCHAR(21) PRIMARY KEY,
	USER_ID VARCHAR(21) REFERENCES USER_SYSTEM(ID),
	ORDER_TIME TIMESTAMP WITH TIME ZONE,
	PRICE REAL,
	PAYMENT_TYPE VARCHAR(21),
	STATUS VARCHAR(11)
)


CREATE TABLE USER_ORDER(
	ID SERIAL PRIMARY KEY,
	BILL_ID VARCHAR(21) REFERENCES BILL(ID),
	PRODUCT_ID VARCHAR(21) REFERENCES PRODUCT_DETAIL(ID),
	PRODUCT_PRICE REAL,
	QUANTITY INT,
	REVIEWED BOOLEAN
)



CREATE TABLE PRODUCT_REVIEW(
	ID VARCHAR(21) PRIMARY KEY,
	USER_ID VARCHAR(21) REFERENCES USER_SYSTEM(ID),
	PRODUCT_ID VARCHAR(21) REFERENCES PRODUCT(ID),
	CONTENT TEXT,
	STARS INT,
	CREATED_AT TIMESTAMP WITH TIME ZONE
)

CREATE TABLE BANNER(
	ID SERIAL PRIMARY KEY,
	IMAGE TEXT,
	LINK TEXT,
	STATUS BOOLEAN
)

CREATE TABLE COMMENT(
	ID VARCHAR(21) PRIMARY KEY,
	USER_ID VARCHAR(21) REFERENCES USER_SYSTEM(ID),
	PRODUCT_ID VARCHAR(21) REFERENCES PRODUCT(ID),
	CONTENT TEXT,
	REPLY VARCHAR(21) REFERENCES COMMENT(ID),
	CREATED_AT TIMESTAMP WITH TIME ZONE,
	MODIFIED_AT TIMESTAMP WITH TIME ZONE
)

DROP TABLE COMMENT

CREATE TABLE COMMENT_REACTION(
	ID SERIAL PRIMARY KEY,
	COMMENT_ID VARCHAR(21) REFERENCES COMMENT(ID),
	REACTION VARCHAR(11),
	CREATED_AT TIMESTAMP WITH TIME ZONE,
	MODIFIED_AT TIMESTAMP WITH TIME ZONE
)

drop table product_review

DROP TABLE COMMENT

drop table product_review


SELECT * FROM ACCOUNT
SELECT * FROM USER_SYSTEM
SELECT * FROM PRODUCT ORDER BY ID
SELECT * FROM PRODUCT_DETAIL
SELECT * FROM USER_ORDER
SELECT * FROM DISCOUNT
SELECT * FROM WAREHOUSE
SELECT * FROM WAREHOUSE_HISTORY
SELECT * FROM CATEGORY
SELECT * FROM BILL
SELECT * FROM PRODUCT_REVIEW
SELECT * FROM COMMENT
SELECT * FROM CART ORDER BY ID LIMIT 5 OFFSET 3

SELECT * FROM COMMENT WHERE REPLY IS NOT NULL

DELETE FROM ACCOUNT
DELETE FROM USER_SYSTEM
DROP TABLE BILL
DROP TABLE ACCOUNT
DROP TABLE USER_SYSTEM

drop table product_review

DELETE FROM PRODUCT_DETAIL
DELETE FROM DISCOUNT
DELETE FROM WAREHOUSE

SELECT * FROM CATEGORY ORDER BY NAME LIMIT 5 OFFSET 0

ALTER TABLE BILL ALTER COLUMN STATUS TYPE VARCHAR(21)