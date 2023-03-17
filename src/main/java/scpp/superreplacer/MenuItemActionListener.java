package scpp.superreplacer;

import java.awt.event.*;
import java.util.List;
import java.util.Optional;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.HttpService;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.http.message.responses.HttpResponse;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
import burp.api.montoya.ui.contextmenu.MessageEditorHttpRequestResponse;

public class MenuItemActionListener implements ActionListener {

    private MontoyaApi api;
    private ReplacerTab tab;
    ContextMenuEvent event;

    public MenuItemActionListener(MontoyaApi api, ReplacerTab tab, ContextMenuEvent event) {

        this.tab = tab;
        this.api = api;
        this.event = event;
    }

    public void actionPerformed(ActionEvent e) {

        HttpRequest request;
        HttpResponse response;
        HttpService service;

        if(this.event.messageEditorRequestResponse().isPresent()) {
            // use message editor
            MessageEditorHttpRequestResponse editorReqRes = this.event.messageEditorRequestResponse().get();
            request = editorReqRes.requestResponse().request();
            response = editorReqRes.requestResponse().response();
        }
        else {
            List<HttpRequestResponse> list = this.event.selectedRequestResponses();
            request = list.get(0).request();
            response = list.get(0).response();
        }

        service = request.httpService();

        this.tab.setRequestViewerValue(request.toString());
        this.tab.setRequestHostValue(service.host());
        this.tab.setRequestPortValue(Integer.toString(service.port()));
        this.tab.setRequestHttpsValue(service.secure());
        this.tab.setResponseViewerValue(response.toString());
    }

}