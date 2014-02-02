package edu.carleton.COMP4601.assignment1.Main;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.mongodb.DBObject;

import edu.carleton.COMP4601.assignment1.*;
import edu.carleton.COMP4601.assignment1.persistence.DocumentsManager;

@Path("/sda")
public class SDA {

	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	private String name;
	private DocumentCollection collection;

	public SDA() {
		name = "COMP4601 Searchable Document Archive";
		collection = new DocumentCollection();
	}

	@GET
	public String printName() {
		return name;
	}

	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String sayXML() {
		return "<?xml version=\"1.0\"?>" + "<sda> " + name + " </sda>";
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayHtml() {
		return "<html> " + "<title>" + name + "</title>" + "<body><h1>" + name
				+ "</body></h1>" + "</html> ";
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String sayJSON() {
		return "{" + name + "}";
	}

	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount() throws UnknownHostException {
		int count = collection.size();
		return String.valueOf(count);
	}

	@GET
	@Path("documents")
	@Produces(MediaType.APPLICATION_XML)
	public List<Document> getDocumentsXML() throws UnknownHostException {
		return collection.getDocuments();
	}
	
	@GET
	@Path("documents")
	@Produces(MediaType.TEXT_HTML)
	public String getDocumentsHTML() throws UnknownHostException {
		List<Document> resultsDoc = collection.getDocuments();
		if (resultsDoc.size() == 0){
			return "No Documents found :( <br /><a href=\"../../create_document.html\">Click Here</a> to add a document";
		}
		String returnStr = "<h2>Documents</h2>There are " + resultsDoc.size() + " documents<br /><br /><table>";
		
		for(Document d: resultsDoc){
			returnStr += d.toHTML();
		}
		returnStr += "</table><br /><br /><a href=\"../../create_document.html\">New Document</a>";
		
		return returnStr;
	}

	@GET
	@Path("search/{tags}")
	@Produces(MediaType.APPLICATION_XML)
	public List<Document> searchDocuments(@PathParam("tags") String tags) throws UnknownHostException {
		List<Document> resultsDoc = new ArrayList<Document>();
		resultsDoc = collection.search(tags);
		return resultsDoc;
	}
	
	@GET
	@Path("search/{tags}")
	@Produces(MediaType.TEXT_HTML)
	public String searchDocumentsHTML(@PathParam("tags") String tags) throws UnknownHostException {
		List<Document> resultsDoc = collection.search(tags);
		String returnStr = "Your search returned " + resultsDoc.size() + " results<table>";
		
		for(Document d: resultsDoc){
			returnStr += d.toHTML();
		}
		returnStr += "</table>";
		
		return returnStr;
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String newDocumentHTML(@FormParam("id") String id,
			@FormParam("name") String name,
			@FormParam("tags") String tags,
			@FormParam("links") String links,
			@FormParam("text") String text,
			@Context HttpServletResponse servletResponse) throws IOException {

		if (name == null || name.isEmpty() || id == null || id.isEmpty() || tags == null || tags.isEmpty() || links == null || links.isEmpty()){
			servletResponse.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return "You have to fill all the fields. Please go <a href=\"javascript:history.back()\">back</a> and fill it.";
		}
		else{

			int newId = new Integer(id).intValue();
			Document doc = DocumentsManager.getDefault().load(newId);
			if (doc == null){
				boolean put = DocumentsManager.getDefault().create(newId,name,tags,links,text);
				if (put){
					servletResponse.setStatus(HttpServletResponse.SC_OK);
					return "Created successfully. <a href=\"/COMP4601Assignment1-100770296/rest/sda/documents\">Click Here</a> to view all documents.";
				}
				else{
					servletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
					return "Error creating document.";
				}
			}
			else{

				servletResponse.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				return "Sorry, but there is already a record exists with id " + newId + ". Please go <a href=\"javascript:history.back()\">back</a> and change it.";
			}
		}
			

	}


	@POST
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Document newDocumentXML(@FormParam("id") String id,
			@FormParam("name") String name,
			@FormParam("tags") String tags,
			@FormParam("links") String links,
			@FormParam("text") String text,
			@Context HttpServletResponse servletResponse) throws IOException {

		if (name == null || name.isEmpty() || id == null || id.isEmpty() || tags == null || tags.isEmpty() || links == null || links.isEmpty()){
			servletResponse.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return null;
		}
		else{

			int newId = new Integer(id).intValue();
			Document doc = DocumentsManager.getDefault().load(newId);
			if (doc == null){
				boolean put = DocumentsManager.getDefault().create(newId,name,tags,links,text);
				if (put){
					servletResponse.setStatus(HttpServletResponse.SC_OK);
					return DocumentsManager.getDefault().load(newId);
				}
				else{
					servletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
					return null;
				}
			}
			else{
				servletResponse.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				return null;
			}
		}
		
	}

	@Path("{doc}")
	public Action getDocument(@PathParam("doc") String id) {
		return new Action(uriInfo, request, id);
	}

}
