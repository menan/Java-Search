package edu.carleton.COMP4601.assignment1.Main;

import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;



import edu.carleton.COMP4601.assignment1.*;
import edu.carleton.COMP4601.assignment1.persistence.DocumentsManager;

public class Action {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	String id;

	public Action(UriInfo uriInfo, Request request, String id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
	}

	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Document getDocumentXML() throws UnknownHostException {
		Document a = DocumentsManager.getDefault().load(new Integer(id));
		if (a == null) {
			throw new RuntimeException("No such Document: " + id);
		}
		else{
			System.out.println("XML - Getting document with id:" + a.getId() + " name:" + a.getName());
		}
		return a;
	}
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getDocumentHTML() throws UnknownHostException {
		Document a = DocumentsManager.getDefault().load(new Integer(id));
		if (a == null) {
			throw new RuntimeException("No such Document: " + id);
		}
		else{
			System.out.println("HTML - Getting document with id:" + a.getId() + " name:" + a.getName());
		}
		
		StringBuilder builder = new StringBuilder();
		// Name
		builder.append("Name: <b>" + a.getName() + "</b>");
		// ID
		builder.append("<br />ID:<b>" + a.getId() + "</b>");
		// Text
		builder.append("<br />Text: <b>" + a.getText() + "</b>");
		// Tags
		if(a.getTags() != null)
			builder.append("<br />Tags:<b>" + a.getTags().toString() + "</b>");
		// Links
		if(a.getTags() != null)
			builder.append("<br />Links:<b>" + a.getLinks().toString());
		
		
		return builder.toString();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	public Response putDocument(JAXBElement<Document> doc) throws NumberFormatException, FileNotFoundException, UnknownHostException, JAXBException {
		Document c = doc.getValue();
		System.out.println("Document:" + c.getName());
		return putAndGetResponse(c);
	}

	@DELETE
	public Response deleteDocument() {
		if (DocumentsManager.getDefault().delete("id",new Integer(id))){
			return Response.status(HttpServletResponse.SC_OK).build();
		}else{
			System.out.println("Action - deleteDocument(): something went wrong deletion didn't go through");
			return Response.status(HttpServletResponse.SC_NO_CONTENT).build();
		}
	}

	private Response putAndGetResponse(Document document) throws NumberFormatException, FileNotFoundException, JAXBException, UnknownHostException {
		Response res = null;

//		if (document.getBalance() < 0){
//			//res = Response.noContent().build();
//			System.out.println("balalnce must have been below 0: " + document.getBalance());
//			res = Response.notModified("Balance is below 0").build();
//		}
//		else{
			DocumentsManager.getDefault().save(document);
			res = Response.created(uriInfo.getAbsolutePath()).build();
			//DocumentsJAXB.getInstance().getModel().put(Document.getId(), Document);
//		}
		return res;
	}
}
