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
		// 关键字总数
		private int WordTotal;
		// 关键字匹配的总数
		private int WordMatch;

		// 关键字总数(不重复)
		private int WordTotalUnrepeat;
		// 关键字匹配的总数(不重复)
		private int WordMatchUnrepeat;

		private boolean isweibo;

		innerresult(int oneinner, int twoinner, int totalUn, int matchUn) {
			WordTotal = oneinner;
			WordMatch = twoinner;
			// 下面的这个2个变量还没有用到
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
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 */
	public static String readFileByLines(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			// System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			String result = "";
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
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
	 * 这个函数是用来计算需要关键字的重复的次数 也就是说 一个词语重复了多少次才算是关键字
	 * 
	 * @param str
	 *            表示文章的字符串
	 * @author xujp
	 **/
	private static void computeRepeatCount(String str) {
		// System.out.println("文章的字数为" + str.length());
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
	 * 先读后写，避免重复写 写入新词到扩展词库里面 提供为WS接口
	 * 
	 * @param str
	 *            表示新词
	 * @param filePath
	 *            表示文件的路径 如果为NewWords，则表示写入到新词库中
	 * @return 0表示写入失败 1 表示写入成功 2表示创建文件，没有写入数据
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
			System.out.println("没有词语或没有写入文件地址");
			return 0;
		}

		ArrayList<String> str = new ArrayList<String>();
		for (int i = 0; i < strs.length; i++) {
			str.add(strs[i]);
		}

		System.out.println("开始写文件：" + filePath);
		System.out.println(str);
		String writeInFile = "";

		File TxtFile = new File(filePath);
		// "D:\\apache-tomcat-7.0.11\\webapps\\axis2\\WEB-INF\\services\\mydict.dic");
		// // 再创建文件
		if (!TxtFile.exists()) {
			TxtFile.createNewFile();
		} else {
			FileWriter fr;
			try {
				ArrayList<String> ste = new ArrayList<String>();
				ste = readFilebyLinewithArray(filePath);
				// "D:\\apache-tomcat-7.0.11\\webapps\\axis2\\WEB-INF\\services\\mydict.dic");

				// 去掉重复的词语
				for (String item : str) {
					if (!ste.contains(item)) {
						writeInFile += item + "\r\n";
					}
				}

				fr = new FileWriter(TxtFile, true);
				if (writeInFile.length() > 0) {
					fr.write(writeInFile);
					System.out.println("已经新词" + writeInFile + "写进去了");
					System.out.println("文件的地址为" + TxtFile.getAbsoluteFile());
					fr.flush();
					fr.close();
					return 1;
				} else {
					System.out.println("没有新词被写入");
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
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 * 
	 * @param filePath
	 *            为文件的地址
	 * @return 返回一个
	 * @author xujp
	 */
	public static ArrayList<String> readFilebyLinewithArray(String filePath) {
		File file = new File(filePath);
		ArrayList<String> ste = new ArrayList<String>();
		BufferedReader reader = null;
		System.out.println("读取的文件的地址为：" + filePath);
		try {
			// System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				ste.add(tempString);
			}
			reader.close();

			if (ste.size() > 0) {
				System.out.println("读取出来的新词为：" + ste);
			} else {
				System.out.println("没有读取词语");
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
		
		// // 匹配的关键字太少了，估计就不是同一篇文章
		if (simular < 0.45) {			
			if (res.getisWeiBo()) {
				if (res.WordMatchUnrepeat == 3)
					simular += 0.1;
				// 关键字有4个是相等的，说明两篇文章讲的内容已经很相似了
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
			// System.out.println("电影名字匹配上了");
			simular += 0.1;
		}

		if (isPerson) {
			
			// System.out.println("人物的名字匹配上了");
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

		// 去除掉这次的计算结果，以免影响下一次计算
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
	 *            表示第一篇文章的内容
	 * @param str2
	 *            表示第二篇文章的内容
	 * @return 返回的结果表示两篇文章的相似度
	 * @author xujp
	 */
	public static float computeSimular(String str1, String str2)
			throws IOException {
		// 文章太短，无法计算相似度
		if (str1.length() < 10 || str2.length() < 10) {
			System.out.println("文章太短，无法计算相似度");
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
			// 计算新词并写入临时词库中
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
			// 计算新词并写入临时词库中
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
		// 取出得到的分词
		while (!(lex == null)) {
			// System.out.println(lex.toString()+"第"+Integer.toString(number)+"个");
			if (lex.getLexemeText().toString().length() > 1) {
				number++;
				List.add(lex.getLexemeText().toString());
			}
			lex = ikseg.next();

		}

		ArrayList<String> array = new ArrayList<String>();
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		// 获取文章中的关键字
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
				// 下面的元素才是我们需要的
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
			// System.out.println("分词用时间是："+(Item_Time_3-Item_Time_2));

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
	 * 参数ikseg表示的是IK分词器的结果集合 返回值为HashMap,是一个存储了关键字的集合。
	 * 结果集中，Sting表示关键字，Integer表示关键字出现的次数
	 */
	public static HashMap<String, Integer> getMap(IKSegmentation ikseg,
			int strLen) throws IOException {
		Lexeme lex = ikseg.next();
		ArrayList<String> List = new ArrayList<String>(500);
		int number = 0;
		int allNumber = 0;

		// 取出得到的分词
		while (!(lex == null)) {
			// System.out.println(lex.toString()+"第"+Integer.toString(number)+"个");
			if (lex.getLexemeText().toString().length() > 1) {
				number++;
				List.add(lex.getLexemeText().toString());
			}
			lex = ikseg.next();
			// System.out.println(lex.getLexemeText());
		}

		ArrayList<String> array = new ArrayList<String>();
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		// 获取文章中的关键字
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
				// System.out.println("从列表中删除第"+Integer.toString(out)+"个");
				out--;
			} else {
				// 下面的元素才是我们需要的
				List.set(out, List.get(out).toString()
				// + "有"+ Integer.toString(count)
				);
				map.put(List.get(out).toString(), count);
				allNumber += count;
				array.add(List.get(out).toString());
			}
		}

		// 处理极高频的词语
		if (map.size() > 4) {
			map = highfeqdown(map, allNumber);
		}

		// System.out.println(map);// 这个就是打印字符出现的次数
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

		// 取出得到的分词
		while (!(lex == null)) {
			// System.out.println(lex.toString()+"第"+Integer.toString(number)+"个");
			if (lex.getLexemeText().toString().length() > 1) {
				number++;
				List.add(lex.getLexemeText().toString());
			}
			lex = ikseg.next();
			// System.out.println(lex.getLexemeText());
		}

		ArrayList<String> array = new ArrayList<String>();
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		// 获取文章中的关键字
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
				// System.out.println("从列表中删除第"+Integer.toString(out)+"个");
				out--;
			} else {
				// 下面的元素才是我们需要的
				List.set(out, List.get(out).toString()
				// + "有"+ Integer.toString(count)
				);

				map.put(List.get(out).toString(), count);
				array.add(List.get(out).toString());
			}
		}

		// -----下面是冒泡算法，给关键字排序--------
		for (int i = 0; i < array.size(); i++) {
			for (int j = 0; j < array.size() - 1 - i; j++) {
				if (map.get(array.get(j)) < map.get(array.get(j + 1))) {
					String tmp = array.get(j);
					array.set(j, array.get(j + 1));
					array.set(j + 1, tmp);
				}
			}
		}

		// System.out.println(map);// 这个就是打印字符出现的次数
		// System.out.println(array);// 这个打印排序后的数组,从小到大
		// 把得到的关键字显示出来
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
							// 成功匹配4个，第五个不检测
							i += 3;
							char same[] = { str.charAt(j), str.charAt(j + 1),
									str.charAt(j + 2), str.charAt(j + 3) };
							String temp = String.copyValueOf(same);
							if (!ste.contains(temp))
								ste.add(String.copyValueOf(same));
						} else
						// 成功匹配3个，第四个不匹配
						{
							i += 2;
							char same[] = { str.charAt(j), str.charAt(j + 1),
									str.charAt(j + 2) };
							String temp = String.copyValueOf(same);
							if (!ste.contains(temp))
								ste.add(String.copyValueOf(same));
						}
					} else {
						// 成功匹配2个，第三个不匹配
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
	 *            表示第一篇文章
	 * @param str2
	 *            表示第二篇文章
	 * @param str3
	 *            表示第三篇文章
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

	// 获取子目录下的所有文件
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
			// 读取地点词典文件
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
			// 读取昵称文件
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
		// 主要作用是加载一下词库信息
		new IKSegmentation(new StringReader("厦门大学"));
		getplacename();
		getNickname();
		
		List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		list = getData("E:\\测试");

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

		System.out.println("相似时的测试次数为" + (TESTALL - TESTSAME));
		System.out.println("不相似时的测试次数为" + (TESTUNSAMEALL - TESTUNSAME));
		System.out.println("测试总次数为" + (TESTALL + TESTUNSAMEALL));
		
		System.out.println("漏判率为" + (1 - (float) TESTSAME / TESTALL));
		System.out.println("误判率为" + (1 - (float) TESTUNSAME / TESTUNSAMEALL));

		long time_end = System.currentTimeMillis();
		System.out.println(new Date());
		System.out.println("所用时间" + (time_end - time_begin));
		System.out.println("平均每次所用的" + (float) (time_end - time_begin)
				/ (TESTALL + TESTUNSAMEALL));
	}
}