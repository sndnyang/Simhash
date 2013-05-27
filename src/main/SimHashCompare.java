package main;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import operator.ENSDSimilar;
import operator.VectorSimilar;

import org.wltea.analyzer.IKSegmentation;

import term.ENSD;
import term.SimHash;
import term.TermFrequencyEle;
import fileclass.DirectoryOperator;

public class SimHashCompare {
	/* 
	 * 
	 */
	private static String fileName = "E:\\yangxiulong\\Simhash\\分类测试"; //"E:\\data\\体育领域" "测试"
	private static ENSDSimilar comENSD = new ENSDSimilar();
	
	private static ArrayList<SimHash> simhashList = new ArrayList<SimHash>();
	private static ArrayList<ENSD> ENSDList = new ArrayList<ENSD>();
	
	
	private void beginExperiment(String dirPath) {
		// TODO Auto-generated method stub
		getFeatureRecurse(dirPath);
	}

	private void getFeatureRecurse(String dirPath) {
		// TODO Auto-generated method stub
		try {
			initFeatureSet(dirPath);
			compareFeature();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<String> subDirList = new DirectoryOperator()
				.getSubDirList(dirPath);
		for (int i = 0; i < subDirList.size(); i++) {
			getFeatureRecurse(subDirList.get(i));
		}
	}

	private void compareFeature() throws IOException {
		// TODO Auto-generated method stub
		VectorSimilar vector = new VectorSimilar();
		for (int i = 0; i < simhashList.size(); i++) {
			SimHash formerSimhash = simhashList.get(i);
			//ENSD formerENSD = ENSDList.get(i);
			for (int j = i + 1; j < simhashList.size(); j++) {
				SimHash latterSimhash = simhashList.get(j);
				int dis = vector.hammingDistance(formerSimhash.getIntegerHash(),
						latterSimhash.getIntegerHash());
				System.out.println(dis);
				/*
				double fpCosine = vector.computeAsVector(formerSimhash.getSimHash(), 
						latterSimhash.getSimHash());
				double vectorCosine = vector.computeAsVector(formerSimhash.getWeightVector(), 
						latterSimhash.getWeightVector());
				double setSimilar = vector.computeAsSet(formerSimhash.getTermFrequency(), 
						latterSimhash.getTermFrequency());
				
				ENSD latterENSD = ENSDList.get(j);
			
				double resENSD = comENSD.computeSimilar(formerENSD, latterENSD);
		
				
				System.out.print(dis + "," + fpCosine + ",");
				//System.out.print(vectorCosine + "," + setSimilar + ",");
				//System.out.println(resENSD);*/
			}
		}
	}

	private void initFeatureSet(String dirPath) throws IOException {
		// TODO Auto-generated method stub
		DirectoryOperator directory = new DirectoryOperator();
		
		ArrayList<String> subFilesList = directory.getSubFilesList(dirPath);
		
		for (int i = 0; i < subFilesList.size(); i++) {
			String fileName = subFilesList.get(i);
			String content  = directory.readFileByLines(fileName);
			//TermFrequencyEle tempEle = new TermFrequencyEle(content, fileName);
			//tempEle.generateEle();
			SimHash tempSimHash = new SimHash(content, fileName);
			//, tempEle.getTermFrequency());
			tempSimHash.generateEle();
			simhashList.add(tempSimHash);
			
			//ENSD tempENSD = new ENSD(content, fileName);
			//tempENSD.setTermFrequency(tempEle.getTermFrequency());
			//tempENSD.generateEle();
			//ENSDList.add(tempENSD);
			
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


		testFile.beginExperiment(fileName);


		long timeEnd = System.currentTimeMillis();
		System.out.println(new Date());

		System.out.println("所用时间" + (timeEnd - timeBegin));
	}
}
