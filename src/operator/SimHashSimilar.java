package operator;

import term.SimHash;

public class SimHashSimilar extends SimilarOperator {
	
	private int dist_threshold = 5;
	private int dist;

	public int getDistThreshold() {
		return dist_threshold;
	}

	public void setDistThreshold(int k) {
		this.dist_threshold = k;
	}

	public double computeSimilar(SimHash a, SimHash b) {
		// TODO Auto-generated method stub

		dist = a.hammingDistance(b);
		
		if (dist <= dist_threshold) {
			return 1;
		} else {
			return 0;
		}
	}
	
	public int getDist() {
		return dist;		
	}
}
