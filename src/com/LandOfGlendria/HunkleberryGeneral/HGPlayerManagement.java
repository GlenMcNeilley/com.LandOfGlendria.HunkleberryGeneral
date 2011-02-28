package com.LandOfGlendria.HunkleberryGeneral;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class HGPlayerManagement {

	@SuppressWarnings("unused")
	private final Logger log = Logger.getLogger("Minecraft");
	private Plugin plugin;
	private HGMessageManagement msg;
	private HGBouncer bouncer;

	public HGPlayerManagement(Plugin plugin, HGMessageManagement msg, HGBouncer bouncer) {
		this.plugin = plugin;
		this.msg = msg;
		this.bouncer = bouncer;
	}

	public Player resolvePlayer(String name) {
		Player player = plugin.getServer().getPlayer(name);
		List<Player> playerList = plugin.getServer().matchPlayer(name);
		Player[] playerArray = plugin.getServer().getOnlinePlayers();
		if (player != null) {
			return player;
		} else if (playerList.size() == 1) {
			return playerList.get(0);
		} else if (playerArray.length > 0) {
			for (Player play : playerArray) {
				if (msg.stripColor(play.getDisplayName()).contains(name)) {
					return play;
				}
			}
		}
		return null;
	}

	public Player resolvePlayer(Player sender, String name) {
		Player player = plugin.getServer().getPlayer(name);
		List<Player> players = plugin.getServer().matchPlayer(name);
		if (player != null) {
			return player;
		} else if (players.size() == 1) {
			return players.get(0);
		} else {
			return sender;
		}
	}

	public String healPlayer(Player player) {
		player.setHealth(HGStatics.MAX_HEALTH.intValue());
		return null;
	}

	public String setDisplayName(Player player, Player sender, String name, String colorArray) {
		StringBuffer displayName = new StringBuffer();
		displayName.append("");
		displayName.append(HGStatics.NO_COLOR);
		displayName.append(name);
		int color = 0;
		int index = 0;
		int cumulative = 2;
		if (colorArray != null) {
			String as[];
			int j = (as = colorArray.split("/")).length;
			for (int i = 0; i < j; i++) {
				String indexAndColor = as[i];
				if (indexAndColor != null) {
					String pair[] = indexAndColor.split(",");
					if (pair.length == 2) {
						try {
							index = Integer.parseInt(pair[0]);
							if (index < 1 || index > name.length()) {
								return msg.formatInvalidArgs(index, "Index out of bounds");
							}
						} catch (NumberFormatException e) {
							return msg.formatInvalidArgs(pair[0], "Invalid index");
						}
						try {
							color = Integer.parseInt(pair[1]);
						} catch (NumberFormatException e) {
							return msg.formatInvalidArgs(pair[1], "Invalid color");
						}
						if (ChatColor.getByCode(color) != null) {
							displayName.insert((index + cumulative) - 1, ChatColor.getByCode(color));
							cumulative += 2;
						} else {
							return msg.formatInvalidArgs(color, "Invalid color");
						}
					} else {
						return msg.formatInvalidArgs(indexAndColor, "Invalid index.color pair");
					}
				}
			}

		}
		displayName.append(HGStatics.NO_COLOR);
		displayName.append("");
		player.setDisplayName(displayName.toString());
		if (player == sender) {
			msg.sendPositiveMessage(player, (new StringBuilder("Set display name to ")).append(displayName.toString()).toString());
		} else {
			msg.sendPositiveMessage(player, (sender.getName() + " has set your display name to " + displayName + "."));
			msg.sendPositiveMessage(sender, ("You have set " + player.getName() + "'s display name to " + displayName + "."));
		}
		return null;
	}

	public Block getFloor(Block block) {
		Block testingBlock = block;
		Location loc = null;
		int blockHeight = (int) block.getLocation().getY();
		for (int height = blockHeight; height > 3; height--) {
			loc = testingBlock.getLocation();
			loc.setY(height);
			testingBlock = block.getWorld().getBlockAt(loc);
			if (HGStatics.DangerBlocks.contains((Byte) ((byte) (testingBlock.getFace(BlockFace.DOWN).getTypeId())))) {
				return null;
			}
			if (!HGStatics.AirBlocks.contains((Byte) ((byte) (testingBlock.getFace(BlockFace.DOWN).getTypeId())))
					&& !HGStatics.DangerBlocks.contains((Byte) ((byte) (testingBlock.getFace(BlockFace.DOWN).getTypeId())))) {
				return testingBlock.getFace(BlockFace.UP);
			}
		}

		return null;
	}

	public String leap(Player sender, Player receiver, String[] commandArray) {

		Server server = plugin.getServer();

		if (receiver == null) {
			receiver = sender;
		}

		int argumentCount = 0;
		World leapWorld = null;
		Player leapPlayer = null;
		Double leapX = null;
		Integer leapY = null;
		Double leapZ = null;
		Location leapLocation = null;
		String argument = commandArray[0];
		for (int i = 1; i < commandArray.length; i++) {
			argument = commandArray[i];
			if (leapWorld == null) {
				leapWorld = server.getWorld(argument);
				if (leapWorld != null) {
					continue;
				}
			}
			if (leapPlayer == null && leapLocation == null) {
				leapPlayer = resolvePlayer(argument);
				if (leapPlayer != null) {
					leapLocation = leapPlayer.getLocation();
					leapWorld = leapLocation.getWorld();
					continue;
				}
			}
			if (leapLocation == null && commandArray.length - argumentCount >= 2) {
				try {
					leapX = (double) Integer.valueOf(Integer.parseInt(commandArray[i]));
					leapY = Integer.valueOf(Integer.parseInt(commandArray[i + 1]));
					leapZ = (double) Integer.valueOf(Integer.parseInt(commandArray[i + 2]));
				} catch (NumberFormatException e) {
					continue;
				}
				i += 3;
				leapLocation = new Location(sender.getWorld(), leapX.intValue(), leapY.intValue(), leapZ.intValue());
			} else {
				return msg.formatInvalidArgs(commandArray[i], "Invalid argument, not a player, world, or complete coordinate triplet");
			}
		}

		if (leapWorld == null) {
			leapWorld = sender.getWorld();
		}
		if (leapLocation == null) {
			CraftWorld cworld = (CraftWorld) leapWorld;
			net.minecraft.server.WorldServer wserver = cworld.getHandle();
			leapX = wserver.q.c() + .5;
			leapY = wserver.q.d();
			leapZ = wserver.q.e() + .5;
			leapLocation = new Location(leapWorld, leapX, leapY, leapZ);
		} else {
			leapLocation.setWorld(leapWorld);
		}

		// return playerManager.leap(player, leapLocation);

		if (sender == receiver) {

			msg.sendPositiveMessage(sender, ("Leaping to " + leapLocation.getWorld().getName() + " (" + (int) leapLocation.getX() + ","
					+ (int) leapLocation.getY() + "," + (int) leapLocation.getZ() + ")."));
		} else {
			msg.sendPositiveMessage(sender,
					("Flinging " + receiver.getName() + " to " + leapLocation.getWorld().getName() + " (" + (int) leapLocation.getX() + ","
							+ (int) leapLocation.getY() + "," + (int) leapLocation.getZ() + ")."));
			msg.sendPositiveMessage(receiver, ("You have been flung by " + sender.getName() + "."));
		}
		leapLocation.setPitch(sender.getLocation().getPitch());
		leapLocation.setYaw(sender.getLocation().getYaw());
		receiver.teleportTo(leapLocation);
		return null;
	}

	public String getLocation(Player player) {
		Location loc = player.getLocation();
		msg.sendPositiveMessage(player, (new StringBuilder("Loc: ")).append(loc.getX()).append(",").append(loc.getY()).append(",").append(loc.getZ())
				.append(").").toString());
		return null;
	}

	public void teleportToSpawn(Player player) {
		CraftWorld cworld = (CraftWorld) player.getWorld();
		net.minecraft.server.WorldServer wserver = cworld.getHandle();
		double dX = wserver.q.c() + .5;
		double dY = wserver.q.d();
		double dZ = wserver.q.e() + .5;
		msg.sendPositiveMessage(player, "Teleporting to spawn point.");
		player.teleportTo(new Location(player.getWorld(), dX, dY, dZ));
	}

	public String stratum(Player player, int strataToJump) {
		HashSet<Byte> blocks = new HashSet<Byte>();
		for (Material mat : Material.values()) {
			blocks.add(Byte.valueOf((byte) mat.getId()));
		}
		Location loc = null;
		if (strataToJump > 0) {
			List<Block> blockList = player.getLineOfSight(blocks, HGStatics.MAX_DISTANCE);
			int strataCount = 1;
			boolean solidFound = false;
			Iterator<Block> iterator = blockList.iterator();
			while (iterator.hasNext()) {
				Block block = (Block) iterator.next();
				if (block.getY() < 3 || block.getY() > 124) {
					break;
				}
				if (solidFound && HGStatics.AirBlocks.contains((Byte) ((byte) (block.getTypeId())))) {
					if (strataCount == strataToJump) {
						if (!HGStatics.AirBlocks.contains(Byte.valueOf((byte) block.getFace(BlockFace.UP).getTypeId()))) {
							continue;
						}
						block = getFloor(block);
						if (block != null) {
							loc = block.getFace(BlockFace.DOWN).getLocation();
						}
						if (loc != null) {
							break;
						}
					} else {
						solidFound = false;
						strataCount++;
					}
				} else if (!HGStatics.AirBlocks.contains((Byte) ((byte) (block.getTypeId())))
						&& !HGStatics.DangerBlocks.contains((Byte) ((byte) (block.getTypeId())))) {
					solidFound = true;
				}
			}
		} else {
			List<Block> blockList = player.getLineOfSight(HGStatics.AirBlocks, HGStatics.MAX_DISTANCE);
			Block block = null;
			for (int i = blockList.size() - 2; i > -1; i--) {
				block = blockList.get(i);
				block = getFloor(block);
				if (block != null) {
					loc = block.getFace(BlockFace.DOWN).getLocation();
				}
				if (loc != null) {
					break;
				}
			}
		}
		if (loc != null) {
			loc.setPitch(player.getLocation().getPitch());
			loc.setYaw(player.getLocation().getYaw());
			loc.setX(loc.getX() + .5);
			loc.setZ(loc.getZ() + .5);
			msg.sendPositiveMessage(player, "Air found, going there.");
			player.teleportTo(loc);
			return null;
		} else {
			return "Unable to find air.";
		}
	}

	public String getBouncedList() {
		@SuppressWarnings("rawtypes")
		ArrayList bouncedArray = bouncer.getFormattedArray();
		StringBuilder sb = new StringBuilder();
		for (@SuppressWarnings("rawtypes")
		Iterator it = bouncedArray.iterator(); it.hasNext();) {
			sb.append(HGStatics.NO_COLOR);
			sb.append("[");
			sb.append(HGStatics.WARNING_COLOR);
			sb.append((String) it.next());
			sb.append(HGStatics.NO_COLOR);
			sb.append("] ");
		}
		return sb.toString();
	}
	
	public void reloadBouncer() {
		bouncer.manageBouncerPropertyFiles();
	}

	public String bounce(Player player, HGCommandData command, String[] commandArray) {
		// validate the duration
		int argumentCount = 2;
		String bounceMessage = new String("");
		String durationString = new String("");
		String friendlyDurationString = new String("");
		// look for duration
		if (commandArray.length > argumentCount) {
			try {
				long duration = (Long.parseLong(commandArray[argumentCount])*60000)+System.currentTimeMillis();
				durationString = "" + duration;
				friendlyDurationString = new String(" until " + new Date(duration).toString());
				argumentCount++;
			} catch (NumberFormatException e) {
				// return msg.formatInvalidArgs(commandArray[2],
				// "Unable to parse duration");
				// do nothing must be message
			}
		} else {
			// bounce forever
			friendlyDurationString = new String(" for life.");
		}

		// look for message
		if (commandArray.length > argumentCount) {
			// concatinate message
			bounceMessage = new String(msg.concatinateRemainingArgs(commandArray, argumentCount));
		}

		// do what to whom
		if (command == HGCommandData.FORCE_BOUNCE) {
			try {
				bouncer.setBounced(commandArray[1], durationString);
				msg.sendPositiveMessage(player, "Bounced " + commandArray[1] + friendlyDurationString);
			} catch (IOException e) {
				e.printStackTrace();
				return ("Error accessing bouncer data.");
			}
		}

		if (command == HGCommandData.BOUNCE) {
			// validate player
			Player bouncee = plugin.getServer().getPlayer(commandArray[1]);
			if (bouncee != null) {
				try {
					bouncer.setBounced(bouncee.getName(), durationString);
					msg.sendPositiveMessage(player, "Bounced " + bouncee.getName() + friendlyDurationString);
					bouncee.kickPlayer(bounceMessage);
				} catch (IOException e) {
					e.printStackTrace();
					return ("Error accessing bouncer data.");
				}
			} else {
				return msg.formatInvalidArgs(commandArray[1], "Unable to determine player");
			}
		}

		if (command == HGCommandData.BOUNCE_IP) {
			// validate ip
			Player bouncee = plugin.getServer().getPlayer(commandArray[1]);
			if (bouncee != null) {
				String bounceeIp = bouncee.getAddress().getAddress().getHostAddress();
				StringBuilder bounced = new StringBuilder();
				try {
					bouncer.setBounced(bounceeIp, durationString);
					for (Player bounceApplicant : plugin.getServer().getOnlinePlayers()) {
						if (bounceApplicant.getAddress().getAddress().getHostAddress().equals(bounceeIp)) {
							bounced.append(HGStatics.NO_COLOR);
							bounced.append("[");
							bounced.append(HGStatics.WARNING_COLOR);
							bounced.append(bounceApplicant.getName());
							bounced.append(HGStatics.NO_COLOR);
							bounced.append("] ");
							bouncer.setBounced(bounceeIp, durationString);
							bounceApplicant.kickPlayer(bounceMessage);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					return ("Error accessing bouncer data.");
				}

				msg.sendPositiveMessage(player, "Bounced " + bounced.toString() + friendlyDurationString);
			} else {
				return msg.formatInvalidArgs(commandArray[1], "Unable to determine ip by player name");
			}
		}

		if (command == HGCommandData.UNBOUNCE) {
			try {
				if (bouncer.setUnBounced(commandArray[1])) {
					msg.sendPositiveMessage(player, "Unbounced " + commandArray[1]);
				} else {
					return msg.formatInvalidArgs(commandArray[1], "Entry not found in bounced list");
				}
			} catch (IOException e) {
				e.printStackTrace();
				return ("Error accessing bouncer data.");
			}
		}
		
		return null;
	}
}
