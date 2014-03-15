package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;
@RuleClass(className = RULENAMES.PUNCTUATION)
public class PunctuationRule implements TokenizerRule {
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
					stream.set(this.filterPunctuations(token));
				}	
				stream.next();
			}
			stream.reset();
		}
	}
	public String filterPunctuations(String textWithPunctuations)
	{
		textWithPunctuations=textWithPunctuations.replaceAll("[?!,;\\.]+($)", "$1");
		textWithPunctuations=textWithPunctuations.replaceAll("[?!,;\\.]+(\\s)", "$1");
		textWithPunctuations=textWithPunctuations.replaceAll("(\\s)[?!,;\\.]+", "$1");
		return textWithPunctuations;
	}
}