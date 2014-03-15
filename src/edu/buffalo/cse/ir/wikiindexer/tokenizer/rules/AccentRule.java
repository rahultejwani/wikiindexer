package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;

import java.text.Normalizer;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.ACCENTS)
public class AccentRule implements TokenizerRule
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
					String value = removeAccents(token);

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
	public String removeAccents(String unNormalizeText)
	{
		String normalizedText=null;
		if(!Normalizer.isNormalized(unNormalizeText, Normalizer.Form.NFD))
		{
			//unNormalizeText = unNormalizeText.toLowerCase();
			normalizedText  = Normalizer.normalize(unNormalizeText, Normalizer.Form.NFD);
			normalizedText = normalizedText.replaceAll("[^\\p{ASCII}]", "");
		//	normalizedText = normalizedText.replaceAll("а̀", "a");
		//	normalizedText = normalizedText.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		//	normalizedText = normalizedText.replaceAll(regex, replacement)
					//System.out.println(NormalizeText);
		}
		else
		{
			normalizedText = unNormalizeText;
		}

		return normalizedText;
	}


	public static void main(String[] args) {
	//	System.out.println(AccentRule.removeAccents("The urban counterpart of château is palais"));
	}
}