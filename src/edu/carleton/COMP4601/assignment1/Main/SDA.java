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
	@Produces(MediaType.TEXT_XML)
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
	public List<Document> getDocuments() throws UnknownHostException {
		return collection.getDocuments();
	}

	@GET
	@Path("search/{tags}")
	@Produces(MediaType.APPLICATION_XML)
	public List<Document> searchDocuments(@PathParam("tags") String tags) throws UnknownHostException {
		List<Document> resultsDoc = new ArrayList<Document>();
		resultsDoc = collection.search(tags);
		return resultsDoc;
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newDocument(@FormParam("id") String id,
			@FormParam("name") String name,
			@FormParam("tags") String tags,
			@FormParam("links") String links,
			@FormParam("text") String text,
			@Context HttpServletResponse servletResponse) throws IOException {

//		String newDescription = description;
//		if (newDescription == null)
//			newDescription = "";

		int newId = new Integer(id).intValue();
		Documents.getInstance().create(newId,name,tags,links,text);

		servletResponse.sendRedirect("../create_document.html");
	}

	@Path("{doc}")
	public Action getDocument(@PathParam("doc") String id) {
		return new Action(uriInfo, request, id);
	}

}
