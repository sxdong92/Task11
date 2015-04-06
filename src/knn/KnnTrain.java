package knn;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * KNN Algorithm Class
 * @version 1.0
 */
public class KnnTrain {
	
	List<List<String>> datasOfSymbolParas;
	List<List<Double>> datasOfNumParas;
	double[] weights;
	
	public KnnTrain(List<List<String>> datasOfSymbolParas, List<List<Double>> datasOfNumParas, double[] weights) {
		this.datasOfSymbolParas = datasOfSymbolParas;
		this.datasOfNumParas = datasOfNumParas;
		this.weights = weights;
	}
	
	/**
	 * 设置优先级队列的比较函数，距离越大，优先级越高
	 */
	private Comparator<KnnNode> comparator = new Comparator<KnnNode>() {
		public int compare(KnnNode o1, KnnNode o2) {
			if (o1.getDistance() >= o2.getDistance()) {
				return 1;
			} else {
				return 0;
			}
		}
	};
	
	/**
	 * 获取K个不同的随机数
	 * @param k 随机数的个数
	 * @param max 随机数最大的范围
	 * @return 生成的随机数数组
	 */
	public List<Integer> getRandKNum(int k, int max) {
		List<Integer> rand = new ArrayList<Integer>(k);
		for (int i = 0; i < k; i++) {
			int temp = (int) (Math.random() * max);
			if (!rand.contains(temp)) {
				rand.add(temp);
			} else {
				i--;
			}
		}
		return rand;
	}
	
	/**
	 * 计算测试元组与训练元组之前的距离
	 * @param d1 测试元组
	 * @param d2 训练元组
	 * @return 距离值
	 */
	public double calDistance(List<Double> d1, List<Double> d2) {
		double distance = 0.00;
		for (int i = 0; i < d1.size(); i++) {
			distance += (d1.get(i) - d2.get(i)) * (d1.get(i) - d2.get(i));
		}
		return distance;
	}
	
	
	public double calDistance(List<String> testDataSym, List<Double> testDataNum, 
							  List<String> currDataSym, List<Double> currDataNum) {
		double distance = 0.0;
		
		// finish the distance calculation, using similarity matrix for first two String features
		if(!testDataSym.get(0).equals(currDataSym.get(0))) distance += 1;
		if(!testDataSym.get(1).equals(currDataSym.get(1))) distance += 1;
		
		for (int i = 0; i < testDataNum.size(); i++) {
			distance += (testDataNum.get(i) - currDataNum.get(i)) * (testDataNum.get(i) - currDataNum.get(i));
		}
		
		return distance;
	}
	
	
	/**
	 * Get the class who has the biggest number of elements in priority queue (K nearest elements)
	 * @param pq priority queue saving K nearest elements
	 * @return the class name
	 */
	private String getMostClass(PriorityQueue<KnnNode> pq) {
		Map<String, Integer> classCount = new HashMap<String, Integer>();
		for (int i = 0; i < pq.size(); i++) {
			KnnNode node = pq.remove();
			String c = node.getC();
			if (classCount.containsKey(c)) {
				classCount.put(c, classCount.get(c) + 1);
			} else {
				classCount.put(c, 1);
			}
		}
		int maxIndex = -1;
		int maxCount = 0;
		Object[] classes = classCount.keySet().toArray();
		for (int i = 0; i < classes.length; i++) {
			if (classCount.get(classes[i]) > maxCount) {
				maxIndex = i;
				maxCount = classCount.get(classes[i]);
			}
		}
		return classes[maxIndex].toString();
	}
	
	/**
	 * 执行KNN算法，获取测试元组的类别
	 * @param datas 训练数据集
	 * @param testData 测试元组
	 * @param k 设定的K值
	 * @return 测试元组的类别
	 */
//	public String knn(List<List<Double>> datas, List<Double> testData, int k) {
//		PriorityQueue<KnnNode> pq = new PriorityQueue<KnnNode>(k, comparator);
//		List<Integer> randNum = getRandKNum(k, datas.size());
//		for (int i = 0; i < k; i++) {
//			int index = randNum.get(i);
//			List<Double> currData = datas.get(index);
//			String c = currData.get(currData.size() - 1).toString();
//			KnnNode node = new KnnNode(index, calDistance(testData, currData), c);
//			pq.add(node);
//		}
//		for (int i = 0; i < datas.size(); i++) {
//			List<Double> t = datas.get(i);
//			double distance = calDistance(testData, t);
//			KnnNode top = pq.peek();
//			if (top.getDistance() > distance) {
//				pq.remove();
//				pq.add(new KnnNode(i, distance, t.get(t.size() - 1).toString()));
//			}
//		}
//		
//		return getMostClass(pq);
//	}
	
	
	public double knn(int testBegin, int testEnd, int k) {
		double singleAccu = 0.0;
		
		//in raw data set, elements from testBegin to testEnd are chosen to be testing set, rest of them are training set.
		String[] labeled = new String[testEnd - testBegin + 1];
		for(int i = testBegin; i <= testEnd; i++) {
			List<String> testDataSym = datasOfSymbolParas.get(i);
			List<Double> testDataNum = datasOfNumParas.get(i);
			
			PriorityQueue<KnnNode> pq = new PriorityQueue<KnnNode>(k, comparator);
			int initPQ = (testEnd == datasOfSymbolParas.size()-1) ? 0 : testEnd+1;
			for(int j = initPQ; j < initPQ + 3; j++) {
				List<String> currDataSym = datasOfSymbolParas.get(j);
				List<Double> currDataNum = datasOfNumParas.get(j);
				String c = currDataSym.get(2);
				KnnNode node = new KnnNode(j, calDistance(testDataSym, testDataNum, currDataSym, currDataNum), c);
				pq.add(node);
			}
			
			for (int j = 0; j < datasOfSymbolParas.size(); j++) {
				if(j >= testBegin && j <= testEnd) continue;
				
				List<String> currDataSym = datasOfSymbolParas.get(j);
				List<Double> currDataNum = datasOfNumParas.get(j);
				double distance = calDistance(testDataSym, testDataNum, currDataSym, currDataNum);
				KnnNode top = pq.peek();
				if (top.getDistance() > distance) {
					pq.remove();
					pq.add(new KnnNode(j, distance, currDataSym.get(currDataSym.size()-1)));
				}
			}
			
			labeled[i - testBegin] = getMostClass(pq);
		}
		
		int rightCounter = 0;
		for(int i = 0; i < labeled.length; i++) {
			if(labeled[i].equals(datasOfSymbolParas.get(i + testBegin).get(2))) rightCounter++;
		}
		singleAccu = (double) rightCounter / (double) labeled.length;
		
		return singleAccu;
	}
	
	
	public double executeKnn(int k) {
		double averAccu = 0.0;
		
		//TODO: divide data into 10 folds and separately call knn() methods to calculate single accuracy
		averAccu += knn(0, 20, k);
		averAccu += knn(21, 40, k);
		averAccu += knn(41, 60, k);
		averAccu += knn(61, 80, k);
		averAccu += knn(81, 100, k);
		averAccu += knn(101, 120, k);
		averAccu += knn(121, 140, k);
		averAccu += knn(141, 160, k);
		averAccu += knn(161, 185, k);
		
		return averAccu / 9;
	}
}
