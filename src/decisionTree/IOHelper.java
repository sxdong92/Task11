package decisionTree;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class IOHelper {

	public static List<String> getAttributesList() {
		List<String> attributesList = new ArrayList<String>();
		
		try {
			FileInputStream file = new FileInputStream("src/aData/trainProdSelection.arff");
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
	
	
	public static List<List<String>> readData() throws IOException {
		List<List<String>> trainningData = new ArrayList<List<String>>();
		try {
			FileInputStream file = new FileInputStream("src/aData/trainProdSelection.arff");
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
				tmpLine.add(row[0].trim());
				tmpLine.add(row[1].trim());
				
				double vacation = Double.parseDouble(row[2].trim());
				if(vacation < 25.0) {
					tmpLine.add("vacation<25");
				}
				else {
					tmpLine.add("vacation>=25");
				}
				
				double eCredit = Double.parseDouble(row[3].trim());
				if(eCredit < 30.0) {
					tmpLine.add("eCredit<30");
				}
				else {
					tmpLine.add("eCredit>=30");
				}
				
				double salary = Double.parseDouble(row[4].trim());
				if(salary < 18.0) {
					tmpLine.add("salary<18");
				}
				else {
					tmpLine.add("salary>=18");
				}
				
				double property = Double.parseDouble(row[5].trim());
				if(property < 8.0) {
					tmpLine.add("property<8");
				}
				else {
					tmpLine.add("property>=8");
				}
				
				tmpLine.add(row[6].trim());
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
