package doc.dynamictanks.Utils;

import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	public static String parseCommas(String s, String preText, String postText) {
		String input = s;
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(input);
		NumberFormat nf = NumberFormat.getInstance();        
		StringBuffer sb = new StringBuffer();
		while(m.find()) {
			String g = m.group();
			m.appendReplacement(sb, nf.format(Double.parseDouble(g)));            
		}
		return preText + sb.toString() + postText;
	}
	
	public static String Cap(String input) {
		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}
	
	public static String lastName (String input) {
		String[] array = input.split(" ");
		return array[array.length - 1];
	}
	
}
