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
		this.fileList  = new DirectoryOperator().getSubFilesList(dirPath);
	}
	
	public String getDirPath() {
		return dirPath;
	}

	public ArrayList<FeatureText> extractFileFeature(String type) {
		// TODO Auto-generated method stub\
		if (this.fileList == null || !this.featureDocList.isEmpty())
			return this.featureDocList;
		
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
}
