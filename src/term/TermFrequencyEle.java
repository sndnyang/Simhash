package term;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

public class TermFrequencyEle extends SimilarEle {

	protected HashMap<String, Integer> termFrequency = null;
	protected int keyThreshold = 1;
	protected int effectiveNum = 0;

	public TermFrequencyEle(String str, String fileName) throws IOException {
		super(str, fileName);
		// TODO Auto-generated constructor stub
	}

	public HashMap<String, Integer> getTermFrequency() {
		return termFrequency;
	}

	public void setTermFrequency(HashMap<String, Integer> termMap) {
		this.termFrequency = new HashMap<String, Integer>();

		Iterator<Entry<String, Integer>> temp = termMap.entrySet().iterator();
		while (temp.hasNext()) {
			Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) temp
					.next();
			String word = entry.getKey();
			int value = entry.getValue();
			this.termFrequency.put(word, value);
		}
	}

	public void generateEle() throws IOException {
		// TODO Auto-generated method stub
		termFrequency = new HashMap<String, Integer>();

		StringReader reader = new StringReader(content);
		IKSegmentation ikseg = new IKSegmentation(reader);
		Lexeme lex = ikseg.next();

		// ȡ���õ��ķִ�
		while (!(lex == null)) {
			String word = lex.getLexemeText().toString();

			if (word.length() > 1) {
				int newValue = 1;
				if (termFrequency.containsKey(word)) {
					newValue += termFrequency.get(word);
				}
				termFrequency.put(word, newValue);
				effectiveNum++;
			}
			lex = ikseg.next();
		}
	}

	/***
	 * �������������������Ҫ�ؼ��ֵ��ظ��Ĵ��� Ҳ����˵ һ�������ظ��˶��ٴβ����ǹؼ���
	 * 
	 * @param text
	 *            ��ʾ���µ��ַ���
	 * @author yancey.yang
	 **/
	public void setKeyThreshold() {
		int[] decrease = new int[] { 1500, 1050, 750, 400, 200 };
		keyThreshold = 6;
		for (int i = 0; i < decrease.length; i++) {
			if (content.length() < decrease[i]) {
				keyThreshold--;
			} else
				break;
		}
	}

	public int getKeyThreshold() {
		return keyThreshold;
	}
}
