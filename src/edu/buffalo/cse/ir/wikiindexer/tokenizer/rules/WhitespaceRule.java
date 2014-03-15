package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;


import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;

@RuleClass(className = RULENAMES.WHITESPACE)
public class WhitespaceRule implements TokenizerRule
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
					String[] value = removeSpaces(token);
					if(value!=null)
					{
						stream.previous();
						stream.set(value);
						//String[] a= this.removeSpaces(token);
						//System.out.println(this.filterHypens(token));
//						for (int i = 0; i < a.length; i++) {
//							
//							System.out.println(a[i]);
//						}
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
	public  String[] removeSpaces(String unfilteredText)
	{
		String[] result=null;
		unfilteredText= unfilteredText.replaceAll("^\\s+","");
		unfilteredText= unfilteredText.replaceAll("\\s+$","");
		result=unfilteredText.split("[\\s]+");

		return result;	

	}


	public static void main(String[] args) {
		//	System.out.println(AccentRule.removeAccents("The urban counterpart of château is palais"));
	/*	String[] a=WhitespaceRule.removeSpaces("this "
								+ "is \r"
								+ "a \n test");
		for (int i = 0; i < a.length; i++) {
			System.out.println(a[i]);

		}
		*/

	}
}
