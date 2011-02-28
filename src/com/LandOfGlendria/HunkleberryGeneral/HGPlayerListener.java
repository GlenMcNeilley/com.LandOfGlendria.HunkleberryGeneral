package com.LandOfGlendria.HunkleberryGeneral;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

public class HGPlayerListener extends PlayerListener {

	@SuppressWarnings("unused")
	private final Logger log = Logger.getLogger("Minecraft");
	private HGMessageManagement msg;
	private HGBouncer bouncer;

	public HGPlayerListener(Plugin plugin, HGMessageManagement msg, HGBouncer bouncer) {
		this.msg = msg;
		this.bouncer = bouncer;
	}

	public void onPlayerLogin(PlayerLoginEvent event) {
		try {
			Player loggingIn = ((PlayerEvent)event).getPlayer();
			String loggingInName = loggingIn.getName();
			//InetSocketAddress loggingInIp = loggingIn.getAddress();
			//InetAddress loggingInIp2 = loggingInIp.getAddress();
			//String loggingInIpString = loggingInIp2.toString();
			if (bouncer.getBounced(loggingInName)) {
				event.disallow(PlayerLoginEvent.Result.KICK_BANNED, bouncer.getReturnTime(loggingIn.getName()));
			}
//			else {
//				if (loggingInIpString != null) {
//					if (bouncer.getBounced(loggingInIpString)) {
//						event.disallow(PlayerLoginEvent.Result.KICK_BANNED, bouncer.getReturnTime(loggingInIpString));
//					}
//				} else {
//					msg.warn("Unable to resolve players ip address. Could not check against ban list.");
//				}
//			}
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

}
