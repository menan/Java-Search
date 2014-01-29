package edu.carleton.COMP4601.assignment1.Main;

import java.io.FileNotFoundException;
import java.net.UnknownHostException;

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
	public Document getDocumentHTML() throws UnknownHostException {
		Document a = Documents.getInstance().find(new Integer(id));
		if (a == null) {
			throw new RuntimeException("No such Document: " + id);
		}
		return a;
	}

	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	public Response putDocument(JAXBElement<Document> doc) throws NumberFormatException, FileNotFoundException, UnknownHostException, JAXBException {
		Document c = doc.getValue();
		System.out.println("Document:" + c.getName());
		return putAndGetResponse(c);
	}

	@DELETE
	public void deleteDocument() throws NumberFormatException, FileNotFoundException, JAXBException, UnknownHostException {
		if (!Documents.getInstance().close(new Integer(id)))
			throw new RuntimeException("Document " + id + " not found");
	}

	private Response putAndGetResponse(Document document) throws NumberFormatException, FileNotFoundException, JAXBException, UnknownHostException {
		Response res = null;

//		if (document.getBalance() < 0){
//			//res = Response.noContent().build();
//			System.out.println("balalnce must have been below 0: " + document.getBalance());
//			res = Response.notModified("Balance is below 0").build();
//		}
//		else{
			Documents.getInstance().save(document);
			res = Response.created(uriInfo.getAbsolutePath()).build();
			//DocumentsJAXB.getInstance().getModel().put(Document.getId(), Document);
//		}
		return res;
	}
}
