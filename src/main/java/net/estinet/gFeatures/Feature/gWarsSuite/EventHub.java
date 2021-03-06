package net.estinet.gFeatures.Feature.gWarsSuite;

import net.estinet.gFeatures.Feature.gWarsSuite.Connection.CheckConfig;
import net.estinet.gFeatures.Feature.gWarsSuite.Connection.Connection;
import net.estinet.gFeatures.Feature.gWarsSuite.MainMenu.Interaction;
import net.estinet.gFeatures.Feature.gWarsSuite.MainMenu.Inventory;
import net.estinet.gFeatures.Feature.gWarsSuite.MainMenu.Join;
import net.estinet.gFeatures.Feature.gWarsSuite.MainMenu.MenuDamage;
import net.estinet.gFeatures.Feature.gWarsSuite.MainMenu.SpawnMenu;
import net.estinet.gFeatures.Feature.gWarsSuite.Multiplayer.BlueTeam;
import net.estinet.gFeatures.Feature.gWarsSuite.Multiplayer.Damage;
import net.estinet.gFeatures.Feature.gWarsSuite.Multiplayer.Death;
import net.estinet.gFeatures.Feature.gWarsSuite.Multiplayer.Interact;
import net.estinet.gFeatures.Feature.gWarsSuite.Multiplayer.Move;
import net.estinet.gFeatures.Feature.gWarsSuite.Multiplayer.OrangeTeam;
import net.estinet.gFeatures.Feature.gWarsSuite.Multiplayer.Point;
import net.estinet.gFeatures.Feature.gWarsSuite.Multiplayer.Source;
import net.estinet.gFeatures.Feature.gWarsSuite.Multiplayer.Team;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.ItemStack;

import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;

import java.util.List;

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

public class EventHub implements Listener {
    Join mm = new Join();
    Statistics stats = new Statistics();
    Inventory inv = new Inventory();
    Interaction interact = new Interaction();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Connection c = new Connection();
        CheckConfig cc = new CheckConfig();
        c.Connect(c.toURL(cc.getPort(), cc.getAddress(), cc.getTablename()), cc.getUsername(), cc.getPassword(), "INSERT INTO Kills(Name, Kills)\n" +
                "SELECT * FROM (SELECT '" + event.getPlayer().getUniqueId() + "', '0') AS tmp\n" +
                "WHERE NOT EXISTS (\n" +
                "SELECT Name FROM Kills WHERE Name = '" + event.getPlayer().getUniqueId() + "'\n" +
                ") LIMIT 1;\n"
        );
        c.Connect(c.toURL(cc.getPort(), cc.getAddress(), cc.getTablename()), cc.getUsername(), cc.getPassword(), "INSERT INTO Deaths(Name, Deaths)\n" +
                "SELECT * FROM (SELECT '" + event.getPlayer().getUniqueId() + "', '0') AS tmp\n" +
                "WHERE NOT EXISTS (\n" +
                "SELECT Name FROM Deaths WHERE Name = '" + event.getPlayer().getUniqueId() + "'\n" +
                ") LIMIT 1;\n"
        );
        Source s = new Source();
        s.flushAll();
        stats.setMode(event.getPlayer(), gWarsMode.MAINMENU);
        mm.start(event);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        stats.setMode(p, gWarsMode.NONE);
        BlueTeam.removePlayer(event.getPlayer());
        OrangeTeam.removePlayer(event.getPlayer());
        Constants.spawndump.remove(p);
        Constants.capturetrigger.remove(event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerOpenInventory(InventoryOpenEvent event) {
        if (stats.getMode(event.getPlayer()).equals(gWarsMode.MAINMENU) || stats.getMode(event.getPlayer()).equals(gWarsMode.GUNMENU) || stats.getMode(event.getPlayer()).equals(gWarsMode.TEAMMENU) || stats.getMode((Player) event.getPlayer()).equals(gWarsMode.SPAWNMENU)) {
            inv.prevent(event);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (stats.getMode(event.getPlayer()).equals(gWarsMode.MAINMENU) || stats.getMode(event.getPlayer()).equals(gWarsMode.GUNMENU) || stats.getMode(event.getPlayer()).equals(gWarsMode.TEAMMENU) || stats.getMode(event.getPlayer()).equals(gWarsMode.SPAWNMENU)) {
            interact.intialize(event);
        } else if (stats.getMode(event.getPlayer()).equals(gWarsMode.TEAM)) {
            Interact interact = new Interact();
            interact.initialize(event);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            if (stats.getMode((Player) event.getEntity()).equals(gWarsMode.MAINMENU) || stats.getMode((Player) event.getEntity()).equals(gWarsMode.GUNMENU) || stats.getMode(event.getEntity()).equals(gWarsMode.TEAMMENU) || stats.getMode(event.getEntity()).equals(gWarsMode.SPAWNMENU)) {
                MenuDamage d = new MenuDamage();
                d.onEntityDamage(event);
            } else if (stats.getMode(event.getDamager()).equals(gWarsMode.TEAM)) {
                Damage d = new Damage();
                d.onEntityDamage(event);
            }
        }
    }

    @EventHandler
    public void onWeaponDamageEntity(WeaponDamageEntityEvent event) {
        if (event.getPlayer() instanceof Player && event.getVictim() instanceof Player) {
            if (stats.getMode((Player) event.getVictim()).equals(gWarsMode.MAINMENU) || stats.getMode((Player) event.getVictim()).equals(gWarsMode.GUNMENU) || stats.getMode(event.getVictim()).equals(gWarsMode.TEAMMENU) || stats.getMode(event.getVictim()).equals(gWarsMode.SPAWNMENU)) {
                MenuDamage d = new MenuDamage();
                d.onWeaponDamage(event);
            } else if (stats.getMode(event.getVictim()).equals(gWarsMode.TEAM)) {
                Damage d = new Damage();
                d.onWeaponDamage(event);
            }
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
		/*if(stats.getMode(event.getPlayer()).equals(gWarsMode.GUNMENU)){
			aapi.sendActionbar(event.getPlayer(), event.getPlayer().getInventory().getItem(event.getNewSlot()).getItemMeta().getLore().get(0));
		}*/
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (stats.getMode((Player) event.getWhoClicked()).equals(gWarsMode.MAINMENU) || stats.getMode((Player) event.getWhoClicked()).equals(gWarsMode.GUNMENU) || stats.getMode((Player) event.getWhoClicked()).equals(gWarsMode.TEAMMENU) || stats.getMode((Player) event.getWhoClicked()).equals(gWarsMode.SPAWNMENU)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        List<Block> blocks = e.blockList();
        for (Block b : blocks) {
            b.getDrops().clear();
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Source s = new Source();
        s.flushAll();
        if (stats.getMode(event.getPlayer()).equals(gWarsMode.TEAM)) {
            stats.setMode(event.getPlayer(), gWarsMode.SPAWNMENU);
            SpawnMenu sm = new SpawnMenu(event.getPlayer());
            sm.intialize();
            if (BlueTeam.hasPlayer(event.getPlayer())) {
                ItemStack wool = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS, 1);
                event.getPlayer().getInventory().setHelmet(wool);
            } else if (OrangeTeam.hasPlayer(event.getPlayer())) {
                ItemStack wool = new ItemStack(Material.ORANGE_STAINED_GLASS, 1);
                event.getPlayer().getInventory().setHelmet(wool);
            }
        }
        Join join = new Join();
        join.algore(event.getPlayer());
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (stats.getMode(event.getPlayer()).equals(gWarsMode.TEAM)) {
            Move move = new Move();
            move.initialize(event);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Death d = new Death();
        d.init(event);
        Player player = event.getEntity();
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getServer().getPluginManager().getPlugin("gFeatures"), () -> {
            if (event.getEntity().isDead())
                player.setHealth(20);
            player.setGameMode(GameMode.SPECTATOR);
            player.sendActionBar(ChatColor.RED + "Respawning in 5 seconds...");
        });
        Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getServer().getPluginManager().getPlugin("gFeatures"), () -> {
            Source s = new Source();
            s.flushAll();
            event.getEntity().setGameMode(GameMode.ADVENTURE);
            if (stats.getMode((Player) event.getEntity()).equals(gWarsMode.TEAM)) {
                stats.setMode(event.getEntity(), gWarsMode.SPAWNMENU);
                SpawnMenu sm = new SpawnMenu(event.getEntity());
                sm.intialize();
                if (BlueTeam.hasPlayer(event.getEntity())) {
                    ItemStack wool = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS, 1);
                    event.getEntity().getInventory().setHelmet(wool);
                } else if (OrangeTeam.hasPlayer(event.getEntity())) {
                    ItemStack wool = new ItemStack(Material.ORANGE_STAINED_GLASS, 1);
                    event.getEntity().getInventory().setHelmet(wool);
                }
            }
            Join join = new Join();
            join.algore(event.getEntity());
        }, 100L);
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        if (stats.getMode(event.getPlayer()).equals(gWarsMode.MAINMENU) || stats.getMode(event.getPlayer()).equals(gWarsMode.GUNMENU) || stats.getMode(event.getPlayer()).equals(gWarsMode.TEAMMENU) || stats.getMode((Player) event.getPlayer()).equals(gWarsMode.SPAWNMENU)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        for (Point point : Constants.multiplayerpossession.keySet()) {
            if (Constants.multiplayerpossession.get(point).equals(Team.BLUE)) {
                point.setBlue();
            } else if (Constants.multiplayerpossession.get(point).equals(Team.ORANGE)) {
                point.setOrange();
            } else if (Constants.multiplayerpossession.get(point).equals(Team.NEUTRAL)) {
                point.setNeutral();
            }
        }
    }
}
