package edu.buffalo.cse.ir.wikiindexer.indexer;

import java.util.HashMap;


public class Postings {
	private HashMap<Integer,Integer> DocFreq;//will have sorted map 
	
	public Postings() {
		DocFreq = new HashMap<Integer, Integer>();
	}
	public void addDocument(int documentID, int frequency)
	{
		DocFreq.put(documentID,frequency);
	}
	public HashMap<Integer, Integer> getDocFreq() {
		return DocFreq;
	}
}