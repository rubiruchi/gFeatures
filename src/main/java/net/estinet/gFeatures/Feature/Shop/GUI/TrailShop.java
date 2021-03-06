package net.estinet.gFeatures.Feature.Shop.GUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.estinet.gFeatures.API.Inventory.InventoryAPI;
import net.estinet.gFeatures.Feature.Shop.SetTrail;
import net.estinet.gFeatures.Feature.Shop.Shop;
import net.estinet.gFeatures.Feature.Shop.Enums.Trails;

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

public class TrailShop {
    public void init(Player player) {
        InventoryAPI open = makeInventory(player);
        open.open(player);
        player.updateInventory();
    }

    public InventoryAPI makeInventory(Player p) {
        try {
            InventoryAPI menu = new InventoryAPI(ChatColor.GOLD + "Trails", 18, event -> {
                if (event.getPosition() == 8) {
                    SetTrail st = new SetTrail();
                    st.init(p, Trails.NONE);
                } else if (event.getPosition() == 0) {
                    SetTrail st = new SetTrail();
                    st.init(p, Trails.FIRE);
                } else if (event.getPosition() == 1) {
                    SetTrail st = new SetTrail();
                    st.init(p, Trails.WATER);
                } else if (event.getPosition() == 2) {
                    SetTrail st = new SetTrail();
                    st.init(p, Trails.GRAY);
                } else if (event.getPosition() == 3) {
                    SetTrail st = new SetTrail();
                    st.init(p, Trails.MUSIC);
                } else if (event.getPosition() == 9) {
                    SetTrail st = new SetTrail();
                    st.init(p, Trails.RAINBOW);
                }
                event.setWillClose(true);
                event.setWillDestroy(true);
            }, Bukkit.getServer().getPluginManager().getPlugin("gFeatures"));

            //Fire Trail
            if (!Shop.getTrail(p.getUniqueId().toString(), Trails.FIRE.toString()).equalsIgnoreCase("true")) {
                menu.setOption(0, createItem(Material.CLAY_BALL, getText(p, Trails.FIRE) + "Fire Trail", ChatColor.GRAY + "Who doesn't like fire?", ChatColor.BLUE + "Costs 450 clupic."));
            } else {
                if (Shop.playerTrail.get(p.getUniqueId()).equals("FIRE")) {
                    menu.setOption(0, createItem(Material.BLAZE_POWDER, getText(p, Trails.FIRE) + "Fire Trail", ChatColor.GRAY + "Who doesn't like fire?", ChatColor.GREEN + "Currently enabled!"));
                } else {
                    menu.setOption(0, createItem(Material.BLAZE_POWDER, getText(p, Trails.FIRE) + "Fire Trail", ChatColor.GRAY + "Who doesn't like fire?", ChatColor.GREEN + "Click to ENABLE."));
                }
            }
            //Water Trail
            if (!Shop.getTrail(p.getUniqueId().toString(), Trails.WATER.toString()).equalsIgnoreCase("true")) {
                menu.setOption(1, createItem(Material.CLAY_BALL, getText(p, Trails.WATER) + "Water Trail", ChatColor.GRAY + "Bubbly watery goodness!", ChatColor.BLUE + "Costs 450 clupic."));
            } else {
                if (Shop.playerTrail.get(p.getUniqueId()).equals("WATER")) {
                    menu.setOption(1, createItem(Material.WATER_BUCKET, getText(p, Trails.WATER) + "Water Trail", ChatColor.GRAY + "Bubbly watery goodness!", ChatColor.GREEN + "Currently enabled!"));
                } else {
                    menu.setOption(1, createItem(Material.WATER_BUCKET, getText(p, Trails.WATER) + "Water Trail", ChatColor.GRAY + "Bubbly watery goodness!", ChatColor.GREEN + "Click to ENABLE."));
                }
            }
            //Grey Trail
            if (!Shop.getTrail(p.getUniqueId().toString(), Trails.GRAY.toString()).equalsIgnoreCase("true")) {
                menu.setOption(2, createItem(Material.CLAY_BALL, getText(p, Trails.GRAY) + "Grey Trail", ChatColor.GRAY + "Coloured trail!", ChatColor.BLUE + "Costs 250 clupic."));
            } else {
                if (Shop.playerTrail.get(p.getUniqueId()).equals("GRAY")) {
                    menu.setOption(2, createItem(Material.INK_SAC, getText(p, Trails.GRAY) + "Grey Trail", 8, ChatColor.GRAY + "Coloured trail!", ChatColor.GREEN + "Currently enabled!"));
                } else {
                    menu.setOption(2, createItem(Material.INK_SAC, getText(p, Trails.GRAY) + "Grey Trail", 8, ChatColor.GRAY + "Coloured trail!", ChatColor.GREEN + "Click to ENABLE."));
                }
            }
            //Music Trail
            if (!Shop.getTrail(p.getUniqueId().toString(), Trails.MUSIC.toString()).equalsIgnoreCase("true")) {
                menu.setOption(3, createItem(Material.CLAY_BALL, getText(p, Trails.MUSIC) + "Music Trail", ChatColor.GRAY + "Blasting musical notes since the 90s!", ChatColor.BLUE + "Costs 350 clupic."));
            } else {
                if (Shop.playerTrail.get(p.getUniqueId()).equals("MUSIC")) {
                    menu.setOption(3, createItem(Material.LEGACY_RECORD_3, getText(p, Trails.MUSIC) + "Music Trail", ChatColor.GRAY + "Blasting musical notes since the 90s!", ChatColor.GREEN + "Currently enabled!"));
                } else {
                    menu.setOption(3, createItem(Material.LEGACY_RECORD_3, getText(p, Trails.MUSIC) + "Music Trail", ChatColor.GRAY + "Blasting musical notes since the 90s!", ChatColor.GREEN + "Click to ENABLE."));
                }
            }
            //Rainbow Trail
            if (!p.hasPermission("gFeatures.Esti+")) {
                menu.setOption(9, createItem(Material.CLAY_BALL, getText(p, Trails.RAINBOW) + "Rainbow Trail", ChatColor.GRAY + "Rainbows are da greatest!", ChatColor.BLUE + "Esti+ and higher."));
            } else {
                if (Shop.playerTrail.get(p.getUniqueId()).equals("RAINBOW")) {
                    menu.setOption(9, createItem(Material.BREWING_STAND, ChatColor.GREEN + "Rainbow Trail", ChatColor.GRAY + "Rainbows are da greatest!", ChatColor.GREEN + "Currently enabled!"));
                } else {
                    menu.setOption(9, createItem(Material.BREWING_STAND, ChatColor.GREEN + "Rainbow Trail", ChatColor.GRAY + "Rainbows are da greatest!", ChatColor.GREEN + "Click to ENABLE."));
                }
            }

            menu.setOption(8, createItem(Material.BARRIER, ChatColor.AQUA + "" + ChatColor.STRIKETHROUGH + "<---" + ChatColor.RESET + ChatColor.RED + "No Trail" + ChatColor.RESET + ChatColor.AQUA + "" + ChatColor.STRIKETHROUGH + "--->"));

            return menu;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ChatColor getText(Player p, Trails trail) {
        if (Shop.getTrail(p.getUniqueId().toString(), trail.toString()).equalsIgnoreCase("true")) {
            return ChatColor.GREEN;
        } else {
            return ChatColor.RED;
        }
    }

    public ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material, 1);
        List<String> lores = new ArrayList<>();
        for (String lor : lore) {
            lores.add(lor);
        }
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lores);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createItem(Material material, String name, int met, String... lore) {
        ItemStack item = new ItemStack(material, 1, (short) met);
        List<String> lores = new ArrayList<>();
        for (String lor : lore) {
            lores.add(lor);
        }
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lores);
        item.setItemMeta(meta);
        return item;
    }
}
