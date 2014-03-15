/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.wikipedia;



import edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;


/**
 * A simple map based token view of the transformed document
 * 
 *
 */
public class IndexableDocument {
	
	TokenStream author=null;
	TokenStream term = null;
	TokenStream link = null;
	TokenStream category = null;
	String doc_id= null;
		
	/**
	 * Default constructor
	 */
	public IndexableDocument() {
		//TODO: Init state as needed
		//this.author= new TokenStream();
		
	}
	
	/**
	 * MEthod to add a field and stream to the map
	 * If the field already exists in the map, the streams should be merged
	 * @param field: The field to be added
	 * @param stream: The stream to be added.
	 */
	public void addField(INDEXFIELD field, TokenStream stream) {
		//TODO: Implement this method
		if(field.equals(INDEXFIELD.AUTHOR))
		{
			this.author = stream;
		}
		else if(field.equals(INDEXFIELD.TERM))
		{
			this.term = stream;
		}
		else if(field.equals(INDEXFIELD.CATEGORY))
		{
			this.category = stream;
		}
		else if(field.equals(INDEXFIELD.LINK))
		{
			this.link = stream;
		}
		else
		{
			System.err.println("invalid stream:");
		}
	}
	
	/**
	 * Method to return the stream for a given field
	 * @param key: The field for which the stream is requested
	 * @return The underlying stream if the key exists, null otherwise
	 */
	public TokenStream getStream(INDEXFIELD key) {
		//TODO: Implement this method
		switch(key)
		{
		case AUTHOR:
		{
			return author;
		}
		case TERM:
		{
			return term;
		}
		case LINK:
		{
			return link;
		}
		case CATEGORY:
		{
			return category;
		}
		}
		return null;
	}
	public void setId(String id)
	{
		//System.out.println("here");
		this.doc_id = id.toLowerCase();
	}
	
	/**
	 * Method to return a unique identifier for the given document.
	 * It is left to the student to identify what this must be
	 * But also look at how it is referenced in the indexing process
	 * @return A unique identifier for the given document
	 */
	public String getDocumentIdentifier() {
		//TODO: Implement this method
		//System.out.println("DocID: "+ doc_id);
		return doc_id;
	}
	
	
}
