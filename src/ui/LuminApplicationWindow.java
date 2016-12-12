package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;

import model.SpecrtrumProcessor;
import appdata.Constants;

public class LuminApplicationWindow implements ActionListener{

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LuminApplicationWindow window = new LuminApplicationWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private JFileChooser fileChooser = new JFileChooser();	

	JFlatButton btnLoadSpectrums = new JFlatButton("Load Spectra");
	JFlatButton btnAddTemp = new JFlatButton("Link Temperature");
	JFlatButton btnSaveSpectrum = new JFlatButton("Save");

	FileList spectrumsList;
	File temperatureFile;

	JPanel topPanel;
	JPanel noFilesCard;
	JPanel filesListCard;
	JPanel bottomNorthPanelContainer;
	
	final static String NOFILESCARD = "Card with No Files Label";
	final static String FILESCARD = "Card with FilesList";
	
	/**
	 * Create the application.
	 */
	public LuminApplicationWindow() {
		//		listModel = new BasicDirectoryModel(multipleFileChooser);
//		list = new JList(listModel);
		btnLoadSpectrums.addActionListener(this);
		btnAddTemp.addActionListener(this);
		btnSaveSpectrum.addActionListener(this);
		btnSaveSpectrum.setEnabled(false);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBackground(Color.WHITE);
		frame.setBounds(100, 100, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
		frame.setMinimumSize(new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));		
		frame.setTitle(Constants.APP_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel noFilesRow = new JPanel();
		noFilesRow.setBackground(Color.WHITE);
		noFilesRow.setLayout(new BoxLayout(noFilesRow, BoxLayout.X_AXIS));
		noFilesRow.add(Box.createHorizontalGlue());
		JLabel noFilesLabel = new JLabel("NO FILES LOADED");
		noFilesLabel.setForeground(Color.GRAY);		
		noFilesRow.add(noFilesLabel);
		noFilesRow.add(Box.createHorizontalGlue());
		
		noFilesCard = new JPanel();
		noFilesCard.setBackground(Color.WHITE);
		noFilesCard.setLayout(new BoxLayout(noFilesCard, BoxLayout.Y_AXIS));
		noFilesCard.add(Box.createVerticalGlue());
		noFilesCard.add(noFilesRow);
		noFilesCard.add(Box.createVerticalGlue());

		filesListCard = new JPanel();
		filesListCard.setLayout(new BorderLayout());
		
		topPanel = new JPanel(new CardLayout());
		topPanel.setBackground(Color.WHITE);
		topPanel.add(noFilesCard, NOFILESCARD);
		topPanel.add(filesListCard, FILESCARD);
		topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#CCCCCC")));
		
		frame.getContentPane().add(topPanel, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
//		bottomPanel.setBackground(Color.WHITE);
		
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));		

		bottomNorthPanelContainer = new JPanel();
		bottomNorthPanelContainer.setBackground(Color.WHITE);
		bottomNorthPanelContainer.setLayout(new BoxLayout(bottomNorthPanelContainer, BoxLayout.X_AXIS));
		bottomPanel.add(bottomNorthPanelContainer);
		bottomPanel.add(Box.createVerticalStrut(12));
		
		JPanel bottomMiddlePanel = new JPanel();
//		bottomMiddlePanel.setBackground(Color.WHITE);
		bottomMiddlePanel.setLayout(new BoxLayout(bottomMiddlePanel, BoxLayout.X_AXIS));
		bottomMiddlePanel.add(Box.createHorizontalStrut(10));
		bottomMiddlePanel.add(btnLoadSpectrums);
		bottomMiddlePanel.add(Box.createHorizontalStrut(10));
		bottomMiddlePanel.add(btnAddTemp);
		bottomMiddlePanel.add(Box.createHorizontalGlue());
		bottomMiddlePanel.add(btnSaveSpectrum);
		bottomMiddlePanel.add(Box.createHorizontalStrut(10));
		bottomPanel.add(bottomMiddlePanel);
		bottomPanel.add(Box.createVerticalStrut(10));

		frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnLoadSpectrums){
			fileChooser.setMultiSelectionEnabled(true);
            int returnVal = fileChooser.showOpenDialog(frame);
            
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File[] files = fileChooser.getSelectedFiles();
                if (files.length < 2) {
                	JOptionPane.showMessageDialog(null, "A minimum of two files is required");
                }
                else {
                	//Create New Files List with Scroll Panel
                	filesListCard.removeAll();
                	
                	spectrumsList = new FileList(files, true);
                	addSelectionChangeHandler(spectrumsList);
                	JScrollPane jsp = new JScrollPane(spectrumsList);
                	jsp.setBorder(BorderFactory.createEmptyBorder());
            	    filesListCard.add(jsp);
                	
                	// Select All Spectrums
                    int start = 0;
                    int end = spectrumsList.getModel().getSize() - 1;
                    if (end >= 0) {
                    	spectrumsList.setSelectionInterval(start, end);
                    }
            	    
                	// Show Files Card
                	CardLayout cl = (CardLayout)(topPanel.getLayout());
            	    cl.last(topPanel);            	    
            	    btnSaveSpectrum.setEnabled(true);
                }
            } else {
                System.out.println("Open command cancelled by user.\n");
            }
		}
		else if (e.getSource() == btnSaveSpectrum){
			SpecrtrumProcessor sp = new SpecrtrumProcessor(spectrumsList.getSelectedObjectsArray());
			if (temperatureFile != null){
				sp.setTemperatureFile(temperatureFile);
			}
			sp.saveCumulated();
			JOptionPane.showMessageDialog(null, "Spectrum \"" + sp.getBaseFilename() + "\" saved successfully"); 
		}
		else if (e.getSource() == btnAddTemp){
			fileChooser.setMultiSelectionEnabled(false);
            int returnVal = fileChooser.showOpenDialog(frame);
            
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                temperatureFile = fileChooser.getSelectedFile();
    			JLabel l1 = new JLabel("Temperature Data:");
                l1.setForeground(Color.decode("#666666"));        
        		l1.setBorder(new EmptyBorder(15, 15, 15, 15));    			
                JLabel l2 = new JLabel();
                l2.setText(temperatureFile.getName());
                l2.setIcon(FileSystemView.getFileSystemView().getSystemIcon(temperatureFile));
                l2.setForeground(Color.decode("#666666"));        
        		l2.setBorder(new EmptyBorder(15, 24, 15, 15));
    			JPanel p = new JPanel();
    			p.setBackground(Color.WHITE);
    			p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
    			p.add(l1);
    			p.add(l2);
    			p.add(Box.createHorizontalGlue());
    			p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#DDDDDD")));
    			bottomNorthPanelContainer.removeAll();
    			bottomNorthPanelContainer.add(p);
    			frame.validate();
            } else {
                System.out.println("Open command cancelled by user.\n");
            }
		}
	}

	private void addSelectionChangeHandler(JList<File> list){
		list.addListSelectionListener(new ListSelectionListener(){
			@Override
			public void valueChanged(ListSelectionEvent e) {
			    if (e.getValueIsAdjusting() == false) {

			        if (spectrumsList.getSelectedIndices().length < 2) {
			            btnSaveSpectrum.setEnabled(false);
			        } else {
			            btnSaveSpectrum.setEnabled(true);
			        }
			    }
			}
		});		
	}
}