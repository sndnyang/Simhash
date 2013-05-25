package operator;

public class SimilarOperator {

	protected int testSimilar;  	//�������ݼ������ƴ���
	protected int testUnsimilar;  	//�������ݼ��Ĳ����ƴ���
	
	protected int isSimilar;		//���ƵĲ��������У����ж�Ϊ���ƵĴ���
	protected int isUnsimilar;		//���ƵĲ��������У����ж�Ϊ���ƵĴ���

	protected int testTimes;		//�ܵıȽϴ���
	protected int similarTimes;		//�ܵ����ƴ���

	protected boolean actualState;	//�ڲ����У���ƪ�ĵ�ԭ�������ƻ����ƣ�
	
	protected long duringTime;

	public SimilarOperator() {
		testSimilar = 0;
		testUnsimilar = 0;
		isSimilar = 0;
		isUnsimilar = 0;

		testTimes = 0;
		similarTimes = 0;

		actualState = true;
		duringTime = 0;
	}


	/*
	 * ��Ϊ˵����ǰҪ��ƪ�ĵ��Ƿ����ƣ�
	 */
	public void setter(boolean status) {
		actualState = status;
	}

	public void recordJudgeResult(double res) {
		
		testTimes++;
		
		if (actualState) {
			testSimilar++;
			
			if (res > 0.45) {
				isSimilar++;
				similarTimes++;
			}
			
		} else {
			testUnsimilar++;
			
			if (res < 0.45) {
				isUnsimilar++;
			} else {
				similarTimes++;
			}
		}

	}

	public void showResult() {
		// TODO Auto-generated method stub

		System.out.println("�����ƴ���Ϊ " + similarTimes);
		System.out.println("���ƴ���\n����:ʵ�� " + isSimilar + " : " + testSimilar);
		System.out.println("�����ƴ���\n����:ʵ�� " + isUnsimilar + " : "
				+ testUnsimilar);
		System.out.println("�����ܴ���Ϊ " + testTimes);

		double precise = (double) isSimilar / similarTimes;
		double recall = (double) isSimilar / testSimilar;

		System.out.println("׼ȷ��Ϊ " + precise);
		System.out.println("�ٻ���Ϊ " + recall);
		System.out.println("F value is " + 2 * precise * recall
				/ (precise + recall));

	
		System.out.println("©����Ϊ " + (1 - recall));
		System.out.println("������Ϊ "
				+ (1 - (double) isUnsimilar / testUnsimilar));
		System.out.println("������Ϊ "
				+ (2 - recall - (double) isUnsimilar / testUnsimilar) / 2);
		
		System.out.println("ģ�ͱȽ�ʱ��  " + duringTime / 1000 + " ��.");
	}
}
