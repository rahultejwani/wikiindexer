/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import edu.buffalo.cse.ir.wikiindexer.IndexerConstants;

/**
 * @author nikhillo
 * This class is used to introspect a given index
 * The expectation is the class should be able to read the index
 * and all associated dictionaries.
 */
public class IndexReader {
	/**
	 * Constructor to create an instance 
	 * @param props: The properties file
	 * @param field: The index field whose index is to be read
	 */
	INDEXFIELD iField;
	Properties props;
	String fileNameSD;				
	FileReader file;				
	BufferedReader br;
	PostingList pl;
	int totalTerms;
	static TreeMap<Integer,String> sharedDictionary;//Id to Doc Name mapping
	public IndexReader(Properties props, INDEXFIELD field) {
		this.iField = field;
		this.props = props;
		try
		{
			file = new FileReader(getFileName());
			br = new BufferedReader(file);
			pl = new PostingList();			
			String indexRow, term;
			int count = 0;
			while ((indexRow = br.readLine()) != null) 
			{
				String[] index = indexRow.split("#");
				term = index[0];
				String[] posting = index[2].split(":");
				for (int i = 0; i < posting.length; i=i+2) {
					pl.add(term, Integer.parseInt(posting[i]), Integer.parseInt(posting[i+1]));
				}
				count++;
			}
			totalTerms = count;
			br.close();
			file.close();
			System.gc();
			file = new FileReader(fileNameSD);
			br = new BufferedReader(file);
			while ((indexRow = br.readLine()) != null) 
			{
				String[] index = indexRow.split("#");
				sharedDictionary.put(Integer.parseInt(index[0]),index[1]);
			}
			br.close();
			file.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.err.println("Exception at IndexReader constructor: Might be because of File read or integer conversion problem");
		}
	}
	public String getFileName()
	{
		String filename = null;
		String filepath = props.getProperty(IndexerConstants.TEMP_DIR)+"/";
		this.fileNameSD = filepath + "SharedDictionary.dat";
		switch(iField)
		{
		case LINK: 
			filename = filepath+"LinkIndex.dat";
			break;
		case AUTHOR:
			filename = filepath+"AuthorIndex.dat";
			break;
		case CATEGORY:
			filename = filepath+"CategoryIndex.dat";
			break;
		case TERM:
			filename = filepath+"TermIndex.dat";
			break;
		default:
			filename = filepath+"InvalidFile.dat";
			break;
		}
		return filename;
	}
	/**
	 * Method to get the total number of terms in the key dictionary
	 * @return The total number of terms as above
	 */
	public int getTotalKeyTerms() {
		return totalTerms;
	}

	/**
	 * Method to get the total number of terms in the value dictionary
	 * @return The total number of terms as above
	 */
	public int getTotalValueTerms() {
		return totalTerms;
	}

	/**
	 * Method to retrieve the postings list for a given dictionary term
	 * @param key: The dictionary term to be queried
	 * @return The postings list with the value term as the key and the
	 * number of occurrences as value. An ordering is not expected on the map
	 */
	public Map<String, Integer> getPostings(String key) {
		if(!pl.getPostList().containsKey(key))
			return null;
		Map<String, Integer> retVal = new HashMap<String, Integer>();
		Postings p = pl.getPostList().get(key);
		try{
			for (Entry<Integer, Integer> entry : p.getDocFreq().entrySet()) {
				retVal.put(sharedDictionary.get(entry.getKey()),entry.getValue());
			}
		}catch(Exception e)
		{
			System.err.println("Cought Error: Might be not able to retrieve a key in function IndexReader.getPostings()");
		}
		return retVal;

	}

	/**
	 * Method to retrieve the all postings lists for a given dictionary pattern
	 * @param key: The dictionary pattern to be queried
	 * @return Array list of the postings list with the value term as the key and the
	 * number of occurrences as value. An ordering is not expected on the map
	 */
	public ArrayList<Map<String, Integer>> getAllPostingsFromPattern(String pattern) 
	{				
		ArrayList<Map<String, Integer>> postingsArray = new ArrayList<Map<String, Integer>>(); // PostingsList Array
		try{
			for(Entry<String, Postings> entry : pl.getPostList().entrySet())
			{
				if(entry.getKey().matches(pattern))
				{
					Map<String,Integer> tempMap = new TreeMap<String, Integer>(); 
					for (Entry<Integer, Integer> docEntry : entry.getValue().getDocFreq().entrySet()) {
						tempMap.put(sharedDictionary.get(docEntry.getKey()),docEntry.getValue());
					}
					postingsArray.add(tempMap);
				}
			}
		}catch(Exception e)
		{
			System.err.println("Cought Error: Might be not able to retrieve a key in function IndexReader.getPostings()");
		}
		return postingsArray;
	}
	/**
	 * Method to get the top k key terms from the given index
	 * The top here refers to the largest size of postings.
	 * @param k: The number of postings list requested
	 * @return An ordered collection of dictionary terms that satisfy the requirement
	 * If k is more than the total size of the index, return the full index and don't 
	 * pad the collection. Return null in case of an error or invalid inputs
	 */
	public Collection<String> getTopK(int k) {
		TreeMap<Integer, String> sm= new TreeMap<Integer, String>();
		Collection<String> retStr = new ArrayList<String>();

		for (Entry<String, Postings> entry : pl.getPostList().entrySet()) {
			sm.put(entry.getValue().getDocFreq().size(), entry.getKey());
		}
		int count =0;
		for (Entry<Integer, String> entry : sm.descendingMap().entrySet()) {
			if(count<k)
				retStr.add(entry.getValue());
			else
				break;
			count++;
		}
		return retStr;		
	}

	/**
	 * Method to execute a boolean AND query on the index
	 * @param terms The terms to be queried on
	 * @return An ordered map containing the results of the query
	 * The key is the value field of the dictionary and the value
	 * is the sum of occurrences across the different postings.
	 * The value with the highest cumulative count should be the
	 * first entry in the map.
	 */
	public Map<String, Integer> query(String... terms) {
		//TODO: Implement this method (FOR A BONUS)
		return null;
	}
}
