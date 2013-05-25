package operator;

public class SimilarOperator {

	protected int testSimilar;  	//测试数据集的相似次数
	protected int testUnsimilar;  	//测试数据集的不相似次数
	
	protected int isSimilar;		//相似的测试数据中，被判定为相似的次数
	protected int isUnsimilar;		//不似的测试数据中，被判定为不似的次数

	protected int testTimes;		//总的比较次数
	protected int similarTimes;		//总的相似次数

	protected boolean actualState;	//在测试中，两篇文档原本是相似或不相似？
	
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
	 * 人为说明当前要两篇文档是否相似，
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

		System.out.println("总相似次数为 " + similarTimes);
		System.out.println("相似次数\n测试:实际 " + isSimilar + " : " + testSimilar);
		System.out.println("不相似次数\n测试:实际 " + isUnsimilar + " : "
				+ testUnsimilar);
		System.out.println("测试总次数为 " + testTimes);

		double precise = (double) isSimilar / similarTimes;
		double recall = (double) isSimilar / testSimilar;

		System.out.println("准确率为 " + precise);
		System.out.println("召回率为 " + recall);
		System.out.println("F value is " + 2 * precise * recall
				/ (precise + recall));

	
		System.out.println("漏判率为 " + (1 - recall));
		System.out.println("误判率为 "
				+ (1 - (double) isUnsimilar / testUnsimilar));
		System.out.println("错误率为 "
				+ (2 - recall - (double) isUnsimilar / testUnsimilar) / 2);
		
		System.out.println("模型比较时间  " + duringTime / 1000 + " 秒.");
	}
}
