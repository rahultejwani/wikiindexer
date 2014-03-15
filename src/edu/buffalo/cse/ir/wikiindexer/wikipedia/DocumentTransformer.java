/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.wikipedia;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.Tokenizer;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.wikipedia.WikipediaDocument.Section;


public class DocumentTransformer implements Callable<IndexableDocument> {
	/**
	 * Default constructor, DO NOT change
	 * @param tknizerMap: A map mapping a fully initialized tokenizer to a given field type
	 * @param doc: The WikipediaDocument to be processed
	 */
	Map<INDEXFIELD,Tokenizer> tokenmap=null;
	WikipediaDocument wd = null;
	IndexableDocument id = null;
	public DocumentTransformer(Map<INDEXFIELD, Tokenizer> tknizerMap, WikipediaDocument doc) {
		//TODO: Implement this method
		this.tokenmap = tknizerMap;
		this.wd = doc;
		try{
			id=call();
		}
		catch(Exception e)
		{
			System.err.println(e);
		}
	}

	/**
	 * Method to trigger the transformation
	 * @throws TokenizerException Inc ase any tokenization error occurs
	 */
	public IndexableDocument call() throws TokenizerException {
		// TODO Implement this method
		IndexableDocument temp= new IndexableDocument();
		for (Map.Entry<INDEXFIELD,Tokenizer> entry : tokenmap.entrySet()) {
			//System.out.println(entry.getKey());
			
			if(entry.getKey().equals(INDEXFIELD.TERM))
			{
				try {
					temp.setId(wd.getTitle().replaceAll("\\s+", "_"));
					StringBuilder sb = new StringBuilder();
					for (Iterator<Section> iterator = wd.getSections().iterator(); iterator
							.hasNext();) {
						Section type = (Section) iterator.next();
						sb.append(type.getTitle() +" ");
						sb.append(type.getText() +" ");
						
					}
					//System.out.println(sb.toString());
					TokenStream ts = new TokenStream(sb);
					//System.out.println(ts.toString());
					entry.getValue().tokenize(ts);
					temp.addField(entry.getKey(), ts);
				
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					//System.err.println(e);
				}
			}
			else if(entry.getKey().equals(INDEXFIELD.AUTHOR))
			{
				try {
					temp.setId(wd.getTitle().replaceAll("\\s+", "_"));
					TokenStream ts = new TokenStream(wd.getAuthor());
					entry.getValue().tokenize(ts);
					temp.addField(entry.getKey(), ts);
				
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
			}
			else if(entry.getKey().equals(INDEXFIELD.CATEGORY))
			{
				try {
					temp.setId(wd.getTitle().replaceAll("\\s+", "_"));
					TokenStream ts = new TokenStream(wd.getCategories());
					entry.getValue().tokenize(ts);
					temp.addField(entry.getKey(), ts);
				
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
			else if(entry.getKey().equals(INDEXFIELD.LINK))
			{
				//System.out.println("Here I am");
				try {
					temp.setId(wd.getTitle().replaceAll("\\s+", "_"));
					TokenStream ts = new TokenStream(wd.getLinks());
					entry.getValue().tokenize(ts);
					temp.addField(entry.getKey(), ts);
				
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
			else
			{

				System.err.println("Indexfied is nor specified");
				return null;
			}
		}
		return temp;
	}

}
