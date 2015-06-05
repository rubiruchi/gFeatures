package net.genesishub.gFeatures.Feature.gWarsSuite.Multiplayer.Kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TierFour{
	Player p;
	ItemStack sniper4, shotgun4, special4, autorifle4;
	public TierFour(Player player) {
		p = player;
		sniper4 = createItem(Material.DIAMOND_PICKAXE, ChatColor.AQUA + "Sniper", ChatColor.GOLD + "Heckler and Koch MP-4");
		shotgun4 = createItem(Material.DIAMOND_SPADE, ChatColor.AQUA + "Shotgun", ChatColor.GOLD + "SPAS-12");
		special4 = createItem(Material.DIAMOND_HOE, ChatColor.AQUA + "Special", ChatColor.GOLD + "Strike Team");
		autorifle4 = createItem(Material.DIAMOND_AXE, ChatColor.AQUA + "Auto-Rifle", ChatColor.GOLD + "m16-Elite");
	}
	public void initialize(String value){
		switch(value){
		case "AUTORIFLE":
			autorifle();
			break;
		case "SHOTGUN":
			shotgun();
			break;
		case "SNIPER":
			sniper();
			break;
		case "SPECIAL": 
			special();
			break;
		}
	}
	public void autorifle(){
		
	}
	public void shotgun(){
		
	}
	public void sniper(){
		
	}
	public void special(){
		
	}
	public void interact(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(p.getItemInHand().equals(sniper4)){
			sniper();
		}
		else if(p.getItemInHand().equals(shotgun4)){
			shotgun();
		}
		else if(p.getItemInHand().equals(special4)){
			special();
		}
		else if(p.getItemInHand().equals(autorifle4)){
			autorifle();
		}
	}
	public ItemStack createItem(Material material, String name, String ... lore){
		ItemStack item = new ItemStack(material, 1);
		List<String> lores = new ArrayList<>();
		for(String lor : lore){
		lores.add(lor);
		}
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lores);
		item.setItemMeta(meta);
		return item;
	}
}
