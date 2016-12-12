package datafile;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TermometerDataFileReader extends SpectrometerDataFileReader {

	@Override
	Date readDateFromHeader(BufferedReader br) {
		Date d = null; 
		try {
			String line = br.readLine();
			line = line.replace("  ", " ");
			line = line.replace("Thusday", "Thursday");			
			DateFormat df = new SimpleDateFormat("EEEE, MMMM d, yyyy HH:mm:ss", Locale.US);
			d = df.parse(line); 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return d;		
	}

}
