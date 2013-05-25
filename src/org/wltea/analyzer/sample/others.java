package org.wltea.analyzer.sample;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class others {		
	public static double  A = 2.5;
	
	public static 	HashMap<String, Integer> mapRelativeFirst
					=	new HashMap<String, Integer>();
	
	public static 	HashMap<String, Integer> mapRelativeSecond
					=	new HashMap<String, Integer>();

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
		
//		System.out.println("�������"+mapRelativeFirst);
//		System.out.println("�������"+mapRelativeSecond);
		
		firstNumber  = getNumberAddition(mapFirst);
		secondNumber  = getNumberAddition(mapSecond);
		
		//����HashMap����
		Iterator<Entry<String, Integer>> iterfirst = mapFirst.entrySet().iterator();
		while (iterfirst.hasNext()) {
			Map.Entry<String, Integer> entryfirst = (Map.Entry<String, Integer>) iterfirst.next();
			String keyfirst = (String) entryfirst.getKey();
			int valfirst = (Integer) entryfirst.getValue();
			
			Iterator<Entry<String, Integer>> itersecond = mapSecond.entrySet().iterator();
			while (itersecond.hasNext()) {
				Map.Entry<String, Integer> entrysecond = (Map.Entry<String, Integer>) itersecond.next();
				String keysecond = (String) entrysecond.getKey();
				int valsecond = (Integer) entrysecond.getValue();
				if (keyfirst.equals(keysecond)) {	
				
					fas+=valfirst+valsecond;
				}				
			}
		}
		
		return  (float)fas*1.0/(firstNumber+secondNumber);		
	}

	//SCAM�㷨�����ҹ�ʽ��ѡ�����Ǹ�
	private static double getScamResult(HashMap<String,Integer> mapFirst, HashMap<String,Integer> mapSecond) {
		// TODO Auto-generated method stub		
		int firstNumber  = 0;
		int secondNumber = 0;
		int fas          = 0;		
		
//		System.out.println("�������"+mapRelativeFirst);
//		System.out.println("�������"+mapRelativeSecond);
		
		firstNumber  = getNumberMultiplication(mapFirst);
		secondNumber = getNumberMultiplication(mapSecond);
		
		//����HashMap����
		Iterator<Entry<String, Integer>> iterfirst = mapRelativeFirst.entrySet().iterator();
		while (iterfirst.hasNext()) {
			Map.Entry<String, Integer> entryfirst = (Entry<String, Integer>) iterfirst.next();
			String keyfirst = (String) entryfirst.getKey();
			int valfirst = (Integer) entryfirst.getValue();
			
			Iterator<Entry<String, Integer>> itersecond = mapRelativeSecond.entrySet().iterator();
			while (itersecond.hasNext()) {
				Map.Entry<String, Integer> entrysecond = (Map.Entry<String, Integer>) itersecond.next();
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
		
		//����HashMap����
		Iterator<Entry<String, Integer>> iterfirst = mapRelativeFirst2.entrySet().iterator();
		while (iterfirst.hasNext()) {
			Map.Entry<String, Integer> entryfirst = (Map.Entry<String, Integer>) iterfirst.next();
			int valfirst = (Integer) entryfirst.getValue();
			number +=valfirst*valfirst;
		}
		return number;
	}

	private static int getNumberAddition(HashMap<String, Integer> mapRelativeFirst2) {
		// TODO Auto-generated method stub
		int number = 0;
		
		//����HashMap����
		Iterator<Entry<String, Integer>> iterfirst = mapRelativeFirst2.entrySet().iterator();
		while (iterfirst.hasNext()) {
			Map.Entry<String, Integer> entryfirst = (Map.Entry<String, Integer>) iterfirst.next();
			int valfirst = (Integer) entryfirst.getValue();
			number +=valfirst;
		}
		return number;
	}
	
	private static void computeRelative(HashMap<String, Integer> mapfirst,
			HashMap<String, Integer> mapsecond) {
		// TODO Auto-generated method stub
    
		//����HashMap����
		Iterator<Entry<String, Integer>> iterfirst = mapfirst.entrySet().iterator();
		while (iterfirst.hasNext()) {
			Map.Entry<String, Integer> entryfirst = (Map.Entry<String, Integer>) iterfirst.next();
			String keyfirst = (String) entryfirst.getKey();
			int valfirst = (Integer) entryfirst.getValue();
			
			Iterator<Entry<String, Integer>> itersecond = mapsecond.entrySet().iterator();
			while (itersecond.hasNext()) {
				Map.Entry<String, Integer> entrysecond = (Map.Entry<String, Integer>) itersecond.next();
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
