package scpp.superreplacer;

import javax.swing.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.IOException;

import burp.api.montoya.MontoyaApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import burp.api.montoya.logging.Logging;

public class ExportActionListener implements ActionListener {

    private MontoyaApi api;
    private ReplacerTab mainTab;
    public ExportActionListener(MontoyaApi api, ReplacerTab mainTab) {

        this.mainTab = mainTab;
        this.api = api;
    }

    public void actionPerformed(ActionEvent e) {
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
            TabConfig cfg = new TabConfig(this.mainTab);
            try {
                String json = cfg.toJSON();
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
