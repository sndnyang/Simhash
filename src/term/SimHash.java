package term;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import operator.SimHashSimilar;

public class SimHash extends TermFrequencyEle {

	public BigInteger intSimHash;
	private String strSimHash;
	private int[] weightVector;
	private int hashbits = 64;

	public SimHash(String str, String fileName) throws IOException {
		// TODO Auto-generated constructor stub
		super(str, fileName);
	}

	public SimHash(String str, String fileName, int hashbits)
			throws IOException {
		// TODO Auto-generated constructor stub
		super(str, fileName);
		this.hashbits = hashbits;
	}
	
	public SimHash(String str, String fileName,
			HashMap<String, Integer> termFrequency) throws IOException {
		// TODO Auto-generated constructor stub
		super(str, fileName);
		this.setTermFrequency(termFrequency);
		generateEle();
	}

	@Override
	public void generateEle() throws IOException {
		// TODO Auto-generated method stub
		if (termFrequency == null) {
			super.generateEle();
		}
		simHash();
	}

	public String simHash() {
		
		int[] v = genWeightVector();
		this.strSimHash = genCorrespondBits(v);
		weightVector = v;
		
		return strSimHash;
	}
	
	/*
	 * ʹ���ęn�õ����������ϣ�
	 * ��ÿ������hash��
	 * �������أ��C�ϵõ�һ�� hashbitsλ��������
	 */
	private int[] genWeightVector() {
		
		int[] v = new int[this.hashbits];

		Iterator<Entry<String, Integer>> iterator = termFrequency
				.entrySet().iterator();

		while (iterator.hasNext()) {
			
			Map.Entry<String, Integer> entry = iterator.next();
			
			String key = entry.getKey();
			int val = (Integer) entry.getValue();

			BigInteger hashKey = this.hash(key);

			for (int i = 0; i < this.hashbits; i++) {
				BigInteger bitmask = new BigInteger("1").shiftLeft(i);
				
				if (hashKey.and(bitmask).signum() != 0) {
					v[i] += val;
				} else {
					v[i] -= val;
				}
			}
		}
		
		return v;
	}
	
	/*
	 * ��������������ֵ����ؓ���õ���K��fingerprint��
	 * @param v
	 * @return
	 */
	private String genCorrespondBits(int[] v) {
		
		StringBuffer fingerPrint = new StringBuffer();
		BigInteger simhashDec = new BigInteger("0");
		
		for (int i = 0; i < this.hashbits; i++) {
			if (v[i] > 0) {
				simhashDec = simhashDec.add(new BigInteger("1").shiftLeft(i));
				fingerPrint.append("1");
			} else {
				fingerPrint.append("0");
			}
		}
		
		this.intSimHash = simhashDec;
		
		return fingerPrint.toString();
	}

	/*
	 * hash ����
	 * @param source
	 * @return 
	 */
	private BigInteger hash(String source) {

		if (source == null || source.length() == 0) {
			return new BigInteger("0");
		} else {
			char[] sourceArray = source.toCharArray();
			BigInteger x = BigInteger.valueOf(((long) sourceArray[0]) << 7);
			
			BigInteger m = new BigInteger("1000003");

			//ֻȡ 0 - 2^hashbits-1 ֮�g��ֵ������Ҫ ��ģ 
			BigInteger mask = new BigInteger("2").pow(this.hashbits).subtract(
					new BigInteger("1"));
			
			for (char item : sourceArray) {
				BigInteger temp = BigInteger.valueOf((long) item);
				
				//Ӌ�㹫ʽ  x = (x * m) ^ a[i]  % mask  �����֡�������eclipse�Ŀ�ݼ������뷨��ͻ��
				x = x.multiply(m).xor(temp).and(mask);
			}
			
			//�ٿ����ַ����ĳ������أ���ǿhash�ľ���Ч��
			x = x.xor(new BigInteger(String.valueOf(source.length())));
			
			if (x.equals(new BigInteger("-1"))) {
				//���ȷʵ��֪��Ϊʲô�ˡ���
				x = new BigInteger("-2");
			}
			
			return x;
		}
	}

		/*
	 * calculate Hamming Distance between two strings ���������д������ַ�������һ�����Ƚ��½��
	 *
	 * @author
	 * @param str1
	 *            the 1st string
	 * @param str2
	 *            the 2nd string
	 * @return Hamming Distance between str1 and str2
	 */
	public int getDistance(String str1, String str2) {
		int distance;
		if (str1.length() != str2.length()) {
			distance = this.hashbits + 1;
		} else {
			distance = 0;
			for (int i = 0; i < str1.length(); i++) {
				if (str1.charAt(i) != str2.charAt(i)) {
					distance++;
				}
			}
		}
		return distance;
	}
	
	/*
	 * ȡ���������Ƶ����ͳ��Ϊ1�ĸ��������Ǻ�������
	 * @param other
	 * @return
	 */
	public int hammingDistance(SimHash other) {
		
		BigInteger x = this.intSimHash.xor(other.intSimHash);
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

	/*
	 * �����������ȡ3����ֳ��Ŀ飬���õ�ÿһ���bigIntegerֵ ����Ϊ����ֵʹ��
	 * 
	 * @param simHash
	 * 
	 * @param distance
	 * 
	 * @return
	 */
	public List<BigInteger> subByDistance(SimHash simHash, int distance) {
		int numEach = this.hashbits / (distance + 1);
		ArrayList<BigInteger> characters = new ArrayList<BigInteger>();
		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < this.intSimHash.bitLength(); i++) {
			boolean sr = simHash.intSimHash.testBit(i);
			if (sr) {
				buffer.append("1");
			} else {
				buffer.append("0");
			}
			if ((i + 1) % numEach == 0) {
				BigInteger eachValue = new BigInteger(buffer.toString(), 2);
				buffer.delete(0, buffer.length());
				characters.add(eachValue);
			}
		}
		return characters;
	}

	public String getSimHash() {
		return strSimHash;
	}
	
	public void setHashBits(int bits) {
		this.hashbits = bits;
	}
	
	public int getHashBits() {
		return hashbits;
	}
	
	public BigInteger getIntegerHash() {
		return intSimHash;
	}
	
	public int[] getWeightVector() {
		return weightVector;
	}

	@Override
	public double compareTo(SimilarEle x) {
		// TODO Auto-generated method stub
		return new SimHashSimilar().computeSimilar(this, (SimHash) x);
	}

	public static void main(String[] args) {
		
		try {
			
			int bitLength = 32;
			String fileName = "E:\\yangxiulong\\python_simhash\\of_study.txt";
			File file = new File(fileName);
			BufferedReader reader = null;
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			String result = "";
			// һ�ζ���һ�У�ֱ������nullΪ�ļ�����
			while ((tempString = reader.readLine()) != null) {
				// ��ʾ�к�
				result += tempString;
			}
			reader.close();
			

			SimHash hash1 = new SimHash(result, null, bitLength);
			System.out.println(hash1.strSimHash);
			System.out.println(hash1.intSimHash + " "
					+ hash1.intSimHash.bitLength());
			hash1.subByDistance(hash1, 3);

			System.out.println("\n");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}