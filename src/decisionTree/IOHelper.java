package decisionTree;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IOHelper {
	public static final int SPLIT_TEST_NO = 10;
	public static final String TRAIN_SET = "src/aData/trainProdSelection.arff";
	
	/**
	 * Read the list of all attributes
	 * 
	 * @return
	 */
	public static List<String> getAttributesList() {
		List<String> attributesList = new ArrayList<String>();
		
		try {
			FileInputStream file = new FileInputStream(TRAIN_SET);
			InputStreamReader sReader = new InputStreamReader(file);
			BufferedReader bReader = new BufferedReader(sReader);
			
			String s = bReader.readLine(); 
			
			while (s != null) {
				if(s.contains("@attribute")) {
					String[] row = s.toString().trim().split(" ");
					attributesList.add(row[1].trim());
				}
				
				s = bReader.readLine();
			}
			bReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("file cannot find");
		} catch (IOException e) {
			System.out.println("I/O exception");
		}
		
		return attributesList;
	}
	
	public static List<Boolean> isNumericList() {
		List<Boolean> attributesList = new ArrayList<Boolean>();
		
		try {
			FileInputStream file = new FileInputStream(TRAIN_SET);
			InputStreamReader sReader = new InputStreamReader(file);
			BufferedReader bReader = new BufferedReader(sReader);
			
			String s = bReader.readLine(); 
			
			while(s != null) {
				if(s.contains("@attribute")) {
					String[] row = s.toString().trim().split(" ");
					if (row[2].trim().equals("real")) {
						attributesList.add(true);
					} else {
						attributesList.add(false);
					}
				}
				
				s = bReader.readLine();
			}
			bReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("file cannot find");
		} catch (IOException e) {
			System.out.println("I/O exception");
		}
		
		return attributesList;
	}
	
	public static List<Double> getSplitValue() {
		List<Boolean> isNumeric = isNumericList();
		List<Double> splitPosition = new ArrayList<Double>();
		
		for (int i = 0; i < isNumeric.size(); i++) {
			if (isNumeric.get(i) == true) {
				ArrayList<Double> possible = getPossibleValues(i);
				Collections.sort(possible);
				splitPosition.add(getBestSplit(possible, i));				
			} else {
				splitPosition.add(0.0);
			}
		}
		
		return splitPosition;
	}
	
	private static ArrayList<Double> getPossibleValues(int rowNumber) {
		ArrayList<Double> result = new ArrayList<Double>();
		FileInputStream file;
		try {
			file = new FileInputStream(TRAIN_SET);
			InputStreamReader sReader = new InputStreamReader(file);
			BufferedReader bReader = new BufferedReader(sReader);
			
			String s = bReader.readLine(); 					
			
			while (s != null) {
				if(s.contains("@") || s.equals("")) {
					s = bReader.readLine();
					continue;
				}
				String[] row = s.toString().trim().split(",");
				double value = Double.parseDouble(row[rowNumber].trim());
				result.add(value);
				s = bReader.readLine();
			}
			bReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found! Please check file path!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO exception! Please check stream!");
			e.printStackTrace();
		}
		return result;
	}
	
	private static double getBestSplit(ArrayList<Double> possible, int rowNumber) {
		double bestSplit = Double.NEGATIVE_INFINITY;
		double maxInfoGain = Double.NEGATIVE_INFINITY;
		for (int p = 0; p < possible.size(); p++) {
			double splitValue = possible.get(p);
			Map<String, Integer> leftLabelCounter = new HashMap<String, Integer>();
			int leftSize = 0;
			Map<String, Integer> rightLabelCounter = new HashMap<String, Integer>();
			int rightSize = 0;
			Map<String, Integer> totalLabelCounter = new HashMap<String, Integer>();
			int totalSize = 0;
			FileInputStream file;
			try {
				file = new FileInputStream(TRAIN_SET);
				InputStreamReader sReader = new InputStreamReader(file);
				BufferedReader bReader = new BufferedReader(sReader);
				
				String s = bReader.readLine(); 					
				
				while (s != null) {
					if(s.contains("@") || s.equals("")) {
						s = bReader.readLine();
						continue;
					}
					String[] row = s.toString().trim().split(",");
					double value = Double.parseDouble(row[rowNumber].trim());
					String tmp = row[row.length - 1];
					if (value > splitValue) {
						if (rightLabelCounter.containsKey(tmp)) {
							rightLabelCounter.put(tmp, rightLabelCounter.get(tmp) + 1);
						} else {
							rightLabelCounter.put(tmp, 1);
						}
						rightSize++;
					} else {
						if (leftLabelCounter.containsKey(tmp)) {
							leftLabelCounter.put(tmp, leftLabelCounter.get(tmp) + 1);
						} else {
							leftLabelCounter.put(tmp, 1);
						}
						leftSize++;
					}
					if (totalLabelCounter.containsKey(tmp)) {
						totalLabelCounter.put(tmp, totalLabelCounter.get(tmp) + 1);
					} else {
						totalLabelCounter.put(tmp, 1);
					}
					totalSize++;
//					possible.add(value);
					s = bReader.readLine();
				}
				bReader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			InfoGain iG = new InfoGain(null, null);
			double leftE = iG.calculateTotalEntropy(leftLabelCounter, leftSize);
			double rightE = iG.calculateTotalEntropy(rightLabelCounter, rightSize);
			double totalE = iG.calculateTotalEntropy(totalLabelCounter, totalSize);
			double curInfoGain = totalE - ((leftSize + 0.0) / (totalSize + 0.0) * leftE
									+ (rightSize + 0.0) / (totalSize + 0.0) * rightE);
			if (curInfoGain > maxInfoGain) {
				bestSplit = splitValue;
				maxInfoGain = curInfoGain;
			}
		}
		return bestSplit;
	}
	
	public static List<List<String>> readData() throws IOException {
		List<List<String>> trainningData = new ArrayList<List<String>>();
		List<String> attributeList = getAttributesList();
		List<Boolean> isNumeric = isNumericList();
		List<Double> splitPosition = getSplitValue();
		try {
			FileInputStream file = new FileInputStream(TRAIN_SET);
			InputStreamReader sReader = new InputStreamReader(file);
			BufferedReader bReader = new BufferedReader(sReader);
			
			String s = bReader.readLine(); 
			
			while (s != null) {
				if(s.contains("@") || s.equals("")) {
					s = bReader.readLine();
					continue;
				}
				
				String[] row = s.toString().trim().split(",");
				List<String> tmpLine = new ArrayList<String>();
				
				for (int i = 0; i < row.length; i++) {
					if (isNumeric.get(i) == false) {
						tmpLine.add(row[i].trim());
					} else {
						double numericValue = Double.parseDouble(row[i].trim());
						double split = splitPosition.get(i);
						if (numericValue > split) {
							tmpLine.add(attributeList.get(i) + " > " + Double.toString(split));
						} else {
							tmpLine.add(attributeList.get(i) + " <= " + Double.toString(split));
						}
					}
				}
				trainningData.add(tmpLine);
				
				 s = bReader.readLine();
			}
			bReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("file cannot find");
		} catch (IOException e) {
			System.out.println("I/O exception");
		}
		
		return trainningData;
	}
}
