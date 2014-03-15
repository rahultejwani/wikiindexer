package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;
/*
 * Should be called after all others rules
 * For now removing everything except @
 */
@RuleClass(className = RULENAMES.SPECIALCHARS)
public class SpecialCharRule implements TokenizerRule{ 
	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		ArrayList<String > y = new ArrayList<String>();
		if (stream != null) {
			String token;
			stream.reset();
			//Collection<String> tokenArray = stream.getAllTokens();
			//stream.previous();
			while (stream.hasNext()) {
				token = stream.next();
				if(token != null)
				{

					stream.previous();
					String[] x= removeSplChars(token);
					for(int i=0;i<x.length;i++){
						if(!x[i].trim().equals(""))
						{
							//System.out.print(x[i]+",");
							y.add(x[i]);
						}
					}
				}	
				stream.next();
			}
			stream.reset();
			while(stream.hasNext())
				stream.remove();
			stream.reset();
			if(y.size()!=0){
				String[] ins = y.toArray(new String[y.size()]);
				//System.out.println("YSIZE: "+ins.length);
				stream.append(ins);
			}
		} 
	}

	private static String[] removeSplChars(String unfilteredText) {

		unfilteredText=unfilteredText.replaceAll("_", " ");
		unfilteredText=unfilteredText.replaceAll("\\s+[^\\d\\w-]+\\s+", "");
		unfilteredText=unfilteredText.replaceAll("^[^\\d\\w-]+$", "");
		unfilteredText=unfilteredText.replaceAll("^[^\\d\\w]+(.*?)", "$1");
		unfilteredText=unfilteredText.replaceAll("(.*?)[^\\d\\w]+$", "$1");
		int start=0,end=0,oEnd=0;
		List<String> result = new ArrayList<String>();
		Pattern p=Pattern.compile("[^\\w\\d\\.-]+");
		//System.out.println("Unfiltered: "+ unfilteredText);
		Matcher m= p.matcher(unfilteredText.toString());
		while(m.find())
		{

			start = m.start();end=m.end();
			if(!m.group().equals("-") && !m.group().equals("."))
			{
				result.add(unfilteredText.substring(oEnd,start));
			//	System.out.println("yes "+m.group());

			}
			oEnd = end;
		}
		result.add(unfilteredText.substring(oEnd,unfilteredText.length()));
		unfilteredText=unfilteredText.replaceAll("[^\\w\\s\\d\\-@\\.]+", "");
		//System.out.println(unfilteredText);
		//result.add(unfilteredText);
		//	if(result.indexOf("")!=-1)
		//	result.remove(result.indexOf(""));
		return result.toArray(new String[result.size()]);
	}
	//return result.toArray(new String[result.size()]);

	public static void main(String[] args) {
		//
		String[] test = SpecialCharRule.removeSplChars("__/\\__");
		for (int i = 0; i < test.length; i++) {
		//	System.out.print(test[i]+",");
		}
		//System.out.println();
	}

}