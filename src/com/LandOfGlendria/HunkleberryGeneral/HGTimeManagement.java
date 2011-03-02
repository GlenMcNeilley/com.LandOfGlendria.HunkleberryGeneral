// TODO: player target of settime to arg 1

package com.LandOfGlendria.HunkleberryGeneral;

import java.util.logging.Logger;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class HGTimeManagement {

	@SuppressWarnings("unused")
	private final Logger log = Logger.getLogger("Minecraft");
	private HGMessageManagement msg;
	private Plugin plugin;

	public HGTimeManagement(Plugin plugin, HGMessageManagement msg) {
		this.msg = msg;
	}

	public String getWorldTime(Player player,World world) {
		msg.sendPositiveMessage(player, "Current server time is: " + world.getTime() + ".");
		return null;
	}

	public String setWorldTime(Player player, World world, long time) {
		time = Math.abs(time % 24000L);
		world.setTime(time);
		msg.sendPositiveMessage(player, "Current server time is: " + world.getTime() + ".");
		return null;
	}
	


}
