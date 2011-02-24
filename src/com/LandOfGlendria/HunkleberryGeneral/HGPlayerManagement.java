package com.LandOfGlendria.HunkleberryGeneral;

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

	public HGPlayerManagement(Plugin plugin, HGMessageManagement msg) {
		this.plugin = plugin;
		this.msg = msg;
	}

	public String healPlayer(Player player) {
		player.setHealth(HGStatics.MAX_HEALTH.intValue());
		return null;
	}

	public String setDisplayName(Player player, String name, String colorArray) {
		StringBuffer displayName = new StringBuffer();
		displayName.append("");
		displayName.append(HGStatics.NO_COLOR);
		displayName.append(name);
		int color = 0;
		int index = 0;
		int cumulative = 3;
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
		displayName.append(" ");
		player.setDisplayName(displayName.toString());
		msg.sendPositiveMessage(player, (new StringBuilder("Set display name to ")).append(displayName.toString()).toString());
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
			if (HGStatics.DangerBlocks.contains((Byte)((byte)(testingBlock.getFace(BlockFace.DOWN).getTypeId())))) {
				return null;
			}
			if (!HGStatics.AirBlocks.contains((Byte)((byte)(testingBlock.getFace(BlockFace.DOWN).getTypeId()))) && 
					!HGStatics.DangerBlocks.contains((Byte)((byte)(testingBlock.getFace(BlockFace.DOWN).getTypeId())))) {
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
				leapPlayer = server.getPlayer(argument);
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

	public String teleport(Player player, double x, double y, double z) {
		player.teleportTo(new Location(player.getWorld(), x, y, z));
		return null;
	}

	public String getLocation(Player player) {
		Location loc = player.getLocation();
		msg.sendPositiveMessage(player, (new StringBuilder("Loc: ")).append(loc.getX()).append(",").append(loc.getY()).append(",").append(loc.getZ())
				.append(").").toString());
		return null;
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
				if (block.getY() < 3 || block.getY() > 123) {
					break;
				}
				if (solidFound && HGStatics.AirBlocks.contains((Byte)((byte)(block.getTypeId())))) {
					if (strataCount == strataToJump) {
						//HGStatics.AirBlocks.contains((Byte)((byte)(testingBlock.getFace(BlockFace.DOWN).getTypeId())))
						if (!HGStatics.AirBlocks.contains((Byte)((byte)(block.getFace(BlockFace.UP).getTypeId())))) {
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
				} else if (!HGStatics.AirBlocks.contains((Byte)((byte)(block.getTypeId()))) && 
						!HGStatics.DangerBlocks.contains((Byte)((byte)(block.getTypeId())))) {
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
}
