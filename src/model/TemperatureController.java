package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import datafile.DataFile;
import datafile.TermometerDataFileReader;

public class TemperatureController {
	
	DataFile tempData;
	ArrayList<Date> dateColumn;
	
	public TemperatureController (File file){
		// Read Temperature File
		TermometerDataFileReader tdfr = new TermometerDataFileReader();
		tempData = tdfr.read(file);


		// If we Set Zero Point - get Rid of it
		ArrayList<Double> secondsColumn = tempData.getColumn(1);
		Double minSecondsValue = Collections.min(secondsColumn); 
		int minSecondIndex = secondsColumn.indexOf(minSecondsValue);
		if (minSecondIndex > 0 && secondsColumn.size() > minSecondIndex + 1){
			double interval = secondsColumn.get(secondsColumn.size() - 1).doubleValue() - secondsColumn.get(secondsColumn.size() - 2).doubleValue();
			for (int i=0; i < minSecondIndex; i++){
				secondsColumn.set(i, Double.valueOf(secondsColumn.get(minSecondIndex).doubleValue() - (minSecondIndex-i) * interval));
			}
		}
		
		// Convert Seconds Column to Date Column
		dateColumn = new ArrayList<Date>();
		Date fileCreationDate = tempData.getCreationDate();
		for (int i=0; i<secondsColumn.size(); i++){
			long ms = fileCreationDate.getTime() + (long)((secondsColumn.get(i).doubleValue() - secondsColumn.get(0).doubleValue())*1000); 
			Date d = new Date(ms);
			dateColumn.add(d);
		}
	}
	
	public Double getMeanTemperature(Date spectrumDate, long spectrumInterval){
		ArrayList<Double> tempPoints = new ArrayList<Double>();
		Date spectrumStartDate = new Date(spectrumDate.getTime() - spectrumInterval);
		for (Date d : dateColumn){
			if (d.getTime() >= spectrumStartDate.getTime() && d.getTime() <= spectrumDate.getTime()){
				int index = dateColumn.indexOf(d);
				tempPoints.add(tempData.getColumn(2).get(index));
			}
		}
		if (tempPoints.size() == 0){
			return null;
		}
		return Util.avg(tempPoints);
	}
}
