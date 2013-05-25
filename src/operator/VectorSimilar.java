package operator;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class VectorSimilar extends SimilarOperator{
	
	public double computeAsSet(HashMap<String, Integer> wordFormer,
			HashMap<String, Integer> wordLatter, 
			HashMap<String, Integer> eleFormer,
			HashMap<String, Integer> eleLatter,
			double lambda) {
		double wordSimilar = computeAsSet(wordFormer, wordLatter);
		double eleSimilar = computeAsSet(eleFormer, eleLatter);
		return wordSimilar * lambda + (1 - lambda) * eleSimilar;
	}

	@SuppressWarnings("unused")
	public double computeAsSet(HashMap<String, Integer> mapFormer,
			HashMap<String, Integer> mapLatter) {
		
		int same = 0;
		int sameUnrepeat = 0;
		int formerSize = countMapValue(mapFormer);
		int latterSize = countMapValue(mapLatter);
		int total = formerSize + latterSize;
		
		Iterator<String> keyIter = mapFormer.keySet().iterator();
		
		while (keyIter.hasNext()) {
			String key = keyIter.next();
			
			if (mapLatter.containsKey(key)) {
				same += mapFormer.get(key);
				same += mapLatter.get(key);
				sameUnrepeat ++;
			}
		}
		
		return (double) same / total;
	}

	/*
	 * ͳ�� map����value�ĺ�
	 */
	private int countMapValue(HashMap<String, Integer> map) {
		// TODO Auto-generated method stub
		int count = 0;
		Collection<Integer> mapValues = map.values();
		Iterator<Integer> iterValue = mapValues.iterator();
		
		while (iterValue.hasNext()) {
			count += iterValue.next();
		}
		
		return count;
	}
	
	public double computeAsVector(HashMap<String, Integer> formerMap,
			HashMap<String, Integer> latterMap) {
		// TODO Auto-generated method stub
		if (formerMap.size() != latterMap.size()) {
			throw new java.util.NoSuchElementException("��һ����");
		}
		
		double formerNorm = 0;
		double latterNorm = 0;
		double dotMul = 0;
		Iterator<Integer> formerIter = formerMap.values().iterator();
		Iterator<Integer> latterIter = latterMap.values().iterator();
		
		while (formerIter.hasNext()) {
			int formerValue = formerIter.next();
			int latterValue = latterIter.next();
			dotMul += formerValue * latterValue;
			formerNorm += formerValue * formerValue;
			latterNorm += latterValue * latterValue;
		}
		
		return dotMul / ( Math.sqrt(formerNorm) * Math.sqrt(latterNorm));
	}
	
	public double computeAsVector(int[] formerVector, int[] latterVector) {
		// TODO Auto-generated method stub
		if (formerVector.length != latterVector.length) {
			throw new java.util.NoSuchElementException("��һ����");
		}
		
		double formerNorm = 0;
		double latterNorm = 0;
		double dotMul = 0;
		
		for (int i = 0; i < formerVector.length; i++) {

			dotMul += formerVector[i] * latterVector[i];
			formerNorm += formerVector[i] * formerVector[i];
			latterNorm += latterVector[i] * latterVector[i];
		}
		return dotMul / ( Math.sqrt(formerNorm) * Math.sqrt(latterNorm));
	}

	public double computeAsVector(String formerStr, String latterStr) {
		// TODO Auto-generated method stub
		if (formerStr.length() != latterStr.length()) {
			throw new java.util.NoSuchElementException("��һ����");
		}
		
		double formerNorm = 0;
		double latterNorm = 0;
		double dotMul = 0;
		
		for (int i = 0; i < formerStr.length(); i++) {
			int formerValue = formerStr.charAt(i) - '0';
			int latterValue = latterStr.charAt(i) - '0';
			dotMul += formerValue * latterValue;
			formerNorm += formerValue * formerValue;
			latterNorm += latterValue * latterValue;
		}
		return dotMul / ( Math.sqrt(formerNorm) * Math.sqrt(latterNorm));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> mapFormer = new HashMap<String, Integer> ();
		HashMap<String, Integer> mapLatter = new HashMap<String, Integer> ();
		mapFormer.put("if", 10);
		mapLatter.put("as", 8);
		mapFormer.put("as", 2);
		mapLatter.put("if", 13);
		mapFormer.put("some", 33);
		mapLatter.put("on", 8);
		mapFormer.put("come", 21);
		mapLatter.put("some", 48);
		mapFormer.put("on", 73);
		mapLatter.put("come", 61);
		
		VectorSimilar vectorOperator = new VectorSimilar();
		double similar = vectorOperator.computeAsSet(mapFormer, mapLatter);
		System.out.println(similar);	
		
		similar = vectorOperator.computeAsVector(mapFormer, mapLatter);
		System.out.println(similar);
		
		int a[] = new int[]{1, 2, 3, 4};
		int b[] = new int[]{3, 5, 7, 9};
		similar = vectorOperator.computeAsVector(a, b);
		System.out.println(similar);
		
		String x = "193024";
		String y = "938424";
		similar = vectorOperator.computeAsVector(x, y);
		System.out.println(similar);
	}

	public int hammingDistance(BigInteger formerInt, BigInteger latterInt) {
		// TODO Auto-generated method stub
		BigInteger x = formerInt.xor(latterInt);
		int tot = 0;
		// ͳ��x�ж�����λ��Ϊ1�ĸ���
		// �������룬һ������������ȥ1����ô��������Ǹ�1�������Ǹ�1�����������ȫ�����ˣ�
		// �԰ɣ�Ȼ��n&(n-1)���൱�ڰѺ����������0��
		// ���ǿ�n�������ٴ������Ĳ�����OK�ˡ�
		// �����λ���㣬����Ҳ��֪��JAVA���棬λ����Ч����Ρ�
		while (x.signum() != 0) {
			tot += 1;
			x = x.and(x.subtract(new BigInteger("1")));
		}
		return tot;
	}
}
