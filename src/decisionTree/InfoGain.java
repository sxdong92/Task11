package decisionTree;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class InfoGain {
	/** subset of data affiliated with this node */
	private List<List<String>> data = new ArrayList<List<String>>();
	/** rest of attributes set affiliated with this node */
	private List<String> restAttributesList = new ArrayList<String>(); 

	
	public InfoGain(List<List<String>> datas, List<String> attrList) {
		this.data = datas;
		this.restAttributesList = attrList;
	}
	
	/**
	 * Calculate the total entropy of the data set before splitting
	 * 
	 * @param labelCounter
	 * @param size
	 * @return
	 */
	public double calculateTotalEntropy(Map<String, Integer> labelCounter, int size) {
		double totalEtropy = 0.0;
		
		for(String str : labelCounter.keySet()) {
			double p = (double) labelCounter.get(str) / (double) size;
			totalEtropy += ((-1) * p * (Math.log(p) / Math.log(2)));
		}
		
		return totalEtropy;
	}

	/**
	 * Find which attribute to use based on the largest information gain
	 * 
	 * @param totalEntropy
	 * @return
	 */
	public int choiceOfAttribute(double totalEntropy) {
		int index = -1;
		
		double maxInfoGain = 0.0;
		
		for(int i=0; i<restAttributesList.size()-1; i++) {
			double curEntropy = calculateNewEntropy(i);
			double curInfoGain = totalEntropy - curEntropy;
			
			if(curInfoGain > maxInfoGain) {
				maxInfoGain = curInfoGain;
				index = i;
			}
		}
		
		return index;
	}
	
	/**
	 * Calculate entropy of each branch
	 * 
	 * @param index
	 * @return
	 */
	public double calculateNewEntropy(int index) {
		double newEntropy = 0.0;
		
		List<String> intervals = CounterHelper.getIntervals(data, index);
		for(int i=0; i<intervals.size(); i++) {
			List<List<String>> subset = CounterHelper.getSubset(index, intervals.get(i), data);
			Hashtable<String, Integer> labelCounter = CounterHelper.countElementsInDiffLabel(subset);
			
			double p = ((double) subset.size()) / ((double) data.size());
			double curEntropy = 0.0;
			
			for(String str : labelCounter.keySet()) {
				double innerP = (double) labelCounter.get(str) / (double) subset.size();
				curEntropy += ((-1) * innerP * (Math.log(innerP) / Math.log(2)));
			}
			
			newEntropy += p * curEntropy;
		}
		
		return newEntropy;
	}
	
}

