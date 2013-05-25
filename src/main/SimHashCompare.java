package main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import operator.SimHashSimilar;
import org.wltea.analyzer.IKSegmentation;

import term.SimHash;
import fileclass.DirectoryOperator;

public class SimHashCompare {
	/* 
	 * 
	 */
	private static String fileName = "E:\\测试"; //"E:\\data\\体育领域" "测试"
	private static SimHashSimilar comSimHash;
	
	private static ArrayList<ArrayList<SimHash>> similarHash = new ArrayList<ArrayList<SimHash>>();
	private static ArrayList<ArrayList<SimHash>> unsimilarHash = new ArrayList<ArrayList<SimHash>>();
	
	private int hashBits = 64;
	private int kThreshold = 5;
	
	public void setThreshold(int threshold) {
		// TODO Auto-generated method stub
		this.kThreshold = threshold;
	}

	public void setBits(int bits) {
		// TODO Auto-generated method stub
		this.hashBits = bits;
	}

	public void getDataFromDir(String strPath) throws IOException {
		// TODO Auto-generated method stub
		DirectoryOperator operator = new DirectoryOperator();
		ArrayList<String> topicDir = operator.getSubDirAndFiles(strPath);
		for (int i = 0; i < topicDir.size(); i++) {
			List<String> typeDir = operator.getSubDirAndFiles(topicDir.get(i));
			for (int j = 0; j < typeDir.size(); j++) {
				String type = typeDir.get(j);
				List<String> fileDir = operator.getSubDirAndFiles(type);
				if (fileDir != null) {
					ArrayList<SimHash> middleList = new ArrayList<SimHash>();
					for (int k = 0; k < fileDir.size(); k++) {
						String fileName = fileDir.get(k);
						String res = operator.readFileByLines(fileName);
						
						SimHash tempHash = new SimHash(res, fileName);
						tempHash.generateEle();
						System.out.println(tempHash.getFileName());
						System.out.println(tempHash.getTermFrequency());
						System.out.println(tempHash.getSimHash());
						middleList.add(tempHash);
					}
					if (type.contains("不相似集合")) {
						unsimilarHash.add(middleList);
					} else if (type.contains("相似集合")) {
						similarHash.add(middleList);
					}
					
				}
				
			}
			testPerformance();
			similarHash.clear();
			unsimilarHash.clear();
		}
	}
	
	public void testPerformance() throws IOException {
		// TODO Auto-generated method stub
		
		String writeFile = "simhash_result.csv";
		FileWriter fw = new FileWriter(writeFile);
		fw.write("位数,k值,总相似次数,准确率,召回率,F-value,漏判率,误判率,错误率\n");
		int[] bitLength = new int[]{16, 32, 64};
		
		for (int i = 2; i < bitLength.length; i++) {
			for (int j = 5; j <= 5; j++) {
				comSimHash = new SimHashSimilar();
				comSimHash.setDistThreshold(j);
				testAlgorithm(bitLength[i]);
				//comSimHash.writeResultFile(bitLength[i], j, fw);
			}
		}
		fw.close();
	}	

	public void testRate(int testTimes) throws IOException {
		// TODO Auto-generated method stub
		
		for (int k = 0; k < testTimes; k++) {
			comSimHash = new SimHashSimilar();
			comSimHash.setDistThreshold(5);
			testAlgorithm(64);
		}
	}

	private void testAlgorithm(int bitLength) throws IOException {
		// TODO Auto-generated method stub
		hashBits = bitLength;
		for (int i = 0; i < similarHash.size(); i++) {
			
			ArrayList<SimHash> similar = similarHash.get(i);
			cmpSimilar(  similar,   similar,  true);
			
			ArrayList<SimHash> unsimilar = unsimilarHash.get(i);
			cmpSimilar(  similar, unsimilar, false);
			//cmpSimilar(unsimilar, unsimilar, false);
		}		
	}

	private void cmpSimilar(ArrayList<SimHash> lista, 
			ArrayList<SimHash> listb, boolean status)
			throws IOException {
		// TODO Auto-generated method stub
		
		for (int i = 0; i < lista.size(); i++) {
			SimHash a = lista.get(i);
			if (a.getHashBits() != hashBits) {
				a.setHashBits(hashBits);
				a.generateEle();
			}

			int j = i + 1;
			if (!status)
				j = 0;

			for (; j < listb.size(); j++) {
				SimHash b = listb.get(j);
				if (b.getHashBits() != hashBits) {
					b.setHashBits(hashBits);
					b.generateEle();
				}
				
				comSimHash.setter(status);
				System.out.print(a.getSimHash()+",");
				System.out.print(b.getSimHash()+",");
				System.out.println(a.hammingDistance(b));
				double res = comSimHash.computeSimilar(a, b);
				

				if (res > 0.45) {
					
				}
				
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new IKSegmentation(new StringReader("厦门大学"));

		long timeBegin = System.currentTimeMillis();
		System.out.println(new Date());

		SimHashCompare testFile = new SimHashCompare();

		try {
			testFile.getDataFromDir(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long timeEnd = System.currentTimeMillis();
		System.out.println(new Date());

		System.out.println("所用时间" + (timeEnd - timeBegin));
	}
}
