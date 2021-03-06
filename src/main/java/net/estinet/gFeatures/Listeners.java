package net.estinet.gFeatures;

import net.estinet.gFeatures.API.Logger.Debug;
import net.estinet.gFeatures.API.PlayerStats.Load;
import net.estinet.gFeatures.API.PlayerStats.gPlayer;
import net.estinet.gFeatures.ClioteSky.ClioteSky;
import net.estinet.gFeatures.Configuration.LoadConfig;
import net.estinet.gFeatures.Configuration.SetupConfig;
import net.estinet.gFeatures.SQL.Update.Entrly;
import net.estinet.gFeatures.SQL.Update.Obtain;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.world.WorldLoadEvent;

import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;

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

public class Listeners extends JavaPlugin implements Listener {
    public static final String version = "4.0.0";
    public static boolean debug = false;

    Enabler enable = new Enabler();
    Disabler disable = new Disabler();
    CommandLibrary commands = new CommandLibrary();
    Setup setup = new Setup();
    Load load = new Load();
    Entrly entrly = new Entrly();
    gLoop gl = new gLoop();
    net.estinet.gFeatures.API.PlayerStats.Setup setups = new net.estinet.gFeatures.API.PlayerStats.Setup();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("_________________________________________________________________________");
        getLogger().info("Starting gFeatures.");
        getLogger().info("Current version: " + version);
        getLogger().info("Starting modules!");
        try {
            setup.onSetup();
            SetupConfig.setup();
            LoadConfig.load();
            ClioteSky.initClioteSky();
        } catch (Exception e) {
            e.printStackTrace();
        }
        enable.onEnable();
        gFeatures.addPlayerSection("Setup", "DO NOT REMOVE!");
        try {
            load.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Obtain.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        gl.start();
        getLogger().info("Complete!");
        getLogger().info("_________________________________________________________________________");
    }

    @Override
    public void onDisable() {
        getLogger().info("_________________________________________________________________________");
        getLogger().info("Stopping gFeatures!");
        getLogger().info("Current version: " + version);
        getLogger().info("Turning off modules!");
        disable.onDisable();
        getLogger().info("Complete!");
        getLogger().info("_________________________________________________________________________");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Thread thr = new Thread(() -> {
            try {
                setups.checkPlayer(event.getPlayer());
                Debug.print("Player API initialized for " + event.getPlayer().getName());
            } catch (Exception e) {
                Debug.print(e.getMessage());
            }
            try {
                entrly.join(event.getPlayer());
            } catch (Exception e) {
                Debug.print(e.getMessage());
            }
        }
        );
        thr.start();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Thread thr = new Thread(() -> {
            try {
                gPlayer gp = gFeatures.getgPlayer(event.getPlayer().getUniqueId().toString());
                for (String valuename : gp.getValues().keySet()) {
                    setups.smartFlush(gp, valuename, gp.getValue(valuename));
                }
                gFeatures.setgPlayer(gFeatures.getgPlayer(event.getPlayer().getUniqueId().toString()), gp);
            } catch (Exception e) {
                Debug.print(e.getMessage());
            }
        });
        thr.start();
    }

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
        commands.Commands(sender, cmd, label, args);
        return true;
    }
}
