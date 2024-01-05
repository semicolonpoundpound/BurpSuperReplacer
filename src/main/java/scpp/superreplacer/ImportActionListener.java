package scpp.superreplacer;

import javax.swing.*;
import java.awt.event.*;
import java.nio.file.Files;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;

import burp.api.montoya.MontoyaApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import burp.api.montoya.logging.Logging;

public class ImportActionListener implements ActionListener {

    private MontoyaApi api;
    private ArrayList<ReplacerTab> tabs;
    public ImportActionListener(MontoyaApi api, ArrayList<ReplacerTab> tabs) {

        this.tabs = tabs;
        this.api = api;
    }

    public void actionPerformed(ActionEvent e) {
        this.loadConfiguration();
    }

    private Boolean loadConfiguration()
    {
        final JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
        { // write file
            Logging logging = this.api.logging();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ObjectMapper mapper = new ObjectMapper();
            try {
                String json_str = new String(Files.readAllBytes(fc.getSelectedFile().toPath()));
                TabConfig cfg = mapper.readValue(json_str, TabConfig.class);
//                this.mainTab.loadConfig(cfg);
            }catch (Exception ioe) {
                ioe.printStackTrace(pw);
                logging.logToError(sw.toString());
            }
        }

        return true;
    }
}
