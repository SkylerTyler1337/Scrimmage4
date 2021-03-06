package me.skylertyler.scrimmage.listeners;

import java.util.Map.Entry;

import me.skylertyler.scrimmage.MatchListener;
import me.skylertyler.scrimmage.Scrimmage;
import me.skylertyler.scrimmage.event.ScoreboardLoadEvent;
import me.skylertyler.scrimmage.event.MatchLoadEvent;
import me.skylertyler.scrimmage.event.MatchStartEvent;
import me.skylertyler.scrimmage.match.Match;
import me.skylertyler.scrimmage.match.MatchState;
import me.skylertyler.scrimmage.regions.Region;
import me.skylertyler.scrimmage.regions.RegionUtils;
import me.skylertyler.scrimmage.regions.types.BlockRegion;
import me.skylertyler.scrimmage.regions.types.CircleRegion;
import me.skylertyler.scrimmage.regions.types.CuboidRegion;
import me.skylertyler.scrimmage.regions.types.CylinderRegion;
import me.skylertyler.scrimmage.regions.types.SphereRegion;
import me.skylertyler.scrimmage.scoreboard.Scoreboard;
import me.skylertyler.scrimmage.scoreboard.ScoreboardType;
import me.skylertyler.scrimmage.team.Team;
import me.skylertyler.scrimmage.utils.Characters;
import me.skylertyler.scrimmage.utils.Log;
import me.skylertyler.scrimmage.utils.TeamUtils;
import static org.bukkit.ChatColor.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.scoreboard.DisplaySlot;

/** a match listener */
public class ConnectionListener extends MatchListener {

	private ChatColor dark_aqua = DARK_AQUA;
	private ChatColor aqua = AQUA;

	public ConnectionListener() {
		super(Scrimmage.getScrimmageInstance().getMatch());
	}

	@EventHandler
	public void onServerList(ServerListPingEvent event) {
		String format = null;
		Match match = this.getMatch();
		String prefix = aqua + "=" + dark_aqua + "=" + aqua + "=" + dark_aqua
				+ "=" + aqua + "=" + dark_aqua + "=" + aqua + "=" + dark_aqua
				+ "=" + aqua + "=" + dark_aqua + "=" + aqua + "=" + dark_aqua
				+ "=[ ";

		String suffix = aqua + " ]=" + dark_aqua + "=" + aqua + "=" + dark_aqua
				+ "=" + aqua + "=" + dark_aqua + "=" + aqua + "=" + dark_aqua
				+ "=" + aqua + "=" + dark_aqua + "=" + aqua + "=" + dark_aqua
				+ "=" + aqua + "=";
		if (match != null) {
			MatchState ms = match.getState();
			if (ms != null) {
				String state = ms.toString();
				String before = Characters.Raquo.getUTF() + " ";
				String after = " " + Characters.Laquo.getUTF();
				String name = match.getMap().getInfo().getName();
				String light_purple = ChatColor.LIGHT_PURPLE + name;
				format = prefix + state + before + light_purple + state + after
						+ suffix;
			}
		}
		event.setMotd(format);
	}

	@EventHandler
	public void onMatchStart(MatchStartEvent event) {
		Match match = event.getMatch();
		if (match != null) {
			Log.logWarning(match.getID() + " ");

			boolean hasPlayers = Bukkit.getOnlinePlayers().size() > 0;
			Scoreboard board = match.getScoreboard();
			board.registerNewObjective("skyler", DisplaySlot.SIDEBAR,
					ScoreboardType.Flag.getDisplayName());
			if (hasPlayers) {
				for (Player player : Bukkit.getOnlinePlayers()) {
					board.setPlayerBoard(player);
				}
			}
		}
	}

	// just testing the regions below :) // will remove this when done!
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		for (Entry<String, Region> region : RegionUtils.getRegions().entrySet()) {
			if (region.getValue() instanceof BlockRegion) {
				BlockRegion bRegion = (BlockRegion) region.getValue();
				if (bRegion != null) {
					Log.logWarning("passed != null checking");
					if (bRegion.containsVector(event.getBlock().getLocation()
							.toVector())) {
						event.setCancelled(true);
						event.getPlayer()
								.sendMessage(
										ChatColor.RED
												+ "You cant place in this region called "
												+ bRegion.getName());
					}
				}
			} else if (region.getValue() instanceof CuboidRegion) {
				CuboidRegion cuboid = (CuboidRegion) region.getValue();
				if (cuboid != null) {
					Log.logWarning("passed!");
					if (cuboid.containsVector(event.getBlock().getLocation()
							.toVector())) {
						event.setCancelled(true);
						String format = null;
						String you = "You cant place a block here";
						if (cuboid.hasName()) {
							String name = cuboid.getName();
							format = you + " in the region called " + name;
						} else {
							format = you;
						}

						event.getPlayer().sendMessage(format);
					}
				}
			} else if (region.getValue() instanceof SphereRegion) {
				SphereRegion sphere = (SphereRegion) region.getValue();
				if (sphere != null
						&& sphere.containsVector(event.getBlock().getLocation()
								.toVector())) {
					event.setCancelled(true);
					String format = null;
					String you = "You cant place blocks in ";
					if (sphere.hasName()) {
						format = you + sphere.getName();
					} else {
						format = you + " here";
					}

					String result = format;
					event.getPlayer().sendMessage(result);
				}
			} else if (region.getValue() instanceof CylinderRegion) {
				CylinderRegion cylinder = (CylinderRegion) region.getValue();
				String format = null;
				if (cylinder != null
						&& cylinder.containsVector(event.getBlock()
								.getLocation().toVector())) {
					event.setCancelled(true);
					if (cylinder.hasName()) {
						String name = cylinder.getName();
						format = "You cant place blocks in " + name;
					} else {
						format = "You cant place blocks in here!";
					}

					String result = format;
					event.getPlayer().sendMessage(result);
				}
			} else if (region.getValue() instanceof CircleRegion) {
				CircleRegion circle = (CircleRegion) region.getValue();
				if (circle != null
						&& circle.containsVector(event.getBlock().getLocation()
								.toVector())) {
					event.setCancelled(true);
					String format = null;
					if (circle.hasName()) {
						format = "You can't build in the " + RED
								+ circle.getName();
					} else {
						format = "You cant build here!";
					}
					String result = format;
					event.getPlayer().sendMessage(result);
				}
			}
		}
	}

	// make this load before
	@EventHandler
	public void onMatchLoad(MatchLoadEvent event) {
		Match match = event.getMatch();
		ScoreboardLoadEvent scoreboard_event = new ScoreboardLoadEvent(match,
				match.getScoreboard().getNewBoard());
		match.getPluginManager().callEvent(scoreboard_event);
	}

	// try to fix this error with this!
	@EventHandler
	public void loadBoard(ScoreboardLoadEvent event) {
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Team observers = TeamUtils.getObservers();
		event.setJoinMessage(YELLOW + player.getName() + " joined the game!");
		if (this.match != null) {
			if (this.match.getScrimmage().getConfigFile().hasName()) {
				String serverName = this.match.getScrimmage().getConfigFile()
						.getName();
				player.sendMessage(WHITE + "" + BOLD.toString()
						+ "---------------------");
				player.sendMessage(DARK_PURPLE + "Welcome to " + GOLD
						+ serverName);
				player.sendMessage(WHITE + "" + BOLD.toString()
						+ "---------------------");
			}
			if (this.match.isIdle() || this.match.isRunning()) {
				this.match.getTeamHandler().addParticpatingMember(observers,
						player);
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Team team = this.match.getTeamHandler()
				.teamForPlayer(event.getPlayer());
		if (team != null) {
			team.removeMember(event.getPlayer());
		}
	}
}
