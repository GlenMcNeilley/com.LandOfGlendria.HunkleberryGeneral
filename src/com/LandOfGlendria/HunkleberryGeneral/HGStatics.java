package com.LandOfGlendria.HunkleberryGeneral;

import org.bukkit.ChatColor;
import org.bukkit.World;

public class HGStatics {

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
	public static final String ALIAS_FILE = "alias.properties";
	public static final String ALLOW_FILE = "allow.properties";
	public static final String PERMISSIONS_FILE = "permissions.properties";
	public static final String OPSONLY_FILE = "opsOnly.properties";
	public static final String WORLD_DATA_FILE = "level.dat";
	public static final String ENV_EXTENSION = ".properties";
	public static final String NETHER_ENV_FILE = (World.Environment.NETHER.toString() + ENV_EXTENSION);;
	public static final String NORMAL_ENV_FILE = (World.Environment.NORMAL.toString() + ENV_EXTENSION);;
	public static final String PLUGIN_NAME = HunkleberryGeneral.pdfFile.getName();
	public static final String LOG_PREFIX = (new StringBuilder("[")).append(PLUGIN_NAME).append("]: ").toString();
	public static final String PLUGIN_PATH = (new StringBuilder("plugins/")).append(PLUGIN_NAME).append("/").toString();
	public static final String BLIND = "blind";
	public static final String NORMAL = "normal";
	public static final String NEAT = "neat";

}
