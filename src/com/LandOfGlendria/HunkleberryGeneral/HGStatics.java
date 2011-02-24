package com.LandOfGlendria.HunkleberryGeneral;

import java.util.HashSet;

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
	public static final byte[] SAFE_BLOCKS = {1,2,3,4,5,6,7,8,9,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,35,41,42,43,44,45,46,47,48,49,52,53,54,56,57,58,61,62,64,67,71,74,79,80,82,84,85,86,87,88,89,91};
	public static final byte[] DANGEROUS_BLOCKS = {10,11,51,81};
	public static final byte[] INSUBSTATNIAL_BLOCKS = {0,37,38,39,40,50,55,56,59,60,63,65,66,68,69,70,71,72,73,74,75,76,77,78,83,90,92,93,94};
	public static final HashSet<Byte> SafeBlocks = new HashSet<Byte>();
	public static final HashSet<Byte> DangerBlocks = new HashSet<Byte>();
	public static final HashSet<Byte> AirBlocks = new HashSet<Byte>();
	
    static {
        for (int i = 0 ; i < SAFE_BLOCKS.length; i++) {
            SafeBlocks.add(SAFE_BLOCKS[i]);
        }
        for (int i = 0 ; i < DANGEROUS_BLOCKS.length; i++) {
            DangerBlocks.add(DANGEROUS_BLOCKS[i]);
        }
        for (int i = 0 ; i < INSUBSTATNIAL_BLOCKS.length; i++) {
            AirBlocks.add(INSUBSTATNIAL_BLOCKS[i]);
        }
    }

}

