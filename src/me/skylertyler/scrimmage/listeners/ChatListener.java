package me.skylertyler.scrimmage.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static org.bukkit.ChatColor.*;
import me.skylertyler.scrimmage.Scrimmage;
import me.skylertyler.scrimmage.channels.AdminChannel;
import me.skylertyler.scrimmage.channels.Channel;
import me.skylertyler.scrimmage.channels.GlobalChannel;
import me.skylertyler.scrimmage.channels.TeamChannel;
import me.skylertyler.scrimmage.match.Match;
import me.skylertyler.scrimmage.team.Team;
import me.skylertyler.scrimmage.utils.ChannelUtils;
import me.skylertyler.scrimmage.utils.Characters;

public class ChatListener implements Listener {

	private Match match;

	public ChatListener() {
		this.match = Scrimmage.getScrimmageInstance().getMatch();
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onChannelChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		Team team = this.match.getTeamHandler().teamForPlayer(player);
		Channel channel = ChannelUtils.getChannel(player);
		// works
		if (channel instanceof TeamChannel) {
			TeamChannel teamChannel = (TeamChannel) channel;
			if (teamChannel != null) {
				List<String> teamMates = team.getMembers();
				for (String string : teamMates) {
					Player member = Bukkit.getPlayer(string);
					// try this;
					event.setCancelled(true);
					if (this.match.getMap().getInfo().isAuthor(player)) {
						event.setFormat(team.getColor() + "[Team] " + WHITE
								+ "<" + GOLD + Characters.AllowCharacters(Characters.DIAMX.getUTF())
								+ team.getColor() + player.getDisplayName()
								+ WHITE + ">: " + event.getMessage());
					} else {
						event.setFormat(team.getColor() + "[Team] " + WHITE
								+ "<" + team.getColor()
								+ player.getDisplayName() + WHITE + ">: "
								+ event.getMessage());
					}
					member.sendMessage(event.getFormat());
				}
			}
		}

		if (channel instanceof GlobalChannel) {
			GlobalChannel global = (GlobalChannel) channel;
			if (global != null) {
				event.setCancelled(true);
				this.match.broadcast(WHITE + "<" + team.getColor()
						+ player.getDisplayName() + WHITE + ">: "
						+ event.getMessage());
			}
		}

		if (channel instanceof AdminChannel) {
			AdminChannel admin = (AdminChannel) channel;
			if (admin != null) {
				for (Player players : Bukkit.getOnlinePlayers()) {
					if (players.isOp()
							|| players.hasPermission("admin.channel.recieve")) {
						event.setCancelled(true);
						if (team != null) {
							event.setFormat(RED + "[" + GOLD + "A" + RED + "] "
									+ WHITE + "<" + team.getColor()
									+ player.getName() + WHITE + ">: "
									+ event.getMessage());
						} else {
							event.setFormat(RED + "[" + GOLD + "A" + RED + "] "
									+ WHITE + "<" + player.getDisplayName()
									+ ">: " + event.getMessage());
						}
						players.sendMessage(event.getFormat());
					}
				}
			}
		}
	}
}
