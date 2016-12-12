package luminapp;
import java.io.File;
import java.util.ArrayList;

import appdata.Constants;
import model.SpecrtrumProcessor;
import ui.LuminApplicationWindow;

public class LuminApp {

	public static void main(String[] args) {
		
		// TODO: Terminal Mode
		if(args.length > 0) {
			File folder = new File(args[0]);
			File[] allFiles = folder.listFiles();
			ArrayList<File> spectrums = new ArrayList<File>();
			File temperatureFile = null;
			for (File f : allFiles){
				if (f.getName().contains(Constants.CUMULATED_FILENAME_SUFFIX) || 
					f.getName().contains(Constants.INT_VS_T_W_FILENAME_SUFFIX) || 
					f.getName().contains(Constants.INT_VS_TEMP_FILENAME_SUFFIX)){
					continue;
				}
				if (f.getName().startsWith("Data")){
					temperatureFile = f;
				}
				else {
					spectrums.add(f);
				}
			}
			System.out.println("\nImported spectrums");
			for (File f : spectrums){
				System.out.println(f.getName());
			}
			if (temperatureFile != null){
				System.out.println("\nImported thermometer data\n" + temperatureFile.getName());				
			}
			SpecrtrumProcessor sp = new SpecrtrumProcessor(spectrums.toArray(new File[spectrums.size()]));
			if (temperatureFile!=null) sp.setTemperatureFile(temperatureFile);
			sp.saveCumulated();			
		}
		else {
			new LuminApplicationWindow();
		}
	}

}
