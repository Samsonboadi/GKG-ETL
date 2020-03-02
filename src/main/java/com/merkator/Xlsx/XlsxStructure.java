/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merkator.Xlsx;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.merkator.gkgexcel2geoserver.ProcessGkgXLS;
import com.merkator.gkgexcel2geoserver.WindowsGUI;

/**
 * @author miguel and Boadi Samson
 */
public class XlsxStructure {
	/** Create a date object. */
	LocalDate myDate = LocalDate.now();
	LocalTime myTime = LocalTime.now();
	int hour = myTime.getHour();
	int minute = myTime.getMinute();
	int seconds = myTime.getSecond();
	String baseName = FilenameUtils.getBaseName(WindowsGUI.excelpath);

	/**
	 * @return the servicesPerKlant
	 */
	public List<StructPerKlant> getServicesPerKlant() {
		return servicesPerKlant;
	}

	/**
	 * @param servicesPerKlant the servicesPerKlant to set
	 */
	/*
	 * public void setServicesPerKlant(List<StructPerKlant> servicesPerKlant) {
	 * this.servicesPerKlant = servicesPerKlant; }
	 */

	private List<StructPerKlant> servicesPerKlant;

	public XlsxStructure() {
		servicesPerKlant = new ArrayList<>();
	}	

		public static void copyXLS(String layerName) {
		File dirFrom = new File(WindowsGUI.excelpath);
		//File f = new File("C:"+layerName +"/" + myDateog +"-"+layerHour+layerMinute+layerSeconds);
		//if (!f.exists() && !f.isDirectory()) {
			//f.mkdirs();
		System.out.println("Hello"+MakeDir.f.getAbsolutePath() +"//" + layerName +".xlsx");
		File dirTo = new File(MakeDir.f.getAbsolutePath() +"//" + layerName +".xlsx");
					//File dirTo = new File("C://"+layerName+"//"+myDateog+"-"+layerHour+layerMinute+layerSeconds+"//" + baseName +".xlsx");
				try {
			ProcessGkgXLS.copyFile(dirFrom, dirTo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
//}
