package edu.buffalo.cse.ir.wikiindexer.indexer;

import java.util.SortedMap;
import java.util.TreeMap;

public class PostingList {
	/*
	 * Postings list for a single document
	 */

	private SortedMap<String, Postings> postList;//will have sorted map
	PostingList() {
		postList = new TreeMap<String, Postings>();
	}
	public void add(String term, int DocId, int frequency)
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
	public SortedMap<String, Postings> getPostList() {
		return postList;
	}
}
