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
	 * 使用文檔得到的特征集合，
	 * 對每個特征hash后，
	 * 根據權重，綜合得到一個 hashbits位的向量。
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
	 * 根據特征向量里值的正負，得到最終的fingerprint。
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
	 * hash 函數
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

			//只取 0 - 2^hashbits-1 之間的值，所以要 求模 
			BigInteger mask = new BigInteger("2").pow(this.hashbits).subtract(
					new BigInteger("1"));
			
			for (char item : sourceArray) {
				BigInteger temp = BigInteger.valueOf((long) item);
				
				//計算公式  x = (x * m) ^ a[i]  % mask  繁体字……汗，eclipse的快捷键和输入法冲突了
				x = x.multiply(m).xor(temp).and(mask);
			}
			
			//再考虑字符串的长度因素，增强hash的均匀效果
			x = x.xor(new BigInteger(String.valueOf(source.length())));
			
			if (x.equals(new BigInteger("-1"))) {
				//这个确实不知道为什么了……
				x = new BigInteger("-2");
			}
			
			return x;
		}
	}

		/*
	 * calculate Hamming Distance between two strings 二进制怕有错，当成字符串，作一个，比较下结果
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
	 * 取两个二进制的异或，统计为1的个数，就是海明距离
	 * @param other
	 * @return
	 */
	public int hammingDistance(SimHash other) {
		
		BigInteger x = this.intSimHash.xor(other.intSimHash);
		int tot = 0;
		// 统计x中二进制位数为1的个数
		// 我们想想，一个二进制数减去1，那么，从最后那个1（包括那个1）后面的数字全都反了，
		// 对吧，然后，n&(n-1)就相当于把后面的数字清0，
		// 我们看n能做多少次这样的操作就OK了。
		// 真会用位运算，不过也不知道JAVA里面，位运算效率如何。
		while (x.signum() != 0) {
			tot += 1;
			x = x.and(x.subtract(new BigInteger("1")));
		}
		return tot;
	}

	/*
	 * 如果海明距离取3，则分成四块，并得到每一块的bigInteger值 ，作为索引值使用
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
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
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