package edu.carleton.COMP4601.assignment1;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@XmlRootElement
public class Document extends BasicDBObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer id;
	String name;
	String text;
	ArrayList<String> tags;
	ArrayList<String> links;
	
	public Document() {
		tags = new ArrayList<String>();
		links = new ArrayList<String>();
	}
	
	public Document(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public ArrayList<String> getTags() {
		return tags;
	}
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	public ArrayList<String> getLinks() {
		return links;
	}
	public void setLinks(ArrayList<String> links) {
		this.links = links;
	}
	public void addTag(String tag) {
		tags.add(tag);
	}
	public void removeTag(String tag) {
		tags.remove(tag);
	}
	public void addLink(String link) {
		links.add(link);
	}
	public void removeLink(String link) {
		links.remove(link);
	}
}