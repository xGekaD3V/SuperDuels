package com.geka.duels.gui;

import org.bukkit.inventory.ItemStack;

public interface ICanHandleGUI {

    ItemStack toDisplay();

    boolean filter();

}
