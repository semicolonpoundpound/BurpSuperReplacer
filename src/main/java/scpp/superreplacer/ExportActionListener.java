package scpp.superreplacer;

import javax.swing.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;

import burp.api.montoya.MontoyaApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import burp.api.montoya.logging.Logging;

public class ExportActionListener implements ActionListener {

    private MontoyaApi api;
    private ArrayList<ReplacerTab> tabs;
    private MainConfig config;
    private MainWindow window;
    Logging logging;
    public ExportActionListener(MontoyaApi api, MainWindow main) {

        this.window = main;
        this.tabs = main.getReplacerTabs();
        this.api = api;
        this.config = new MainConfig(main);
        this.logging = api.logging();

        // logging.logToOutput("export tab count:"+String.valueOf(main.getReplacerTabs().size()));
    }

    public void actionPerformed(ActionEvent e) {
        // logging.logToOutput("eal performed:"+String.valueOf(this.tabs.size()));
        this.saveConfiguration();
    }

    private Boolean saveConfiguration()
    {
        final JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
        { // write file
            Logging logging = this.api.logging();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);

            try {
                String json = new MainConfig(this.window).toJSON();
                // logging.logToOutput("export json:\n"+json);
                try (FileWriter fw = new FileWriter(fc.getSelectedFile())) {
                    fw.write(json);
                } catch (IOException ioe) {
                    ioe.printStackTrace(pw);
                    logging.logToError(sw.toString());
                }
            }
            catch(JsonProcessingException e) {
                e.printStackTrace(pw);
                logging.logToError(sw.toString());
            }
        }

        return true;
    }
}
