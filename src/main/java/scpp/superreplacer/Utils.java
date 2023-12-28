package scpp.superreplacer;

import burp.api.montoya.http.HttpService;

abstract public class Utils {

    static public HttpService createHttpService(TabConfig cfg)
    {
        String dynRequestHost = cfg.getRequestHost();
        String port = cfg.getRequestPort();
        Integer dynRequestPort = Integer.parseInt(cfg.getRequestPort());
        Boolean dynRequestUseHttps = cfg.getRequestUseHttps();

        HttpService newService = HttpService.httpService(dynRequestHost, dynRequestPort, dynRequestUseHttps);

        return newService;
    }
}
