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
	
	// 获取子目录下的所有文件
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
	
	/**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 */
	public String readFileByLines(String fileName) {
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
