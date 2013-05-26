package operator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import fileclass.TextContent;
import term.ENSD;

public class ENSDSimilar extends SimilarOperator {

	private static TextContent content = new TextContent();
	
	public ENSDSimilar() {
		super();
		content.getplacename();
		content.getNickname();
	}

	public static class InnerResult {
		// �ؼ�������
		private int wordTotal;
		// �ؼ���ƥ�������
		private int wordMatch;

		// �ؼ�������(���ظ�)
		private int wordTotalUnrepeat;
		// �ؼ���ƥ�������(���ظ�)
		private int wordMatchUnrepeat;

		private boolean isweibo;

		InnerResult(int oneinner, int twoinner, int totalUn, int matchUn) {

			wordTotal = oneinner;
			wordMatch = twoinner;

			wordTotalUnrepeat = totalUn;
			wordMatchUnrepeat = matchUn;

			isweibo = false;
		}

		void setWeiBo(boolean isWeiBo) {
			isweibo = isWeiBo;
		}

		boolean getisWeiBo() {
			return isweibo;
		}

		int getWordTotal() {
			return wordTotal;
		}

		int getWordMatch() {
			return wordMatch;
		}

		int getWordTotalUnrepeat() {
			return wordTotalUnrepeat;
		}

		int getWordMatchUnrepeat() {
			return wordMatchUnrepeat;
		}
	}

	private float computesimulation(InnerResult res) {
		float simular = 0;
		boolean isPerson = false;
		boolean isMovie = false;
		boolean isTime = false;

		simular = (float) (res.getWordMatch()) / res.getWordTotal();

		// // ƥ��Ĺؼ���̫���ˣ����ƾͲ���ͬһƪ����
		if (simular < 0.45) {
			if (res.getisWeiBo()) {
				if (res.wordMatchUnrepeat == 3)
					simular += 0.1;
				// �ؼ�����4������ȵģ�˵����ƪ���½��������Ѿ���������
				else if (res.wordMatchUnrepeat >= 4)
					simular += 0.2;
			} else {
				if (res.wordMatchUnrepeat == 4)
					simular += 0.1;
				if (res.wordMatchUnrepeat == 5)
					simular += 0.15;
				else if (res.wordMatchUnrepeat > 5)
					simular += 0.2;
			}
		}

		ArrayList<String> firstMovieName = content.getItem("movie", 1);
		ArrayList<String> secondMovieName = content.getItem("movie", 2);

		if (firstMovieName.size() > 0 && secondMovieName.size() > 0) {
			for (String strfmn : firstMovieName) {
				if (secondMovieName.contains(strfmn)) {
					isMovie = true;
					break;
				}
			}

			for (String strsmn : secondMovieName) {
				if (firstMovieName.contains(strsmn)) {
					isMovie = true;
					break;
				}
			}
		}

		ArrayList<String> firstPersonName = content.getItem("person", 1);
		ArrayList<String> secondPersonName = content.getItem("person", 2);

		if (firstPersonName.size() > 0 && secondPersonName.size() > 0) {
			for (String strfpn : firstPersonName) {
				if (secondPersonName.contains(strfpn)) {
					isPerson = true;
					break;
				}
			}

			for (String strspn : secondPersonName) {
				if (firstPersonName.contains(strspn)) {
					isPerson = true;
					break;
				}
			}
		}

		ArrayList<String> firstTime = content.getItem("time", 1);
		ArrayList<String> secondTime = content.getItem("time", 2);

		if (firstTime.size() > 0 && secondTime.size() > 0) {
			for (String strfmn : firstTime) {
				if (secondTime.contains(strfmn)) {
					isTime = true;
					break;
				}
			}

			for (String strsmn : secondTime) {
				if (firstTime.contains(strsmn)) {
					isTime = true;
					break;
				}
			}
		}

		if (isMovie) {
			// System.out.println("��Ӱ����ƥ������");
			simular += 0.1;
		}

		if (isPerson) {

			// System.out.println("���������ƥ������");
			simular += 0.1;
		}

		String firstPlace = content.getPlace(1);
		String secondPlace = content.getPlace(2);

		if (firstPlace != null && secondPlace != null) {
			if (firstPlace.equals(secondPlace)) {
				simular += 0.1;
				// System.out.println(firstPlace+secondPlace);
			}
		}

		if (isTime) {

			simular += 0.1;
		}

		if (simular > 1) {
			simular = 1;
		}

		// ȥ������εļ�����������Ӱ����һ�μ���
		firstMovieName.clear();
		secondMovieName.clear();
		firstPersonName.clear();
		secondPersonName.clear();
		firstTime.clear();
		secondTime.clear();
		firstPlace = null;
		secondPlace = null;

		return simular;
	}

	/**
	 * @param a
	 *            ��ʾ��һƪ���µ�����
	 * @param b
	 *            ��ʾ�ڶ�ƪ���µ�����
	 * @return ���صĽ����ʾ��ƪ���µ����ƶ�
	 * @author yancey.yang
	 */
	public double computeSimilar(ENSD a, ENSD b) throws IOException {
		// ����̫�̣��޷��������ƶ�
		if (a.getContent().length() < 10 || b.getContent().length() < 10) {
			System.out.println("����̫�̣��޷��������ƶ�");
			return 0;
		}

		content.getMovieName(a.getContent(), b.getContent());

		boolean isweibo = false;

		InnerResult res = compareMap(a, b);
		
		if (a.getContent().length() < 180 || b.getContent().length() < 180) {
			isweibo = true;
		}

		res.setWeiBo(isweibo);
		double similarity = computesimulation(res);
		recordJudgeResult(similarity);

		return similarity;
	}

	public InnerResult compareMap(ENSD first, ENSD second) {

		int same = 0;
		int sameUnrepeat = 0;
		int total = 0;
		int firstSize = 0;
		int secondSize = 0;

		HashMap<String, Integer> firstMap = first.getTermFrequency();
		HashMap<String, Integer> secondMap = second.getTermFrequency();

		int rf = first.getKeyThreshold();
		int rs = second.getKeyThreshold();
		
		firstSize = sum(firstMap);
		secondSize = sum(secondMap);

		Iterator<Entry<String, Integer>> iterfirst = firstMap.entrySet()
				.iterator();

		while (iterfirst.hasNext()) {
			Map.Entry<String, Integer> entryfirst = (Map.Entry<String, Integer>) iterfirst
					.next();

			String firstKey = (String) entryfirst.getKey();
			int firstValue = (Integer) entryfirst.getValue();

			if (secondMap.containsKey(firstKey)) {
				same += firstValue + secondMap.get(firstKey);
				sameUnrepeat++;
			}
		}
		
		if (rf == 1 && firstMap.size() > 20) {
			firstSize = getTopFiveNumber(firstMap);
		}

		if (rs == 1 && secondMap.size() > 20) {
			secondSize = getTopFiveNumber(secondMap);
		}

		total = secondSize + firstSize;
		InnerResult in = new InnerResult(total, same, firstMap.size()
				+ secondMap.size(), sameUnrepeat);

		return in;
	}

	private int sum(HashMap<String, Integer> map) {
		// TODO Auto-generated method stub
		Iterator<Integer> temp = map.values().iterator();
		int sum = 0;
		while (temp.hasNext()) {
			sum += temp.next();
		}
		return sum;
	}

	private static int getTopFiveNumber(HashMap<String, Integer> map) {
		// TODO Auto-generated method stub
		int topFiveNumber = 0;
		ArrayList<Integer> numList = new ArrayList<Integer>();

		Iterator<Entry<String, Integer>> iterfirst = map.entrySet().iterator();
		while (iterfirst.hasNext()) {
			Map.Entry<String, Integer> entryfirst = (Map.Entry<String, Integer>) iterfirst
					.next();

			int valfirst = (Integer) entryfirst.getValue();

			numList.add(valfirst);
		}

		int numtemp = 0;
		int numtempindex = 0;

		for (int j = 0; j < 5; j++) {
			for (int i = 0; i < numList.size(); i++) {
				if (numtemp < (Integer) numList.get(i)) {
					numtemp = (Integer) numList.get(i);
					numtempindex = i;
				}
			}

			topFiveNumber += numtemp;
			numList.remove(numtempindex);
			numtemp = 0;
		}

		return topFiveNumber;
	}

	public static ArrayList<String> computeNewWord(String str) {
		ArrayList<String> ste = new ArrayList<String>();
		int length = str.length();
		for (int i = 0; i < length - 3; i++) {
			char first = str.charAt(i);
			char second = str.charAt(i + 1);
			char third = str.charAt(i + 2);
			char four = str.charAt(i + 3);
			for (int j = i + 1; j < length - 1; j++) {

				if (first == str.charAt(j) && second == str.charAt(j + 1)) {
					// third = str.charAt(i + 2);
					if (third == str.charAt(j + 2)) {
						if (four == str.charAt(j + 3)) {
							// �ɹ�ƥ��4��������������
							i += 3;
							char same[] = { str.charAt(j), str.charAt(j + 1),
									str.charAt(j + 2), str.charAt(j + 3) };
							String temp = String.copyValueOf(same);
							if (!ste.contains(temp))
								ste.add(String.copyValueOf(same));
						} else
						// �ɹ�ƥ��3�������ĸ���ƥ��
						{
							i += 2;
							char same[] = { str.charAt(j), str.charAt(j + 1),
									str.charAt(j + 2) };
							String temp = String.copyValueOf(same);
							if (!ste.contains(temp))
								ste.add(String.copyValueOf(same));
						}
					} else {
						// �ɹ�ƥ��2������������ƥ��
						i++;
						char same[] = { str.charAt(j), str.charAt(j + 1) };
						String temp = String.copyValueOf(same);
						if (!ste.contains(temp))
							ste.add(String.copyValueOf(same));
					}
				}
			}
		}
		return ste;
	}
}
