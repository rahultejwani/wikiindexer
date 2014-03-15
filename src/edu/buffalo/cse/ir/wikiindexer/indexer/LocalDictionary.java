/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author nikhillo
 * This class represents a subclass of a Dictionary class that is
 * local to a single thread. All methods in this class are
 * assumed thread safe for the same reason.
 */
public class LocalDictionary extends Dictionary 
{	
	HashMap<String,Integer> localDictionary = new HashMap<String, Integer>();
	static volatile int linkID = 0;
	Properties properties;
	FileOutputStream file;
	int totalTerms = 0;
	BufferedWriter bw;

	/**
	 * Public default constructor
	 * @param props: The properties file
	 * @param field: The field being indexed by this dictionary
	 */	
	public LocalDictionary(Properties props, INDEXFIELD field) 
	{
		super(props, field);		
	}

	/**
	 * Method to lookup and possibly add a mapping for the given value
	 * in the dictionary. The class should first try and find the given
	 * value within its dictionary. If found, it should return its
	 * id (Or hash value). If not found, it should create an entry and
	 * return the newly created id.
	 * @param value: The value to be looked up
	 * @return The id as explained above.
	 */
	
	//map must be a bijection in order for this to work properly
	public static <K,V> HashMap<V,K> reverse(Map<K,V> map) {
	    HashMap<V,K> rev = new HashMap<V, K>();
	    for(Map.Entry<K,V> entry : map.entrySet())
	        rev.put(entry.getValue(), entry.getKey());
	    return rev;
	}
	public synchronized int lookup(String value) 
	{		
		// Look for a value .. if found .. return ID else add with a new ID
		//System.out.println("in Lookup");
		if (reverse(linkDictionary).containsKey(value))
			return reverse(linkDictionary).get(value);
		else
		{	
			linkID++;
			linkDictionary.put(linkID,value);
			++totalTerms;
			return linkID;
		}
	}	

	public synchronized int getTotalTerms() 
	{
		return totalTerms;
	}
}
