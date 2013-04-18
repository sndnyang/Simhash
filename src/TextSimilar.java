import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.sample.NewWord;
import org.wltea.analyzer.sample.others;
import org.wltea.analyzer.sample.paperSimilation.innerresult;

import fileclass.FileOperator;
import fileclass.TextContent;

public class TextSimilar {

	private static TextContent content = new TextContent();

	private static final String urlPlace = "/org/wltea/analyzer/sample/placename.dic";
	private static final String urlNick = "/org/wltea/analyzer/sample/nickname.dic";
	private static ArrayList<String> placeName = new ArrayList<String>();
	private static ArrayList<String> nickName = new ArrayList<String>();

	private static int numberitem = 0;
	private static int REPEATCOUNT = 3;
	private static String firstTop = "";
	private static String secondTop = "";
	private static int TESTALL = 0;
	private static int TESTUNSAMEALL = 0;
	private static int TESTSAME = 0;
	private static boolean ISSCAM = false;
	private static int TESTUNSAME = 0;
	private static String firstnick = null;
	private static String secondnick = null;

	TextSimilar() {

		TESTALL = 0;
		TESTSAME = 0;
		ISSCAM = false;
		
		content.getplacename();
		content.getNickname();
	}

	public static class innerresult {
		// �ؼ�������
		private int WordTotal;
		// �ؼ���ƥ�������
		private int WordMatch;

		// �ؼ�������(���ظ�)
		private int WordTotalUnrepeat;
		// �ؼ���ƥ�������(���ظ�)
		private int WordMatchUnrepeat;

		private boolean isweibo;

		innerresult(int oneinner, int twoinner, int totalUn, int matchUn) {
			WordTotal = oneinner;
			WordMatch = twoinner;
			// ��������2��������û���õ�
			WordTotalUnrepeat = totalUn;
			WordMatchUnrepeat = matchUn;
			isweibo = false;
		}

		void setWeiBo(boolean isWeiBo) {
			isweibo = isWeiBo;
		}

		boolean getisWeiBo() {
			return isweibo;
		}

		int getWordTotal() {
			return WordTotal;
		}

		int getWordMatch() {
			return WordMatch;
		}

		int getWordTotalUnrepeat() {
			return WordTotalUnrepeat;
		}

		int getWordMatchUnrepeat() {
			return WordMatchUnrepeat;
		}
	}

	/***
	 * �������������������Ҫ�ؼ��ֵ��ظ��Ĵ��� Ҳ����˵ һ�������ظ��˶��ٴβ����ǹؼ���
	 * 
	 * @param str
	 *            ��ʾ���µ��ַ���
	 * @author xujp
	 **/
	private static void computeRepeatCount(String str) {
		// System.out.println("���µ�����Ϊ" + str.length());
		if (str.length() < 200) {
			REPEATCOUNT = 1;
		}
		if (str.length() > 200) {
			REPEATCOUNT = 2;
		}
		if (str.length() > 400) {
			REPEATCOUNT = 3;
		}
		if (str.length() > 750) {
			REPEATCOUNT = 4;
		}
		if (str.length() > 1050) {
			REPEATCOUNT = 5;
		}
		if (str.length() > 1500) {
			REPEATCOUNT = 6;
		}
	}

	private static float computesimulation(innerresult res) {
		float simular = 0;
		Boolean isPerson = false;
		Boolean isMovie = false;
		Boolean isTime = false;

		simular = (float) (res.getWordMatch()) / res.getWordTotal();

		// // ƥ��Ĺؼ���̫���ˣ����ƾͲ���ͬһƪ����
		if (simular < 0.45) {
			if (res.getisWeiBo()) {
				if (res.WordMatchUnrepeat == 3)
					simular += 0.1;
				// �ؼ�����4������ȵģ�˵����ƪ���½��������Ѿ���������
				else if (res.WordMatchUnrepeat >= 4)
					simular += 0.2;
			} else {
				if (res.WordMatchUnrepeat == 4)
					simular += 0.1;
				if (res.WordMatchUnrepeat == 5)
					simular += 0.15;
				else if (res.WordMatchUnrepeat > 5)
					simular += 0.2;
			}
		}

		ArrayList<String> firstMovieName = content.getMovie(1);
		ArrayList<String> secondMovieName = content.getMovie(2);

		if (firstMovieName.size() > 0 && secondMovieName.size() > 0) {
			for (String strfmn : firstMovieName) {
				if (secondMovieName.contains(strfmn)) {
					isMovie = true;
					break;
				}
			}

			for (String strsmn : secondMovieName) {
				if (firstMovieName.contains(strsmn)) {
					isMovie = true;
					break;
				}
			}
		}

		ArrayList<String> firstPersonName = content.getPerson(1);
		ArrayList<String> secondPersonName = content.getPerson(2);

		if (firstPersonName.size() > 0 && secondPersonName.size() > 0) {
			for (String strfpn : firstPersonName) {
				if (secondPersonName.contains(strfpn)) {
					isPerson = true;
					break;
				}
			}

			for (String strspn : secondPersonName) {
				if (firstPersonName.contains(strspn)) {
					isPerson = true;
					break;
				}
			}
		}

		ArrayList<String> firstTime = content.getTime(1);
		ArrayList<String> secondTime = content.getTime(2);

		if (firstTime.size() > 0 && secondTime.size() > 0) {
			for (String strfmn : firstTime) {
				if (secondTime.contains(strfmn)) {
					isTime = true;
					break;
				}
			}

			for (String strsmn : secondTime) {
				if (firstTime.contains(strsmn)) {
					isTime = true;
					break;
				}
			}
		}

		if (isMovie) {
			// System.out.println("��Ӱ����ƥ������");
			simular += 0.1;
		}

		if (isPerson) {

			// System.out.println("���������ƥ������");
			simular += 0.1;
		}

		String firstPlace = content.getPlace(1);
		String secondPlace = content.getPlace(2);

		if (firstPlace != null && secondPlace != null) {
			if (firstPlace.equals(secondPlace)) {
				simular += 0.1;
				// System.out.println(firstPlace+secondPlace);
			}
		}

		if (isTime) {

			simular += 0.1;
		}

		if (simular > 1) {
			simular = 1;
		}

		// ȥ������εļ�����������Ӱ����һ�μ���
		firstMovieName.clear();
		secondMovieName.clear();
		firstPersonName.clear();
		secondPersonName.clear();
		firstTime.clear();
		secondTime.clear();
		firstPlace = null;
		secondPlace = null;

		return simular;
	}

	/**
	 * @param str1
	 *            ��ʾ��һƪ���µ�����
	 * @param str2
	 *            ��ʾ�ڶ�ƪ���µ�����
	 * @return ���صĽ����ʾ��ƪ���µ����ƶ�
	 * @author xujp
	 */
	public float computeSimular(String str1, String str2)
			throws IOException {
		// ����̫�̣��޷��������ƶ�
		if (str1.length() < 10 || str2.length() < 10) {
			System.out.println("����̫�̣��޷��������ƶ�");
			return 0;
		}

		if (!ISSCAM) {
			content.getMovieName(str1, str2);
		}

		HashMap<String, Integer> mapFirst;
		HashMap<String, Integer> mapSecond;
		int repeatfirst = 0;
		int repeatsecond = 0;
		boolean isweibo = false;
		computeRepeatCount(str1);
		mapFirst = getFinalMap(str1);
		repeatfirst = REPEATCOUNT;

		if (!ISSCAM) {
			// �����´ʲ�д����ʱ�ʿ���
			ArrayList<String> firstPersonName = NewWord.obtainNewWords(
					mapFirst, str1);
			firstnick = NewWord.getNamebyNick(nickName);
			if (firstnick != null) {
				addNickname(firstnick, mapFirst);
				firstnick = null;
			}
		}

		computeRepeatCount(str2);
		mapSecond = getFinalMap(str2);
		repeatsecond = REPEATCOUNT;

		if (!ISSCAM) {
			// �����´ʲ�д����ʱ�ʿ���
			ArrayList<String> secondPersonName = NewWord.obtainNewWords(
					mapSecond, str2);
			secondnick = NewWord.getNamebyNick(nickName);
			if (secondnick != null) {
				addNickname(secondnick, mapSecond);
				secondnick = null;
			}
		}

		secondnick = null;

		if (ISSCAM) {
			HashMap<String, Integer> mapScamFirst = getScamMap(str1);
			HashMap<String, Integer> mapScamSecond = getScamMap(str2);
			double douScam = others.computeScam(mapScamFirst, mapScamSecond);
			return (float) douScam;

		} else {
			innerresult res = compareMap(mapFirst, repeatfirst, mapSecond,
					repeatsecond);

			if (str1.length() < 180 || str2.length() < 180) {
				isweibo = true;
			}

			res.setWeiBo(isweibo);
			float resmymethod = computesimulation(res);

			return resmymethod;
		}
	}

	private static void addNickname(String strnick,
			HashMap<String, Integer> mapSecond) {
		// TODO Auto-generated method stub

		Iterator iterfirst = mapSecond.entrySet().iterator();
		while (iterfirst.hasNext()) {
			Map.Entry entryfirst = (Map.Entry) iterfirst.next();
			int val = (Integer) entryfirst.getValue();
			String str = (String) entryfirst.getKey();

			if (str.equals(strnick)) {
				entryfirst.setValue(val + 5);
			}
		}
	}

	private static HashMap<String, Integer> getScamMap(String str1)
			throws IOException {
		// TODO Auto-generated method stub

		StringReader reader1 = new StringReader(str1);
		IKSegmentation ikseg = new IKSegmentation(reader1);

		Lexeme lex = ikseg.next();
		ArrayList<String> List = new ArrayList(500);
		String result = lex.toString();
		int number = 0;
		int allNumber = 0;

		// ȡ���õ��ķִ�
		while (!(lex == null)) {
			// System.out.println(lex.toString()+"��"+Integer.toString(number)+"��");
			if (lex.getLexemeText().toString().length() > 1) {
				number++;
				List.add(lex.getLexemeText().toString());
			}
			lex = ikseg.next();

		}

		ArrayList<String> array = new ArrayList<String>();
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		// ��ȡ�����еĹؼ���
		for (int out = 0; out < number; out++) {
			int count = 1;
			String temp = (String) List.get(out);

			for (int in = out + 1; in < number; in++) {
				if (List.get(in).toString().equals(temp)) {
					count++;
					List.remove(in);
					number--;
				}
			}

			if (count < 0) {
				List.remove(out);
				number--;
				out--;
			} else {
				// �����Ԫ�ز���������Ҫ��
				List.set(out, List.get(out).toString());
				map.put(List.get(out).toString(), count);
				allNumber += count;
				array.add(List.get(out).toString());
			}
		}

		return map;
	}

	private static HashMap<String, Integer> getFinalMap(String str1)
			throws IOException {
		// TODO Auto-generated method stub
		HashMap<String, Integer> mapdata;
		boolean isfirst = true;

		while (true) {

			StringReader reader1 = new StringReader(str1);
			IKSegmentation iksegs1 = new IKSegmentation(reader1);

			// long Item_Time_3 =System.currentTimeMillis();
			// System.out.println("�ִ���ʱ���ǣ�"+(Item_Time_3-Item_Time_2));

			mapdata = getMap(iksegs1, str1.length());
			if (mapdata.size() >= 5 && mapdata.size() <= 20) {
				break;
			}

			if (mapdata.size() < 5) {
				isfirst = false;

				REPEATCOUNT--;
				if (REPEATCOUNT <= 0) {
					REPEATCOUNT = 1;
					break;
				}
			}

			if (mapdata.size() > 20) {
				if (!isfirst) {
					break;
				} else
					REPEATCOUNT++;
			}
		}

		return mapdata;
	}

	// ����ӿ��ǲ���ʹ�õ�
	private static void getFinalMap(String str1, boolean isprint)
			throws IOException {
		// TODO Auto-generated method stub
		HashMap<String, Integer> mapdata;
		boolean isfirst = true;

		while (true) {
			StringReader reader1 = new StringReader(str1);

			// long Item_Time_2 =System.currentTimeMillis();
			IKSegmentation iksegs1 = new IKSegmentation(reader1);
			// long Item_Time_3 =System.currentTimeMillis();
			// System.out.println("�ִ���ʱ���ǣ�"+(Item_Time_3-Item_Time_2));

			mapdata = getMap(iksegs1, str1.length());
			if (mapdata.size() >= 5 && mapdata.size() <= 20) {
				break;
			}

			if (mapdata.size() < 5) {
				isfirst = false;

				REPEATCOUNT--;
				if (REPEATCOUNT <= 0) {
					REPEATCOUNT = 1;
					break;
				}
			}

			if (mapdata.size() > 20) {
				if (!isfirst) {
					break;
				} else
					REPEATCOUNT++;
			}
		}

		System.out.println(mapdata);
	}

	/**
	 * 
	 * ����ikseg��ʾ����IK�ִ����Ľ������ ����ֵΪHashMap,��һ���洢�˹ؼ��ֵļ��ϡ�
	 * ������У�Sting��ʾ�ؼ��֣�Integer��ʾ�ؼ��ֳ��ֵĴ���
	 */
	public static HashMap<String, Integer> getMap(IKSegmentation ikseg,
			int strLen) throws IOException {
		Lexeme lex = ikseg.next();
		ArrayList<String> List = new ArrayList(500);
		String result = lex.toString();
		int number = 0;
		int allNumber = 0;

		// ȡ���õ��ķִ�
		while (!(lex == null)) {
			// System.out.println(lex.toString()+"��"+Integer.toString(number)+"��");
			if (lex.getLexemeText().toString().length() > 1) {
				number++;
				List.add(lex.getLexemeText().toString());
			}
			lex = ikseg.next();
			// System.out.println(lex.getLexemeText());
		}

		ArrayList<String> array = new ArrayList<String>();
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		// ��ȡ�����еĹؼ���
		for (int out = 0; out < number; out++) {
			int count = 1;
			String temp = (String) List.get(out);

			for (int in = out + 1; in < number; in++) {
				if (List.get(in).toString().equals(temp)) {
					count++;
					List.remove(in);
					number--;
				}
			}

			if (count < REPEATCOUNT) {
				List.remove(out);
				number--;
				// System.out.println("���б���ɾ����"+Integer.toString(out)+"��");
				out--;
			} else {
				// �����Ԫ�ز���������Ҫ��
				List.set(out, List.get(out).toString()
				// + "��"+ Integer.toString(count)
						);
				map.put(List.get(out).toString(), count);
				allNumber += count;
				array.add(List.get(out).toString());
			}
		}

		// -----������ð���㷨�����ؼ�������--------
		for (int i = 0; i < array.size(); i++) {
			for (int j = 0; j < array.size() - 1 - i; j++) {
				if (map.get(array.get(j)) < map.get(array.get(j + 1))) {
					String tmp = array.get(j);
					array.set(j, array.get(j + 1));
					array.set(j + 1, tmp);
				}
			}
		}

		// System.out.println(map);// ������Ǵ�ӡ�ַ����ֵĴ���
		// System.out.println(array);// �����ӡ����������,��С����
		// �ѵõ��Ĺؼ�����ʾ����
		// if (array.size() > 10) {
		// for (int i = 0; i < 10; i++) {
		// System.out.println(array.get(i) + map.get(array.get(i)) + " ");
		// // System.out.println(map.get(array.get(i)));
		// }
		// } else {
		// for (int i = 0; i < array.size(); i++) {
		// System.out.println(array.get(i) + map.get(array.get(i)) + " ");
		// // System.out.println(map.get(array.get(i)));
		// }
		// }

		// Integer numberTop = map.get(array.get(0));
		// numberTop+=15;
		// map.put(array.get(0), numberTop);
		// if(strLen>1500&&map.size()<12&&REPEATCOUNT>=1)
		// {
		// REPEATCOUNT--;
		// map = getMap(ikseg,strLen);
		// }

		// ������Ƶ�Ĵ���
		if (map.size() > 4) {
			map = highfeqdown(map, allNumber);
		}

		// System.out.println(map);// ������Ǵ�ӡ�ַ����ֵĴ���
		return map;
	}

	private static HashMap<String, Integer> highfeqdown(
			HashMap<String, Integer> map, int allNumber) {
		// TODO Auto-generated method stub

		int avg = allNumber / 4;
		int overavg = 0;
		int overavgnumber = 0;
		int change = 0;
		int changevalue = 0;

		Iterator iterfirst = map.entrySet().iterator();
		while (iterfirst.hasNext()) {
			Map.Entry entryfirst = (Map.Entry) iterfirst.next();
			int valfirst = (Integer) entryfirst.getValue();
			if (valfirst > avg) {
				overavg++;
				overavgnumber += valfirst;
			}
		}
		change = 4 - overavg;
		if (change > 0) {
			changevalue = (allNumber - overavgnumber) / change + 1;
		}

		if (overavg > 0) {
			Iterator iterfirsts = map.entrySet().iterator();
			while (iterfirsts.hasNext()) {
				Map.Entry entryfirst = (Map.Entry) iterfirsts.next();
				int valfirst = (Integer) entryfirst.getValue();
				if (valfirst > avg) {
					entryfirst.setValue(changevalue);
				}
			}
		}

		return map;
	}

	public static ArrayList<String> getList(IKSegmentation ikseg)
			throws IOException {
		Lexeme lex = ikseg.next();
		ArrayList<String> List = new ArrayList(500);
		String result = lex.toString();
		int number = 0;

		// ȡ���õ��ķִ�
		while (!(lex == null)) {
			// System.out.println(lex.toString()+"��"+Integer.toString(number)+"��");
			if (lex.getLexemeText().toString().length() > 1) {
				number++;
				List.add(lex.getLexemeText().toString());
			}
			lex = ikseg.next();
			// System.out.println(lex.getLexemeText());
		}

		ArrayList<String> array = new ArrayList<String>();
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		// ��ȡ�����еĹؼ���
		for (int out = 0; out < number; out++) {
			int count = 1;
			String temp = (String) List.get(out);
			for (int in = out + 1; in < number; in++) {
				if (List.get(in).toString().equals(temp)) {
					count++;
					List.remove(in);
					number--;
				}
			}

			if (count < REPEATCOUNT) {
				List.remove(out);
				number--;
				// System.out.println("���б���ɾ����"+Integer.toString(out)+"��");
				out--;
			} else {
				// �����Ԫ�ز���������Ҫ��
				List.set(out, List.get(out).toString()
				// + "��"+ Integer.toString(count)
						);

				map.put(List.get(out).toString(), count);
				array.add(List.get(out).toString());
			}
		}

		// -----������ð���㷨�����ؼ�������--------
		for (int i = 0; i < array.size(); i++) {
			for (int j = 0; j < array.size() - 1 - i; j++) {
				if (map.get(array.get(j)) < map.get(array.get(j + 1))) {
					String tmp = array.get(j);
					array.set(j, array.get(j + 1));
					array.set(j + 1, tmp);
				}
			}
		}

		// System.out.println(map);// ������Ǵ�ӡ�ַ����ֵĴ���
		// System.out.println(array);// �����ӡ����������,��С����
		// �ѵõ��Ĺؼ�����ʾ����
		if (array.size() > 10) {
			for (int i = 0; i < 10; i++) {
				System.out.println(array.get(i) + map.get(array.get(i)) + " ");
				// System.out.println(map.get(array.get(i)));
			}
		} else {
			for (int i = 0; i < array.size(); i++) {
				System.out.println(array.get(i) + map.get(array.get(i)) + " ");
				// System.out.println(map.get(array.get(i)));
			}
		}

		return array;
	}

	public static int compare(ArrayList<String> arrayfirst,
			ArrayList<String> arraysecond) {
		int same = 0;
		for (int i = 0; i < arrayfirst.size(); i++) {
			String temp = arrayfirst.get(i);
			for (int j = 0; j < arraysecond.size(); j++) {
				if (temp.equals(arraysecond.get(j).toString())) {
					same++;
					numberitem++;
				}
			}
		}
		return same;
	}

	public static innerresult compareMap(HashMap<String, Integer> mapfirst,
			int rf, HashMap<String, Integer> mapsecond, int rs) {

		int same = 0;
		int sameUnrepeat = 0;
		int total = 0;
		int firstSize = 0;
		int secondSize = 0;
		int first10 = 0;
		int second10 = 0;
		int firstauto10 = 0;
		int secondauto10 = 0;

		Iterator iterfirst = mapfirst.entrySet().iterator();
		while (iterfirst.hasNext()) {
			Map.Entry entryfirst = (Map.Entry) iterfirst.next();
			String keyfirst = (String) entryfirst.getKey();
			int valfirst = (Integer) entryfirst.getValue();
			Iterator itersecond = mapsecond.entrySet().iterator();
			firstauto10++;
			Boolean flag = false;
			if (total == 0) {
				flag = true;
			} else {
				flag = false;
			}

			while (itersecond.hasNext()) {
				Map.Entry entrysecond = (Map.Entry) itersecond.next();
				String keysecond = (String) entrysecond.getKey();
				int valsecond = (Integer) entrysecond.getValue();
				if (keyfirst.equals(keysecond)) {
					same += valfirst;
					same += valsecond;
					sameUnrepeat++;
					// System.out.println("ƥ���ϵĹؼ���" + keysecond);
				}

				secondauto10++;

				if (flag) {
					total += valsecond;
					secondSize += valsecond;
					if (secondauto10 < 10) {
						second10 += valsecond;
					}
				}
			}

			total += valfirst;
			firstSize += valfirst;
			if (firstauto10 < 10) {
				first10 += valfirst;
			}
		}

		if (rf == 1 && mapfirst.size() > 20) {
			firstSize = getTopFiveNumber(mapfirst);
		}

		if (rs == 1 && mapsecond.size() > 20) {
			secondSize = getTopFiveNumber(mapsecond);
		}

		total = secondSize + firstSize;
		// System.out.println("fisrtsize="+firstSize+"second"+secondSize);
		innerresult in = new innerresult(total, same, mapfirst.size()
				+ mapsecond.size(), sameUnrepeat);

		// System.out.println(total + "," + same + "," + mapfirst.size()
		// +","+ mapsecond.size() + "," + sameUnrepeat);

		return in;
	}

	private static int getTopFiveNumber(HashMap<String, Integer> mapfirst) {
		// TODO Auto-generated method stub
		int TopFiveNumber = 0;
		ArrayList numList = new ArrayList();

		Iterator iterfirst = mapfirst.entrySet().iterator();
		while (iterfirst.hasNext()) {
			Map.Entry entryfirst = (Map.Entry) iterfirst.next();
			String keyfirst = (String) entryfirst.getKey();
			int valfirst = (Integer) entryfirst.getValue();

			numList.add(valfirst);
		}

		int numtemp = 0;
		int numtempindex = 0;

		for (int j = 0; j < 5; j++) {
			for (int i = 0; i < numList.size(); i++) {
				if (numtemp < (Integer) numList.get(i)) {
					numtemp = (Integer) numList.get(i);
					numtempindex = i;
				}
			}

			TopFiveNumber += numtemp;
			numList.remove(numtempindex);
			numtemp = 0;
		}

		return TopFiveNumber;
	}

	public static ArrayList<String> computeNewWord(String str) {
		ArrayList<String> ste = new ArrayList<String>();
		int length = str.length();
		for (int i = 0; i < length - 3; i++) {
			char first = str.charAt(i);
			char second = str.charAt(i + 1);
			char third = str.charAt(i + 2);
			char four = str.charAt(i + 3);
			for (int j = i + 1; j < length - 1; j++) {

				if (first == str.charAt(j) && second == str.charAt(j + 1)) {
					// third = str.charAt(i + 2);
					if (third == str.charAt(j + 2)) {
						if (four == str.charAt(j + 3)) {
							// �ɹ�ƥ��4��������������
							i += 3;
							char same[] = { str.charAt(j), str.charAt(j + 1),
									str.charAt(j + 2), str.charAt(j + 3) };
							String temp = String.copyValueOf(same);
							if (!ste.contains(temp))
								ste.add(String.copyValueOf(same));
						} else
						// �ɹ�ƥ��3�������ĸ���ƥ��
						{
							i += 2;
							char same[] = { str.charAt(j), str.charAt(j + 1),
									str.charAt(j + 2) };
							String temp = String.copyValueOf(same);
							if (!ste.contains(temp))
								ste.add(String.copyValueOf(same));
						}
					} else {
						// �ɹ�ƥ��2������������ƥ��
						i++;
						char same[] = { str.charAt(j), str.charAt(j + 1) };
						String temp = String.copyValueOf(same);
						if (!ste.contains(temp))
							ste.add(String.copyValueOf(same));
					}
				}
			}
		}
		return ste;
	}

	void computedata(float res, boolean similar) {

		if (similar) {
			TESTALL++;
			if (res > 0.45) {
				TESTSAME++;
			}
		} else {
			TESTUNSAMEALL++;
			if (res < 0.45) {
				TESTUNSAME++;
			}
		}
	}

	public void showResult() {
		// TODO Auto-generated method stub
		System.out.println("����ʱ�Ĳ��Դ���Ϊ" + (TESTALL - TESTSAME));
		System.out.println("������ʱ�Ĳ��Դ���Ϊ" + (TESTUNSAMEALL - TESTUNSAME));
		System.out.println("�����ܴ���Ϊ" + (TESTALL + TESTUNSAMEALL));

		System.out.println("©����Ϊ" + (1 - (float) TESTSAME / TESTALL));
		System.out.println("������Ϊ" + (1 - (float) TESTUNSAME / TESTUNSAMEALL));
	}
}
