package main;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import operator.ENSDSimilar;

import org.wltea.analyzer.IKSegmentation;

import term.ENSD;

import fileclass.DirectoryOperator;

public class ENSDJudge {
	/* 
	 * 
	 */
	private static String fileName = "E:\\data\\测试"; //"E:\\data\\体育领域" "测试"

	private static ENSDSimilar comTerm = new ENSDSimilar();
	private static ArrayList<ENSD> similarMap = new ArrayList<ENSD>();
	private static ArrayList<ENSD> unsimilarMap = new ArrayList<ENSD>();

	public void getData(String strPath) throws IOException {
		// TODO Auto-generated method stub

		DirectoryOperator operator = new DirectoryOperator();
		List<String> topicDir = operator.getSubDirAndFiles(strPath);
		int count = 0;

		for (int i = 0; i < topicDir.size(); i++) {
			List<String> typeDir = operator.getSubDirAndFiles(topicDir.get(i));
			for (int j = 0; j < typeDir.size(); j++) {
				String type = typeDir.get(j);
				List<String> fileDir = operator.getSubDirAndFiles(type);

				if (fileDir != null) {
					for (int k = 0; k < fileDir.size(); k++) {
						String fileName = fileDir.get(k);
						String res = operator.readFileByLines(fileName);
						
						ENSD tempMap = new ENSD(res,
								fileName);
						tempMap.generateEle();
						
						if (type.contains("不相似集合")) {
							unsimilarMap.add(tempMap);
						} else if (type.contains("相似集合")) {
							similarMap.add(tempMap);
						}
					}
				}
			}
			count += unsimilarMap.size() * similarMap.size() 
					+ similarMap.size() * (similarMap.size() - 1);
			testAlgorithm(unsimilarMap, similarMap);

			similarMap.clear();
			unsimilarMap.clear();
		}
		System.out.println(count);
	}

	private void testAlgorithm(ArrayList<ENSD> unsimilar, 
			ArrayList<ENSD> similar) throws IOException {
		// TODO Auto-generated method stub
		//cmpSimilar(unsimilar, unsimilar, false);
		cmpSimilar(similar  , unsimilar, false);
		cmpSimilar(similar  ,   similar,  true);
	}

	private void cmpSimilar(ArrayList<ENSD> lista,
			ArrayList<ENSD> listb, boolean status)
			throws IOException {
		// TODO Auto-generated method stub
		
		for (int i = 0; i < lista.size(); i++) {
			ENSD a = lista.get(i);

			int j = i + 1;
			if (!lista.equals(listb))
				j = 0;

			for (; j < listb.size(); j++) {
				ENSD b = listb.get(j);
				double res;
				comTerm.setter(status);
				res = comTerm.computeSimilar(a, b);
				System.out.println(res);
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

		ENSDJudge testFile = new ENSDJudge();

		try {
			testFile.getData(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		comTerm.showResult();
		//comSimHash.showResult();

		long timeEnd = System.currentTimeMillis();
		System.out.println(new Date());

		System.out.println("所用时间" + (timeEnd - timeBegin));
	}
}
