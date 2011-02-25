package com.LandOfGlendria.HunkleberryGeneral;

import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HunkleberryGeneral extends JavaPlugin {

	private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
	private static HGPlayerListener listener;
	public static PluginDescriptionFile pdfFile;
	public static HGConfig config;
	private static HGMessageManagement msg;

	public HunkleberryGeneral() {
	}

	public void onEnable() {
		pdfFile = getDescription();
		msg = new HGMessageManagement(this);
		config = new HGConfig(this, msg);
		listener = new HGPlayerListener(this, msg);
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvent(org.bukkit.event.Event.Type.PLAYER_COMMAND, listener, org.bukkit.event.Event.Priority.Normal, this);
		pm.registerEvent(org.bukkit.event.Event.Type.PLAYER_JOIN, listener, org.bukkit.event.Event.Priority.Normal, this);
		
		msg.info((new StringBuilder(String.valueOf(pdfFile.getName()))).append(" version ").append(pdfFile.getVersion()).append(" is enabled").toString());
	}

	public void onDisable() {
		
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
