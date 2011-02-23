package com.LandOfGlendria.HunkleberryGeneral;

import java.io.File;
import java.io.IOException;
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
		msg.sendPositiveMessage(player, ("Removed " + killcount + " living entities."));
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
					entityItem.C();
				}
				killcount++;
			}
		}

		msg.sendPositiveMessage(player, ("Removed " + killcount + " item entities."));
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
	
	public synchronized String worldLoader(Player player, HGCommandData command, String name, String env) {
		if (command == HGCommandData.LOAD_WORLD) {
			return loadWorld(player,name);
		} else if (command == HGCommandData.CREATE_WORLD) {
			return createWorld(player,name,env);
		} else if (command == HGCommandData.DECLARE_WORLD_ENV) {
			return createWorldEnvFile(player,name,env);
		} else {
			return ("Internal error. Invalid command to worldLoader. Command = " + command.getCommand());
		}
	}

	private String loadWorld(Player player, String name) {
		World.Environment envEnum =  World.Environment.NORMAL;
		File worldFile = new File(name + "/" + HGStatics.WORLD_DATA_FILE);
		File netherFile = new File(name + "/" + HGStatics.NETHER_ENV_FILE);
		File normalFile = new File(name + "/" + HGStatics.NORMAL_ENV_FILE);
		if (!worldFile.exists()) {
			msg.warn(player.getName() + " attempted to load a world that doesn't exist [" + name + "].");
			return (name + "does not exist.");
		}
		
		if (netherFile.exists() && normalFile.exists()) {
			msg.warn(player.getName() + " attempted to load world " + name + " but it has both NORMAL and NETHER environment files.  Please delete the appropriate file.");
			return ("Unable to load environment file for " + name + ".  See the server console for more information.");
		} else if (netherFile.exists()) {
			envEnum = World.Environment.NETHER;
		} else if (normalFile.exists()) {
			envEnum = World.Environment.NORMAL;
		} else {
			msg.warn(player.getName() + " attempted to load world " + name + " but it does not have a NORMAL or NETHER environment file.  Use /setenvtype to correct this issue.");
			return ("Unable to load environment file for " + name + ".  See the server console for more information.");		
		}
		
		msg.sendPositiveMessage(player, ("Loading ") + envEnum.toString() + " world " + name + ". Please wait for completion message.");
		World created = plugin.getServer().createWorld(name, envEnum);
		if (created != null) {
			msg.sendPositiveMessage(player, (envEnum.toString() + " world " + name + " has loaded and is available."));
			return null;
		} else {
			return ("Loading of " + envEnum.toString() + " world " + name + " has been refused by the server for an unknown reason.");
		}
	}
	
	
	private String createWorld(Player player, String name, String env) {
		World.Environment envEnum =  World.Environment.NORMAL;
		File worldFile = new File(name + "/" + HGStatics.WORLD_DATA_FILE);
		if (worldFile.exists()) {
			msg.warn(player.getName() + " attempted to create a world that already exists [" + name + "].");
			return (name + " already exists.");
		}

		if (env.equalsIgnoreCase("nether")) {
			envEnum = World.Environment.NETHER;
		} else if (env.equalsIgnoreCase("normal")) {
			envEnum = World.Environment.NORMAL;
		} else {
			return msg.formatInvalidArgs(env, "Invalid environment");
		}

		msg.sendPositiveMessage(player, ("Creating ") + envEnum.toString() + " world " + name + ". Please wait for completion message (may take a moment).");
		World created = plugin.getServer().createWorld(name, envEnum);
		if (created != null) {
			createWorldEnvFile(player,name,env);
			msg.sendPositiveMessage(player, (envEnum.toString() + " world " + name + " has been created and is available."));
			return null;
		} else {
			return ("Creating of " + envEnum.toString() + " world " + name + " has been refused by the server for an unknown reason.");
		}
	}

	private String createWorldEnvFile(Player player, String name, String env) {
		File worldFile = new File(name + "/" + HGStatics.WORLD_DATA_FILE);
		File netherFile = new File(name + "/" + HGStatics.NETHER_ENV_FILE);
		File normalFile = new File(name + "/" + HGStatics.NORMAL_ENV_FILE);
		if (!worldFile.exists()) {
			msg.warn(player.getName() + " attempted to access a world that doesn't exist [" + name + "].");
			return (name + "does not exist.");
		}
		if (netherFile.exists() || normalFile.exists()) {
			msg.warn(player.getName() + " attempted to create an environment file for a world that already has one [" + name + "].");
			return (name + " already has an environment file.");
		}
		try {
			if (env.equalsIgnoreCase("nether")) {
				netherFile.createNewFile();
				msg.sendPositiveMessage(player, ("Created environment file " + netherFile.getPath() + "."));
				return null;
			} else if (env.equalsIgnoreCase("normal")) {
				normalFile.createNewFile();
				msg.sendPositiveMessage(player, ("Created environment file " + normalFile.getPath() + "."));
				return null;
			} else {
				return msg.formatInvalidArgs(env, "Invalid environment");
			}
		} catch (IOException e) {
			msg.warn(player.getName() + " System level error creating world environment file for [" + name + "].");
			return ("Operating system has refused to create world environment file for name" + name +".");
		}
	}
	

	public String setSpawnLocation(Player player, World world, int x, int y, int z) {
        CraftWorld cworld=(CraftWorld)world;
        net.minecraft.server.WorldServer wserver = cworld.getHandle();
        net.minecraft.server.WorldData wdata = wserver.q;
        wdata.a(x, y, z);
		msg.sendPositiveMessage(player, ("Set spawn point in " + world.getName() + " to (" + x + "," + y + "," + z + ")."));
		return null;
	}
	
	public String getSpawnLocation(Player player, World world) {

        CraftWorld cworld=(CraftWorld)world;
        net.minecraft.server.WorldServer wserver = cworld.getHandle();
        int x1 = wserver.q.c();
        int y1 = wserver.q.d();
        int z1 = wserver.q.e();

		msg.sendPositiveMessage(player, Integer.toString(x1+y1+z1));
		return null;
	}
}
