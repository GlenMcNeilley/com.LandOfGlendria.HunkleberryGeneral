package com.LandOfGlendria.HunkleberryGeneral;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.bukkit.plugin.Plugin;
import java.io.*;
import java.util.*;

public class HGBouncer {
 
	private Plugin plugin;
	private Properties bouncerProperties;
	public HGMessageManagement msg;
	public HGConfig cfg;
	public File bouncerPropertiesFile;
		
	public HGBouncer(Plugin plugin, HGMessageManagement msg, HGConfig cfg) {
		this.plugin = plugin;
		this.msg = msg;
		this.cfg = cfg;
		bouncerProperties = new Properties(); 
		bouncerPropertiesFile = new File(HGStatics.BOUNCER_PROPERTIES);
		try {
			manageBouncerPropertyFiles();
		} catch (Exception e) {
			msg.severe((new StringBuilder("Caught ")).append(e.getClass().getName()).append(" in HGBouncer().").toString());
			e.printStackTrace(); 
		}
	}
	
	public boolean getBounced(String key) throws IOException{
		boolean stillBounced = false;
		if (bouncerProperties.containsKey(key)) {
			String bouncedFor = bouncerProperties.getProperty(key);
			if (bouncedFor != null) {
				long bouncedInMillis = Long.parseLong(bouncedFor);
				if (bouncedInMillis > System.currentTimeMillis()) {
					stillBounced = true;
				} else {//not bounced anymore
					setUnBounced(key);
					saveConfigFileProperties();
				}
			} else {// bounced forever...
				stillBounced = true;
			} 
		} //not bounced
		return stillBounced;
	}
	
	public boolean setBounced(String key, String value) throws IOException {
		Object isBounced = bouncerProperties.setProperty(key,value);
		if (isBounced == null) {
			return false;
		} else {
			saveConfigFileProperties();
			return true;
		}
	}
	
	public boolean setUnBounced(String key) throws IOException {
		if (bouncerProperties.containsKey(key)) {
			if (bouncerProperties.remove(key) != null) {
				saveConfigFileProperties();
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public void manageBouncerPropertyFiles() {
		try {
			cfg.getPropertiesFromFile(bouncerPropertiesFile, bouncerProperties);
			saveConfigFileProperties();
		} catch (IOException e) {
			msg.severe("Error reading/writing bouncer properties files.");
			e.printStackTrace();
		}
	}	

	public void saveConfigFileProperties() throws IOException{
		cfg.saveConfigFileProperties(bouncerPropertiesFile, bouncerProperties,
				"Use this file to set bounced players by name or ip address, entries will appear as follows: "
				+ "playerName=999999999 (bounced until system clock = 99999999), playerName= (empty value, bounced forever), "
				+ "1.1.1.1=99999999 (ip, and/or hostname, bounced until system clock = 99999999, "
				+ "1.1.1.1= (empty value, bounced by ip/hostname forever)");
	}

}
