package edu.carleton.COMP4601.assignment1.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.UndirectedGraph;

import com.mongodb.DBObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

import edu.carleton.COMP4601.assignment1.Document;

import edu.carleton.COMP4601.assignment1.common.Marshaller;


public class DocumentsManager extends AbstractMongoDBManager {

	private static DocumentsManager manager;
	private static String DEFAULT_DB = "sda";
	private static String DEFAULT_COLLECTION = "documents";
	
	
	/**
	 * Shorten constructor for DocumentsManager where host and port will be substituted by
	 * their default values specified in class AbstractMongoDB. Parameter <objectClass>
	 * can have the value null in case there is no specific class this collection holds 
	 * into.
	 * Returns a new instance of class DocumentsManager.
	 * 
	 * @param db_name
	 * @param collection_name
	 * @param objectClass
	 * @return
	 */
	private DocumentsManager(String db_name, String collection_name, Class objectClass) {
		super(db_name, collection_name, objectClass);
	}
	
	private DocumentsManager(String host, int port, String db_name,
			String collection_name, Class objectClass) {
		super(host, port, db_name, collection_name, objectClass);
	}


	/**
	 * Returns default instance.
	 * 
	 * @return
	 */
	public static DocumentsManager getDefault() {
		
		if (manager == null) {
			manager = new DocumentsManager(DEFAULT_DB, DEFAULT_COLLECTION, null);
		}
		return manager;
	}

	
	@Override
	public boolean setupTable() {
		if(collection != null && this.drop()){
			collection = db.getCollection(this.collection_name);
			return (collection != null);
		}
		return false;
	}

	@Override
	public String getClassName() {
		if(objecClass == null)
			return "";
		return this.objecClass.getSimpleName();
	}

	@Override
	public String getCollectionName() {
		if(collection == null)
			return "";
		return collection.getName();
	}
	
	public static List<Document> convertDBObject(List<DBObject> list){
		List<Document> docs = new ArrayList<Document>(); 
		for(DBObject obj: list){
			docs.add(new Document(obj.toMap()));
		}
		return docs;
	}

	/**
	 * Find all documents in the database that has the given field.
	 * Returns a List<DBObject> if documents were found.
	 * 
	 * @param field
	 * @param new_value
	 * @param searchQuery
	 * @return List<DBObject>
	 */

	public Document create(int id, String name, String tags, String links, String text) {
		Document a = new Document(id);
		a.setName(name);
		a.setText(text);
		a.setTags(new ArrayList<String>(Arrays.asList(tags.split(" "))));
		a.setLinks(new ArrayList<String>(Arrays.asList(links.split(" "))));
		try {
			save(a);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	public synchronized WriteResult add(Document a){

		BasicDBObject doc = new BasicDBObject("id", a.getId());
		doc.put("name", a.getName());
		doc.put("text", a.getText());
		doc.put("tags", a.getTags());
		doc.put("links", a.getLinks());
		
		if(collection != null){
//			System.out.println("Creating document with name:" + a.getName());
			return collection.insert(doc, WriteConcern.SAFE);
		}
		else
			return null;
	}

	
	public boolean remove(Document doc) {
		return remove(doc.getId());
	}
	
	public boolean remove(Integer doc_id) {
		return this.delete("id", doc_id);
	}

	public void save(Document a){
//		if(this.exists("id",a.getId())){
//			this.delete("id", a.getId());
//		}
		this.add(a);
	}

	public Document load(int id){
		BasicDBObject query = new BasicDBObject("id", id);
		DBCursor cursor = collection.find(query);
		BasicDBObject obj = (BasicDBObject) cursor.next();
		Document doc = new Document(obj.getInt("id"));

		return doc;

	}

	
	public static void main(String[] args) {
		DocumentsManager manager = DocumentsManager.getDefault();
		// Query #1: Find the first 20 documents and print their URL's and Parent-URLs
		List<DBObject> results1 = manager.findNDocsStatringAtIndex("url", 20, 0);
		System.out.println("Query #1 - Results size: "+results1.size());
		for(DBObject r : results1){
			System.out.println("URL: "+r.get("url") +" | Parent-URL: "+r.get("parent_url"));
		//	System.out.println(h.toString());
		}
		
		// Query #2: Search for a string
		List<DBObject> results2 = manager.search("TeXt","alamoudi", false);
		System.out.println("Query #2 - Results size: "+results2.size());
		for(DBObject r : results2){
			System.out.println("URL: "+r.get("url") +" | Parent-URL: "+r.get("parent_url"));
		//	System.out.println(h.toString());
		}


		// Query #3: Get graph
		DBObject results3 = manager.find("graph");
		System.out.println("Query #3 - Result: "+results3.toString());
		byte[] b = (byte[]) results3.get("graph");
		try {
			@SuppressWarnings("unchecked")
			UndirectedGraph<String, DefaultEdge> graph = (UndirectedGraph<String, DefaultEdge>) Marshaller.deserializeObject(b);
			System.out.println("	Graph	\n"+graph.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Query #4: Delete a list of URLs
		String url1 = "http://www.ics.uci.edu/community/";
		String url2 = "http://www.ics.uci.edu/about/bren/";
		
		List<DBObject> url1_before = manager.search("url", url1, true);
		List<DBObject> url2_before = manager.search("url", url2, true);
		System.out.println("Query #4 - URL counts   URL1:"+url1_before.size()+", URL2:"+url2_before.size());

		
	}
}
