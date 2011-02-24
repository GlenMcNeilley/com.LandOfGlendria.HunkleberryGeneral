package com.LandOfGlendria.HunkleberryGeneral;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import org.bukkit.plugin.Plugin;

public class HGConfig {

	private final Logger log = Logger.getLogger("Minecraft");
	private Plugin plugin;
	private Properties allowProperties;
	private Properties aliasProperties;
	private Properties opsOnlyProperties;
	private Properties permissionsProperties;
	public static PermissionHandler permissions;
	public HGMessageManagement msg;
	public HGCommandData commands;

	public HGConfig(Plugin plugin, HGMessageManagement msg) {
		this.plugin = plugin;
		this.msg = msg;
		allowProperties = new Properties();
		aliasProperties = new Properties();
		opsOnlyProperties = new Properties();
		permissionsProperties = new Properties();
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
		} catch (Exception e) {
			msg.severe((new StringBuilder("Caught ")).append(e.getClass().getName()).append(" in HGConfig().").toString());
			msg.info(e.toString());
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
		File aliasPropertiesFile = new File((new StringBuilder(String.valueOf(HGStatics.PLUGIN_PATH))).append("alias.properties").toString());
		File allowPropertiesFile = new File((new StringBuilder(String.valueOf(HGStatics.PLUGIN_PATH))).append("allow.properties").toString());
		File opsOnlyPropertiesFile = new File((new StringBuilder(String.valueOf(HGStatics.PLUGIN_PATH))).append("opsOnly.properties").toString());
		File permissionsPropertiesFile = new File((new StringBuilder(String.valueOf(HGStatics.PLUGIN_PATH))).append("permissions.properties").toString());
		if (!getPropertiesFromFile(aliasPropertiesFile, aliasProperties)) {
			setCurrentAliasConfigFileProperties();
			saveConfigFileProperties(
					aliasPropertiesFile,
					aliasProperties,
					"Use this file to set command aliases. Any alias completely overrides the default command, which becomes unavailable. Change/add only values, the keys must remain unchanged or they will be overwritten.");
		} else {
			applyAliasPropertyFileChanges();
		}
		if (!getPropertiesFromFile(allowPropertiesFile, allowProperties)) {
			setCurrentAllowConfigFileProperties();
			saveConfigFileProperties(
					allowPropertiesFile,
					allowProperties,
					"Use this file to allow/disallow commands on a server level. A value of false or and empty value will cause the command to not be recognized by the plugin for anyone. Keys missing from this file will use the default value of true, i.e., allowed. Delete the file to force regeneration. Use Permissions for greater control.");
		} else {
			applyAllowPropertyFileChanges();
		}
		if (!getPropertiesFromFile(opsOnlyPropertiesFile, opsOnlyProperties)) {
			setCurrentOpsOnlyConfigFileProperties();
			saveConfigFileProperties(
					opsOnlyPropertiesFile,
					opsOnlyProperties,
					"Use this file to set commands for ops use only.  Permissions may add command use privledges to other users, but cannot take away privledges from ops gained by these settings.");
		} else {
			applyOpsOnlyPropertyFileChanges();
		}
		if (!getPropertiesFromFile(permissionsPropertiesFile, permissionsProperties)) {
			setCurrentPermissionsConfigFileProperties();
			saveConfigFileProperties(permissionsPropertiesFile, permissionsProperties,
					"This file allows editing of the permissions string associated with each command");
		} else {
			applyPermissionsPropertyFileChanges();
		}
	}

	private void applyAliasPropertyFileChanges() {
		for (Iterator<Object> iterator = aliasProperties.keySet().iterator(); iterator.hasNext();) {
			Object key = iterator.next();
			HGCommandData command = HGCommandData.getCommandDataByName((String) key);
			if (command != null) {
				command.setCommandAlias(aliasProperties.getProperty((String) key));
			}
		}

	}

	private void applyAllowPropertyFileChanges() {
		for (Iterator<Object> iterator = allowProperties.keySet().iterator(); iterator.hasNext();) {
			Object key = iterator.next();
			HGCommandData command = HGCommandData.getCommandDataByName((String) key);
			if (command != null) {
				command.setServerAllowed(Boolean.valueOf(Boolean.parseBoolean(allowProperties.getProperty((String) key))));
			}
		}

	}

	private void applyOpsOnlyPropertyFileChanges() {
		for (Iterator<Object> iterator = opsOnlyProperties.keySet().iterator(); iterator.hasNext();) {
			Object key = iterator.next();
			HGCommandData command = HGCommandData.getCommandDataByName((String) key);
			if (command != null) {
				command.setOpsOnly(Boolean.valueOf(Boolean.parseBoolean(opsOnlyProperties.getProperty((String) key))));
			}
		}

	}

	private void applyPermissionsPropertyFileChanges() {
		for (Iterator<Object> iterator = permissionsProperties.keySet().iterator(); iterator.hasNext();) {
			Object key = iterator.next();
			HGCommandData command = HGCommandData.getCommandDataByName((String) key);
			if (command != null) {
				command.setPermissions(permissionsProperties.getProperty((String) key));
			}
		}

	}

	public void setCurrentAliasConfigFileProperties() {
		HGCommandData ahgcommanddata[];
		int j = (ahgcommanddata = HGCommandData.values()).length;
		for (int i = 0; i < j; i++) {
			HGCommandData command = ahgcommanddata[i];
			aliasProperties.setProperty(command.getDefaultCommand(), command.getCommandAlias());
		}

	}

	public void setCurrentAllowConfigFileProperties() {
		HGCommandData ahgcommanddata[];
		int j = (ahgcommanddata = HGCommandData.values()).length;
		for (int i = 0; i < j; i++) {
			HGCommandData command = ahgcommanddata[i];
			allowProperties.setProperty(command.getDefaultCommand(), Boolean.toString(command.getServerAllowed().booleanValue()));
		}

	}

	public void setCurrentOpsOnlyConfigFileProperties() {
		HGCommandData ahgcommanddata[];
		int j = (ahgcommanddata = HGCommandData.values()).length;
		for (int i = 0; i < j; i++) {
			HGCommandData command = ahgcommanddata[i];
			opsOnlyProperties.setProperty(command.getDefaultCommand(), Boolean.toString(command.getOpsOnly().booleanValue()));
		}

	}

	public void setCurrentPermissionsConfigFileProperties() {
		HGCommandData ahgcommanddata[];
		int j = (ahgcommanddata = HGCommandData.values()).length;
		for (int i = 0; i < j; i++) {
			HGCommandData command = ahgcommanddata[i];
			permissionsProperties.setProperty(command.getDefaultCommand(), command.getPermissions());
		}

	}

	private boolean getPropertiesFromFile(File propertiesFile, Properties properties) {
		if (propertiesFile.exists()) {
			try {
				FileInputStream fileReader = new FileInputStream(propertiesFile);
				properties.load(fileReader);
				fileReader.close();
				log.info((new StringBuilder(String.valueOf(HGStatics.PLUGIN_NAME))).append(": Read ").append(HGStatics.PLUGIN_PATH)
						.append(propertiesFile.getName()).append(".").toString());
			} catch (IOException e) {
				log.severe((new StringBuilder(String.valueOf(HGStatics.PLUGIN_NAME))).append(": Unable to load ").append(HGStatics.PLUGIN_PATH)
						.append(propertiesFile.getName()).append(".").toString());
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	public void saveConfigFileProperties(File file, Properties properties, String message) {
		try {
			FileOutputStream fileWriter = new FileOutputStream(file);
			properties.store(fileWriter, message);
			fileWriter.close();
			log.info((new StringBuilder(String.valueOf(HGStatics.PLUGIN_NAME))).append(": Wrote default config properties to ").append(file.getName())
					.append(".").toString());
		} catch (IOException e) {
			log.severe((new StringBuilder(String.valueOf(HGStatics.PLUGIN_NAME))).append(": Unable to save properties to ").append(file.getName())
					.append(".").toString());
			e.printStackTrace();
		}
	}

	public String saveConfigFileProperties() {
		setCurrentAliasConfigFileProperties();
		setCurrentAllowConfigFileProperties();
		setCurrentOpsOnlyConfigFileProperties();
		setCurrentPermissionsConfigFileProperties();
		return null;
	}

	public String writeCommandsToHtml() {
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<link href=\"hgcommands.css\" rel=\"stylesheet\" type=\"text/css\" />");
		sb.append("</head>");
		sb.append("<body font-type=sans font-size:small>");
		sb.append("<table border=0>");
		for (HGCommandData command : HGCommandData.values()) {
			sb.append("<tr><td colspan=2>&nbsp;</td></tr><tr><td colspan=2 class=command>/");
			sb.append(command.getCommand());
			sb.append("<span class=arguments>&nbsp;&nbsp;&nbsp;&nbsp;");
			sb.append(command.getCommandArgs());
			sb.append("</span></td></tr>");
			sb.append("<tr><td align=center>");
			if (command.getServerAllowed().booleanValue()) {
				sb.append("<span class=enabled>Server Enabled");
			} else {
				sb.append("<span class=disabled>Server Disabled");
			}
			sb.append("</span></td><td rowspan=3 valign=top align=left><span class=usage><span class=usagedef>");
			sb.append("Usage:");
			sb.append("</span> ");
			sb.append(command.getCommandUsage());
			sb.append("</span></td></tr>");
			sb.append("<tr><td align=center>");
			if (command.getOpsOnly().booleanValue()) {
				sb.append("<span class=enabled>Everyone");
			} else {
				sb.append("<span class=disabled>Ops Only");
			}
			sb.append("</span></td></tr>");
			sb.append("<tr><td align=center>");
			sb.append(command.getPermissions());
			sb.append("</td></tr>");
		}

		sb.append("</table>");
		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}

	public String writeCommandsToHtmlSimple() {
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<body font-type=sans font-size:small>");
		sb.append("<table border=0>");

		for (HGCommandData command : HGCommandData.values()) {
			sb.append("<tr><td colspan=2 bgcolor=ffffff>&nbsp;</td></tr>");
			sb.append("<tr><td bgcolor=ffffff>/");
			sb.append(command.getCommand());
			sb.append("</td><td bgcolor=f0f0f0>");
			sb.append(command.getCommandArgs());
			sb.append("</td></tr><tr><td align=center>");
			sb.append("</td><td valign=top align=left bgcolor=c0c0c0>");
			sb.append("Usage: ");
			sb.append(command.getCommandUsage());
			sb.append("</td></tr>");

		}

		sb.append("</table>");
		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}

	public String writeCommandsToBukkit() {
		StringBuffer sb = new StringBuffer();
		for (HGCommandData command : HGCommandData.values()) {
			sb.append("[LIST]\n");
			sb.append("[*][FONT=helvetica][SIZE=3][COLOR=rgb(51, 102, 255)]/");
			sb.append(command.getCommand());
			sb.append("[/COLOR][/SIZE][/FONT][SIZE=3]    [FONT=helvetica][COLOR=rgb(51, 153, 102)]");
			sb.append(command.getCommandArgs());
			sb.append("[/COLOR][/FONT][/SIZE]\n");
			sb.append("[INDENT=1][FONT=helvetica][SIZE=3][COLOR=rgb(0, 0, 0)]    Usage: ");
			sb.append(command.getCommandUsage());
			sb.append("[/COLOR][/SIZE][/FONT][/INDENT]\n");
			sb.append("[/LIST]\n");
		}
		return sb.toString();
	}
}
