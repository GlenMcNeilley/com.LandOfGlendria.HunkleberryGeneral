package com.LandOfGlendria.HunkleberryGeneral;

import java.io.IOException;
import java.util.logging.Logger;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class HGCommandHandler {

	private final Logger log = Logger.getLogger("Minecraft");
	private HGConfig config;
	private HGMessageManagement msg;
	private HGCommandDAO commandDAO;
	private HGInventoryManagement inventoryManager;
	private HGPlayerManagement playerManager;
	private HGWorldManagement worldManager;
	private HGWorldlyThings worldly;
	private HGTimeManagement timeManager;
	private HGPluginManagement pluginManager;
	private HunkleberryGeneral plugin;

	public HGCommandHandler(HunkleberryGeneral plugin, HGMessageManagement msg,HGBouncer bouncer,HGCommandDAO commandDAO) {
		this.plugin = plugin;
		this.msg = msg;
		this.commandDAO = commandDAO;
		config = HunkleberryGeneral.config;
		pluginManager = new HGPluginManagement(plugin,msg);
		worldly = new HGWorldlyThings(plugin,msg,config);
		//locationDAO = new HGLocationDAO(worldly);
		playerManager = new HGPlayerManagement(plugin,msg,bouncer,worldly);
		inventoryManager = new HGInventoryManagement(msg);
		worldManager = new HGWorldManagement(plugin, msg, worldly, commandDAO);
		timeManager = new HGTimeManagement(msg);

	}
	
	public void autoLoadWorlds() {
		worldManager.loadWorldArray(worldly.getWorldAutoLoadArray());
		return;
	}

	public String handleCommand(Player player, HGCommand cmd, String commandArray[]) {
		if (cmd == HGCommand.HG_HELP) {
			if (commandArray.length > 1) {
				boolean allowedToUse = true;
				HGCommand commandForHelp = HGCommandDAO.getCommandDataByName(commandArray[1].replaceAll("/", ""));
				if (commandForHelp == null) {
					return msg.formatInvalidArgs(commandArray[1], "Unknown command");
				}
				if (!player.isOp() && commandDAO.getOpsOnly(commandForHelp).booleanValue()) {
					if (HGConfig.permissions == null) {
						allowedToUse = false;
					} else if (!HGConfig.permissions.has(player, commandDAO.getPermissions(commandForHelp))) {
						allowedToUse = false;
					}
				}
				msg.sendColorHelp(player, commandForHelp, allowedToUse);
			} else {
				msg.sendCommandList(player);
			}
			return null;
		}
		
		if (cmd == HGCommand.LOAD_PLUGIN) {
			if (commandArray.length > 1) {
				String name = msg.concatinateRemainingArgs(commandArray, 1);
				String message = pluginManager.loadPlugin(name);
				if (message != null) {
					return message;
				} else {
					msg.sendPositiveMessage(player, (commandArray[1] + " is loaded. It must now be enabled."));
					return null;
				}
			} else {
				return "Plugin path required.";
			}
		}
		if (cmd == HGCommand.ENABLE_PLUGIN) {
			String name = null;
			if (commandArray.length > 1) {
				name = msg.concatinateRemainingArgs(commandArray, 1);
				String message = pluginManager.enablePlugin(name);
				if (message != null) {
					return message;
				} else {
					msg.sendPositiveMessage(player, (name + " is being enabled."));
					return null;
				}
			} else {
				return "Plugin name required.";
			}
		}
		if (cmd == HGCommand.DISABLE_PLUGIN) {
			String name = null;
			if (commandArray.length > 1) {
				name = msg.concatinateRemainingArgs(commandArray, 1);
				String message = pluginManager.disablePlugin(name);
				if (message != null) {
					return message;
				} else {
					log.info((new StringBuilder(String.valueOf(HunkleberryGeneral.pdfFile.getName()))).append(": ").append(player.getDisplayName())
							.append(" is disabling plugin ").append(name).append(".").toString());
					msg.sendPositiveMessage(player, (new StringBuilder(String.valueOf(name))).append(" is being disabled.").toString());
					return null;
				}
			} else {
				return "Plugin name required.";
			}
		}
		if (cmd == HGCommand.CLEAR_PLUGINS) {
			log.info((new StringBuilder(String.valueOf(HunkleberryGeneral.pdfFile.getName()))).append(": ").append(player.getDisplayName())
					.append(" is disabling and clearing all plugins.").toString());
			log.info("Use /reload to re-enable all plugins.");
			pluginManager.clearAllPlugins();
			msg.sendPositiveMessage(
					player,
					(new StringBuilder("Disabling and clearing all plugins. Use ")).append(HGStatics.WARNING_COLOR).append("/reload")
							.append(HGStatics.POSITIVE_COLOR).append(" to re-enable all plugins.").toString());
			return null;
		}
		if (cmd == HGCommand.LIST_PLUGINS) {
			pluginManager.listPlugins(player);
			return null;
		}
		if (cmd == HGCommand.CLEAR_INVENTORY) {
			Player resolvedPlayer;
			if (commandArray.length > 1) {
				String receiverName = commandArray[1];
				Player receiver = playerManager.resolvePlayer(receiverName);
				if (receiver == null) {
					return msg.formatInvalidArgs(receiverName, "Invalid player name");
				} 
				resolvedPlayer = receiver;
			} else {
				resolvedPlayer = player;
			}
			inventoryManager.clearPlayerInventory(resolvedPlayer);
			msg.sendPositiveMessage(player, "Cleared inventory.");
			return null;
		}
		if (cmd == HGCommand.CLEAR_INVENTORY_ALL) {
			Player resolvedPlayer;
			if (commandArray.length > 1) {
				String receiverName = commandArray[1];
				Player receiver = playerManager.resolvePlayer(receiverName);
				if (receiver == null) {
					return msg.formatInvalidArgs(receiverName, "Invalid player name");
				} 
				resolvedPlayer = receiver;
			} else {
				resolvedPlayer = player;
			}
			inventoryManager.clearEntirePlayerInventory(resolvedPlayer);
			msg.sendPositiveMessage(player, "Cleared entire inventory.");
			return null;
		}
		if (cmd == HGCommand.LIST_INVENTORY) {
			Player resolvedPlayer;
			if (commandArray.length > 1) {
				String receiverName = commandArray[1];
				Player receiver = playerManager.resolvePlayer(receiverName);
				if (receiver == null) {
					return msg.formatInvalidArgs(receiverName, "Invalid player name");
				} 
				resolvedPlayer = receiver;
			} else {
				resolvedPlayer = player;
			}
			inventoryManager.listPlayerInventory(player,resolvedPlayer);
			return null;
		}
		if (cmd == HGCommand.HEAL_PLAYER) {
			Player resolvedPlayer = player;
			if (commandArray.length > 1) {
				String receiverName = commandArray[1];
				Player receiver = playerManager.resolvePlayer(receiverName);
				if (receiver == null) {
					return msg.formatInvalidArgs(receiverName, "Invalid player name");
				}
				resolvedPlayer = receiver;
			}
			String message = playerManager.healPlayer(resolvedPlayer);
			if (message != null) {
				return message;
			}
			if (player != resolvedPlayer) {
				msg.sendPositiveMessage(resolvedPlayer, (new StringBuilder("Healed by ")).append(player.getDisplayName()).toString());
				msg.sendPositiveMessage(player, (new StringBuilder("Healed ")).append(resolvedPlayer.getDisplayName()).append(".").toString());
			} else {
				msg.sendPositiveMessage(player, "Fully healed.");
			}
			return null;
		}
		if (cmd == HGCommand.SLAY_LIVING) {
			worldManager.removeNonPlayerEntities(player);
			return null;
		}
		if (cmd == HGCommand.REMOVE_DROPS) {
			worldManager.removeItemDropEntities(player);
			return null;
		}
		if (cmd == HGCommand.SET_DISPLAY_NAME) {
			//first arg must be player name
			String newName = null;
			String indexAndColors = null;
			Player receiver = null;
			if (commandArray.length > 1) {
				receiver = playerManager.resolvePlayer(commandArray[1]);
				if (receiver == null) {
					return msg.formatInvalidArgs(commandArray[1], "Must be current player name");
				}
				if (commandArray.length > 2) {
					newName = commandArray[2];
					if (commandArray.length > 3) {
						indexAndColors = commandArray[3];
					}
				} else {
					newName = player.getName();
				}
			} else {
				return ("First argument must be the name of the player whose name is to be changed.");
			}
			
			return playerManager.setDisplayName(receiver, player, newName, indexAndColors);
			
		}

		if (cmd == HGCommand.LOC) {
			return playerManager.getLocation(player);
		}
		
		if (cmd == HGCommand.SIGHTS) {
			boolean anyone = false;
			if (commandArray.length < 2) {
				return playerManager.sendLocationList(player,anyone);
			}
			String name = null;
			if (commandArray.length > 2) {
				name = commandArray[2];
			}
			if (commandArray[1].equalsIgnoreCase("add")) {
				if (name != null) {
					return playerManager.addLocation(player, name, anyone);
				} else {
					return ("Invalid arguments, location name required.");
				}
			} else if (commandArray[1].equalsIgnoreCase("remove")) {
				if (name != null) {
					return playerManager.removeLocation(player, name, anyone);
				} else {
					return ("Invalid arguments, location name required.");
				}
			} else if (commandArray[1].equalsIgnoreCase("save")) {
				return playerManager.saveLocationsByOwner(player, anyone);
			} else if (commandArray[1].equalsIgnoreCase("list")) {
				return playerManager.sendLocationList(player,anyone);
			} else {
				return msg.formatInvalidArgs(commandArray[1], "Invalid argument");
			}
		}
		
		if (cmd == HGCommand.SEE) {
			boolean anyone = false;
			if (commandArray.length > 1) {
				return playerManager.goSight(player,commandArray[1],anyone);
			} else {
				return playerManager.goSight(player,"home",anyone);
			}
		}
		
		if (cmd == HGCommand.STRATUM) {
			int strataToJump = 0;
			if (commandArray.length > 1) {
				try {
					strataToJump = Integer.parseInt(commandArray[1]);
				} catch (NumberFormatException e) {
					return (new StringBuilder()).append(new StringBuilder("Invalid strata count: [")).append(commandArray[1]).append("].").toString()
							.toString();
				}
				if (strataToJump < 0) {
					return msg.formatInvalidArgs(commandArray[1], "Invalid strata count. Must be greater or equal to Zero");
				}
			}
			return playerManager.stratum(player, strataToJump);
		}
		
		if (cmd == HGCommand.SPAWN) {
			playerManager.teleportToSpawn(player);
			return null;
		}
		
		if (cmd == HGCommand.MOTD) {
			if (commandArray.length > 1) {
				config.readMotd();
			}
			if (HGStatics.MOTD_STRING != null) {
				msg.parseMotdForPlayer(player,new String(HGStatics.MOTD_STRING));
			}
			return null;
		}
		
		if (cmd == HGCommand.PLAYER_INFO) {
			if (commandArray.length != 2) {
				return ("Invalid Arguments.");
			}
			Player resolvedPlayer = playerManager.resolvePlayer(commandArray[1]);
			msg.sendPositiveMessage(player, resolvedPlayer.getName());
			msg.sendPositiveMessage(player, resolvedPlayer.getDisplayName());
			msg.sendPositiveMessage(player, "Location: " + player.getWorld().getName() + " ( " + resolvedPlayer.getLocation().getBlockX() + " , " + 
					resolvedPlayer.getLocation().getBlockY() + " , " + 
					resolvedPlayer.getLocation().getBlockZ() + " )" );
			msg.sendPositiveMessage(player, resolvedPlayer.getAddress().toString());
			if (resolvedPlayer.isOp()) {
				msg.sendPositiveMessage(player, "Is an Op.");
			} else {
				msg.sendPositiveMessage(player, "Is not an Op.");
			}
		}
		
		if (cmd == HGCommand.SET_COMPASS) {
			Player resolvedPlayer = null;
			Location location = null;
			int locX = 0;
			int locY = 0;
			int locZ = 0;
			for (int i = 1;i<commandArray.length;i++) {
				//here
				if (resolvedPlayer == null && location == null) {
					if (commandArray[i].equalsIgnoreCase(HGStatics.HERE)) {
						location=player.getLocation();
						msg.sendPositiveMessage(player,"Setting compass target to this location.");
						break;
					}
				}
				//player
				if (resolvedPlayer == null && location == null) {
					resolvedPlayer = playerManager.resolvePlayer(commandArray[i]);
					if (resolvedPlayer != null) {
						location = resolvedPlayer.getLocation();
						if (!resolvedPlayer.getWorld().equals(player.getWorld())) {
							return "That player is in a different world.";
						}
						msg.sendPositiveMessage(player,"Setting compass target to current location of player " + resolvedPlayer.getName() + ".");
						break;
					}
				}
				//xyz
				if (location == null && commandArray.length - i >= 2) {
					try {
						locX = Integer.valueOf(Integer.parseInt(commandArray[i]));
						locY = Integer.valueOf(Integer.parseInt(commandArray[i + 1]));
						locZ = Integer.valueOf(Integer.parseInt(commandArray[i + 2]));
						i += 3;
						location = new Location(player.getWorld(), locX, locY, locZ);
						msg.sendPositiveMessage(player,"Setting compass target to ( " + locX + " , " + locY + " , " + locZ + " ).");
						break;
					} catch (NumberFormatException e) {
						//inue;
					} 
				} else {
					return msg.formatInvalidArgs(commandArray[i], "Invalid argument, not a player, 'here', or complete integer coordinate triplet");
				}
			}
			if (location == null) {
				CraftWorld cworld = (CraftWorld) player.getWorld();
				net.minecraft.server.WorldServer wserver = cworld.getHandle();
				locX = wserver.q.c();
				locY = wserver.q.d();
				locZ = wserver.q.e();
				location = new Location(player.getWorld(), locX, locY, locZ);
				msg.sendPositiveMessage(player,"Setting compass target to world spawn point.");
			}
			player.setCompassTarget(location);
		}
		if (cmd == HGCommand.COMPASS) {
			Vector lookingAt = player.getLocation().getDirection();
			double radians = Math.atan2(lookingAt.getX(), lookingAt.getZ());
			double s = Math.PI/8;
			String facing = new String("Nowhere");
			if ((radians > -(s) && radians <= 0) || radians > 0 && radians <= s){
				facing = "West";
			} else if (radians > s && radians <= s*3){
				facing = "SouthWest";
			} else if (radians > s*3 && radians <= s*5){
				facing = "South";
			} else if (radians > s*5 && radians <= s*7){
				facing = "SouthEast";
			} else if (radians > s*7 || radians <= -(s*7)){
				facing = "East";
			} else if (radians > -(s*7) && radians <= -(s*5)){
				facing = "NorthEast";
			} else if (radians > -(s*5) && radians <= -(s*3)){
				facing = "North";
			} else if (radians > -(s*3) && radians <= -(s)){
				facing = "NorthWest";
			}
			msg.sendPositiveMessage(player,"You are facing: " + facing);	
		}
		
		if (cmd == HGCommand.LIST_PLAYERS) {
			World world = null;
			int qualifier = -1;
			for (int i = 1;i<commandArray.length;i++) {
				if (world == null) {
					world = plugin.getServer().getWorld(commandArray[i]);
					if (world != null) {
						continue;
					}
				}
				if (HGStatics.BRIEF.equalsIgnoreCase(commandArray[i])) {
					qualifier = 0;
					continue;
				} else if (HGStatics.LONG.equalsIgnoreCase(commandArray[i])) {
					qualifier = 1;
					continue;
				} else {
					return msg.formatInvalidArgs(commandArray[i],"Not world or expected qualifier");
				}
			}
			
			StringBuilder sb = new StringBuilder("Listing players");
			if (world != null) {
				sb.append(" in world " + world.getName());
			}
			sb.append("'");
			msg.sendPositiveMessage(player, sb.toString());
			msg.sendPositiveMessage(player,msg.getPlayerList(world,qualifier));
			return null;
		}

		if (cmd == HGCommand.GET_TIME) {
			//second arg is either world or player
			World receiverWorld = player.getWorld();
			if (commandArray.length > 1) {
				Player receiver = plugin.getServer().getPlayer(commandArray[1]);
				if (receiver == null) {
					receiverWorld = plugin.getServer().getWorld(commandArray[1]);
					if (receiverWorld == null) {
					return msg.formatInvalidArgs(commandArray[1], "Invalid Player/World Name");
					}
				} else {
					receiverWorld = player.getWorld();
				}
			}				
			return timeManager.getWorldTime(player,receiverWorld);
		}

		if (cmd == HGCommand.SET_TIME) {
			int index = 1;
			Player resolvedPlayer;
			if (commandArray.length > index) {
				String receiverName = commandArray[index];
				Player receiver = playerManager.resolvePlayer(receiverName);;
				if (receiver == null) {
					resolvedPlayer = player;
				} else {
					resolvedPlayer = receiver;
					index++;
				}
			} else {
				resolvedPlayer = player;
			}

			int time = 6000;

			if (commandArray.length > index) {
				if (commandArray[index].equalsIgnoreCase("sunrise")) {
					time = 22500;
				} else if (commandArray[index].equalsIgnoreCase("sunset")) {
					time = 12000;
				} else if (commandArray[index].equalsIgnoreCase("noon")) {
					time = 6000;
				} else if (commandArray[index].equalsIgnoreCase("midnight")) {
					time = 18000;
				} else {
					try {
						time = Integer.parseInt(commandArray[index]);
					} catch (NumberFormatException e) {
						return msg.formatInvalidArgs(commandArray[index], "Invalid time value");
					}
				}
			}
			World receiverWorld = resolvedPlayer.getWorld();

			return timeManager.setWorldTime(player, receiverWorld, time);
		}
		
		if (cmd == HGCommand.LIST_WORLDS) {
			worldManager.listWorlds(player);
			return null;
		}
		
		if (cmd == HGCommand.CREATE_WORLD ) {
			if (commandArray.length != 3) {
				return "Invalid Arguments";
			} else {
				return worldManager.worldLoader(player,cmd,commandArray[1],commandArray[2]);
			}
		}
		
		if (cmd == HGCommand.LOAD_WORLD) {
			if (commandArray.length != 2) {
				return "Invalid arguments";
			} else {
				return worldManager.worldLoader(player,cmd,commandArray[1],null);
			}
		}
		
		if (cmd == HGCommand.DECLARE_WORLD_ENV) {
			if (commandArray.length != 3) {
				return "Invalid arguments";
			} else {
				return worldManager.worldLoader(player, cmd, commandArray[1], commandArray[2]);
			}
		}
		
		if (cmd == HGCommand.WORLD_LOAD_LIST) {
			if (commandArray.length != 2) {
				return "Invalid arguments";
			} else {
				return worldManager.worldLoadList(player, commandArray[1]);
			}
		}
			
		if (cmd == HGCommand.FLING) {
			//first arg must be player
			Player resolvedPlayer;
			if (commandArray.length > 1) {
				String receiverName = commandArray[1];
				Player receiver = playerManager.resolvePlayer(receiverName);
				if (receiver == null) {
					return msg.formatInvalidArgs(receiverName, "Invalid player name");
				}
				resolvedPlayer = receiver;
			} else {
				return "Player name is required";
			}
			int count = 0;
			int index = 0;
			String[] revisedArray = new String[commandArray.length -1];
			for (String commandElement : commandArray) {
				if (count != 1) {
					revisedArray[index] = commandElement;
					index++;
				}
				count++;
			}
			return playerManager.leap(player,resolvedPlayer,revisedArray);
		}
		
		if (cmd == HGCommand.LEAP) {
			return playerManager.leap(player,player,commandArray);
		}
		
		if (cmd == HGCommand.GATHER) {
			World world = null;
			Player resolvedPlayer = null;
			for (int i = 1 ; i < commandArray.length;i++) {
				if (resolvedPlayer == null) {
					resolvedPlayer = playerManager.resolvePlayer(commandArray[i]);
					if (resolvedPlayer != null) {
						continue;
					}
				}
				if (world == null) {
					world = plugin.getServer().getWorld(commandArray[i]);
					if (world != null) {
						continue;
					}
				}
				return ("Invalid arguments, unable to match to player or world name.");
			}
			if (resolvedPlayer != null) {
				msg.sendPositiveMessage(player, "Teleporting " + resolvedPlayer.getName() + " to you.");
				resolvedPlayer.teleportTo(player.getLocation());
				return null;
			}
			Player[] players = plugin.getServer().getOnlinePlayers();
			if (players.length > 1) {
				StringBuilder sb = new StringBuilder();
				sb.append("Teleporting ");
				int gathered = 0;
				for (Player toGather : players ) {
					if (!toGather.equals(player)) {
						if (world == null) {
							sb.append(toGather.getName());
							sb.append(" ");
							toGather.teleportTo(player.getLocation());
							gathered++;
						} else {
							if (player.getWorld().equals(world)) {
								sb.append(toGather.getName());
								sb.append(" ");
								toGather.teleportTo(player.getLocation());
								gathered++;
							}
						}
					}
				}
				sb.append("to you.");
				if (gathered > 0) {
					msg.sendPositiveMessage(player, sb.toString());
				} else {
					msg.sendNegativeMessage(player, "Unable to find any players to teleport to you.");
				}
				return null;
			} else {
				msg.sendNegativeMessage(player, "Unable to find any players to teleport to you.");
			}
		}

		if (cmd == HGCommand.SET_SPAWN) {
			World world = null;
			int x = 0;
			int y = 0;
			int z = 0;
			if (commandArray.length > 3) {
				try {
					x = Integer.parseInt(commandArray[1]);
					y = Integer.parseInt(commandArray[2]);
					z = Integer.parseInt(commandArray[3]);
				} catch (NumberFormatException e) {
					return msg.formatInvalidArgs(msg.concatinateRemainingArgs(commandArray, 1), "Not a valid coordinate triplet");
				}
			} else {
				x = player.getLocation().getBlockX();
				y = player.getLocation().getBlockY();
				z = player.getLocation().getBlockZ();
			}
			if (commandArray.length > 4) {
				world = plugin.getServer().getWorld(commandArray[4]);
				if (world == null) {
					return msg.formatInvalidArgs(commandArray[4], "Invalid world name");
				}
			}
			if (world == null) {
				world = player.getLocation().getWorld();
			}
			return worldManager.setSpawnLocation(player, world, x, y, z);
		}
		if (cmd == HGCommand.GET_ITEM || cmd == HGCommand.GIVE_ITEM || cmd == HGCommand.GET_TARGETED_BLOCK
				|| cmd == HGCommand.GIVE_TARGETED_BLOCK) {
			Player sender = player;
			Player receiver = player;
			int commandIndex = 1;
			String senderName = sender.getDisplayName();
			int itemNumber = -1;
			int itemQuantity = 1;
			int available = 0;
			byte itemData = -1;
			String receiverName;
			if (cmd == HGCommand.GIVE_ITEM || cmd == HGCommand.GIVE_TARGETED_BLOCK) {
			//PLAYER
				if (commandArray.length > commandIndex && commandArray[commandIndex] != null) {
					receiverName = commandArray[commandIndex];
					receiver = playerManager.resolvePlayer(receiverName);
					if (receiver == null) {
						return (new StringBuilder()).append(new StringBuilder("Invalid Player Name [")).append(receiverName).append("].").toString()
								.toString();
					}
					commandIndex++;
					receiverName = receiver.getDisplayName();
				} else {
					return msg.formatInvalidArgs("", "Player name required.");
				}
			} else {
				receiverName = sender.getDisplayName();
			}
			//ITEM BY TARGETED
			if (cmd == HGCommand.GIVE_TARGETED_BLOCK || cmd == HGCommand.GET_TARGETED_BLOCK) {
				Block targeted = ((CraftLivingEntity) player).getTargetBlock(null, 200);
				if (targeted != null) {
					itemNumber = targeted.getTypeId();
					itemData = targeted.getData();
				} else {
					return "Unable to determine target block.";
				}
			} else if (commandArray.length > commandIndex && commandArray[commandIndex] != null) {
				//ITEM BY NAME
				//better matching?
				Material mat = Material.matchMaterial(commandArray[commandIndex]);
				if (mat == null) {
					String[] parts = commandArray[commandIndex].replace('_', ' ').replace('.',' ').split(" ");
					StringBuilder regex = new StringBuilder();
					int i = 0;
					regex.append(".*(");
					String startString = new String();
					for (String part : parts){
						if (i > 0) {
							regex.append(".*");
						}
						regex.append(part.toLowerCase());
						startString += part.toLowerCase();
						i++;
					}
					regex.append(").*");
					int regexCount = 0;
					int startCount = 0;
					Material[] matRegexArray = new Material[20];
					Material[] matStartArray = new Material[20];
					for (Material possible : Material.values()) {
						if (regexCount > 19) {
							return msg.formatInvalidArgs(commandArray[commandIndex], "Too many matches, be more specific.");
						}
						if (possible.name().toLowerCase().matches(regex.toString())) {
							matRegexArray[regexCount] = possible;
							regexCount++;
						}
						if (possible.name().replaceAll("_","").toLowerCase().startsWith(startString)) {
							matStartArray[startCount] = possible;
							startCount++;
						}
					}
					if (regexCount == 1) {
						itemNumber = matRegexArray[0].getId();
						commandIndex++;
					} else if (startCount == 1) {
						itemNumber = matStartArray[0].getId();
						commandIndex++;
					} else if (regexCount > 1 || startCount > 1) {
						StringBuilder sb = new StringBuilder("Possible material matches are:");
						for (Material match : matRegexArray ) {
							if (match != null) {
								sb.append(" [");
								sb.append(match.name());
								sb.append("]");		
							}
						}sb.append(": Which contain the words, and :");
						for (Material match : matStartArray ) {
							if (match != null) {
								sb.append(" [");
								sb.append(match.name());
								sb.append("]");		
							}
						}sb.append(": Which start with the words.");
						return sb.toString();
					}
				} else {
					itemNumber = mat.getId();
					commandIndex++;
				}
				if (itemNumber == -1) {
				//ITEM BY ID
					try {
						itemNumber = Integer.parseInt(commandArray[commandIndex]);
						if (Material.getMaterial(itemNumber) == null) {
							return msg.formatInvalidArgs(commandArray[commandIndex], "Unable to determing material");
						} else {
							commandIndex++;
						}
					} catch (NumberFormatException e) {
						return msg.formatInvalidArgs(commandArray[commandIndex], "Unable to determing material");
					}
				}
			} else {
				return "Item number or name required.";
			}
			//QUANTITY
			if (commandArray.length > commandIndex && commandArray[commandIndex] != null) {
				try {
					itemQuantity = Integer.parseInt(commandArray[commandIndex]);
				} catch (NumberFormatException e) {
					return msg.formatInvalidArgs(commandArray[commandIndex], "Invalid item quantity");
				}
				commandIndex++;
			}
			if (itemQuantity > 2304) {
				itemQuantity = 2304;
			}
			

			if (commandArray.length > commandIndex && commandArray[commandIndex] != null) {
			//DATA
				try {
					itemData = Byte.parseByte(commandArray[commandIndex]);
				} catch (NumberFormatException e) {
					return (new StringBuilder()).append(new StringBuilder("Invalid Item Data[")).append(commandArray[commandIndex]).append("].")
							.toString().toString();
				}
				commandIndex++;
			}

			if (commandArray.length > commandIndex && commandArray[commandIndex] != null) {
				return msg.formatInvalidArgs(msg.concatinateRemainingArgs(commandArray, 1), "Too many arguments");
			}
			if (itemNumber != -1) {
			//AVAILABLE SPACE
				available = inventoryManager.getAvailableSpace(player, itemNumber);
				if (itemQuantity > available) {
					itemQuantity = available;
				}

				//ADDING INVENTORY
				int stacksNotAdded = inventoryManager.addItemToInventory(receiver, itemNumber, itemQuantity, itemData);
				StringBuffer message = new StringBuffer();
				if (senderName != receiverName) {
					msg.sendPositiveMessage(sender, (new StringBuilder()).append(new StringBuilder("Sent items to ")).append(receiverName).append(".")
							.toString().toString());
					message.append(senderName);
					message.append(" sent you ");
					message.append(itemQuantity);
				} else {
					message.append("Added ");
					message.append(itemQuantity);
				}
				message.append(" of item ");
				message.append(Material.getMaterial(itemNumber));
				message.append("[");
				message.append(itemNumber);
				message.append("]");
				if (stacksNotAdded != 0) {
					message.append(" (");
					message.append(stacksNotAdded);
					message.append(" stacks wouldn't fit)");
				}
				message.append(".");
				msg.sendPositiveMessage(receiver, message.toString());
			} else {
				return "Unable to determine item type.";
			}
			return null;
		}
		
		if (cmd == HGCommand.LIST_MATS) {
			if (commandArray.length > 1) {
				msg.sendSegmented(player,msg.getMaterialList(commandArray[1]));
			} else {
				return ("Partial material name required.");
			}
			return null;
		}
		
		if (cmd == HGCommand.COLOR_CHART) {
			msg.sendColorList(player);
			return null;
		}
		
		if (cmd == HGCommand.BOUNCE ||
				cmd == HGCommand.UNBOUNCE ||
				cmd == HGCommand.BOUNCE_IP ||
				cmd == HGCommand.UNBOUNCE ||
				cmd == HGCommand.FORCE_BOUNCE) {
			if (commandArray.length > 1 ) {
				return playerManager.bounce(player,cmd,commandArray);
			} else {
				return ("Invalid arguments.");
			}
		}
		
		if (cmd == HGCommand.LIST_BOUNCED) {
				msg.sendPositiveMessage(player, "Bounced: " + playerManager.getBouncedList());
		}
		
		if (cmd == HGCommand.RELOAD_BOUNCED) {
			playerManager.reloadBouncer();
			msg.sendPositiveMessage(player, "Reloaded bounce list. " + playerManager.getBouncedList());
		}
		
		if (cmd == HGCommand.WRITE_COMMAND_HTML) {
			try {
				config.writeFile(HGStatics.COMMAND_HTML_FILE,(config.writeCommandsToHtmlSimple()),HGStatics.OVERWRITE);
			} catch (IOException e) {
				msg.severe("Failed attempt to write file.");
				return ("Unable to write file.");
			}
			return null;
		} 
		
		if (cmd == HGCommand.WRITE_COMMAND_BUKKIT) {
			try {
				config.writeFile(HGStatics.COMMAND_BUKKIT_FILE,(config.writeCommandsToBukkit()),HGStatics.OVERWRITE);
			} catch (IOException e) {
				msg.severe("Failed attempt to write file.");
				return ("Unable to write file.");
			}
			return null;
		} 
		if (cmd == HGCommand.WRITE_COMMAND_YAML) {
			try {
				config.writeFile(HGStatics.COMMAND_YAML_FILE,(config.writePluginYaml()),HGStatics.OVERWRITE);
			} catch (IOException e) {
				msg.severe("Failed attempt to write file.");
				return ("Unable to write file.");
			}
			return null;
		} 
		if (cmd == HGCommand.WRITE_COMMAND_BUKKIT) {
			log.info(config.writeCommandsToBukkit());
			return null;
		}
		
		if (cmd == HGCommand.SAVE_PROPERTIES) {
			msg.sendPositiveMessage(player, "Saving command changes to properties files.");
			config.setCurrentConfigFileProperties();
			try {
				config.saveConfigFileProperties();
			} catch (IOException e) {
				e.printStackTrace();
				return ("Error writing properties file. See server console.");
			}
			return null;
		}
		
		if (cmd == HGCommand.RELOAD_PROPERTIES) {
			msg.sendPositiveMessage(player, "Reloading commands and applying properties files settings. Clearing all non-saved changes.");
			config.reloadCommandsFromPropertyFiles();
			return null;
		}
		
		if (cmd == HGCommand.SET_ALIAS) {
			return "Alias functionality temporarily removed";
/**
			if (commandArray.length > 1) {
				String command = commandArray[1].replaceAll("/", ""); 
				HGCommand commandToChange = HGCommand.getCommandDataByName(command);
				if (commandToChange !=null) {
					String newValue = new String("");
					if (commandArray.length > 2) {
						newValue = commandArray[2].replaceAll("/", "");
						HGCommand aliasCommand = HGCommand.getCommandDataByName(newValue);
						if (aliasCommand != null) {
							return msg.formatInvalidArgs(commandArray[2],"Name already in use");
						}
					}
					commandToChange.setCommandAlias(newValue);
					
//------------IFNDEF 440
					//++++++++++++++++++++++++++
					plugin.setCommandAlias(commandToChange.getDefaultCommand(),commandToChange.getCommandAlias());
//------------ENDIFNDEF 440

					HGCommand.reloadLookup();
					if (commandArray.length > 2) {
						msg.sendPositiveMessage(player,"Set alias of [/" + newValue + "] for command [/" + command + "].");
					} else {
						msg.sendPositiveMessage(player, "Removed alias for " + commandToChange.getCommand());
					}
					return null;
				} else {
					return msg.formatInvalidArgs(commandArray[1],"Unknown command");
				}
			} else {
				return "Invalid Arguments";
			}
*/
		}
		
		if (cmd == HGCommand.SET_SERVER_ALLOW) {
			if (commandArray.length > 2) {
				String command = commandArray[1].replaceAll("/", "");
				HGCommand commandToChange = HGCommandDAO.getCommandDataByName(command);
				if (commandToChange !=null) {
					String newValue = new String("");
					newValue = commandArray[2];
					if (newValue.equalsIgnoreCase("true") || newValue.equalsIgnoreCase("false")) {
						commandDAO.setServerAllowed(commandToChange,Boolean.parseBoolean(newValue));
						//HGCommand.reloadLookup();
					} else {
						return msg.formatInvalidArgs(newValue, "Invalid value");
					}
					msg.sendPositiveMessage(player,"Set server allow to " + newValue + " for command [/" + command + "].");
					return null;
				} else {
					return msg.formatInvalidArgs(commandArray[1],"Unknown command");
				}
			} else {
				return "Invalid Arguments";
			}
		}
		
		if (cmd == HGCommand.SET_OPS_ONLY) {
			if (commandArray.length > 2) {
				String command = commandArray[1].replaceAll("/", "");
				HGCommand commandToChange = HGCommandDAO.getCommandDataByName(command);
				if (commandToChange !=null) {
					String newValue = new String("");
					newValue = commandArray[2];
					if (newValue.equalsIgnoreCase("true") || newValue.equalsIgnoreCase("false")) {
						commandDAO.setOpsOnly(commandToChange,Boolean.parseBoolean(newValue));
						//HGCommand.reloadLookup();
					} else {
						return msg.formatInvalidArgs(newValue, "Invalid value");
					}
					msg.sendPositiveMessage(player,"Set ops only to " + newValue + " for command [/" + command + "].");
					return null;
				} else {
					return msg.formatInvalidArgs(commandArray[1],"Unknown command");
				}
			} else {
				return "Invalid Arguments";
			}
		}
		
		if (cmd == HGCommand.SET_PERMISSIONS) {
			if (commandArray.length > 1) {
				String command = commandArray[1].replaceAll("/", "");
				HGCommand commandToChange = HGCommandDAO.getCommandDataByName(command);
				if (commandToChange !=null) {
					String newValue = null;
					if (commandArray.length > 2) {
						newValue = commandArray[2];
					}
					commandDAO.setPermissions(commandToChange,newValue);
					commandDAO.reloadLookup();
					msg.sendPositiveMessage(player,"Set permission string of [" + newValue + "] for command [/" + command + "].");
					return null;
				} else {
					return msg.formatInvalidArgs(commandArray[1],"Unknown command");
				}
			} else {
				return "Invalid Arguments";
			}
		}

		
		
		
		
		// end of all commands
		return null;
	}
}
