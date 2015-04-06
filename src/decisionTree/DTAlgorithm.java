package decisionTree;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public class DTAlgorithm {
	
	/**
	 * Recursive function to generate decision tree
	 * 
	 * @param data
	 * @param restAttributesList
	 * @return
	 */
	public TreeNode generateDT(List<List<String>> data, List<String> restAttributesList) {
		TreeNode root = new TreeNode();
		root.setData(data);
		root.setRestAttributesList(restAttributesList);
		
		//get elements number in different label
		Hashtable<String, Integer> labelCounter = CounterHelper.countElementsInDiffLabel(data);
		
		//stop growing conditions
		//because the rest attributes list contain at least the label column, so set it <= 1
		if(labelCounter.size() <= 1 || restAttributesList.size() <= 1) {
			root.setParaName(CounterHelper.findDominantLabel(labelCounter));
			return root;
		}
		
		//calculate information gain, find the best attribute
		InfoGain infoGain = new InfoGain(data, restAttributesList);
		double totalEntropy = infoGain.calculateTotalEntropy(labelCounter, data.size());
		int choice = infoGain.choiceOfAttribute(totalEntropy);
		
		//if no attribute can enhance the information gain, then stop
		if(choice == -1) {
			root.setParaName(CounterHelper.findDominantLabel(labelCounter));
			return root;
		}
		
		List<String> intervals = CounterHelper.getIntervals(data, choice); 
		root.setIntervalsList(intervals); 
		root.setParaName(restAttributesList.get(choice));

		//recurring to generate tree, depth first
		for(int i=0; i<intervals.size(); i++) {
			String interval = intervals.get(i);
			List<List<String>> subset = CounterHelper.getSubset(choice, interval, data);
			for (int j = 0; j < subset.size(); j++) {
				subset.get(j).remove(choice);
			}
			
			List<String> newAttributesList = new ArrayList<String>(restAttributesList);
			newAttributesList.remove(choice);
			
			if(subset.size() == 0) {
				TreeNode leaf = new TreeNode();
				leaf.setData(subset);
				leaf.setRestAttributesList(newAttributesList);
				root.getChildrenList().add(leaf);
			} 
			else {
				TreeNode newNode = generateDT(subset, newAttributesList);
				root.getChildrenList().add(newNode);
			}
		}
		return root;
	}
	
}

