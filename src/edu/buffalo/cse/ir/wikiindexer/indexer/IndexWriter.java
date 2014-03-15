/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.indexer;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Properties;

import edu.buffalo.cse.ir.wikiindexer.IndexerConstants;

/**
 * @author nikhillo
 * This class is used to write an index to the disk
 * 
 */
public class IndexWriter implements Writeable {
	Properties properties;
	INDEXFIELD iKeyField;
	INDEXFIELD iValueField;
	boolean isForward;
	PostingList plf;
	LinkIndexList lif;
	/**
	 * Constructor that assumes the underlying index is inverted
	 * Every index (inverted or forward), has a key field and the value field
	 * The key field is the field on which the postings are aggregated
	 * The value field is the field whose postings we are accumulating
	 * For term index for example:
	 * 	Key: Term (or term id) - referenced by TERM INDEXFIELD
	 * 	Value: Document (or document id) - referenced by LINK INDEXFIELD
	 * @param props: The Properties file
	 * @param keyField: The index field that is the key for this index
	 * @param valueField: The index field that is the value for this index
	 */
	public IndexWriter(Properties props, INDEXFIELD keyField, INDEXFIELD valueField) {
		this(props, keyField, valueField, false);
	}

	/**
	 * Overloaded constructor that allows specifying the index type as
	 * inverted or forward
	 * Every index (inverted or forward), has a key field and the value field
	 * The key field is the field on which the postings are aggregated
	 * The value field is the field whose postings we are accumulating
	 * For term index for example:
	 * 	Key: Term (or term id) - referenced by TERM INDEXFIELD
	 * 	Value: Document (or document id) - referenced by LINK INDEXFIELD
	 * @param props: The Properties file
	 * @param keyField: The index field that is the key for this index
	 * @param valueField: The index field that is the value for this index
	 * @param isForward: true if the index is a forward index, false if inverted
	 */
	public IndexWriter(Properties props, INDEXFIELD keyField, INDEXFIELD valueField, boolean isForward) {
		this.properties = props;
		this.iKeyField = keyField;
		this.iValueField = valueField;
		this.isForward = isForward;
		if(keyField.equals(INDEXFIELD.LINK) )
			lif = new LinkIndexList();
		else
			plf = new PostingList();
	}

	/**
	 * Method to make the writer self aware of the current partition it is handling
	 * Applicable only for distributed indexes.
	 * @param pnum: The partition number
	 */
	public void setPartitionNumber(int pnum) {
		//TODO: Optionally implement this method
		// Leaving for later
	}

	/**
	 * Method to add a given key - value mapping to the index
	 * @param keyId: The id for the key field, pre-converted
	 * @param valueId: The id for the value field, pre-converted
	 * @param numOccurances: Number of times the value field is referenced
	 *  by the key field. Ignore if a forward index
	 * @throws IndexerException: If any exception occurs while indexing
	 */
	public void addToIndex(int keyId, int valueId, int numOccurances) throws IndexerException {
		lif.add(keyId, valueId, numOccurances);
	}

	/**
	 * Method to add a given key - value mapping to the index
	 * @param keyId: The id for the key field, pre-converted
	 * @param value: The value for the value field
	 * @param numOccurances: Number of times the value field is referenced
	 *  by the key field. Ignore if a forward index
	 * @throws IndexerException: If any exception occurs while indexing
	 */
	public void addToIndex(int keyId, String valueId, int numOccurances) throws IndexerException {
		lif.add(keyId, valueId, numOccurances);
	}

	/**
	 * Method to add a given key - value mapping to the index
	 * @param key: The key for the key field
	 * @param valueId: The id for the value field, pre-converted
	 * @param numOccurances: Number of times the value field is referenced
	 *  by the key field. Ignore if a forward index
	 * @throws IndexerException: If any exception occurs while indexing
	 */
	public void addToIndex(String key, int valueId, int numOccurances) throws IndexerException {
		key = key.replaceAll("[^\\w\\d]+","");
		if(key.trim().length()>0)
		{
			plf.add(key, valueId, numOccurances);
			String reverse = new StringBuilder(key).reverse().toString();
			if(!reverse.equals(key))
				plf.add(reverse, valueId, numOccurances);
		}
	}

	/**
	 * Method to add a given key - value mapping to the index
	 * @param key: The key for the key field
	 * @param value: The value for the value field
	 * @param numOccurances: Number of times the value field is referenced
	 *  by the key field. Ignore if a forward index
	 * @throws IndexerException: If any exception occurs while indexing
	 */
	public void addToIndex(String key, String valueId, int numOccurances) throws IndexerException {
		//Should never come to this function
		key = key.replaceAll("[^\\w\\d]+","");
		if(key.trim().length()>0)
			plf.add(key, valueId.length(), numOccurances);
	}

	/* (non-Javadoc)
	 * @see edu.buffalo.cse.ir.wikiindexer.indexer.Writeable#writeToDisk()
	 */
	public void writeToDisk() throws IndexerException {
		String filename = getFileName();
		FileOutputStream file;
		BufferedWriter bw;
		try
		{
			String tempPath = properties.getProperty(IndexerConstants.TEMP_DIR);			
			file = new FileOutputStream(tempPath+"/"+filename);				
			bw = new BufferedWriter(new OutputStreamWriter(file, "UTF-8"));
			if(iKeyField == INDEXFIELD.LINK)
			{
				for (Entry<Integer, Postings> entry : lif.getPostList().entrySet()) {
					HashMap<Integer, Integer> df= entry.getValue().getDocFreq();
					int i=0;
					bw.write(entry.getKey()+"#"+df.size()+"#");
					//System.out.print(entry.getKey()+"#"+df.size()+"#");
					for (Entry<Integer,Integer> docFreq : df.entrySet()) {
						bw.write(docFreq.getKey().toString()+":"+docFreq.getValue().toString());
						//System.out.println(docFreq.getKey()+":"+docFreq.getValue());
						if(i != (df.size()-1))
							bw.write(":");
						else
							bw.write("\n");
						i++;
					}

				}

			}
			else
			{
				for (Entry<String, Postings> entry : plf.getPostList().entrySet()) {
					HashMap<Integer, Integer> df= entry.getValue().getDocFreq();
					int i=0;
					bw.write(entry.getKey()+"#"+df.size()+"#");
					for (Entry<Integer,Integer> docFreq : df.entrySet()) {
						bw.write(docFreq.getKey()+":"+docFreq.getValue());
						
						if(i != (df.size()-1))
							bw.write(":");
						else
							bw.write("\n");
						i++;
					}
				}
				
			}
			bw.close();
		}
		catch(IOException e)
		{
			System.err.println(e.getStackTrace()+":"+e);
		}

	}
	public String getFileName()
	{
		String filename = null;
		switch(iKeyField)
		{
		case LINK: 
			filename = "LinkIndex.dat";
			break;
		case AUTHOR:
			filename = "AuthorIndex.dat";
			break;
		case CATEGORY:
			filename = "CategoryIndex.dat";
			break;
		case TERM:
			filename = "TermIndex.dat";
			break;
		default:
			filename = "InvalidFile.dat";
			break;
		}
		return filename;
	}
	/* (non-Javadoc)
	 * @see edu.buffalo.cse.ir.wikiindexer.indexer.Writeable#cleanUp()
	 */
	public void cleanUp() {
		// TODO Implement this method

	}

}
