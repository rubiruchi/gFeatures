package net.estinet.gFeatures.Feature.CTF;

import net.estinet.gFeatures.Feature.CTF.EventBase.GameFunc.Respawn;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
import net.estinet.gFeatures.Feature.CTF.EventBase.Dead;
import net.estinet.gFeatures.Feature.CTF.EventBase.FlagHit;
import net.estinet.gFeatures.Feature.CTF.EventBase.Join;
import net.estinet.gFeatures.Feature.CTF.EventBase.Leave;
import net.estinet.gFeatures.Feature.CTF.Holo.SpawnMenu;
import net.estinet.gFeatures.Feature.CTF.Holo.WaitingMenu;

/*
gFeatures
https://github.com/EstiNet/gFeatures

   Copyright 2017 EstiNet

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

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(ChatColor.GOLD + "[" + ChatColor.DARK_AQUA + "Join" + ChatColor.GOLD + "]" + ChatColor.RESET + " " + ChatColor.WHITE + event.getPlayer().getName());
        Join.init(event);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Leave.init(event);
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
                if (Basic.modes.get(p.getUniqueId()).equals(PlayerMode.WAITING) || Basic.modes.get(p.getUniqueId()).equals(PlayerMode.SELECT)) {
                    event.setCancelled(true);
                } else if (Basic.modes.get(p.getUniqueId()).equals(PlayerMode.INGAME)) {
                    Player pl = (Player) event.getDamager();
                    if (((Basic.teams.get(p.getUniqueId()).equals(Team.BLUE) && Basic.teams.get(pl.getUniqueId()).equals(Team.BLUE))) || ((Basic.teams.get(p.getUniqueId()).equals(Team.ORANGE) && Basic.teams.get(pl.getUniqueId()).equals(Team.ORANGE)))) {
                        event.setCancelled(true);
                    } else {
                        int health = (int) p.getHealth();
                        double damage = event.getDamage();
                        if (health - damage <= 0) {
                            ClearInventory.clearInv(p);
                            event.setCancelled(true);
                            int deaths = Basic.deaths.get(p.getUniqueId())+1;
                            Basic.deaths.put(p.getUniqueId(), deaths);

                            int kills = Basic.kills.get(pl.getUniqueId()) + 1;
                            Basic.kills.put(pl.getUniqueId(), kills);

                            Bukkit.broadcastMessage(ChatColor.AQUA + "[" + ChatColor.GOLD + "Kill" + ChatColor.AQUA + "] " + ChatColor.DARK_AQUA + event.getDamager().getName() + " killed " + event.getEntity().getName() + "!");

                            Dead.init(p);
                        }
                    }
                }
            } else {
                if (event.getEntity().getType().equals(EntityType.ENDER_CRYSTAL)) {
                    event.setCancelled(true);
                    FlagHit.processEvent(event.getEntity().getLocation(), (Player) event.getDamager());
                }
            }
        } else {
            if (event.getEntity().getType().equals(EntityType.ENDER_CRYSTAL)) {
                event.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void onWeaponDamageEntity(WeaponDamageEntityEvent event) {
        if (event.getVictim().getType().equals(EntityType.PLAYER)) {
            Player p = (Player) event.getVictim();
            if (Basic.modes.get(p.getUniqueId()).equals(PlayerMode.WAITING) || Basic.modes.get(p.getUniqueId()).equals(PlayerMode.SELECT)) {
                event.setCancelled(true);
            } else if (Basic.modes.get(p.getUniqueId()).equals(PlayerMode.INGAME)) {
                Player pl = event.getPlayer(); // CHECK IT OUT
                if ((Basic.isInBlue(p) && Basic.isInBlue(pl)) || (Basic.isInOrange(p) && Basic.isInOrange(pl))) {
                    event.setCancelled(true);
                    if (Basic.teams.get(p.getUniqueId()).equals(Team.BLUE) && Basic.teams.get(pl.getUniqueId()).equals(Team.BLUE)) {
                        Debug.print("[CTF] Both are in blue.");
                    } else if (Basic.teams.get(p.getUniqueId()).equals(Team.ORANGE) && Basic.teams.get(pl.getUniqueId()).equals(Team.ORANGE)) {
                        Debug.print("[CTF] Both are in orange.");
                    }
                    Debug.print("[CTF] Prevented team kill.");
                } else {
                    int health = (int) p.getHealth();
                    double damage = event.getDamage();
                    if (health - damage <= 0) {
                        ClearInventory.clearInv(p);
                        int deaths = Basic.deaths.get(p.getUniqueId()) + 1;
                        Basic.deaths.put(p.getUniqueId(), deaths);
                        Debug.print("[CTF] first deaths:" + (deaths - 1) + " afterdeaths: " + deaths);

                        int kills = Basic.kills.get(pl.getUniqueId()) + 1;
                        Basic.kills.put(pl.getUniqueId(), kills);
                        Debug.print("[CTF] first kills:" + (kills - 1) + " afterkill: " + kills);
                        Bukkit.broadcastMessage(ChatColor.AQUA + "[" + ChatColor.GOLD + "Kill" + ChatColor.AQUA + "] " + ChatColor.DARK_AQUA + event.getPlayer().getDisplayName() + " killed " + ((Player) event.getVictim()).getDisplayName() + "!");
                        event.setCancelled(true);
                        Dead.init(p);
                    }
                }
            }
        } else {
            if (event.getVictim().getType().equals(EntityType.ENDER_CRYSTAL)) {
                event.setCancelled(true);
                FlagHit.processEvent(event.getVictim().getLocation(), (Player) event.getDamager());
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
        if (Basic.modes.get(event.getPlayer().getUniqueId()).equals(PlayerMode.SELECT)) {
            SpawnMenu.interact(event);
        } else if (Basic.modes.get(event.getPlayer().getUniqueId()).equals(PlayerMode.WAITING)) {
            WaitingMenu wm = new WaitingMenu();
            wm.init(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity() != null) {
            if (event.getEntity().getInventory().contains(Material.LIGHT_BLUE_STAINED_GLASS)) {
                event.getDrops().remove(new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS));
            }
            if (event.getEntity().getInventory().contains(Material.ORANGE_STAINED_GLASS)) {
                event.getDrops().remove(new ItemStack(Material.ORANGE_STAINED_GLASS));
            }
            if (Basic.modes.get(event.getEntity().getUniqueId()).equals(PlayerMode.WAITING)) {
                event.getEntity().setHealth(20);

                event.getEntity().teleport(Basic.waitspawn);
                Player player = event.getEntity();
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getServer().getPluginManager().getPlugin("gFeatures"), () -> {
                    if (event.getEntity().isDead())
                        player.setHealth(20);
                });
            } else if (Basic.modes.get(event.getEntity().getUniqueId()).equals(PlayerMode.SELECT)) {
                event.getEntity().setHealth(20);

                if (Basic.teams.get(event.getEntity().getUniqueId()).equals(Team.BLUE)) {
                    event.getEntity().teleport(Basic.bluespawn);
                } else if (Basic.teams.get(event.getEntity().getUniqueId()).equals(Team.ORANGE)) {
                    event.getEntity().teleport(Basic.orangespawn);
                }
                Player player = event.getEntity();
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getServer().getPluginManager().getPlugin("gFeatures"), () -> {
                    Respawn.respawn(player);
                    if (event.getEntity().isDead())
                        player.setHealth(20);
                });
            } else if (Basic.modes.get(event.getEntity().getUniqueId()).equals(PlayerMode.INGAME)) {

                ClearInventory.clearInv(event.getEntity());
                for (int i = 0; i < event.getDrops().size(); i++) {
                    event.getDrops().set(i, new ItemStack(Material.AIR));
                }
                Dead.init(event.getEntity());
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getServer().getPluginManager().getPlugin("gFeatures"), () -> {
                    if (event.getEntity().isDead())
                        event.getEntity().setHealth(20);
                });
            }
        }
    }

    @EventHandler
    public void onSandFall(EntityChangeBlockEvent event) {
        if (event.getEntityType() == EntityType.FALLING_BLOCK && event.getTo() == Material.AIR) {
            if (event.getBlock().getType() == Material.SAND) {
                event.setCancelled(true);
                event.getBlock().getState().update(false, false);
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        ((Player) event.getEntity()).setFoodLevel(20);
        event.setCancelled(true);
    }
}
