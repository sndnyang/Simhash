package term;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import operator.ENSDSimilar;

import org.wltea.analyzer.sample.NewWord;

public class ENSD extends TermFrequencyEle {

	private ArrayList<String> nickName = new ArrayList<String>();
	private String nick = null;

	public ENSD(String str, String fileName) throws IOException {
		// TODO Auto-generated constructor stub
		super(str, fileName);
	}

	@Override
	public void generateEle() throws IOException {
		// TODO Auto-generated method stub
		
		if (termFrequency == null) {
			super.setKeyThreshold();
			super.generateEle();
			if (termFrequency.size() > 4) {
				reduceHighFreq();
			}
		}
		
		// 前一次生成的map的size太小，而后一次又太大，需要中断循环。
		boolean smallToLarge = false;
		effectiveNum = 0;
		
		HashMap<String, Integer> temp = new HashMap<String, Integer>();

		while (true) {
			reduceLowFreq(temp);
			if (temp.size() >= 5 && temp.size() <= 20) {
				break;
			}
			
			if ((keyThreshold <= 1 || smallToLarge) && temp.size() > 20) {
				break;
			}

			if (temp.size() < 5) {
				smallToLarge = true;
				keyThreshold--;
			}

			if (temp.size() > 20) {
				keyThreshold++;
			}
			
			temp.clear();
		}

		termFrequency = temp;

		// 计算新词并写入临时词库中
		nick = NewWord.getNamebyNick(nickName);
		if (nick != null) {
			addNickname(nick);
		}
	}

	private void reduceLowFreq(HashMap<String, Integer> temp) {
		// TODO Auto-generated method stub
		Iterator<Entry<String, Integer>> iterfirst = termFrequency.entrySet()
				.iterator();

		while (iterfirst.hasNext()) {
			Map.Entry<String, Integer> entry = iterfirst.next();
			int val = (Integer) entry.getValue();
			String key = (String) entry.getKey();

			if (val >= this.keyThreshold) {
				temp.put(key, val);
				effectiveNum += val;
			}
		}
	}

	private void addNickname(String strnick) {
		// TODO Auto-generated method stub
		Iterator<Entry<String, Integer>> iterfirst = this.termFrequency
				.entrySet().iterator();
		while (iterfirst.hasNext()) {
			Map.Entry<String, Integer> entryfirst = iterfirst.next();
			int val = (Integer) entryfirst.getValue();
			String str = (String) entryfirst.getKey();

			if (str.equals(strnick)) {
				entryfirst.setValue(val + 5);
			}
		}
	}

	public int size() {
		// TODO Auto-generated method stub
		return termFrequency.size();
	}

	protected void reduceHighFreq() {
		// TODO Auto-generated method stub

		int avg = effectiveNum / 4;
		int overavg = 0;
		int overavgnumber = 0;
		int change = 0;
		int changevalue = 0;

		Iterator<Entry<String, Integer>> iterfirst = termFrequency.entrySet()
				.iterator();
		while (iterfirst.hasNext()) {
			Map.Entry<String, Integer> entryfirst = (Map.Entry<String, Integer>) iterfirst
					.next();
			int valfirst = (Integer) entryfirst.getValue();
			if (valfirst > avg) {
				overavg++;
				overavgnumber += valfirst;
			}
		}
		change = 4 - overavg;
		if (change > 0) {
			changevalue = (effectiveNum - overavgnumber) / change + 1;
		}

		if (overavg > 0) {
			Iterator<Entry<String, Integer>> iterfirsts = termFrequency
					.entrySet().iterator();
			while (iterfirsts.hasNext()) {
				Map.Entry<String, Integer> entryfirst = (Map.Entry<String, Integer>) iterfirsts
						.next();
				int valfirst = entryfirst.getValue();
				if (valfirst > avg) {
					entryfirst.setValue(changevalue);
				}
			}
		}
	}

	public double compareTo(ENSD x) throws IOException {
		return new ENSDSimilar().computeSimilar(this, x);
	}

	public static void main(String[] args) {
		String s = "This is a test string for testing";

		try {
			ENSD temp = new ENSD(s, "temp");

			System.out.println(temp.getTermFrequency());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
