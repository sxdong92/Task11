package decisionTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class TestDT {
	
	public static void main(String[] args) {
		TestDT testDT = new TestDT();
		List<String> restAttributesList = new ArrayList<String>(); 
		List<List<String>> trainingData = new ArrayList<List<String>>();

		// read data
		try {
			restAttributesList = IOHelper.getAttributesList();
			//test attributes list
			System.out.println("Attributes are : " + restAttributesList);
			System.out.println("--------------------------------\n");
			
			trainingData = IOHelper.readData();
			//test training data
//			for(int i=0; i<trainingData.size(); i++) {
//				System.out.println(i + " : " + trainingData.get(i));
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// build the tree
		DTAlgorithm mainDT = new DTAlgorithm();
		TreeNode root = mainDT.generateDT(trainingData, restAttributesList);
		
		testDT.shuffle(trainingData);
//		for(int i=0; i<trainingData.size(); i++) {
//			System.out.println(i + " : " + trainingData.get(i));
//		}
		
		// print trainning accuracy:
		double trainingAccu = testDT.culculateSingleAccuracy(root, restAttributesList, trainingData);
		System.out.println("Training Accuracy is : " + trainingAccu);
		System.out.println("--------------------------------\n");
		
		// print tree before pruning
		System.out.println("DT before pruning : ");
		testDT.visualizeDT(root, 0);
		System.out.println("--------------------------------\n");
		
		//TODO: call pruning function
		Pruning prune = new Pruning(root, restAttributesList, trainingData, testDT, trainingAccu);
		prune.pruningWithREP(root);
		
		// print tree after pruning
		System.out.println("DT after pruning : ");
		testDT.visualizeDT(root, 0);
		System.out.println("--------------------------------\n");
		
		double superTestAccuSum = 0.0;
		for(int i=0; i<100; i++) {
			testDT.shuffle(trainingData);
			superTestAccuSum += testDT.crossValidation(trainingData, restAttributesList);
		}
		double finalAccu = superTestAccuSum / 100.0;
		System.out.println("final accuracy : " + finalAccu);
		
	}
	
	
	public void shuffle(List<List<String>> data) {
		Collections.shuffle(data);
	}
	
	
	public double crossValidation(List<List<String>> trainingData, List<String> restAttributesList) {
		//int foldSize = data.size() / 10;
		double[] accuracy = new double[10];
		for (int i = 0; i < 10; i++) {
			List<List<String>> train = new ArrayList<List<String>>();
			List<List<String>> test = new ArrayList<List<String>>();
			
			//Generate train and test data set
 			if (i == 9) {
 				for (int j = 0; j < trainingData.size(); j++) {
 					if (j > i * 10) {
 						test.add(trainingData.get(j));
 					} else {
 						train.add(trainingData.get(j));
 					}
 				}
				break;
			}
 			for (int j = 0; j < trainingData.size(); j++) {
 				if (j >= i * 10 + 1 && j <= (i + 1) * 10) {
 					test.add(trainingData.get(j));
 				} else {
 					train.add(trainingData.get(j));
 				}
 			}
 			
 			//train the model
 			DTAlgorithm tree = new DTAlgorithm();
 			TreeNode root = tree.generateDT(train, restAttributesList);
 			
 			//pruning
 			double trainingAccu = culculateSingleAccuracy(root, restAttributesList, train);
 			Pruning prune = new Pruning(root, restAttributesList, train, this, trainingAccu);
 			prune.pruningWithREP(root);
 			
 			//test
 			accuracy[i] = culculateSingleAccuracy(root, restAttributesList, test);
		}
		
		double sum = 0.0;
		for (double d : accuracy) {
			sum += d;
		}
		
//		System.out.println("accuracy: " + sum/10.0);
		return sum/10.0;
	}
	
	
	public double culculateSingleAccuracy(TreeNode realRoot, List<String> restAttributesList, List<List<String>> test) {
		int correct = 0;
		int sum = 0;
		for (int k = 0; k < test.size(); k++) {
			List<String> current = test.get(k);
			TreeNode root = realRoot;
			
			boolean firstScan = true;
			while (root.getChildrenList().size() != 0 && firstScan) {
				firstScan = false;
				
				int index = nameParseToIndex(root.getParaName(), restAttributesList);
				List<String> rules = root.getIntervalsList();
				for (int n = 0; n < rules.size(); n++) {
					if ((current.get(index)).equals(rules.get(n))) {
						root = root.getChildrenList().get(n);
						firstScan = true;
					}
				}
				
			}
			String result = root.getParaName();
			int size = test.get(0).size();
			if (result.equals(test.get(k).get(size - 1))) {
				correct++;
			}
			sum++;
		}
		
		return (correct + 0.0) / (sum + 0.0);
	}
	
	public int nameParseToIndex(String name, List<String> candAttr) {
		int i = 0;
		for (String s : candAttr) {
			if (s.equals(name)) {
				return i;
			}
			i++;
		}
		return -1;
	}

	public void visualizeDT(TreeNode root, int level) {
		System.out.println(root.getParaName());
		List<String> rules = root.getIntervalsList();

		List<TreeNode> childrenList = root.getChildrenList();
		for (int i = 0; i < rules.size(); i++) {
			for (int j = 0; j <= level; j++)
				System.out.print("     ");
			System.out.print(rules.get(i) + "--> ");
			visualizeDT(childrenList.get(i), (level + 1));
		}

	}
}

