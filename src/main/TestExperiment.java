package main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;

import org.wltea.analyzer.IKSegmentation;

import term.TopicDirectory;
import operator.FeatureTextSimilar;
import fileclass.DirectoryOperator;
import fileclass.FeatureText;

public class TestExperiment {

	private static String[] testDataSet = { ".\\Simhash\\testData",
			".\\Simhash\\�������", "..\\data\\��������", ".\\Simhash\\С����"};

	private ArrayList<String> formerDirList;
	private ArrayList<String> latterDirList;

	// private boolean isRecursive = true;

	private int testTimes = 1;

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
		DistToCosine test = new DistToCosine();
		test.testInTopic(dirPath);
	}

	private void testRate(String dirPath) {
		// TODO Auto-generated method stub

		long timeBegin = System.currentTimeMillis();
		System.out.println("�ٶȲ���ʵ�飬��ʼʱ��Ϊ " + new Date());
		
		FeatureTextSimilar tester = new FeatureTextSimilar(3);
		DirectoryOperator dirOper = new DirectoryOperator();
		ArrayList<String> subDir = dirOper.getSubDirList(dirPath);
		
		for (int i = 0; i < subDir.size(); i++) {
			ArrayList<String> secondLevel = dirOper.getSubDirList(subDir.get(i));
			for (int j = 0; j < secondLevel.size(); j++) {
				TopicDirectory topicDir = new TopicDirectory(secondLevel.get(i));
				ArrayList<FeatureText> filesList = topicDir.extractFileFeature("simhash");
				
				for (int k = 0; k < testTimes; k++) {
					for (int l = 0; l < filesList.size(); l++) {
						cmpDocToDir(filesList.get(l), filesList, l+1, tester);
					}
				}
			}
		}

		long timeEnd = System.currentTimeMillis();
		System.out.println("�ٶȲ���ʵ�飬����ʱ��Ϊ " + new Date());
		System.out.println("����ʱ��" + (timeEnd - timeBegin));
	}

	private void testFactor(String dirPath) throws IOException {
		// TODO Auto-generated method stub
		Prepare(dirPath);
		if (formerDirList.size() != latterDirList.size()) {
			System.out.println("һ������ĳ�������ļ�����.");
			System.exit(0);
		}
		long timeBegin = System.currentTimeMillis();
		long timeEnd;
		
		ArrayList<ArrayList<FeatureText>> formerSet = 
				getTopicSetData(formerDirList);
		
		ArrayList<ArrayList<FeatureText>> latterSet =
				getTopicSetData(latterDirList);
		
		timeEnd = System.currentTimeMillis();
		System.out.println("����ʱ��" + (timeEnd - timeBegin));
		
		if (formerSet.size() != latterSet.size()) {
			System.out.println("һ������ĳ�������ļ�����.");
			System.exit(0);
		}
		
		cmpTopicSet(formerSet, latterSet);
	}

	private void cmpTopicSet(ArrayList<ArrayList<FeatureText>> formerSet,
			ArrayList<ArrayList<FeatureText>> latterSet) throws IOException {
		// TODO Auto-generated method stub
		String writeFile = "E:\\yangxiulong\\Simhash\\output\\simhash_result.csv";

		FileWriter fw = new FileWriter(writeFile);
		fw.write("λ��,kֵ,�����ƴ���,׼ȷ��,�ٻ���,F-value,©����,������,������\n");
		
		int[] bitLength = new int[] { 16, 32, 64 };

		for (int i = 0; i < bitLength.length; i++) {
			for (int j = 0; j <= bitLength[i] / 3; j++) {
				FeatureTextSimilar testSimilar = new FeatureTextSimilar(3);
				testSimilar.setSimHashPara(bitLength[i], j);
				
				for (int k = 0; k < formerSet.size(); k++) {
					cmpFeatureTextSet(formerSet.get(k), 
							latterSet.get(k), testSimilar);
				}
				
				testSimilar.writeFactorResult(fw);
			}
		}
		fw.close();
	}

	private void cmpFeatureTextSet(ArrayList<FeatureText> formerList,
			ArrayList<FeatureText> latterList, FeatureTextSimilar tester) {
		// TODO Auto-generated method stub
		for (int i = 0; i < formerList.size(); i++) {
			FeatureText formerDoc = formerList.get(i);
				
			cmpDocToDir(formerDoc, formerList, i + 1, tester);
			cmpDocToDir(formerDoc, latterList, 0, tester);
		}
	}

	private ArrayList<ArrayList<FeatureText>> getTopicSetData(
			ArrayList<String> dirList) {
		// TODO Auto-generated method stub
		ArrayList<ArrayList<FeatureText>> tempSet = 
				new ArrayList<ArrayList<FeatureText>>();
		
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
			System.out.println("һ������ĳ�������ļ�����.");
			System.exit(0);
		}
		
		testAccuracyAlgorithm(1);		
		testAccuracyAlgorithm(2);		
		testAccuracyAlgorithm(3);
	}

	private void testAccuracyAlgorithm(int type) {
		// TODO Auto-generated method stub
		FeatureTextSimilar operator = new FeatureTextSimilar(type);
		
		for (int i = 0; i < formerDirList.size(); i++) {
			TopicDirectory formerTopic = new TopicDirectory(formerDirList.get(i));
			TopicDirectory latterTopic = new TopicDirectory(latterDirList.get(i));

			String typeName = operator.getType();

			cmpFeatureTextSet(formerTopic.extractFileFeature(typeName),
					latterTopic.extractFileFeature(typeName), operator);
		}
		
		switch (type) {
		case 1:
			System.out.println("��Ƶ�������������");
			break;
		case 2:
			System.out.println("ENSD���������");
			break;
		default:
			System.out.println("SimHash���������");
			break;
		}
		operator.showResult();
	}

	private void cmpDocToDir(FeatureText cmpDoc, ArrayList<FeatureText> cmpSet,
			int begin, FeatureTextSimilar operator) {
		// TODO Auto-generated method stub
		for (int i = begin; i < cmpSet.size(); i++) {
			FeatureText cmpToDoc = cmpSet.get(i);
			if (begin != 0)
				operator.setter(true);
			else
				operator.setter(false);
			
			double similarity = operator.computeSimilar(cmpDoc, cmpToDoc);
				
			operator.recordJudgeResult(similarity);
		}
	}

	/**
	 * @param args
	 *            -repeat-time ʵ����Խ��е�ѭ����������Ҫ�������ݼ���С���ظ�����ȡ��ƽ��ʱ�䡣
	 *            ע��ѭ������ֻ����С���ݼ��Ƚ�ʱ��Ĳ���ʵ�� -r �ݹ��־����Ҫ���ڶ���ļ��еĵݹ��ȡ -threshold
	 *            SimHash�㷨��kֵ����ֵ��Ĭ��Ϊ5 -bits SimHash�㷨������ָ�Ƶĳ��ȣ�Ĭ��Ϊ64
	 * 
	 *            -accuracy ׼ȷ��ʵ��,��������Ϊ���������Ʋ����Ƽ��ϵ����ݼ������ļ���·�� -rate
	 *            �ٶȲ���ʵ�飬��������Ϊ���������ݼ������ļ���·�� -dist-cosine
	 *            Hamming������cosineֵ�Ա�ʵ�飬��������ͬrate -factor bits��kֵʵ��
	 *            -find-similar �����ı������Ĳ�Ʒ�� ����������������ǰ���ǲ��Լ���������ѵ�����������ǵ��������ļ�
	 *            ע:�����ĸ����������ĸ�ʵ�飬ͬʱֻ�ܳ���һ������Ȼ��ܷ�ʱ
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new IKSegmentation(new StringReader("���Ŵ�ѧ"));
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
