/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.wikipedia;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * This class implements Wikipedia markup processing.
 * Wikipedia markup details are presented here: http://en.wikipedia.org/wiki/Help:Wiki_markup
 * It is expected that all methods marked "todo" will be implemented by students.
 * All methods are static as the class is not expected to maintain any state.
 */
public class WikipediaParser {
	static WikipediaDocument wd = null;
	/* TODO */
	/**
	 * Method to parse section titles or headings.
	 * Refer: http://en.wikipedia.org/wiki/Help:Wiki_markup#Sections
	 * @param titleStr: The string to be parsed
	 * @return The parsed string with the markup removed
	 */
	public static WikipediaDocument getWikiDocument(RawWikipediaDocument rwd)
	{

		try {
			wd = new WikipediaDocument(Integer.parseInt(rwd.getsID()),rwd.getsTimeStamp(),rwd.getsAuthor(),rwd.getsTitle());
		} catch (NumberFormatException e) {
			System.out.println("Number Format Exception Handeled: "+ e);
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("Integer Parsing Exception Handeled: "+ e);
			e.printStackTrace();
		}
		//ArrayList<String> linksHolder = new ArrayList<String>(Arrays.asList(WikipediaParser.parseLinks(rwd.getsText())));
		WikipediaParser.parseSectionTitle(rwd.getsText());
		//linksHolder.remove(linksHolder.get(0));
	//	wd.;
		WikipediaParser.parseLinks(rwd.getsText());
		return wd;

	} 
	public static String parseSectionTitle(String titleStr) {

		int start=0,end=0;
		if(titleStr==null)
			return null;
		else if(titleStr.equals(""))
			return "";
		Pattern pattern = Pattern.compile("(==+)(.*?)(==+)");
		Matcher m = pattern.matcher(titleStr);
		Matcher m_old = pattern.matcher(titleStr);
		String text="",title="";
		m_old.find();

		title = "Default";
		try{
			text= titleStr.substring(0,m_old.start()).trim();
		}catch(Exception e)
		{
			text = "";
		}
		if(wd != null)
			wd.addSection(new String(title), new String(text));
		while(m.find())
		{
			title=m.group(2).trim();
			m_old.find();
			start = m.end();
			try{
				end = m_old.start();
			}
			catch(Exception e)
			{
				end = titleStr.length();
			}
			text = titleStr.substring(start, end);
			if(wd != null)
				wd.addSection(new String(title), new String(text));
		}
		return title;
		//return titleStr.replaceAll("==+", "").trim();
	}
	/*
	 * boolean firstEq=false;
		boolean lastEq=false;

		for (int i = 0; i < titleStr.length(); i++) {
			if(titleStr.charAt(i) == '=')
			{
				if(firstEq == false)
				{
					firstEq = true;
				}
				else{

				}
			}
		}
	 */
	/*
	 * 		

	 */
	/* TODO */
	/**
	 * Method to parse list items (ordered, unordered and definition lists).
	 * Refer: http://en.wikipedia.org/wiki/Help:Wiki_markup#Lists
	 * @param itemText: The string to be parsed
	 * @return The parsed string with markup removed
	 */
	public static String parseListItem(String itemText) {
		Pattern pattern = Pattern.compile("(^#+|\\*+|;+|:+)(\\s+)(.*)",Pattern.MULTILINE);
		if(itemText==null)
			return null;
		else if(itemText=="")
			return "";
		Matcher m = pattern.matcher(itemText);
		return m.replaceAll("$3");

	}

	/* TODO */
	/**
	 * Method to parse text formatting: bold and italics.
	 * Refer: http://en.wikipedia.org/wiki/Help:Wiki_markup#Text_formatting first point
	 * @param text: The text to be parsed
	 * @return The parsed text with the markup removed
	 */
	/*boldItalic removes bold or Italics or both tags from the text stream.
	 * It is then passed to remove text tags like small and big and long spaces, &nbsp;
	 */
	public static String parseTextFormatting(String text) {
		if(text==null)
			return null;
		else if(text.equals(""))
			return "";
		Pattern pattern = Pattern.compile("(.*?)['][']+(.*?)['][']+(.*?)",Pattern.MULTILINE);
		Matcher m = pattern.matcher(text);
		return m.replaceAll("$1$2$3");
	}

	/* TODO */
	/**
	 * Method to parse *any* HTML style tags like: <xyz ...> </xyz>
	 * For most cases, simply removing the tags should work.
	 * @param text: The text to be parsed
	 * @return The parsed text with the markup removed.
	 */

	/*
	 * Redundant formating, have already parsed the following in the parseTextFormatting method
	 */
	public static String parseTagFormatting(String text) {
		if(text==null)
			return null;
		else if(text.equals(""))
			return "";
		Pattern pattern = Pattern.compile("<[^>]*>",Pattern.MULTILINE);
		Matcher m = pattern.matcher(text);
		text=m.replaceAll("");
		text= text.replaceAll("^\\s+|\\s+$", "");
		text=text.replaceAll("\\s+", " ");
		text=text.replaceAll("\\&lt;(.*?)\\&gt;", "");
		return text;
		//text.replaceAll("  ", " ");
		//return null;
	}

	/* TODO */
	/**
	 * Method to parse wikipedia templates. These are *any* {{xyz}} tags
	 * For most cases, simply removing the tags should work.
	 * @param text: The text to be parsed
	 * @return The parsed text with the markup removed
	 */


	public static String parseTemplates(String text) {
		if(text==null)
			return null;
		else if(text.equals(""))
			return "";

		Pattern pattern = Pattern.compile("\\{\\{[^\\}]*\\}\\}",Pattern.MULTILINE);
		Matcher m = pattern.matcher(text);
		return m.replaceAll("");
	}


	/* TODO */
	/**
	 * Method to parse links and URLs.
	 * Refer: http://en.wikipedia.org/wiki/Help:Wiki_markup#Links_and_URLs
	 * @param text: The text to be parsed
	 * @return An array containing two elements as follows - 
	 *  The 0th element is the parsed text as visible to the user on the page
	 *  The 1st element is the link url
	 */


	public static String convertToUrl(String word)
	{
		if(word==null || word.equals(""))
			return "";
		int ind=0;
		ind=word.indexOf(':');
		if(ind==-1)
		{
			char[] stringArray = word.toCharArray();
			char a=stringArray[0];
			if((a>=97 && a<=122)||(a>=65 && a<=90))
			{
				stringArray[0] = Character.toUpperCase(stringArray[0]);
				word = new String(stringArray);
				word=word.replaceAll("\\s", "_");
				return  word;
			}
			else 
				return "";
		}
		else
			return "";
	}

	public static String[] parseLinks(String text) {
		//text=WikipediaParser.parseListItem(text);
		//text=WikipediaParser.parseSectionTitle(text);
		text=WikipediaParser.parseTagFormatting(text);
		//text=WikipediaParser.parseTextFormatting(text);
	//	text=WikipediaParser.parseTemplates(text);
		ArrayList<String> result = new ArrayList<String>();
		//String linkString;
		if(text=="" || text == null)
		{
			result.add("");
			result.add("");
			String[] linkText = new String[result.size()]; 
			linkText = result.toArray(linkText);
			return linkText;
		}
		Pattern pattern = Pattern.compile("\\[\\[([^\\]]*)\\]\\]");
		ArrayList<String> list = new ArrayList<String>();
		Matcher m = pattern.matcher(text);
		int a=0,c=0;
		// String[] result= new String[2];
		String temp="", temp1="";
		// String temp1 = "";
		//  StringBuffer sb = new StringBuffer(text.length());
		// m.find();
		while (m.find()) 
		{
			temp=m.group(1);
			temp1=m.group(1);
			
			// b= temp.length() - temp.replace(":", "").length();
			c= temp.length() - temp.replace("|", "").length();
			/* if (b>1)
        	   temp=temp.substring(temp.indexOf(':')+1, temp.length());
			 */
			a=temp.indexOf('|');
			// System.out.println("Index value of |");
			// System.out.println(a);
			if (c<2)
			{
				//  a=temp.length();
				if(a==(temp.length()-1))
				{
					temp=temp.substring(0,temp.length()-1);
					if(temp.indexOf('#')!=-1)
						temp1="";
					

					else
					{
						temp=temp.replaceAll("Wikipedia:|Wiktionary:", "");  
						temp=temp.replaceAll(",.*", "");  
						temp=temp.replaceAll("\\(.*\\)", ""); 
						temp=temp.trim();
						temp1=temp1.substring(0, temp1.length()-1);
						
					}
				}
				else if(a!=-1)
				{
					if(!temp.contains("Category:"))
					{
						temp=temp.substring(a+1, temp.length());
						temp1=temp1.substring(0, a);
						
					}
				}
			}
			else
			{
				int d=temp.lastIndexOf('|');
				temp=temp.substring(d+1, temp.length());
				temp1="";
				
			}
			temp=temp.replaceAll("File:wiki(.*)", "");  
			if(temp.indexOf(':')==0 && temp.contains("Category:"))
			{
				temp1="";
				temp=temp.substring(1, temp.length());
			}
			else if(temp.contains("Category:"))
			{
				if(wd != null)
					wd.addCategory(new String(temp.substring(temp.indexOf("Category:")+9,temp.length())));
				temp=temp.replaceAll("Category:", "");
				temp1="";
			}
			if(wd!=null && !temp1.equals(""))
				wd.addLink(WikipediaParser.convertToUrl(temp1));
		}
		text =  m.replaceAll(temp);
		text=text.replaceAll("\\[http:\\/\\/www\\.wikipedia\\.org\\s(.*?)\\]|\\[http:\\/\\/www\\.wikipedia\\.org(.*?)\\]", "$1");
		//if(list.size()==0)
			list.add(text);
		//else
			//list.set(0,text);
		//list.remove(0);
			
		list.add(WikipediaParser.convertToUrl(temp1));
		String[] linkText = new String[list.size()];
		linkText = list.toArray(linkText);
		// return ;
		return linkText;
	}

}
