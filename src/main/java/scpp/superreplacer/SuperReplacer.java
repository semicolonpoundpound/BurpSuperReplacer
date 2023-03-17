package scpp.superreplacer;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.handler.HttpResponseReceived;
import burp.api.montoya.ui.UserInterface;
import burp.api.montoya.ui.menu.MenuItem;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;
import burp.api.montoya.ui.editor.HttpRequestEditor;
import burp.api.montoya.ui.editor.HttpResponseEditor;
import burp.api.montoya.ui.editor.Editor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.TitledBorder;

import static burp.api.montoya.ui.editor.EditorOptions.READ_ONLY;

public class SuperReplacer implements BurpExtension {
    private MontoyaApi api;

    private final String EXTENSION_NAME = "Super Replacer";
    private final String MATCH_PANE_TITLE = "Parameters";
    private final String MATCH_PANE_LABEL = "Search & Replace";
    private final String TOOL_PANE_TITLE = "Settings";
    private final String TOOL_PANE_LABEL = "Tools";

    private final String REQ_PANE_TITLE = "Dynamic Request";
    private final String REQ_PANE_LABEL = "Dynamic Value";
    @Override
    public void initialize(MontoyaApi api)
    {
        this.api = api;
        api.extension().setName(EXTENSION_NAME);

        ReplacerTab mainTab = new ReplacerTab(this.api);

        ContextMenuItemsProvider contextProvider = new MyContextMenuItemsProvider(this.api, mainTab);

        this.api.userInterface().registerSuiteTab(EXTENSION_NAME, mainTab.getTabUI());
        this.api.http().registerHttpHandler(new MyHttpHandler(this.api, mainTab));
        this.api.userInterface().registerContextMenuItemsProvider(contextProvider);
    }
}
