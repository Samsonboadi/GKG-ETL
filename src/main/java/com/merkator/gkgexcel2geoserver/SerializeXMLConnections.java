/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merkator.gkgexcel2geoserver;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class SerializeXMLConnections {
	public static File xml;
	GKGConnections gkgconnection;
				public GKGConnections deSerializeGkgConnections() {
		try {
			JAXBContext jc = JAXBContext.newInstance(GKGConnections.class);

			Unmarshaller unmarshaller = jc.createUnmarshaller();
			// System.out.println(main.dbtype);
			System.out.println(WindowsGUI.dbtype);
			if ("Test".equals(WindowsGUI.dbtype)) {
				InputStream xml = SerializeXMLConnections.class.getResourceAsStream("/GKGConnectionstest.xml");

				//xml = new File("GKGConnectionstest.xml");
				gkgconnection = (GKGConnections) unmarshaller.unmarshal(xml);
				// System.out.println(main.dbtype);
			}

			else if ("Production".equals(WindowsGUI.dbtype)) {
				InputStream xml = SerializeXMLConnections.class.getResourceAsStream("/GKGConnectionsprod.xml");
				//xml = new File("GKGConnectionsprod.xml");
				gkgconnection = (GKGConnections) unmarshaller.unmarshal(xml);
				// System.out.println(main.dbtype);
							}else{
				//System.out.println(WindowsGUI.dbtype);
				System.out.println(
						"Something happened please check the spelling of the Dbtype\nAccepted types are test or production");
				JOptionPane.showMessageDialog(null, "Please select the Database type Test or Production");
							} //else {
				//System.out.println(
						//"Something happened please check the spelling of the Dbtype\nAccepted types are test or production");
				// System.out.println(main.dbtype);
			//}

			// File xml = new File("src/main/resources/GKGConnectionsprod.xml");

			// GKGConnections gkgconnection = (GKGConnections) unmarshaller.unmarshal(xml);

			// Marshaller marshaller = jc.createMarshaller();
			// marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			// marshaller.marshal(gkgconnection, System.out);

			return gkgconnection;
		} catch (JAXBException ex) {
			Logger.getLogger(SerializeXMLConnections.class.getName()).log(Level.SEVERE, null, ex);
			JOptionPane.showMessageDialog(null, "Please select the Database type Test or Production");
		}
		return null;
	}

	public void serialize() {
		try {
			GKGConnections gkg = new GKGConnections();

			gkg.locationXLSX = "C:\\temp\\test.xlsx";
			gkg.geoserver.url = "";
			gkg.ogr2Ogr.path = "C:\\os4gw\\bin\\ogr2ogr.exe";
			gkg.postgresSQL.host = "";
			gkg.postgresSQL.port = "";
			gkg.postgresSQL.dname = "";
			gkg.postgresSQL.password = "";
			gkg.postgresSQL.schema = "";
			gkg.postgresSQL.user = "";
			gkg.geoserver.password = "";
			gkg.geoserver.user = "";
			
			// Write it
			JAXBContext ctx = JAXBContext.newInstance(GKGConnections.class);

			Marshaller m = ctx.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			StringWriter sw = new StringWriter();
			m.marshal(gkg, sw);
			sw.close();
			System.out.println(sw.toString());

			// Read it back
			JAXBContext readCtx = JAXBContext.newInstance(GKGConnections.class);
			Unmarshaller um = readCtx.createUnmarshaller();

			GKGConnections newOrder = (GKGConnections) um.unmarshal(new StringReader(sw.toString()));
			System.out.println(newOrder);
		} catch (Exception ex) {
			Logger.getLogger(WindowsGUI.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
