package com.merkator.gkgexcel2geoserver;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;

import com.merkator.Xlsx.MakeDir;

/**
* @author miguel and Boadi Samson
*/

public class WindowsGUI {
	/** Define fields. */
	public static String fileName;
	public static String path;	
	public static String dbtype;
	public  static String excelpath;
	public  static String secondNamen;
	public static String excelpathnew;
	/** Public static Scanner input;. */
	
	
	private JFrame frmGkgEtl;
	private static String[] savedArgs;
			/** Launch the application. */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					WindowsGUI window = new WindowsGUI();
					window.frmGkgEtl.setVisible(true);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
		savedArgs = args;	
	}	
	

		/** Create the application. 
		 * @throws Throwable */
	public WindowsGUI() throws Throwable {
		initialize();
	}
	
	public static void openDir(String namen) throws IOException {
		String path = System.getProperty("user.home") + File.separator + "Documents";
        Desktop desktop = Desktop.getDesktop();
        File dirToOpen = null;
        try {
            dirToOpen = new File(path+ File.separator + namen );
            desktop.open(dirToOpen);
        } catch (IllegalArgumentException iae) {
            System.out.println("File Not Found");
            System.out.println(path+ File.separator + namen );
        }
    }
	

	/** Initialize the contents of the frame. 
	 * @throws Throwable */
	private void initialize() throws Throwable {
		frmGkgEtl = new JFrame();
		frmGkgEtl.setTitle("GKG ETL 2.2.1");
		frmGkgEtl.setBounds(100, 100, 450, 300);
		frmGkgEtl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGkgEtl.getContentPane().setLayout(null);

		JComboBox<String> comboBox = new JComboBox<>();
		comboBox.setBounds(24, 34, 114, 22);
		comboBox.addItem("Test");
		comboBox.addItem("Production");
		//comboBox.setSelectedItem(null);
		frmGkgEtl.getContentPane().add(comboBox);
		JLabel label = new JLabel();
		label.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/gkg1.png")).getImage().getScaledInstance(200, 120, Image.SCALE_SMOOTH)));
		label.setBounds(200, 54, 150, 100);
		frmGkgEtl.getContentPane().add(label);
		
		
		
		
		
		

		JButton btnSelectExcel = new JButton("Select Excel");
		btnSelectExcel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel file", "xls", "xlsx");
				fc.setFileFilter(filter);				

				int response = fc.showOpenDialog(null);
				if (response == JFileChooser.APPROVE_OPTION) {
					excelpath = fc.getSelectedFile().getAbsolutePath();
									
									}
			}
		});
		

		btnSelectExcel.setBounds(24, 79, 114, 25);
		frmGkgEtl.getContentPane().add(btnSelectExcel);

		JButton btnNewButton = new JButton("Generate");
		btnNewButton.addActionListener(new ActionListener() {
			/** Try {. */

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String path = System.getProperty("user.home") + File.separator + "Documents";
				dbtype = (String) comboBox.getSelectedItem();
				if (dbtype ==null){
					JOptionPane.showMessageDialog(null, "Please select the Database type Test or Production");
					return; 
				}
				GKGConnections connections = new SerializeXMLConnections().deSerializeGkgConnections();
								File f = new File(excelpath);
		        	             if (!f.exists())  {
	            	 
	     			JOptionPane.showConfirmDialog(null, 
					   "Did you forget to select the Service Register?",null, JOptionPane.YES_NO_OPTION);
		            	 	             }
				String fileNameWithOutExt = FilenameUtils.getBaseName(excelpath);
				secondNamen = fileNameWithOutExt.toLowerCase().replace(" ", "");
	
				File file = new File("C:"+secondNamen);
				if (!file.exists()) {
					file.mkdir();
				}
				MakeDir folder = new MakeDir();
				folder.NewDir(secondNamen,ProcessGkgXLS.myDateog);	 
				if (savedArgs.length > 0) {
					switch (savedArgs[0].toLowerCase()) {
					case "serializeXML":
						new SerializeXMLConnections().serialize();
						break;
					default:
						new ProcessGkgXLS(connections).startProcess();
					}
				} else {

					new ProcessGkgXLS(connections).startProcess();
				}

								JOptionPane.showMessageDialog(null,
						"CMD for " + dbtype + " generated. Check "+ path+ File.separator +secondNamen+"\\"+ProcessGkgXLS.myDateog+"-"+ "for results");
								try {
									WindowsGUI.openDir(secondNamen);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						}
			
			});
		
				btnNewButton.setBounds(173, 184, 97, 25);
		frmGkgEtl.getContentPane().add(btnNewButton);

	}
	
	
	
}
