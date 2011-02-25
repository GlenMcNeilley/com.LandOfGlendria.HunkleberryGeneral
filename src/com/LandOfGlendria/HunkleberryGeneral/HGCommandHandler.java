package com.LandOfGlendria.HunkleberryGeneral;

import java.io.IOException;
import java.util.logging.Logger;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class HGCommandHandler {

	private final Logger log = Logger.getLogger("Minecraft");
	private HGConfig config;
	private HGMessageManagement msg;
	private HGInventoryManagement inventoryManager;
	private HGPlayerManagement playerManager;
	private HGWorldManagement worldManager;
	private HGTimeManagement timeManager;
	private HGPluginManagement pluginManager;
	private Plugin plugin;

	public HGCommandHandler(Plugin plugin, HGMessageManagement msg) {
		this.plugin = plugin;
		this.msg = msg;
		config = HunkleberryGeneral.config;
		pluginManager = new HGPluginManagement(plugin,msg);
		playerManager = new HGPlayerManagement(plugin,msg);
		inventoryManager = new HGInventoryManagement(msg);
		worldManager = new HGWorldManagement(plugin, msg);
		timeManager = new HGTimeManagement(msg);
	}

	public String handleCommand(Player player, HGCommandData cmd, String commandArray[]) {
		if (cmd == HGCommandData.HG_HELP) {
			if (commandArray.length > 1) {
				boolean allowedToUse = true;
				HGCommandData commandForHelp = HGCommandData.getCommandDataByName(commandArray[1].replaceAll("/", ""));
				if (commandForHelp == null) {
					return msg.formatInvalidArgs(commandArray[1], "Unknown command");
				}
				if (!player.isOp() && commandForHelp.getOpsOnly().booleanValue()) {
					if (HGConfig.permissions == null) {
						allowedToUse = false;
					} else if (!HGConfig.permissions.has(player, commandForHelp.getPermissions())) {
						allowedToUse = false;
					}
				}
				msg.sendColorHelp(player, commandForHelp, allowedToUse);
			} else {
				msg.sendCommandList(player,cmd);
			}
			return null;
		}
		
		if (cmd == HGCommandData.LOAD_PLUGIN) {
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
		if (cmd == HGCommandData.ENABLE_PLUGIN) {
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
		if (cmd == HGCommandData.DISABLE_PLUGIN) {
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
		if (cmd == HGCommandData.CLEAR_PLUGINS) {
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
		if (cmd == HGCommandData.LIST_PLUGINS) {
			pluginManager.listPlugins(player);
			return null;
		}
		if (cmd == HGCommandData.CLEAR_INVENTORY) {
			Player resolvedPlayer;
			if (commandArray.length > 1) {
				String receiverName = commandArray[1];
				Player receiver = plugin.getServer().getPlayer(receiverName);
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
		if (cmd == HGCommandData.CLEAR_INVENTORY_ALL) {
			Player resolvedPlayer;
			if (commandArray.length > 1) {
				String receiverName = commandArray[1];
				Player receiver = plugin.getServer().getPlayer(receiverName);
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
		if (cmd == HGCommandData.LIST_INVENTORY) {
			Player resolvedPlayer;
			if (commandArray.length > 1) {
				String receiverName = commandArray[1];
				Player receiver = plugin.getServer().getPlayer(receiverName);
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
		if (cmd == HGCommandData.HEAL_PLAYER) {
			Player resolvedPlayer = player;
			if (commandArray.length > 1) {
				String receiverName = commandArray[1];
				Player receiver = plugin.getServer().getPlayer(receiverName);
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
				msg.sendPositiveMessage(player, (new StringBuilder("Healed by ")).append(player.getDisplayName()).toString());
				msg.sendPositiveMessage(player, (new StringBuilder("Healed ")).append(resolvedPlayer.getDisplayName()).append(".").toString());
			} else {
				msg.sendPositiveMessage(player, "Fully healed.");
			}
			return null;
		}
		if (cmd == HGCommandData.SLAY_LIVING) {
			worldManager.removeNonPlayerEntities(player);
			return null;
		}
		if (cmd == HGCommandData.REMOVE_DROPS) {
			worldManager.removeItemDropEntities(player);
			return null;
		}
		if (cmd == HGCommandData.SET_DISPLAY_NAME) {
			//first arg must be player name
			String newName = null;
			String indexAndColors = null;
			Player receiver = null;
			if (commandArray.length > 1) {
				receiver = plugin.getServer().getPlayer(commandArray[1]);
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
			return playerManager.setDisplayName(player, newName, indexAndColors);
		}

		if (cmd == HGCommandData.LOC) {
			msg.sendPositiveMessage(player, playerManager.getLocation(player));
		}
		if (cmd == HGCommandData.STRATUM) {
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
		
		if (cmd == HGCommandData.MOTD) {
			if (commandArray.length > 1) {
				config.readMotd();
			}
			if (HGStatics.MOTD_STRING != null) {
				msg.parseMotdForPlayer(player,new String(HGStatics.MOTD_STRING));
			}
			return null;
		}

		if (cmd == HGCommandData.GET_TIME) {
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

		if (cmd == HGCommandData.SET_TIME) {
			int index = 1;
			Player resolvedPlayer;
			if (commandArray.length > index) {
				String receiverName = commandArray[index];
				Player receiver = plugin.getServer().getPlayer(receiverName);
				if (receiver == null) {
					return msg.formatInvalidArgs(receiverName, "Invalid player name");
				}
				resolvedPlayer = receiver;
				index++;
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
		
		if (cmd == HGCommandData.LIST_WORLDS) {
			worldManager.listWorlds(player);
			return null;
		}
		
		if (cmd == HGCommandData.CREATE_WORLD ) {
			if (commandArray.length != 3) {
				return "Invalid Arguments";
			} else {
				return worldManager.worldLoader(player,cmd,commandArray[1],commandArray[2]);
			}
		}
		
		if (cmd == HGCommandData.LOAD_WORLD) {
			if (commandArray.length != 2) {
				return "Invalid Arguments";
			} else {
				return worldManager.worldLoader(player,cmd,commandArray[1],null);
			}
		}
		
		if (cmd == HGCommandData.DECLARE_WORLD_ENV) {
			if (commandArray.length != 3) {
				return "Invalid Arguments";
			} else {
				return worldManager.worldLoader(player, cmd, commandArray[1], commandArray[2]);
			}
		}
			
		if (cmd == HGCommandData.FLING) {
			//first arg must be player
			Player resolvedPlayer;
			if (commandArray.length > 1) {
				String receiverName = commandArray[1];
				Player receiver = plugin.getServer().getPlayer(receiverName);
				if (receiver == null) {
					return msg.formatInvalidArgs(receiverName, "Invalid player name");
				}
				resolvedPlayer = receiver;
			} else {
				return "Player name is required.";
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
		
		if (cmd == HGCommandData.LEAP) {
			return playerManager.leap(player,player,commandArray);
		}
		
		if (cmd == HGCommandData.SET_SPAWN) {
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
		if (cmd == HGCommandData.GET_ITEM || cmd == HGCommandData.GIVE_ITEM || cmd == HGCommandData.GET_TARGETED_BLOCK
				|| cmd == HGCommandData.GIVE_TARGETED_BLOCK) {
			Player sender = player;
			Player receiver = player;
			int commandIndex = 1;
			String senderName = sender.getDisplayName();
			int itemNumber = -1;
			int itemQuantity = 1;
			int available = 0;
			byte itemData = -1;
			String receiverName;
			if (cmd == HGCommandData.GIVE_ITEM || cmd == HGCommandData.GIVE_TARGETED_BLOCK) {
			//PLAYER
				if (commandArray.length > commandIndex && commandArray[commandIndex] != null) {
					receiverName = commandArray[commandIndex];
					receiver = plugin.getServer().getPlayer(receiverName);
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
			if (cmd == HGCommandData.GIVE_TARGETED_BLOCK || cmd == HGCommandData.GET_TARGETED_BLOCK) {
				Block targeted = ((CraftLivingEntity) player).getTargetBlock(null, 200);
				if (targeted != null) {
					itemNumber = targeted.getTypeId();
					itemData = targeted.getData();
				} else {
					return "Unable to determine target block.";
				}
			} else if (commandArray.length > commandIndex && commandArray[commandIndex] != null) {
			//ITEM BY ID
				try {
					itemNumber = Integer.parseInt(commandArray[commandIndex]);
					if (Material.getMaterial(itemNumber) == null) {
						return msg.formatInvalidArgs(commandArray[commandIndex], "Invalid item number");
					}
				} catch (NumberFormatException e) {
					return msg.formatInvalidArgs(commandArray[commandIndex], "Invalid item number");
				}
				commandIndex++;
			} else {
				return "Item number required.";
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
		
		if (cmd == HGCommandData.COLOR_CHART) {
			log.info(msg.sendColorList(player));
			return null;
		}
		
		if (cmd == HGCommandData.WRITE_COMMAND_HTML) {
			try {
				config.writeFile(HGStatics.COMMAND_HTML_FILE,(config.writeCommandsToHtmlSimple()),HGStatics.OVERWRITE);
			} catch (IOException e) {
				msg.severe("Failed attempt to write file.");
				return ("Unable to write file.");
			}
			return null;
		} 
		
		if (cmd == HGCommandData.WRITE_COMMAND_BUKKIT) {
			log.info(config.writeCommandsToBukkit());
			return null;
		}
		
		if (cmd == HGCommandData.SAVE_PROPERTIES) {
			msg.sendPositiveMessage(player, "Saving command changes to properties files.");
			config.setCurrentConfigFileProperties();
			config.saveConfigFileProperties();
			return null;
		}
		
		if (cmd == HGCommandData.RELOAD_PROPERTIES) {
			msg.sendPositiveMessage(player, "Reloading commands and applying properties files settings");
			config.managePropertyFiles();
			return null;
		}
		
		if (cmd == HGCommandData.SET_ALIAS) {
			if (commandArray.length > 1) {
				String command = commandArray[1].replaceAll("/", "");
				HGCommandData commandToChange = HGCommandData.getCommandDataByName(command);
				if (commandToChange !=null) {
					String newValue = new String("");
					if (commandArray.length > 2) {
						newValue = commandArray[2].replaceAll("/", "");
						HGCommandData aliasCommand = HGCommandData.getCommandDataByName(newValue);
						if (aliasCommand != null) {
							return msg.formatInvalidArgs(commandArray[2],"Name already in use");
						}
					}
					commandToChange.setCommandAlias(newValue);
					HGCommandData.reloadLookup();
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
		}
		
		if (cmd == HGCommandData.SET_SERVER_ALLOW) {
			if (commandArray.length > 2) {
				String command = commandArray[1].replaceAll("/", "");
				HGCommandData commandToChange = HGCommandData.getCommandDataByName(command);
				if (commandToChange !=null) {
					String newValue = new String("");
					newValue = commandArray[2];
					if (newValue.equalsIgnoreCase("true") || newValue.equalsIgnoreCase("false")) {
						commandToChange.setServerAllowed(Boolean.parseBoolean(newValue));
						//HGCommandData.reloadLookup();
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
		
		if (cmd == HGCommandData.SET_OPS_ONLY) {
			if (commandArray.length > 2) {
				String command = commandArray[1].replaceAll("/", "");
				HGCommandData commandToChange = HGCommandData.getCommandDataByName(command);
				if (commandToChange !=null) {
					String newValue = new String("");
					newValue = commandArray[2];
					if (newValue.equalsIgnoreCase("true") || newValue.equalsIgnoreCase("false")) {
						commandToChange.setOpsOnly(Boolean.parseBoolean(newValue));
						//HGCommandData.reloadLookup();
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
		
		if (cmd == HGCommandData.SET_PERMISSIONS) {
			if (commandArray.length > 1) {
				String command = commandArray[1].replaceAll("/", "");
				HGCommandData commandToChange = HGCommandData.getCommandDataByName(command);
				if (commandToChange !=null) {
					String newValue = null;
					if (commandArray.length > 2) {
						newValue = commandArray[2];
					}
					commandToChange.setPermissions(newValue);
					HGCommandData.reloadLookup();
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
