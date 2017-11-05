package datafile;

import java.io.BufferedReader;
import java.io.File;
import java.util.Date;

public abstract class DataFileReader {
	
	abstract Date readDateFromHeader(BufferedReader br);
	
	public abstract DataFile read(File file);

	protected boolean useRange = false;
	protected int dataMin;
	protected int dataMax;
	
	public void setDataRange(int min, int max){
		dataMin = min;
		dataMax = max;
		useRange = true;
	}
}
