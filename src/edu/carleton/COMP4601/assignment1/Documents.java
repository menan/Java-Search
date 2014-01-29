package edu.carleton.COMP4601.assignment1;


import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mongodb.*;

public class Documents {
	public static String HOST = "localhost";
	public static String DB = "bank";


	public ConcurrentHashMap<Integer, Document> getDocuments() {
		return documents;
	}

	public void setDocuments(ConcurrentHashMap<Integer, Document> documents) {
		this.documents = documents;
	}

	public static void setInstance(Documents instance) {
		Documents.instance = instance;
	}

	private ConcurrentHashMap<Integer, Document> documents;
	private static Documents instance;

	private MongoClient mc;
	private DB db;
	private DBCollection coll;

	public Documents() throws UnknownHostException {
		documents = new ConcurrentHashMap<Integer, Document>();
		Document a = new Document(1);
		Document a1 = new Document(2);
		
		mc = new MongoClient(HOST, 27017);
		db = mc.getDB(DB);
		coll = db.getCollection("documents");

//		save(a);
//		save(a1);
	}

	public Document find(int id) {
		return load(id);
	}

	public Document create(int id, String name, String tags, String links, String text) {
		Document a = new Document(id);
		a.setName(name);
		a.setText(tags);
		System.out.println("Creating document with name:" + name);
		try {
			save(a);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	public boolean close(int id) {
		if (find(id) != null) {
			Integer no = new Integer(id);
			documents.remove(no);
			return true;
		} else
			return false;
	}

	public Map<Integer, Document> getModel() {
		return documents;
	}

	public void save(Document a){
		BasicDBObject doc = new BasicDBObject("id", a.getId());
		doc.put("name", a.getName());
		doc.put("text", a.getText());
		documents.put(a.getId(), a);
		coll.insert(doc);

	}

	public Document load(int id){
		BasicDBObject query = new BasicDBObject("id", id);
		DBCursor cursor = coll.find(query);
		BasicDBObject obj = (BasicDBObject) cursor.next();
		Document doc = new Document(obj.getInt("id"));

//		System.out.println(obj.getString("description"));
		return new Document();

	}

	public int size() {
		return documents.size();
	}

	public static Documents getInstance() throws UnknownHostException {
		if (instance == null)
			instance = new Documents();
		return instance;
	}


}
