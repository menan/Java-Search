COMP 4601

Mobile Applications

Assignment #1

Due Feb 02, 2014 at 11:55 pm

Last updated 01/30/2014 00:29:28

Objective

The objective of this assignment is to have the student develop a searchable document archive (SDA) using RESTful web services using MongoDB to store the documents.
Submission

A README.TXT file must be provided that documents how your assignment is to be tested. It is your responsibility to ensure that the instructions are accurate, tested, and sufficient to allow the TA to test your submission.

Submission is through cuLearn. Package your Eclipse project submission in a Zip file. NOTE: Ensure that your upload is successful. A 10% penalty will be incurred for submissions which do not contain all files, or are empty. No late assignments are accepted.

Read the guidelines for software design, implementation and assignment submission.

Assignment Details

The following naming conventions must be observed:
The server project name within Eclipse must be COMP4601Assignment1-Z. Here Z is your student number; e.g., 100118019.
The package edu.carleton.COMP4601.assignment1 must be used.
The main Jersey class implementing your basic RESTful web service must be edu.carleton.COMP4601.assignment1.Main.
The name of your web service must be COMP4601SDA. The path to the root web service should be /sda.
You are to develop an application that acts as a searchable document store. There are several requirements:

You are to develop a client and a server.
The client may be built using: Chrome (version 32 and above), iOS (version 7) or Android (version 4.1 and above).
The server must be built using Tomcat (version 7) and Jersey (version 1.17).
The server must use MongoDB (version 2.4) and use the Java driver (version 2.11.3).
When running, directing a browser to http://localhost:8080/COMP4601SDA/rest/sda should display the text "COMP4601 Searchable Document Archive" in a browser window.
The SDA stores documents which are both tagged and linked together. Document and DocumentCollection classes are provided. So, a document contains:
name: This is a user-friendly display string for the document.
id: This is a unique number for a document.
text: This is a string that contains the content of the document. It is not structured in any way (for now).
tags: This is an array of strings representing the searchable content for the document. A document must be tagged with at least 1 keyword/phrase.
links: This is an array of URLs representing links to other documents. You may assume that these links are relative to the URL of the web service itself. A document may have no links.
In the text below, the web service is indicated as: sda/X, where X is the web service. You must provide the following RESTful web services:
Create a document and insert it into the SDA. The SDA should indicate success using an HTTP response code of 200 and 204 otherwise. sda using POST.
Update an existing document with new tags or new links. The SDA should indicate success using an HTTP response code of 200. sda/{id} using PUT or POST.
View a document. sda/{id} using GET. You must support XML and HTML representations.
Delete a document, sda/{id} using DELETE. If the document exists, an HTTP response code of 200 is returned, otherwise 204.
Delete an existing set of document(s) with specific tags. If one or more documents is deleted, an HTTP response code of 200 should be provided, otherwise 204. sda/delete/{tags} using GET.
Search for documents with specific tags. sda/search/{tags}
Tags must conform to the format: tag1:tag2:tag3: ... meaning: retrieve documents with tag1 AND tag2 AND tag3 AND ...
The documents found should be returned as a list. You must support XML and HTML representations.
Clicking on a document displays its contents. You must support XML and HTML representations.
If no documents are found, "No documents found." should be displayed. This will be returned in either plain text or HTML representations.
When the results of a search are displayed, it should be possible to navigate to the linked documents.
View the library: the names of all documents should be displayed. sda/documents using GET. You must support XML and HTML representations.
REST is unlike an RPC, so it is vital that you handle errors appropriately in your RESTful web service. Both XML and HTML representations are expected:
No document(s) found.
Link not found.
Arguments not provided to web service; e.g., tags.


Copyright © 2014 The School of Computer Science
