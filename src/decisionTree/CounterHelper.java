package decisionTree;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class CounterHelper {

	public static Hashtable<String, Integer> countElementsInDiffLabel(List<List<String>> data) {
		Hashtable<String, Integer> labelCounter = new Hashtable<String, Integer>();
		
		String label = "";
		List<String> row = null;
		
		for(int i=0; i<data.size(); i++) {
			row = data.get(i);
			label = row.get(row.size() - 1);

			if(labelCounter.containsKey(label)) { 
				labelCounter.put(label, labelCounter.get(label) + 1);
			} 
			else {
				labelCounter.put(label, 1);
			}
		}
		
		return labelCounter;
	}
	
	
	public static String findDominantLabel(Hashtable<String, Integer> labelCounter) {
		String dominantLabel = "";
		
		int max = 0;
		for(String key : labelCounter.keySet()) {
			if(labelCounter.get(key) > max) {
				max = labelCounter.get(key);
				dominantLabel = key;
			}
		}
		
		return dominantLabel;
	}
	
	
	public static List<String> getIntervals(List<List<String>> datas, int index) {
		List<String> intervals = new ArrayList<String>();
		
		String r = "";
		for (int i = 0; i < datas.size(); i++) {
			r = datas.get(i).get(index);
			if (!intervals.contains(r)) {
				intervals.add(r);
			}
		}
		
		return intervals;
	}
	
	
	public static List<List<String>> getSubset(int index, String interval, List<List<String>> data) {
		List<List<String>> subset = new ArrayList<List<String>>();
		
		List<String> curRow = new ArrayList<String>();
		for(int i = 0; i < data.size(); i++) {
			curRow = data.get(i);
			if(curRow.get(index).equals(interval)) {
				subset.add(new ArrayList<String>(data.get(i)));
			}
		}
		
		return subset;
	}
}
