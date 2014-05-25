package controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Constant.Url;

public class RegexParser {
	private final String itemParse = "([A-Za-z0-9-]*)$";
	private final String staticParse = "([A-Za-z0-9._%+-\\/]*)";
	public RegexParser(){};
	
	public String getStaticUrl(String url, String txt){
		Pattern regex = Pattern.compile(url + staticParse);
		Matcher m = regex.matcher(txt);
		//capturem l'arxiu
		String matched = null;
		while (m.find()) {
			if(!m.group(1).equals("")){
				matched= m.group(1);
			}
		}
		return matched;
	}
	public String getItemName(String txt){
		Pattern regex = Pattern.compile(itemParse);
		Matcher m = regex.matcher(txt);
		//capturem l'arxiu
		String matched = null;
		
		while (m.find()) {
			if(!m.group(1).equals("")){
				matched= m.group(1);
			}
		}
		return matched;
		
	}
}
