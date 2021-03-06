package net.estinet.gFeatures.Feature.Gliders.EventBase.GameFunc;

import com.shampaggon.crackshot.CSUtility;
import net.estinet.gFeatures.API.Inventory.ClearInventory;
import net.estinet.gFeatures.Feature.Gliders.Basic;
import net.estinet.gFeatures.Feature.Gliders.PlayerMode;
import net.estinet.gFeatures.Feature.Gliders.Team;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
	public void respawn(Player p){
		ClearInventory ci = new ClearInventory();
		ci.clearInv(p);
		Basic.modes.remove(p.getUniqueId());
		Basic.modes.put(p.getUniqueId(), PlayerMode.INGAME);
		if((Basic.teams.get(p.getUniqueId()).equals(Team.ORANGE) && !Basic.swap) || (Basic.teams.get(p.getUniqueId()).equals(Team.BLUE) && Basic.swap)){
			p.setGameMode(GameMode.ADVENTURE);
			Bukkit.getLogger().info(Basic.towerspawn.size() + "");
			int random = (int) Math.floor(Math.random() * Basic.towerspawn.size());
			p.teleport(Basic.towerspawn.get(random));

			ItemStack wool = new ItemStack(Material.ORANGE_STAINED_GLASS, 1);
			p.getInventory().setHelmet(wool);

			CSUtility cs = new CSUtility();
			cs.giveWeapon(p, "Sniper", 1);
			cs.giveWeapon(p, "Shotgun", 1);
			cs.giveWeapon(p, "Auto-Rifle", 1);
		}
		else{
			p.setGameMode(GameMode.ADVENTURE);
			int random = (int) Math.floor(Math.random() * Basic.planespawn.size());
			p.teleport(Basic.planespawn.get(random));
			ItemStack wool = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS, 1);
			p.getInventory().setHelmet(wool);

			p.getInventory().setChestplate(new ItemStack(Material.ELYTRA, 1));

			CSUtility cs = new CSUtility();
			cs.giveWeapon(p, "Pistol", 1);
			cs.giveWeapon(p, "Grenade", 20);
			cs.giveWeapon(p, "GrenadeLauncher", 1);
		}
	}
}
