package com.ffapp.ffxx.ffplayers.comman;

public class ModelMenu {
    String menuText;
    int menuIcon;

    public ModelMenu(String menuText, int menuIcon) {
        this.menuText = menuText;
        this.menuIcon = menuIcon;
    }

    public String getMenuText() {
        return menuText;
    }

    public int getMenuIcon() {
        return menuIcon;
    }
}
