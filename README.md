
COMP 4601

Mobile Applications

Assignment #1

Due Feb 02, 2014 at 11:55 pm

Last updated 01/30/2014 00:29:28

Objective

The objective of this assignment is to have the student develop a searchable document archive (SDA) using RESTful web services using MongoDB to store the documents.
Submission


Notes:
======

1- In order for tags and links to be created, the following points has to be considered:
	> Use the SPACE character to seperate each tag/link.
	> It's assumed that a tag/link is one word.

2- To test XML representation, you can use the following commands in the Terminal:
	> For "GET" requests
	curl -G "format=APPLICATION_XML" http://localhost:8080/COMP4601Assignment1-100770296/rest/sda/
	> For "POST" requests
	curl -d "id=1000&name=somename 1&tags=go home&links=google.com&format=APPdICATION_XML" http://localhost:8080/COMP4601Assignment1-100770296/rest/sda/
