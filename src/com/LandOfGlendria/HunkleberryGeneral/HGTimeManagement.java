package com.LandOfGlendria.HunkleberryGeneral;

import java.util.logging.Logger;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class HGTimeManagement
{

	@SuppressWarnings("unused")
	private final Logger log = Logger.getLogger("Minecraft");

	public HGTimeManagement()
	{
	}

	public long getWorldTime(World world)
	{
		return world.getTime();
	}

	public long setWorldTime(World world, long time)
	{
		time = Math.abs(time % 24000L);
		world.setTime(time);
		return time;
	}

	public void displayWorldTime(Player player)
	{
		player.sendMessage((new StringBuilder("Current server time is: ")).append(getWorldTime(player.getWorld())).append(".").toString());
	}

	public void setWorldTime(Player player, int time)
	{
		player.sendMessage((new StringBuilder("Current server time is: ")).append(setWorldTime(player.getWorld(), time)).append(".").toString());
	}
}
