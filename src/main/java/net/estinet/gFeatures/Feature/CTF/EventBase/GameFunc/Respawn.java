package net.estinet.gFeatures.Feature.CTF.EventBase.GameFunc;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.estinet.gFeatures.API.Inventory.ClearInventory;
import net.estinet.gFeatures.Feature.CTF.Basic;
import net.estinet.gFeatures.Feature.CTF.PlayerMode;
import net.estinet.gFeatures.Feature.CTF.Team;
import net.estinet.gFeatures.Feature.CTF.Holo.SpawnMenu;

/*
gFeatures
https://github.com/EstiNet/gFeatures

   Copyright 2018 EstiNet

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

public class Respawn {
	public static void respawn(Player p){
		ClearInventory.clearInv(p);
		Basic.modes.remove(p.getUniqueId());
		Basic.modes.put(p.getUniqueId(), PlayerMode.SELECT);
		if(Basic.teams.get(p.getUniqueId()).equals(Team.ORANGE)){
			p.setGameMode(GameMode.ADVENTURE);
			p.teleport(Basic.orangespawn);
			p.setWalkSpeed((float) 0.3);
			ItemStack wool = new ItemStack(Material.ORANGE_STAINED_GLASS, 1);
			p.getInventory().setHelmet(wool);
		}
		else{
			p.setGameMode(GameMode.ADVENTURE);
			p.teleport(Basic.bluespawn);
			p.setWalkSpeed((float) 0.3);
			ItemStack wool = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS, 1);
			p.getInventory().setHelmet(wool);
		}
		SpawnMenu.init(p);
		if(Basic.teams.get(p.getUniqueId()).equals(Team.ORANGE)){
			p.setGameMode(GameMode.ADVENTURE);
			p.teleport(Basic.orangespawn);
			p.setWalkSpeed((float) 0.3);
			ItemStack wool = new ItemStack(Material.ORANGE_STAINED_GLASS, 1);
			p.getInventory().setHelmet(wool);
		}
		else{
			p.setGameMode(GameMode.ADVENTURE);
			p.teleport(Basic.bluespawn);
			p.setWalkSpeed((float) 0.3);
			ItemStack wool = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS, 1);
			p.getInventory().setHelmet(wool);
		}
	}
	public static void fullrespawn(Player p){
		Basic.modes.remove(p.getUniqueId());
		Basic.modes.put(p.getUniqueId(), PlayerMode.INGAME);
		if(Basic.teams.get(p.getUniqueId()).equals(Team.ORANGE)){
			p.setGameMode(GameMode.ADVENTURE);
			p.teleport(Basic.orangeafterspawn);
			p.setWalkSpeed((float) 0.3);
			ItemStack wool = new ItemStack(Material.ORANGE_STAINED_GLASS, 1);
			p.getInventory().setHelmet(wool);
		}
		else{
			p.setGameMode(GameMode.ADVENTURE);
			p.teleport(Basic.blueafterspawn);
			p.setWalkSpeed((float) 0.3);
			ItemStack wool = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS, 1);
			p.getInventory().setHelmet(wool);
		}
	}
}
