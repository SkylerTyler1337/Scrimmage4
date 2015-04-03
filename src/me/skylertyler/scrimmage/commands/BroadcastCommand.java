package me.skylertyler.scrimmage.commands;

import me.skylertyler.scrimmage.Scrimmage;
import me.skylertyler.scrimmage.utils.MessageUtils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BroadcastCommand implements CommandExecutor {

	private final Scrimmage scrim;

	public BroadcastCommand(Scrimmage scrimmage) {
		this.scrim = scrimmage;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		 if(sender instanceof Player){
			 Player player = (Player) sender;
			 if(cmd.getName().equalsIgnoreCase("broadcast")){
				  if(args.length <= 0){
				      player.sendMessage(MessageUtils.notEnoughArgs(cmd));
				      return false;
				  }
				  
				  
				  String message = null;
				  if(args.length >= 1){
					  Scrimmage scrim = this.getScrimmage();
					  for(int i = 0; i < args.length; i++){
						  message += args.toString().substring(i);
					  }
					  
					  String prefix = scrim.getConfigFile().getFullPrefix();
					  Bukkit.broadcastMessage(prefix + " " + message);
				  }
			 }
		 }
	                                                                                       
		return false;
	}

	public Scrimmage getScrimmage() {
		return this.scrim;
	}
}