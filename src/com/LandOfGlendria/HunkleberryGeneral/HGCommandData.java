
package com.LandOfGlendria.HunkleberryGeneral;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public enum HGCommandData {
	//@formatter:off
	GET_ITEM				("get",				"hg.inv",				true,	"",	true,	"{itemId|itemName} [quantity:default=1] [itemData:default=null]",				"Add a quantity of items to your inventory.  The optional itemData affects wood and wool types as well as item durability."),
	GIVE_ITEM				("give",			"hg.inv.other",			true,	"",	true,	"{playerName} {itemId|itemName} [quantity:default=1] [itemData:default=null]",	"Attempts to find a player by name and add quantity of items to their inventory.  The optional itemData affects wood and wool types as well as item durability."),
	GET_TARGETED_BLOCK		("getblock",		"hg.inv",				true,	"",	true,	"[quantity:default=1] [itemData:default=targeted]",						"Attempts to determine what material is under your crosshair and add a quantity of this material to your inventory.  The optional itemData will override the targeted material itemData, affecting wood and wool types as well as item durability."),
	GIVE_TARGETED_BLOCK		("giveblock",		"hg.inv.other",			true,	"",	true,	"{playerName} [quantity:default=1] [itemData:default=targeted]",		"Attempts to find a player by name and determine what material is under your crosshair and add a quantity of this material to their inventory.  The optional itemData will override the targeted material itemData, affecting wood and wool types as well as item durability."),
	CLEAR_INVENTORY			("clearinv",		"hg.inv",				true,	"",	true,	"[playerName:default=self]",											"Clear your inventory or the inventory of the specified player.  The quick access bar and armor slots will not be cleared."),
	CLEAR_INVENTORY_ALL		("clearinvall",		"hg.inv",				true,	"",	true,	"[playerName:default=self]",											"Clear the entire inventory, including quick access and armor slots, of you or the specified player."),
	LIST_INVENTORY			("listinv",			"hg.list",				true,	"",	true,	"[playerName:default=self]",											"List your inventory or the inventory of the specified player."),
	LIST_MATS				("matcontains",		"hg.list",				true,	"",	true,	"{string}",																"List all mats that contain the given string."),
	HEAL_PLAYER				("heal",			"hg.heal",				true,	"",	true,	"[playerName:default=self]",											"Fully heal you or the specified player."),
	SET_DISPLAY_NAME		("setname",			"hg.rename",			true,	"",	true,	"{playerName} {newName} [index,color/...:default=no color]",			"Set your chat display name with optional colorizing, e.g., Bobbin 1,6/4,12"),
	LIST_PLAYERS			("who",				"hg.rename",			true,	"",	true,	"[world:default=all] [brief|long:default=brief]",						"List the online players. Specify a world if desired. The optional specifier is resolved as [brief=name][long=name,display name,world]"),
	PLAYER_INFO				("playerinfo",		"hg.rename",			true,	"",	true,	"",																		"List expanded information about a specific player."),
	COLOR_CHART				("colorchart",		"hg.misc",				true,	"",	true,	"",																		"Display the available color numbers."),
	SET_SPAWN				("setspawn",		"hg.spawn",				true,	"",	true,	"[{x} {y} {z} [worldName]]:default=current location",					"Set the default spawn point to the given coords or the players current location in the current or specified world."),
	SET_COMPASS				("setcompass",		"hg.misc",				true,	"",	true,	"[playerName] [x y z] [here]:default=current world spawn",				"Sets your compass to point to the desired location, [here] indicates the literal string 'here'."),
	COMPASS					("compass",			"hg.misc",				true,	"",	true,	"",																		"Returns the direction that the player is currently facing."),
	LOC						("loc",				"hg.loc",				true,	"",	true,	"",																		"Get your current x y z location."),
	LEAP					("leap",			"hg.loc",				true,	"",	true,	"[[worldName] [playerName] [x y z]]:default=current world spawn",		"Teleport yourself to specified [player] or [world] or [world,coords] or [coords], in order of precedence, or by default, the world spawn point."),
	FLING					("fling",			"hg.loc.other",			true,	"",	true,	"{name}[worldName] [playerName] [x y z]:default=current world spawn",	"Teleport player to specified [player] or [world] or [world,coords] or [coords], in order of precedence, or by default, the world spawn point."),
	GATHER					("gather",			"hg.loc.other",			true,	"",	true,	"[playerName:default=all] [worldName:default=all]",						"Teleport a particular player (ignoring world) or everyone from a particular world, or just everyone to you."),
	SPAWN					("spawn",			"hg.misc",				true,	"",	true,	"",																		"Teleport to spawn point."),
	STRATUM					("strata",			"hg.loc",				true,	"",	true,	"[number:default=0]",													"Move to the nth occurrence of a solid to air transition in the direction of the crosshair."),
	MOTD					("motd",			"hg.list",				true,	"",	true,	"",																		"Display the current world motd."),
	SLAY_LIVING				("slay",			"hg.mobs",				true,	"",	true,	"",																		"Remove all non-player living entities from the world."),
	REMOVE_DROPS			("cleardrops",		"hg.drops",				true,	"",	true,	"",																		"Remove all item drop entities from the world."),
	GET_TIME				("gettime",			"hg.list",				true,	"",	true,	"[playerName:default=self]",											"Display the current world time for your world or the world of the selected player."),
	SET_TIME				("time",			"hg.time.set",			true,	"",	true,	"[playerName:default=self] [time:default=noon]",						"Set the time of your current world or the current world of the specified player. Values of [ 0 - 24000 | sunrise | noon | sunset | midnight ] are valid for time."),
	LIST_WORLDS				("listworlds",		"hg.list",				true,	"",	true,	"",																		"List the loaded worlds."),
	LOAD_WORLD				("loadworld",		"hg.worlds",			true,	"",	true,	"{worldName}",															"Loads the already created and named world. Requires the existance of world environment type file. See /setworldtype (or its alias)."),
	CREATE_WORLD			("createworld",		"hg.worlds",			true,	"",	true,	"{worldName} {normal|nether}",											"Create and load a world of the name and environment specified. Creates world environment type file"),
	DECLARE_WORLD_ENV		("setworldtype",	"hg.worlds",			true,	"",	true,	"{worldName} {normal|nether}",											"Creates a file in the world directory that declares the world environment type."),
	HG_HELP					("hg_help",			"hg.help",				false,	"",	true,	"[command]:default=list commands",										"Display the basic allowed command list or the usage and help text of the command if a specific command is supplied."),
	LIST_PLUGINS			("listplugins",		"hg.list",				true,	"",	true,	"",																		"List all plugins."),
	BOUNCE					("bounce",			"hg.bounce",			true,	"",	true,	"{player} [minutes:default=forever] [message]",									"The named player is kicked off the server and not allowed back for the specified number of minutes, or forever(until unbounced)."),
	UNBOUNCE				("unbounce",		"hg.bounce",			true,	"",	true,	"{player}",																"Unbounce a player name."),
	BOUNCE_IP				("bounceip",		"hg.bounce",			true,	"",	true,	"{player} [minutes:default=forever] [message]",							"Those with the ip of the specified player are kicked off the server and not allowed back for the specified number of minutes, or forever(until unbounced)."),
	FORCE_BOUNCE			("forcebounce",		"hg.bounce",			true,	"",	true,	"{player|ip}",															"Since the other bounces require the bouncee to be online for name/ip validation and actual kikking this command is provided to allow offline name/IPs to be bounced without validation. IPs must be formatted as /x.x.x.x. No attempt is made to ban a currently online player."),
	LIST_BOUNCED			("listbounced",		"hg.bounce",			true,	"",	true,	"",																		"List all currently bounced players and IPs"),
	RELOAD_BOUNCED			("reloadbounced",	"hg.bounce",			true,	"",	true,	"",																		"Reload bouncer.properties, reading any hand edits to the file."),
	LOAD_PLUGIN				("loadplugin",		"hg.plugin",			true,	"",	true,	"{name}",																"Load the named plugin. The full path relative to the server directory must be specified, e.g., plugins/SamplePlugin.jar"),
	DISABLE_PLUGIN			("disableplugin",	"hg.plugin",			true,	"",	true,	"{name}",																"Disable this plugin. Use /reload to re-enable it."),
	ENABLE_PLUGIN			("enableplugin",	"hg.plugin",			true,	"",	true,	"{name}",																"Enable the named plugin. The plugin name only must be passed, e.g., SamplePlugin"),
	CLEAR_PLUGINS			("clearplugins",	"hg.plugin",			true,	"",	true,	"",																		"Disable and clear all running plugins. Use /reload to re-enable it."),
	WRITE_COMMAND_HTML		("writehtml",		"hg.commands.change",	true,	"",	true,	"(no arguments)",														"Write the current command configuration to an html format."),
	WRITE_COMMAND_BUKKIT	("writebukkit",		"hg.commands.change",	true,	"",	true,	"(no arguments)",														"Write the current command configuration to a bukkit forum format."),
	WRITE_COMMAND_YAML  	("writeyaml",		"hg.commands.change",	true,	"",	true,	"(no arguments)",														"Write the current command configuration to a yaml forum format."),
	SET_ALIAS				("setcommandalias",	"hg.commands.change",	true,	"",	true,	"{command} [alias:default=\"\"]",										"Assign an alias to a command. The default command will no longer be registered with HunkleberryGeneral.  Omitting the optional alias will clear the alias for default command. Use /saveproperties to save any changes, otherwise they will be lost."),
	SET_SERVER_ALLOW		("setserverallow",	"hg.commands.change",	true,	"",	true,	"{command} {true|false}",												"Set the plugins visibility to the command. Assigning false to a command will cause the command to no longer be registered with HunkleberryGeneral. Use /saveproperties to save any changes, otherwise they will be lost."),
	SET_OPS_ONLY			("setopsonly",		"hg.commands.change",	true,	"",	true,	"{command} {true|false}",												"Set to true to only allow ops access to the command, false to allow everyone to use it. Use /saveproperties to save any changes, otherwise they will be lost."),
	SET_PERMISSIONS			("setpermissions",	"hg.commands.change",	true,	"",	true,	"{command} [new string:default=\"\"]",									"Change the permissions string. See the properties file for information on resetting the value to the default.  Use /saveproperties to save any changes, otherwise they will be lost."),
	SAVE_PROPERTIES			("saveproperties",	"hg.commands.change",	true,	"",	true,	"",																		"Save any changes to the server config settings to their respective files. This command must be used to save any changes made via chat commands."),
	RELOAD_PROPERTIES		("reloadproperties","hg.commands.change",	true,	"",	true,	"",																		"Reload commands and apply any changes made to the config files.  Overwrites any non-saved changes made via chat commands.");
	//@formatter:on
	
	protected String defaultCommand;
	protected String permissions;
	protected Boolean opsOnly;
	protected String commandAlias;
	protected Boolean serverAllowed;
	protected String commandArgs;
	protected String commandUsage;
	
    private static final Map<String, HGCommandData> lookupName = new HashMap<String, HGCommandData>();
	@SuppressWarnings("unused")
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
    
    public static void reloadLookup() {
    	lookupName.clear();
        for (HGCommandData command : values()) {
            lookupName.put(command.getCommand(), command);
        }
    }
    

    static {
        for (HGCommandData command : values()) {
            lookupName.put(command.getCommand(), command);
        }
    }
}
