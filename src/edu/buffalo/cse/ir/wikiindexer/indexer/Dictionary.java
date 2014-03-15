/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.TreeMap;

import edu.buffalo.cse.ir.wikiindexer.IndexerConstants;

/**
 * @author nikhillo
 * An abstract class that represents a dictionary object for a given index
 */
public abstract class Dictionary implements Writeable 
{
	TreeMap<Integer, String> linkDictionary = new TreeMap<Integer, String>();
	static volatile int linkID = 0;
	Properties properties;
	FileOutputStream file;
	BufferedWriter bw;
	
	public Dictionary (Properties props, INDEXFIELD field) 
	{
		properties = props;
	}
	
	/* (non-Javadoc)
	 * @see edu.buffalo.cse.ir.wikiindexer.indexer.Writeable#writeToDisk()
	 */
	public void writeToDisk() throws IndexerException 
	{
		try
	    {
			
			String path = properties.getProperty(IndexerConstants.TEMP_DIR)+"/";			
			file = new FileOutputStream(path + "SharedDictionary.dat");				
			bw = new BufferedWriter(new OutputStreamWriter(file, "UTF-8"));	
			
	        if(linkDictionary!=null)
	        {
	        	Iterator<Entry<Integer, String>> itr = linkDictionary.entrySet().iterator();
	        	while(itr.hasNext())
	        	{	        		
	        		Entry<Integer, String> entry = itr.next();
	        		String str = entry.getKey() + "#" + entry.getValue(); 	        		
	        		bw.write(str + System.getProperty("line.separator"));
	        	}	        	
	        	System.out.println("***********SharedDictionary size: "  + linkDictionary.size());
	        }
	        else
	        {
	        	System.out.println("**********LinkDict is null**************");
	        }
	    }
		catch(IOException i)
	    {
	        i.printStackTrace();
	    }

	}

	/* (non-Javadoc)
	 * @see edu.buffalo.cse.ir.wikiindexer.indexer.Writeable#cleanUp()
	 */
	public void cleanUp() 
	{
		try 
		{
			bw.close();
			file.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	public static <K,V> HashMap<V,K> reverse(Map<K,V> map) {
	    HashMap<V,K> rev = new HashMap<V, K>();
	    for(Map.Entry<K,V> entry : map.entrySet())
	        rev.put(entry.getValue(), entry.getKey());
	    return rev;
	}
	/**
	 * Method to check if the given value exists in the dictionary or not
	 * Unlike the subclassed lookup methods, it only checks if the value exists
	 * and does not change the underlying data structure
	 * @param value: The value to be looked up
	 * @return true if found, false otherwise
	 */
	public synchronized boolean exists(String value) 
	{
		if(reverse(linkDictionary).containsKey(value))
			return true;
		return false;
	}
	
	/**
	 * MEthod to lookup a given string from the dictionary.
	 * The query string can be an exact match or have wild cards (* and ?)
	 * Must be implemented ONLY AS A BONUS
	 * @param queryStr: The query string to be searched
	 * @return A collection of ordered strings enumerating all matches if found
	 * null if no match is found
	 */
	public Collection<String> query(String queryStr) 
	{		
		String pattern =  "^" + queryStr.replaceAll("\\*", ".*").replaceAll("\\?", ".") + "$";
		//System.out.println(pattern);
		Iterator<Entry<Integer, String>> itr = linkDictionary.entrySet().iterator();
		List<String> templist = new ArrayList<String>();
		while(itr.hasNext())
		{
			Entry<Integer, String> entry = itr.next();
			if(entry.getValue().matches(pattern))
			{
				templist.add(entry.getValue());
			}
				
		}		
		Collections.sort(templist);
		if(templist.size() == 0)
			return null;
		else
			return templist;
	}
	
	/**
	 * Method to get the total number of terms in the dictionary
	 * @return The size of the dictionary
	 */
	public synchronized int getTotalTerms() 
	{
		return linkDictionary.size();
	}		
}
