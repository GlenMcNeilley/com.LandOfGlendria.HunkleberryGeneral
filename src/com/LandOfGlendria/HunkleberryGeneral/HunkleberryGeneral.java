package com.LandOfGlendria.HunkleberryGeneral;

//import java.util.Arrays;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
//import org.bukkit.command.PluginCommand;
//import org.bukkit.command.SimpleCommandMap;
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
	private static HGCommandDAO commandDAO;
	private static HGCommandHandler commandHandler;

	public HunkleberryGeneral() {
	}

/**	
	public void setCommandAlias(String name,String newValue) {
		Command registeringCommand = getCommand(name);
		if (registeringCommand != null) {
			registeringCommand.setAliases(Arrays.asList(newValue));	
			msg.info("HG Setting alias for "+name+" to "+newValue);
		}
	}
*/	

	public void onEnable() {
		commandDAO = new HGCommandDAO();
		pdfFile = getDescription();
		msg = new HGMessageManagement(this,commandDAO);
		config = new HGConfig(this, msg, commandDAO);
		bouncer = new HGBouncer(this, msg, config);
		listener = new HGPlayerListener(this, msg, bouncer);
		commandHandler = new HGCommandHandler(this, msg, bouncer,commandDAO);
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvent(org.bukkit.event.Event.Type.PLAYER_JOIN, listener, org.bukkit.event.Event.Priority.Normal, this);
		pm.registerEvent(org.bukkit.event.Event.Type.PLAYER_LOGIN, listener, org.bukkit.event.Event.Priority.Normal, this);
		pm.registerEvent(org.bukkit.event.Event.Type.PLAYER_RESPAWN, listener, org.bukkit.event.Event.Priority.Normal, this);

//		try {
//		String test = msg.encodeString("Now is the time for all,,,...///===" + HGStatics.ERROR_COLOR + "   ");
//				msg.info(test);
//		msg.info(msg.decodeString(test));
//		} catch (UnsupportedEncodingException e){
//			e.printStackTrace();
//		}
		
/**			
 			for (HGCommandData command : HGCommandData.values()) {
				Command registeredCommand = this.getCommand(command.getDefaultCommand());
				if (registeredCommand != null) {
					if (command.getCommandAlias() != null && !command.getCommandAlias().isEmpty()) {
						setCommandAlias(command.getDefaultCommand(),command.getCommandAlias());
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
*/

		commandHandler.autoLoadWorlds();
		msg.info((new StringBuilder(String.valueOf(pdfFile.getName()))).append(" version ").append(pdfFile.getVersion()).append(" is enabled").toString());
	}

	public void onDisable() {
	}
	
    public boolean onCommand(CommandSender sender, Command incomingCommand, String commandLabel, String[] args) {
    	if (!(sender instanceof Player)) return false;
    	Player player = (Player)sender;
    	//msg.info("recieved command: " + incomingCommand.getName());
		if (incomingCommand.getName().equalsIgnoreCase("hg_test")) {
			msg.sendPositiveMessage(player,
					(new StringBuilder(String.valueOf(HunkleberryGeneral.pdfFile.getName()))).append(" version ")
							.append(HunkleberryGeneral.pdfFile.getVersion()).append(" is listening.").toString());
			return true;
		}
		
		String base = incomingCommand.getName();
		HGCommand command = HGCommandDAO.getCommandDataByName(base);
		if (command == null) {
			return false;
		}
		if (!commandDAO.getServerAllowed(command).booleanValue()) {
			return false;
		}
		if (!player.isOp() && commandDAO.getOpsOnly(command).booleanValue()) {
			if (HGConfig.permissions == null) {
				return false;
			}
			if (!HGConfig.permissions.has(player, commandDAO.getPermissions(command))) {
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
			String helpCommand = HGCommandDAO.getCommand(HGCommand.HG_HELP);
			msg.sendColorErrorUsage(player, error, HGCommandDAO.getCommand(command), commandDAO.getCommandArgs(command), helpCommand);
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
