package com.LandOfGlendria.HunkleberryGeneral;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import java.io.*;
import java.util.*;

import org.bukkit.plugin.Plugin;

public class HGConfig {

	private HunkleberryGeneral plugin;
	private Properties allowProperties;
	private Properties aliasProperties;
	private Properties opsOnlyProperties;
	private Properties permissionsProperties;
	public static PermissionHandler permissions;
	public HGMessageManagement msg;
	public HGCommandDAO commandDAO;
	public File aliasPropertiesFile;
	public File allowPropertiesFile;
	public File opsOnlyPropertiesFile;
	public File permissionsPropertiesFile;


	public HGConfig(HunkleberryGeneral plugin, HGMessageManagement msg, HGCommandDAO commandDAO) {
		this.plugin = plugin;
		this.msg = msg;
		this.commandDAO = commandDAO;
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
			e.printStackTrace(); 
		}
//		File jdbcjar = new File("./lib/sqlitejdbc-v056.jar");   
//		String lcStr = "org.sqlite.JDBC";   
//		URL jarfile;
//		Class loadedClass;
//		try {
//			jarfile = new URL("jar", "","file:" + jdbcjar.getAbsolutePath()+"!/");
//			URLClassLoader cl = URLClassLoader.newInstance(new URL[] {jarfile });   
//			loadedClass = cl.loadClass(lcStr);
//		    //Class.forName("org.sqlite.JDBC");
//
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (MalformedURLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} 
//		
//		try {
//			Connection conn =
//				DriverManager.getConnection("jdbc:sqlite:plugins/HunkleberryGeneral/HGDB.db");
//			Statement stat = conn.createStatement();
//
//
//		    ResultSet rs;
//				rs = stat.executeQuery("select * from location;");
//				// TODO Auto-generated catch block
//			
//				while (rs.next()) {
//					msg.info("name = " + rs.getString("player_name"));
//					msg.info("job = " + rs.getString("world_name"));
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//		    }


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
		try {
			setCurrentConfigFileProperties();
			getPropertiesFromFile(aliasPropertiesFile, aliasProperties,HGStatics.ALIAS_MESSAGE);
			getPropertiesFromFile(allowPropertiesFile, allowProperties,HGStatics.ALLOW_MESSAGE);
			getPropertiesFromFile(opsOnlyPropertiesFile, opsOnlyProperties,HGStatics.OPSONLY_MESSAGE);
			getPropertiesFromFile(permissionsPropertiesFile, permissionsProperties,HGStatics.PERMISSIONS_MESSAGE);
			applyConfigFileChanges();
			commandDAO.reloadLookup();
			saveConfigFileProperties();
		} catch (IOException e) {
			msg.severe("Error reading/writing properties files.");
			e.printStackTrace();
		}
	}
	
	public void reloadCommandsFromPropertyFiles() {
		aliasProperties.clear();
		allowProperties.clear();
		opsOnlyProperties.clear();
		permissionsProperties.clear();

		try {
			getPropertiesFromFile(aliasPropertiesFile, aliasProperties,HGStatics.ALIAS_MESSAGE);
			getPropertiesFromFile(allowPropertiesFile, allowProperties,HGStatics.ALLOW_MESSAGE);
			getPropertiesFromFile(opsOnlyPropertiesFile, opsOnlyProperties,HGStatics.OPSONLY_MESSAGE);
			getPropertiesFromFile(permissionsPropertiesFile, permissionsProperties,HGStatics.PERMISSIONS_MESSAGE);
			applyConfigFileChanges();
			commandDAO.reloadLookup();
			setCurrentConfigFileProperties();
			saveConfigFileProperties();
		} catch (IOException e) {
			msg.severe("Error reading/writing properties files.");
			e.printStackTrace();
		}
	}

	public void setCurrentConfigFileProperties() {
		for (HGCommand command : HGCommand.values()) {
			aliasProperties.setProperty(commandDAO.getDefaultCommand(command), commandDAO.getCommandAlias(command));
			allowProperties.setProperty(commandDAO.getDefaultCommand(command), Boolean.toString(commandDAO.getServerAllowed(command).booleanValue()));
			opsOnlyProperties.setProperty(commandDAO.getDefaultCommand(command), Boolean.toString(commandDAO.getOpsOnly(command).booleanValue()));
			permissionsProperties.setProperty(commandDAO.getDefaultCommand(command), commandDAO.getPermissions(command));
		}
	}

	public void getPropertiesFromFile(File propertiesFile, Properties properties,String message) throws IOException {
		if (propertiesFile.exists()) {
			FileInputStream fileReader = new FileInputStream(propertiesFile);
			try {
				properties.load(fileReader);
			} finally {
				fileReader.close();
			}
		} else {
			saveConfigFileProperties(propertiesFile,properties,message);
		}
	}

	private void applyConfigFileChanges() {
		for (HGCommand command : HGCommand.values()) {
			commandDAO.setCommandAlias(command,aliasProperties.getProperty(commandDAO.getDefaultCommand(command)));
//------------IFNDEF 440
/**
			if (command.getCommandAlias() != null && !command.getCommandAlias().isEmpty()) {
				//++++++++++++++++++++
				plugin.setCommandAlias(command.getDefaultCommand(),command.getCommandAlias());
			}
*/
//------------ENDIFNDEF 440
			commandDAO.setServerAllowed(command,Boolean.valueOf(Boolean.parseBoolean(allowProperties.getProperty(commandDAO.getDefaultCommand(command)))));
			commandDAO.setOpsOnly(command,Boolean.valueOf(Boolean.parseBoolean(opsOnlyProperties.getProperty(commandDAO.getDefaultCommand(command)))));
			commandDAO.setPermissions(command,permissionsProperties.getProperty(commandDAO.getDefaultCommand(command)));
		}
	}

	public void saveConfigFileProperties() throws IOException{
		saveConfigFileProperties(aliasPropertiesFile, aliasProperties,HGStatics.ALIAS_MESSAGE);
		saveConfigFileProperties(allowPropertiesFile, allowProperties,HGStatics.ALLOW_MESSAGE);
		saveConfigFileProperties(opsOnlyPropertiesFile, opsOnlyProperties,HGStatics.OPSONLY_MESSAGE);
		saveConfigFileProperties(permissionsPropertiesFile, permissionsProperties,HGStatics.PERMISSIONS_MESSAGE);
	}

	public void saveConfigFileProperties(File propertiesFile, Properties properties, String message) throws IOException {
		FileOutputStream fileWriter = new FileOutputStream(propertiesFile);
		try {
			properties.store(fileWriter, message);
			fileWriter.close();
		} finally {
			fileWriter.close();
		}
	}


	public String writeCommandsToHtmlSimple() {
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<body font-type=sans font-size:small>");
		sb.append("<table border=0>");
		sb.append(HGStatics.NEW_LINE);
		for (HGCommand command : HGCommand.values()) {
			sb.append("<tr><td colspan=2 bgcolor=ffffff>&nbsp;</td></tr>");
			sb.append("<tr><td bgcolor=ffffff>/");
			sb.append(commandDAO.getCommand(command));
			sb.append("</td><td bgcolor=f0f0f0>");
			sb.append(commandDAO.getCommandArgs(command));
			sb.append("</td></tr><tr><td align=center>");
			sb.append("</td><td valign=top align=left bgcolor=c0c0c0>");
			sb.append(commandDAO.getCommandUsage(command));
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
		for (HGCommand command : HGCommand.values()) {
			sb.append("[INDENT=1][FONT=helvetica][SIZE=3][COLOR=rgb(51, 102, 255)]/");
			sb.append(commandDAO.getCommand(command));
			sb.append("[/COLOR][/SIZE][/FONT][SIZE=3]    [FONT=helvetica][COLOR=rgb(51, 153, 102)]");
			sb.append(commandDAO.getCommandArgs(command));
			sb.append("[/COLOR][/FONT][/SIZE][/INDENT]");
			sb.append(HGStatics.NEW_LINE);		
			sb.append("[INDENT=2][FONT=helvetica][SIZE=3][COLOR=rgb(0, 0, 0)]");
			sb.append(commandDAO.getCommandUsage(command));
			sb.append("[/COLOR][/SIZE][/FONT][/INDENT]");
			sb.append(HGStatics.NEW_LINE);		
			sb.append("[INDENT=1][/INDENT]");
			sb.append(HGStatics.NEW_LINE);
		}
		return sb.toString();
	}

	public String writePluginYaml() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("name: HunkleberryGeneral");
		sb.append(HGStatics.NEW_LINE);
		sb.append(HGStatics.NEW_LINE);
		sb.append("main: com.LandOfGlendria.HunkleberryGeneral.HunkleberryGeneral");
		sb.append(HGStatics.NEW_LINE);
		sb.append(HGStatics.NEW_LINE);
		sb.append("version: ");
		sb.append(HGStatics.VERSION);
		sb.append(HGStatics.NEW_LINE);
		sb.append(HGStatics.NEW_LINE);
		sb.append("website: TBD");
		sb.append(HGStatics.NEW_LINE);
		sb.append(HGStatics.NEW_LINE);
		sb.append("description: Friendly general command plugin.");
		sb.append(HGStatics.NEW_LINE);
		sb.append(HGStatics.NEW_LINE);
		sb.append("author: Glen McNeilley");
		sb.append(HGStatics.NEW_LINE);
		sb.append(HGStatics.NEW_LINE);				
		sb.append("commands:");
		sb.append(HGStatics.NEW_LINE);
		sb.append(HGStatics.NEW_LINE);				
		for (HGCommand command : HGCommand.values()) {
			sb.append(" ");				
			sb.append(commandDAO.getDefaultCommand(command));
			sb.append(":");				
			sb.append(HGStatics.NEW_LINE);
			sb.append("  description: ");				
			//sb.append(command.getCommandUsage());
			sb.append(HGStatics.NEW_LINE);
			sb.append("  usage: ");				
			//sb.append(command.getCommandArgs());
			sb.append(HGStatics.NEW_LINE);
			//sb.append("  aliases: [");
			//sb.append(command.getCommandAlias());
			//sb.append("]");
			//sb.append(HGStatics.NEW_LINE);
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
