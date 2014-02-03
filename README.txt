COMP 4601
Mobile Applications - Assignment #1

Due Feb 02, 2014 at 11:55 pm

Last updated 01/30/2014 00:29:28

Students:
========
Menan Vadivelpillai 100770296
Abdulrahman Alamoudi 100806966

Objective:
=========
The objective of this assignment is to have the student develop a searchable document archive (SDA) using RESTful web services using MongoDB to store the documents. Submission

Notes:
======
1- In order for tags and links to be created, the following points has to be considered:
	- Use the ':' character to seperate each tag.
	- Use the SPACE character to seperate each link.

2- To test XML representation, you can use the following commands in the Terminal:
	- For "GET" requests:
		curl -H "Accept: application/xml" -X GET "http://localhost:8080/COMP4601SDA/rest/sda/"
	- For "POST" requests:
		curl -H "Accept: text/html" -d 'id=1000&name=Doc 1&text=Hello World&links=scs.carleton.ca&tags=COMP 4106:Assignment 1:REST Application' -X POST "http://localhost:8080/COMP4601SDA/rest/sda/"

3- To update a document use PUT request. For example (XXXX = id):
	curl -H "Accept: application/xml" -X PUT "http://localhost:8080/COMP4601SDA/rest/sda/XXXX"

4- All necessary libaries are stored inside of directory : \lib