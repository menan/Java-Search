package edu.carleton.COMP4601.assignment1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import edu.carleton.COMP4601.assignment1.Document;
import edu.carleton.COMP4601.assignment1.persistence.DocumentsManager;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentCollection {
	@XmlElement(name="documents")
	private List<Document> documents;
	
	public DocumentCollection(){
		DocumentsManager manager = DocumentsManager.getDefault();
		setDocuments(DocumentsManager.convertDBObject(manager.findAll("id")));
	}
	
	public List<Document> search(String tags_string){
		List<String> 	tags = new ArrayList<String>(Arrays.asList(tags_string.split(":")));
		List<DBObject> 	resultsObj = DocumentsManager.getDefault().search("tags", tags);

		List<Document> 	results = new ArrayList<Document>();
		if (resultsObj != null && resultsObj.size() > 0){
			results = DocumentsManager.convertDBObject(resultsObj);
			for(Document d : results)
				System.out.println(d.toString());
		}
		else{
			System.out.println("no results returned");
		}
		return results;
		
	}

	public Document create(int id, String name, String tags, String links, String text) {
		Document a = new Document(id);
		a.setName(name);
		a.setText(tags);
		System.out.println("Creating document with name:" + name);
		try {
			DocumentsManager.getDefault().create(id,name,tags,links,text);
			documents.add(a);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	public boolean update(int id, String name, String tags, String links, String text) {
		BasicDBObject searchQuery = new BasicDBObject().append("id", id);
		boolean result = true;
		if(name!=null)
			result = result && DocumentsManager.getDefault().update("name", name, searchQuery);
		if(tags!=null)
			result = result && DocumentsManager.getDefault().update("tags", new ArrayList<String>(Arrays.asList(tags.split(":"))), searchQuery);
		if(links!=null)
			result = result && DocumentsManager.getDefault().update("links", new ArrayList<String>(Arrays.asList(links.split(" "))), searchQuery);
		if(text!=null)
			result = result && DocumentsManager.getDefault().update("text", text, searchQuery);
		
			
		return result;
	}
	
	public int size() {
		return documents.size();
	}

	public List<Document> getDocuments() {
		setDocuments(DocumentsManager.convertDBObject(DocumentsManager.getDefault().findAll("id")));
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}
}