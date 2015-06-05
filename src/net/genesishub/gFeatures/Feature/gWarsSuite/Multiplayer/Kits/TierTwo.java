package net.genesishub.gFeatures.Feature.gWarsSuite.Multiplayer.Kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.shampaggon.crackshot.CSUtility;

public class TierTwo{
	Player p;
	ItemStack sniper2, shotgun2, special2, autorifle2;
	public TierTwo(Player player) {
		p = player;
		sniper2 = createItem(Material.GOLD_PICKAXE, ChatColor.AQUA + "Sniper", ChatColor.GOLD + "SilSil69");
		shotgun2 = createItem(Material.GOLD_SPADE, ChatColor.AQUA + "Shotgun", ChatColor.GOLD + "SPAS Regular");
		special2 = createItem(Material.GOLD_HOE, ChatColor.AQUA + "Special", ChatColor.GOLD + "Flamethrower");
		autorifle2 = createItem(Material.GOLD_AXE, ChatColor.AQUA + "Auto-Rifle", ChatColor.GOLD + "AK47 Silver");
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
		CSUtility cs = new CSUtility();
		cs.giveWeapon(p, "AK47-Silver", 1);
		cs.giveWeapon(p, "Python", 1);	
	}
	public void shotgun(){
		CSUtility cs = new CSUtility();
		cs.giveWeapon(p, "SPAS", 1);
		cs.giveWeapon(p, "Python", 1);
	}
	public void sniper(){
		CSUtility cs = new CSUtility();
		cs.giveWeapon(p, "SilSil69", 1);
		cs.giveWeapon(p, "Python", 1);
	}
	public void special(){
		CSUtility cs = new CSUtility();
		cs.giveWeapon(p, "Flamethrower", 1);
		cs.giveWeapon(p, "Python", 1);
	}
	public void interact(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(p.getItemInHand().equals(sniper2)){
			sniper();
		}
		else if(p.getItemInHand().equals(shotgun2)){
			shotgun();
		}
		else if(p.getItemInHand().equals(special2)){
			special();
		}
		else if(p.getItemInHand().equals(autorifle2)){
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
