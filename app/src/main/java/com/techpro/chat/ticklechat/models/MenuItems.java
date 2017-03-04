package com.techpro.chat.ticklechat.models;

/**
 * Created by vishalrandive on 15/02/16.
 */
public class MenuItems {
    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public int getMenuItemDrawable() {
        return menuItemDrawable;
    }

    public void setMenuItemDrawable(int menuItemDrawable) {
        this.menuItemDrawable = menuItemDrawable;
    }

    public String getMenuItemAlert() {
        return menuItemAlert;
    }

    public void setMenuItemAlert(String menuItemAlert) {
        this.menuItemAlert = menuItemAlert;
    }

    private String menuItemName;
    private int menuItemDrawable;
    private String menuItemsDrawableTxt;

    public String getMenuItemsDrawableTxt() {
        return menuItemsDrawableTxt;
    }

    public void setMenuItemsDrawableTxt(String menuItemsDrawableTxt) {
        this.menuItemsDrawableTxt = menuItemsDrawableTxt;
    }

    private String menuItemAlert;

    public String getMenuItemSubTitles() {
        return menuItemSubTitles;
    }

    public void setMenuItemSubTitles(String menuItemSubTitles) {
        this.menuItemSubTitles = menuItemSubTitles;
    }

    private String menuItemSubTitles;

}
