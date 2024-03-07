package scpp.superreplacer;

import burp.api.montoya.http.message.requests.HttpRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class TabConfig {
    private Boolean enabled;
    private String intercept;
    private Boolean matchSearchRegex;
    private String matchSearch;
    private String replaceCount;
    private Boolean matchReplaceRegex;
    private String replaceWith;
    private Boolean sendRequest;

    private String requestHost;
    private String requestPort;
    private Boolean requestUseHttps;
    private String dynamicRequest;

    //Tool Pane Controls
    private Boolean toolProxy;
    private Boolean toolRepeater;
    private Boolean toolExtensions;
    private Boolean toolIntruder;
    private Boolean toolScanner;
    private Boolean toolSequencer;

    public TabConfig() {}

    public TabConfig(ReplacerTab tab)
    {
        // this.enabled = tab.getEnabledValue();
        this.intercept = tab.getInterceptValue();
        this.matchSearchRegex = tab.getMatchSearchRegexValue();
        this.matchSearch = tab.getMatchSearchValue();
        this.replaceCount = tab.getReplaceCountValue();
        this.matchReplaceRegex = tab.getMatchReplaceRegexValue();
        this.replaceWith = tab.getReplaceWithValue();
        this.sendRequest = tab.getSendRequestValue();
        this.requestHost = tab.getRequestHostValue();
        this.requestPort = tab.getRequestPortValue();
        this.requestUseHttps = tab.getRequestHttpsValue();
        this.dynamicRequest = tab.getRequestViewerValue();
        this.toolProxy = tab.getToolProxyValue();
        this.toolRepeater = tab.getToolRepeaterValue();
        this.toolExtensions = tab.getToolExtensionsValue();
        this.toolIntruder = tab.getToolIntruderValue();
        this.toolScanner = tab.getToolScannerValue();
        this.toolSequencer = tab.getToolSequencerValue();
    }

    public String toJSON() throws JsonProcessingException
    {

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(this);

        return json;
    }

    // public Boolean getEnabled() { return this.enabled; }
    public String getIntercept() { return this.intercept; }
    public Boolean getMatchSearchRegex() { return this.matchSearchRegex; }
    public String getMatchSearch() { return this.matchSearch; }
    public String getReplaceCount() { return this. replaceCount; }
    public Boolean getMatchReplaceRegex() { return this.matchReplaceRegex; }
    public String getReplaceWith() { return this.replaceWith; }
    public Boolean getSendRequest() { return this.sendRequest; }
    public String getRequestHost() { return this.requestHost; }
    public String getRequestPort() { return this.requestPort; }
    public Boolean getRequestUseHttps() { return this.requestUseHttps; }
    public String getDynamicRequest() { return this.dynamicRequest; }

    //Tool Pane Controls
    public Boolean getToolProxy() { return this.toolProxy; };
    public Boolean getToolRepeater() { return this.toolRepeater; };
    public Boolean getToolExtensions() { return this.toolExtensions; };
    public Boolean getToolIntruder() { return this.toolIntruder; };
    public Boolean getToolScanner() { return this.toolScanner; };
    public Boolean getToolSequencer() { return this.toolSequencer; };

    // public void setEnabled(Boolean val) { this.enabled = val; }
    public void setIntercept(String val) { this.intercept = val; }
    public void setMatchSearchRegex(Boolean val) { this.matchSearchRegex = val; }
    public void setMatchSearch(String val) { this.matchSearch = val; }
    public void setReplaceCount(String val) { this. replaceCount = val; }
    public void setMatchReplaceRegex(Boolean val) { this.matchReplaceRegex = val; }
    public void setReplaceWith(String val) { this.replaceWith = val; }
    public void setSendRequest(Boolean val) { this.sendRequest = val; }
    public void setRequestHost(String val) { this.requestHost = val; }
    public void setRequestPort(String val) { this.requestPort = val; }
    public void setRequestUseHttps(Boolean val) { this.requestUseHttps = val; }
    public void setDynamicRequest(String val) { this.dynamicRequest = val; }

    //Tool Pane Controls
    public void setToolProxy(Boolean val) { this.toolProxy = val; };
    public void setToolRepeater(Boolean val) { this.toolRepeater = val; };
    public void setToolExtensions(Boolean val) { this.toolExtensions = val; };
    public void setToolIntruder(Boolean val) { this.toolIntruder = val; };
    public void setToolScanner(Boolean val) { this.toolScanner = val; };
    public void setToolSequencer(Boolean val) { this.toolSequencer = val; };


}
