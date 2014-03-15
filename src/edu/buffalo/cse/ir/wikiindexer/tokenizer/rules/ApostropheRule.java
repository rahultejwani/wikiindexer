package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;
/*
 * Considering mostly this would be called at the time when all the tokens 
 * are well formed and most of the white spaces are removed
 */
@RuleClass(className = RULENAMES.APOSTROPHE)
public class ApostropheRule implements TokenizerRule{
	static HashMap<String, String> ccMap= new HashMap<String, String>(); 
	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		if (stream != null) {
			String token;
			stream.reset();
			while (stream.hasNext()) {
				token = stream.next();
				stream.previous();
				if(token != null)
				{
					stream.set(expandCommonContractions(token));
				}	
				stream.next();
			}
			stream.reset();
		}
	}
	public static String[] expandCommonContractions(String unfilteredText)
	{
		String tempCC;
		String expandedCC;
		StringBuilder tempUFT = new StringBuilder();
		ArrayList<String> retStr = new ArrayList<String>();
		int start=0;
		int end=0,oEnd=0;
		if(ccMap.size() ==0 )
		{
			fillCC();
		}
		Pattern p = Pattern.compile("([a-zA-Z]*'[a-zA-Z]+)");//Pattern
		Matcher m = p.matcher(unfilteredText);
		while(m.find())
		{
			tempCC = m.group(1);
			start = m.start();end=m.end();
			expandedCC = ccMap.get(tempCC);
			if(expandedCC == null || expandedCC.isEmpty())
			{
				expandedCC = getRestCC(tempCC);
				if(expandedCC.equals(tempCC))
				{
					tempUFT.append(unfilteredText.substring(oEnd,start));
					tempUFT.append(tempCC);
				}
				else
				{
					if(!expandedCC.split(" ")[0].equals(""))
						retStr.add(unfilteredText.substring(oEnd,start)+expandedCC.split(" ")[0]);
					retStr.addAll(Arrays.asList(expandCommonContractions(expandedCC.split(" ")[1]+unfilteredText.substring(end,unfilteredText.length()))));
					return retStr.toArray(new String[retStr.size()]);
				}
			}
			else	
			{
				retStr.add(unfilteredText.substring(oEnd,start)+expandedCC.split(" ")[0]);
				if(end == -1) end = 0;
				try{
					retStr.addAll(Arrays.asList(expandCommonContractions(expandedCC.split(" ")[1]+unfilteredText.substring(end,unfilteredText.length()))));
				}
				catch(Exception e)
				{}
				return retStr.toArray(new String[retStr.size()]);
			}
			oEnd=end;
		}
		tempUFT.append(unfilteredText.substring(oEnd, unfilteredText.length()));
		unfilteredText = tempUFT.toString();
		unfilteredText=unfilteredText.replaceAll("[']+($)", "$1");
		unfilteredText=unfilteredText.replaceAll("[']+(\\s)", "$1");
		unfilteredText=unfilteredText.replaceAll("(\\s)[']+", "$1");
		unfilteredText=unfilteredText.replaceAll("'s", "");
		unfilteredText=unfilteredText.replaceAll("'", "");
		retStr.add(unfilteredText);
		return retStr.toArray(new String[retStr.size()]);
	}
	private static String getRestCC(String ccText)
	{
		String[] splitted = ccText.split("'");
		if(splitted[1].equals("em"))
		{
			return splitted[0] + " them"; 
		}
		else if(splitted[1].equals("ll"))
		{
			return splitted[0] + " will";
		}
		else if(splitted[1].equals("d"))
		{
			return splitted[0] + " would";
		}
		else if(splitted[1].equals("ve"))
		{
			return splitted[0] + " have";
		}
		return ccText;
	}
	private static void fillCC() {
		String[] CommonCotractions = {"aren't","are not","can't","cannot","couldn't","could not","didn't","did not","doesn't","does not","don't","do not","hadn't","had not","hasn't","has not","haven't","have not","he'd","he had","he'll","he will","he's","he is","I'd","I had","I'll","I will","I'm","I am","I've","I have","isn't","is not","it's","it is","let's","let us","mightn't","might not","mustn't","must not","shan't","shall not","she'd","she had","she'll","she will","she's","she is","shouldn't","should not","that's","that is","there's","there is","they'd","they had","they'll","they will","they're","they are","they've","they have","we'd","we had","we're","we are","we've","we have","weren't","were not","what'll","what will","what're","what are","what's","what is","what've","what have","where's","where is","who'd","who had","who'll","who will","who're","who are","who's","who is","who've","who have","won't","will not","wouldn't","would not","you'd","you had","you'll","you will","you're","you are","you've","you have"};
		for (int i = 0; i < CommonCotractions.length; i=i+2) {
			ccMap.put(CommonCotractions[i], CommonCotractions[i+1]);
		}
	}
}