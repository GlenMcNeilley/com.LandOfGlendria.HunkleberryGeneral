package com.LandOfGlendria.HunkleberryGeneral;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import java.io.*;
import java.util.*;
import org.bukkit.plugin.Plugin;

public class HGConfig {

	private Plugin plugin;
	private Properties allowProperties;
	private Properties aliasProperties;
	private Properties opsOnlyProperties;
	private Properties permissionsProperties;
	public static PermissionHandler permissions;
	public HGMessageManagement msg;
	public HGCommandData commands;
	public File aliasPropertiesFile;// = new File(HGStatics.ALIAS_PROPERTIES);
	public File allowPropertiesFile;// = new File(HGStatics.ALLOW_PROPERTIES);
	public File opsOnlyPropertiesFile;// = new File(HGStatics.OPSONLY_PROPERTIES);
	public File permissionsPropertiesFile;// = new File(HGStatics.PERMISSIONS_PROPERTIES);


	public HGConfig(Plugin plugin, HGMessageManagement msg) {
		this.plugin = plugin;
		this.msg = msg;
		allowProperties = new Properties();
		aliasProperties = new Properties();
		opsOnlyProperties = new Properties();
		permissionsProperties = new Properties();
		aliasPropertiesFile = new File(HGStatics.ALIAS_PROPERTIES);
		allowPropertiesFile = new File(HGStatics.ALLOW_PROPERTIES);
		opsOnlyPropertiesFile = new File(HGStatics.OPSONLY_PROPERTIES);
		permissionsPropertiesFile = new File(HGStatics.PERMISSIONS_PROPERTIES);

		File file = new File(HGStatics.PLUGIN_PATH);
		try {
			if (!file.exists()) {
				if (file.mkdir()) {
					msg.info((new StringBuilder("Created config directory ")).append(HGStatics.PLUGIN_PATH).toString());
				} else {
					msg.warn((new StringBuilder("Unable to create config directory ")).append(HGStatics.PLUGIN_NAME)
							.append(".  Please check file system permissions.").toString());
				}
			}
			setupPermissions();
			managePropertyFiles();
			readMotd();

		} catch (Exception e) {
			msg.severe((new StringBuilder("Caught ")).append(e.getClass().getName()).append(" in HGConfig().").toString());
			//msg.info(
					e.printStackTrace(); //);
		}
	}

	public void setupPermissions() {
		Plugin test = plugin.getServer().getPluginManager().getPlugin("Permissions");
		if (permissions == null) {
			if (test != null) {
				permissions = ((Permissions) test).getHandler();
				msg.info("Found Permissions, using it for permissions.");
			} else {
				msg.info("Permissions not found. Only ops may use commands.");
			}
		}
	}

	public void managePropertyFiles() {
		setCurrentConfigFileProperties();

		if (getPropertiesFromFile(aliasPropertiesFile, aliasProperties)) {
			applyAliasPropertyFileChanges();
		}
		if (getPropertiesFromFile(allowPropertiesFile, allowProperties)) {
			applyAllowPropertyFileChanges();
		}
		if (getPropertiesFromFile(opsOnlyPropertiesFile, opsOnlyProperties)) {
			applyOpsOnlyPropertyFileChanges();
		}
		if (getPropertiesFromFile(permissionsPropertiesFile, permissionsProperties)) {
			applyPermissionsPropertyFileChanges();
		}
		HGCommandData.reloadLookup();
		saveConfigFileProperties();
	}
	
	public void setCurrentConfigFileProperties() {
		for (HGCommandData command : HGCommandData.values()) {
			aliasProperties.setProperty(command.getDefaultCommand(), command.getCommandAlias());
			allowProperties.setProperty(command.getDefaultCommand(), Boolean.toString(command.getServerAllowed().booleanValue()));
			opsOnlyProperties.setProperty(command.getDefaultCommand(), Boolean.toString(command.getOpsOnly().booleanValue()));
			permissionsProperties.setProperty(command.getDefaultCommand(), command.getPermissions());
		}
	}

	private boolean getPropertiesFromFile(File propertiesFile, Properties properties) {
		try {
			if (propertiesFile.exists()) {
				FileInputStream fileReader = new FileInputStream(propertiesFile);
				try {
					properties.load(fileReader);
				} catch (IOException e) {
					msg.severe("Unable to load " + propertiesFile.toString());
					e.printStackTrace();
				} finally {
					fileReader.close();
				}
			} else {
				return false;
			}
			return true;
		} catch (IOException e) {
			msg.severe("Failed to read " + propertiesFile.toString());
			return false;
		}
	}

	private void applyAliasPropertyFileChanges() {
		for (Enumeration<Object> e = aliasProperties.keys(); e.hasMoreElements(); /**/) {
			String key = (String) e.nextElement();
			HGCommandData command = HGCommandData.getCommandDataByName(key);
			if (command != null) {
				command.setCommandAlias(aliasProperties.getProperty((String) key));
			} else {
				allowProperties.remove((String) key);
			}
		}
	}

	private void applyAllowPropertyFileChanges() {

		for (Enumeration<Object> e = allowProperties.keys(); e.hasMoreElements(); /**/) {
			String key = (String) e.nextElement();
			HGCommandData command = HGCommandData.getCommandDataByName(key);
			if (command != null) {
				command.setServerAllowed(Boolean.valueOf(Boolean.parseBoolean(allowProperties.getProperty((String) key))));
			} else {
				allowProperties.remove((String) key);
			}
		}
	}

	private void applyOpsOnlyPropertyFileChanges() {

		for (Enumeration<Object> e = opsOnlyProperties.keys(); e.hasMoreElements(); /**/) {
			String key = (String) e.nextElement();
			HGCommandData command = HGCommandData.getCommandDataByName(key);
			if (command != null) {
				command.setOpsOnly(Boolean.valueOf(Boolean.parseBoolean(opsOnlyProperties.getProperty((String) key))));
			} else {
				opsOnlyProperties.remove((String) key);
			}
		}
	}
	
	private void applyPermissionsPropertyFileChanges() {
		for (Enumeration<Object> e = permissionsProperties.keys(); e.hasMoreElements(); /**/) {
			String key = (String) e.nextElement();
			HGCommandData command = HGCommandData.getCommandDataByName(key);
			if (command != null) {
				command.setPermissions(permissionsProperties.getProperty((String) key));
			} else {
				permissionsProperties.remove((String)key);
			}
		}
	}

	public void saveConfigFileProperties() {
		saveConfigFileProperties(aliasPropertiesFile, aliasProperties,
				"Use this file to set command aliases. Any alias completely overrides the default command, "
						+ "which becomes unavailable. Change/add only values, the keys must remain unchanged " 
						+ "or they will be overwritten.");
		saveConfigFileProperties(allowPropertiesFile, allowProperties,
				"Use this file to allow/disallow commands on a server level. A value of false or and empty "
						+ "value will cause the command to not be recognized by the plugin for anyone. Keys "
						+ "missing from this file will use the default value of true, i.e., allowed. Delete "
						+ "the file to force regeneration. Use Permissions for greater control.");
		saveConfigFileProperties(opsOnlyPropertiesFile, opsOnlyProperties,
				"Use this file to set commands for ops use only.  Permissions may add command use privledges "
						+ "to other users, but cannot take away privledges from ops gained by these settings.");
		saveConfigFileProperties(permissionsPropertiesFile, permissionsProperties,
				"This file allows editing of the permissions string associated with each command. To reset a " 
						+ "permissions string to the default value delete the entire line for the command, save "
						+ "the file and reload the plugin.");
	}
	
	public void saveConfigFileProperties(File propertiesFile, Properties properties, String message) {
		try {
			FileOutputStream fileWriter = new FileOutputStream(propertiesFile);
			try {
				properties.store(fileWriter, message);
				fileWriter.close();
			} catch (IOException e) {
				msg.severe("Unable to save " + propertiesFile.toString());
				e.printStackTrace();
			} finally {
				fileWriter.close();
			}
		} catch (IOException e) {
			msg.severe("Failed to write " + propertiesFile.toString());
		}
	}

	public String writeCommandsToHtmlSimple() {
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<body font-type=sans font-size:small>");
		sb.append("<table border=0>");
		sb.append(HGStatics.NEW_LINE);
		for (HGCommandData command : HGCommandData.values()) {
			sb.append("<tr><td colspan=2 bgcolor=ffffff>&nbsp;</td></tr>");
			sb.append("<tr><td bgcolor=ffffff>/");
			sb.append(command.getCommand());
			sb.append("</td><td bgcolor=f0f0f0>");
			sb.append(command.getCommandArgs());
			sb.append("</td></tr><tr><td align=center>");
			sb.append("</td><td valign=top align=left bgcolor=c0c0c0>");
			sb.append(command.getCommandUsage());
			sb.append("</td></tr>");
			sb.append(HGStatics.NEW_LINE);
		}
		sb.append("</table>");
		sb.append("</body>");
		sb.append("</html>");
		sb.append(HGStatics.NEW_LINE);
		return sb.toString();
	}

	public String writeCommandsToBukkit() {
		StringBuffer sb = new StringBuffer();
		for (HGCommandData command : HGCommandData.values()) {
			sb.append("[INDENT=1][FONT=helvetica][SIZE=3][COLOR=rgb(51, 102, 255)]/");
			sb.append(command.getCommand());
			sb.append("[/COLOR][/SIZE][/FONT][SIZE=3]    [FONT=helvetica][COLOR=rgb(51, 153, 102)]");
			sb.append(command.getCommandArgs());
			sb.append("[/COLOR][/FONT][/SIZE][/INDENT]");
			sb.append(HGStatics.NEW_LINE);		
			sb.append("[INDENT=2][FONT=helvetica][SIZE=3][COLOR=rgb(0, 0, 0)]");
			sb.append(command.getCommandUsage());
			sb.append("[/COLOR][/SIZE][/FONT][/INDENT]");
			sb.append(HGStatics.NEW_LINE);		
			sb.append("[INDENT=1][/INDENT]");
			sb.append(HGStatics.NEW_LINE);
		}
		return sb.toString();
	}

	public void readMotd() {
		StringBuffer sb = new StringBuffer();
		Scanner scanner = null;
		try {
			scanner = new Scanner(new FileReader(HGStatics.MOTD_FILE));
			while (scanner.hasNextLine()) {
				String scanned = scanner.nextLine();
				if (!scanned.startsWith("#") && !scanned.startsWith("//")) {
					sb.append(scanned);
					sb.append(HGStatics.MOTD_EOL);
				}
			}
		} catch (FileNotFoundException e) {
			msg.warn("Unable to find MOTD: " + HGStatics.MOTD_FILE);
		} finally {
			if (scanner != null)
				scanner.close();
		}
		HGStatics.MOTD_STRING = sb.toString();
	}

	public String writeFile(String fileName, String data, boolean overwrite) throws IOException {
		File file = new File(fileName);

		if (!overwrite && file.exists()) {
			msg.severe("Attempted to write to file " + file.toString() + ", which should not be overwritten.");
			return ("Unable to write file. File already exists.");
		}
		if (!file.exists()) {
			file.createNewFile();
		}
		if (!file.isFile()) {
			msg.severe("Attempt to write file without file name.");
			return ("File name required.");
		}
		if (!file.canWrite()) {
			msg.severe("Attempt to write file without permission.");
			return ("Permission to write file denied.");
		}
		Writer output = new BufferedWriter(new FileWriter(file));
		try {
			output.write(data);
		} finally {
			output.close();
		}
		return null;
	}
}
