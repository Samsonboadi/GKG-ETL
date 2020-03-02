/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merkator.gkgexcel2geoserver;

import java.io.File;

import javax.xml.bind.annotation.XmlRootElement;

import com.merkator.gkgexcel2geoserver.GKGConnectionClasses.GeoserverConn;
import com.merkator.gkgexcel2geoserver.GKGConnectionClasses.Ogr2OgrConn;
import com.merkator.gkgexcel2geoserver.GKGConnectionClasses.PostgresSQLConn;

/**
 * @author miguel
 */

@XmlRootElement
public class GKGConnections {
	public PostgresSQLConn postgresSQL;
	public Ogr2OgrConn ogr2Ogr;
	public GeoserverConn geoserver;
	public String locationXLSX;
	public File xmldta;

	public GKGConnections() {
		postgresSQL = new PostgresSQLConn();
		ogr2Ogr = new Ogr2OgrConn();
		geoserver = new GeoserverConn();
	}
}
