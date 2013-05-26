package fileclass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import term.ENSD;
import term.SimHash;
import term.TermFrequencyEle;

public class FeatureText {
	
	private String name;
	private TermFrequencyEle freqFeature;
	private ENSD ensdFeature;
	private SimHash fingerprint;
	public int debug;
	
	public FeatureText(String fileName) {
		// TODO Auto-generated constructor stub
		this.name = fileName;
	}
	
	public HashMap<String, Integer> getFrequency() {
		// TODO Auto-generated method stub
		return freqFeature.getTermFrequency();
	}
	
	public SimHash extractSimHash(int bits) {
		// TODO Auto-generated method stub
		
		if (this.fingerprint != null && 
				bits == fingerprint.getHashBits())
			return this.fingerprint;
		
		String content = null;
		SimHash tempHash = null;
		
		try {
			
			if (this.freqFeature == null) {
				content = readFileByLine();
				tempHash = new SimHash(content, this.name);
			}
			else {
				tempHash = new SimHash(
						this.freqFeature.getContent(), this.name);
				tempHash.setTermFrequency(
						this.freqFeature.getTermFrequency());
			}
			tempHash.debug = this.debug;
			tempHash.setHashBits(bits);
			tempHash.generateEle();
			this.fingerprint = tempHash; 
			return fingerprint;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public ENSD extractENSD() {
		// TODO Auto-generated method stub
		if (this.ensdFeature != null) {
			return ensdFeature;
		}
		
		String content = null;
		ENSD tempENSD = null;
		try {
			if (this.freqFeature == null) {
				content = readFileByLine();
				tempENSD = new ENSD(content, this.name);
			}
			else {
				tempENSD = new ENSD(
						this.freqFeature.getContent(), this.name);
				tempENSD.setTermFrequency(
						this.freqFeature.getTermFrequency());
			}
			tempENSD.setKeyThreshold();
			tempENSD.generateEle();
			ensdFeature = tempENSD;
			
			return ensdFeature;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public TermFrequencyEle extractTermFrequency() {
		// TODO Auto-generated method stub
		if (this.freqFeature != null)
			return this.freqFeature;
		
		String content = readFileByLine();
		try {
			this.freqFeature = new TermFrequencyEle(content, this.name);
			this.freqFeature.generateEle();
			return this.freqFeature;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private String readFileByLine() {
		// TODO Auto-generated method stub
		
		File file = new File(this.name);
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			String result = "";
			
			while ((tempString = reader.readLine()) != null) {
				result += tempString;
			}
			reader.close();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} 
	}

	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String fileName = "E:\\yangxiulong\\Simhash\\testData\\similarSet\\´ºÍí\\·ï»ËÍø.txt";
		FeatureText testText = new FeatureText(fileName);
		System.out.println(testText.extractTermFrequency());
		System.out.println(testText.extractENSD());
		System.out.println(testText.extractSimHash(64));
	}

	public SimHash getSimHash() {
		// TODO Auto-generated method stub
		return fingerprint;
	}
}
