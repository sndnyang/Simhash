package fileclass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.wltea.analyzer.dic.Dictionary;

public class TextContent {

	private static ArrayList<String> firstMovieName = new ArrayList<String>();
	private static ArrayList<String> secondMovieName = new ArrayList<String>();

	private static ArrayList<String> firstTime = new ArrayList<String>();
	private static ArrayList<String> secondTime = new ArrayList<String>();

	private static ArrayList<String> firstPersonName = new ArrayList<String>();
	private static ArrayList<String> secondPersonName = new ArrayList<String>();

	private static String firstPlace = null;
	private static String secondPlace = null;

	private static final String URLPLACE = "/org/wltea/analyzer/sample/placename.dic";
	private static final String URLNICK = "/org/wltea/analyzer/sample/nickname.dic";
	private static ArrayList<String> placeName = new ArrayList<String>();
	private static ArrayList<String> nickName = new ArrayList<String>();

	public void getplacename() {
		// 读取地点词典文件
		InputStream is = Dictionary.class.getResourceAsStream(URLPLACE);
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
					placeName.add(theWord);
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

	public void getNickname() {
		// TODO Auto-generated method stub
		// 读取昵称文件
		InputStream is = Dictionary.class.getResourceAsStream(URLNICK);
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
					nickName.add(theWord);
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

	public void getMovieName(String strfirst, String strsecond) {
		// TODO Auto-generated method stub

		Pattern p = Pattern.compile("《(.*?)》");
		Matcher m = p.matcher(strfirst);
		while (m.find()) {
			if (!firstMovieName.contains(m.group(1)))
				firstMovieName.add(m.group(1));
		}

		Matcher ms = p.matcher(strsecond);
		while (ms.find()) {
			if (!secondMovieName.contains(ms.group(1)))
				secondMovieName.add(ms.group(1));
		}

		Pattern pTime = Pattern.compile("[0-9]{1,2}[月][0-9]{1,2}[日]");
		Matcher mTime = pTime.matcher(strfirst);
		while (mTime.find()) {
			if (!firstTime.contains(mTime.group(0))) {
				firstTime.add(mTime.group(0));
			}
		}

		Matcher mTimes = pTime.matcher(strsecond);
		while (mTimes.find()) {
			if (!secondTime.contains(mTimes.group(0))) {
				secondTime.add(mTimes.group(0));
			}
		}

		for (String item : placeName) {
			if (strfirst.contains(item)) {
				firstPlace = item;
				break;
			}
		}

		for (String item : placeName) {
			if (strsecond.contains(item)) {
				secondPlace = item;
				break;
			}
		}
	}

	public ArrayList<String> getItem(String string, int i) {
		// TODO Auto-generated method stub
		if (string.equals("movie")) {
			if (i == 1)
				return firstMovieName;
			else
				return secondMovieName;
		} else if (string.equals("person")) {
			if (i == 1)
				return firstPersonName;
			else
				return secondPersonName;
		} else if (string.equals("time")) {
			if (i == 1)
				return firstTime;
			else
				return secondTime;
		}
		return null;
	}

	public String getPlace(int i) {
		// TODO Auto-generated method stub
		if (i == 1)
			return firstPlace;
		else if (i == 2)
			return secondPlace;
		return null;
	}
}
