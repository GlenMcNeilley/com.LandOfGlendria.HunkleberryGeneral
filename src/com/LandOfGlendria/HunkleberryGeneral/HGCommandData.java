
package com.LandOfGlendria.HunkleberryGeneral;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public enum HGCommandData {
	//@formatter:off
	GET_ITEM				("get",				"hg.inv",			true,	"",	true,	"{itemId} [quantity:default=1] [itemData:default=null]",				"Add a quantity of items to your inventory.  The optional itemData affects wood and wool types as well as item durability."),
	GIVE_ITEM				("give",			"hg.inv.other",		true,	"",	true,	"{playername} {itemId} [quantity:default=1] [itemData:default=null]",	"Attempts to find a player by name and add quantity of items to their inventory.  The optional itemData affects wood and wool types as well as item durability."),
	GET_TARGETED_BLOCK		("getblock",		"hg.inv",			true,	"",	true,	"[quantity:default=1] [itemData:default=targeted]",						"Attempts to determine what material is under your crosshair and add a quantity of this material to your inventory.  The optional itemData will override the targeted material itemData, affecting wood and wool types as well as item durability."),
	GIVE_TARGETED_BLOCK		("giveblock",		"hg.inv.other",		true,	"",	true,	"{playername} [quantity:default=1] [itemData:default=targeted]",		"Attempts to find a player by name and determine what material is under your crosshair and add a quantity of this material to their inventory.  The optional itemData will override the targeted material itemData, affecting wood and wool types as well as item durability."),
	CLEAR_INVENTORY			("clearinv",		"hg.inv",			true,	"",	true,	"[playername:default=self]",											"Clear your inventory or the inventory of the specified player.  The quick access bar and armor slots will not be cleared."),
	CLEAR_INVENTORY_ALL		("clearinvall",		"hg.inv",			true,	"",	true,	"[playerName:default=self]",											"Clear the entire inventory, including guick access and armor slots, of you or the specified player."),
	LIST_INVENTORY			("listinv",			"hg.list",			true,	"",	true,	"[playername:default=self]",											"List your inventory or the inventory of the specified player."),
	HEAL_PLAYER				("heal",			"hg.heal",			true,	"",	true,	"[playername:default=self]",											"Fully heal you or the specified player."),
	SET_DISPLAY_NAME		("setname",			"hg.rename",		true,	"",	true,	"{playername} {newname} [index.color/...:default=no color]",			"Set your chat display name with optional colorizing, e.g., Bobbin 1.6/4.12"),
	COLOR_CHART				("colorchart",		"hg.misc",			true,	"",	true,	"",																		"Display the available color numbers."),
	SLAY_LIVING				("slay",			"hg.mobs",			true,	"",	true,	"",																		"Remove all non-player living entities from the world."),
	REMOVE_DROPS			("cleardrops",		"hg.drops",			true,	"",	true,	"",																		"Remove all item drop entities from the world."),
	SET_SPAWN				("setspawn",		"hg.spawn",			true,	"",	true,	"[{x} {y} {z} [worldname]]:default=current location",					"Set the default spawn point to the given coords or the players current location in the current or specified world."),
	LOC						("loc",				"hg.loc",			true,	"",	true,	"",																		"Get your current x y z location."),
	LEAP					("leap",			"hg.loc",			true,	"",	true,	"[[worldname] [playername] [x y z]]:default=current world spawn",		"Teleport yourself to specified [player] or [world] or [world,coords] or [coords], in order of precedence, or by default, the world spawn point."),
	FLING					("fling",			"hg.loc.other",		true,	"",	true,	"{name}[worldname] [playername] [x y z]:default=current world spawn",	"Teleport player to specified [player] or [world] or [world,coords] or [coords], in order of precedence, or by default, the world spawn point."),
	TELEPORT				("teleport",		"hg.loc",			true,	"",	true,	"{x} {y} {z}",															"Teleport yourself to the specified location."),
	STRATUM					("strata",			"hg.loc",			true,	"",	true,	"[number:default=0]",													"Move to the nth occurrence of a solid to air transition in the direction of the crosshair."),
	GET_TIME				("gettime",			"hg.list",			true,	"",	true,	"[playername:default=self]",											"Display the current world time for your world or the world of the selected player."),
	SET_TIME				("time",			"hg.time.set",		true,	"",	true,	"[playername:default=self] [time:default=noon]",						"Set the time of your current world or the current world of the specified player. Values of [ 0 - 24000 | sunrise | noon | sunset | midnight ] are valid for time."),
	LIST_WORLDS				("listworlds",		"hg.list",			true,	"",	true,	"",																		"List the loaded worlds."),
	LOAD_WORLD				("loadworld",		"hg.worlds",		true,	"",	true,	"{worldname}",															"Loads the already created and named world. Requires the existance of world environment type file. See /setworldtype (or its alias)."),
	CREATE_WORLD			("createworld",		"hg.worlds",		true,	"",	true,	"{worldname} {normal|nether}",											"Create and load a world of the name and environment specified. Creates world environment type file"),
	DECLARE_WORLD_ENV		("setworldtype",	"hg.worlds",		true,	"",	true,	"{worldName} {normal|nether}",											"Creates a file in the world directory that declares the world environment type."),
	HG_HELP					("hg_help",			"hg.help",			false,	"",	true,	"[command]:default=list commands",										"Display the basic allowed command list or the usage and help text of the command if a specific command is supplied."),
	LIST_PLUGINS			("listplugins",		"hg.list",			true,	"",	true,	"",																		"List all plugin."),
	LOAD_PLUGIN				("loadplugin",		"hg.plugin",		true,	"",	true,	"{name}",																"Load the named plugin. The full path relative to the server directory must be specified, e.g., plugins/SamplePlugin.jar"),
	DISABLE_PLUGIN			("disableplugin",	"hg.plugin",		true,	"",	true,	"{name}",																"Disable this plugin. Use /reload to re-enable it."),
	ENABLE_PLUGIN			("enableplugin",	"hg.plugin",		true,	"",	true,	"{name}",																"Enable the named plugin. The plugin name only must be passed, e.g., SamplePlugin"),
	CLEAR_PLUGINS			("clearplugins",	"hg.plugin",		true,	"",	true,	"",																		"Disable and clear all running plugins. Use /reload to re-enable it."),
	WRITE_COMMAND_HTML		("writehtml",		"hg.commands.change",	true,	"",	true,	"(no arguments)",														"Write the current command configuration to an html file."),
	WRITE_COMMAND_BUKKIT	("bukkit",		"hg.commands.change",	true,	"",	true,	"(no arguments)",														"Write the current command configuration to a bukkit forum format."),
	SET_ALIAS				("(notimpl)setcommandalias",	"hg.commands.change",	true,	"",	true,	"{command} [alias:default=\"\"]",							"(not impl.)Assign an alias to a command. The default command will no longer be registered with HunkleberryGeneral.  Omitting the optional alias will clear the alias for default command."),
	SET_SERVER_ALLOW		("(notimpl)setserverallow",	"hg.commands.change",	true,	"",	true,	"{command} {true|false}",									"(not impl.)Set the plugins visibility to the command. Assigning false to a command will cause the command to no longer be registered with HunkleberryGeneral."),
	RESET_PROPERTIES		("(notimpl)resetproperties",	"hg.commands.change",	true,	"",	true,	"[all|alias|allow:default=all]",							"(not impl.)Reset the property files. Select 'alias' to remove all aliases, 'allow' to allow all, and all for both."),
	SAVE_PROPERTIES			("(notimpl)saveproperties",	"hg.commands.change",	true,	"",	true,	"[all|alias|allow:default=all]",							"(not impl.)Save any changes to the server allow command and command alias settings to their respective files.");
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

    static {
        for (HGCommandData command : values()) {
            lookupName.put(command.getDefaultCommand(), command);
        }
    }
}
