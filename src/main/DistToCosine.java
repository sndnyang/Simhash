package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import operator.ENSDSimilar;
import operator.VectorSimilar;

import term.SimHash;
import term.ENSD;
import term.TopicDirectory;

import fileclass.DirectoryOperator;
import fileclass.FeatureText;


public class DistToCosine {
	
	String dirPath = "分类测试";

	private int compareTimes;
	private int similarTimes;
	BufferedWriter out;
	
	public DistToCosine() {
		
		compareTimes = 0;
		similarTimes = 0;
	}
	
	private void simulate(String dir) throws IOException {
		// TODO Auto-generated method stub
		File file;
		file = new File("E:\\yangxiulong\\DistToPrecise.csv");
		
		FileWriter fw = new FileWriter(file, false);
		out = new BufferedWriter(fw);
		out.write("汉明距离,fp余弦值,降维余弦值,集合相似度,ENSD相似度,特征数量\r\n");
		getFeatureRecurse(dir);
		

		out.close();
		System.out.println("比较次数 " + compareTimes);
		System.out.println("相似次数 " + similarTimes);
	}

	public void testInTopic(String dirPath) throws IOException {
		// TODO Auto-generated method stub

		TopicDirectory topic = new TopicDirectory(dirPath);
			
		ArrayList<FeatureText> textList = topic.extractFileFeature("simhash");
		System.out.println(textList.size());
		distToPrecise(textList);
	}

	private void distToPrecise(ArrayList<FeatureText> textList) throws IOException {
		// TODO Auto-generated method stub
		int listSize = textList.size();
		ENSDSimilar ensdOperator = new ENSDSimilar();
		VectorSimilar vOperator = new VectorSimilar();
		for (int i = 0; i < listSize; i++) {
			FeatureText formerText = textList.get(i);
			
			for (int j = i + 1; j < listSize; j++) {
				FeatureText latterText = textList.get(j);
				
				SimHash formerHash = formerText.extractSimHash(64);
				SimHash latterHash = latterText.extractSimHash(64);
				
				double setSimilar = vOperator.computeAsSet(
						formerHash.getTermFrequency(), 
						latterHash.getTermFrequency());
				
				int dis = vOperator.hammingDistance(
						formerHash.getIntegerHash(), 
						latterHash.getIntegerHash());
				
				double fpCosine = vOperator.computeAsVector(
						formerHash.getSimHash(), 
						latterHash.getSimHash());
				
				double reducedCosine = vOperator.computeAsVector(
						formerHash.getWeightVector(),
						latterHash.getWeightVector());
				
				ENSD a = formerText.extractENSD();	
				ENSD b = latterText.extractENSD();
				
				double ENSDsimilar = ensdOperator.computeSimilar(a, b);

				DecimalFormat df=new DecimalFormat("#.000");
				
				out.append(dis + "," + df.format(fpCosine) + ",");
				out.append(df.format(reducedCosine) + "," 
				+ df.format(setSimilar) + ",");
				out.append(df.format(ENSDsimilar) + ",");
				
				out.append(formerHash.getTermFrequency().size() + "," +
						latterHash.getTermFrequency().size() + "\r\n");
			}
		}
	}
	
	public void getFeatureRecurse(String dirPath) throws IOException {
		// TODO Auto-generated method stub
		
		testInTopic(dirPath);

		ArrayList<String> subDirList = new DirectoryOperator()
				.getSubDirList(dirPath);
		for (int i = 0; i < subDirList.size(); i++) {
			getFeatureRecurse(subDirList.get(i));
		}
	}
	
	public static void main(String[] args) {
		String dir = "E:\\yangxiulong\\Simhash\\分类测试";
		DistToCosine test = new DistToCosine();
		try {
			test.simulate(dir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
