package com.LandOfGlendria.HunkleberryGeneral;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
//------------IFDEF 440
//import org.bukkit.event.player.PlayerChatEvent;
//------------ENDIFDEF 440
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

public class HGPlayerListener extends PlayerListener {

	@SuppressWarnings("unused")
	private final Logger log = Logger.getLogger("Minecraft");
	private HGMessageManagement msg;
	
//------------IFDEF 440
//	private static HGCommandHandler commandHandler;
//------------ENDIFDEF 440

	private HGBouncer bouncer;
	
	
		public HGPlayerListener(Plugin plugin, HGMessageManagement msg, HGBouncer bouncer) {
		this.msg = msg;
		this.bouncer = bouncer;
		
//------------IFDEF 440
//		commandHandler = new HGCommandHandler((HunkleberryGeneral)plugin, msg,bouncer);
//------------ENDIFDEF 440

	}

	public void onPlayerLogin(PlayerLoginEvent event) {
		try {
			Player loggingIn = ((PlayerEvent)event).getPlayer();
			String loggingInName = loggingIn.getName();

			if (bouncer.getBounced(loggingInName)) {
				event.disallow(PlayerLoginEvent.Result.KICK_BANNED, bouncer.getReturnTime(loggingIn.getName()));
			}

		} catch (Exception e) {
			msg.severe("Exception in getting bounced list.");
			e.printStackTrace();			
		}
	}
	
	public void onPlayerJoin(PlayerEvent event) {
		Player player = event.getPlayer();
		msg.parseMotdForPlayer(player,new String(HGStatics.MOTD_STRING));

		
		try {
			Player loggingIn = ((PlayerEvent)event).getPlayer();
			String loggingInName = loggingIn.getName();
			InetSocketAddress loggingInIp = loggingIn.getAddress();
			InetAddress loggingInIp2 = loggingInIp.getAddress();
			String loggingInIpString = loggingInIp2.toString();
			if (bouncer.getBounced(loggingInName)) {
				player.setHealth(0);
				player.kickPlayer(loggingInName);
			}
			else {
				if (loggingInIpString != null) {
					msg.info("looking for :"+loggingInIpString+":");
					if (bouncer.getBounced(loggingInIpString)) {
						player.setHealth(0);
					}
				} else {
					msg.warn("Unable to resolve players ip address. Could not check against ban list.");
				}
			}
		} catch (Exception e) {
			msg.severe("Exception in getting bounced list.");
			e.printStackTrace();			
		}
	}
	
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		try {
			Player loggingIn = ((PlayerEvent)event).getPlayer();
			String loggingInName = loggingIn.getName();
			InetSocketAddress loggingInIp = loggingIn.getAddress();
			InetAddress loggingInIp2 = loggingInIp.getAddress();
			String loggingInIpString = loggingInIp2.toString();
			if (bouncer.getBounced(loggingInName)) {
				player.kickPlayer(loggingInName);
			}
			else {
				if (loggingInIpString != null) {
					msg.info("looking for :"+loggingInIpString+":");
					if (bouncer.getBounced(loggingInIpString)) {
						player.kickPlayer(loggingInName);
					}
				} else {
					msg.warn("Unable to resolve players ip address. Could not check against ban list.");
				}
			}
		} catch (Exception e) {
			msg.severe("Exception in getting bounced list.");
			e.printStackTrace();			
		}
	}
	
//------------IFDEF 440
//	public void onPlayerCommand(PlayerChatEvent event) {
//		String split[] = event.getMessage().split(" ");
//		Player player = event.getPlayer();
//		if (split[0].equalsIgnoreCase("/hg_test")) {
//			msg.sendPositiveMessage(
//					player,
//					(new StringBuilder(String.valueOf(HunkleberryGeneral.pdfFile.getName()))).append(" version ")
//							.append(HunkleberryGeneral.pdfFile.getVersion()).append(" is listening.").toString());
//			event.setCancelled(true);
//			return;
//		}
//		String base = split[0].substring(1);
//		HGCommandData command = HGCommandData.getCommandDataByName(base);
//		if (command == null) {
//			return;
//		}
//		if (!command.getServerAllowed().booleanValue()) {
//			return;
//		}
//		if (!player.isOp() && command.getOpsOnly().booleanValue()) {
//			if (HGConfig.permissions == null) {
//				return;
//			}
//			if (!HGConfig.permissions.has(player, command.getPermissions())) {
//				return;
//			}
//		}
//		String error;
//		if ((error = commandHandler.handleCommand(player, command, split)) != null) {
//			String helpCommand = HGCommandData.HG_HELP.getCommand();
//			msg.sendColorErrorUsage(player, error, command.getCommand(), command.getCommandArgs(), helpCommand);
//		}
//		if (event.isCancelled()) {
//			log.warning((new StringBuilder(String.valueOf(HunkleberryGeneral.pdfFile.getName()))).append(": /").append(base)
//					.append(" Another plugin is listening for this command, consider creating an alias.").toString());
//		} else {
//			event.setCancelled(true);
//		}
//	}
//------------ENDIFDEF 440
}
