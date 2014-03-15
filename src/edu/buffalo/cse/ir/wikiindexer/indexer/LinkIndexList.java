package edu.buffalo.cse.ir.wikiindexer.indexer;

import java.util.SortedMap;
import java.util.TreeMap;

public class LinkIndexList {
	/*
	 * Postings list for a single document
	 */
	private SortedMap<Integer, Postings> postList;//will have sorted map
	LinkIndexList() {
		postList = new TreeMap<Integer, Postings>();
	}
	
	public void add(Integer term, int DocId, int frequency)
	{
		if(postList.containsKey(term))
		{
			Postings p = postList.get(term);
			p.addDocument(DocId, frequency);
			postList.put(term, p);
		}
		else
		{
			Postings p = new Postings();
			p.addDocument(DocId, frequency);
			postList.put(term, p);
		}
	}
	public void add(Integer term, String DocId, int frequency)
	{
		//For now using key(term) as value also
		
		if(postList.containsKey(term))
		{
			Postings p = postList.get(term);
			p.addDocument(term, frequency);
			postList.put(term, p);
		}
		else
		{
			Postings p = new Postings();
			p.addDocument(term, frequency);
			postList.put(term, p);
		}
	}
	public SortedMap<Integer, Postings> getPostList() {
		return postList;
	}
}
