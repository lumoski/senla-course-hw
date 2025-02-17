package com.hotel.controller.console.ui;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Menu {
    
    private String name = "Hotel administrator";
    private List<MenuItem> menuItems = new ArrayList<>();

    public Menu addMenuItem(MenuItem menuItem) {
        this.menuItems.add(menuItem);
        return this;
    }
}
