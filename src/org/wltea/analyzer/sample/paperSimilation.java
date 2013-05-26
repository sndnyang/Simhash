package org.wltea.analyzer.sample;

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
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.dic.Dictionary;

/**
 * 
 * @author 
 */
public class paperSimilation {

	private static ArrayList<String> firstMovieName = new ArrayList<String>();
	private static ArrayList<String> secondMovieName = new ArrayList<String>();
	
	private static ArrayList<String> firstTime = new ArrayList<String>();
	private static ArrayList<String> secondTime  = new ArrayList<String>();
	
	private static ArrayList<String> firstPersonName = new ArrayList<String>();
	private static ArrayList<String> secondPersonName = new ArrayList<String>();

	private static String firstPlace =null;
	private static String secondPlace =null;
	
	private static  final String   urlPlace ="/org/wltea/analyzer/sample/placename.dic";
	private static  final String   urlNick ="/org/wltea/analyzer/sample/nickname.dic";
	private static ArrayList<String > placeName = new ArrayList<String >() ;
	private static ArrayList<String > nickName = new ArrayList<String >() ;
	
	private static int REPEATCOUNT = 3;
	private static int TESTALL = 0;
	private static int TESTUNSAMEALL = 0;
	private static int TESTSAME = 0;
	private static boolean ISSCAM = false;
	private static int TESTUNSAME = 0;
	private static String firstnick = null;
	private static String secondnick = null;
	
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

	/**
	 * ����Ϊ��λ��ȡ�ļ��������ڶ������еĸ�ʽ���ļ�
	 */
	public static String readFileByLines(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			// System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			String result = "";
			// һ�ζ���һ�У�ֱ������nullΪ�ļ�����
			while ((tempString = reader.readLine()) != null) {
				// ��ʾ�к�
				result += tempString;
			}
			reader.close();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
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

	/**
	 * 
	 * �ȶ���д�������ظ�д д���´ʵ���չ�ʿ����� �ṩΪWS�ӿ�
	 * 
	 * @param str
	 *            ��ʾ�´�
	 * @param filePath
	 *            ��ʾ�ļ���·�� ���ΪNewWords�����ʾд�뵽�´ʿ���
	 * @return 0��ʾд��ʧ�� 1 ��ʾд��ɹ� 2��ʾ�����ļ���û��д������
	 * 
	 * @author xujp
	 * */
	@SuppressWarnings("resource")
	public static int WriteNewWordIntofile(String[] strs, String filePath)
			throws IOException {
		if (filePath.equals("NewWords")) {
			ArrayList<String> str = new ArrayList<String>();

			for (int i = 0; i < strs.length; i++) {
				str.add(strs[i]);
			}
			NewWord.WriteNewWordIntofile(str, true);
		}

		if (strs.length == 0 || filePath == null) {
			System.out.println("û�д����û��д���ļ���ַ");
			return 0;
		}

		ArrayList<String> str = new ArrayList<String>();
		for (int i = 0; i < strs.length; i++) {
			str.add(strs[i]);
		}

		System.out.println("��ʼд�ļ���" + filePath);
		System.out.println(str);
		String writeInFile = "";

		File TxtFile = new File(filePath);
		// "D:\\apache-tomcat-7.0.11\\webapps\\axis2\\WEB-INF\\services\\mydict.dic");
		// // �ٴ����ļ�
		if (!TxtFile.exists()) {
			TxtFile.createNewFile();
		} else {
			FileWriter fr;
			try {
				ArrayList<String> ste = new ArrayList<String>();
				ste = readFilebyLinewithArray(filePath);
				// "D:\\apache-tomcat-7.0.11\\webapps\\axis2\\WEB-INF\\services\\mydict.dic");

				// ȥ���ظ��Ĵ���
				for (String item : str) {
					if (!ste.contains(item)) {
						writeInFile += item + "\r\n";
					}
				}

				fr = new FileWriter(TxtFile, true);
				if (writeInFile.length() > 0) {
					fr.write(writeInFile);
					System.out.println("�Ѿ��´�" + writeInFile + "д��ȥ��");
					System.out.println("�ļ��ĵ�ַΪ" + TxtFile.getAbsoluteFile());
					fr.flush();
					fr.close();
					return 1;
				} else {
					System.out.println("û���´ʱ�д��");
					return 2;
				}

				// return 1;
			} catch (IOException e) {
				e.printStackTrace();
				return 0;
			}
		}

		return 2;
	}

	/**
	 * 
	 * ����Ϊ��λ��ȡ�ļ��������ڶ������еĸ�ʽ���ļ�
	 * 
	 * @param filePath
	 *            Ϊ�ļ��ĵ�ַ
	 * @return ����һ��
	 * @author xujp
	 */
	public static ArrayList<String> readFilebyLinewithArray(String filePath) {
		File file = new File(filePath);
		ArrayList<String> ste = new ArrayList<String>();
		BufferedReader reader = null;
		System.out.println("��ȡ���ļ��ĵ�ַΪ��" + filePath);
		try {
			// System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// һ�ζ���һ�У�ֱ������nullΪ�ļ�����
			while ((tempString = reader.readLine()) != null) {
				ste.add(tempString);
			}
			reader.close();

			if (ste.size() > 0) {
				System.out.println("��ȡ�������´�Ϊ��" + ste);
			} else {
				System.out.println("û�ж�ȡ����");
			}

			return ste;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
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
			}			
			else {		
				if (res.WordMatchUnrepeat == 4)
					simular += 0.1;
				if (res.WordMatchUnrepeat == 5)
					simular += 0.15;
				else if (res.WordMatchUnrepeat > 5)
					simular += 0.2;
			}
		}

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

		if(firstPlace!=null&&secondPlace!=null)
		{
			if(firstPlace.equals(secondPlace))
			{
				simular += 0.1;	
//				System.out.println(firstPlace+secondPlace);
			}
		}
		
        if(isTime){
        	
        	simular+=0.1;
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
		firstPlace =null;
		secondPlace=null;
		
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
	public static float computeSimular(String str1, String str2)
			throws IOException {
		// ����̫�̣��޷��������ƶ�
		if (str1.length() < 10 || str2.length() < 10) {
			System.out.println("����̫�̣��޷��������ƶ�");
			return 0;
		}
		
		if (!ISSCAM) {
			getMovieName(str1, str2);
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
			firstPersonName = NewWord.obtainNewWords(mapFirst, str1);
			firstnick =  NewWord.getNamebyNick(nickName);
			if(firstnick!=null)
			{
				addNickname(firstnick,mapFirst);	
				firstnick =null;				
			}
		}
		computeRepeatCount(str2);
		mapSecond = getFinalMap(str2);
		repeatsecond = REPEATCOUNT;

		if (!ISSCAM)
		{
			// �����´ʲ�д����ʱ�ʿ���
			secondPersonName = NewWord.obtainNewWords(mapSecond, str2);
			secondnick =  NewWord.getNamebyNick(nickName);				
			if(secondnick!=null)
			{
				addNickname(secondnick,mapSecond);	
				secondnick =null;				
			}
		}

		secondnick =null;
		
		if (ISSCAM) {
			HashMap<String, Integer> mapScamFirst = getScamMap(str1);
			HashMap<String, Integer> mapScamSecond = getScamMap(str2);
			double douScam = others.computeScam(mapScamFirst, mapScamSecond);
			return (float) douScam;
			
		} else {
			innerresult res = compareMap(mapFirst, repeatfirst, mapSecond,
					repeatsecond);
			System.out.println((float) res.getWordMatch() / res.getWordTotal());
			
			if (str1.length() < 180 || str2.length() < 180) {
				isweibo = true;
			}

			res.setWeiBo(isweibo);
			float resmymethod = computesimulation(res);
			System.out.println(resmymethod);
			return resmymethod;
		}
	}

	private static void addNickname(String strnick, HashMap<String, Integer> mapSecond) {
		// TODO Auto-generated method stub

		Iterator<Entry<String, Integer>> iterfirst = mapSecond.entrySet().iterator();
		while (iterfirst.hasNext()) {
			Map.Entry<String, Integer> entryfirst = (Map.Entry<String, Integer>) iterfirst.next();
			int val = (Integer) entryfirst.getValue();
            String str = (String )entryfirst.getKey();
			
			if(str.equals(strnick))
			{
				entryfirst.setValue(val+5);
			}				
		}		
	}

	private static HashMap<String, Integer> getScamMap(String str1)
			throws IOException {
		// TODO Auto-generated method stub

		StringReader reader1 = new StringReader(str1);
		IKSegmentation ikseg = new IKSegmentation(reader1);

		Lexeme lex = ikseg.next();
		ArrayList<String> List = new ArrayList<String>(500);
		int number = 0;
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
		
		int round = 1;
		while (true) {

			StringReader reader1 = new StringReader(str1);
			IKSegmentation iksegs1 = new IKSegmentation(reader1);

			// long Item_Time_3 =System.currentTimeMillis();
			// System.out.println("�ִ���ʱ���ǣ�"+(Item_Time_3-Item_Time_2));

			mapdata = getMap(iksegs1, str1.length());
			if (round <= 1 ) {
				System.out.println(mapdata.size() +" "+ mapdata);
			}

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

	/**
	 * 
	 * ����ikseg��ʾ����IK�ִ����Ľ������ ����ֵΪHashMap,��һ���洢�˹ؼ��ֵļ��ϡ�
	 * ������У�Sting��ʾ�ؼ��֣�Integer��ʾ�ؼ��ֳ��ֵĴ���
	 */
	public static HashMap<String, Integer> getMap(IKSegmentation ikseg,
			int strLen) throws IOException {
		Lexeme lex = ikseg.next();
		ArrayList<String> List = new ArrayList<String>(500);
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

		Iterator<Entry<String, Integer>> iterfirst = map.entrySet().iterator();
		while (iterfirst.hasNext()) {
			Map.Entry<String, Integer> entryfirst = (Map.Entry<String, Integer>) iterfirst.next();
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
			Iterator<Entry<String, Integer>> iterfirsts = map.entrySet().iterator();
			while (iterfirsts.hasNext()) {
				Map.Entry<String, Integer> entryfirst = (Map.Entry<String, Integer>) iterfirsts.next();
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
		ArrayList<String> List = new ArrayList<String>(500);
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
		int firstauto10 = 0;
		int secondauto10 = 0;


		Iterator<Entry<String, Integer>> iterfirst = mapfirst.entrySet().iterator();
		while (iterfirst.hasNext()) {
			Map.Entry<String, Integer> entryfirst = (Map.Entry<String, Integer>) iterfirst.next();
			String keyfirst = (String) entryfirst.getKey();
			int valfirst = (Integer) entryfirst.getValue();
			Iterator<Entry<String, Integer>> itersecond = mapsecond.entrySet().iterator();
			firstauto10++;
			Boolean flag = false;
			if (total == 0) {
				flag = true;
			} else {
				flag = false;
			}

			while (itersecond.hasNext()) {
				Map.Entry<String, Integer> entrysecond = (Map.Entry<String, Integer>) itersecond.next();
				String keysecond = (String) entrysecond.getKey();
				int valsecond = (Integer) entrysecond.getValue();
				if (keyfirst.equals(keysecond)) {
					same += valfirst;
					same += valsecond;
					sameUnrepeat++;
				}

				secondauto10++;

				if (flag) {
					total += valsecond;
					secondSize += valsecond;
					if (secondauto10 < 10) {
						
					}
				}
			}

			total += valfirst;
			firstSize += valfirst;
			if (firstauto10 < 10) {
			}
		}
		
		if (rf == 1 && mapfirst.size() > 20) {
			firstSize = getTopFiveNumber(mapfirst);
		}

		if (rs == 1 && mapsecond.size() > 20) {
			secondSize = getTopFiveNumber(mapsecond);
		}

		total = secondSize + firstSize;
		innerresult in = new innerresult(total, same, mapfirst.size()
				+ mapsecond.size(), sameUnrepeat);

		

		return in;
	}

	private static int getTopFiveNumber(HashMap<String, Integer> mapfirst) {
		// TODO Auto-generated method stub
		int TopFiveNumber = 0;
		ArrayList<Integer> numList = new ArrayList<Integer>();

		Iterator<Entry<String, Integer>> iterfirst = mapfirst.entrySet().iterator();
		while (iterfirst.hasNext()) {
			Map.Entry<String, Integer> entryfirst = (Map.Entry<String, Integer>) iterfirst.next();
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

	/**
	 * @param str1
	 *            ��ʾ��һƪ����
	 * @param str2
	 *            ��ʾ�ڶ�ƪ����
	 * @param str3
	 *            ��ʾ����ƪ����
	 * @author xujp
	 * @throws IOException
	 * */
	public static void computeThreeSimulation(String str1, String str2,
			String str3) throws IOException {
		System.out.println(computeSimular(str1, str2));
		System.out.println(computeSimular(str1, str3));
		System.out.println(computeSimular(str2, str3));
	}

	private static void getMovieName(String strfirst, String strsecond) {
		// TODO Auto-generated method stub
		
		Pattern p = Pattern.compile("��(.*?)��");
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

		Pattern pTime = Pattern.compile("[0-9]{1,2}[��][0-9]{1,2}[��]");
		Matcher mTime = pTime.matcher(strfirst);
		while (mTime.find()) {			                
			if(!firstTime.contains(mTime.group(0)))
			{
				firstTime.add(mTime.group(0));
			} 		
		}	
		
		Matcher mTimes = pTime.matcher(strsecond);
		while (mTimes.find()) {			                
			if(!secondTime.contains(mTimes.group(0)))
			{
				secondTime.add(mTimes.group(0));
			} 		
		}	
	
		for(String item:placeName)
		{
			if(strfirst.contains(item))
			{
				firstPlace = item;
				break;
			}
		}
		
		for(String item:placeName)
		{
			if(strsecond.contains(item))
			{
				secondPlace = item;
				break;
			}
		}
	}

	
	public static float computetest(List<ArrayList<String>> list)
			throws IOException {	

		for (int i = 0; i < list.size(); i += 2) {
			List<String> Item_unsimilar = list.get(i);
			List<String> Item_similar = list.get(i + 1);

			for (int j = 0; j < Item_unsimilar.size(); j++) {

				for (int j_similar = 0; j_similar < Item_similar.size(); j_similar++) {

					float res = computeSimular(Item_unsimilar.get(j),
							Item_similar.get(j_similar));
					
					computedata(res, false);
				}
				
			}

			for (int j = 0; j < Item_similar.size(); j++) {				
				for (int k = j + 1; k < Item_similar.size(); k++) {
					float res = computeSimular(Item_similar.get(j),
							Item_similar.get(k));
					computedata(res, true);
					
				}
			}
		}

		return (float) TESTSAME / TESTALL;
	}

	// ��ȡ��Ŀ¼�µ������ļ�
	public static List<String> getFiles(String dir) {
		File f = new File(dir);
		String[] files = f.list();
		if (files == null)
			return null;
		if (files.length == 0)
			return null;

		List<String> list = new ArrayList<String>();
		for (int i = 0; i < files.length; i++) {
			File subfile = new File(dir, files[i]);
			if (subfile.isFile()) {

				list.add(subfile.toString());
			} else if (subfile.isDirectory()) {

				list.add(subfile.toString());
			}
		}
		
		return list;
	}

	private static List<ArrayList<String>> getData(String strPath) {
		// TODO Auto-generated method stub
		List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();

		List<String> firstdir = new ArrayList<String>();
		List<String> seconddir = new ArrayList<String>();
		List<String> threedir = new ArrayList<String>();

		firstdir = getFiles(strPath);
		for (int i = 0; i < firstdir.size(); i++) {
			seconddir = getFiles(firstdir.get(i));
			for (int j = 0; j < seconddir.size(); j++) {
				threedir = getFiles(seconddir.get(j));
				if (threedir != null) {
					ArrayList<String> fourStr = new ArrayList<String>();
					for (int k = 0; k < threedir.size(); k++) {
						String res = readFileByLines(threedir.get(k));
						fourStr.add(res);
					}
					list.add(fourStr);
				}
			}
		}
		
		return list;
	}

	   private static void getplacename()
	   {
			// ��ȡ�ص�ʵ��ļ�
			InputStream is = Dictionary.class
					.getResourceAsStream(urlPlace);
			if (is == null) {
				throw new RuntimeException("Quantifier Dictionary not found!!!");
			}
			
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(is), 512);
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

		private static void getNickname() {
			// TODO Auto-generated method stub
			// ��ȡ�ǳ��ļ�
			InputStream is = Dictionary.class
					.getResourceAsStream(urlNick);
			if (is == null) {
				throw new RuntimeException("Quantifier Dictionary not found!!!");
			}
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(is), 512);
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
		
		private static void computedata(float res, boolean similar) {

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

	public static void main(String[] args) throws IOException {		
		// ��Ҫ�����Ǽ���һ�´ʿ���Ϣ
		new IKSegmentation(new StringReader("���Ŵ�ѧ"));
		getplacename();
		getNickname();
		
		List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		list = getData("E:\\����");

		long time_begin = System.currentTimeMillis();
		System.out.println(new Date());
		TESTALL = 0;
		TESTSAME = 0;
		ISSCAM = false;

		for (int i = 0; i < 1; i++) {
			computetest(list);
		}
		
		System.out.println("test all is " + TESTALL);
		System.out.println("test same is " + TESTSAME);
		System.out.println("test UNSAME all is " + TESTUNSAMEALL);
		System.out.println("test UNSAME is " + TESTUNSAME);

		System.out.println("����ʱ�Ĳ��Դ���Ϊ" + (TESTALL - TESTSAME));
		System.out.println("������ʱ�Ĳ��Դ���Ϊ" + (TESTUNSAMEALL - TESTUNSAME));
		System.out.println("�����ܴ���Ϊ" + (TESTALL + TESTUNSAMEALL));
		
		System.out.println("©����Ϊ" + (1 - (float) TESTSAME / TESTALL));
		System.out.println("������Ϊ" + (1 - (float) TESTUNSAME / TESTUNSAMEALL));

		long time_end = System.currentTimeMillis();
		System.out.println(new Date());
		System.out.println("����ʱ��" + (time_end - time_begin));
		System.out.println("ƽ��ÿ�����õ�" + (float) (time_end - time_begin)
				/ (TESTALL + TESTUNSAMEALL));
	}
}