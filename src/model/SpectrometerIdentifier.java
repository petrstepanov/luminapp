package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SpectrometerIdentifier {

	static SpectrometerType getSpectrometerType(File file){
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			br.close();
			if (line.equalsIgnoreCase("SpectraSuite Data File")){
				return SpectrometerType.SPECTROMETER_OLD;
			}
			return SpectrometerType.SPECTROMETER_NEW;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;		
	}
}
