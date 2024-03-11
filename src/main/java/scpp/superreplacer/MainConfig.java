package scpp.superreplacer;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.ArrayList;

public class MainConfig {

    private String verbosity;
    private Boolean enabled;
    private ArrayList<TabConfig> tabConfigs = new ArrayList<TabConfig>();

    // private final MontoyaApi api;
    // private final Logging logging;

    public MainConfig() {}
    public MainConfig(MainWindow main) {
        this.verbosity = main.getVerbosity();
        this.enabled = main.getEnabled();
        // this.api = api;
        // this.logging = api.logging();

        // logging.logToOutput("mc: " + String.valueOf(main.getReplacerTabs().size()));
        for(ReplacerTab curTab : main.getReplacerTabs()) {
            this.tabConfigs.add(new TabConfig(curTab));
        }

    }

    public String getVerbosity() { return this.verbosity; }
    public Boolean getEnabled() { return this.enabled; }
    public ArrayList<TabConfig> getTabConfigs() { return this.tabConfigs; }

    public void setVerbosity(String v) { this.verbosity = v; }
    public void setEnabled(Boolean e) { this.enabled = e; }
    public void setTabConfigs(ArrayList<TabConfig> cfgs) { this.tabConfigs = cfgs; }

    public String toJSON() throws JsonProcessingException
    {

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(this);

        return json;
    }
}
