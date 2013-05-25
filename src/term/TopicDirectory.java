package term;

import java.util.ArrayList;

import fileclass.DirectoryOperator;
import fileclass.FeatureText;

public class TopicDirectory {

	private String dirPath = null;
	
	private ArrayList<String> fileList = null;
	private ArrayList<FeatureText> featureDocList = 
			new ArrayList<FeatureText>();

	private int bits = 64;

	public TopicDirectory(String dirPath) {
		// TODO Auto-generated constructor stub
		setTopicDir(dirPath);	
	}

	public void setTopicDir(String dirPath) {
		// TODO Auto-generated method stub
		this.dirPath = dirPath;
		DirectoryOperator operator = new DirectoryOperator();
		this.fileList  = operator.getSubFilesList(dirPath);
	}
	
	public String getDirPath() {
		return dirPath;
	}

	public ArrayList<FeatureText> extractFileFeature(String type) {
		// TODO Auto-generated method stub
		for (int i = 0; i < this.fileList.size(); i++) {
			FeatureText temp = new FeatureText(fileList.get(i));
			if (type.equals("frequency")) {
				temp.extractTermFrequency();
			} else if (type.equals("ENSD")) {
				temp.extractENSD();
			} else if (type.equals("simhash")) {
				temp.extractSimHash(this.bits);
			}
			featureDocList.add(temp);
		}
		
		return featureDocList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("adfsd");
		String dirPath = "E:\\yangxiulong\\Simhash\\testData\\similarSet\\´ºÍí";
		System.out.println(dirPath);
		TopicDirectory test = new TopicDirectory(dirPath);
		ArrayList<FeatureText> docSet = test.extractFileFeature(dirPath);
		for (int i = 0; i < docSet.size(); i++) {
		
		}
	}
}
