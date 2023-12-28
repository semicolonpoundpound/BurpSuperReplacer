package scpp.superreplacer;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.ByteArray;
import burp.api.montoya.http.handler.HttpResponseReceived;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.http.message.responses.HttpResponse;
import burp.api.montoya.ui.UserInterface;
import burp.api.montoya.ui.editor.HttpRequestEditor;
import burp.api.montoya.ui.editor.HttpResponseEditor;
import burp.api.montoya.ui.editor.RawEditor;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import static burp.api.montoya.ui.editor.EditorOptions.READ_ONLY;

public class ReplacerTab {

    private MontoyaApi api;

    private final String EXTENSION_NAME = "Super Replacer";
    private final String MATCH_PANE_TITLE = "Parameters";
    private final String MATCH_PANE_LABEL = "Search & Replace";
    private final String TOOL_PANE_TITLE = "Settings";
    private final String TOOL_PANE_LABEL = "Tools";

    private final String REQUEST_HOST_LABEL = "Host";
    private final String REQUEST_PORT_LABEL = "Port";
    private final String REQ_PANE_TITLE = "Dynamic Request";
    private final String REQ_PANE_LABEL = "Dynamic Value";

    //matchPane Controls
    private JCheckBox chkEnabled= new JCheckBox("Enabled");
    private String cboInterceptItems[] = {"Requests", "Responses", "Requests and Responses"};;
    private JComboBox<String> cboIntercept = new JComboBox<String>(cboInterceptItems);
    private JCheckBox chkMatchSearchRegex= new JCheckBox("Use Regex");
    private JTextField txtMatchSearch= new JTextField();
    private String cboReplaceCountItems[] = {"First", "All"};
    private JComboBox<String> cboReplaceCount = new JComboBox<String>(cboReplaceCountItems);
    private JCheckBox chkMatchReplaceRegex = new JCheckBox("Use Regex");
    private JTextField txtReplaceWith = new JTextField();

    private JButton btnFetchResponse = new JButton("Fetch Response");
    private JTextField txtRequestHost = new JTextField();
    private JTextField txtRequestPort = new JTextField();
    private JCheckBox chkRequestHttps = new JCheckBox("Use HTTPS");
    private HttpRequestEditor requestViewer;
    private HttpResponseEditor responseViewer;
    private JCheckBox chkSendRequest = new JCheckBox("Send the request below and search the response");

    //Tool Pane Controls
    private JCheckBox chkToolProxy = new JCheckBox("Proxy");
    private JCheckBox chkToolRepeater = new JCheckBox("Repeater");
    private JCheckBox chkToolExtensions = new JCheckBox("Extensions");
    private JCheckBox chkToolIntruder = new JCheckBox("Intruder");
    private JCheckBox chkToolScanner = new JCheckBox("Scanner");
    private JCheckBox chkToolSequencer = new JCheckBox("Sequencer");
    private JButton btnImportCfg = new JButton("Import Config");
    private JButton btnExportCfg = new JButton("Export Config");


    public ReplacerTab(MontoyaApi api)
    {
        this.api = api;

        UserInterface userInterface = api.userInterface();

        this.requestViewer = userInterface.createHttpRequestEditor();
        this.responseViewer = userInterface.createHttpResponseEditor(READ_ONLY);
    }

    public TabConfig getConfig()
    {
        return new TabConfig(this);
    }

    public void loadConfig(TabConfig cfg)
    {
        this.setEnabledValue(cfg.getEnabled());
        this.setInterceptValue(cfg.getIntercept());
        this.setMatchSearchRegexValue(cfg.getMatchSearchRegex());
        this.setMatchSearchValue(cfg.getMatchSearch());
        this.setReplaceCountValue(cfg.getReplaceCount());
        this.setMatchReplaceRegexValue(cfg.getMatchReplaceRegex());
        this.setReplaceWithValue(cfg.getReplaceWith());
        this.setSendRequestValue(cfg.getSendRequest());
        this.setRequestHostValue(cfg.getRequestHost());
        this.setRequestPortValue(cfg.getRequestPort());
        this.setRequestHttpsValue(cfg.getRequestUseHttps());
        this.setRequestViewerValue(cfg.getDynamicRequest());
        this.setToolProxyValue(cfg.getToolProxy());
        this.setToolRepeaterValue(cfg.getToolRepeater());
        this.setToolExtensionsValue(cfg.getToolExtensions());
        this.setToolIntruderValue(cfg.getToolIntruder());
        this.setToolScannerValue(cfg.getToolScanner());
        this.setToolSequencerValue(cfg.getToolSequencer());
    }

    public ArrayList<String> getEnabledTools() {
        ArrayList <String> enabledTools = new ArrayList<String>();

        if(this.getToolProxyValue()) {
            enabledTools.add("proxy");
        }
        if(this.getToolRepeaterValue()) {
            enabledTools.add("repeater");
        }
        if(this.getToolExtensionsValue()) {
            enabledTools.add("extensions");
        }
        if(this.getToolIntruderValue()) {
            enabledTools.add("intruder");
        }
        if(this.getToolScannerValue()) {
            enabledTools.add("scanner");
        }
        if(this.getToolSequencerValue()) {
            enabledTools.add("sequencer");
        }

        return enabledTools;
    }

    public Boolean getEnabledValue(){
        return chkEnabled.isSelected();
    }
    public String getInterceptValue(){
        return cboIntercept.getSelectedItem().toString();
    }
    public Boolean getMatchSearchRegexValue(){
        return chkMatchSearchRegex.isSelected();
    }

    public String getMatchSearchValue(){
        return txtMatchSearch.getText();
    }

    public String getReplaceCountValue(){
        return cboReplaceCount.getSelectedItem().toString();
    }

    public Boolean getMatchReplaceRegexValue(){
        return chkMatchReplaceRegex.isSelected();
    }

    public String getReplaceWithValue(){
        return txtReplaceWith.getText();
    }

    public Boolean getSendRequestValue(){
        return chkSendRequest.isSelected();
    }

    public String getRequestHostValue() { return this.txtRequestHost.getText(); }

    public String getRequestPortValue() { return this.txtRequestPort.getText(); }

    public Boolean getRequestHttpsValue() { return this.chkRequestHttps.isSelected(); }

    public String getRequestViewerValue() { return this.requestViewer.getRequest().toString(); }

    public String getResponseViewerValue() { return this.responseViewer.getResponse().toString(); }

    public Boolean getToolProxyValue(){
        return chkToolProxy.isSelected();
    }

    public Boolean getToolRepeaterValue(){
        return chkToolRepeater.isSelected();
    }

    public Boolean getToolExtensionsValue(){
        return chkToolExtensions.isSelected();
    }

    public Boolean getToolIntruderValue(){
        return chkToolIntruder.isSelected();
    }

    public Boolean getToolScannerValue(){
        return chkToolScanner.isSelected();
    }

    public Boolean getToolSequencerValue(){
        return chkToolSequencer.isSelected();
    }

    public void setEnabledValue(Boolean val){
        chkEnabled.setSelected(val);
    }
    public void setInterceptValue(String val){
        cboIntercept.setSelectedItem(val);
    }

    public void setMatchSearchRegexValue(Boolean val){
        chkMatchSearchRegex.setSelected(val);
    }

    public void setMatchSearchValue(String val){ txtMatchSearch.setText(val); }

    public void setReplaceCountValue(String val){
        cboReplaceCount.setSelectedItem(val);
    }

    public void setMatchReplaceRegexValue(Boolean val){
        chkMatchReplaceRegex.setSelected(val);
    }

    public void setReplaceWithValue(String val){
        txtReplaceWith.setText(val);
    }

    public void setSendRequestValue(Boolean val){
        chkSendRequest.setSelected(val);
    }

    public void setRequestHostValue(String val) { this.txtRequestHost.setText(val); }

    public void setRequestPortValue(String val) { this.txtRequestPort.setText(val); }

    public void setRequestHttpsValue(Boolean val) { this.chkRequestHttps.setSelected(val); }

    public void setRequestViewerValue(String val) { this.requestViewer.setRequest(HttpRequest.httpRequest(val)); }

    public void setResponseViewerValue(String val) { this.responseViewer.setResponse(HttpResponse.httpResponse(val)); }

    public void setToolProxyValue(Boolean val){
        chkToolProxy.setSelected(val);
    }

    public void setToolRepeaterValue(Boolean val){
        chkToolRepeater.setSelected(val);
    }

    public void setToolExtensionsValue(Boolean val){
        chkToolExtensions.setSelected(val);
    }

    public void setToolIntruderValue(Boolean val){
        chkToolIntruder.setSelected(val);
    }

    public void setToolScannerValue(Boolean val){
        chkToolScanner.setSelected(val);
    }

    public void setToolSequencerValue(Boolean val){
        chkToolSequencer.setSelected(val);
    }


    private JPanel getMatchPaneUI()
    {
        JPanel matchPanel = new JPanel();
        matchPanel.setLayout(new GridBagLayout());
        matchPanel.setBorder(BorderFactory.createTitledBorder(MATCH_PANE_TITLE));

        GridBagConstraints tpc = new GridBagConstraints();

        // add chkEnabled
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = 1.0;
        tpc.gridwidth = 2;
        tpc.gridx = 0;
        tpc.gridy = 0;
        tpc.insets = new Insets(0,10,20,10);
        matchPanel.add(chkEnabled, tpc);

        // add label for cboIntercept
        JLabel lblInterceptLabel = new JLabel("Intercept:");
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = 0.0;
        tpc.gridx = 0;
        tpc.gridy += 1;
        tpc.insets = new Insets(0,10,30,10);
        matchPanel.add(lblInterceptLabel, tpc);

        // add cboIntercept to panel
        tpc.weightx = 0.9;
        tpc.gridx = 1;
        tpc.insets = new Insets(0,0,30,10);
        matchPanel.add(cboIntercept, tpc);

        // add label to lblMatchLabel
        JLabel lblMatchLabel = new JLabel("Search for:");
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = 0.0;
        tpc.gridwidth = 1;
        tpc.gridx = 0;
        tpc.gridy += 1;
        tpc.insets = new Insets(0,10,10,10);
        matchPanel.add(lblMatchLabel, tpc);

        // add chkMatchSearchRegex
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = 0.9;
        tpc.gridwidth = 1;
        tpc.gridx = 1;
        tpc.insets = new Insets(0,0,10,0);
        matchPanel.add(chkMatchSearchRegex, tpc);


        // add txtMatchSearch
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = 1.0;
        tpc.gridwidth = 2;
        tpc.gridx = 0;
        tpc.gridy += 1;
        tpc.insets = new Insets(0,10,30,10);
        matchPanel.add(txtMatchSearch, tpc);


        JLabel lblReplaceCountLabel = new JLabel("Replace:");
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = 0.0;
        tpc.gridx = 0;
        tpc.gridy += 1;
        tpc.insets = new Insets(0,10,30,10);
        matchPanel.add(lblReplaceCountLabel, tpc);

        // add cboReplaceCount
        tpc.weightx = 0.9;
        tpc.gridx = 1;
        tpc.insets = new Insets(0,0,30,10);
        matchPanel.add(cboReplaceCount, tpc);

        // add label to txtReplaceWith
        JLabel lblReplaceLabel = new JLabel("Replace with:");
        tpc.weightx = 0.0;
        tpc.gridwidth = 1;
        tpc.gridx = 0;
        tpc.gridy += 1;
        tpc.insets = new Insets(0,10,10,10);
        matchPanel.add(lblReplaceLabel, tpc);

        // add chkMatchReplaceRegex
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = 0.9;
        tpc.gridwidth = 1;
        tpc.gridx = 1;
        tpc.insets = new Insets(0,0,10,0);
        matchPanel.add(chkMatchReplaceRegex, tpc);


        // add txtReplaceWith
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = 1.0;
        tpc.gridwidth = 2;
        tpc.gridx = 0;
        tpc.gridy += 1;
        tpc.insets = new Insets(0,10,30,10);
        matchPanel.add(txtReplaceWith, tpc);

        // add chkSendRequest
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = 1.0;
        tpc.gridwidth = 2;
        tpc.gridx = 0;
        tpc.gridy += 1;
        tpc.insets = new Insets(0,10,20,10);
        matchPanel.add(chkSendRequest, tpc);

        return matchPanel;
    }

    private JPanel getToolPaneUI() {
        JPanel matchPanel = new JPanel();
        matchPanel.setLayout(new GridBagLayout());
        matchPanel.setBorder(BorderFactory.createTitledBorder(MATCH_PANE_TITLE));

        GridBagConstraints tpc = new GridBagConstraints();

        JPanel toolPanel = new JPanel();
        toolPanel.setLayout(new GridBagLayout());
        toolPanel.setBorder(BorderFactory.createTitledBorder(TOOL_PANE_TITLE));

        JLabel lblToolLabel = new JLabel(TOOL_PANE_LABEL);
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = 0.0;
        tpc.gridwidth = 3;
        tpc.gridx = 0;
        tpc.gridy = 0;
        tpc.insets = new Insets(0, 0, 10, 0);
        toolPanel.add(lblToolLabel, tpc);

        // add chkToolProxy
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = 0.0;
        tpc.gridwidth = 1;
        tpc.gridx = 0;
        tpc.gridy += 1;
        tpc.insets = new Insets(0, 0, 10, 10);
        toolPanel.add(chkToolProxy, tpc);

        // add chkToolRepeater
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = 0.0;
        tpc.gridwidth = 1;
        tpc.gridx = 1;
        tpc.insets = new Insets(0, 0, 10, 10);
        toolPanel.add(chkToolRepeater, tpc);

        // add chkToolExtensions
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = 0.0;
        tpc.gridwidth = 1;
        tpc.gridx = 2;
        tpc.insets = new Insets(0, 0, 10, 0);
        toolPanel.add(chkToolExtensions, tpc);

        // add chkToolIntruder
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = 0.0;
        tpc.gridwidth = 1;
        tpc.gridx = 0;
        tpc.gridy += 1;
        tpc.insets = new Insets(0, 0, 10, 10);
        toolPanel.add(chkToolIntruder, tpc);

        // add chkToolScanner
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = 0.0;
        tpc.gridwidth = 1;
        tpc.gridx = 1;
        tpc.insets = new Insets(0, 0, 10, 10);
        toolPanel.add(chkToolScanner, tpc);

        // add chkToolSequencer
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = 0.0;
        tpc.gridwidth = 1;
        tpc.gridx = 2;
        tpc.insets = new Insets(0, 0, 10, 0);
        toolPanel.add(chkToolSequencer, tpc);

        // add btnImportCfg
        btnImportCfg.addActionListener(new ImportActionListener(this.api, this));
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.gridwidth = 3;
        tpc.gridx = 0;
        tpc.gridy += 1;
        tpc.insets = new Insets(0, 0, 10, 0);
        toolPanel.add(btnImportCfg, tpc);

        // add btnExportCfg
        btnExportCfg.addActionListener(new ExportActionListener(this.api, this));
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.gridwidth = 3;
        tpc.gridx = 0;
        tpc.gridy += 1;
        tpc.insets = new Insets(0, 0, 10, 0);
        toolPanel.add(btnExportCfg, tpc);

        return toolPanel;
    }

    private JPanel getTopPaneUI()
    {
        JPanel topPane = new JPanel();
        topPane.setLayout(new GridBagLayout());

        GridBagConstraints tpc = new GridBagConstraints();

        // MATCH PANE
        JPanel matchPanel = this.getMatchPaneUI();
        tpc.anchor = GridBagConstraints.NORTHWEST;
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.weightx = .6;
        tpc.weighty = 0;
        tpc.gridx = 0;
        tpc.gridy = 0;
        tpc.ipadx = 20;
        tpc.ipady = 20;
        tpc.insets = new Insets(10,10,10,10);
        topPane.add(matchPanel, tpc);

        // TOOL PANE
        JPanel toolPanel = this.getToolPaneUI();
        tpc.anchor = GridBagConstraints.NORTHWEST;
        tpc.fill = GridBagConstraints.NONE;
        tpc.weightx = .4;
        tpc.gridx = 1;
        tpc.gridy = 0;
        tpc.ipadx = 20;
        tpc.ipady = 20;
        tpc.insets = new Insets(10,10,10,10);
        topPane.add(toolPanel,tpc);

        return topPane;
    }

    private JPanel getBottomPaneUI() {

        JPanel bottomPane = new JPanel();
        bottomPane.setLayout(new GridBagLayout());
        bottomPane.setBorder(BorderFactory.createTitledBorder(REQ_PANE_TITLE));

        // tabs with request/response viewers

        GridBagConstraints tpc = new GridBagConstraints();
        JLabel lblRequestHost = new JLabel(REQUEST_HOST_LABEL);
        tpc.weightx = 0;
        tpc.anchor = GridBagConstraints.WEST;
        tpc.gridx = 0;
        tpc.gridy = 0;
        tpc.insets = new Insets(10,10,10,0);
        bottomPane.add(lblRequestHost, tpc);

        tpc.weightx = .3;
        tpc.anchor = GridBagConstraints.WEST;
        tpc.gridx = 1;
        tpc.gridy = 0;
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.insets = new Insets(10,10,10,0);
        bottomPane.add(txtRequestHost, tpc);

        JLabel lblRequestPort = new JLabel(REQUEST_PORT_LABEL);
        tpc.weightx = 0;
        tpc.anchor = GridBagConstraints.WEST;
        tpc.gridx = 2;
        tpc.gridy = 0;
        tpc.fill = GridBagConstraints.NONE;
        tpc.insets = new Insets(10,10,10,0);
        bottomPane.add(lblRequestPort, tpc);

        ((AbstractDocument)txtRequestPort.getDocument()).setDocumentFilter(new DocumentFilter(){
            private Pattern regEx = Pattern.compile("\\d*");
            private int limit = 5;

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                Matcher matcher = regEx.matcher(text);
                Boolean valid = true;
                String val = fb.getDocument().getText(0,fb.getDocument().getLength()) + text;
                int port = -1;
                try {
                    port = Integer.parseInt(val);
                } catch (Exception e) {
                }

                if (port < 0 || port > 65536) {
                    valid = false;
                }

                if (false) {
                    super.insertString(fb, offset, text, attrs);
                }

                if(!matcher.matches() || !valid || (fb.getDocument().getLength() + text.length()) > limit){
                    return;
                }
                super.replace(fb, offset, length, text, attrs);
            }
        });

        tpc.weightx = .1;
        tpc.anchor = GridBagConstraints.WEST;
        tpc.gridx = 3;
        tpc.gridy = 0;
        tpc.fill = GridBagConstraints.HORIZONTAL;
        tpc.insets = new Insets(10,10,10,0);
        bottomPane.add(txtRequestPort, tpc);

        tpc.weightx = 0.6;
        tpc.anchor = GridBagConstraints.WEST;
        tpc.gridx = 4;
        tpc.gridy = 0;
        tpc.fill = GridBagConstraints.NONE;
        tpc.insets = new Insets(10,10,10,0);
        bottomPane.add(chkRequestHttps, tpc);

        btnFetchResponse.addActionListener(new SendRequestActionListener(this.api, this));
        tpc.weightx = 0;
        tpc.anchor = GridBagConstraints.WEST;
        tpc.gridwidth = 5;
        tpc.gridx = 0;
        tpc.gridy += 1;
        tpc.fill = GridBagConstraints.NONE;
        tpc.insets = new Insets(10,10,10,0);
        bottomPane.add(btnFetchResponse, tpc);

        JSplitPane reqResPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        // reqResPane.setBorder(BorderFactory.createTitledBorder(REQ_PANE_TITLE));
        JTabbedPane reqTab = new JTabbedPane();
        JTabbedPane resTab = new JTabbedPane();

        reqTab.addTab("Request", requestViewer.uiComponent());
        resTab.addTab("Response", responseViewer.uiComponent());

        reqResPane.setLeftComponent(reqTab);
        reqResPane.setRightComponent(resTab);

        tpc.weightx = 1;
        tpc.weighty = .9;
        tpc.gridwidth = 5;
        tpc.gridx = 0;
        tpc.gridy += 1;
        tpc.ipadx = 20;
        tpc.ipady = 20;
        tpc.fill = GridBagConstraints.BOTH;
        tpc.insets = new Insets(0,10,10,10);
        bottomPane.add(reqResPane, tpc);

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
