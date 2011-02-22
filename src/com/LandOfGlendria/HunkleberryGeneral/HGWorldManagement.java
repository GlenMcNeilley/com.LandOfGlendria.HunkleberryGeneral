package com.LandOfGlendria.HunkleberryGeneral;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.server.EntityItem;
import org.bukkit.*;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.plugin.Plugin;

public class HGWorldManagement {

	private HGMessageManagement msg;
	private Plugin plugin;

	public HGWorldManagement(Plugin plugin, HGMessageManagement msg) {
		this.msg = msg;
		this.plugin = plugin;
	}

	public String removeNonPlayerEntities(Player player) {
		ArrayList<LivingEntity> entityList = (ArrayList<LivingEntity>) player.getWorld().getLivingEntities();
		CraftPlayer craftPlayer = null;
		int killcount = 0;
		Iterator<LivingEntity> iterator = entityList.iterator();
		while (iterator.hasNext()) {
			LivingEntity entity = (LivingEntity) iterator.next();
			try {
				craftPlayer = (CraftPlayer) entity;
			} catch (ClassCastException e) {
				entity.setHealth(0);
				killcount++;
				continue;
			}
			if (!craftPlayer.isPlayer()) {
				entity.setHealth(0);
				killcount++;
			}
		}
		msg.sendPositiveMessage(player, (new StringBuilder("Removed ")).append(killcount).append(" living entities.").toString());
		return null;
	}

	public String removeItemDropEntities(Player player) {
		ArrayList<Entity> entityList = (ArrayList<Entity>) player.getWorld().getEntities();
		int killcount = 0;
		for (Iterator<Entity> iterator = entityList.iterator(); iterator.hasNext();) {
			Entity entity = (Entity) iterator.next();
			if (entity.getClass().getName().contains("CraftItem")) {
				CraftItem item = (CraftItem) entity;
				synchronized (item) {
					EntityItem entityItem = (EntityItem) item.getHandle();
					entityItem.q();
				}
				killcount++;
			}
		}

		msg.sendPositiveMessage(player, (new StringBuilder("Removed ")).append(killcount).append(" item entities.").toString());
		return null;
	}

	public String listWorlds(Player player) {
		ArrayList<World> worlds = (ArrayList<World>) plugin.getServer().getWorlds();
		StringBuffer sb = new StringBuffer();
		for (Iterator<World> iterator = worlds.iterator(); iterator.hasNext(); sb.append("] ")) {
			World world = (World) iterator.next();
			sb.append(HGStatics.NO_COLOR);
			sb.append("[");
			if (world.getEnvironment() == org.bukkit.World.Environment.NORMAL) {
				sb.append(HGStatics.POSITIVE_COLOR);
			} else {
				sb.append(HGStatics.ERROR_COLOR);
			}
			sb.append(world.getName());
			sb.append(HGStatics.NO_COLOR);
		}

		msg.sendSegmented(player, sb.toString());
		return null;
	}

	public String loadWorld(Player player, HGCommandData command, String name, String env) {
		boolean exists = false;
		Enum<World.Environment> envEnum;
		if (env.equalsIgnoreCase("nether")) {
			envEnum = org.bukkit.World.Environment.NETHER;
		} else if (env.equalsIgnoreCase("normal")) {
			envEnum = org.bukkit.World.Environment.NORMAL;
		} else {
			return msg.formatInvalidArgs(env, "Invalid environment");
		}
		File file = new File((new StringBuilder()).append(new StringBuilder(String.valueOf(name))).append("/level.dat").toString().toString());
		if (file.exists()) {
			exists = true;
		}
		if (exists) {
			if (command == HGCommandData.CREATE_WORLD) {
				return (new StringBuilder("Unable to create world. File ")).append(file.getName()).append(" already exists.").toString();
			}
			plugin.getServer().createWorld(name, (org.bukkit.World.Environment) envEnum);
			msg.sendPositiveMessage(player, (new StringBuilder("Loading ")).append(env).append(" world ").append(name).append(".").toString());
		} else {
			if (command == HGCommandData.LOAD_WORLD) {
				return (new StringBuilder("Unable to load world. File ")).append(file.getName()).append(" doesn't exist.").toString();
			}
			plugin.getServer().createWorld(name, (org.bukkit.World.Environment) envEnum);
			msg.sendPositiveMessage(player, (new StringBuilder("Creating ")).append(env).append(" world ").append(name).append(".").toString());
		}
		return null;
	}

	public String setSpawnLocation(Player player, World world, int x, int y, int z) {
		((CraftWorld) world).getHandle().spawnX = x;
		((CraftWorld) world).getHandle().spawnY = y;
		((CraftWorld) world).getHandle().spawnZ = z;
		msg.sendPositiveMessage(player, (new StringBuilder("Set spawn point in ")).append(world.getName()).append(" to (").append(x).append(",").append(y)
				.append(",").append(z).append(").").toString());
		return null;
	}

	public String leap(Player player, Location location) {
		msg.sendPositiveMessage(player, (new StringBuilder("Leaping to ")).append(location.getWorld().getName()).append(" (")
				.append((int) location.getX()).append(",").append((int) location.getY()).append(",").append((int) location.getZ()).append(").").toString());
		player.teleportTo(location);
		return null;
	}
}
