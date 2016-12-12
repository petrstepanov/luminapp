package datafile;

import java.io.BufferedReader;
import java.io.File;
import java.util.Date;

public abstract class DataFileReader {
	
	abstract Date readDateFromHeader(BufferedReader br);
	
	public abstract DataFile read(File file);

}
