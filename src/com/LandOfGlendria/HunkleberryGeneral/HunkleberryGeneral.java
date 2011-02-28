package com.LandOfGlendria.HunkleberryGeneral;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HunkleberryGeneral extends JavaPlugin {

	private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
	private static HGPlayerListener listener;
	public static PluginDescriptionFile pdfFile;
	public static HGConfig config;
	public static HGBouncer bouncer;
	private static HGMessageManagement msg;
	private static HGCommandHandler commandHandler;

	public HunkleberryGeneral() {
	}
	
	public PluginCommand getMyCommand(String name) {
		return this.getCommand(name);
	}
	
	public void onEnable() {
		pdfFile = getDescription();
		msg = new HGMessageManagement(this);
		config = new HGConfig(this, msg);
		bouncer = new HGBouncer(this, msg, config);
		listener = new HGPlayerListener(this, msg, bouncer);
		commandHandler = new HGCommandHandler(this, msg, bouncer);
		PluginManager pm = getServer().getPluginManager();
// this doesn't look fun
//		getCommand("get").setExecutor(new TimePluginCommand(this));
//		getCommand("give").setExecutor(new TimePluginCommand(this));
//		getCommand("getblock").setExecutor(new TimePluginCommand(this));
//		getCommand("giveblock").setExecutor(new TimePluginCommand(this));
//		getCommand("clearinv").setExecutor(new TimePluginCommand(this));
//		getCommand("clearinvall").setExecutor(new TimePluginCommand(this));
//		getCommand("listinv").setExecutor(new TimePluginCommand(this));
//		getCommand("matcontains").setExecutor(new TimePluginCommand(this));
//		getCommand("heal").setExecutor(new TimePluginCommand(this));
//		getCommand("setname").setExecutor(new TimePluginCommand(this));
//		getCommand("who").setExecutor(new TimePluginCommand(this));
//		getCommand("playerinfo").setExecutor(new TimePluginCommand(this));
//		getCommand("colorchart").setExecutor(new TimePluginCommand(this));
//		getCommand("setspawn").setExecutor(new TimePluginCommand(this));
//		getCommand("setcompass").setExecutor(new TimePluginCommand(this));
//		getCommand("compass").setExecutor(new TimePluginCommand(this));
//		getCommand("loc").setExecutor(new TimePluginCommand(this));
//		getCommand("leap").setExecutor(new TimePluginCommand(this));
//		getCommand("fling").setExecutor(new TimePluginCommand(this));
//		getCommand("gather").setExecutor(new TimePluginCommand(this));
//		getCommand("spawn").setExecutor(new TimePluginCommand(this));
//		getCommand("strata").setExecutor(new TimePluginCommand(this));
//		getCommand("motd").setExecutor(new TimePluginCommand(this));
//		getCommand("slay").setExecutor(new TimePluginCommand(this));
//		getCommand("cleardrops").setExecutor(new TimePluginCommand(this));
//		getCommand("gettime").setExecutor(new TimePluginCommand(this));
//		getCommand("time").setExecutor(new TimePluginCommand(this));
//		getCommand("listworlds").setExecutor(new TimePluginCommand(this));
//		getCommand("loadworld").setExecutor(new TimePluginCommand(this));
//		getCommand("createworld").setExecutor(new TimePluginCommand(this));
//		getCommand("setworldtype").setExecutor(new TimePluginCommand(this));
//		getCommand("hg_help").setExecutor(new TimePluginCommand(this));
//		getCommand("listplugins").setExecutor(new TimePluginCommand(this));
//		getCommand("bounce").setExecutor(new TimePluginCommand(this));
//		getCommand("unbounce").setExecutor(new TimePluginCommand(this));
//		getCommand("bounceip").setExecutor(new TimePluginCommand(this));
//		getCommand("forcebounce").setExecutor(new TimePluginCommand(this));
//		getCommand("listbounced").setExecutor(new TimePluginCommand(this));
//		getCommand("loadplugin").setExecutor(new TimePluginCommand(this));
//		getCommand("disableplugin").setExecutor(new TimePluginCommand(this));
//		getCommand("enableplugin").setExecutor(new TimePluginCommand(this));
//		getCommand("clearplugins").setExecutor(new TimePluginCommand(this));
//		getCommand("writehtml").setExecutor(new TimePluginCommand(this));
//		getCommand("bukkit").setExecutor(new TimePluginCommand(this));
//		getCommand("setcommandalias").setExecutor(new TimePluginCommand(this));
//		getCommand("setserverallow").setExecutor(new TimePluginCommand(this));
//		getCommand("setopsonly").setExecutor(new TimePluginCommand(this));
//		getCommand("setpermissions").setExecutor(new TimePluginCommand(this));
//		getCommand("saveproperties").setExecutor(new TimePluginCommand(this));
//		getCommand("reloadproperties").setExecutor(new TimePluginCommand(this));
		pm.registerEvent(org.bukkit.event.Event.Type.PLAYER_JOIN, listener, org.bukkit.event.Event.Priority.Normal, this);
		pm.registerEvent(org.bukkit.event.Event.Type.PLAYER_LOGIN, listener, org.bukkit.event.Event.Priority.Normal, this);
		pm.registerEvent(org.bukkit.event.Event.Type.PLAYER_RESPAWN, listener, org.bukkit.event.Event.Priority.Normal, this);
		
		//iterate through my command data and set aliases and stuff
		for (HGCommandData command : HGCommandData.values()) {
			Command registeredCommand = this.getCommand(command.getDefaultCommand());
			if (registeredCommand != null) {
				if (command.getCommandAlias() != null && !command.getCommandAlias().isEmpty()) {
					registeredCommand.setAliases(Arrays.asList(command.getCommandAlias()));
				}
				if (command.getCommandArgs() != null && !command.getCommandArgs().isEmpty()) {
					registeredCommand.setUsage(command.getCommandArgs());
				}
				if (command.getCommandUsage() != null && !command.getCommandUsage().isEmpty()) {
					registeredCommand.setTooltip(command.getCommandArgs());
				}
			} else {
				msg.severe("Command not registered " + command.getDefaultCommand());
			}
		}

		msg.info((new StringBuilder(String.valueOf(pdfFile.getName()))).append(" version ").append(pdfFile.getVersion()).append(" is enabled").toString());
	}

	public void onDisable() {
		
	}
	
    public boolean onCommand(CommandSender sender, Command incomingCommand, String commandLabel, String[] args) {
    	if (!(sender instanceof Player)) return false;
    	Player player = (Player)sender;
    	
		if (incomingCommand.getName().equalsIgnoreCase("hg_test")) {
			msg.sendPositiveMessage(player,
					(new StringBuilder(String.valueOf(HunkleberryGeneral.pdfFile.getName()))).append(" version ")
							.append(HunkleberryGeneral.pdfFile.getVersion()).append(" is listening.").toString());
			return true;
		}
		
		String base = incomingCommand.getName();
		HGCommandData command = HGCommandData.getCommandDataByName(base);
		if (command == null) {
			return false;
		}
		if (!command.getServerAllowed().booleanValue()) {
			return false;
		}
		if (!player.isOp() && command.getOpsOnly().booleanValue()) {
			if (HGConfig.permissions == null) {
				return false;
			}
			if (!HGConfig.permissions.has(player, command.getPermissions())) {
				return false;
			}
		}
		String[] commandArray = new String[args.length+1];
		int i = 0;
		for (String arg : args) {
			commandArray[i+1] = arg;
			i++;
		}
		commandArray[0] = base;
		
		String error;
		if ((error = commandHandler.handleCommand(player, command, commandArray)) != null) {
			String helpCommand = HGCommandData.HG_HELP.getCommand();
			msg.sendColorErrorUsage(player, error, command.getCommand(), command.getCommandArgs(), helpCommand);
		}
		return true;
	}


	public boolean isDebugging(Player player) {
		if (debugees.containsKey(player)) {
			return ((Boolean) debugees.get(player)).booleanValue();
		} else {
			return false;
		}
	}

	public void setDebugging(Player player, boolean value) {
		debugees.put(player, Boolean.valueOf(value));
	}
}
