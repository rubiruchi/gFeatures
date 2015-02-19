package tk.genesishub.gFeatures.Listeners;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;

public class Listeners extends JavaPlugin implements Listener{
	@Override
    public void onEnable() { //What to do on server load/reload
        getLogger().info("[gFeatures] Starting gFeatures...");
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this, this);
        getLogger().info("[gFeatures] Complete! gFeatures has loaded. All clear for take off!");    
        }
 
    @Override
    public void onDisable() { //What to do on server unload/reload
       getLogger().info("[gFeatures] gFeatures is turning off...!");  
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
    	ListenersHub lh = new ListenersHub(event);
    	lh.PlayerJoinInitialize();
    }
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
    	ListenersHub lh = new ListenersHub(event);
    	lh.PlayerQuitInitialize();
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
    	ListenersHub lh = new ListenersHub(event);
    	lh.PlayerMoveInitialize();
    }
    @EventHandler(priority=EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event){
    	ListenersHub lh = new ListenersHub(event);
    	lh.PlayerRespawnInitialize();
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event){
    	ListenersHub lh = new ListenersHub(event);
    	lh.PlayerDeathInitialize();
    }
    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
    	ListenersHub lh = new ListenersHub(event);
    	lh.EntityDamageEntityInitialize();
    }
    @EventHandler
    public void onHit1(WeaponDamageEntityEvent event) {
        ListenersHub lh = new ListenersHub(event);
        lh.WeaponDamageEntityInitialize();
    }
    @EventHandler
    public void PlayerInteract(PlayerInteractEvent event){
        ListenersHub lh = new ListenersHub(event);
        lh.PlayerInteractInitialize();
    }
    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
        return true;
    }
}
