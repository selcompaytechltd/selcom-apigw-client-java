package io.github.selcompaytechltd;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.json.simple.JSONObject;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.simple.parser.JSONParser;

public class ApigwClient 
{
    String baseUrl;
    String apiKey;
    String apiSecret;

    public  ApigwClient(String baseUrl, String apiKey, String apiSecret){
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;

    }
    public  Map<String,Object> computeHeader( JSONObject dataMap) {
        Map<String,Object> header = new HashMap<>();
        String encodekey = Base64.getEncoder().encodeToString((apiKey).getBytes());
        String authToken = "SELCOM "+ encodekey;
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date now = new Date();
        String timestamp = sdfDate.format(now);
        
        String message = "", signed_fields = "";
    
        List<String> keys = new ArrayList<String>();
        List<String> serializedJson = new ArrayList<String>();
        serializedJson.add("timestamp="+timestamp);
    
        for (Object key : dataMap.keySet()) {
            String keyStr = (String)key;
            String keyvalue = dataMap.get(keyStr).toString();
            serializedJson.add(keyStr+"="+keyvalue);
            keys.add(keyStr);
    
        }
    
        message =  String.join("&", serializedJson);
        signed_fields =  String.join(",", keys);
    
        try {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(apiSecret.getBytes(), "HmacSHA256");
        
            sha256_HMAC.init(secret_key);
       
    
        String digest = new String(Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(message.getBytes())));
        
    
        header.put("Content-type", "application/json");
        header.put("Authorization", authToken);
        header.put("Digest-Method", "HS256");
        header.put("Digest", digest);
        header.put("Timestamp", timestamp);
        header.put("Signed-Fields", signed_fields);
    
        return header;
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            return header;
            
        }
    
    }
    
    public  JSONObject postFunc(String path, JSONObject jsonData){
        Map <String,Object> header = computeHeader(jsonData);
        String url = this.baseUrl + path;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            HttpPost request = new HttpPost(url);
            StringEntity params = new StringEntity(jsonData.toString());

            for (Object key : header.keySet()) {
                request.addHeader(key.toString(), header.get(key).toString());
            }

            request.setEntity(params);
            HttpResponse hresp  = httpClient.execute(request);

            HttpEntity httpEntity = hresp.getEntity();
            String apiOutput = EntityUtils.toString(httpEntity);
            JSONParser parser = new JSONParser();

            return (JSONObject)parser.parse(apiOutput);
        } catch (Exception ex) {
            JSONObject err = new JSONObject();
            err.put("error", ex.getMessage());
            return err;
        } 
    }

    public  JSONObject getFunc(String path, JSONObject jsonData){
        Map<String, Object> header = computeHeader(jsonData);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        List<String> qstring_list = new ArrayList<String>();

        for (Object key : jsonData.keySet()) {
            qstring_list.add(key.toString() + "="+jsonData.get(key).toString());
        }

        try {
            String url = this.baseUrl + path + "?" + String.join("&", qstring_list);
            HttpGet request = new HttpGet(url);

            for (Object key : header.keySet()) {
                request.addHeader(key.toString(), header.get(key).toString());
            }

            HttpResponse hresp  = httpClient.execute(request);

            HttpEntity httpEntity = hresp.getEntity();
            String apiOutput = EntityUtils.toString(httpEntity);
        
            JSONParser parser = new JSONParser();

            return (JSONObject)parser.parse(apiOutput);

        } catch (Exception ex) {
            JSONObject err = new JSONObject();
            err.put("error", ex.getMessage());
            return err;
        } 
  

    }

    public JSONObject deleteFunc(String path, JSONObject jsonData){
        Map<String,Object> header = computeHeader(jsonData);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        List<String> qstring_list = new ArrayList<String>();

        for (Object key : jsonData.keySet()) {
            qstring_list.add(key.toString() + "="+jsonData.get(key).toString());
        }

        try {
            String url = this.baseUrl + path + "?" + String.join("&", qstring_list);
            HttpDelete request = new HttpDelete(url);

            for (Object key : header.keySet()) {
                request.addHeader(key.toString(), header.get(key).toString());
            }

            HttpResponse hresp  = httpClient.execute(request);

            HttpEntity httpEntity = hresp.getEntity();
            String apiOutput = EntityUtils.toString(httpEntity);
            JSONParser parser = new JSONParser();

            return (JSONObject)parser.parse(apiOutput);

        } catch (Exception ex) {
            JSONObject err = new JSONObject();
            err.put("error", ex.getMessage());
            return err;
        } 

        
  

    }
    public static void main(String[] args){
        
    };
}
