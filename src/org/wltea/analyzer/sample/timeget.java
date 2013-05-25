package org.wltea.analyzer.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.wltea.analyzer.dic.Dictionary;

public class timeget {

	private static final String url = "/org/wltea/analyzer/sample/placename.dic";
	private static ArrayList<String> placename = new ArrayList<String>();

	private static void getplacename() {
		// 读取地点词典文件
		InputStream is = Dictionary.class.getResourceAsStream(url);
		if (is == null) {
			throw new RuntimeException("Quantifier Dictionary not found!!!");
		}

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is),
					512);
			String theWord = null;
			do {
				theWord = br.readLine();
				if (theWord != null && !"".equals(theWord.trim())) {
					placename.add(theWord);
				}
			} while (theWord != null);

		} catch (IOException ioe) {
			System.err.println("Quantifier Dictionary loading exception.");
			ioe.printStackTrace();

		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		String str = "本页面最后修订于2月14日 (星期二) 2015-13月35日 11:29。2012-135/14<br /></li>";
		Pattern p = Pattern.compile("[0-9]{1,2}[月][0-9]{1,2}[日]");
		Matcher m = p.matcher(str);

		while (m.find()) {
			System.out.println(m.group(0));
		}
		getplacename();
		System.out.println(placename);
	}

}