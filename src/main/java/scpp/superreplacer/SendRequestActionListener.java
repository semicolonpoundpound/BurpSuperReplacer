package scpp.superreplacer;

import javax.swing.*;
import java.awt.event.*;
import java.nio.file.Files;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.IOException;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.HttpMode;
import burp.api.montoya.http.HttpService;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.logging.Logging;

public class SendRequestActionListener implements ActionListener {

    private MontoyaApi api;
    private ReplacerTab tab;
    public SendRequestActionListener(MontoyaApi api, ReplacerTab tab) {

        this.tab = tab;
        this.api = api;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            UpdateResponseViewerRunnable runme = new UpdateResponseViewerRunnable(this.api, this.tab);
            Thread t = new Thread(runme);
            t.start();
        }catch (Exception ioe) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ioe.printStackTrace(pw);
            this.api.logging().logToError(sw.toString());
        }

    }

}
