package com.etoy.processing.sarcophagus;


import java.io.FileInputStream; 
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {
	
	public static char DIRECTORY_SEPARATOR = '/';
	
	protected static Config instance;
	protected Properties config;
	
	
	public Config() {
		setup();
	}
	
	
	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}
	
	
	protected void setConfig(Properties cfg) {
		config = cfg;
	}
	
	
	protected Properties getConfig() {
		return config;
	}
	
	
	public boolean setup() {
		FileInputStream cfgFile = getConfigFile();
		if (null == cfgFile) {
			return false;
		}
		
		Properties cfg = new Properties();
		try {
			cfg.loadFromXML(cfgFile);
			setConfig(cfg);
			return true;
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
		
		return false;
	}
	
	
	public FileInputStream getConfigFile() {
		String cfgfile = System.getProperty("user.home");
		cfgfile += "/Library/Preferences/Sarcophagus/properties.xml";
		try {
			return new FileInputStream(cfgfile);
		} catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	
	public String getProperty(String name) {
		return getProperty(name, null);
	}
	
	
	public String getProperty(String name, String def) {
		return getConfig().getProperty(name, def);
	}
	
	public String getPlaylist() {
		String plsfile = getProperty("playlistdir");
		plsfile+= DIRECTORY_SEPARATOR;
		plsfile+= getProperty("playlist");
		plsfile+= ".xml";
		return plsfile;
	}
	
	
	
}
