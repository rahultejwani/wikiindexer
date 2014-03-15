/**
 * 
 */
package edu.buffalo.cse.ir.wikiindexer.tokenizer;

import java.util.Properties;

import edu.buffalo.cse.ir.wikiindexer.indexer.INDEXFIELD;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.Tokenizer;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.AccentRule;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.ApostropheRule;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.CapitalizationRule;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.DateRule;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.EnglishStemmer;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.HyphenRule;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.NumberRule;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.PunctuationRule;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.SpecialCharRule;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.StopWordsRule;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.WhitespaceRule;

/**
 * Factory class to instantiate a Tokenizer instance
 * The expectation is that you need to decide which rules to apply for which field
 * Thus, given a field type, initialize the applicable rules and create the tokenizer
 * @author nikhillo
 *
 */
public class TokenizerFactory {
	//private instance, we just want one factory
	private static TokenizerFactory factory;

	//properties file, if you want to read soemthing for the tokenizers
	private static Properties props;

	/**
	 * Private constructor, singleton
	 */
	private TokenizerFactory() {
		//TODO: Implement this method
	}

	/**
	 * MEthod to get an instance of the factory class
	 * @return The factory instance
	 */
	public static TokenizerFactory getInstance(Properties idxProps) {
		if (factory == null) {
			factory = new TokenizerFactory();
			props = idxProps;
		}

		return factory;
	}

	/**
	 * Method to get a fully initialized tokenizer for a given field type
	 * @param field: The field for which to instantiate tokenizer
	 * @return The fully initialized tokenizer
	 */
	public Tokenizer getTokenizer(INDEXFIELD field) {
		//TODO: Implement this method
		/*
		 * For example, for field F1 I want to apply rules R1, R3 and R5
		 * For F2, the rules are R1, R2, R3, R4 and R5 both in order
		 * So the pseudo-code will be like:
		 * if (field == F1)
		 * 		return new Tokenizer(new R1(), new R3(), new R5())
		 * else if (field == F2)
		 * 		return new TOkenizer(new R1(), new R2(), new R3(), new R4(), new R5())
		 * ... etc
		 */
		if(field.equals(INDEXFIELD.AUTHOR))
		{
			try {
				return new Tokenizer(new AccentRule());

			}
			catch (TokenizerException e) {
				System.out.println(e);
				//e.printStackTrace();
			}
		}
		else if(field.equals(INDEXFIELD.TERM))
		{
			try {
				return new Tokenizer(new NumberRule(),new WhitespaceRule(),new StopWordsRule(),new CapitalizationRule(),
						new AccentRule(),new PunctuationRule(), new ApostropheRule(),
						new HyphenRule(),new EnglishStemmer());

			}
			catch (TokenizerException e) {
				System.out.println(e);
				//e.printStackTrace();
			}
		}
		else if(field.equals(INDEXFIELD.CATEGORY))
		{
			try {
				return new Tokenizer(new WhitespaceRule(),new StopWordsRule(),new CapitalizationRule(),
						new AccentRule(),new PunctuationRule(), new ApostropheRule(),
						new HyphenRule(),new NumberRule());
			}
			catch (TokenizerException e) {
				System.out.println(e);
				//e.printStackTrace();
			}

		}
		else if(field.equals(INDEXFIELD.LINK))
		{
			try {
				return new Tokenizer(new WhitespaceRule());

			}
			catch (TokenizerException e) {
				System.out.println(e);
				//e.printStackTrace();
			}

		}
		else
		{
			System.err.println("Type not specified:");
		}
		return null;
	}
}
