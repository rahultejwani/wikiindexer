package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import java.text.Normalizer;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.NUMBERS)
public class NumberRule implements TokenizerRule
{ 
	@Override
	public void apply(TokenStream stream) throws TokenizerException
	{
		if (stream != null) {
			String token;
			stream.reset();
			//Collection<String> tokenArray = stream.getAllTokens();
			//stream.previous();
			while (stream.hasNext()) {
				token = stream.next();
				if(token != null && !token.equals(""))
				{
					String value = removeNumbers(token);
					if(value!=null )
					{
						stream.previous();
						stream.set(value);
						//System.out.println(this.filterHypens(token));
					}
					else if(this.removeNumbers(token).equals(""))
					{
						//System.out.println("remove loop:");
						stream.previous();
						stream.remove();
					}

				}	
				stream.next();
			}
			stream.reset();
		}
		

	}
	public String removeNumbers(String unfilteredText)
	{
		
		unfilteredText = unfilteredText.replaceAll("\\d+[,.]\\d+", "");
		unfilteredText = unfilteredText.replaceAll("\\d+", "");
		unfilteredText = unfilteredText.replaceAll("\\s+", " ");
	//	unfilteredText = unfilteredText.replaceAll("^\\s[^\\w\\d]+", "");
	/*	Matcher m = Pattern.compile("^[^\\d\\w]+$").matcher(unfilteredText);
		while(m.find())
		{ 
			System.out.println("Pattern find:");
			if(m.group(1).equals("") && m.group(2).equals(""))
			{
				m.replaceAll("");
				System.out.println("remove dots");
			}
		}  */
	//	unfilteredText = unfilteredText.replaceAll("[^\\d\\w\\s]+", "");
		
		//System.out.println(unfilteredText);
		return unfilteredText;
	}


	public static void main(String[] args) {
	//	System.out.println(AccentRule.removeAccents("The urban counterpart of château is palais"));
	}
}