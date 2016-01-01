package net.estinet.gFeatures.Feature.gFactions.Menus.Autorifle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import net.estinet.gFeatures.Basic;
import net.estinet.gFeatures.API.Inventory.InventoryAPI;
import net.estinet.gFeatures.API.PlayerStats.gPlayer;

public class Beginner {
	public InventoryAPI makeInventory(final Player p, gPlayer player){
		try{
		InventoryAPI menu = new InventoryAPI("Auto-Rifle Tier", 9, new InventoryAPI.OptionClickEventHandler() {
	        @Override
	        public void onOptionClick(final InventoryAPI.OptionClickEvent event) {
	        	final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	            if(event.getName().equals(ChatColor.BOLD + "" + ChatColor.AQUA +  "Switch to Auto-Rifle")){
	            	event.getPlayer().closeInventory();
	            	scheduler.scheduleSyncDelayedTask(Bukkit.getServer().getPluginManager().getPlugin("gFeatures"), new Runnable() {
	                	public void run(){
	                		p.sendMessage(ChatColor.BOLD + "[" + ChatColor.AQUA + "Tiers" + ChatColor.BOLD + "] " + ChatColor.DARK_AQUA + "You have joined the Auto-Rifle tier.");
	                		player.setValue("gFactionsTier", "autorifle");
	                		Basic.setgPlayer(Basic.getgPlayer(event.getPlayer().getUniqueId().toString()), player);
	                   }
	                }, 9L);
	            }
	            event.setWillClose(true);
	        }
	    }, Bukkit.getServer().getPluginManager().getPlugin("gFeatures"))
    .setOption(0, new ItemStack(Material.STAINED_GLASS_PANE, 1), ChatColor.BOLD + "Choose wisely!", "")
    .setOption(1, new ItemStack(Material.STAINED_GLASS_PANE, 1), ChatColor.BOLD + "Choose wisely!", "")
    .setOption(2, new ItemStack(Material.STAINED_GLASS_PANE, 1), ChatColor.BOLD + "Choose wisely!", "")
    .setOption(3, new ItemStack(Material.STAINED_GLASS_PANE, 1), ChatColor.BOLD + "Choose wisely!", "")
    .setOption(4, new ItemStack(Material.ENCHANTED_BOOK, 1), ChatColor.BOLD + "" + ChatColor.AQUA +  "Switch to Auto-Rifle", ChatColor.RED + "" + ChatColor.BOLD + "Are you sure you want to switch to the auto-rifle tier?")
    .setOption(5, new ItemStack(Material.STAINED_GLASS_PANE, 1), ChatColor.BOLD + "Choose wisely!", "")
    .setOption(6, new ItemStack(Material.STAINED_GLASS_PANE, 1), ChatColor.BOLD + "Choose wisely!", "")
    .setOption(7, new ItemStack(Material.STAINED_GLASS_PANE, 1), ChatColor.BOLD + "Choose wisely!", "")
    .setOption(8, new ItemStack(Material.STAINED_GLASS_PANE, 1), ChatColor.BOLD + "Choose wisely!", "");
	return menu;
	}catch(Exception e){
		e.printStackTrace();
	}
		return null;
    }
}
