package bing;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class BingSearch {

	public static String excuteBingSearch(String q, String count,String offset){
		String result=null;
		HttpClient httpclient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        // Waiting for a connection from connection manager
                        .setConnectionRequestTimeout(10000)
                        // Waiting for connection to establish
                        .setConnectTimeout(5000)
                        .setExpectContinueEnabled(false)
                        // Waiting for data
                        .setSocketTimeout(5000)
                        .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                        .build())
                .setMaxConnPerRoute(20)
                .setMaxConnTotal(100)
                .build();
        try
        {
            URIBuilder builder = new URIBuilder("https://api.cognitive.microsoft.com/bing/v7.0/news/search");

            builder.setParameter("q", q);
//            builder.setParameter("count", count);
//            builder.setParameter("offset", offset);
//            builder.setParameter("mkt", "zh-CN");
//            builder.setParameter("safesearch", "Moderate");

            URI uri = builder.build();
            HttpGet searchReq = new HttpGet(uri);
            searchReq.setHeader("Ocp-Apim-Subscription-Key", "0d872f7e9250466cb7c8bedece1bbe70");

            // Request body
//            StringEntity reqEntity = new StringEntity("");
//            request.setEntity(reqEntity);
            System.out.println(uri.toString());
            HttpResponse searchRes = httpclient.execute(searchReq);
            HttpEntity entity = searchRes.getEntity();

            if (entity != null) 
            {
                result = EntityUtils.toString(entity);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
		
		return result;
	}
	
    public static String SearchWeb (String searchQuery) throws Exception {
        // construct URL of search request (endpoint + query string)
    	String host = "https://api.cognitive.microsoft.com";
    	String path = "/bing/v7.0/video";
        URL url = new URL(host + path + "?q=" +  URLEncoder.encode(searchQuery, "UTF-8"));
        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", "0d872f7e9250466cb7c8bedece1bbe70");

        // receive JSON body
        InputStream stream = connection.getInputStream();
        String response = new Scanner(stream).useDelimiter("\\A").next();

        // construct result object for return

        // extract Bing-related HTTP headers
//        Map<String, List<String>> headers = connection.getHeaderFields();
//        for (String header : headers.keySet()) {
//            if (header == null) continue;      // may have null key
//            if (header.startsWith("BingAPIs-") || header.startsWith("X-MSEdge-")) {
//                results.relevantHeaders.put(header, headers.get(header).get(0));
//            }
//        }

        stream.close();
        return response;
    }
	
    public static void main (String[] args) {

        try {
            System.out.println("Searching the Web for: " + "玫瑰");
            String result = excuteBingSearch("shoes", "10", "0");
//            String result = SearchWeb("玫瑰");
            System.out.println(result);
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            System.exit(1);
        }
    }
	
}
