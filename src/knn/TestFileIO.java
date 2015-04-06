package knn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class TestFileIO {

	public static void main(String[] args) {
		TestFileIO t = new TestFileIO();
//		String datafile = new File("").getAbsolutePath() + File.separator + "datafile";
//		String testfile = new File("").getAbsolutePath() + File.separator + "testfile";
		
		String datafile = "src/knnData/trainProdSelection.arff";
//		String testfile = "D:\\Application\\eclipse\\jworkspace\\Task11\\src\\knnData\\testProdSelection.arff";
		
		try {
			List<List<Double>> datas = new ArrayList<List<Double>>();
//			List<List<Double>> testDatas = new ArrayList<List<Double>>();
			t.read(datas, datafile);
//			t.read(testDatas, testfile);
//			KnnTrain knn = new KnnTrain();
//			for (int i = 0; i < testDatas.size(); i++) {
//				List<Double> test = testDatas.get(i);
//				System.out.print("测试元组: ");
//				for (int j = 0; j < test.size(); j++) {
//					System.out.print(test.get(j) + " ");
//				}
//				System.out.print("类别为: ");
//				System.out.println(Math.round(Float.parseFloat((knn.knn(datas, test, 3)))));
//			}
			
//			t.normalizeData(datas);
			
			for(int i=0; i<datas.size(); i++) {
				System.out.println(i+1 + " : " + datas.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	public void read(List<List<Double>> datas, String path){
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));
			String data = br.readLine();
			List<Double> l = null;
			while (data != null) {
				String t[] = data.split(",");
				if(t.length < 7) {
					data = br.readLine();
					continue;
				}
				l = new ArrayList<Double>();
				
				l.add(convertType(t[0]));
				l.add(convertLifeStyle(t[1]));
				for (int i = 2; i < t.length - 1; i++) {
					l.add(Double.parseDouble(t[i]));
				}
				datas.add(l);
				data = br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public double convertType(String s) {
		if(s.equals("student")) {
			return 1.0;
		}
		else if(s.equals("engineer")) {
			return 2.0;
		}
		else if(s.equals("librarian")) {
			return 3.0;
		}
		else if(s.equals("professor")) {
			return 4.0;
		}
		else {
			return 5.0;
		}
	}
	
	public double convertLifeStyle(String s) {
		if(s.equals("spend<<saving")) {
			return 1.0;
		}
		else if(s.equals("spend<saving")) {
			return 2.0;
		}
		else if(s.equals("spend>saving")) {
			return 3.0;
		}
		else {
			return 4.0;
		}
	}
	
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
}
