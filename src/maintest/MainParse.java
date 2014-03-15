package maintest;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import edu.buffalo.cse.ir.wikiindexer.FileUtil;
import edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD;
import edu.buffalo.cse.ir.wikiindexer.indexer.IndexWriter;
import edu.buffalo.cse.ir.wikiindexer.indexer.IndexerException;


public class MainParse {
	public static void main(String[] args) throws IndexerException {
		Properties props = loadProperties("./files/properties.config");
		IndexWriter iw = new IndexWriter(props, INDEXFIELD.AUTHOR, INDEXFIELD.LINK);
		iw.addToIndex("First", 3, 3);
		iw.addToIndex("Second", 4, 3);
		iw.addToIndex("Third", 4, 4);
		iw.addToIndex("Second", 1, 1);
		iw.addToIndex("Fourth", 2, 2);

		iw.writeToDisk();
		
	}
	private static Properties loadProperties(String filename) {

		try {
			Properties props = FileUtil.loadProperties(filename);
				return props;
		} catch (FileNotFoundException e) {
			System.err.println("Unable to open or load the specified file: " + filename);
		} catch (IOException e) {
			System.err.println("Error while reading properties from the specified file: " + filename);
		}
		
		return null;
	}
}

