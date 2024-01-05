package scpp.superreplacer;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.ui.UserInterface;
import burp.api.montoya.ui.editor.HttpRequestEditor;
import burp.api.montoya.ui.editor.HttpResponseEditor;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static burp.api.montoya.ui.editor.EditorOptions.READ_ONLY;

public class HelpTab {
    private MontoyaApi api;
    private ArrayList<ReplacerTab> tabs;

    private final String TITLE_PANE_TITLE = "Super Replacer";
    private final String SETTINGS_PANE_TITLE = "Settings";
    private final String HELP_PANE_TITLE = "Tips";

    //matchPane Controls
    private JCheckBox chkEnabled= new JCheckBox("Enabled");
    private String verbosityItems[] = {"None", "Info", "Debug"};;
    private JComboBox<String> cboVerbosity = new JComboBox<String>(verbosityItems);
    private JButton btnImportCfg = new JButton("Import Config");
    private JButton btnExportCfg = new JButton("Export Config");

    public HelpTab(MontoyaApi api, ArrayList<ReplacerTab> tabs)
    {
        this.api = api;

        this.tabs = tabs;
    }

    private JPanel getTitlePaneUI()
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

    private JPanel getSettingsPaneUI() {
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
        btnImportCfg.addActionListener(new ImportActionListener(this.api, this.tabs));
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = 1.0;
        tpc.gridwidth = 2;
        tpc.gridx = 0;
        tpc.gridy += 1;
        tpc.insets = new Insets(0, 10, 10, 10);
        toolPanel.add(btnImportCfg, tpc);

        // add btnExportCfg
        btnExportCfg.addActionListener(new ExportActionListener(this.api, this.tabs));
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.gridwidth = 2;
        tpc.gridx = 0;
        tpc.gridy += 1;
        tpc.insets = new Insets(0, 10, 10, 10);
        toolPanel.add(btnExportCfg, tpc);

        return toolPanel;
    }

    private JPanel getTopPaneUI()
    {
        JPanel topPane = new JPanel();
        topPane.setLayout(new GridBagLayout());

        GridBagConstraints tpc = new GridBagConstraints();

        // MATCH PANE
        JPanel titlePanel = this.getTitlePaneUI();
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
        JPanel settingsPanel = this.getSettingsPaneUI();
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

    private JPanel getBottomPaneUI() {

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

    public Component getTabUI()
    {
        // main split pane
        // JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JPanel mainPane = new JPanel();
        mainPane.setLayout(new GridBagLayout());
        GridBagConstraints tpc = new GridBagConstraints();

        // TOP PANE
        JPanel topPane = this.getTopPaneUI();
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
        JPanel bottomPane = this.getBottomPaneUI();
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
