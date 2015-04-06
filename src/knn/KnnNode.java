package knn;

/**
 * KNN Node Class, to store information of K nearest tuples
 * @version 1.0
 */
public class KnnNode {
	private int index; // Ԫ����
	private double distance; // �����Ԫ��ľ���
	private String c; // �������
	public KnnNode(int index, double distance, String c) {
		super();
		this.index = index;
		this.distance = distance;
		this.c = c;
	}
	
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
}
