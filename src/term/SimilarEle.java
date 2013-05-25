package term;

import java.io.IOException;

public class SimilarEle {
	
	protected String fileName;
	protected String content;
	
	public SimilarEle(String str, String fileName) {
		// TODO Auto-generated constructor stub
		this.fileName = fileName;
		this.content = str;
	}
	
	public void generateEle() throws IOException {
		
	}
	
	public String getContent() {
		// TODO Auto-generated method stub
		return content;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public double compareTo(SimilarEle x) {
		return 0;
	}
}
