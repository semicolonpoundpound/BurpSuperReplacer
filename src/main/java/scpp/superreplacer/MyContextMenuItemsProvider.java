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
    MontoyaApi api;
    ReplacerTab tab;
    public MyContextMenuItemsProvider(MontoyaApi api, ReplacerTab tab)
    {
        this.api = api;
        this.tab = tab;
    }
    @Override
    public List<Component> provideMenuItems(ContextMenuEvent event) {

        JMenuItem jitem = new JMenuItem("Use as Dynamic Request");

        MenuItemActionListener sendToSuperReplacerListener = new MenuItemActionListener(this.api, this.tab,
                event);

        jitem.addActionListener(sendToSuperReplacerListener);

        List<Component> items = new ArrayList<Component>();

        items.add(jitem);

        return items;
    }
}
