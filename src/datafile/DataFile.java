package datafile;

import java.util.ArrayList;
import java.util.Date;

public class DataFile {
	private Date creationDate;
	
	private ArrayList<ArrayList<Double>> data;
	
	DataFile(ArrayList<ArrayList<Double>> data, Date cretionDate){
		this.data = data;
		this.creationDate = cretionDate;
	}
	
	public ArrayList<Double> getColumn(int index){
		if (data == null) return null;
		try {
			ArrayList<Double> column = data.get(index - 1);			
			return column;
		}
		catch (IndexOutOfBoundsException e){
			System.out.println("DataFile.class: No Column with index specified");
			return null;
		}
	}

	public Date getCreationDate(){
		return creationDate;
	}

	public Double getColumnSum(int index) {
		if (data == null) return null;
		try {
			ArrayList<Double> column = data.get(index - 1);
			Double sum = Double.valueOf(0);
			for (Double d : column){
				sum += d;
			}
			return sum;
		}
		catch (IndexOutOfBoundsException e){
			System.out.println("DataFile.class: No Column with index specified");
			return null;
		}
	}
}
