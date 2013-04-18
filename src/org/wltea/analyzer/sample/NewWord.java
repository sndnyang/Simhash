package org.wltea.analyzer.sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.dic.Hit;

public class NewWord {
	

	private static ArrayList<String> NewWord = new ArrayList<String>();
	
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
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				// System.out.println("line " + line + ": " + tempString);
				result += tempString;
				line++;
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

	/**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 */
	public static ArrayList<String> readFilebyLinewithArray(String fileName) {
		File file = new File(fileName);
		ArrayList<String> ste = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			// System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			String result = "";
			// int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				ste.add(tempString);
				// System.out.println("读出的内容为" + tempString);
				// line++;
			}
			reader.close();

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

	/**
	 * 新词的判断方法：首先使用统计的方法计算满足出现一定次数的新词，词语的长度规定最长为4个 然后再把新词中出现的关键字给去除掉，剩下的就是备选的新词了
	 * 这个算法，默认是出现2次就可以算是一个新词了，2次不够，需要更多次 所以需要更多的修改
	 * 
	 * @param str
	 *            是输入的字符串
	 * @param which
	 *            是看要输出哪个
	 * @return 返回值为输入串中计算出来的词语
	 * @author xjp
	 * */
	public static ArrayList<String> computeNewWord(String str, int which) {

		ArrayList<String> ste = new ArrayList<String>();
		ArrayList<String> stedou = new ArrayList<String>();
		HashMap<String, Integer> map = new HashMap<String, Integer>();

		int length = str.length();
		for (int i = 0; i < str.length() - 3; i++) {
			char first = str.charAt(i);
			char second = str.charAt(i + 1);
			char third = str.charAt(i + 2);
			char four = str.charAt(i + 3);
			for (int j = i + 1; j < str.length() - 1; j++) {
				if (first == str.charAt(j) && second == str.charAt(j + 1)) {
					// third = str.charAt(i + 2);
					if (third == str.charAt(j + 2)) {
						if (four == str.charAt(j + 3)) {
							// 成功匹配4个，第五个不检测
							char same[] = { str.charAt(j), str.charAt(j + 1),
									str.charAt(j + 2), str.charAt(j + 3) };
							String temp = String.copyValueOf(same);

							if (!ste.contains(temp)) {
								ste.add(String.copyValueOf(same));
								i += 3;
							}
							// else
							// str = str.replaceAll(temp, "");
						} else
						// 成功匹配3个，第四个不匹配
						{
							char same[] = { str.charAt(j), str.charAt(j + 1),
									str.charAt(j + 2) };
							String temp = String.copyValueOf(same);
							if (!ste.contains(temp)) {
								ste.add(String.copyValueOf(same));
								i += 2;
							}
							// else
							// str = str.replaceAll(temp, "");
						}
					} else {
						// 成功匹配2个，第三个不匹配
						char same[] = { str.charAt(j), str.charAt(j + 1) };
						String temp = String.copyValueOf(same);
						if (!ste.contains(temp)) {
							ste.add(String.copyValueOf(same));
							map.put(temp, 2);
							i++;
						} else
						// 包含在里面
						{
							int time = map.get(temp);
							time++;
							map.put(temp, time);
						}

					}
				}
			}
		}

		System.out.println(map);
		return ste;
	}

	/**
	 * 写入新词到临时词库中
	 * 
	 * @param str
	 *            表示新词
	 * 
	 * @param isOverWrite
	 *            是否为覆盖写 如果是，则会用写入的覆盖的原来的内容； 如果不是，则会先读后写，避免写入重复的内容
	 * 
	 * @author xujp
	 * */
	public static void WriteNewWordIntofile(ArrayList<String> str,
			boolean isOverWrite) throws IOException {
//		System.out.println("开始写temp.dic文件");
//		System.out.println(str);
		String writeInFile = "";
		ArrayList<String> oldWords = new ArrayList<String>();
		ArrayList<String> oldWordsFromErrors = new ArrayList<String>();
		oldWords = readFilebyLinewithArray("temp.dic");

		// 不是词的,不写入新词库中
		oldWordsFromErrors = readFilebyLinewithArray("error.dic");
		if (oldWordsFromErrors.size() > 0) {
			for (String errorItem : oldWordsFromErrors) {
				oldWords.add(errorItem);
			}
		}

		// 确保写入的数据合理
		for (String item : str) {
			if (!oldWords.contains(item) || isOverWrite) {
				writeInFile += item + "\r\n";
			}
		}

		File TxtFile = new File(
				"/temp.dic"); // 再创建文件
		if (!TxtFile.exists()) {
			TxtFile.createNewFile();
		} else {
			FileWriter fr;
			try {
				// 不是覆盖写，则为添加到后面
				fr = new FileWriter(TxtFile, !isOverWrite);
				if (writeInFile.length() > 0) {
					fr.write(writeInFile);
//					System.out.println("已经新词" + writeInFile + "写进去了");
//					System.out.println("文件的地址为" + TxtFile.getAbsoluteFile());
				} else {
//					System.out.println("没有新词被写入");
				}

				fr.flush();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** 
	 * 获取新词
	 * 
	 * @param papermap
	 *            文章用分词算法得到的词语
	 * 
	 * @param paperString
	 *            文章的内容
	 * 
	 * @author xujp
	 * @return
	 * 		        返回文章中包含的不在词库中的人物的名字 
	 * @throws IOException
	 */
	public static ArrayList<String> obtainNewWords(HashMap<String, Integer> papermap,
			String paperString) throws IOException {
//		System.out.println("开始计算文章的新词");

		// 文章用分词算法得到的词语
		ArrayList<String> paperList = new ArrayList<String>();
        ArrayList<String> personName = new ArrayList<String>() ;
		
		Iterator paperItem = papermap.entrySet().iterator();
		while (paperItem.hasNext()) {
			Map.Entry entryfirst = (Map.Entry) paperItem.next();
			String keyfirst = (String) entryfirst.getKey();
			paperList.add(keyfirst);
		}

		// 得到计算的新词
		ArrayList<String> newWordList = computeNewWords(
				paperString.replaceAll("[^\u4E00-\u9FA5]", ""), 5);

		// 下面这一步是去掉重复的词语
		ArrayList<String> nWList = new ArrayList<String>();
		for (String newWordItem : newWordList) {
			if (!paperList.contains(newWordItem)) {
				nWList.add(newWordItem);
			}
		}

		if (nWList.size() > 0) {
//			System.out.println("文章的新词为" + nWList);
			for(String itemList: nWList )
			{
			if(Dictionary.matchInSurnameDict(itemList.toCharArray(), 0, 1)
					.isMatch())
				{
//				   System.out.println(itemList+"是一个人的名字");
					personName.add(itemList);
				}			
			}
			
			// 写入新词到临时词库中
			if (nWList.size() > 0)
			{
				
				WriteNewWordIntofile(nWList, false);
				NewWord= nWList;
			
			}
		} else
		{
//			System.out.println("没有新词");
		}
//		if(personName.size()>0)
			return personName;
//		else
//			return null;
	}

	public static String getNamebyNick(ArrayList<String> nickName)
	{
		
		String name =null;
		for(String nickitem: NewWord)
		{
			for(int i=1;i<nickName.size();i++,i++)
			{
				if(nickitem.equals(nickName.get(i)))
				{
					name = nickName.get(i-1);
				}
			}
		}
		
		//清除数据		
		NewWord.clear();
		return name;
	}
	
	public static void main(String[] args) throws IOException {
		String result = readFileByLines("testsimilar.txt");

		StringReader reader1 = new StringReader(result);
		IKSegmentation iksegs1 = new IKSegmentation(reader1);

		ArrayList<String> paperList = paperSimilation.getList(iksegs1);
		ArrayList<String> newWordList = computeNewWords(
				result.replaceAll("[^\u4E00-\u9FA5]", ""), 5);

		System.out.println(newWordList);
		ArrayList<String> nWList = new ArrayList<String>();
		for (String newWordItem : newWordList) {
			if (!paperList.contains(newWordItem)) {
				nWList.add(newWordItem);
			}
		}

		if (nWList.size() > 0)
			WriteNewWordIntofile(nWList, false);
		else
		{
//			System.out.println("没有新词");			
		}

	}

	private static ArrayList<String> computeNewWords(String result,
			int repeatCount) {
		// TODO Auto-generated method stub
		ArrayList<String> twoWords = new ArrayList<String>();
		ArrayList<String> threeWords = new ArrayList<String>();
		ArrayList<String> fourWords = new ArrayList<String>();
		ArrayList<String> fiveWords = new ArrayList<String>();
		ArrayList<String> resultWords = new ArrayList<String>();

		twoWords = computeTwoNewWord(result, repeatCount);
		threeWords = computeThreeNewWord(result, repeatCount);
		fourWords = computeFourNewWord(result, repeatCount);
		fiveWords = computeFiveNewWord(result, repeatCount);

		if (fiveWords.size() == 0) {
			if (fourWords.size() == 0) {
				if (threeWords.size() == 0) {
					if (twoWords.size() == 0) {

					} else {
						resultWords.addAll(twoWords);
					}
				} else {
					resultWords.addAll(threeWords);
				}

			} else {
				resultWords.addAll(fourWords);
			}
		} else {
			resultWords.addAll(fiveWords);
		}

		for (String twoItem : twoWords) {
			boolean isNotContain = true;
			for (String threeItem : threeWords) {
				// 包含
				if ((threeItem.indexOf(twoItem) >= 0)) {
					isNotContain = false;
					break;
				}
			}

			if (isNotContain && !resultWords.contains(twoItem)) {
				resultWords.add(twoItem);
			}
		}

		for (String threeItem : threeWords) {
			boolean isNotContain = true;
			for (String fourItem : fourWords) {
				if (fourItem.indexOf(threeItem) >= 0) {
					isNotContain = false;
					break;
				}
			}
			if (isNotContain && !resultWords.contains(threeItem)) {
				resultWords.add(threeItem);
			}
		}

		for (String fourItem : fourWords) {
			boolean isNotContain = true;
			for (String fiveItem : fiveWords) {
				if (fiveItem.indexOf(fourItem) >= 0) {
					isNotContain = false;
					break;
				}
			}

			if (isNotContain && !resultWords.contains(fourItem)) {
				resultWords.add(fourItem);
			}
		}

//		System.out.println(resultWords);
		return resultWords;
	}

	/**
	 * 
	 * 计算一篇文章中两个字的新词
	 * 
	 * @param strPaper
	 *            表示计算新词的文章
	 * @param repeatCount
	 *            表示需要重复的次数
	 * @author xujp
	 * @return 返回结果为
	 * 
	 **/
	private static ArrayList<String> computeTwoNewWord(String strPaper,
			int repeatCount) {
		// TODO Auto-generated method stub
		ArrayList<String> stedou = new ArrayList<String>();
		HashMap<String, Integer> strPaperMap = new HashMap<String, Integer>();

		for (int i = 0; i < strPaper.length() - 1; i++) {
			char first = strPaper.charAt(i);
			char second = strPaper.charAt(i + 1);

			char outsame[] = { strPaper.charAt(i), strPaper.charAt(i + 1) };
			String outtemp = String.copyValueOf(outsame);
			if (!strPaperMap.containsKey(outtemp)) {
				for (int j = i + 1; j < strPaper.length() - 1; j++) {

					if (first == strPaper.charAt(j)
							&& second == strPaper.charAt(j + 1)) {
						char same[] = { strPaper.charAt(j),
								strPaper.charAt(j + 1) };
						String temp = String.copyValueOf(same);

						if (!strPaperMap.containsKey(temp)) {
							strPaperMap.put(temp, 2);
							i++;
						} else
						// 包含在里面
						{
							int time = strPaperMap.get(temp);
							time++;
							strPaperMap.put(temp, time);
							if (time >=repeatCount && !stedou.contains(temp)) {
								stedou.add(temp);
							}
						}
					}
				}
			}
		}

//		System.out.println(stedou);
		return stedou;
	}

	/**
	 * 
	 * 计算一篇文章中三个字的新词
	 * 
	 * @param strPaper
	 *            表示计算新词的文章
	 * @param repeatCount
	 *            表示需要重复的次数
	 * @author xujp
	 * @return 返回结果为
	 * 
	 **/
	private static ArrayList<String> computeThreeNewWord(String strPaper,
			int repeatCount) {
		// TODO Auto-generated method stub
		ArrayList<String> stedou = new ArrayList<String>();
		HashMap<String, Integer> strPaperMap = new HashMap<String, Integer>();

		for (int i = 0; i < strPaper.length() - 2; i++) {
			char first = strPaper.charAt(i);
			char second = strPaper.charAt(i + 1);
			char three = strPaper.charAt(i + 2);
			char outsame[] = { strPaper.charAt(i), strPaper.charAt(i + 1),
					strPaper.charAt(i + 2) };
			String outtemp = String.copyValueOf(outsame);
			if (!strPaperMap.containsKey(outtemp)) {
				for (int j = i + 1; j < strPaper.length() - 2; j++) {
					if (first == strPaper.charAt(j)
							&& second == strPaper.charAt(j + 1)
							&& three == strPaper.charAt(j + 2)) {
						char same[] = { strPaper.charAt(j),
								strPaper.charAt(j + 1), strPaper.charAt(j + 2) };
						String temp = String.copyValueOf(same);

						if (!strPaperMap.containsKey(temp)) {
							strPaperMap.put(temp, 2);
							i = i + 2;
							// strPaperMap.
						} else
						// 包含在里面
						{
							int time = strPaperMap.get(temp);
							time++;
							strPaperMap.put(temp, time);
							if (time >= repeatCount && !stedou.contains(temp)) {
								stedou.add(temp);
							}
						}
					}
				}
			}
		}
//		System.out.println(stedou);
		return stedou;
	}

	/**
	 * 
	 * 计算一篇文章中四个字的新词
	 * 
	 * @param strPaper
	 *            表示计算新词的文章
	 * @param repeatCount
	 *            表示需要重复的次数
	 * 
	 * @author xujp
	 * @return 返回结果为
	 **/
	private static ArrayList<String> computeFourNewWord(String strPaper,
			int repeatCount) {
		// TODO Auto-generated method stub
		ArrayList<String> stedou = new ArrayList<String>();
		HashMap<String, Integer> strPaperMap = new HashMap<String, Integer>();

		for (int i = 0; i < strPaper.length() - 3; i++) {
			char first = strPaper.charAt(i);
			char second = strPaper.charAt(i + 1);
			char three = strPaper.charAt(i + 2);
			char four = strPaper.charAt(i + 3);

			char outsame[] = { strPaper.charAt(i), strPaper.charAt(i + 1),
					strPaper.charAt(i + 2), strPaper.charAt(i + 3) };
			String outtemp = String.copyValueOf(outsame);
			if (!strPaperMap.containsKey(outtemp)) {

				for (int j = i + 1; j < strPaper.length() - 3; j++) {
					if (first == strPaper.charAt(j)
							&& second == strPaper.charAt(j + 1)
							&& three == strPaper.charAt(j + 2)
							&& four == strPaper.charAt(j + 3)) {

						char same[] = { strPaper.charAt(j),
								strPaper.charAt(j + 1), strPaper.charAt(j + 2),
								strPaper.charAt(j + 3) };
						String temp = String.copyValueOf(same);

						if (!strPaperMap.containsKey(temp)) {
							strPaperMap.put(temp, 2);
							i = i + 3;
							// strPaperMap.
						} else
						// 包含在里面
						{
							int time = strPaperMap.get(temp);
							time++;
							strPaperMap.put(temp, time);
							if (time >= repeatCount && !stedou.contains(temp)) {
								stedou.add(temp);
							}
						}

					}
				}
			}
		}

//		System.out.println(stedou);
		return stedou;
	}

	/**
	 * 计算一篇文章中五个字的新词
	 * 
	 * @param strPaper
	 *            表示计算新词的文章
	 * @param repeatCount
	 *            表示需要重复的次数
	 * @author xujp
	 * @return 返回结果为
	 **/
	private static ArrayList<String> computeFiveNewWord(String strPaper,
			int repeatCount) {
		// TODO Auto-generated method stub
		ArrayList<String> stedou = new ArrayList<String>();
		HashMap<String, Integer> strPaperMap = new HashMap<String, Integer>();

		for (int i = 0; i < strPaper.length() - 4; i++) {
			char first = strPaper.charAt(i);
			char second = strPaper.charAt(i + 1);
			char three = strPaper.charAt(i + 2);
			char four = strPaper.charAt(i + 3);
			char five = strPaper.charAt(i + 4);

			char outsame[] = { strPaper.charAt(i), strPaper.charAt(i + 1),
					strPaper.charAt(i + 2), strPaper.charAt(i + 3),
					strPaper.charAt(i + 4) };
			String outtemp = String.copyValueOf(outsame);
			if (!strPaperMap.containsKey(outtemp)) {
				for (int j = i + 1; j < strPaper.length() - 4; j++) {
					if (first == strPaper.charAt(j)
							&& second == strPaper.charAt(j + 1)
							&& three == strPaper.charAt(j + 2)
							&& four == strPaper.charAt(j + 3)
							&& five == strPaper.charAt(j + 4)) {

						char same[] = { strPaper.charAt(j),
								strPaper.charAt(j + 1), strPaper.charAt(j + 2),
								strPaper.charAt(j + 3), strPaper.charAt(j + 4) };
						String temp = String.copyValueOf(same);

						if (!strPaperMap.containsKey(temp)) {
							strPaperMap.put(temp, 2);
							i = i + 4;
						} else
						// 包含在里面
						{
							int time = strPaperMap.get(temp);
							time++;
							strPaperMap.put(temp, time);
							if (time >= repeatCount && !stedou.contains(temp)) {
								stedou.add(temp);
							}
						}
					}
				}
			}
		}

//		System.out.println(stedou);
		return stedou;
	}

}