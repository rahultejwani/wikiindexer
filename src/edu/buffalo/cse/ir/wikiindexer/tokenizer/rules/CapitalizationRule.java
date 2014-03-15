package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.CAPITALIZATION)
public class CapitalizationRule implements TokenizerRule
{ 
static	boolean firstWord= true;

	@Override
	public void apply(TokenStream stream) throws TokenizerException
	{if (stream != null) {
		String token;
		stream.reset();
		//firstWord = 1;
		//Collection<String> tokenArray = stream.getAllTokens();
		//stream.previous();
		while (stream.hasNext()) {
			token = stream.next();
			if(token != null)
			{
				String value= capitalize(token);
				if(value!=null)
				{
					//firstWord=1;
					//String tempToken = this.capitalize(token);
					//	System.out.println(this.capitalize(token));
					stream.previous();
					stream.set(value);
					//firstWord = 0;
					//System.out.println(this.filterHypens(token));
				}
				else
				{
					stream.previous();
					stream.remove();
				}

			}	
			stream.next();
		}
		stream.reset();
		firstWord = true;
	}


	}
	public static String capitalize(String unfilteredText)
	{	
		/*	if(unfilteredText.matches("^\\w+\\."))
			firstWord= true;

		else
			firstWord = false;
		 */
		//System.out.println(firstWord);
		// boolean firstWord = true;
		//System.out.println(unfilteredText);
		Pattern p= Pattern.compile("[A-Z]+");
		Matcher m = p.matcher(unfilteredText);
		if(!m.find())
		{
			if(unfilteredText.matches("\\w+\\."))
				firstWord= true;

			else
				firstWord = false;
			//System.out.println("loop1:");
			return unfilteredText;
		}
		else
		{
			
			if (firstWord)
			{
								//firstWord=0;
				if(m.find(1))
				{
					//System.out.println("loop 2:");
					return unfilteredText;
				}
				if(Character.isUpperCase(unfilteredText.charAt(0)))
				{
					//firstWord = 0;
					//firstWord = false;
					//System.out.println("lowercase: ");
					if(unfilteredText.matches("^\\w+\\."))
						firstWord= true;

					else
						firstWord = false;
					//System.out.println("loop3:");

					return unfilteredText.toLowerCase();
				}
				else
				{
					//firstWord = false;
					if(unfilteredText.matches("^\\w+\\."))
						firstWord= true;

					else
						firstWord = false;
				//	System.out.println("loop4:");

					return unfilteredText;
				}
			}
			else
			{
				if(unfilteredText.matches("^\\w+\\."))
					firstWord= true;

				else
					firstWord = false;
				//System.out.println("loop5:");
				return unfilteredText;
			}
		}




		//	return unfilteredText.toLowerCase();
	}


	public static void main(String[] args) {
		String[] a= { "This" , "is" ,"a" ,"test.","San","Francisco", "is","in","California.","Hgavy"};
		for (int i = 0; i < a.length; i++) {
			//System.out.println(capitalize(a[i]));
		}
	//	System.out.println(capitalize(a));
		//	System.out.println(AccentRule.removeAccents("The urban counterpart of château is palais"));
	}
}