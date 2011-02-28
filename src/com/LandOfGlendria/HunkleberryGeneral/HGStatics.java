package com.LandOfGlendria.HunkleberryGeneral;

import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.World;

public class HGStatics {

	public static final String VERSION = "0.67";
	public static final Integer MAX_INVENTORY_ITEMS = Integer.valueOf(2304);
	public static final Integer MAX_MESSAGE_LENGTH = Integer.valueOf(58);
	public static final Integer MAX_DISTANCE = Integer.valueOf(300);
	public static final Integer MAX_HEALTH = Integer.valueOf(20);
	public static final String POSITIVE_COLOR = ChatColor.GREEN.toString();
	public static final String ERROR_COLOR = ChatColor.RED.toString();
	public static final String WARNING_COLOR = ChatColor.GOLD.toString();
	public static final String INFO_COLOR = ChatColor.BLUE.toString();
	public static final String COMMAND_COLOR = ChatColor.AQUA.toString();
	public static final String NO_COLOR = ChatColor.WHITE.toString();
	public static final String ARGUMENTS_COLOR = ChatColor.DARK_AQUA.toString();
	public static final char COLOR_IND = ChatColor.DARK_AQUA.toString().charAt(0);
	public static final String WORLD_DATA_FILE = "level.dat";
	public static final String ENV_EXTENSION = ".properties";
	public static final String NETHER_ENV_FILE = (World.Environment.NETHER.toString() + ENV_EXTENSION);;
	public static final String NORMAL_ENV_FILE = (World.Environment.NORMAL.toString() + ENV_EXTENSION);;
	public static final String PLUGIN_NAME = HunkleberryGeneral.pdfFile.getName();
	public static final String LOG_PREFIX = (new StringBuilder("[")).append(PLUGIN_NAME).append("]: ").toString();
	public static final String PLUGIN_PATH = (new StringBuilder("plugins/")).append(PLUGIN_NAME).append("/").toString();
	public static final String BRIEF = "brief";
	public static final String LONG = "long";
	public static final String HERE = "here";
	public static final String BLIND = "blind";
	public static final String NORMAL = "normal";
	public static final String NEAT = "neat";
	public static final String FOREVER = "forever";

	public static final byte[] SAFE_BLOCKS = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 35, 41, 42, 43, 44,
			45, 46, 47, 48, 49, 52, 53, 54, 56, 57, 58, 61, 62, 64, 67, 71, 74, 79, 80, 82, 84, 85, 86, 87, 88, 89, 91 };
	public static final byte[] DANGEROUS_BLOCKS = { 10, 11, 51, 81 };
	public static final byte[] INSUBSTATNIAL_BLOCKS = { 0, 37, 38, 39, 40, 50, 55, 56, 59, 60, 63, 65, 66, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 83,
			90, 92, 93, 94 };
	public static final HashSet<Byte> SafeBlocks = new HashSet<Byte>();
	public static final HashSet<Byte> DangerBlocks = new HashSet<Byte>();
	public static final HashSet<Byte> AirBlocks = new HashSet<Byte>();

	public static final String ALIAS_PROPERTIES = (new StringBuilder(String.valueOf(HGStatics.PLUGIN_PATH))).append("alias.properties").toString();
	public static final String ALLOW_PROPERTIES = (new StringBuilder(String.valueOf(HGStatics.PLUGIN_PATH))).append("allow.properties").toString();
	public static final String OPSONLY_PROPERTIES = (new StringBuilder(String.valueOf(HGStatics.PLUGIN_PATH))).append("opsOnly.properties").toString();
	public static final String PERMISSIONS_PROPERTIES = (new StringBuilder(String.valueOf(HGStatics.PLUGIN_PATH))).append("permissions.properties")
			.toString();
	public static final String ALIAS_MESSAGE = ("Use this file to set command aliases. Any alias completely overrides the default command, "
			+ "which becomes unavailable. Change/add only values, the keys must remain unchanged " + "or they will be overwritten.");
	public static final String ALLOW_MESSAGE = ("Use this file to allow/disallow commands on a server level. A value of false or and empty "
			+ "value will cause the command to not be recognized by the plugin for anyone. Keys "
			+ "missing from this file will use the default value of true, i.e., allowed. Delete "
			+ "the file to force regeneration. Use Permissions for greater control.");
	public static final String OPSONLY_MESSAGE = ("Use this file to set commands for ops use only.  Permissions may add command use privledges "
			+ "to other users, but cannot take away privledges from ops gained by these settings.");
	public static final String PERMISSIONS_MESSAGE = ("This file allows editing of the permissions string associated with each command. To reset a "
			+ "permissions string to the default value delete the entire line for the command, save " + "the file and reload the plugin.");
	public static final String BOUNCER_PROPERTIES = (new StringBuilder(String.valueOf(HGStatics.PLUGIN_PATH))).append("bouncer.properties").toString();
	public static final String BOUNCER_MESSAGE = ("Use this file to set bounced players by name, entries will appear as follows: " + HGStatics.NEW_LINE
			+ "#playerName=999999999 (bounced until system clock = 99999999) " + HGStatics.NEW_LINE + "#playerName= (empty value, bounced forever) "
			+ HGStatics.NEW_LINE + "#/1.1.1.1=99999999 (ip, and/or hostname, bounced until system clock = 99999999 " 
			+ HGStatics.NEW_LINE + "#/1.1.1.1= (empty value, bounced by ip/hostname forever)");

	public static String COMMAND_HTML_FILE = (new StringBuilder(PLUGIN_PATH)).append("commands.html").toString();
	public static String COMMAND_BUKKIT_FILE = (new StringBuilder(PLUGIN_PATH)).append("commands.bukkit").toString();
	public static String COMMAND_YAML_FILE = (new StringBuilder(PLUGIN_PATH)).append("plugin.yaml").toString();
	public static boolean OVERWRITE = true;

	public static String NEW_LINE = System.getProperty("line.separator");

	public static String MOTD_FILE = (new StringBuilder(PLUGIN_PATH)).append("MOTD.txt").toString();
	public static String MOTD_EOL = "§EOL§";
	public static String[] MOTD_SPECIAL_CHARS = { "§", "%" };
	public static String MOTD_STRING = null;

	public static String MOTD_AQUA = "aqua";
	public static String MOTD_BLACK = "black";
	public static String MOTD_BLUE = "blue";
	public static String MOTD_DARK_AQUA = "dark_aqua";
	public static String MOTD_DARK_BLUE = "dark_blue";
	public static String MOTD_DARK_GRAY = "dark_gray";
	public static String MOTD_DARK_GREEN = "dark_green";
	public static String MOTD_DARK_PURPLE = "dark_purple";
	public static String MOTD_DARK_RED = "dark_red";
	public static String MOTD_GOLD = "gold";
	public static String MOTD_GRAY = "gray";
	public static String MOTD_GREEN = "green";
	public static String MOTD_LIGHT_PURPLE = "light_purple";
	public static String MOTD_RED = "red";
	public static String MOTD_WHITE = "white";
	public static String MOTD_YELLOW = "yellow";
	public static String MOTD_NUMP = "num_players";
	public static String MOTD_MAXP = "max_players";
	public static String MOTD_PPL = "list_ppl";
	public static String MOTD_WORLDS = "list_worlds";
	public static String MOTD_NAME = "real_name";
	public static String MOTD_ALIAS = "alias_name";
	public static String MOTD_ADDR = "inter_addr";
	public static String MOTD_PORT = "port=";
	public static String MOTD_DATE = "day";
	public static String MOTD_HOLD = "in_hand=";

	static {
		for (int i = 0; i < SAFE_BLOCKS.length; i++) {
			SafeBlocks.add(SAFE_BLOCKS[i]);
		}
		for (int i = 0; i < DANGEROUS_BLOCKS.length; i++) {
			DangerBlocks.add(DANGEROUS_BLOCKS[i]);
		}
		for (int i = 0; i < INSUBSTATNIAL_BLOCKS.length; i++) {
			AirBlocks.add(INSUBSTATNIAL_BLOCKS[i]);
		}
	}

}
