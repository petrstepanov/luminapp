package model;

import java.util.ArrayList;

public class Util {

	public static boolean isNumeric(String str) {  
		try {  
			Double.parseDouble(str);  
		}  
		catch(NumberFormatException nfe){  
			return false;  
		}  
		return true;  
	}

	public static Boolean isNumeric(String[] array){
		for (String s : array){
			if(!isNumeric(s)){
				return false;
			}
		}
		return true;
	}	
	
	public static Double avg(ArrayList<Double> points) {
		Double sum = 0.0;
		if(!points.isEmpty()) {
			for (Double mark : points) {
				sum += mark;
			}
			return sum.doubleValue() / points.size();
		}
		return Double.valueOf(sum);
	}	

	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();
	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}	
	
	private static int progressBarLength = 10;
	
	// Output Progress bar to Console; percent is from 0 to 1
	public static void writeProgress(double percent){ 
		char empty = '░';
		char full = '▓';
		if (percent < 1){
			for (int i = 0; i< progressBarLength; i++){
				if (i < percent*progressBarLength){
					System.out.print(full);
				}
				else {
					System.out.print(empty);
				}
			}
			System.out.print(" " + (int)(percent*100) + "%\r");
		}
		else {
			System.out.print("done!         \n");			
		}
	}
}
