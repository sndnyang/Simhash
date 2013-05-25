package operator;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import fileclass.FeatureText;

public class FeatureTextSimilar extends SimilarOperator{
	private int intType;
	private String type;
	public SimilarOperator operator;
	private int bits = 64;
	private int kThreshold = 5;
	
	public FeatureTextSimilar(int type) {
		// TODO Auto-generated constructor stub
		super();
		this.intType = type;
		switch (type) {
		case 1:
			this.type = "frequency";
			operator = new VectorSimilar();
			break;
		case 2:
			this.type = "ENSD";
			operator = new ENSDSimilar();
			break;
		default:
			this.type = "simhash";
			operator = new SimHashSimilar();
			break;
		}
	}
	
	public void setSimHashPara(int bits, int k) {
		this.bits = bits;
		this.kThreshold = k;
	}
	
	public double computeSimilar(FeatureText cmpDoc, FeatureText cmpToDoc) {
		// TODO Auto-generated method stub
		double similarity = 0;
		long beginTime =  System.currentTimeMillis();
		switch (intType) {
		case 1:
			this.type = "frequency";
			similarity = ((VectorSimilar) operator).
					computeAsSet(cmpDoc.getFrequency(), 
							cmpToDoc.getFrequency());
			break;
		case 2:
			this.type = "ENSD";
			try {
				similarity = ((ENSDSimilar) operator).
						computeSimilar(cmpDoc.extractENSD(), 
								cmpToDoc.extractENSD());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			this.type = "simhash";
			((SimHashSimilar) operator).setDistThreshold(kThreshold);
			similarity = ((SimHashSimilar) operator).
					computeSimilar(cmpDoc.extractSimHash(bits), 
							cmpToDoc.extractSimHash(bits));
			break;
		}
		long endTime =  System.currentTimeMillis();
		this.duringTime = endTime - beginTime;
		
		return similarity;
	}
	
	public String getType() {
		return this.type;
	}

	public void writeFactorResult(FileWriter fw) throws IOException {
		// TODO Auto-generated method stubs
		
		double precise = (double) isSimilar / similarTimes;
		double recall = (double) isSimilar / testSimilar;
		double f_value = 2 * precise * recall / (precise + recall);

		double leak = 1 - recall;
		double wrongJudge = 1 - (double) isUnsimilar / testUnsimilar;
		double errorRate = (leak + wrongJudge) / 2;		
		
		DecimalFormat df=new DecimalFormat("#.000");
		fw.append(this.bits + "," + this.kThreshold + ",");
		fw.append(this.similarTimes + ",");
		fw.append(df.format(precise) + ",");
		fw.append(df.format(recall) + ",");
		fw.append(df.format(f_value) + ",");
		fw.append(df.format(leak) + ",");
		fw.append(df.format(wrongJudge) + ",");
		fw.append(df.format(errorRate) + "\n");
	}
}
