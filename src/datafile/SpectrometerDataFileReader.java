package datafile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import model.Util;

public class SpectrometerDataFileReader extends DataFileReader{

	protected final String DELIMETER = "\t";
	
	@Override
	Date readDateFromHeader(BufferedReader br) {
		Date d = null;
		try {		
			String line;
			// Read Lines Until we found 'Date' word in the beginning of the line
			do {
					line = br.readLine();
			} while (!line.split(":")[0].equals("Date"));
			
			String tmp = line.split(": ")[1];
			DateFormat df = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.ENGLISH);
			d = df.parse(tmp);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return d;
	}

	@Override	
	public DataFile read(File file){
		BufferedReader br = null;		
		try {
			br = new BufferedReader(new FileReader(file));
			
			// Read Creation Date
			Date creationDate = readDateFromHeader(br);
			
			// Read Spectrum
			ArrayList<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();
			String line = null;
			while ((line = br.readLine()) != null){
				String tmp[] = line.split(DELIMETER);

				// Skip lines that do not start with a number
				if (Util.isNumeric(tmp[0])){

					// Initialize the array if not yet done
					if (data.isEmpty()){
						for (int i=0; i<tmp.length; i++){
							data.add(new ArrayList<Double>());
						}
					}					
					
					Double wavelength = Double.parseDouble(tmp[0]);
					if (useRange){
						if (wavelength >= dataMin && wavelength < dataMax){
							// Read data row
							for (int i=0; i<tmp.length; i++){
								data.get(i).add(Double.parseDouble(tmp[i]));
							}
						}
					}
					else {
						// Read data row
						for (int i=0; i<tmp.length; i++){
							data.get(i).add(Double.parseDouble(tmp[i]));
						}
					}					
				}
			}
			br.close();
			return new DataFile(data, creationDate);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}	
}
