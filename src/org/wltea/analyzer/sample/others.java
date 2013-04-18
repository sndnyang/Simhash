package org.wltea.analyzer.sample;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class others {		
	public static double  A = 2.5;
	
	public static 	HashMap<String, Integer> mapRelativeFirst
					=	new HashMap<String, Integer>();
	
	public static 	HashMap<String, Integer> mapRelativeSecond
					=	new HashMap<String, Integer>();
	
	public static void main(String[] args) throws IOException {		
	
	System.out.println("hello world!");	
	
	//第一步是得到两篇文档的词频和单词向量
	//第二步是计算出相近集
	//第三步是利用余弦公式计算结果	
	HashMap<String, Integer> mapFirst  = new HashMap<String, Integer>();
	HashMap<String, Integer> mapSecond = new HashMap<String, Integer>();
	
//	computeScam(mapFirst,mapSecond);
	}

	public static double computeScam(HashMap<String,Integer> mapFirst, HashMap<String,Integer> mapSecond) {
		double res =0;
		
		computeRelative(mapFirst,mapSecond);
		
		 res =  getScamResult(mapFirst,mapSecond);				
		
		mapRelativeFirst.clear();
		mapRelativeSecond.clear();		
        
		return res;   
	}
	
	public static double computeHfm(HashMap<String,Integer> mapFirst, HashMap<String,Integer> mapSecond) {
		// TODO Auto-generated method stub
		double res = 0;		
		
		res = getHfm(mapFirst,mapSecond);		
//		computeRelative(mapFirst,mapSecond);
//		
//		res =  getScamResult(mapFirst,mapSecond);				
//		
//		mapRelativeFirst.clear();
//		mapRelativeSecond.clear();			
	
		return res;
	}

	private static double getHfm(HashMap<String, Integer> mapFirst,
			HashMap<String, Integer> mapSecond) {
		// TODO Auto-generated method stub
		
		int firstNumber  = 0;
		int secondNumber = 0;
		int fas          = 0;		
		
//		System.out.println("相近集合"+mapRelativeFirst);
//		System.out.println("相近集合"+mapRelativeSecond);
		
		firstNumber  = getNumberAddition(mapFirst);
		secondNumber  = getNumberAddition(mapSecond);
		
		//遍历HashMap数据
		Iterator iterfirst = mapFirst.entrySet().iterator();
		while (iterfirst.hasNext()) {
			Map.Entry entryfirst = (Map.Entry) iterfirst.next();
			String keyfirst = (String) entryfirst.getKey();
			int valfirst = (Integer) entryfirst.getValue();
			
			Iterator itersecond = mapSecond.entrySet().iterator();
			while (itersecond.hasNext()) {
				Map.Entry entrysecond = (Map.Entry) itersecond.next();
				String keysecond = (String) entrysecond.getKey();
				int valsecond = (Integer) entrysecond.getValue();
				if (keyfirst.equals(keysecond)) {	
				
					fas+=valfirst+valsecond;
				}				
			}
		}
		
		return  (float)fas*1.0/(firstNumber+secondNumber);		
	}

	//SCAM算法，余弦公式加选最大的那个
	private static double getScamResult(HashMap<String,Integer> mapFirst, HashMap<String,Integer> mapSecond) {
		// TODO Auto-generated method stub		
		int firstNumber  = 0;
		int secondNumber = 0;
		int fas          = 0;		
		
//		System.out.println("相近集合"+mapRelativeFirst);
//		System.out.println("相近集合"+mapRelativeSecond);
		
		firstNumber  = getNumberMultiplication(mapFirst);
		secondNumber = getNumberMultiplication(mapSecond);
		
		//遍历HashMap数据
		Iterator iterfirst = mapRelativeFirst.entrySet().iterator();
		while (iterfirst.hasNext()) {
			Map.Entry entryfirst = (Map.Entry) iterfirst.next();
			String keyfirst = (String) entryfirst.getKey();
			int valfirst = (Integer) entryfirst.getValue();
			
			Iterator itersecond = mapRelativeSecond.entrySet().iterator();
			while (itersecond.hasNext()) {
				Map.Entry entrysecond = (Map.Entry) itersecond.next();
				String keysecond = (String) entrysecond.getKey();
				int valsecond = (Integer) entrysecond.getValue();
				if (keyfirst.equals(keysecond)) {	
				
					fas+=valfirst*valsecond;
				}				
			}
		}
		
//		System.out.println(firstNumber+","+secondNumber+","+fas);
		return (firstNumber-secondNumber)>0?fas*1.0/secondNumber:fas*1.0/firstNumber;
	}

	private static int getNumberMultiplication(HashMap<String, Integer> mapRelativeFirst2) {
		// TODO Auto-generated method stub
		int number = 0;
		
		//遍历HashMap数据
		Iterator iterfirst = mapRelativeFirst2.entrySet().iterator();
		while (iterfirst.hasNext()) {
			Map.Entry entryfirst = (Map.Entry) iterfirst.next();
			String keyfirst = (String) entryfirst.getKey();
			int valfirst = (Integer) entryfirst.getValue();
			number +=valfirst*valfirst;
		}
		return number;
	}

	private static int getNumberAddition(HashMap<String, Integer> mapRelativeFirst2) {
		// TODO Auto-generated method stub
		int number = 0;
		
		//遍历HashMap数据
		Iterator iterfirst = mapRelativeFirst2.entrySet().iterator();
		while (iterfirst.hasNext()) {
			Map.Entry entryfirst = (Map.Entry) iterfirst.next();
			String keyfirst = (String) entryfirst.getKey();
			int valfirst = (Integer) entryfirst.getValue();
			number +=valfirst;
		}
		return number;
	}
	
	private static void computeRelative(HashMap<String, Integer> mapfirst,
			HashMap<String, Integer> mapsecond) {
		// TODO Auto-generated method stub
    
		//遍历HashMap数据
		Iterator iterfirst = mapfirst.entrySet().iterator();
		while (iterfirst.hasNext()) {
			Map.Entry entryfirst = (Map.Entry) iterfirst.next();
			String keyfirst = (String) entryfirst.getKey();
			int valfirst = (Integer) entryfirst.getValue();
			
			Iterator itersecond = mapsecond.entrySet().iterator();
			while (itersecond.hasNext()) {
				Map.Entry entrysecond = (Map.Entry) itersecond.next();
				String keysecond = (String) entrysecond.getKey();
				int valsecond = (Integer) entrysecond.getValue();
				if (keyfirst.equals(keysecond)) {	
				
					if(isRelative(valfirst,valsecond))
					{
						mapRelativeFirst.put(keyfirst,valfirst);
						mapRelativeSecond.put(keysecond, valsecond);						
					}
				}				
	        }
		}
	}

	private static boolean isRelative(int keyfirst, int keysecond) {
		// TODO Auto-generated method stub
		
		if((A*keyfirst*keysecond-keyfirst*keyfirst-keysecond*keysecond)>0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}	
}
