package edu.carleton.COMP4601.assignment1.webcrawler;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.Graph;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.SimpleGraph;

import com.mongodb.BasicDBObject;

import edu.carleton.COMP4601.assignment1.persistence.DocumentsManager;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" 
                                                      + "|png|tiff?|mid|mp2|mp3|mp4"
                                                      + "|wav|avi|mov|mpeg|ram|m4v|pdf" 
                                                      + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    public static DocumentsManager db_manager = DocumentsManager.getDefault();
    
	public static UndirectedGraph<String, DefaultEdge> graph;
	
	public static Graph<String, DefaultEdge> getInstanceOfGraph(){
		if(graph == null){
			graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
		}
		return graph;
	}


	public static void setupANewGraph(){
		graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
	}

    
    /**
     * You should implement this function to specify whether
     * the given url should be crawled or not (based on your
     * crawling logic).
     */
    @Override
    public boolean shouldVisit(WebURL url) {
            String href = url.getURL().toLowerCase();
            return !FILTERS.matcher(href).matches() && href.startsWith("http://www.ics.uci.edu/");
    }

    /**
     * This function is called when a page is fetched and ready 
     * to be processed by your program.
     */
    @Override
    public void visit(Page page) {          
            String url = page.getWebURL().getURL();
            System.out.println("URL: " + url);

            if (page.getParseData() instanceof HtmlParseData) {
                    HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                    String text = htmlParseData.getText();
                    String html = htmlParseData.getHtml();
                    List<WebURL> links = htmlParseData.getOutgoingUrls();
                    Header[] headers = page.getFetchResponseHeaders();
                    
                    String visited_url = page.getWebURL().getURL();
                    String parent_url = page.getWebURL().getParentUrl();
                    
                    // Add page to db
                    BasicDBObject doc = new BasicDBObject();
                    doc.append("url", visited_url);
                    doc.append("parent_url", parent_url);
                    doc.append("text", text);
                    for(Header h : headers){
                    	doc.append(h.getName(), h.getValue());
                    }
                    db_manager.add(doc);
                    // Add page to graph
                    // - Add vertex
                    if(!getInstanceOfGraph().containsVertex(visited_url)){
                        getInstanceOfGraph().addVertex(visited_url);
                    }
                    // - Add edge
                    if(parent_url != null){
                    	getInstanceOfGraph().addEdge(parent_url, visited_url);
                    }

                    System.out.println("Text length: " + text.length());
                    System.out.println("Html length: " + html.length());
                    System.out.println("Number of outgoing links: " + links.size());
                    System.out.println("Thread ID: " + Thread.currentThread().getId());                    
            }
    }
}