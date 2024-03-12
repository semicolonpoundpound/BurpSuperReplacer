package scpp.superreplacer;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;
import burp.api.montoya.ui.menu.MenuItem;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MyContextMenuItemsProvider implements ContextMenuItemsProvider {
    private MontoyaApi api;
    private MainWindow main;
    public MyContextMenuItemsProvider(MontoyaApi api, MainWindow main)
    {
        this.api = api;
        this.main = main;
    }
    @Override
    public List<Component> provideMenuItems(ContextMenuEvent event) {

        List<Component> items = new ArrayList<Component>();

        int count = 0;
        // for tab in this.tabs
        for (ReplacerTab current_tab : this.main.getReplacerTabs()) {

            JMenuItem jitem = new JMenuItem("Tab" + String.valueOf(++count) + ": Use as Dynamic Request");

            MenuItemActionListener sendToSuperReplacerListener = new MenuItemActionListener(this.api, current_tab,
                    event);

            jitem.addActionListener(sendToSuperReplacerListener);

            items.add(jitem);
        }

        return items;
    }
}
