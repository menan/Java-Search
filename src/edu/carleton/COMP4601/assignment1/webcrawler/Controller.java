package edu.carleton.COMP4601.assignment1.webcrawler;

import org.apache.log4j.PropertyConfigurator;

import com.mongodb.BasicDBObject;

import edu.carleton.COMP4601.assignment1.common.Marshaller;
import edu.carleton.COMP4601.assignment1.persistence.DocumentsManager;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
	
	
	public static void main(String[] args) throws Exception {
		/*
		 * crawlStorageFolder is a folder where intermediate crawl data is
		 * stored.
		 */
		String crawlStorageFolder = "/Users/abdulrahmanalamoudi/Desktop/temp";
		/*
		 * numberOfCrawlers shows the number of concurrent threads that should
		 * be initiated for crawling.
		 */
		int numberOfCrawlers = 7;

		if (args.length == 2) {
			crawlStorageFolder = args[0];
			numberOfCrawlers = Integer.parseInt(args[1]);
		}


		CrawlConfig config = new CrawlConfig();

		config.setCrawlStorageFolder(crawlStorageFolder);

		PropertyConfigurator.configure("log4j.properties");

		
		/*
		 * Be polite: Make sure that we don't send more than 1 request per
		 * second (1000 milliseconds between requests).
		 */
		config.setPolitenessDelay(1000);

		/*
		 * You can set the maximum crawl depth here. The default value is -1 for
		 * unlimited depth
		 */
		config.setMaxDepthOfCrawling(2);

		/*
		 * You can set the maximum number of pages to crawl. The default value
		 * is -1 for unlimited number of pages
		 */
		config.setMaxPagesToFetch(100);

		/*
		 * This config parameter can be used to set your crawl to be resumable
		 * (meaning that you can resume the crawl from a previously
		 * interrupted/crashed crawl). Note: if you enable resuming feature and
		 * want to start a fresh crawl, you need to delete the contents of
		 * rootFolder manually.
		 */
		config.setResumableCrawling(false);

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,
				pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher,
				robotstxtServer);

		/*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */

		controller.addSeed("http://www.ics.uci.edu/");
		controller.addSeed("http://www.ics.uci.edu/~lopes/");
		controller.addSeed("http://www.ics.uci.edu/~welling/");

		/*
		 * Setup a Mongodb collection and a graph
		 */
		if(MyCrawler.db_manager == null)
			MyCrawler.db_manager = DocumentsManager.getDefault();
		MyCrawler.db_manager.setupTable();
		// Setup a new graph
		MyCrawler.setupANewGraph();
		
		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(MyCrawler.class, numberOfCrawlers);
		
		// Store graph in database
		MyCrawler.db_manager.add(new BasicDBObject("graph", Marshaller.serializeObject(MyCrawler.getInstanceOfGraph())));
	}
}
