package com.LandOfGlendria.HunkleberryGeneral;

import java.util.logging.Logger;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public class HGCommandHandler
{

	private final Logger log = Logger.getLogger("Minecraft");
	private HGConfig config;
	private HGMessageManagement msg;
	private HGInventoryManagement inventoryManager;
	private HGPlayerManagement playerManager;
	private HGWorldManagement worldManager;
	private HGTimeManagement timeManager;
	private HGPluginManagement pluginManager;
	private Plugin plugin;

	public HGCommandHandler(Plugin plugin, HGMessageManagement msg)
	{
		this.plugin = plugin;
		this.msg = msg;
		config = HunkleberryGeneral.config;
		pluginManager = new HGPluginManagement(plugin);
		playerManager = new HGPlayerManagement(msg);
		inventoryManager = new HGInventoryManagement();
		worldManager = new HGWorldManagement(plugin, msg);
		timeManager = new HGTimeManagement();
	}

	public String handleCommand(Player player, HGCommandData cmd, String commandArray[])
	{
		if(cmd == HGCommandData.HG_HELP)
		{
			if(commandArray.length > 1)
			{
				boolean allowedToUse = true;
				HGCommandData commandForHelp = HGCommandData.getCommandDataByName(commandArray[1]);
				if(commandForHelp == null)
				{
					return msg.formatInvalidArgs(commandArray[1], "Unknown command");
				}
				if(!player.isOp() && commandForHelp.getOpsOnly().booleanValue())
				{
					if(HGConfig.permissions == null)
					{
						allowedToUse = false;
					} else
					if(!HGConfig.permissions.has(player, commandForHelp.getPermissions()))
					{
						allowedToUse = false;
					}
				}
				msg.sendColorHelp(player, commandForHelp, allowedToUse);
			}
			return null;
		}
		if(cmd == HGCommandData.LOAD_PLUGIN)
		{
			if(commandArray.length > 1)
			{
				String name = msg.concatinateRemainingArgs(commandArray, 1);
				String message = pluginManager.loadPlugin(name);
				if(message != null)
				{
					return message;
				} else
				{
					msg.sendPositiveMessage(player, (new StringBuilder(String.valueOf(commandArray[1]))).append(" is loaded. It must now be enabled.").toString());
					return null;
				}
			} else
			{
				return "Plugin path required.";
			}
		}
		if(cmd == HGCommandData.ENABLE_PLUGIN)
		{
			String name = null;
			if(commandArray.length > 1)
			{
				name = msg.concatinateRemainingArgs(commandArray, 1);
				String message = pluginManager.enablePlugin(name);
				if(message != null)
				{
					return message;
				} else
				{
					msg.sendPositiveMessage(player, (new StringBuilder(String.valueOf(name))).append(" is being enabled.").toString());
					return null;
				}
			} else
			{
				return "Plugin name required.";
			}
		}
		if(cmd == HGCommandData.DISABLE_PLUGIN)
		{
			String name = null;
			if(commandArray.length > 1)
			{
				name = msg.concatinateRemainingArgs(commandArray, 1);
				String message = pluginManager.disablePlugin(name);
				if(message != null)
				{
					return message;
				} else
				{
					log.info((new StringBuilder(String.valueOf(HunkleberryGeneral.pdfFile.getName()))).append(": ").append(player.getDisplayName()).append(" is disabling plugin ").append(name).append(".").toString());
					msg.sendPositiveMessage(player, (new StringBuilder(String.valueOf(name))).append(" is being disabled.").toString());
					return null;
				}
			} else
			{
				return "Plugin name required.";
			}
		}
		if(cmd == HGCommandData.CLEAR_PLUGINS)
		{
			log.info((new StringBuilder(String.valueOf(HunkleberryGeneral.pdfFile.getName()))).append(": ").append(player.getDisplayName()).append(" is disabling and clearing all plugins.").toString());
			log.info("Use /reload to re-enable all plugins.");
			pluginManager.clearAllPlugins();
			msg.sendPositiveMessage(player, (new StringBuilder("Disabling and clearing all plugins. Use ")).append(HGStatics.WARNING_COLOR).append("/reload").append(HGStatics.POSITIVE_COLOR).append(" to re-enable all plugins.").toString());
		}
		if(cmd == HGCommandData.LIST_PLUGINS)
		{
			String message[] = pluginManager.listPlugins();
			msg.sendSegmented(player, message[0]);
			msg.sendSegmented(player, message[1]);
			return null;
		}
		if(cmd == HGCommandData.CLEAR_INVENTORY)
		{
			inventoryManager.clearPlayerInventory(player);
			msg.sendPositiveMessage(player, "Cleared inventory.");
			return null;
		}
		if(cmd == HGCommandData.CLEAR_INVENTORY_ALL)
		{
			inventoryManager.clearEntirePlayerInventory(player);
			msg.sendPositiveMessage(player, "Cleared entire inventory.");
			return null;
		}
		if(cmd == HGCommandData.LIST_INVENTORY)
		{
			inventoryManager.listPlayerInventory(player);
			return null;
		}
		if(cmd == HGCommandData.HEAL_PLAYER)
		{
			Player resolvedPlayer = player;
			if(commandArray.length > 1)
			{
				String receiverName = commandArray[1];
				Player receiver = plugin.getServer().getPlayer(receiverName);
				if(receiver == null)
				{
					return (new StringBuilder("Invalid Player Name: ")).append(receiverName).append(".").toString();
				}
				resolvedPlayer = receiver;
			}
			String message = playerManager.healPlayer(resolvedPlayer);
			if(message != null)
			{
				return message;
			}
			if(player != resolvedPlayer)
			{
				msg.sendPositiveMessage(player, (new StringBuilder("Healed by ")).append(player.getDisplayName()).toString());
				msg.sendPositiveMessage(player, (new StringBuilder("Healed ")).append(resolvedPlayer.getDisplayName()).append(".").toString());
			} else
			{
				msg.sendPositiveMessage(player, "Fully healed.");
			}
			return null;
		}
		if(cmd == HGCommandData.SLAY_LIVING)
		{
			worldManager.removeNonPlayerEntities(player);
			return null;
		}
		if(cmd == HGCommandData.REMOVE_DROPS)
		{
			worldManager.removeItemDropEntities(player);
			return null;
		}
		if(cmd == HGCommandData.SET_DISPLAY_NAME)
		{
			String newName = null;
			String indexAndColors = null;
			if(commandArray.length > 1)
			{
				newName = commandArray[1];
				if(commandArray.length > 2)
				{
					indexAndColors = commandArray[2];
				}
			} else
			{
				newName = "";
			}
			return playerManager.setDisplayName(player, newName, indexAndColors);
		}
		if(cmd == HGCommandData.TELEPORT)
		{
			double x = 0.0D;
			double y = 0.0D;
			double z = 0.0D;
			if(commandArray.length > 3)
			{
				try
				{
					x = Double.parseDouble(commandArray[1]);
					y = Double.parseDouble(commandArray[2]);
					z = Double.parseDouble(commandArray[3]);
				}
				catch(NumberFormatException e)
				{
					return msg.formatInvalidArgs((new StringBuilder(String.valueOf(commandArray[1]))).append(" ").append(commandArray[1]).append(" ").append(commandArray[1]).toString(), "Invalid coordinate values");
				}
				return playerManager.teleport(player, x, y, z);
			} else
			{
				return msg.formatInvalidArgs(msg.concatinateRemainingArgs(commandArray, 1), "Not a valid coordinate triplet");
			}
		}
		if(cmd == HGCommandData.LOC)
		{
			msg.sendPositiveMessage(player, playerManager.getLocation(player));
		}
		if(cmd == HGCommandData.STRATUM)
		{
			int strataToJump = 1;
			if(commandArray.length > 1)
			{
				try
				{
					strataToJump = Integer.parseInt(commandArray[1]);
				}
				catch(NumberFormatException e)
				{
					return (new StringBuilder()).append(new StringBuilder("Invalid strata count: [")).append(commandArray[1]).append("].").toString().toString();
				}
				if(strataToJump < 1)
				{
					return msg.formatInvalidArgs(commandArray[1], "Invalid strata count. Must be greater than Zero");
				}
			}
			return playerManager.stratum(player, strataToJump);
		}
		if(cmd == HGCommandData.GETTIME)
		{
			timeManager.displayWorldTime(player);
			return null;
		}
		if(cmd == HGCommandData.SETTIME)
		{
			Player resolvedPlayer = player;
			String resolvedPlayerName = player.getDisplayName();
			int time = 6000;
			if(commandArray.length > 1)
			{
				if(commandArray[1].equalsIgnoreCase("sunrise"))
				{
					time = 22500;
				} else
				if(commandArray[1].equalsIgnoreCase("sunset"))
				{
					time = 12000;
				} else
				if(commandArray[1].equalsIgnoreCase("noon"))
				{
					time = 6000;
				} else
				if(commandArray[1].equalsIgnoreCase("midnight"))
				{
					time = 18000;
				} else
				{
					try
					{
						time = Integer.parseInt(commandArray[1]);
					}
					catch(NumberFormatException e)
					{
						return msg.formatInvalidArgs(commandArray[1], "Invalid time value");
					}
				}
			}
			if(commandArray.length > 2)
			{
				String receiverName = commandArray[2];
				Player receiver = plugin.getServer().getPlayer(receiverName);
				if(receiver == null)
				{
					return msg.formatInvalidArgs(receiverName, "Invalid Player Name");
				}
				resolvedPlayer = receiver;
				resolvedPlayerName = receiver.getDisplayName();
				player.sendMessage((new StringBuilder("Setting time for world of ")).append(resolvedPlayerName).append(".").toString());
			}
			timeManager.setWorldTime(resolvedPlayer, time);
			return null;
		}
		if(cmd == HGCommandData.LIST_WORLDS)
		{
			worldManager.listWorlds(player);
			return null;
		}
		if((cmd == HGCommandData.LOAD_WORLD || cmd == HGCommandData.CREATE_WORLD) && commandArray.length != 3)
		{
			return "Invalid arguments.";
		}
		if(cmd == HGCommandData.LEAP)
		{
			int argumentCount = 0;
			World leapWorld = null;
			Player leapPlayer = null;
			Integer leapX = null;
			Integer leapY = null;
			Integer leapZ = null;
			Location leapLocation = null;
			String argument = commandArray[0];
			for(int i = 1; i < commandArray.length; i++)
			{
				argument = commandArray[i];
				if(leapWorld == null)
				{
					leapWorld = plugin.getServer().getWorld(argument);
					if(leapWorld != null)
					{
						continue;
					}
				}
				if(leapPlayer == null && leapLocation == null)
				{
					leapPlayer = plugin.getServer().getPlayer(argument);
					if(leapPlayer != null)
					{
						leapLocation = leapPlayer.getLocation();
						continue;
					}
				}
				if(leapLocation == null && commandArray.length - argumentCount >= 2)
				{
					try
					{
						leapX = Integer.valueOf(Integer.parseInt(commandArray[i]));
						leapY = Integer.valueOf(Integer.parseInt(commandArray[i + 1]));
						leapZ = Integer.valueOf(Integer.parseInt(commandArray[i + 2]));
					}
					catch(NumberFormatException e)
					{
						continue;
					}
					i += 3;
					leapLocation = new Location(player.getWorld(), leapX.intValue(), leapY.intValue(), leapZ.intValue());
				} else
				{
					return msg.formatInvalidArgs(commandArray[i], "Invalid argument, not a player, world, or element of complete coordinate triplet");
				}
			}

			if(leapWorld == null)
			{
				leapWorld = player.getWorld();
			}
			if(leapLocation == null)
			{
				leapX = Integer.valueOf(((CraftWorld)leapWorld).getHandle().spawnX);
				leapY = Integer.valueOf(((CraftWorld)leapWorld).getHandle().spawnY);
				leapZ = Integer.valueOf(((CraftWorld)leapWorld).getHandle().spawnZ);
				leapLocation = new Location(leapWorld, leapX.intValue(), leapY.intValue(), leapZ.intValue());
			} else
			{
				leapLocation.setWorld(leapWorld);
			}
			return worldManager.leap(player, leapLocation);
		}
		if(cmd == HGCommandData.SETSPAWN)
		{
			World world = null;
			int x = 0;
			int y = 0;
			int z = 0;
			if(commandArray.length > 3)
			{
				try
				{
					x = Integer.parseInt(commandArray[1]);
					y = Integer.parseInt(commandArray[2]);
					z = Integer.parseInt(commandArray[3]);
				}
				catch(NumberFormatException e)
				{
					return msg.formatInvalidArgs(msg.concatinateRemainingArgs(commandArray, 1), "Not a valid coordinate triplet");
				}
			} else
			{
				x = player.getLocation().getBlockX();
				y = player.getLocation().getBlockY();
				z = player.getLocation().getBlockZ();
			}
			if(commandArray.length > 4)
			{
				world = plugin.getServer().getWorld(commandArray[4]);
				if(world == null)
				{
					return msg.formatInvalidArgs(commandArray[4], "Invalid world name");
				}
			}
			if(world == null)
			{
				world = player.getLocation().getWorld();
			}
			return worldManager.setSpawnLocation(player, world, x, y, z);
		}
		if(cmd == HGCommandData.GET_ITEM || cmd == HGCommandData.GIVE_ITEM || cmd == HGCommandData.GET_TARGETED_BLOCK || cmd == HGCommandData.GIVE_TARGETED_BLOCK)
		{
			Player sender = player;
			Player receiver = player;
			int commandIndex = 1;
			String senderName = sender.getDisplayName();
			int itemNumber = -1;
			int itemQuantity = 1;
			int available = 0;
			byte itemData = 0;
			String receiverName;
			if(cmd == HGCommandData.GIVE_ITEM || cmd == HGCommandData.GIVE_TARGETED_BLOCK)
			{
				if(commandArray.length > commandIndex && commandArray[commandIndex] != null)
				{
					receiverName = commandArray[commandIndex];
					receiver = plugin.getServer().getPlayer(receiverName);
					if(receiver == null)
					{
						return (new StringBuilder()).append(new StringBuilder("Invalid Player Name [")).append(receiverName).append("].").toString().toString();
					}
					commandIndex++;
					receiverName = receiver.getDisplayName();
				} else
				{
					return msg.formatInvalidArgs("", "Player name required.");
				}
			} else
			{
				receiverName = sender.getDisplayName();
			}
			if(cmd == HGCommandData.GIVE_TARGETED_BLOCK || cmd == HGCommandData.GET_TARGETED_BLOCK)
			{
				Block targeted = ((CraftLivingEntity)player).getTargetBlock(null, 200);
				if(targeted != null)
				{
					itemNumber = targeted.getTypeId();
					itemData = targeted.getData();
				} else
				{
					return "Unable to determine target block.";
				}
			} else
			if(commandArray.length > commandIndex && commandArray[commandIndex] != null)
			{
				try
				{
					itemNumber = Integer.parseInt(commandArray[commandIndex]);
					if(Material.getMaterial(itemNumber) == null)
					{
						return msg.formatInvalidArgs(commandArray[commandIndex], "Invalid item number");
					}
				}
				catch(NumberFormatException e)
				{
					return msg.formatInvalidArgs(commandArray[commandIndex], "Invalid item number");
				}
				commandIndex++;
			} else
			{
				return "Item number required.";
			}
			if(commandArray.length > commandIndex && commandArray[commandIndex] != null)
			{
				try
				{
					itemQuantity = Integer.parseInt(commandArray[commandIndex]);
				}
				catch(NumberFormatException e)
				{
					return msg.formatInvalidArgs(commandArray[commandIndex], "Invalid item quantity");
				}
				commandIndex++;
			}
			if(itemQuantity > 2304)
			{
				itemQuantity = 2304;
			}
			if(commandArray.length > commandIndex && commandArray[commandIndex] != null)
			{
				try
				{
					itemData = Byte.parseByte(commandArray[commandIndex]);
				}
				catch(NumberFormatException e)
				{
					return (new StringBuilder()).append(new StringBuilder("Invalid Item Data[")).append(commandArray[commandIndex]).append("].").toString().toString();
				}
				commandIndex++;
			}
			if(commandArray.length > commandIndex && commandArray[commandIndex] != null)
			{
				return msg.formatInvalidArgs(msg.concatinateRemainingArgs(commandArray, 1), "Too many arguments");
			}
			if(itemNumber != -1)
			{
				available = inventoryManager.getAvailableSpace(player, itemNumber, itemData);
				if(itemQuantity > available)
				{
					itemQuantity = available;
				}
				int stacksNotAdded = inventoryManager.addItemToInventory(receiver, itemNumber, itemQuantity, itemData);
				StringBuffer message = new StringBuffer();
				if(senderName != receiverName)
				{
					msg.sendPositiveMessage(sender, (new StringBuilder()).append(new StringBuilder("Sent items to ")).append(receiverName).append(".").toString().toString());
					message.append(senderName);
					message.append(" sent you ");
					message.append(itemQuantity);
				} else
				{
					message.append("Added ");
					message.append(itemQuantity);
				}
				message.append(" of item ");
				message.append(Material.getMaterial(itemNumber));
				message.append("[");
				message.append(itemNumber);
				message.append("]");
				if(stacksNotAdded != 0)
				{
					message.append(" (");
					message.append(stacksNotAdded);
					message.append(" stacks wouldn't fit)");
				}
				message.append(".");
				msg.sendPositiveMessage(receiver, message.toString());
			} else
			{
				return "Unable to determine item type.";
			}
			return null;
		}
		if(cmd == HGCommandData.WRITE_COMMAND_HTML)
		{
			log.info(config.writeCommandsToHtml());
			return null;
		} else
		{
			return null;
		}
	}
}
