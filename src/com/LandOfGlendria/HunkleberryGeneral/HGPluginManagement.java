package com.LandOfGlendria.HunkleberryGeneral;

import java.io.File;

import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class HGPluginManagement {

	private Plugin plugin = null;

	public HGPluginManagement(Plugin plugin) {
		this.plugin = plugin;
	}

	public String[] listPlugins() {
		String[] messages = new String[2];
		Plugin[] pluginList = plugin.getServer().getPluginManager().getPlugins();
		StringBuilder pluginInfo = new StringBuilder();
		pluginInfo.append("Plugin list:");
		pluginInfo.append(HGStatics.POSITIVE_COLOR);
		pluginInfo.append(" [enabled]");
		pluginInfo.append(HGStatics.ERROR_COLOR);
		pluginInfo.append(" [disabled]");
		messages[0] = pluginInfo.toString();
		pluginInfo = new StringBuilder();
		for (Plugin p : pluginList) {
			if (p.isEnabled()) {
				pluginInfo.append(HGStatics.NO_COLOR);
				pluginInfo.append("[");
				pluginInfo.append(HGStatics.POSITIVE_COLOR);
			} else {
				pluginInfo.append(HGStatics.NO_COLOR);
				pluginInfo.append("[");
				pluginInfo.append(HGStatics.ERROR_COLOR);
			}
			pluginInfo.append(p.getDescription().getName());
			pluginInfo.append(HGStatics.NO_COLOR);
			pluginInfo.append(":");
			pluginInfo.append(HGStatics.WARNING_COLOR);
			pluginInfo.append("v");
			pluginInfo.append(p.getDescription().getVersion());
			pluginInfo.append(HGStatics.NO_COLOR);
			pluginInfo.append("] ");
		}
		messages[1] = pluginInfo.toString();
		return messages;
	}

	private String enableInThread(final Plugin plugin) {
		try {
			Thread enableThread = new Thread() {
				public void run() {
					synchronized (plugin.getPluginLoader()) {
						plugin.getPluginLoader().enablePlugin(plugin);}}};
			enableThread.start();
		} catch (Exception e) {
			return ("Error enabling plugin.");
		}
		return null;
	}

	public String enablePlugin(String pluginName) {
		final Plugin pluginToEnable = plugin.getServer().getPluginManager().getPlugin(pluginName);
		if (pluginToEnable == null) {
			return ("Plugin " + pluginName + " not loaded and cannot be enabled.");
		}
		if (pluginToEnable.isEnabled()) {
			return ("Plugin already enabled.");
		} else {
			return enableInThread(pluginToEnable);
		}
	}

	private String disableInThread(final Plugin plugin) {
		try {
			Thread disableThread = new Thread() {
				public void run() {
					synchronized (plugin.getPluginLoader()) {
						plugin.getPluginLoader().disablePlugin(plugin);}}};
			disableThread.start();
		} catch (Exception e) {
			return ("Error disabling plugin.");
		}
		return null;
	}

	public String disablePlugin(String pluginName) {
		final Plugin pluginToDisable = plugin.getServer().getPluginManager().getPlugin(pluginName);
		if (pluginToDisable == null) {
			return ("Plugin " + pluginName + " not loaded and cannot be disabled.");
		}
		if (pluginToDisable.isEnabled()) {
			return ("Plugin already disabled.");
		} else {
			return disableInThread(pluginToDisable);
		}
	}

	public String loadPlugin(String pluginPath) {
		Plugin pluginToLoad = null;
		PluginManager pm = plugin.getServer().getPluginManager();
		File pluginFile = new File(pluginPath);
		if (!pluginFile.exists()) {
			return ("Unable to load plugin: " + pluginPath + " not found.");
		}
		try {
			pluginToLoad = pm.loadPlugin(pluginFile);
		} catch (InvalidPluginException e) {
			return ("Unable to load Plugin: Invalid Plugin");
		} catch (InvalidDescriptionException e) {
			return ("Unable to load Plugin: Invalid Plugin Description");
		}
		if (pluginToLoad == null) {
			return ("Error loading plugin.");
		}
		return null;
	}

	public void clearAllPlugins() {
		plugin.getServer().getPluginManager().clearPlugins();
	}
}
