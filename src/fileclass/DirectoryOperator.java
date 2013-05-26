package fileclass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/*
 * 获取子目录下的所有文件
 */
public class DirectoryOperator {
	private String dirPath = null;
	private ArrayList<String> subDirList = new ArrayList<String>();
	private ArrayList<String> subFileList = new ArrayList<String>();
	
	public ArrayList<String> getSubDirList(String dirPath) {
		// TODO Auto-generated method stub
		if (this.dirPath == null || !this.dirPath.equals(dirPath)) {
			this.getSubDirAndFiles(dirPath);
		}
		if (this.subDirList == null&& 
				new File(dirPath).list().length != 0) {
			System.out.println(dirPath + "  获取子文件夹列表出错");
			System.exit(0);
		}			
		return this.subDirList;
	}

	public ArrayList<String> getSubFilesList(String strPath) {
		// TODO Auto-generated method stub
		if (this.dirPath == null || !this.dirPath.equals(dirPath)) {
			this.getSubDirAndFiles(strPath);
		}
		
		if (this.subFileList == null && 
				new File(strPath).list().length != 0) {
			System.out.println(new File(strPath).list().length);
			System.out.println(dirPath + " 获取子文件列表出错");
			System.exit(0);
		}	
		return this.subFileList;
	}

	public ArrayList<String> getSubDirAndFiles(String dirPath) {
		// TODO Auto-generated method stub
		File f = new File(dirPath);
		String[] files = f.list();
		
		this.dirPath = dirPath;

		if (files == null || files.length == 0) {
			return null;
		}

		ArrayList<String> list = new ArrayList<String>();
		
		subDirList = new ArrayList<String>();
		subFileList = new ArrayList<String>();
		
		for (int i = 0; i < files.length; i++) {
			File subfile = new File(dirPath, files[i]);
			
			if (subfile.isFile()) {
				this.subFileList.add(subfile.toString());
				list.add(subfile.toString());
			} else if (subfile.isDirectory()) {
				this.subDirList.add(subfile.toString());
				list.add(subfile.toString());
			}
		}
		return list;
	}

	public String readFileByLines(String fileName) {
		// TODO Auto-generated method stub
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			String result = "";
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
}
