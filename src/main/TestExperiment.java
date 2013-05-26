package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import org.wltea.analyzer.IKSegmentation;

import term.ENSD;
import term.SimHash;
import term.TopicDirectory;
import operator.ENSDSimilar;
import operator.FeatureTextSimilar;
import operator.VectorSimilar;
import fileclass.DirectoryOperator;
import fileclass.FeatureText;

public class TestExperiment {

	private static String[] testDataSet = {
			"E:\\yangxiulong\\Simhash\\testData",
			"E:\\yangxiulong\\Simhash\\分类测试", "E:\\data\\体育领域",
			"E:\\yangxiulong\\Simhash\\小测试", "E:\\yangxiulong\\Simhash\\分类小测试" };

	private ArrayList<String> formerDirList;
	private ArrayList<String> latterDirList;

	int debug = 0;

	VectorSimilar vOperator = new VectorSimilar();
	ENSDSimilar ENSDOperator = new ENSDSimilar();

	private FeatureTextSimilar freqOperator = new FeatureTextSimilar(1);
	private FeatureTextSimilar ensdOperator = new FeatureTextSimilar(2);
	private FeatureTextSimilar simHashOperator = new FeatureTextSimilar(3);
	private BufferedWriter out;
	long freqTime = 0;
	long ENSDTime = 0;
	long simHashTime = 0;

	int totalCmpTimes = 0;
	int testTimes = 1;
	int task;

	private void setTestTimes(int times) {
		// TODO Auto-generated method stub
		if (times < 1) {
			System.out.println("can't not set test time less than 1!");
		}
		this.testTimes = times;
	}

	private void testFindSimilar(String testSet, String trainSet) {
		// TODO Auto-generated method stub

	}

	private void testDistToCosine(String dirPath) throws IOException {
		// TODO Auto-generated method stub
		this.task = 4;
		File file = new File(
				"E:\\yangxiulong\\Simhash\\output\\DistToPrecise.csv");

		FileWriter fw = new FileWriter(file);
		out = new BufferedWriter(fw);
		out.write("汉明距离,fp余弦值,降维余弦值,集合相似度,ENSD相似度,特征数量\r\n");
		getFeatureRecurse(dirPath, 1);
		out.close();
	}

	private void testRate(String dirPath) throws IOException {
		// TODO Auto-generated method stub
		long timeBegin = System.currentTimeMillis();
		System.out.println("速度测试实验，开始时间为 " + new Date());

		getFeatureRecurse(dirPath, 2);

		System.out.println("比较时间  " + (double) simHashTime / 1000 + " 秒.");
		System.out.println("比较时间  " + (double) ENSDTime / 1000 + " 秒.");
		System.out.println("比较时间  " + (double) freqTime / 1000 + " 秒.");
		System.out.println("比较次数  " + totalCmpTimes);
		long timeEnd = System.currentTimeMillis();
		System.out.println("速度测试实验，结束时间为 " + new Date());
		System.out.println("所用时间 " + ((double) timeEnd - timeBegin) / 1000
				+ " 秒.");
	}

	private void getFeatureRecurse(String dirPath, int job) throws IOException {
		// TODO Auto-generated method stub
		ArrayList<FeatureText> filesList = initFeatureSet(dirPath);

		switch (job) {
		case 1:
			cmpFeatureTextSet(filesList, filesList);
			break;
		case 2:
			for (int i = 0; i < testTimes; i++) {
				totalCmpTimes += filesList.size() * (filesList.size() - 1) / 2;
				for (int j = 1; j < 4; j++) {
					this.task = j;
					cmpFeatureTextSet(filesList, filesList);
				}
			}
			break;
		}

		ArrayList<String> subDirList = new DirectoryOperator()
				.getSubDirList(dirPath);
		for (int i = 0; i < subDirList.size(); i++) {
			getFeatureRecurse(subDirList.get(i), job);
		}
	}

	private ArrayList<FeatureText> initFeatureSet(String dirPath) {
		// TODO Auto-generated method stub
		TopicDirectory topicDir = new TopicDirectory(dirPath);
		ArrayList<FeatureText> filesList = topicDir
				.extractFileFeature("frequency");

		for (int i = 0; i < filesList.size(); i++) {
			FeatureText temp = filesList.get(i);
			temp.debug = this.debug;
			if (this.debug != 0) {
				System.out.println(temp.getName());
				System.out.println(temp.getFrequency());
				temp.extractSimHash(64);
				temp.extractENSD();
			}
		}
		return filesList;
	}

	private void testFactor(String dirPath) throws IOException {
		// TODO Auto-generated method stub
		Prepare(dirPath);
		if (formerDirList.size() != latterDirList.size()) {
			System.out.println("一定少了某个话题文件夹了.");
			System.exit(0);
		}

		ArrayList<ArrayList<FeatureText>> formerSet = getTopicSetData(formerDirList);

		ArrayList<ArrayList<FeatureText>> latterSet = getTopicSetData(latterDirList);

		if (formerSet.size() != latterSet.size()) {
			System.out.println("一定少了某个话题文件夹了.");
			System.exit(0);
		}

		cmpTopicSet(formerSet, latterSet);
	}

	private void cmpTopicSet(ArrayList<ArrayList<FeatureText>> formerSet,
			ArrayList<ArrayList<FeatureText>> latterSet) throws IOException {
		// TODO Auto-generated method stub
		String writeFile = "E:\\yangxiulong\\Simhash\\output\\simhash_result.csv";

		FileWriter fw = new FileWriter(writeFile);
		fw.write("位数,k值,总相似次数,准确率,召回率,F-value,漏判率,误判率,错误率\n");

		int[] bitLength = new int[] { 16, 32, 64 };

		task = 3;

		for (int i = 0; i < bitLength.length; i++) {
			for (int j = 0; j <= bitLength[i] / 3; j++) {
				FeatureTextSimilar testSimilar = new FeatureTextSimilar(3);
				testSimilar.setSimHashPara(bitLength[i], j);

				for (int k = 0; k < formerSet.size(); k++) {
					cmpFeatureTextSet(formerSet.get(k), latterSet.get(k));
				}

				testSimilar.writeFactorResult(fw);
			}
		}
		fw.close();
	}

	private void cmpFeatureTextSet(ArrayList<FeatureText> formerList,
			ArrayList<FeatureText> latterList) {
		// TODO Auto-generated method stub

		long start = System.currentTimeMillis();
		for (int i = 0; i < formerList.size(); i++) {

			FeatureText formerDoc = formerList.get(i);
			cmpDocToDir(formerDoc, formerList, i + 1);

			if (formerList != latterList) {
				cmpDocToDir(formerDoc, latterList, 0);
			}
		}

		long end = System.currentTimeMillis();

		switch (task) {
		case 1:
			freqTime += end - start;
			break;
		case 2:
			ENSDTime += end - start;
			break;
		default:
			simHashTime += end - start;
			break;
		}
	}

	private ArrayList<ArrayList<FeatureText>> getTopicSetData(
			ArrayList<String> dirList) {
		// TODO Auto-generated method stub
		ArrayList<ArrayList<FeatureText>> tempSet = new ArrayList<ArrayList<FeatureText>>();

		for (int i = 0; i < dirList.size(); i++) {
			TopicDirectory topic = new TopicDirectory(dirList.get(i));
			tempSet.add(topic.extractFileFeature("simhash"));
		}
		return tempSet;
	}

	private void Prepare(String dirPath) {
		DirectoryOperator dirOper = new DirectoryOperator();
		ArrayList<String> subDir = dirOper.getSubDirList(dirPath);
		formerDirList = dirOper.getSubDirList(subDir.get(0));
		latterDirList = dirOper.getSubDirList(subDir.get(1));
	}

	private void testAccuracy(String dirPath) throws IOException {
		// TODO Auto-generated method stub
		Prepare(dirPath);

		if (formerDirList.size() != latterDirList.size()) {
			System.out.println("一定少了某个话题文件夹了.");
			System.exit(0);
		}

		for (int i = 1; i < 4; i++) {
			task = i;
			testAccuracyAlgorithm(task);
		}
	}

	private FeatureTextSimilar getCurOperator() {
		switch (task) {
		case 1:
			return freqOperator;
		case 2:
			return ensdOperator;
		default:
			return simHashOperator;
		}
	}

	private void testAccuracyAlgorithm(int type) {
		// TODO Auto-generated method stub
		FeatureTextSimilar operator = getCurOperator();

		for (int i = 0; i < formerDirList.size(); i++) {
			TopicDirectory formerTopic = new TopicDirectory(
					formerDirList.get(i));
			TopicDirectory latterTopic = new TopicDirectory(
					latterDirList.get(i));

			String typeName = operator.getType();

			cmpFeatureTextSet(formerTopic.extractFileFeature(typeName),
					latterTopic.extractFileFeature(typeName));
		}

		String[] message = { "词频向量相似情况：", "ENSD相似情况：", "SimHash相似情况：" };
		System.out.println(message[task - 1]);
		operator.showResult();
	}

	private void cmpDocToDir(FeatureText cmpDoc, ArrayList<FeatureText> cmpSet,
			int begin) {
		// TODO Auto-generated method stub
		FeatureTextSimilar operator = getCurOperator();

		for (int i = begin; i < cmpSet.size(); i++) {
			FeatureText cmpToDoc = cmpSet.get(i);
			if (this.task == 4) {
				getAllPara(cmpDoc, cmpToDoc);
				continue;
			}
			if (begin != 0)
				operator.setter(true);
			else
				operator.setter(false);

			double similarity = operator.computeSimilar(cmpDoc, cmpToDoc);

			operator.recordJudgeResult(similarity);
		}
	}

	private void getAllPara(FeatureText formerText, FeatureText latterText) {
		// TODO Auto-generated method stub
		System.out.println(formerText.getName() + " " + latterText.getName());
		SimHash formerHash = formerText.getSimHash();
		SimHash latterHash = latterText.getSimHash();
		int[] temp = formerHash.getWeightVector();
		for (int i = 0; i < temp.length; i++)
			System.out.print(temp[i] + " ");
		System.out.println();
		temp = latterHash.getWeightVector();
		for (int i = 0; i < temp.length; i++)
			System.out.print(temp[i] + " ");
		System.out.println();
		System.out.println(formerHash.getSimHash() + " "
				+ latterHash.getSimHash());

		double setSimilar = vOperator.computeAsSet(
				formerHash.getTermFrequency(), latterHash.getTermFrequency());

		int dis = vOperator.hammingDistance(formerHash.getIntegerHash(),
				latterHash.getIntegerHash());

		double fpCosine = vOperator.computeAsVector(formerHash.getSimHash(),
				latterHash.getSimHash());

		double reducedCosine = vOperator.computeAsVector(
				formerHash.getWeightVector(), latterHash.getWeightVector());

		ENSD a = formerText.extractENSD();
		ENSD b = latterText.extractENSD();
		System.out.println(a.getTermFrequency().size() + " "
				+ b.getTermFrequency().size());
		DecimalFormat df = new DecimalFormat("#.000");

		try {
			double ENSDsimilar = ENSDOperator.computeSimilar(a, b);
			out.append(dis + "," + df.format(fpCosine) + ",");
			out.append(df.format(reducedCosine) + "," + df.format(setSimilar)
					+ ",");
			out.append(df.format(ENSDsimilar) + ",");

			out.append(formerHash.getTermFrequency().size() + ","
					+ latterHash.getTermFrequency().size() + "\r\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param args
	 *            -repeat-time 实验测试进行的循环次数，主要用于数据集较小，重复测试取其平均时间。
	 *            注：循环测试只用于小数据集比较时间的测试实验 -r 递归标志，主要用于多层文件夹的递归读取 -threshold
	 *            SimHash算法的k值的阈值，默认为5 -bits SimHash算法的特征指纹的长度，默认为64
	 * 
	 *            -accuracy 准确度实验,后续参数为，分有相似不相似集合的数据集所在文件夹路径 -rate
	 *            速度测试实验，后续参数为，测试数据集所在文件夹路径 -dist-cosine
	 *            Hamming距离与cosine值对比实验，后续参数同rate -factor bits与k值实验
	 *            -find-similar 相似文本检索的产品， 后续参数有两个，前者是测试集，后者是训练集，可以是单个或多个文件
	 *            注:以上四个参数代表四个实验，同时只能出现一个。不然会很费时
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new IKSegmentation(new StringReader("厦门大学"));
		TestExperiment test = new TestExperiment();

		for (int i = 0; i < args.length; i++) {

			if (args[i].equals("-repeat-time")) {
				int times = Integer.parseInt(args[i + 1]);
				test.setTestTimes(times);
			} else if (args[i].equals("-r")) {
				continue;
			} else if (args[i].equals("-simhash")) {
				continue;
			} else if (args[i].equals("-ENSD")) {
				continue;
			} else if (args[i].equals("-term-frequency")) {
				continue;
			} else if (args[i].equals("-bits")) {
				continue;
			} else if (args[i].equals("-threshold")) {
				continue;
			} else if (args[i].equals("-debug")) {
				test.debug = 1;
			}
		}

		for (int i = 0; i + 1 < args.length; i++) {

			if (args[i].equals("-accuracy")) {
				int setNo = Integer.parseInt(args[i + 1]);
				test.testAccuracy(testDataSet[setNo - 1]);
				break;
			} else if (args[i].contains("-factor")) {
				int setNo = Integer.parseInt(args[i + 1]);
				test.testFactor(testDataSet[setNo - 1]);
				break;
			} else if (args[i].contains("-rate")) {
				int setNo = Integer.parseInt(args[i + 1]);
				test.testRate(testDataSet[setNo - 1]);
				break;
			} else if (args[i].equals("-dist-cosine")) {
				int setNo = Integer.parseInt(args[i + 1]);
				test.testDistToCosine(testDataSet[setNo - 1]);
				break;
			} else if (args[i].equals("-find-similar") && i + 2 < args.length) {
				test.testFindSimilar(args[i + 1], args[i + 2]);
				break;
			}
		}

	}
}
