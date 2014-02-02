package edu.carleton.COMP4601.assignment1;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.mongodb.DBObject;

import edu.carleton.COMP4601.assignment1.Document;
import edu.carleton.COMP4601.assignment1.persistence.DocumentsManager;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentCollection {
	@XmlElement(name="documents")
	private List<Document> documents;
	private DocumentsManager manager;
	
	public DocumentCollection(){
		DocumentsManager manager = DocumentsManager.getDefault();
		setDocuments(DocumentsManager.convertDBObject(manager.findAll("id")));
	}
	
	public List<Document> search(String tag){
		List<Document> results = new ArrayList<Document>();
		List<DBObject> resultsObj = DocumentsManager.getDefault().search("text", tag, false);
		
		if (resultsObj != null && resultsObj.size() > 0){
			results = DocumentsManager.convertDBObject(resultsObj);
		}
		else{
			System.out.println("no results returned");
		}
		return results;
		
	}
	
	public int size() {
		return documents.size();
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}
}