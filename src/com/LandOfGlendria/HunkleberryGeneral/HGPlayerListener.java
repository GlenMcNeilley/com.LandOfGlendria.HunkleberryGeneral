package com.LandOfGlendria.HunkleberryGeneral;

import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.Plugin;

public class HGPlayerListener extends PlayerListener {

	private final Logger log = Logger.getLogger("Minecraft");
	private static HGCommandHandler commandHandler;
	private HGMessageManagement msg;

	public HGPlayerListener(Plugin plugin, HGMessageManagement msg) {
		this.msg = msg;
		commandHandler = new HGCommandHandler(plugin, msg);
	}
	public void onPlayerJoin(PlayerEvent event) {
		Player player = event.getPlayer();
		msg.parseMotdForPlayer(player,new String(HGStatics.MOTD_STRING));
	}

	public void onPlayerCommand(PlayerChatEvent event) {
		String split[] = event.getMessage().split(" ");
		Player player = event.getPlayer();
		if (split[0].equalsIgnoreCase("/hg_test")) {
			msg.sendPositiveMessage(
					player,
					(new StringBuilder(String.valueOf(HunkleberryGeneral.pdfFile.getName()))).append(" version ")
							.append(HunkleberryGeneral.pdfFile.getVersion()).append(" is listening.").toString());
			event.setCancelled(true);
			return;
		}
		String base = split[0].substring(1);
		HGCommandData command = HGCommandData.getCommandDataByName(base);
		if (command == null) {
			return;
		}
		if (!command.getServerAllowed().booleanValue()) {
			return;
		}
		if (!player.isOp() && command.getOpsOnly().booleanValue()) {
			if (HGConfig.permissions == null) {
				return;
			}
			if (!HGConfig.permissions.has(player, command.getPermissions())) {
				return;
			}
		}
		String error;
		if ((error = commandHandler.handleCommand(player, command, split)) != null) {
			String helpCommand = HGCommandData.HG_HELP.getCommand();
			msg.sendColorErrorUsage(player, error, command.getCommand(), command.getCommandArgs(), helpCommand);
		}
		if (event.isCancelled()) {
			log.warning((new StringBuilder(String.valueOf(HunkleberryGeneral.pdfFile.getName()))).append(": /").append(base)
					.append(" Another plugin is listening for this command, consider creating an alias.").toString());
		} else {
			event.setCancelled(true);
		}
	}
}
