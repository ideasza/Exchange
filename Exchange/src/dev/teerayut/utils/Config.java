package dev.teerayut.utils;

import java.io.File;

public class Config {

	public static String DB_FILE = "currency.sqlite";
	public static String DB_PATH = "C:\\Exchange\\db\\";
	public static String REPORT_PATH = "C:\\Exchange\\report\\";
	public static String CONFIG_PATH = "C:\\Exchange\\config\\";
	
	/**
	 * CREATE DATABASE SCRIPT
	 * 
	 * 
	 * CREATE TABLE CURRENCY
		(
		   CR_ID				INTEGER		PRIMARY KEY		AUTOINCREMENT,
		   CR_NAME				TEXT		NOT NULL,
		   CR_DESC				TEXT,
		   CR_BUY_RATE			REAL,
		   CR_SELL_RATE			REAL,
		   CR_STATUS			TEXT,
		   CR_CREATED_DATE		TEXT,
		   CR_UPDATED_DATE		TEXT
		);
	 * 
	 * 
	 * 
	 * CREATE TABLE RECEIPT
		(
			RC_ID				INTEGER		PRIMARY KEY		AUTOINCREMENT,
			CR_ID				INTEGER		NOT NULL,
			RC_DATE				TEXT,
			RC_RATE				REAL,
			RC_AMOUNT			REAL,
			RC_TOTAL			REAL,
			RC_TYPE				TEXT,
			RC_CREATED_DATE		TEXT,
			RC_UPDATED_DATE		TEXT,
			FOREIGN KEY (CR_ID) REFERENCES CURRENCY(CR_ID)
		);
	 * 
	 * 
	 * CREATE TABLE SETTINGS
	 * (
	 * 		ST_ID				INTEGER		PRIMARY KEY		AUTOINCREMENT,
	 * 		ST_KEY				TEXT,
	 * 		ST_VALUE			TEXT,
	 * 		ST_CREATED_DATE		TEXT,
	 * 		ST_UPDATED_DATE		TEXT
	 * );
	 * 
	 * 
	 * 
	 * 
	 */
}
