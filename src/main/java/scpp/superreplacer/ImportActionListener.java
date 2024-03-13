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

    private MainWindow main;

    public ImportActionListener(MontoyaApi api, MainWindow main) {

        // this.tabs = helpTab.getReplacerTabs();
        this.api = api;
        this.main = main;
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
                MainConfig cfg = mapper.readValue(json_str, MainConfig.class);

                // this.main = MainWindow.fromConfig(this.api, cfg);
                this.main.updateFromConfig(cfg);
            } catch (Exception ioe) {
                ioe.printStackTrace(pw);
                logging.logToError(sw.toString());
            }
        }

        return true;
    }
}
