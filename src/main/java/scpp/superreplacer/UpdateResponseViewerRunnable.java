package scpp.superreplacer;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.HttpMode;
import burp.api.montoya.http.HttpService;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;

public class UpdateResponseViewerRunnable implements Runnable {

    private MontoyaApi api;
    private ReplacerTab tab;

    public UpdateResponseViewerRunnable(MontoyaApi api, ReplacerTab tab) {
        this.api = api;
        this.tab = tab;
    }
    public void run() {
        String dynRequestAsStr = this.tab.getRequestViewerValue();

        HttpService dynService = Utils.createHttpService(this.tab.getConfig());

        HttpRequest dynRequest = HttpRequest.httpRequest(dynService,
                dynRequestAsStr);

        dynRequest = dynRequest.withBody(dynRequest.bodyToString());

        HttpRequestResponse dynReqRes = this.api.http().sendRequest(dynRequest, HttpMode.AUTO);

        String dynResponseAsStr = dynReqRes.response().toString();

        this.tab.setResponseViewerValue(dynResponseAsStr);
    }
}
