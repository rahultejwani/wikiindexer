package edu.buffalo.cse.ir.wikiindexer.tokenizer.rules;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenStream;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.TokenizerException;
import edu.buffalo.cse.ir.wikiindexer.tokenizer.rules.TokenizerRule.RULENAMES;
/*
 * My Date doesn't rule   
 * 
 */
@RuleClass(className = RULENAMES.DATES)
public class DateRule implements TokenizerRule{
	static HashMap<String, Integer> ccMap= new HashMap<String, Integer>(); 
	@Override
	public void apply(TokenStream stream) throws TokenizerException {
		if (stream != null) {
			Collection<String> text = stream.getAllTokens();
			boolean isArr = true;
			if(text.size() ==1)
				isArr = false;
			StringBuilder sb = new StringBuilder();
		    String sep = "";
		    for(String s: text) {
		        sb.append(sep).append(s);
		        sep = " ";
		    }
			String token = sb.toString();
			while(stream.hasNext())
				stream.remove();
			fillCC();

			stream.reset();
			if(token != null)
			{
				//System.out.println(testLogic(token+"   ").trim());
				String retStr = testLogic(token+"   ").trim();

			    if(isArr)
			    {
					String[] strAll = retStr.split(" ");
					for (int i = 0; i < strAll.length; i++) {
						strAll[i] = strAll[i].replaceAll("(\\d+)_(\\d+)", "$1 $2");
					}
			    	stream.append(strAll);
			    }
			    else
			    	stream.append(retStr.replaceAll("_", " "));
			}	
		}
	}
	private static String testLogic(String input)
	{
		String[] spcTokens = input.split(" ");
		String output = new String(input);

		Pattern p;
		Matcher m;
		int count=0,min=0,max=0;
		String passtext="";
		for (int i = 0; i < spcTokens.length; i++) {
			int foundCode = DateRule.AlertDate(spcTokens[i].replaceAll("[\\.]", ""));
			count=0;
			if(foundCode == 1000){
				continue;
			}

			for (int j = i-3; j <= i+3; j++) {
				if(j>=0 && j<spcTokens.length)
				{
					passtext = passtext+" "+spcTokens[j];
					count++;
				}

			}
			int hour=0,minute=0,second=0;
			String[] time;
			if(foundCode<=24)//Month
			{
				//Try to find date and year
				int date=1,year=1900,month=(foundCode+1)/2;
				int num;
				p = Pattern.compile("\\d+");
				m = p.matcher(passtext);
				while (m.find()) {
					num = Integer.parseInt(m.group());
					if(num>=1 && num <=31)
					{
						date = num;
					}
					else
						year = num;
				}
				output = output.replaceFirst("[^\\d]\\d?\\d?\\d?\\d,*[^\\d]*"+spcTokens[i]+".*?\\d(,*)\\s+([^\\d])", " "+ String.format("%04d%02d%02d",year,month,date)+"$1 $2");
				output = output.replaceFirst(spcTokens[i]+".*?\\d(,*)\\s+([^\\d])", String.format("%04d%02d%02d",year,month,date)+"$1 $2");
			}
			if(foundCode == 25)
			{
				int date=1,year=1900,month=1;
				int num;

				p = Pattern.compile("\\d+");//\\d+:?\\d+
				m = p.matcher(passtext);
				while (m.find()) {
					if(min > m.start() || min == 0)
						min = m.start();
					if(max < m.end()||max == 0)
						max = m.end();
					num = Integer.parseInt(m.group());
					if(num>=1 && num <=31)
					{
						date = num;
					}
					else
						year = num;
				}
				output = output.replaceFirst("\\s+\\d+,*[^\\d]*[Aa][Dd]", " "+String.format("%04d%02d%02d",year,month,date));
			}
			if(foundCode == 26 )
			{
				int date=1,year=1900,month=1;
				int num;

				p = Pattern.compile("\\d+");//\\d+:?\\d+
				m = p.matcher(passtext);
				while (m.find()) {
					if(min > m.start() || min == 0)
						min = m.start();
					if(max < m.end()||max == 0)
						max = m.end();
					num = Integer.parseInt(m.group());
					if(num>=1 && num <=31)
					{
						date = num;
					}
					else
						year = num;
				}
				output = output.replaceFirst("\\s+\\d+,*[^\\d]*[Bb][Cc]", " "+String.format("-%04d%02d%02d",year,month,date));
			}
			if(foundCode == 100)
			{
				int date=1,year=1900,month=1;
				int num;

				p = Pattern.compile("\\d+");//\\d+:?\\d+
				m = p.matcher(passtext);
				while (m.find()) {
					if(min > m.start() || min == 0)
						min = m.start();
					if(max < m.end()||max == 0)
						max = m.end();
					num = Integer.parseInt(m.group());
					if(num>=1 && num <=31)
					{
						date = num;
					}
					else
						year = num;
				}
				output = output.replaceFirst("\\s+\\d\\d\\d\\d,*\\s+", " "+String.format("%04d%02d%02d",year,month,date)+" ");
			}

			if(foundCode == 27 ||foundCode == 101|| foundCode ==102)
			{
				p = Pattern.compile("\\d+:\\d+:\\d+|\\d+:*\\d+");
				m = p.matcher(passtext);
				while (m.find()) 
				{
					time = m.group().split(":");
					if(time.length == 1)
					{
						hour = Integer.parseInt(time[0])%12;
					}
					if(time.length == 2)
					{
						hour = Integer.parseInt(time[0]);
						minute = Integer.parseInt(time[1])%60;
					}
					if(time.length == 3)
					{
						hour = Integer.parseInt(time[0])%12;
						minute = Integer.parseInt(time[1])%60;
						second = Integer.parseInt(time[2])%60;
					}
				}
				output = output.replaceFirst("\\d?\\d:\\d?\\d?:*\\d+\\s*[Aa]*[Mm]*", String.format("%02d:%02d:%02d",hour,minute,second));
			}
			if(foundCode == 28 )
			{
				p = Pattern.compile("\\d+:\\d+:\\d+|\\d+:\\d+");
				m = p.matcher(passtext);
				while (m.find()) 
				{
					time = m.group().split(":");
					if(time.length == 1)
					{
						hour = Integer.parseInt(time[0]);
					}
					if(time.length == 2)
					{
						hour = Integer.parseInt(time[0]);

						minute = Integer.parseInt(time[1]);
					}
					if(time.length == 3)
					{
						hour = Integer.parseInt(time[0]);
						minute = Integer.parseInt(time[1]);
						second = Integer.parseInt(time[2]);
					}
					hour+=12;
				}

				output = output.replaceFirst("\\d+:\\d?\\d?:*\\d+\\s*[Pp]*[Mm]*", String.format("%02d:%02d:%02d",hour,minute,second));
			}
			if(foundCode == 103 )
			{
				String[] years = spcTokens[i].split("[^\\d]");
				String charac = spcTokens[i].replaceAll("[\\d\\.]", "");
				int date=1,year=1900,year2=1900,month=1;
				year = Integer.parseInt(years[0]);
				year2 = Integer.parseInt(years[1])+(year/100)*100;
				output = output.replaceFirst(spcTokens[i].replaceAll("\\.",""),String.format("%04d%02d%02d%s%04d%02d%02d",year,month,date,charac,year2,month,date));
			}
			passtext = "";
			count=0;
		}
		output = output.replaceAll("(\\d\\d:\\d\\d:\\d\\d).*\\s(\\d\\d\\d\\d\\d\\d\\d\\d)", "$2_$1");
		return output; 
	}

	private static int AlertDate(String token) {
		String[] spcTokens = token.split(" ");
		String[] numMatch = {"\\d\\d\\d\\d","[\\d]+:[\\d]+:[\\d]+","[\\d]+:[\\d]+","\\d\\d\\d\\d[^\\d]\\d\\d"};
		Pattern p; //Pattern
		Matcher m;
		for (int i = 0; i < spcTokens.length; i++) {
			if(ccMap.containsKey(spcTokens[i].replaceAll("[^a-zA-Z]", "").toLowerCase()))
			{
				return ccMap.get(spcTokens[i].replaceAll("[^a-zA-Z]", "").toLowerCase());
			}
			for (int j = 0; j < numMatch.length; j++) {
				p =Pattern.compile(numMatch[j]);
				m = p.matcher(spcTokens[i]);
				if(m.matches())
					return 100+j;
			}


		}
		return 1000;
	}
	private static void fillCC() {
		String[] CommonCotractions = {"January","Jan","February","Feb","March","Mar","April","Apr","May","May","June","Jun","July","Jul","August","Aug","September","Sep","October","Oct","November","Nov","December","Dec","AD","BC","AM","PM"};
		for (int i = 0; i < CommonCotractions.length; i++) {
			ccMap.put(CommonCotractions[i].toLowerCase(), i+1);
		}
	}

	
}