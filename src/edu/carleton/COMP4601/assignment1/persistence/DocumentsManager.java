package edu.carleton.COMP4601.assignment1.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.UndirectedGraph;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

import edu.carleton.COMP4601.assignment1.Document;
import edu.carleton.COMP4601.assignment1.common.Marshaller;


public class DocumentsManager extends AbstractMongoDBManager {

	private static DocumentsManager manager;

	private static String DEFAULT_DB = "bank";
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
