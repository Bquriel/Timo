CREATE TABLE "Storage"(
	"storageID" 	INTEGER 	PRIMARY KEY AUTOINCREMENT
);

CREATE TABLE "SmartPost"(
	"postID"	 	INTEGER 	PRIMARY KEY AUTOINCREMENT,
	"name" 			VARCHAR(50) NOT NULL,
	"openData"	 	VARCHAR(50) NOT NULL,
	"storageID" 	INTEGER 	NOT NULL,
	
	FOREIGN KEY( "storageID" ) REFERENCES "Storage"( "storageID" )
);

CREATE TABLE "TIMOTEI-guy"(
	"workerID"	 	INTEGER	 	PRIMARY KEY AUTOINCREMENT,
	"postID"	 	INTEGER	 	NOT NULL,
	"name"		 	VARCHAR(25) NOT NULL,
	"stressLevel" 	INTEGER	 	NOT NULL,
	
	FOREIGN KEY( "postID" ) REFERENCES "SmartPost"( "postID" )
);

CREATE TABLE "GeoPoint"(
	"postID"	 	INTEGER 	PRIMARY KEY AUTOINCREMENT,
	"longitude"	 	FLOAT8	 	NOT NULL,
	"latitude"	 	FLOAT8	 	NOT NULL,
	
	FOREIGN KEY( "postID" ) REFERENCES "SmartPost"( "postID" )	
);

CREATE TABLE "Adress"(
	"postID"	 	INTEGER	 	PRIMARY KEY AUTOINCREMENT,
	"city"		 	VARCHAR(15) NOT NULL,
	"street"	 	VARCHAR(20) NOT NULL,
	
	FOREIGN KEY( "postID" ) REFERENCES "SmartPost"( "postID" )
);

CREATE TABLE "PostNumber"(
	"postID"  		INTEGER 	PRIMARY KEY AUTOINCREMENT,
	"areaCode"	    VARCHAR(6) 	NOT NULL,
	
	FOREIGN KEY( "postID" ) REFERENCES "Adress"( "postID" )
);

CREATE TABLE "Item"(
	"itemID"	  	INTEGER	  	PRIMARY KEY,
	"name"		  	VARCHAR(25) NOT NULL,
	"description"  	VARCHAR(120),
	"weight" 		FLOAT 		NOT NULL,
	"breakable"	 	INTEGER 	NOT NULL,
	"storageID" 	INTEGER	  	NOT NULL,
	
	FOREIGN KEY( "storageID" ) REFERENCES "Storage"( "storageID" )
);

CREATE TABLE "SizeData"(
	"sizeID"	 	INTEGER  	PRIMARY KEY,
	"sizeX"		 	FLOAT 	 	NOT NULL,
	"sizeY"		 	FLOAT	 	NOT NULL,
	"sizeZ"		 	FLOAT	 	NOT NULL,
	"itemID"	 	INTEGER	  	NOT NULL,
	
	FOREIGN KEY( "itemID" ) REFERENCES "Item"( "itemID" )
);

CREATE TABLE "Package"(
	"packageID"	 	INTEGER	 	PRIMARY KEY AUTOINCREMENT,
	"storageID"	 	INTEGER 	NOT NULL,
	
	FOREIGN KEY( "storageID" ) REFERENCES "Storage"( "storageID" )
);

CREATE TABLE "MaxData"(
	"maxSizeID"	 	INTEGER  	PRIMARY KEY AUTOINCREMENT,
	"sizeX"		 	FLOAT,
	"sizeY"		 	FLOAT,
	"sizeZ"		 	FLOAT,
	"weightLimit" 	FLOAT,
	"distanceLimit" INTEGER,
	"packageID"	 	INT		 	NOT NULL,
	
	FOREIGN KEY( "packageID" ) REFERENCES "Package"( "packageID" )
);

CREATE TABLE "History"(
	"historyID"  	INTEGER 	PRIMARY KEY,
	"storageID" 	INTEGER  	NOT NULL,
	"timeStamp" 	VARCHAR(35) NOT NULL,
	
	FOREIGN KEY( "storageID" ) REFERENCES "Storage"( "storageID" )
);

CREATE TABLE "ItemEvent"(
	"historyID" 	INTEGER 	PRIMARY KEY,
	"itemName"  	VARCHAR(20) NOT NULL,
	"wasCreated"  	INTEGER 	NOT NULL,
	
	FOREIGN KEY( "historyID" ) REFERENCES "History"( "historyID" )
);

CREATE TABLE "SendEvent"(
	"historyID"  	INTEGER 	PRIMARY KEY,
	"sentFromName" 	VARCHAR(10) NOT NULL,
	"sentFromSurname" VARCHAR(15) NOT NULL,
	"sentToName"  	VARCHAR(10) NOT NULL,
	"sentToSurname" VARCHAR(10) NOT NULL,
	"sentLocation" 	VARCHAR(50) NOT NULL,
	"receivedLocation" VARCHAR(50) NOT NULL,
	"sentItemName"  VARCHAR(20) NOT NULL,
	"routeLength" 	INTEGER 	NOT NULL,
	"packageInfo" 	VARCHAR(35) NOT NULL,
	"broke" 		INTEGER 	NOT NULL,
	
	FOREIGN KEY( "historyID" ) REFERENCES "History"( "historyID" )
);

CREATE VIEW "PackageInfo" AS SELECT
"Package"."packageID", "sizeX", "sizeY", "sizeZ", "weightLimit", "distanceLimit"
FROM "Package" INNER JOIN "MaxData" ON "Package"."packageID" = "MaxData"."packageID";

CREATE VIEW "PostData" AS SELECT
"name", "openData", "longitude", "latitude", "city", "street", "areaCode"
FROM "PostNumber" INNER JOIN 
"Adress" ON "PostNumber"."postID" = "Adress"."postID" INNER JOIN 
"SmartPost" ON "Adress"."postID" = "SmartPost"."postID" INNER JOIN
"GeoPoint" ON "SmartPost"."postID" = "GeoPoint"."postID";

CREATE VIEW "SendData" AS SELECT 
"History"."historyID", "timeStamp", "sentFromName", "sentFromSurname", "sentToName", "sentToSurname",
"sentLocation", "receivedLocation", "sentItemName", "routeLength", "packageInfo", "broke"
FROM "History" INNER JOIN 
"SendEvent" ON "History"."historyID" = "SendEvent"."historyID";

CREATE VIEW "ItemData" AS SELECT
"History"."historyID", "timeStamp", "itemName", "wasCreated"
FROM "History" INNER JOIN
"ItemEvent" ON "History"."historyID" = "ItemEvent"."historyID";