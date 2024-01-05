package scpp.superreplacer;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainWindow {

    private final MontoyaApi api;
    private final Logging logging;
    private ArrayList<ReplacerTab> tabs;

    private final int PRETABS = 1; // num of tabs before first replacer
    private final int POSTTABS = 1; //num of tabs after last replacer
    private final String HELP_TAB_TITLE = "Settings";

    public MainWindow(MontoyaApi api, ArrayList<ReplacerTab> allTabs) {
        this.api = api;
        this.logging = api.logging();
        this.tabs = allTabs;
    }

    public Component getTabUI()
    {
        MainWindow that = this;

        JTabbedPane tabPanel = new JTabbedPane();

        HelpTab helpTab = new HelpTab(this.api, this.tabs);
        tabPanel.addTab(HELP_TAB_TITLE, helpTab.getTabUI());

        for(int i = 0; i < this.tabs.size(); i++) {
            String title = Integer.toString(i+1);
            tabPanel.addTab(title, this.tabs.get(i).getTabUI());
            this.createCloseButton(this, tabPanel, i);
        }

        this.createAddButton(this, tabPanel);

        return tabPanel;
    }

    private void createCloseButton(MainWindow main, JTabbedPane tabPanel, int i) {
        String title = Integer.toString(i+1);
        JPanel pnlTab = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
        pnlTab.setOpaque(false);
        JLabel lblTitle = new JLabel(title);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        JButton btnClose = new JButton("x");
        btnClose.setBorder(null);
        btnClose.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        btnClose.setContentAreaFilled(false);
//            btnClose.setBorderPainted(false);
//            btnClose.setMargin(new Insets(2,2,2,2));

        ActionListener listener = e -> {
            // logging.logToOutput("remove i: " + String.valueOf(i));
            if (i >= 0) {
                main.tabs.remove(i);
                tabPanel.removeTabAt(i+PRETABS);
                for(int j = i; j < main.tabs.size(); j++) {
                    this.createCloseButton(main, tabPanel, j);
                }
            }
        };

        btnClose.addActionListener(listener);

        pnlTab.add(lblTitle);
        pnlTab.add(btnClose);

        tabPanel.setTabComponentAt(i+1, pnlTab);
    }

    private void createAddButton(MainWindow main, JTabbedPane tabPanel) {
        FlowLayout f = new FlowLayout(FlowLayout.CENTER, 5, 0);

        // Make a small JPanel with the layout and make it non-opaque
        JPanel pnlTab = new JPanel (f);
        pnlTab.setOpaque (false);
        // Create a JButton for adding the tabs
        JButton addTab = new JButton ("+");
        addTab.setOpaque (false); //
        addTab.setBorder (null);
        addTab.setContentAreaFilled (false);
        addTab.setFocusPainted (false);
        addTab.setFocusable (false);
        pnlTab.add (addTab);

        ActionListener listener = e -> {
            ReplacerTab newTab = new ReplacerTab(main.api);
            main.tabs.add(newTab);
            String title = String.valueOf(main.tabs.size());
            tabPanel.insertTab(title, null, newTab.getTabUI(), null, tabPanel.getTabCount()-1);
            // logging.logToOutput("getTabCount-1: " + String.valueOf(tabPanel.getTabCount()-1));
            // logging.logToOutput("tabs.size: " + String.valueOf(main.tabs.size()));
            main.createCloseButton(main, tabPanel, main.tabs.size()-POSTTABS);
        };

        addTab.addActionListener (listener);
        tabPanel.addTab ("addbutton", null);
        tabPanel.setTabComponentAt (tabPanel.getTabCount() - 1, pnlTab);
    }
}


