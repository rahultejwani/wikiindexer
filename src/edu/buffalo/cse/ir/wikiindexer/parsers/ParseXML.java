package edu.buffalo.cse.ir.wikiindexer.parsers;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.buffalo.cse.ir.wikiindexer.wikipedia.RawWikipediaDocument;

public class ParseXML extends DefaultHandler{

	boolean title = false;
	boolean id = false;
	boolean timestamp = false;
	boolean author = false;
	boolean text = false;
	StringBuffer accumulator = new StringBuffer();
	private ArrayList<RawWikipediaDocument> rwdList = new ArrayList<RawWikipediaDocument>();
	String xmlFilePath;
	RawWikipediaDocument rwdTemp;
	public ParseXML(String filename) {
		this.xmlFilePath = filename;
		parseDocument();

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
			System.out.println("IO error");
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
			text = false;
			rwdList.add(rwdTemp);
		}

	}
	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		//tmpValue = new String(ac, i, j);
		//System.out.println(new String(ch, start, length));
		if (title) {
			rwdTemp = new RawWikipediaDocument();
		}
		accumulator.append(new String(ch, start, length));

		
	}
	public ArrayList<RawWikipediaDocument> getRwdList() {
		return rwdList;
	}
}


