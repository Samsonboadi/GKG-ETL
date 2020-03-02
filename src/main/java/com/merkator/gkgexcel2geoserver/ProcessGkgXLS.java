/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merkator.gkgexcel2geoserver;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate; // import the LocalDate class
import java.time.LocalTime; // import the LocalTime class
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xslf.model.geom.Path;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.merkator.Xlsx.Lagen;
import com.merkator.Xlsx.MakeDir;
import com.merkator.Xlsx.StructPerKlant;
import com.merkator.Xlsx.XlsxStructure;
import com.sun.xml.bind.v2.schemagen.xmlschema.List;

/**
 * @author miguel and Boadi Samson
 */
public class ProcessGkgXLS {
	private GKGConnections connections;
	public static XlsxStructure xlslDoc;
	public static LocalDate myDateog = LocalDate.now();
	public LocalTime myTimeog = LocalTime.now();
	public int hour = myTimeog.getHour();
	public int minute = myTimeog.getMinute();
	public int seconds = myTimeog.getSecond();
	public ArrayList<String> xlaags = new ArrayList<>();
	static final String AT = "\\AT\\"; 
	static final String PROD = "\\Prod\\"; 	
	static final String DEPRECATED = "vervallen";
	static final String GEGENEREERD = ".\\gegenereerd\\";
	static final String ATGEGENEREERD = "\\AT\\gegenereerd\\";
	static final String PRODGEGENEREERD = "\\Prod\\gegenereerd\\";
	static final String ENCODER = "UTF-8";
	static final String PSQLPATH = "\"C:\\Program Files (x86)\\pgAdmin 4\\v4\\runtime\\psql" +"\"";
	

	public ProcessGkgXLS(GKGConnections connections) {
		this.connections = connections;
		xlslDoc = new XlsxStructure();
	}
	
	public void layerStatus() {
		StructPerKlant layers = xlslDoc.getServicesPerKlant().get(0);
		for (int i = 0; i < layers.getLagen().size(); i++) {
			Lagen laag = layers.getLagen().get(i);
			String layerStatus = laag.status;
			 xlaags.add(layerStatus);
	}
	}
	



	private void generateOgrCommands() {
		layerStatus();
		PrintWriter writer  = null;		
		PrintWriter writerXdelete  = null;	
		try {	
			StructPerKlant layers = xlslDoc.getServicesPerKlant().get(0);

			
				if ("Test".equals(WindowsGUI.dbtype)) {	
				writer = new PrintWriter(MakeDir.f.getAbsolutePath() + ATGEGENEREERD + layers.Klantnaam + "_ogr2ogr" + ".cmd",
						ENCODER);
				writer.println("set GDAL_DATA=C:\\OSGeo4W64\\share\\gdal");
				writer.println(PSQLPATH + " -U "+ connections.postgresSQL.user + " -h " + connections.ogr2Ogr.host_ogr +" -p "+ connections.ogr2Ogr.port_ogr + " -d " + connections.postgresSQL.dname + " -c "+ "\"CREATE SCHEMA IF NOT EXISTS "+ layers.Klantnaam + "\"");
				PrintWriter writerRun = new PrintWriter(MakeDir.f.getAbsolutePath() + AT +"0200 run_ogr2ogr.cmd");
				writerRun.println(GEGENEREERD +layers.Klantnaam+"_ogr2ogr.cmd >> .\\log\\"+layers.Klantnaam+"_ogr2ogr.log  2>" +".\\log\\"+layers.Klantnaam+"_ogr2ogr.err");
				writerRun.close();
				//java.nio.file.Path currentRelativePath = Paths.get("");
				//String s = currentRelativePath.toAbsolutePath().toString();
				//System.out.println("Current relative path is: " + s);
				String path = System.getProperty("user.home") + File.separator + "Documents";
				System.out.println("Current relative path is: " + path);


				if ((xlaags).contains(DEPRECATED)) {
					
					writerXdelete = new PrintWriter(MakeDir.f.getAbsolutePath() + ATGEGENEREERD + layers.Klantnaam + "_ogr2ogrDelete" + ".cmd",
							ENCODER);	
					writerXdelete.println("@SET PGPASSWORD=vKupHKQBCfLze74B");
					PrintWriter xdeleteRun = new PrintWriter(MakeDir.f.getAbsolutePath() + AT +"0100 run_ogr2ogrDelete.cmd");
					xdeleteRun.println(GEGENEREERD +layers.Klantnaam+"_ogr2ogrDelete.cmd >> .\\log\\"+layers.Klantnaam+"_ogr2ogrDelete.log  2>" +".\\log\\"+layers.Klantnaam+"_ogr2ogrDelete.err");
					xdeleteRun.close();
			}
							
			}else{
				writer = new PrintWriter(MakeDir.f.getAbsolutePath() + PRODGEGENEREERD + layers.Klantnaam + "_ogr2ogr" + ".cmd",
						ENCODER);
				writer.println("set GDAL_DATA=C:\\OSGeo4W64\\share\\gdal");
				writer.println("\"C:\\Program Files (x86)\\pgAdmin 4\\v4\\runtime\\psql" +"\""+ " -U "+ connections.postgresSQL.user + " -h  " + "gegevensknooppuntgroningen.nl" +" -p "+ connections.postgresSQL.port + " -d " + connections.postgresSQL.dname + " -c  "+ "\"CREATE SCHEMA IF NOT EXISTS "+ layers.Klantnaam +"\"");
				PrintWriter writerRun = new PrintWriter(MakeDir.f.getAbsolutePath() + PROD +"0200 run_ogr2ogr.cmd");
				writerRun.println(GEGENEREERD +layers.Klantnaam+"_ogr2ogr.cmd >> .\\log\\"+layers.Klantnaam+"_ogr2ogr.log  2>" +".\\log\\"+layers.Klantnaam+"_ogr2ogr.err");
				writerRun.close();

				if ((xlaags).contains(DEPRECATED)) {
					writerXdelete = new PrintWriter(MakeDir.f.getAbsolutePath() + PRODGEGENEREERD + layers.Klantnaam + "_ogr2ogrDelete" + ".cmd",
							ENCODER);
					writerXdelete.println("@SET PGPASSWORD=vKupHKQBCfLze74B");
										PrintWriter xdeleteRun = new PrintWriter(MakeDir.f.getAbsolutePath() + PROD +"0100 run_ogr2ogrDelete.cmd");
					xdeleteRun.println(GEGENEREERD+layers.Klantnaam+"_ogr2ogrDelete.cmd >> .\\log\\"+layers.Klantnaam+"_ogr2ogrDelete.log  2>" +".\\log\\"+layers.Klantnaam+"_ogr2ogrDelete.err");
					xdeleteRun.close();
			}
			}
			
				for (int i = 0; i < layers.getLagen().size(); i++) {
				Lagen laag = layers.getLagen().get(i);
				if (laag.Kaartlaag != null && laag.Url != null && !laag.Kaartlaag.isEmpty()
						&& !laag.Url.isEmpty()) {
					if (laag.Type != null && laag.Type.toLowerCase().contains("wfs") && laag.status.contains("actueel")) // These must be in the service register to avoid errors
						{
						String commandOgr = connections.ogr2Ogr.path
								+ " --config GDAL_HTTP_UNSAFESSL YES --config CPL_DEBUG ON -lco OVERWRITE=YES -overwrite -f PostgreSQL PG:\"";

						commandOgr += "host=" + connections.ogr2Ogr.host_ogr;
						commandOgr += " port=" + connections.ogr2Ogr.port_ogr;
						commandOgr += " user=" + connections.postgresSQL.user;
						commandOgr += " password=" + connections.postgresSQL.password;
						commandOgr += " dbname=" + connections.postgresSQL.dname;
						commandOgr += "\" ";
						commandOgr += "WFS:";
														commandOgr += "\""+laag.Url + "\"";

						String namen[] = laag.Kaartlaag.split("\\s+");
						String name = "";
						for (String namen1 : namen) {
											String firstNamen = namen1.substring(0, 1).toLowerCase().replace(" ", "");
												String secondNamen = namen1.substring(1).toLowerCase().replace(" ", "");
							firstNamen = firstNamen.replace("_", "");						
							secondNamen = secondNamen.replace("_", "");
							secondNamen = secondNamen.replace("-", "");						
							name += firstNamen + secondNamen;					
													}
						commandOgr += " -nln " + layers.Klantnaam.toLowerCase() + "." + name ;

						writer.println(commandOgr);
					}
					
					if (laag.Type != null && laag.Type.toLowerCase().contains("arcgis")) {
						String commandOgr = connections.ogr2Ogr.path + " -overwrite -f PostgreSQL PG:\"";

						commandOgr += "host=" + connections.postgresSQL.host;
						commandOgr += " port=" + connections.postgresSQL.port;
						commandOgr += " user=" + connections.postgresSQL.user;
						commandOgr += " password=" + connections.postgresSQL.password;
						commandOgr += " dbname=" + connections.postgresSQL.dname;	
						commandOgr += "\" ";
						commandOgr += "WFS:";
						commandOgr += "\""+laag.Url + "\"";
						commandOgr += " -nln " + layers.Klantnaam.toLowerCase() + "." + laag.Kaartlaag.toLowerCase().replace(" ", "");
											writer.println(commandOgr);
					}
					String url;
					if ("Test".equals(WindowsGUI.dbtype)) {
						url ="at.gegevensknooppuntgroningen.nl";
					}else {
						url ="gegevensknooppuntgroningen.nl";
											}

					
					if (laag.Aanwezig != null && laag.Type != null && laag.status.contentEquals(DEPRECATED)) { 

				
											String commandXdelete = "\"C:\\Program Files (x86)\\pgAdmin 4\\v4\\runtime\\psql" +"\"";
												commandXdelete += " -U " + connections.postgresSQL.user;
						commandXdelete +=  " -h " + url;
						commandXdelete += " -p " + connections.ogr2Ogr.port_ogr;
						commandXdelete += " -d " + connections.postgresSQL.dname;	
						commandXdelete += " -c ";
						commandXdelete += "\"DROP TABLE IF EXISTS ";
						commandXdelete += layers.Klantnaam.toLowerCase() + "." + laag.Kaartlaag.toLowerCase().replace(" ", "")+"\"";
						writerXdelete.println(commandXdelete);
					}
					}
															}
			

		} catch (Exception ex) {
			Logger.getLogger(ProcessGkgXLS.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			/* if (writer != null && writerXdelete != null) {
				    try {
				    	writer.close();
				    	if ((xlaags).contains(DEPRECATED)) {
							writerXdelete.close();
									}
				    } catch(Exception e){
				    	Logger.getLogger(ProcessGkgXLS.class.getName()).log(Level.SEVERE, null, e);
				    }
				  }*/

			writer.close();
			if ((xlaags).contains(DEPRECATED)) {
			writerXdelete.close();
					}
		}
	}
/** Generating the Geoserver commands. */
	private void generateGeoserverCommands() {
		StructPerKlant layers;
		PrintWriter writer = null;
		PrintWriter writerXdeleteGeo  = null;	
		PrintWriter xdeleteRun = null;
		try {
			String codeduser = URLEncoder.encode(connections.geoserver.user, StandardCharsets.UTF_8.toString());			
			String codedpass = connections.geoserver.password;
			layers = xlslDoc.getServicesPerKlant().get(0);
			
			if ("Test".equals(WindowsGUI.dbtype)) {
								writer = new PrintWriter(MakeDir.f.getAbsolutePath() + ATGEGENEREERD + layers.Klantnaam + "_geoserver" +".cmd",
										ENCODER);
				PrintWriter writerRun = new PrintWriter(MakeDir.f.getAbsolutePath() + AT +"0300 run_geoserver.cmd");
				writerRun.println(GEGENEREERD +layers.Klantnaam+"_geoserver.cmd >> .\\log\\"+layers.Klantnaam+"_geoserver.log  2>" +".\\log\\"+layers.Klantnaam+"_geoserver.err");
				writerRun.close();
				ArrayList<String> xlaaggeo = new ArrayList<>();
				for (int i = 0; i < layers.getLagen().size(); i++) {
					Lagen laag = layers.getLagen().get(i);
					String layerStatus = laag.status;
					xlaaggeo.add(layerStatus);
				if ((xlaaggeo).contains(DEPRECATED)) {
					try {
					writerXdeleteGeo = new PrintWriter(MakeDir.f.getAbsolutePath() + ATGEGENEREERD + layers.Klantnaam + "_geoserverDelete" + ".cmd",
							ENCODER);					
					xdeleteRun = new PrintWriter(MakeDir.f.getAbsolutePath() + AT +"0000 run_geoserverDelete.cmd");
					xdeleteRun.println(GEGENEREERD +layers.Klantnaam+"_geoserverDelete.cmd >> .\\log\\"+layers.Klantnaam+"_geoserverDelete.log  2>" +".\\log\\"+layers.Klantnaam+"_geoserverDelete.err");
					}catch (Exception ex) {
						Logger.getLogger(ProcessGkgXLS.class.getName()).log(Level.SEVERE, null, ex);
					}finally {
						 if (xdeleteRun != null ) {
							    try {
							    	xdeleteRun.close();;
							    	
							    } catch(Exception e){
							    	Logger.getLogger(ProcessGkgXLS.class.getName()).log(Level.SEVERE, null, e);
							    }
							  }
						
					}
					
								}
				}
			}else {				
				writer = new PrintWriter(MakeDir.f.getAbsolutePath() + PRODGEGENEREERD + layers.Klantnaam + "_geoserver" + ".cmd",
						ENCODER);
				PrintWriter writerRun = new PrintWriter(MakeDir.f.getAbsolutePath() + PROD +"0300 run_geoserver.cmd");
				writerRun.println(GEGENEREERD +layers.Klantnaam+"_geoserver.cmd >> .\\log\\"+layers.Klantnaam+"_geoserver.log  2>" +".\\log\\"+layers.Klantnaam+"_geoserver.err");
				writerRun.close();
				ArrayList<String> xlaags = new ArrayList<>();
				for (int i = 0; i < layers.getLagen().size(); i++) {
					Lagen laag = layers.getLagen().get(i);
					String layerStatus = laag.status;
					 xlaags.add(layerStatus);
				if ((xlaags).contains(DEPRECATED)) {
					writerXdeleteGeo = new PrintWriter(MakeDir.f.getAbsolutePath() + PRODGEGENEREERD + layers.Klantnaam + "_geoserverDelete" + ".cmd",
							ENCODER);	
					xdeleteRun = new PrintWriter(MakeDir.f.getAbsolutePath() + PROD +"0000 run_geoserverDelete.cmd");
					xdeleteRun.println( GEGENEREERD +layers.Klantnaam+"_geoserverDelete.cmd >> .\\log\\"+layers.Klantnaam+"_geoserverDelete.log  2>" +".\\log\\"+layers.Klantnaam+"_geoserverDelete.err");
					xdeleteRun.close();					
			}
				}
			}
						String previousThema;
			String previousKlant = "";	
			previousThema = layers.Klantnaam.toLowerCase();
			String cmdWsp = "curl -v --insecure -u " + codeduser;
			cmdWsp += ":" + codedpass + " -XPOST -H \"Content-type: text/xml\" -d ";
			cmdWsp += "\"<workspace><name>" + previousThema + "</name></workspace>\" ";
			cmdWsp += connections.geoserver.url + "/geoserver/rest/workspaces";
			writer.println(cmdWsp);

			for (int i = 0; i < layers.getLagen().size(); i++) {
				Lagen laag = layers.getLagen().get(i);

				if (laag.Kaartlaag != null && laag.Url != null && !laag.Kaartlaag.isEmpty()
						&& !laag.Url.isEmpty() && laag.status.toLowerCase().contains("actueel")) {
					if (!previousKlant.equals(layers.Klantnaam)) {
						previousKlant = layers.Klantnaam;
						String cmd = "curl -v --insecure -u " + codeduser;
						cmd += ":" + codedpass + " -XPOST -H \"Content-type: text/xml\" -d ";
						cmd += "\"<dataStore><name>" + layers.Klantnaam.toLowerCase() + "</name><connectionParameters>";
						cmd += "<host>" + connections.postgresSQL.host + "</host>";
						cmd += "<port>" + connections.postgresSQL.port + "</port>";
						cmd += "<database>" + connections.postgresSQL.dname + "</database>";
						cmd += "<user>" + connections.postgresSQL.user + "</user>";
						cmd += "<passwd>" + connections.postgresSQL.password + "</passwd>";
						cmd += "<dbtype>postgis</dbtype>";
						cmd += "<entry key=\\\"Expose primary keys\\\">true</entry><entry key=\\\"schema\\\">"
								+ layers.Klantnaam.toLowerCase() + "</entry>";

						cmd += "</connectionParameters></dataStore>\" ";
						cmd += connections.geoserver.url + "/geoserver/rest/workspaces/" + previousThema + "/datastores";
						writer.println(cmd);
					}

					String namen[] = laag.Kaartlaag.split("\\s+");
					String name = "";
					for (String namen1 : namen) {
						String firstNamen = namen1.substring(0, 1).toLowerCase().replace(" ", "");
						String secondNamen = namen1.substring(1).toLowerCase().replace(" ", "");
						firstNamen = firstNamen.replace("_", "");
						secondNamen = secondNamen.replace("_", "");
						secondNamen = secondNamen.replace("-", "");						
						name += firstNamen + secondNamen;
										
					}					

					String cmd = "curl -v --insecure -u " + codeduser;
					cmd += ":" + codedpass + " -XPOST -H \"Content-type: text/xml\" -d \"";
					cmd += "<featureType><name>" + name.toLowerCase() + "</name><nativeName>" + name.toLowerCase()
							 + "</nativeName> <srs>EPSG:28992</srs>";
					cmd += "<nativeBoundingBox><minx>0</minx><maxx>999999</maxx><miny>0</miny><maxy>999999</maxy></nativeBoundingBox></featureType>\" ";
					cmd += connections.geoserver.url + "/geoserver/rest/workspaces/" + previousThema + "/datastores/"
							+ layers.Klantnaam.toLowerCase() + "/featuretypes";
					writer.println(cmd);				
				}
					if (laag.Type != null && laag.status.toLowerCase().contains(DEPRECATED)) {					
					String commandXdeleteGeo = "curl -v --insecure ";
					String commandXdeleteGe = "curl -v --insecure";
					commandXdeleteGeo += " -u " + codeduser+":" + codedpass;
					commandXdeleteGe += " -u " + codeduser+":" + codedpass;
					commandXdeleteGeo += " -XDELETE ";
					commandXdeleteGe += " -XDELETE ";
					commandXdeleteGeo += "\""+connections.geoserver.url;
					commandXdeleteGe += "\""+connections.geoserver.url;
					commandXdeleteGeo += "/geoserver/rest/layers/";		
					commandXdeleteGe += "/geoserver/rest/workspaces/" + previousThema + "/datastores/" + previousThema + "/featuretypes/"+laag.Kaartlaag.toLowerCase().replace(" ", "")+".xml";

					commandXdeleteGeo += layers.Klantnaam.toLowerCase() + ":" + laag.Kaartlaag.toLowerCase().replace(" ", "")+"\"";							

					writerXdeleteGeo.println(commandXdeleteGeo);
					writerXdeleteGeo.println(commandXdeleteGe);
					
				}
							}
		} catch (Exception ex) {
			Logger.getLogger(ProcessGkgXLS.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			 if (writer != null ) {
				    try {
				    	writer.close();
				    	writerXdeleteGeo.close();
				    	
				    } catch(Exception e){
				    	Logger.getLogger(ProcessGkgXLS.class.getName()).log(Level.SEVERE, null, e);
				    }
				  }
			
			
			
					}

	}

	public void startProcess() {
		FileInputStream fis = null;

		try {
			File myFile = new File(WindowsGUI.excelpath);
			//System.out.println(WindowsGUI.excelpath);
			fis = new FileInputStream(myFile);
			// Finds the workbook instance for XLSX file
			XSSFWorkbook myWorkBook = new XSSFWorkbook(fis); // Return first sheet from the XLSX workbook
			XSSFSheet mySheet = myWorkBook.getSheetAt(0); // Get iterator to all the rows in current sheet
			Iterator<Row> rowIterator = mySheet.iterator(); // Traversing over each row of XLSX file
			int rownum = 1;
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next(); // For each row, iterate through each columns
				if (rownum == 1) {
					processFirstRow(row);
				}
				if (rownum > 2) {
					processDataRows(row);
				}

				rownum++;
			}
			StructPerKlant layers = xlslDoc.getServicesPerKlant().get(0);
			generateGeoserverCommands();
			generateOgrCommands();
			XlsxStructure.copyXLS(layers.Klantnaam);
			xlaags.clear();

			// Read more:
			// http://www.java67.com/2014/09/how-to-read-write-xlsx-file-in-java-apache-poi-example.html#ixzz4uSNt5CF6
		} catch (IOException ex) {
			Logger.getLogger(WindowsGUI.class.getName()).log(Level.SEVERE, null, ex);
					} finally {
						 if (fis != null ) {
							    try {
							    	fis.close();							    	
							    } catch(Exception e){
							    	Logger.getLogger(ProcessGkgXLS.class.getName()).log(Level.SEVERE, null, e);
							    }
							  }
		}
	}

	private String cellStr(Cell cell) {
		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			return cell.getStringCellValue();
		}

		return null;
	}

	private StructPerKlant getKlant(int cellNum) {
		if (cellNum <= 2) {
			return null;
		} else {
			cellNum = cellNum - 2;
			float calcNum = (float) cellNum / 4;
			int nr = (int) Math.abs(calcNum);
			if (cellNum % 4 != 0) {
				nr++;
			}
			return xlslDoc.getServicesPerKlant().get(nr - 1);
		}
	}

	private void processDataRows(Row row) {
		StructPerKlant klant = xlslDoc.getServicesPerKlant().get(0);

		if (klant != null) {	
			Lagen laag = new Lagen();

			laag.Kaartlaag = cellStr(row.getCell(0, Row.CREATE_NULL_AS_BLANK));
			laag.Thema = cellStr(row.getCell(1, Row.CREATE_NULL_AS_BLANK));
			laag.Aanwezig = cellStr(row.getCell(2, Row.CREATE_NULL_AS_BLANK));
			laag.Type = cellStr(row.getCell(3, Row.CREATE_NULL_AS_BLANK));
			laag.Metadata = cellStr(row.getCell(4, Row.CREATE_NULL_AS_BLANK));
			laag.Url = cellStr(row.getCell(5, Row.CREATE_NULL_AS_BLANK));
			laag.status = cellStr(row.getCell(7, Row.CREATE_NULL_AS_BLANK));
			klant.getLagen().add(laag);
		}
	}

	private void processFirstRow(Row row) {
		Iterator<Cell> cellIterator = row.cellIterator();
		int cellnum = 0;

		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				String waarde = cell.getStringCellValue();
				if (!waarde.contains("PDOK waterschapen")) {
					StructPerKlant klant = new StructPerKlant();
					klant.Klantnaam = waarde;
					xlslDoc.getServicesPerKlant().add(klant);
				}
			}
			cellnum++;
		}
	}
		public static void copyFile( File from, File to ) throws IOException {
	    Files.copy( from.toPath(), to.toPath(),StandardCopyOption.REPLACE_EXISTING);
	   
	    
	}

	
	}

