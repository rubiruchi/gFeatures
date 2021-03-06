package net.estinet.gFeatures.Feature.Gliders;

import net.estinet.gFeatures.Feature.gRanks.gRanks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;

import net.estinet.gFeatures.API.Inventory.ClearInventory;
import net.estinet.gFeatures.API.Logger.Debug;
import net.estinet.gFeatures.Feature.Gliders.EventBase.Dead;
import net.estinet.gFeatures.Feature.Gliders.EventBase.FlagHit;
import net.estinet.gFeatures.Feature.Gliders.EventBase.Join;
import net.estinet.gFeatures.Feature.Gliders.EventBase.Leave;
import net.estinet.gFeatures.Feature.Gliders.Holo.WaitingMenu;

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
    Join join = new Join();
    Leave leave = new Leave();
    Dead d = new Dead();
    FlagHit fh = new FlagHit();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        try {
            String prefixs = gRanks.getRankOfPlayer(event.getPlayer(), false).getPrefix();
            String prefix = prefixs.replace('&', '§');
            event.setJoinMessage(ChatColor.GOLD + "[" + ChatColor.DARK_AQUA + "Join" + ChatColor.GOLD + "]" + ChatColor.RESET + " " + prefix + "" + ChatColor.WHITE + p.getName());
        } catch (Exception e) {
            event.setJoinMessage(ChatColor.GOLD + "[" + ChatColor.DARK_AQUA + "Join" + ChatColor.GOLD + "]" + ChatColor.RESET + " " + ChatColor.WHITE + p.getName());
        }
        join.init(event);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        leave.init(event);
    }

    @EventHandler
    public void onOpenInventory(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType().equals(EntityType.PLAYER)) {
            if (event.getEntityType().equals(EntityType.PLAYER)) {
                Player p = (Player) event.getEntity();
                if (Basic.modes.get(p.getUniqueId()).equals(PlayerMode.WAITING)) {
                    event.setCancelled(true);
                } else if (Basic.modes.get(p.getUniqueId()).equals(PlayerMode.INGAME)) {
                    if (event.getDamager().getType().equals(EntityType.PLAYER)) {
                        Player pl = (Player) event.getDamager();
                        if (((Basic.teams.get(p.getUniqueId()).equals(Team.BLUE) && Basic.teams.get(pl.getUniqueId()).equals(Team.BLUE))) || ((Basic.teams.get(p.getUniqueId()).equals(Team.ORANGE) && Basic.teams.get(pl.getUniqueId()).equals(Team.ORANGE)))) {
                            event.setCancelled(true);
                        } else {
                            int health = (int) p.getHealth();
                            double damage = event.getDamage();
                            if (health - damage <= 0) {
                                ClearInventory.clearInv(p);
                                event.setCancelled(true);
                                int deaths = Basic.deaths.get(p.getUniqueId());
                                deaths += 1;
                                Basic.deaths.remove(p.getUniqueId());
                                Basic.deaths.put(p.getUniqueId(), deaths);

                                int kills = Basic.kills.get(pl.getUniqueId());
                                kills += 1;
                                Basic.kills.remove(pl.getUniqueId());
                                Basic.kills.put(pl.getUniqueId(), kills);

                                Bukkit.broadcastMessage(ChatColor.AQUA + "[" + ChatColor.GOLD + "Kill" + ChatColor.AQUA + "] " + ChatColor.DARK_AQUA + ((Player) event.getDamager()).getDisplayName() + " killed " + ((Player) event.getEntity()).getDisplayName() + "!");

                                d.init(p);
                            }
                        }
                    }
                }
            }
            if (event.getEntity().getType().equals(EntityType.ENDER_CRYSTAL)) {
                event.setCancelled(true);
                try {
                    fh.init(event.getEntity().getLocation(), (Player) event.getDamager());
                } catch (ClassCastException e) {
                }
            }
        } else {
            if (event.getEntity().getType().equals(EntityType.ENDER_CRYSTAL)) {
                event.setCancelled(true);
                try {
                    fh.init(event.getEntity().getLocation(), (Player) event.getDamager());
                } catch (ClassCastException e) {
                }
            }
        }
    }

    @EventHandler
    public void onWeaponDamageEntity(WeaponDamageEntityEvent event) {
        if (event.getVictim().getType().equals(EntityType.PLAYER)) {
            Player p = (Player) event.getVictim();
            if (Basic.modes.get(p.getUniqueId()).equals(PlayerMode.WAITING)) {
                event.setCancelled(true);
            } else if (Basic.modes.get(p.getUniqueId()).equals(PlayerMode.INGAME)) {
                Player pl = (Player) event.getPlayer(); // CHECK IT OUT
                if ((Basic.isInBlue(p) && Basic.isInBlue(pl)) || (Basic.isInOrange(p) && Basic.isInOrange(pl))) {
                    event.setCancelled(true);
                    if (Basic.teams.get(p.getUniqueId()).equals(Team.BLUE) && Basic.teams.get(pl.getUniqueId()).equals(Team.BLUE)) {
                        Debug.print("[Gliders] Both are in blue.");
                    } else if (Basic.teams.get(p.getUniqueId()).equals(Team.ORANGE) && Basic.teams.get(pl.getUniqueId()).equals(Team.ORANGE)) {
                        Debug.print("[Gliders] Both are in orange.");
                    }
                    Debug.print("[Gliders] Prevented team kill.");
                } else {
                    int health = (int) p.getHealth();
                    double damage = event.getDamage();
                    if (health - damage <= 0) {
                        ClearInventory.clearInv(p);
                        int deaths = Basic.deaths.get(p.getUniqueId());
                        deaths += 1;
                        Basic.deaths.remove(p.getUniqueId());
                        Basic.deaths.put(p.getUniqueId(), deaths);
                        int kills = Basic.kills.get(pl.getUniqueId());
                        kills += 1;
                        Basic.kills.remove(pl.getUniqueId());
                        Basic.kills.put(pl.getUniqueId(), kills);
                        Bukkit.broadcastMessage(ChatColor.AQUA + "[" + ChatColor.GOLD + "Kill" + ChatColor.AQUA + "] " + ChatColor.DARK_AQUA + event.getPlayer().getDisplayName() + " killed " + ((Player) event.getVictim()).getDisplayName() + "!");
                        event.setCancelled(true);
                        d.init(p);
                    }
                }
            }
        } else {
            if (event.getVictim().getType().equals(EntityType.ENDER_CRYSTAL)) {
                event.setCancelled(true);
                fh.init(event.getVictim().getLocation(), (Player) event.getDamager());
            }
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickup(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (Basic.modes.get(event.getPlayer().getUniqueId()).equals(PlayerMode.WAITING)) {
            WaitingMenu wm = new WaitingMenu();
            wm.init(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            for (ItemStack im : event.getDrops()) {
                event.getDrops().remove(im);
            }
            if (Basic.modes.get(event.getEntity().getUniqueId()).equals(PlayerMode.WAITING)) {
                event.getEntity().setHealth(20);

                event.getEntity().teleport(Basic.waitspawn);
                Player player = event.getEntity();
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getServer().getPluginManager().getPlugin("gFeatures"), new Runnable() {
                    public void run() {
                        if (event.getEntity().isDead())
                            player.setHealth(20);
                    }
                });
            } else if (Basic.modes.get(event.getEntity().getUniqueId()).equals(PlayerMode.INGAME)) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getServer().getPluginManager().getPlugin("gFeatures"), new Runnable() {
                    public void run() {
                        if (event.getEntity().isDead())
                            event.getEntity().setHealth(20);
                    }
                });
                ClearInventory.clearInv(event.getEntity());
                for (int i = 0; i < event.getDrops().size(); i++) {
                    event.getDrops().set(i, new ItemStack(Material.AIR));
                }
                d.init(event.getEntity());
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        ((Player) event.getEntity()).setFoodLevel(20);
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            int health = (int) p.getHealth();
            double damage = event.getDamage();
            if (health - damage <= 0) {
                if (Basic.modes.get(event.getEntity().getUniqueId()).equals(PlayerMode.WAITING)) {
                    p.setHealth(20);

                    p.teleport(Basic.waitspawn);
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getServer().getPluginManager().getPlugin("gFeatures"), () -> {
                        if (event.getEntity().isDead())
                            p.setHealth(20);
                    });
                } else if (Basic.modes.get(p.getUniqueId()).equals(PlayerMode.INGAME)) {
                    p.getInventory().clear();
                    int deaths = Basic.deaths.get(p.getUniqueId());
                    deaths += 1;
                    Basic.deaths.remove(p.getUniqueId());
                    Basic.deaths.put(p.getUniqueId(), deaths);
                    Bukkit.broadcastMessage(ChatColor.GOLD + "[" + ChatColor.DARK_AQUA + "Death" + ChatColor.GOLD + "] " + ChatColor.DARK_AQUA + p.getDisplayName() + " died. .-.");
                    event.setCancelled(true);
                    d.init(p);
                }
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntityType().equals(EntityType.ENDER_CRYSTAL)) {
            event.setCancelled(true);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getServer().getPluginManager().getPlugin("gFeatures"), () -> Bukkit.getWorld(Basic.mapName).spawn(Basic.flag, EnderCrystal.class), 20L);
        }
    }
}
