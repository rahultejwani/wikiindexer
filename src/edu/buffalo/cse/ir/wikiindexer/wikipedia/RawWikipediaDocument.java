// This is Just a temporary thing just to test stuff :-/
package edu.buffalo.cse.ir.wikiindexer.wikipedia;

import java.util.ArrayList;

public class RawWikipediaDocument {
	/*
	 * This class is responsible for getting Raw data from SAX parser and forwarding it for further parsing
	 * I hope this would not slow down the system.
	 */
	private String sTitle;
	private String sTimeStamp;//will be converted later
	private String sAuthor;
	private String sID;//will convert it to int afterwords
	private String sText;//Will contain wiki markup for later on parsing
	public String getsTitle() {
		return sTitle;
	}
	public void setsTitle(String sTitle) {
		ArrayList<String> xyz = new ArrayList<String>();
		xyz.listIterator();
		if(this.sTitle == null||this.sTitle.equals(""))
			this.sTitle = sTitle;
	}
	public String getsTimeStamp() {
		return sTimeStamp;
	}
	public void setsTimeStamp(String sTimeStamp) {
		if(this.sTimeStamp == null||this.sTimeStamp.equals(""))
			this.sTimeStamp = sTimeStamp;
	}
	public String getsAuthor() {
		return sAuthor;
	}
	public void setsAuthor(String sAuthor) {
		if(this.sAuthor == null||this.sAuthor.equals(""))
			this.sAuthor = sAuthor;
	}
	public String getsID() {
		return sID;
	}
	public void setsID(String sID) {
		if(this.sID == null||this.sID.equals(""))
			this.sID = sID;
	}
	public String getsText() {
		return sText;
	}
	public void setsText(String sText) {
		if(this.sText == null||this.sText.equals(""))
			this.sText = sText;
	}

}
