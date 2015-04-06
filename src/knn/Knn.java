package knn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
/**
 * Entrance of program
 * @version 1.0
 */
public class Knn {
	
	/**
	 * read numeric data from file
	 * @param datas Data set implemented by ArrayList
	 * @param path Path of file
	 */
	public void read(List<List<String>> datasOfSymbolParas, List<List<Double>> datasOfNumParas, String path) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));
			String data = br.readLine();
			List<String> l1 = null;
			List<Double> l2 = null;
			
			while (data != null) {
				String t[] = data.split(",");
				if(t.length < 7) {
					data = br.readLine();
					continue;
				}
				
				l1 = new ArrayList<String>();
				l2 = new ArrayList<Double>();
				
				for (int i = 0; i < 2; i++) {
					l1.add(t[i].trim());
				}
				l1.add(t[t.length-1].trim());
				datasOfSymbolParas.add(l1);
				
				for (int i = 2; i < t.length - 1; i++) {
					l2.add(Double.parseDouble(t[i]));
				}
				datasOfNumParas.add(l2);
				data = br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Normalize numeric data from a higher range to [0.0, 1.0]
	 * @param datas
	 */
	public void normalizeData(List<List<Double>> datas) {
		for(int para=0; para<datas.get(0).size(); para++) {
			double max = datas.get(0).get(para);
			double min = datas.get(0).get(para);
			for(int i=0; i<datas.size(); i++) {
				max = Math.max(max, datas.get(i).get(para));
				min = Math.min(min, datas.get(i).get(para));
			}
			for(int i=0; i<datas.size(); i++) {
				if(max == min) {
					datas.get(i).set(para, 0.0);
					continue;
				}
				double tmp = (datas.get(i).get(para) - min) / (max - min);
				datas.get(i).set(para, tmp);
			}
		}
	}
	
	public void shuffle(List<List<String>> datasOfSymbolParas, List<List<Double>> datasOfNumParas) {
		//TODO: shuffle the data set !
		//pay attention that the two sets must keep matching after shuffled
		for(int i = 0; i < datasOfSymbolParas.size(); i++) {
			datasOfSymbolParas.get(i).add(i + "");
		}
		Collections.shuffle(datasOfSymbolParas);
		
		List<List<Double>> tmp = new ArrayList<List<Double>>();
		for(int i = 0; i < datasOfSymbolParas.size(); i++) {
			int index = Integer.parseInt(datasOfSymbolParas.get(i).get(3));
			tmp.add(datasOfNumParas.get(index));
			datasOfSymbolParas.get(i).remove(3);
		}
		
		for(int i = 0; i < datasOfNumParas.size(); i++) {
			datasOfNumParas.set(i, tmp.get(i));
		}
		datasOfNumParas = tmp;
	}
	
	/**
	 * Entrance !
	 * @param args
	 */
	public static void main(String[] args) {
		Knn t = new Knn();
		String datafile = "src/aData/trainProdSelection.arff";
		
		try {
			//all elements with first two features whose value is String type: "Type" and "Life-style"
			List<List<String>> datasOfSymbolParas = new ArrayList<List<String>>();
			//all elements with last four features whose value is double type: "Vacation", "eCredit", "Salary", "Property value"
			List<List<Double>> datasOfNumParas = new ArrayList<List<Double>>();
			
			t.read(datasOfSymbolParas, datasOfNumParas, datafile);
			t.normalizeData(datasOfNumParas);
			if(datasOfSymbolParas.size() != datasOfNumParas.size()) {
				System.out.println("two data sets are not match !");
				return;
			}
			
			t.shuffle(datasOfSymbolParas, datasOfNumParas);
			
			//print out data FOR TEST
			for(int i=0; i<datasOfSymbolParas.size(); i++) {
				System.out.println(i+1 + " : " + datasOfSymbolParas.get(i) + datasOfNumParas.get(i));
			}
			
//			t.read(testDatas, testfile);
//			KnnTrain train = new KnnTrain();
//			for (int i = 0; i < testDatas.size(); i++) {
//				List<Double> test = testDatas.get(i);
//				System.out.print("测试元组: ");
//				for (int j = 0; j < test.size(); j++) {
//					System.out.print(test.get(j) + " ");
//				}
//				System.out.print("类别为: ");
//				System.out.println(Math.round(Float.parseFloat((train.knn(datas, test, 3)))));
//			}
			
			double[] weights = {1,1,1,1,1,1};
			KnnTrain train = new KnnTrain(datasOfSymbolParas, datasOfNumParas, weights);
			double averAccu = train.executeKnn(3);
			System.out.println(averAccu);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
