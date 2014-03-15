/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.buffalo.cse.ir.wikiindexer.wikipedia.RawWikipediaDocument;
import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaDocument;
import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaParser;

/**
 * @author nikhillo
 *
 */
public class Parser extends DefaultHandler{
	/* */
	private final Properties props;
	int garbageCollectionFlag = 0;
	boolean title = false;
	boolean id = false;
	boolean timestamp = false;
	boolean author = false;
	boolean text = false;
	StringBuffer accumulator = new StringBuffer();
	String xmlFilePath;
	RawWikipediaDocument rwdTemp;
	WikipediaDocument wd;
	Collection<WikipediaDocument> documents = new ArrayList<WikipediaDocument>();
	/**
	 * 
	 * @param idxConfig
	 * @param parser
	 */
	public Parser(Properties idxProps) {
		props = idxProps;

	}
	///////////////////////////////////Remove it
	public Parser() {	
		props= null;
	}
	////////////////////
	/* TODO: Implement this method */
	/**
	 * 
	 * @param filename
	 * @param docs
	 */
	public void parse(String filename, Collection<WikipediaDocument> docs) {

		this.xmlFilePath = filename;
		if(filename != null && !filename.equals(""))
		{	
			parseDocument();
			//docs = this.documents;
			docs.addAll(this.documents);
			//System.out.println("Parse end"+ docs.size());
		}
	}

	/**
	 * Method to add the given document to the collection.
	 * PLEASE USE THIS METHOD TO POPULATE THE COLLECTION AS YOU PARSE DOCUMENTS
	 * For better performance, add the document to the collection only after
	 * you have completely populated it, i.e., parsing is complete for that document.
	 * @param doc: The WikipediaDocument to be added
	 * @param documents: The collection of WikipediaDocuments to be added to
	 */
	@SuppressWarnings("unused")
	private synchronized void add(WikipediaDocument doc, Collection<WikipediaDocument> documents) {
		documents.add(doc);
	}
	private synchronized void add(WikipediaDocument doc) {
		//System.out.println("in add"+ this.documents.size());
		this.documents.add(doc);
	}

	private void parseDocument() {
		// parse
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(xmlFilePath, this);
		} catch (ParserConfigurationException e) {
			System.out.println("ParserConfig error");
		} catch (SAXException e) {
			System.out.println("SAXException : xml not well formed");
		} catch (IOException e) {
			
			System.err.println("IO error: not able to do operations on:"+ xmlFilePath);
		}
	}
	public void startElement(String uri, String localName, String elementName, Attributes attributes) throws SAXException {
		accumulator.setLength(0);
		if (elementName.equalsIgnoreCase("TITLE")) {
			title = true;
		}

		if (elementName.equalsIgnoreCase("TIMESTAMP")) {
			timestamp = true;
		}

		if (elementName.equalsIgnoreCase("ID")) {
			id = true;
		}

		if (elementName.equalsIgnoreCase("IP") || elementName.equalsIgnoreCase("USERNAME")) {
			author = true;
		}
		if (elementName.equalsIgnoreCase("TEXT")) {
			text = true;
		}
	}
	public void endElement(String uri, String localName,String qName)throws SAXException {
		if (title) {
			rwdTemp.setsTitle(accumulator.toString().trim());
			title = false;
		}

		if (timestamp) {
			rwdTemp.setsTimeStamp(accumulator.toString());
			timestamp = false;
		}

		if (id) {
			rwdTemp.setsID(accumulator.toString());
			id = false;
		}

		if (author) {
			rwdTemp.setsAuthor(accumulator.toString());
			author = false;
		}
		if (text) {
			rwdTemp.setsText(accumulator.toString());
			wd = WikipediaParser.getWikiDocument(rwdTemp);
			this.add(wd);
			text = false;
			//this.add(rwdTemp); Here I have to add stuff to collections ;)
			if(garbageCollectionFlag==1000)
			{
				//To collect garbage to save up some space 
				garbageCollectionFlag=0;
				System.gc();
			}
		}

	}
	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		//tmpValue = new String(ac, i, j);
		//System.out.println(new String(ch, start, length));
		if (title) {
			rwdTemp = new RawWikipediaDocument();
			garbageCollectionFlag++;
		}
		accumulator.append(new String(ch, start, length));		
	}
}
