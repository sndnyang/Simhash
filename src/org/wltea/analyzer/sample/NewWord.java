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
			int line = 1;
			// һ�ζ���һ�У�ֱ������nullΪ�ļ�����
			while ((tempString = reader.readLine()) != null) {
				// ��ʾ�к�
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
	 * ����Ϊ��λ��ȡ�ļ��������ڶ������еĸ�ʽ���ļ�
	 */
	public static ArrayList<String> readFilebyLinewithArray(String fileName) {
		File file = new File(fileName);
		ArrayList<String> ste = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			// System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			String result = "";
			// int line = 1;
			// һ�ζ���һ�У�ֱ������nullΪ�ļ�����
			while ((tempString = reader.readLine()) != null) {
				ste.add(tempString);
				// System.out.println("����������Ϊ" + tempString);
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
	 * �´ʵ��жϷ���������ʹ��ͳ�Ƶķ��������������һ���������´ʣ�����ĳ��ȹ涨�Ϊ4�� Ȼ���ٰ��´��г��ֵĹؼ��ָ�ȥ������ʣ�µľ��Ǳ�ѡ���´���
	 * ����㷨��Ĭ���ǳ���2�ξͿ�������һ���´��ˣ�2�β�������Ҫ����� ������Ҫ������޸�
	 * 
	 * @param str
	 *            ��������ַ���
	 * @param which
	 *            �ǿ�Ҫ����ĸ�
	 * @return ����ֵΪ���봮�м�������Ĵ���
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
							// �ɹ�ƥ��4��������������
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
						// �ɹ�ƥ��3�������ĸ���ƥ��
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
						// �ɹ�ƥ��2������������ƥ��
						char same[] = { str.charAt(j), str.charAt(j + 1) };
						String temp = String.copyValueOf(same);
						if (!ste.contains(temp)) {
							ste.add(String.copyValueOf(same));
							map.put(temp, 2);
							i++;
						} else
						// ����������
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
	 * д���´ʵ���ʱ�ʿ���
	 * 
	 * @param str
	 *            ��ʾ�´�
	 * 
	 * @param isOverWrite
	 *            �Ƿ�Ϊ����д ����ǣ������д��ĸ��ǵ�ԭ�������ݣ� ������ǣ�����ȶ���д������д���ظ�������
	 * 
	 * @author xujp
	 * */
	public static void WriteNewWordIntofile(ArrayList<String> str,
			boolean isOverWrite) throws IOException {
//		System.out.println("��ʼдtemp.dic�ļ�");
//		System.out.println(str);
		String writeInFile = "";
		ArrayList<String> oldWords = new ArrayList<String>();
		ArrayList<String> oldWordsFromErrors = new ArrayList<String>();
		oldWords = readFilebyLinewithArray("temp.dic");

		// ���Ǵʵ�,��д���´ʿ���
		oldWordsFromErrors = readFilebyLinewithArray("error.dic");
		if (oldWordsFromErrors.size() > 0) {
			for (String errorItem : oldWordsFromErrors) {
				oldWords.add(errorItem);
			}
		}

		// ȷ��д������ݺ���
		for (String item : str) {
			if (!oldWords.contains(item) || isOverWrite) {
				writeInFile += item + "\r\n";
			}
		}

		File TxtFile = new File(
				"/temp.dic"); // �ٴ����ļ�
		if (!TxtFile.exists()) {
			TxtFile.createNewFile();
		} else {
			FileWriter fr;
			try {
				// ���Ǹ���д����Ϊ��ӵ�����
				fr = new FileWriter(TxtFile, !isOverWrite);
				if (writeInFile.length() > 0) {
					fr.write(writeInFile);
//					System.out.println("�Ѿ��´�" + writeInFile + "д��ȥ��");
//					System.out.println("�ļ��ĵ�ַΪ" + TxtFile.getAbsoluteFile());
				} else {
//					System.out.println("û���´ʱ�д��");
				}

				fr.flush();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** 
	 * ��ȡ�´�
	 * 
	 * @param papermap
	 *            �����÷ִ��㷨�õ��Ĵ���
	 * 
	 * @param paperString
	 *            ���µ�����
	 * 
	 * @author xujp
	 * @return
	 * 		        ���������а����Ĳ��ڴʿ��е���������� 
	 * @throws IOException
	 */
	public static ArrayList<String> obtainNewWords(HashMap<String, Integer> papermap,
			String paperString) throws IOException {
//		System.out.println("��ʼ�������µ��´�");

		// �����÷ִ��㷨�õ��Ĵ���
		ArrayList<String> paperList = new ArrayList<String>();
        ArrayList<String> personName = new ArrayList<String>() ;
		
		Iterator paperItem = papermap.entrySet().iterator();
		while (paperItem.hasNext()) {
			Map.Entry entryfirst = (Map.Entry) paperItem.next();
			String keyfirst = (String) entryfirst.getKey();
			paperList.add(keyfirst);
		}

		// �õ�������´�
		ArrayList<String> newWordList = computeNewWords(
				paperString.replaceAll("[^\u4E00-\u9FA5]", ""), 5);

		// ������һ����ȥ���ظ��Ĵ���
		ArrayList<String> nWList = new ArrayList<String>();
		for (String newWordItem : newWordList) {
			if (!paperList.contains(newWordItem)) {
				nWList.add(newWordItem);
			}
		}

		if (nWList.size() > 0) {
//			System.out.println("���µ��´�Ϊ" + nWList);
			for(String itemList: nWList )
			{
			if(Dictionary.matchInSurnameDict(itemList.toCharArray(), 0, 1)
					.isMatch())
				{
//				   System.out.println(itemList+"��һ���˵�����");
					personName.add(itemList);
				}			
			}
			
			// д���´ʵ���ʱ�ʿ���
			if (nWList.size() > 0)
			{
				
				WriteNewWordIntofile(nWList, false);
				NewWord= nWList;
			
			}
		} else
		{
//			System.out.println("û���´�");
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
		
		//�������		
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
//			System.out.println("û���´�");			
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
				// ����
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
	 * ����һƪ�����������ֵ��´�
	 * 
	 * @param strPaper
	 *            ��ʾ�����´ʵ�����
	 * @param repeatCount
	 *            ��ʾ��Ҫ�ظ��Ĵ���
	 * @author xujp
	 * @return ���ؽ��Ϊ
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
						// ����������
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
	 * ����һƪ�����������ֵ��´�
	 * 
	 * @param strPaper
	 *            ��ʾ�����´ʵ�����
	 * @param repeatCount
	 *            ��ʾ��Ҫ�ظ��Ĵ���
	 * @author xujp
	 * @return ���ؽ��Ϊ
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
						// ����������
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
	 * ����һƪ�������ĸ��ֵ��´�
	 * 
	 * @param strPaper
	 *            ��ʾ�����´ʵ�����
	 * @param repeatCount
	 *            ��ʾ��Ҫ�ظ��Ĵ���
	 * 
	 * @author xujp
	 * @return ���ؽ��Ϊ
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
						// ����������
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
	 * ����һƪ����������ֵ��´�
	 * 
	 * @param strPaper
	 *            ��ʾ�����´ʵ�����
	 * @param repeatCount
	 *            ��ʾ��Ҫ�ظ��Ĵ���
	 * @author xujp
	 * @return ���ؽ��Ϊ
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
						// ����������
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