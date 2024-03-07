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

    private final String TITLE_PANE_TITLE = "Super Replacer";
    private final String SETTINGS_PANE_TITLE = "Settings";
    private final String HELP_PANE_TITLE = "Tips";

    //matchPane Controls
    private JCheckBox chkEnabled= new JCheckBox("Enabled");
    private String verbosityItems[] = {"None", "Info", "Debug"};;
    private JComboBox<String> cboVerbosity = new JComboBox<String>(verbosityItems);
    private JButton btnImportCfg = new JButton("Import Config");
    private JButton btnExportCfg = new JButton("Export Config");

    public MainWindow(MontoyaApi api, ArrayList<ReplacerTab> allTabs) {
        this.api = api;
        this.logging = api.logging();
        this.tabs = allTabs;
        logging.logToOutput("mw init: " + String.valueOf(allTabs.size()));
    }

    public Boolean getEnabled() { return this.chkEnabled.isSelected(); }
    public String getVerbosity() { return this.cboVerbosity.getSelectedItem().toString(); }
    public ArrayList<ReplacerTab> getReplacerTabs() { return this.tabs; }

    public Component getTabUI()
    {
        MainWindow that = this;

        JTabbedPane tabPanel = new JTabbedPane();

        tabPanel.addTab(HELP_TAB_TITLE, this.getHelpTabUI());

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
            String title = String.valueOf(main.tabs.size());
            ReplacerTab newTab = new ReplacerTab(main.api, title);
            main.tabs.add(newTab);
            logging.logToOutput("cab: " + String.valueOf(main.tabs.size()));
            tabPanel.insertTab(title, null, newTab.getTabUI(), null, tabPanel.getTabCount()-1);
            // logging.logToOutput("getTabCount-1: " + String.valueOf(tabPanel.getTabCount()-1));
            // logging.logToOutput("tabs.size: " + String.valueOf(main.tabs.size()));
            main.createCloseButton(main, tabPanel, main.tabs.size()-POSTTABS);
        };

        addTab.addActionListener (listener);
        tabPanel.addTab ("addbutton", null);
        tabPanel.setTabComponentAt (tabPanel.getTabCount() - 1, pnlTab);
    }

    private JPanel getHelpTitlePaneUI()
    {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridBagLayout());
        titlePanel.setBorder(BorderFactory.createTitledBorder(TITLE_PANE_TITLE));

        GridBagConstraints tpc = new GridBagConstraints();


        // add chkSendRequest
        JLabel lblTitleLabel = new JLabel("Something will be here");
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = 1.0;
        tpc.gridwidth = 2;
        tpc.gridx = 0;
        tpc.gridy = 0;
        tpc.insets = new Insets(0,10,20,10);
        titlePanel.add(lblTitleLabel, tpc);

        return titlePanel;
    }

    private JPanel getHelpSettingsPaneUI() {
        GridBagConstraints tpc = new GridBagConstraints();

        JPanel toolPanel = new JPanel();
        toolPanel.setLayout(new GridBagLayout());
        toolPanel.setBorder(BorderFactory.createTitledBorder(SETTINGS_PANE_TITLE));

        // add chkEnabled
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = 1.0;
        tpc.gridwidth = 2;
        tpc.gridx = 0;
        tpc.gridy = 0;
        tpc.insets = new Insets(0,10,20,10);
        toolPanel.add(chkEnabled, tpc);

        // add label for cboIntercept
        JLabel lblInterceptLabel = new JLabel("Verbosity:");
        // tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = 0.0;
        tpc.gridwidth = 1;
        tpc.gridx = 0;
        tpc.gridy += 1;
        tpc.insets = new Insets(0,10,30,10);
        toolPanel.add(lblInterceptLabel, tpc);

        // add cboIntercept to panel
        tpc.weightx = 0.0;
        tpc.gridx += 1;
        tpc.insets = new Insets(0,0,30,10);
        toolPanel.add(cboVerbosity, tpc);

        // add btnImportCfg
        btnImportCfg.addActionListener(new ImportActionListener(this.api, new MainConfig(this.api, this)));
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = 1.0;
        tpc.gridwidth = 2;
        tpc.gridx = 0;
        tpc.gridy += 1;
        tpc.insets = new Insets(0, 10, 10, 10);
        toolPanel.add(btnImportCfg, tpc);

        // add btnExportCfg
        btnExportCfg.addActionListener(new ExportActionListener(this.api, this));
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.gridwidth = 2;
        tpc.gridx = 0;
        tpc.gridy += 1;
        tpc.insets = new Insets(0, 10, 10, 10);
        toolPanel.add(btnExportCfg, tpc);

        return toolPanel;
    }

    private JPanel getHelpTopPaneUI()
    {
        JPanel topPane = new JPanel();
        topPane.setLayout(new GridBagLayout());

        GridBagConstraints tpc = new GridBagConstraints();

        // MATCH PANE
        JPanel titlePanel = this.getHelpTitlePaneUI();
        tpc.anchor = GridBagConstraints.NORTHWEST;
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = .6;
        tpc.weighty = 0;
        tpc.gridx = 0;
        tpc.gridy = 0;
        tpc.ipadx = 20;
        tpc.ipady = 20;
        tpc.insets = new Insets(10,10,10,10);
        topPane.add(titlePanel, tpc);

        // TOOL PANE
        JPanel settingsPanel = this.getHelpSettingsPaneUI();
        tpc.anchor = GridBagConstraints.NORTHWEST;
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = .4;
        tpc.gridx = 1;
        tpc.gridy = 0;
        tpc.ipadx = 20;
        tpc.ipady = 20;
        tpc.insets = new Insets(10,10,10,10);
        topPane.add(settingsPanel,tpc);

        return topPane;
    }

    private JPanel getHelpBottomPaneUI() {

        JPanel bottomPane = new JPanel();
        bottomPane.setLayout(new GridBagLayout());
        bottomPane.setBorder(BorderFactory.createTitledBorder(HELP_PANE_TITLE));

        // tabs with request/response viewers

        GridBagConstraints tpc = new GridBagConstraints();
        JLabel lblHelpTips = new JLabel("Some instructions will go here...");
        tpc.weightx = 0;
        tpc.anchor = GridBagConstraints.WEST;
        tpc.gridx = 0;
        tpc.gridy = 0;
        tpc.insets = new Insets(10,10,10,0);
        bottomPane.add(lblHelpTips, tpc);

        return bottomPane;
    }

    public Component getHelpTabUI()
    {
        // main split pane
        // JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JPanel mainPane = new JPanel();
        mainPane.setLayout(new GridBagLayout());
        GridBagConstraints tpc = new GridBagConstraints();

        // TOP PANE
        JPanel topPane = this.getHelpTopPaneUI();
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.anchor = GridBagConstraints.NORTHEAST;
        tpc.weightx = 1;
        tpc.weighty = .0;
        tpc.gridx = 0;
        tpc.gridy = 0;
        tpc.ipadx = 20;
        tpc.ipady = 20;
        mainPane.add(topPane, tpc);

        // TOP PANE
        JPanel bottomPane = this.getHelpBottomPaneUI();
        tpc.weighty = 1;
        tpc.gridwidth = 2;
        tpc.gridx = 0;
        tpc.gridy = 1;
        tpc.ipadx = 20;
        tpc.ipady = 20;
        tpc.fill = GridBagConstraints.BOTH;
        tpc.insets = new Insets(10,10,10,10);

        mainPane.add(bottomPane, tpc);

        return mainPane;
        // return mainSplitPane;
    }
}


