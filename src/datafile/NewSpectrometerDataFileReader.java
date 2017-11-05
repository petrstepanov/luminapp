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

public class NewSpectrometerDataFileReader extends DataFileReader {

	protected final String DELIMETER = ",";
	
	@Override
	Date readDateFromHeader(BufferedReader br) {
		Date d = null;
		try {		
			String line;
			// Read Lines Until we found 'Date' word in the beginning of the line
			do {
				line = br.readLine();
			} while (!line.split(",")[0].equals("Date"));
			
			String tmp = line.split(",")[1];
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
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
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			// Read Creation Date
			Date creationDate = readDateFromHeader(br);
			
			// Read Spectrum
			ArrayList<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();
			String line = null;
			while ((line = br.readLine()) != null){
				String tmp[] = line.split(DELIMETER);
				if (tmp.length == 11 && Util.isNumeric(tmp)){

					// Initialize the array if not yet done
					if (data.isEmpty()){
						data.add(new ArrayList<Double>());
						data.add(new ArrayList<Double>());
					}
					
					// Read data row
					// Not sure I'm right here
					Double wavelength = Double.parseDouble(tmp[1]);
					Double time = Double.parseDouble(tmp[7]);
					
					if (useRange){
						if (wavelength >= dataMin && wavelength <= dataMax){
							data.get(0).add(wavelength);
							data.get(1).add(time);					
						}
					}
					else {
						data.get(0).add(wavelength);
						data.get(1).add(time);
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
