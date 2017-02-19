package bing;

import java.net.URI;

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
            URIBuilder builder = new URIBuilder("https://api.cognitive.microsoft.com/bing/v5.0/search");

            builder.setParameter("q", q);
            builder.setParameter("count", count);
            builder.setParameter("offset", offset);
            builder.setParameter("mkt", "zh-CN");
            builder.setParameter("safesearch", "Moderate");

            URI uri = builder.build();
            HttpGet searchReq = new HttpGet(uri);
            searchReq.setHeader("Ocp-Apim-Subscription-Key", "295888fae75f437d8157e8b044510d66");

            // Request body
//            StringEntity reqEntity = new StringEntity("");
//            request.setEntity(reqEntity);

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
}
