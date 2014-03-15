/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.tokenizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;


/**
 * This class represents a stream of tokens as the name suggests.
 * It wraps the token stream and provides utility methods to manipulate it
 * @author nikhillo
 *
 */
public class TokenStream implements Iterator<String>{

	/**
	 * Default constructor
	 * @param bldr: THe stringbuilder to seed the stream
	 */
	List<String> result = new ArrayList<String>();

	ListIterator<String> it;
	public TokenStream(StringBuilder bldr) {
		//TODO: Implement this method
		String s=bldr.toString();
		if (s!=null&&s!="")
			result.add(bldr.toString());
		else
			result=null;

	}

	/**
	 * Overloaded constructor
	 * @param bldr: THe stringbuilder to seed the stream
	 */
	public TokenStream(String string) {
		//TODO: Implement this method
		if(string!=null&&string!="")
			result.add(string);
		else 
			result = null;

	}
	/**
	 * Overloaded constructor
	 * @param array: THe ArrayList to seed the stream
	 */
	public TokenStream(List<String> array) {
		//TODO: Implement this method
		this.result = array;

	}


	public TokenStream(Set<String> links) {
		// TODO Auto-generated constructor stub
		result = new ArrayList<String>(links); 
	}

	/**
	 * Method to append tokens to the stream
	 * @param tokens: The tokens to be appended
	 */
	public void append(String... tokens) {
		//TODO: Implement this method
		if(tokens!=null)
		{
			for(String s: tokens)
			{
				if(s!=null&&s!="")
				{
					result.add(s);
				}
			}
		}

	}

	/**
	 * Method to retrieve a map of token to count mapping
	 * This map should contain the unique set of tokens as keys
	 * The values should be the number of occurrences of the token in the given stream
	 * @return The map as described above, no restrictions on ordering applicable
	 */
	public Map<String, Integer> getTokenMap() {
		//TODO: Implement this method
		Map<String,Integer> map = new HashMap<String,Integer>();
		try{
			if(result.toArray()==null || result.toArray().equals(""))
				return null;
			
	
		int count=1;
		
		for (String s : result) 
			if (map.containsKey(s))
			{
				map.put(s, map.get(s) + 1);
			}
			else
				map.put(s,count);
		
		}
		catch(Exception e)
		{
			return null;
		}
		return map;
	}

	/**
	 * Method to get the underlying token stream as a collection of tokens
	 * @return A collection containing the ordered tokens as wrapped by this stream
	 * Each token must be a separate element within the collection.
	 * Operations on the returned collection should NOT affect the token stream
	 */
	public Collection<String> getAllTokens() {
		//TODO: Implement this method
		if (result==null)
			return null;
		return result;
	}

	/**
	 * Method to query for the given token within the stream
	 * @param token: The token to be queried
	 * @return: THe number of times it occurs within the stream, 0 if not found
	 */
	public int query(String token) {
		//TODO: Implement this method
		int count=0;
		try{


			if(token==null||token.equals(""))
				return 0;

			for(String s: result)
			{
				if(s.equals(token))
					count++;
			}
		}
		catch(Exception e)
		{
			return 0;
		}


		return count;
	}

	/**
	 * Iterator method: Method to check if the stream has any more tokens
	 * @return true if a token exists to iterate over, false otherwise
	 */
	public boolean hasNext() {
		// TODO: Implement this method
		boolean b = false;
		try
		{
			if(it == null)
			{
				it = result.listIterator();
			}

			b=it.hasNext();
		}
		catch(Exception e)
		{

			return false;
		}
		return b;

	}

	/**
	 * Iterator method: Method to check if the stream has any more tokens
	 * @return true if a token exists to iterate over, false otherwise
	 */
	public boolean hasPrevious() {
		//TODO: Implement this method
		//	ListIterator<String> it=result.listIterator();
		boolean b = false;
		try
		{
			if(it == null)
			{
				it = result.listIterator();

			}
			b=it.hasPrevious();
		}
		catch (Exception e)
		{
			return false;
		}

		return b;




	}

	/**
	 * Iterator method: Method to get the next token from the stream
	 * Callers must call the set method to modify the token, changing the value
	 * of the token returned by this method must not alter the stream
	 * @return The next token from the stream, null if at the end
	 */
	public String next() {
		// TODO: Implement this method
		String s=new String();

		try{
			if(it == null)
			{
				it = result.listIterator();
			}
			s = it.next();
			//	System.out.println(s);
		}
		catch(Exception e)
		{
			return null;
		}
		return s;
	}

	/**
	 * Iterator method: Method to get the previous token from the stream
	 * Callers must call the set method to modify the token, changing the value
	 * of the token returned by this method must not alter the stream
	 * @return The next token from the stream, null if at the end
	 */
	public String previous() {
		//TODO: Implement this method

		String s=new String();

		try{
			if(it == null)
			{
				it = result.listIterator();
			}
			s = it.previous();
			//	System.out.println(s);
		}
		catch(Exception e)
		{
			return null;
		}
		return s;
	}

	/**
	 * Iterator method: Method to remove the current token from the stream
	 */
	public void remove() {
		// TODO: Implement this method

		//	ListIterator<String> it = result.listIterator();
		if(result!=null)
		{
			if(it == null)
			{
				it = result.listIterator();
			}
			try{
				it.next();
				it.remove();
			//	it.previous();
			}
			catch (Exception e)
			{
				//System.err.println(e);
				//result = null;
				//	it = null;
				//this.seekEnd();
			}


		}



	}

	/**
	 * Method to merge the current token with the previous token, assumes whitespace
	 * separator between tokens when merged. The token iterator should now point
	 * to the newly merged token (i.e. the previous one)
	 * @return true if the merge succeeded, false otherwise
	 */
	public boolean mergeWithPrevious() {
		//TODO: Implement this method
		try
		{
			it.next();
			String current=it.previous();
			String prev;
			if(current!=null)
			{
				prev=it.previous();
				it.next();
				//this.previous();
				if(prev!=null)
				{
					it.set(prev+" "+current);
					it.next();
					it.remove();
					it.previous();
					return true;  
				}
			}
		}
		catch (Exception e)
		{

			return false;
		}
		return false;


	}

	/**
	 * Method to merge the current token with the next token, assumes whitespace
	 * separator between tokens when merged. The token iterator should now point
	 * to the newly merged token (i.e. the current one)
	 * @return true if the merge succeeded, false otherwise
	 */
	public boolean mergeWithNext() {
		//TODO: Implement this method
		try
		{
			if(it == null)
			{
				it = result.listIterator();
			}

			//it.next();
			//it.next();
			String current=it.next();
			String next;
			//System.out.println(current);
			//it.previous();
			//this.reset();

			if(current!=null)
			{
				next=it.next();

				//it.next();
				//System.out.println(next+current);

				if(next!=null)
				{
					//it.previous();
					//	it.next();
					//System.out.println(next+current);
					it.set(current+" "+next);
					it.previous();
					it.previous();	
					it.remove();
					//	it.next();
					//it.previous();
					//	it.next();
					return true;  
				}
			}
		}
		catch (Exception e)
		{
			//System.err.println(e);
			return false;
		}
		return false;

	}

	/**
	 * Method to replace the current token with the given tokens
	 * The stream should be manipulated accordingly based upon the number of tokens set
	 * It is expected that remove will be called to delete a token instead of passing
	 * null or an empty string here.
	 * The iterator should point to the last set token, i.e, last token in the passed array.
	 * @param newValue: The array of new values with every new token as a separate element within the array
	 */
	public void set(String... newValue) {
		//TODO: Implement this method
		try{
			if(it == null)
			{
				it= result.listIterator();
			}
			if(newValue[0]!=null && !newValue[0].equals("")){


				if(result!=null)

				{

				//	System.out.println("Value "+ newValue[0]);
					//String first=newValue[0];
					it.next();
					it.set(newValue[0]);
					//.out.println(newValue[0]);
					int index=0;
					index=it.nextIndex()-1;
					it.previous();
					for(int i=1;i<newValue.length;i++)
					{
						if(newValue[i]!=null && newValue[i]!="")
						{
							//System.out.println("check");
							result.add(index + i, newValue[i]);

						}
					}
					it=result.listIterator(index+newValue.length-1);
					//it.previous();

				}



			}
			else
			{
			//	System.out.println("I am here");
				if(it!=null)
				{
					it.previous();
					it.remove();
					it.next();
				}

			}
			//it.next();
		}
		catch(Exception e)
		{

		}

	}

	/**
	 * Iterator method: Method to reset the iterator to the start of the stream
	 * next must be called to get a token
	 */
	public void reset() {
		//TODO: Implement this method
		try
		{
			it=result.listIterator(0);
		}
		catch(Exception e)
		{

		}
	}

	/**
	 * Iterator method: Method to set the iterator to beyond the last token in the stream
	 * previous must be called to get a token
	 */
	public void seekEnd() {
		try
		{
			if(it==null)
				it = result.listIterator();
			it=result.listIterator(result.size());
		}
		catch(Exception e)
		{

		}
		
	}

	/**
	 * Method to merge this stream with another stream
	 * @param other: The stream to be merged
	 */

	public void merge(TokenStream other) {
		//TODO: Implement this method
		try{
			if(other.getAllTokens().size()!=0 && other!=null)
			{
				//if(result!=null )
				if(result==null)

					result=new ArrayList<String>();

				result.addAll(other.getAllTokens());	
			}
		}
		catch(Exception e)
		{
			//	System.out.println("xlk");
			//	System.err.println(e);
		}

	}

	public static void main(String[] args) {
		TokenStream	stream = new TokenStream("this");
		stream.append("is","a","test","stream");
		//stream.reset();
		//stream.mergeWithNext();
		String a=null;
		//	a="cx ";
		TokenStream stream2 = new TokenStream(a);
		//stream.merge(stream2);
		stream2.merge(stream);
		//System.out.println(stream2.getAllTokens().toString());
		//	System.out.println(stream.next());
		//stream();
	      stream.reset();
		//stream.next();
		stream.set((String)null);
		System.out.println(stream.getAllTokens().toString());
		//System.out.println(stream.);
	}
}
