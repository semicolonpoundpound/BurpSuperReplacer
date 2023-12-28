package scpp.superreplacer;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.Annotations;
import burp.api.montoya.http.Http;
import burp.api.montoya.core.HighlightColor;
import burp.api.montoya.http.HttpMode;
import burp.api.montoya.http.HttpService;
import burp.api.montoya.http.handler.*;
import burp.api.montoya.http.message.HttpHeader;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.logging.Logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static burp.api.montoya.http.handler.RequestToBeSentAction.continueWith;
import static burp.api.montoya.http.handler.ResponseReceivedAction.continueWith;
import static burp.api.montoya.http.message.params.HttpParameter.urlParameter;

public class MyHttpHandler implements HttpHandler
{
    private final MontoyaApi api;
    private final Logging logging;
    private final ReplacerTab tab;

    public MyHttpHandler(MontoyaApi api, ReplacerTab mainTab)
    {
        this.api = api;
        this.logging = api.logging();
        this.tab = mainTab;
    }

//    @Override
//    public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent requestToBeSent)
//    {
//        HttpRequest modifiedRequest = HttpRequest.httpRequest(requestToBeSent.httpService(), requestToBeSent.toByteArray());
//
//        return continueWith(modifiedRequest);
//    }
    @Override
    public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent requestToBeSent)
    {

        HttpRequest modifiedRequest = requestToBeSent;
        Annotations annotations = requestToBeSent.annotations();

        try {
            String[] options = {"requests", "requests and responses"};

            Boolean enabled = this.tab.getEnabledValue();
            String intercept = this.tab.getInterceptValue().toLowerCase();

            // logging.logToOutput("tool: " + requestToBeSent.toolSource().toolType().toolName());

            if (enabled && Arrays.asList(options).contains(intercept)) {
                ArrayList<String> enabledTools = this.tab.getEnabledTools();

                String toolSource = requestToBeSent.toolSource().toolType().toolName();

                if (enabledTools.contains(toolSource.toLowerCase())) {
                    String matchRegex = this.tab.getMatchSearchValue();
                    String requestAsStr = requestToBeSent.toString();
                    Boolean matchisRegex = this.tab.getMatchSearchRegexValue();


                    String matchedValue = this.searchMessage(matchRegex, requestAsStr, matchisRegex);

                    if (!matchedValue.isEmpty()) {

                        // logging.logToOutput("Matched: " + matchedValue);

                        annotations = annotations.withNotes("Request was modified with Super Replacer");

                        String replacerValue = null;
                        String replaceTargetString = requestAsStr;

                        Boolean replaceIsRegex = this.tab.getMatchReplaceRegexValue();

                        if (replaceIsRegex) {
                            String replaceRegex = this.tab.getReplaceWithValue();

                            if(this.tab.getSendRequestValue())
                            {
                                String dynRequestAsStr = this.tab.getRequestViewerValue();

                                HttpService dynService = Utils.createHttpService(this.tab.getConfig());

                                // logging.logToOutput("Creating new HTtpService");
                                // logging.logToOutput("host: " + dynService.host());
                                // logging.logToOutput("port: " + dynService.port());
                                // logging.logToOutput("secure: " + dynService.secure());

                                HttpRequest dynRequest = HttpRequest.httpRequest(dynService,
                                        dynRequestAsStr);

                                dynRequest = dynRequest.withBody(dynRequest.bodyToString());

                                HttpRequestResponse dynReqRes = this.api.http().sendRequest(dynRequest, HttpMode.AUTO);

                                String dynResponseAsStr = dynReqRes.response().toString();

                                replaceTargetString = dynResponseAsStr;

                                // logging.logToOutput("dynamic response");
                                // logging.logToOutput(dynResponseAsStr);

                            }

                            replacerValue = this.searchMessage(replaceRegex, replaceTargetString, replaceIsRegex);
                        } else {
                            replacerValue = this.tab.getReplaceWithValue();
                        }

                        // logging.logToOutput("Replacer: " + replacerValue);

                        if (replacerValue != null) {

                            String replaceCount = this.tab.getReplaceCountValue();

                            requestAsStr = this.replaceInMessage(matchRegex, replacerValue, requestAsStr,
                                    matchisRegex, replaceCount);

                            logging.logToOutput("Replaced " + matchedValue + " with " + replacerValue);

                            // logging.logToOutput("Creating new HTtpService");
                            // logging.logToOutput("host: " + requestToBeSent.httpService().host());
                            // logging.logToOutput("port: " + requestToBeSent.httpService().port());
                            // logging.logToOutput("secure: " + requestToBeSent.httpService().secure());
                            // logging.logToOutput(requestAsStr);

                            //Modify the request by adding url param.
                            modifiedRequest = HttpRequest.httpRequest(
                                    requestToBeSent.httpService(),
                                    requestAsStr
                            );

                            modifiedRequest = modifiedRequest.withBody(modifiedRequest.bodyToString());



                        } else {
                            // logging.logToOutput("Replacer does not have a value");
                        }

                    } else {
                        // logging.logToOutput("Matcher failed: " + matchRegex);
                    }
                } else {
                    // logging.logToOutput(toolSource + " is not enabled for this tab.");
                }
            } else {
                // logging.logToOutput("Tab is disabled or not set to intercept requests");
            }
            //Return the modified request to burp with updated annotations.
        }
        catch(Exception e)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logging.logToError(sw.toString());
        }

        return RequestToBeSentAction.continueWith(modifiedRequest, annotations);
    }

    @Override
    public ResponseReceivedAction handleHttpResponseReceived(HttpResponseReceived responseReceived)
    {
        Annotations annotations = responseReceived.annotations();
        String bodyAsStr = responseReceived.bodyToString();

        try {
            String[] options = {"responses", "requests and responses"};

            Boolean enabled = this.tab.getEnabledValue();
            String intercept = this.tab.getInterceptValue().toLowerCase();

            // logging.logToOutput("tool: " + responseReceived.toolSource().toolType().toolName());

            if (enabled && Arrays.asList(options).contains(intercept)) {
                ArrayList<String> enabledTools = this.tab.getEnabledTools();

                String toolSource = responseReceived.toolSource().toolType().toolName();

                if (enabledTools.contains(toolSource.toLowerCase())) {
                    String matchRegex = this.tab.getMatchSearchValue();

                    Boolean matchIsRegex = this.tab.getMatchSearchRegexValue();


                    String matchedValue = this.searchMessage(matchRegex, bodyAsStr, matchIsRegex);

                    if (!matchedValue.isEmpty()) {

                        // logging.logToOutput("Matched: " + matchedValue);

                        String replacerValue = null;
                        String replaceTargetString = bodyAsStr;

                        Boolean replaceIsRegex = this.tab.getMatchReplaceRegexValue();

                        if (replaceIsRegex) {
                            String replaceRegex = this.tab.getReplaceWithValue();

                            if(this.tab.getSendRequestValue())
                            {
                                String dynRequestAsStr = this.tab.getRequestViewerValue();

                                HttpService dynService = Utils.createHttpService(this.tab.getConfig());

                                HttpRequest dynRequest = HttpRequest.httpRequest(dynService,
                                        dynRequestAsStr);

                                HttpRequestResponse dynReqRes = this.api.http().sendRequest(dynRequest, HttpMode.HTTP_2);

                                String dynResponseAsStr = dynReqRes.response().toString();

                                replaceTargetString = dynResponseAsStr;

                            }

                            replacerValue = this.searchMessage(replaceRegex, replaceTargetString, replaceIsRegex);
                        } else {
                            replacerValue = this.tab.getReplaceWithValue();
                        }

                        // logging.logToOutput("Replacer: " + replacerValue);

                        if (replacerValue != null) {

                            String replaceCount = this.tab.getReplaceCountValue();

                            bodyAsStr = this.replaceInMessage(matchRegex, replacerValue, bodyAsStr,
                                    matchIsRegex, replaceCount);

                            logging.logToOutput("Replaced " + matchedValue + " with " + replacerValue);

                            annotations = annotations.withNotes("Response was modified with Super Replacer");

                        } else {
                            // logging.logToOutput("Replacer does not have a value");
                        }

                    } else {
                        // logging.logToOutput("Matcher failed: " + matchRegex);
                    }
                } else {
                    // logging.logToOutput(toolSource + " is not enabled for this tab.");
                }
            } else {
                // logging.logToOutput("Tab is disabled or not set to intercept responses");
            }
            //Return the modified request to burp with updated annotations.
        }
        catch(Exception e)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            logging.logToError(sw.toString());
        }

        return ResponseReceivedAction.continueWith(responseReceived.withBody(bodyAsStr), annotations);
    }

    private String searchMessage(String needle, String haystack, Boolean isRegex)
    {
        String ret = "";

        Pattern matchPattern = Pattern.compile(needle, Pattern.MULTILINE);
        Matcher matcher = matchPattern.matcher(haystack);

        Boolean matched = matcher.find();

        if (matched) {

            ret = matcher.group(0);
        }

        return ret;
    }

    private String replaceInMessage(String needle, String replacement, String haystack, Boolean isRegex,
                                    String replaceCount)
    {
        String ret = haystack;

        if(isRegex) {
            Pattern matchPattern = Pattern.compile(needle, Pattern.MULTILINE);
            Matcher matcher = matchPattern.matcher(haystack);

            if (replaceCount.equalsIgnoreCase("first")) {
                ret = matcher.replaceFirst(replacement);
            } else if (replaceCount.equalsIgnoreCase("all")) {
                ret = matcher.replaceAll(replacement);
            }
        }
        else {
            if (replaceCount.equalsIgnoreCase("first")) {
                ret = haystack.replace(needle, replacement);
            } else if (replaceCount.equalsIgnoreCase("all")) {
                ret = haystack.replaceAll(needle, replacement);
            }
        }

        return ret;
    }
}