package me.skylertyler.scrimmage.listeners;

import me.skylertyler.scrimmage.MatchListener;
import me.skylertyler.scrimmage.Scrimmage;
import me.skylertyler.scrimmage.map.MapInfo;
import me.skylertyler.scrimmage.match.Match;
import me.skylertyler.scrimmage.team.Team;
import me.skylertyler.scrimmage.utils.TeamUtils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ObserverListener extends MatchListener {

	public ObserverListener() {
		super(Scrimmage.getScrimmageInstance().getMatch());
	}

	/**
	 * return {@link Boolean}
	 */
	public boolean check(Player player) {
		Team team = TeamUtils.getObservers();
		return team.containsPlayer(player);
	}

	// cancelling block breaking :)
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		event.setCancelled(check(player));
	}

	// cancelling block placing :)
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		event.setCancelled(check(player));
	}

	// dont allow the compass go above 255 if the map attribute 'internal'is
	// true :)
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack hand = player.getItemInHand();
		Match match = this.getMatch();
		MapInfo info = match.getMap().getInfo();
		boolean internal = info.isInternal();
		int max = 255;
		if (hand.getType().equals(Material.COMPASS) && internal) {
			Block loc = player.getTargetBlock(null, max);
			if (loc.getY() > max) {
				event.setCancelled(check(player));
			}
		}
	}
}
