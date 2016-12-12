package ui;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JList;

/**
This code uses a JList in two forms (layout orientation vertical & horizontal wrap) to
display a File[].  The renderer displays the file icon obtained from FileSystemView.
*/
class FileList extends JList<File> {

	private static final long serialVersionUID = 1L;

	FileList (File[] all, boolean vertical){
		super(all);
		
        setCellRenderer(new FileRenderer(!vertical));

        if (!vertical) {
            setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
//            fileList.setVisibleRowCount(-1);
        } else {
//            fileList.setVisibleRowCount(9);
        }
//		this.setFixedCellHeight(24);
	}

	public File[] getSelectedObjectsArray(){
		ArrayList<File> temp = new ArrayList<File>();
		for (int i:getSelectedIndices()) {
			temp.add((File) (getModel().getElementAt(i)));
		}
		return temp.toArray(new File[temp.size()]);
	}
}

/*
class TextFileFilter implements FileFilter {

    public boolean accept(File file) {
        // implement the logic to select files here..
        String name = file.getName().toLowerCase();
        //return name.endsWith(".java") || name.endsWith(".class");
        return name.length()<20;
    }
}
*/