package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import appdata.Constants;
import datafile.DataFile;
import datafile.DataFileReader;
import datafile.NewSpectrometerDataFileReader;
import datafile.SpectrometerDataFileReader;

public class SpecrtrumProcessor {
	
	private File[] intensitySpectrums;
	private File temperatureFile;
	
	private int minWavelength;
	private int maxWavelength;
	private boolean useWavelengthRange = false;
	
	public SpecrtrumProcessor(File[] files){
		this.intensitySpectrums = files;
		this.temperatureFile = null;
	}
	
	public void setWavelengthRange(int min, int max){
		this.minWavelength = min;
		this.maxWavelength = max;
		this.useWavelengthRange = true;
	}
	
	public void saveCumulated() {
		SpectrometerType st = SpectrometerIdentifier.getSpectrometerType(intensitySpectrums[0]);
		DataFileReader dfr;
		if (st == SpectrometerType.SPECTROMETER_OLD){
			dfr = new SpectrometerDataFileReader();
		}
		else {
			dfr = new NewSpectrometerDataFileReader();
		}
		if (useWavelengthRange) dfr.setDataRange(minWavelength, maxWavelength);
		
		ArrayList<DataFile> spectrums = new ArrayList<DataFile>();

		try {
			System.out.println("\nReading spectrums");
			// Read Files to Memory
			for (int i = 0; i < intensitySpectrums.length; i++){
				DataFile df = dfr.read(intensitySpectrums[i]);
				spectrums.add(df);
				Util.writeProgress((double)i/(intensitySpectrums.length-1));				
			}
						
			// Write cumulated file
			FileWriter out = new FileWriter(getCumulatedFilePath() + File.separator + getBaseFilename() + Constants.CUMULATED_FILENAME_SUFFIX + ".dat");
			System.out.println("\nWriting intensity spectrum");
			int cols = spectrums.size();
			int rows = spectrums.get(0).getColumn(1).size();

			// Header
			out.write("Wavelength\t");
			for(int i=0; i < cols; i++){
				out.write(intensitySpectrums[i].getName());
				if (i != cols-1){
					out.write("\t");							
				}				
			}
			out.write(System.lineSeparator());
			
			// Data
			for(int j=0; j < rows; j++){					
				for(int i=-1; i < cols; i++){
					// Write Wavelength to first column
					if (i==-1){
						out.write(spectrums.get(0).getColumn(1).get(j).toString());
					} 
					// Write Intensity to other columns
					else {
						out.write(spectrums.get(i).getColumn(2).get(j).toString());
					}
					if (i != cols-1){
						out.write("\t");							
					}
				}
				out.write(System.lineSeparator());
				Util.writeProgress((double)j/(rows-1));				
			}

			// Parameters: total
/*			ArrayList<Double> totalIntensity = new ArrayList<Double>();			
			for (DataFile df: spectrums){
				totalIntensity.add(df.getColumnSum(2));
			}
			out.write("I_total\t");
			for(int i=0; i < cols; i++){
				out.write(Util.round(totalIntensity.get(i),2) + "");
				if (i != cols-1){
					out.write("\t");							
				}
			}
			out.write(System.lineSeparator());			
*/
			// Parameters: totalNormalized
/*			ArrayList<Double> totalIntensityNormalized = new ArrayList<Double>();			
			Double minIntensity = Collections.min(totalIntensity);
			for (Double d: totalIntensity){
				totalIntensityNormalized.add(Double.valueOf(d - minIntensity));
			}
			out.write("I_total_norm\t");
			for(int i=0; i < cols; i++){
				out.write(Util.round(totalIntensityNormalized.get(i),2) + "");
				if (i != cols-1){
					out.write("\t");							
				}
			}
*/			
			
			if (temperatureFile != null){
				// Temperature
				out.write(System.lineSeparator());
				long interval = getInterval(spectrums);
				if (temperatureFile != null){			
//					System.out.println("Reading temperature file\n");				
					TemperatureController tc = new TemperatureController(temperatureFile);
//					Util.writeProgress(1);	
					
					System.out.println("\nCalculating Temperature");				
					out.write("Temperature\t");
					for (DataFile df : spectrums){
						Date fileCreationDate = df.getCreationDate();
						Double t = tc.getMeanTemperature(fileCreationDate, interval);
						if (t!= null){
							out.write(Util.round(t.doubleValue(),2)+"");
							if (spectrums.indexOf(df) != spectrums.size() - 1){
								out.write("\t");
							}							
						}
						Util.writeProgress((double)spectrums.indexOf(df)/(spectrums.size()-1));							
					}
				}				
				out.close();
			
				// Intensity vs Temperature
				System.out.println("\nWriting intensity vs temperature");
				out = new FileWriter(getCumulatedFilePath() + File.separator + getBaseFilename() + Constants.INT_VS_TEMP_FILENAME_SUFFIX + ".dat");
				ArrayList<Double> totalIntensity = new ArrayList<Double>();			
				for (DataFile df: spectrums){
					totalIntensity.add(df.getColumnSum(2));
				}
				Double minIntensity = Collections.min(totalIntensity);
				ArrayList<Double> totalIntensityNormalized = new ArrayList<Double>();			
				for (Double d: totalIntensity){
					totalIntensityNormalized.add(Double.valueOf(d - minIntensity));
				}
				// long interval = getInterval(spectrums);
				TemperatureController tc = new TemperatureController(temperatureFile);
				out.write("Temperature\tIntensity");
				out.write(System.lineSeparator());
				for (DataFile df : spectrums){
					Date fileCreationDate = df.getCreationDate();
					Double t = tc.getMeanTemperature(fileCreationDate, interval);
					double i = totalIntensityNormalized.get(spectrums.indexOf(df)).doubleValue();
					if (t!=null){
						out.write(Util.round(t.doubleValue(),2) + "\t" + Util.round(i,2));
						out.write(System.lineSeparator());						
					}
					Util.writeProgress((double)spectrums.indexOf(df)/(spectrums.size()-1));										
				}
				out.close();

				// Temperature vs Wavelength vs Intensity
				System.out.println("\nWriting 3D data - intensity vs temperature and wavelength");				
				out = new FileWriter(getCumulatedFilePath() + File.separator + getBaseFilename() + Constants.INT_VS_T_W_FILENAME_SUFFIX + ".dat");
				out.write("Temperature\tWavelength\tIntensity");	
				out.write(System.lineSeparator());
				for (DataFile df : spectrums){
					Date fileCreationDate = df.getCreationDate();
					Double t = tc.getMeanTemperature(fileCreationDate, interval);
					if (t!=null){
						for (int i=0; i<df.getColumn(2).size();i++){
							out.write(Util.round(t.doubleValue(),2) + "\t" + df.getColumn(1).get(i).toString() + "\t" + df.getColumn(2).get(i).toString());
							out.write(System.lineSeparator());							
						}
					}
					Util.writeProgress((double)spectrums.indexOf(df)/(spectrums.size()-1));					
				}
				out.close();				
			}
			else {
				out.close();
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
	}
	
	public void setTemperatureFile(File t){
		temperatureFile = t;
	}
	
	public String getCumulatedFilePath(){
		File f = intensitySpectrums[0];		
		String absolutePath = f.getAbsolutePath();
		return absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));		
	}
	
	public String getBaseFilename(){
		File f = intensitySpectrums[0];		
		return f.getName().split("-")[0];
	}
	
	private long getInterval(ArrayList<DataFile> spectrums){
		Date d1 = spectrums.get(0).getCreationDate();
		Date d2 = spectrums.get(1).getCreationDate();		
		long interval = d2.getTime() - d1.getTime();
		return interval;
	}
}
