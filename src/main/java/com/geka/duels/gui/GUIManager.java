package com.geka.duels.gui;

import com.geka.duels.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class GUIManager implements Listener {

    private final List<GUI> registered = new ArrayList<>();

    public GUIManager(Core instance) {
        Bukkit.getPluginManager().registerEvents(this, instance);
    }

    public void register(GUI gui) {
        registered.add(gui);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void on(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory top = player.getOpenInventory().getTopInventory();
        Inventory clicked = event.getClickedInventory();

        if (top == null || clicked == null) {
            return;
        }

        for (GUI gui : registered) {
            if (!gui.isPage(top)) {
                continue;
            }

            event.setCancelled(true);

            if (!clicked.equals(top)) {
                continue;
            }

            gui.getListener().onClick(event);
        }
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void on(InventoryCloseEvent event) {
        for (GUI gui : registered) {
            if (gui.isPage(event.getInventory())) {
                gui.getListener().onClose(event);
            }
        }
    }
}
