package com.merkator.Xlsx;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.JOptionPane;

/**
* @author Boadi Samson
*/

public class MakeDir {
	public static String serviceName;
	public static LocalDate serviceDate;
	static LocalDate myDateog = LocalDate.now();
	LocalTime myTimeog = LocalTime.now();
	int H = myTimeog.getHour();
	int M = myTimeog.getMinute();
	int S = myTimeog.getSecond();
	public static File f;
	

	public void NewDir(String serviceNam,LocalDate serviceDate) {
						//Path path = "C:Hunzeenaas";
		String path = System.getProperty("user.home") + File.separator + "Documents";
		String baseName = path + File.separator + serviceNam;
		String baseNameExtend = baseName +"/" + serviceDate;
		String baseNameExtendTime = baseNameExtend +"-"+ H+M+S;
		f = new File(path+ File.separator +serviceNam +"/" + serviceDate +"-"+H+M+S);
		System.out.println(f);
		System.out.println(H+M+S);
		if (f.exists()) {
						File prod = new File(baseNameExtendTime +"/Prod");
			File aT = new File(baseNameExtendTime +"/AT");
			File binProd = new File(baseNameExtendTime +"/Prod/gegenereerd");		
			File binAT = new File(baseNameExtendTime +"/AT/gegenereerd");	
			File logsProd = new File(baseNameExtendTime +"/Prod/log");
			File logsAT = new File(baseNameExtendTime +"/AT/log");
			// check if the pathname already exists
			// if not create it

			// create the folder
							prod.mkdir();
				aT.mkdir();			
				logsProd.mkdir();
				logsAT.mkdir();
				binProd.mkdir();
				binAT.mkdir();
		}else {				
							  		// initialize File object		

		File file = new File(baseName);
		File fileDate = new File(baseNameExtend +"-"+H+M+S);
		File prod = new File(baseNameExtend +"-"+ H+M+S+"/Prod");
		File aT = new File(baseNameExtend +"-"+ H+M+S+"/AT");	
		File binProd = new File(baseNameExtendTime+"/Prod/gegenereerd");
		File binAT = new File(baseNameExtendTime +"/AT/gegenereerd");
		File logsProd = new File(baseNameExtendTime +"/Prod/log");
		File logsAT = new File(baseNameExtendTime +"/AT/log");
		// check if the pathname already exists
		// if not create it
			// create the folder
			file.mkdir();
			fileDate.mkdir();			
			boolean resultprod=prod.mkdir();
			aT.mkdir();		
			logsProd.mkdir();
			logsAT.mkdir();
			binProd.mkdir();
			binAT.mkdir();
								if(resultprod){
				System.out.println("Successfully created ");
			}
			else{
				System.out.println("Failed creating ");
				JOptionPane.showMessageDialog(null, "Please Restart the EL tool");
			}
		}
	}
}	