package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.HYPHEN)
public class HyphenRule implements TokenizerRule
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
				if(token != null)
				{	
					String value = filterHypens(token);
					
					if(value!=null)
						{
						stream.previous();
						stream.set(value);
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
		}
		
	}
	public String filterHypens(String textWithHyphens)
	{
		Pattern p=Pattern.compile("^(.*?)[\\-]+(.*?)$");
		Matcher m= p.matcher(textWithHyphens.toString());
		while(m.find())
		{
			String first = m.group(1);
			String last = m.group(2);
			//System.out.println("first: "+first);
			//System.out.println("Last: "+last);
			if(first.matches(".*\\d.*") || last.matches(".*\\d.*"))
			{
				return textWithHyphens;
			}
			else if(first.matches("\\s+") && !last.matches("\\s+"))
			{
				
				return last.trim();
			}
			else if(!first.matches("\\s+") && last.matches("\\s+"))
			{
				
				return first.trim();
			}
			else if(first.matches("\\s+") && last.matches("\\s+"))
			{
				
				return null;
			}
			else 
			{
				return (first + " " + last).trim();
			}
		}
		//textWithHyphens=textWithHyphens.replaceAll("", "");
		return textWithHyphens;
	}
}