package com.LandOfGlendria.HunkleberryGeneral;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public enum HGCommandData {
	GET_ITEM				("get",				"hg.inv.get",			true,	"",	true,	"<itemId> [quantity:default=1] [itemData:default=0]",				"Add a quantity of items to your inventory.  The optional itemData affects wood and wool types as well as item durability."),
	GIVE_ITEM				("give",			"hg.inv.give",			true,	"",	true,	"<playerName> <itemId> [quantity:default=1] [itemData:default=0]",	"Attempts to find a player by name and add quantity of items to their inventory.  The optional itemData affects wood and wool types as well as item durability."),
	GET_TARGETED_BLOCK		("getblock",		"hg.inv.get",			true,	"",	true,	"[quantity:default=1] [itemData:default=0]",						"Attempts to determine what material is under your crosshair and add a quantity of this material to your inventory.  The optional itemData will override the targeted material itemData, affecting wood and wool types as well as item durability."),
	GIVE_TARGETED_BLOCK		("giveblock",		"hg.inv.give",			true,	"",	true,	"<playerName> [quantity:default=1] [itemData:default=0]",			"Attempts to find a player by name and determine what material is under your crosshair and add a quantity of this material to their inventory.  The optional itemData will override the targeted material itemData, affecting wood and wool types as well as item durability."),
	CLEAR_INVENTORY			("clearinv",		"hg.inv.clear",			true,	"",	true,	"[playername]",													"Clear your inventory or the inventory of the specified player.  The quick access bar and armor slots will not be cleared."),
	CLEAR_INVENTORY_ALL		("clearinvall",		"hg.inv.clear",			true,	"",	true,	"[playerName]",													"Clear the entire inventory, including guick access and armor slots, of you or the specified player."),
	LIST_INVENTORY			("listinv",			"hg.inv",				true,	"",	true,	"[playername]",													"List your inventory or the inventory of the specified player."),
	HEAL_PLAYER				("heal",			"hg.player.heal",		true,	"",	true,	"[playername]",													"Fully heal you or the specified player."),
	SET_DISPLAY_NAME		("setname",			"hg.player.rename",		true,	"",	true,	"<newname> [index.color/...]",									"Set your chat display name with optional colorizing, e.g., Bobbin 1.6/4.12 creates " + HGStatics.WARNING_COLOR + "Bob" + HGStatics.ERROR_COLOR + "bin."),
	TELEPORT				("teleport",		"hg.player.loc.change",	true,	"",	true,	"<x> <y> <z>",														"Teleport yourself to the specified location."),
	LOC						("loc",				"hg.player.loc",		true,	"",	true,	"(no arguments)",												"Get your current x y z location."),
	STRATUM					("strata",			"hg.player.loc.change",	true,	"",	true,	"<number of stratum>",														"Move to the nth occurrence of a solid to air transition in the direction of the crosshair."),
	GETTIME					("gettime",			"hg.time.get",			true,	"",	true,	"(no arguments)",												"Display the current world time."),
	SETTIME					("time",			"hg.time.set",			true,	"",	true,	"[time:default=noon] [playername]",								"Set the time of your current world or the current world of the specified player. Values of " + HGStatics.ARGUMENTS_COLOR + "[ 0 - 24000 | sunrise | noon | sunset | midnight ]" + HGStatics.NO_COLOR + " are valid for time."),
	LIST_WORLDS				("listworlds",		"hg.world",				true,	"",	true,	"(no arguments)",												"List the loaded worlds."),
	LOAD_WORLD				("loadworld",		"hg.worlds.change",		true,	"",	true,	"<name>",															"Loads the already created and named world. Requires the existance of world environment type file. See " + HGStatics.WARNING_COLOR + "/setworldtype" + HGStatics.NO_COLOR + " (or its alias)."),
	CREATE_WORLD			("createworld",		"hg.worlds.change",		true,	"",	true,	"<name> <normal|nether>",											"Create and load a world of the name and environment specified. Creates world environment type file"),
	DECLARE_WORLD_ENV		("setworldtype",	"hg.worlds.change",		true,	"",	true,	"<normal|nether>",												"Creates a file in the worlds directory that declares the world environment type."),
	LEAP					("leap",			"hg.player.loc.change",	true,	"",	true,	"<name>",															"Teleport player to specified world."),
	SETSPAWN				("setspawn",		"hg.spawn.change",		true,	"",	true,	"[worldname][player][x y z]",											"Set the default spawn point to the given coords or the players current location in the current or specified world."),
	SLAY_LIVING				("slay",			"hg.mobs.set",			true,	"",	true,	"(no arguments)",												"Remove all non-player living entities from the world."),
	REMOVE_DROPS			("cleardrops",		"hg.drops.set",			true,	"",	true,	"(no arguments)",												"Remove all item drop entities from the world."),
	HG_HELP					("hg_help",			"hg.help",				false,	"",	true,	"<command>",													"Display the basic allowed command list or the usage and help text of the command if a specific command is supplied."),
	DISABLE_PLUGIN			("disableplugins",	"hg.plugin.modify",		true,	"",	true,	"(no arguments)",												"Disable this plugin. Use " + HGStatics.WARNING_COLOR + "/reload" + HGStatics.NO_COLOR + " to re-enable it."),
	LIST_PLUGINS			("listplugins",		"hg.plugin",			true,	"",	true,	"(no arguments)",												"List all plugin."),
	LOAD_PLUGIN				("loadplugin",		"hg.plugin.modify",		true,	"",	true,	"<name>",															"Load the named plugin. The full path relative to the server directory must be specified, e.g., " + HGStatics.WARNING_COLOR + "plugins/SamplePlugin.jar"),
	ENABLE_PLUGIN			("enableplugin",	"hg.plugin.modify",		true,	"",	true,	"<name>",															"Enable the named plugin. The plugin name only must be passed, e.g., " + HGStatics.WARNING_COLOR + "SamplePlugin"),
	CLEAR_PLUGINS			("clearplugins",	"hg.plugin.modify",		true,	"",	true,	"(no arguments)",												"Disable and clear all running plugins. Use " + HGStatics.WARNING_COLOR + "/reload" + HGStatics.NO_COLOR + " to re-enable it."),
	SET_ALIAS				("setcommandalias",	"hg.commands.change",	true,	"",	true,	"<command> [alias:default=\"\"]",												"Assign an alias to a command. The default command will no longer be registered with HunkleberryGeneral.  Omitting the optional alias will clear the alias for default command."),
	SET_SERVER_ALLOW		("setserverallow",	"hg.commands.change",	true,	"",	true,	"<command> <true|false>",										"Set the plugins visibility to the command. Assigning false to a command will cause the command to no longer be registered with HunkleberryGeneral."),
	RESET_PROPERTIES		("resetproperties",	"hg.commands.change",	true,	"",	true,	"[all|alias|allow:default=all]",											"Reset the property files. Select 'alias' to remove all aliases, 'allow' to allow all, and all for both."),
	SAVE_PROPERTIES			("saveproperties",	"hg.commands.change",	true,	"",	true,	"[all|alias|allow:default=all]",											"Save any changes to the server allow command and command alias settings to their respective files."),
	WRITE_COMMAND_HTML		("writehtml",		"hg.commands.change",	true,	"",	true,	"(no arguments)",											"Write the current command configuration to an html file.");
	
	protected String defaultCommand;
	protected String permissions;
	protected Boolean opsOnly;
	protected String commandAlias;
	protected Boolean serverAllowed;
	protected String commandArgs;
	protected String commandUsage;
	
    private static final Map<String, HGCommandData> lookupName = new HashMap<String, HGCommandData>();
    private final static Logger log = Logger.getLogger("Minecraft");


    private HGCommandData(
    		String defaultCommand,
    		String permissions,
    		Boolean opsOnly,
    		String commandAlias,
    		Boolean serverAllowed,
    		String commandArgs,
    		String commandUsage){
    	this.defaultCommand = defaultCommand;
    	this.permissions = permissions;
    	this.opsOnly = opsOnly;
    	this.commandAlias = commandAlias;
    	this.serverAllowed = serverAllowed;
    	this.commandArgs = commandArgs;
    	this.commandUsage = commandUsage;
    }
	
	/**
	 * @return the resolved command string
	 */
	public String getCommand() {
		if (!(commandAlias == null) && !commandAlias.isEmpty()) {
			return commandAlias;
		} else {
			return defaultCommand;
		}
	}

	/**
	 * @return the commandPermissions
	 */
	public String getPermissions() {
		return permissions;
	}

	/**
	 * @param commandPermissions the commandPermissions to set
	 */
	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	/**
	 * @return the opsOnly
	 */
	public Boolean getOpsOnly() {
		return opsOnly;
	}

	/**
	 * @param opsOnly the opsOnly to set
	 */
	public void setOpsOnly(Boolean opsOnly) {
		this.opsOnly = opsOnly;
	}

	/**
	 * @return the commandAlias
	 */
	public String getCommandAlias() {
		return commandAlias;
	}

	/**
	 * @param commandAlias the commandAlias to set
	 */
	public void setCommandAlias(String commandAlias) {
		this.commandAlias = ((commandAlias == null) ? "" : commandAlias);
	}

	/**
	 * @return the serverAllowed
	 */
	public Boolean getServerAllowed() {
		return serverAllowed;
	}

	/**
	 * @param serverAllowed the serverAllowed to set
	 */
	public void setServerAllowed(Boolean serverAllowed) {
		this.serverAllowed = serverAllowed;
	}

	/**
	 * @return the defaultCommand
	 */
	public String getDefaultCommand() {
		return defaultCommand;
	}

	/**
	 * @return the commandArgs
	 */
	public String getCommandArgs() {
		return commandArgs;
	}

	/**
	 * @return the commandUsage
	 */
	public String getCommandUsage() {
		return commandUsage;
	}
	
	public String getPermissionsForProperty() {
		return ((getPermissions() != null) ? getPermissions() : "");
	}
	
	public String getOpsOnlyForProperty() {
		return ((getOpsOnly() != null) ? getOpsOnly() : "").toString();
	}
	
	public String getCommandAliasForProperty() {
		return ((getCommandAlias() != null) ? getCommandAlias() : "");
	}
	
	public String getServerAllowedForProperty() {
		return ((getServerAllowed() != null) ? getServerAllowed() : "").toString();
	}
    
    public static HGCommandData getCommandDataByName(final String name) {
        return lookupName.get(name);
    }

    static {
        for (HGCommandData command : values()) {
        	log.info("in the static:"+command.getDefaultCommand()+":"+command.getCommandAlias()+":");
            lookupName.put(command.getDefaultCommand(), command);
        }
    }
}


