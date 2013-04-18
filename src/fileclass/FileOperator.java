package fileclass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.wltea.analyzer.sample.NewWord;

public class FileOperator {
	
	// ��ȡ��Ŀ¼�µ������ļ�
	public List<String> getFiles(String dir) {
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
	
	/**
	 * ����Ϊ��λ��ȡ�ļ��������ڶ������еĸ�ʽ���ļ�
	 */
	public String readFileByLines(String fileName) {
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

	public List<ArrayList<String>> getData(String strPath) {
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
}
