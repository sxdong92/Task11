package decisionTree;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Pruning {
	private TreeNode originRoot;
	private List<String> restAttributesList;
	private List<List<String>> testData;
	private TestDT t;
	private double originAccu;
	
	/**
	 * Prune the generated tree with REP.
	 * 
	 * @param root
	 * @param restAttributesList
	 * @param testData
	 * @param t
	 * @param originAccu
	 */
	public Pruning(TreeNode root, List<String> restAttributesList, List<List<String>> testData, TestDT t, double originAccu) {
		this.originRoot = root;
		this.restAttributesList = restAttributesList;
		this.testData = testData;
		this.t = t;
		this.originAccu = originAccu;
	}
	
	/**
	 * Recursive function to prune tree
	 * 
	 * @param root
	 */
	public void pruningWithREP(TreeNode root) {
		if(root == null) return;
		if(root.childrenList.size() == 0) return;
		
		for(int i=0; i<root.childrenList.size(); i++) {
			pruningWithREP(root.childrenList.get(i));
		}
		
		// cache all origin variables in root
		List<TreeNode> cacheChildren = root.childrenList;
		root.childrenList = new ArrayList<TreeNode>();
		
		List<String> cacheIntervals = root.intervalsList;
		root.intervalsList = new ArrayList<String>();
		
		String cacheParaName = root.paraName;
		Hashtable<String, Integer> labelCounter = CounterHelper.countElementsInDiffLabel(root.data);
		root.paraName = CounterHelper.findDominantLabel(labelCounter);
		
		
		double accuracy = t.culculateSingleAccuracy(originRoot, restAttributesList, testData);
		if(accuracy < originAccu) {
			// roll back
			root.childrenList = cacheChildren;
			root.intervalsList = cacheIntervals;
			root.paraName = cacheParaName;
		}
	}
}
