package com.LandOfGlendria.HunkleberryGeneral;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import org.bukkit.plugin.Plugin;


public class HGBouncer {
 
	private Properties bouncerProperties;
	public HGMessageManagement msg;
	public HGConfig cfg;
	public File bouncerPropertiesFile;
		
	public HGBouncer(Plugin plugin, HGMessageManagement msg, HGConfig cfg) {
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
	
	public String getReturnTime(String bouncee) {
		String message;
		if (bouncerProperties.containsKey(bouncee)) {
			if (bouncerProperties.getProperty(bouncee) != null && !bouncerProperties.getProperty(bouncee).isEmpty()) {
				try {
					long logintime = Long.parseLong(bouncerProperties.getProperty(bouncee));
					message = new String("You are not allowed to log in until " + new Date(logintime).toString());
				} catch (NumberFormatException e) {
					message = new String("Who knows when you may log in again.");
				}
			} else {
				message = new String("You are banned for life.");
			}
		} else {
			message = new String("You aren't actually bounced...");
		}
		return message;
	}
	
	public boolean getBounced(String key) throws IOException{
		boolean stillBounced = false;
		if (bouncerProperties.containsKey(key)) {
			String bouncedFor = bouncerProperties.getProperty(key);
			if (bouncedFor != null && !bouncedFor.isEmpty()) {
				long bouncedInMillis = 0;
				try {
					bouncedInMillis = Long.parseLong(bouncedFor);
				} catch (NumberFormatException e) {
					stillBounced = true;
				}
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
	
	public void setBounced(String key, String value) throws IOException {
		bouncerProperties.setProperty(key,value);
		saveConfigFileProperties();
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
				"Use this file to set bounced players by name, entries will appear as follows: " + HGStatics.NEW_LINE
				+ "#playerName=999999999 (bounced until system clock = 99999999) " + HGStatics.NEW_LINE
				+ "#playerName= (empty value, bounced forever) " + HGStatics.NEW_LINE
				+ "#/1.1.1.1=99999999 (ip, and/or hostname, bounced until system clock = 99999999 " + HGStatics.NEW_LINE
				+ "#/1.1.1.1= (empty value, bounced by ip/hostname forever)"); 
	}
	
	public ArrayList<String> getFormattedArray() { //syncronize because of this?
		ArrayList<String>  bounced = new ArrayList<String>();
		@SuppressWarnings("rawtypes")
		Enumeration bouncedNames = bouncerProperties.keys();
		while (bouncedNames.hasMoreElements()) {
			String name = (String)bouncedNames.nextElement();
			String value;
			if (bouncerProperties.getProperty(name) != null && !bouncerProperties.getProperty(name).isEmpty()) {
				try {
					value = "|" + new Date(Long.parseLong(bouncerProperties.getProperty(name))).toString();
				} catch (Exception e) {
					value = new String("|unknown");
				}
			} else {
				value = new String("|forever");
			}
			bounced.add(name + " " + value);
		}
		return bounced;
	}
}
