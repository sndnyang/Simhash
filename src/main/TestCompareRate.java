package main;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import org.wltea.analyzer.IKSegmentation;

import operator.ENSDSimilar;
import operator.VectorSimilar;

import term.SimHash;
import term.TermFrequencyEle;
import term.ENSD;
import fileclass.DirectoryOperator;

public class TestCompareRate {

	/**
	 * @param args
	 */

	private static ENSDSimilar comText = new ENSDSimilar();
	private static ArrayList<ENSD> termList = new ArrayList<ENSD>();
	private static ArrayList<SimHash> simhashList = new ArrayList<SimHash>();

	private int simHashTime = 0;
	private int ENSDTime = 0;
	private int featureSetTime = 0;
	private int compareTimes = 0;

	public void recursiveCompute(String dirPath) throws IOException {
		// TODO Auto-generated method stub

		File f = new File(dirPath);
		String[] files = f.list();

		if (files == null)
			return ;

		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < files.length; i++) {
			File subfile = new File(dirPath, files[i]);
			
			if (subfile.isFile()) {

				list.add(subfile.toString());
			} else if (subfile.isDirectory()) {
				recursiveCompute(subfile.getAbsolutePath());
			}
		}
		
		if (list.size() != 0) {
			testFiles(list);
		}
	}

	public void testFiles(ArrayList<String> fileDir) throws IOException {
		DirectoryOperator operator = new DirectoryOperator();
		
		if (fileDir != null) {
			for (int k = 0; k < fileDir.size(); k++) {

				String fileName = fileDir.get(k);

				String res = operator.readFileByLines(fileName);

				TermFrequencyEle tempEle = new TermFrequencyEle(res, fileName);
				tempEle.generateEle();
				
				SimHash tempSim = new SimHash(res, 
						fileName, tempEle.getTermFrequency());

				simhashList.add(tempSim);
/*
				//*ENSD tempMap = new ENSD(res, 
						fileName);
				tempMap.setTermFrequency(tempEle.getTermFrequency());
				tempMap.generateEle();
				termList.add(tempMap);	*/		
			}
		}
		compareTimes += simhashList.size() * (simhashList.size() - 1) / 2;
		for (int i = 0; i < 100; i++)
			compareEle();

		termList.clear();
		simhashList.clear();
	}

	private void compareEle() throws IOException {
		// TODO Auto-generated method stub
		if (simhashList.size() != termList.size()) {
			throw new java.util.NoSuchElementException();
		}

		int listSize = simhashList.size();

		VectorSimilar operator = new VectorSimilar();
		
		long timeBegin, timeEnd;
		timeBegin = System.currentTimeMillis();
		
		/*for (int i = 0; i < listSize; i++) {
			for (int j = i + 1; j < listSize; j++) {

				ENSD a = termList.get(i);
				ENSD b = termList.get(j);

				comText.computeSimilar(a, b);
			}
		}*/
		
		timeEnd = System.currentTimeMillis();
		ENSDTime += timeEnd - timeBegin;
		timeBegin = timeEnd;
		for (int i = 0; i < listSize; i++) {

			for (int j = i + 1; j < listSize; j++) {
				SimHash hasha = simhashList.get(i);
				SimHash hashb = simhashList.get(j);
				operator.hammingDistance(hasha.getIntegerHash(), 
						hashb.getIntegerHash());
			}
		}
		timeEnd = System.currentTimeMillis();
		simHashTime += timeEnd - timeBegin;
		
		timeBegin = timeEnd;
		
		for (int i = 0; i < listSize; i++) {

			for (int j = i + 1; j < listSize; j++) {
				SimHash hasha = simhashList.get(i);
				SimHash hashb = simhashList.get(j);
				operator.computeAsSet(hasha.getTermFrequency(), 
						hashb.getTermFrequency());
			}
		}
		timeEnd = System.currentTimeMillis();
		featureSetTime += timeEnd - timeBegin;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new IKSegmentation(new StringReader("厦门大学"));

		TestCompareRate classifier = new TestCompareRate();

		long timeBegin = System.currentTimeMillis();
		System.out.println(new Date());

		try {
			classifier.recursiveCompute("E:\\data\\体育领域");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long timeEnd = System.currentTimeMillis();
		System.out.println(new Date());
		System.out.println("比较次数 " + classifier.compareTimes);

		System.out.println("所用时间" + (timeEnd - timeBegin));
		
		System.out.println("所用时间  " + classifier.simHashTime + " " +
		classifier.ENSDTime + " " +classifier.featureSetTime);
	} 
}
