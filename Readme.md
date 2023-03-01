 
# Selcom API Gateway Client - JAVA

<p align='center'>
<img src="https://img.shields.io/badge/java-100%25-green">
</p>


## Homepage
https://developers.selcommobile.com/

## Description
This is a library containing functions that aid in the accessing of selcom api. 


## Installation 
Get Snippet for relevant package manager from https://central.sonatype.com/artifact/io.github.selcompaytechltd/apigwClient/1.0.0/overview 

or 
search for io.github.selcompaytechltd/apigwClient using package manager

### Use

```java
//use package
import io.github.selcompaytechltd.ApigwClient;

//// initalize a new apiAccess instace with values of the base url, api key and api secret

ApigwClient client = new ApigwClient(baseUrl, apiKey, apiSecret);

// computeHeader a dictionary containing data to bes submitted
// computeHeader returns an array with values for the following header fields: 
// Authorization, Timestamp, Digest, Signed-Fields
client.computeHeader( jsonData):

// postFuct takes relative path to base url. a JSONObject containing data to be submitted 
// It performs a POST request of the submitted data to the destniation url generatingg the header internally
// IT returns a JSONObject containing the response data to the request
client.postFunc(path, jsonData)

// getFuct takestakes relative path to base url. a JSONObject containing data to be submitted  
// It performs a GET request adding the query to the  url and generatingg the header internally
// IT returns a SJSONObject containing the response data to the request
client.getFunc(path, jsonData)

// deletetFuct takes relative path to base url.a JSONObject containing data to be submitted 
// It performs a DELETE request adding the query to the  url and generatingg the header internally
// IT returns a JSONObject containing the response data to the request
client.deleteFunc(path, jsonData)
```
### Examples
```java
//import package
import io.github.selcompaytechltd.ApigwClient;
import org.json.simple.JSONObject;
import java.util.Map;

String apiKey = '202cb962ac59075b964b07152d234b70';
String apiSecret = '81dc9bdb52d04dc20036dbd8313ed055';
String baseUrl = "http://example.com";



// initalize a new Client instace with values of the base url, api key and api secret
Client client = new ApigwClient(baseUrl,apiKey,apiSecret);

// path relatiive to base url
String orderPath = "/v1/checkout/create-order-minimal";

//order data
JSONObject orderDict = new JSONObject();
orderDict.put("vendor","VENDORTILL");
orderDict.put("order_id","1218d00Y");
orderDict.put("buyer_email", "john@example.com");
orderDict.put("buyer_name", "John Joh");
orderDict.put("buyer_phone", "255682555555");
orderDict.put("amount",  8000);
orderDict.put("currency","TZS");
orderDict.put("buyer_remarks","None");
orderDict.put("merchant_remarks","None");
orderDict.put("no_of_items", 1 );

//post data
JSONObject response = client.postFunc(orderPath ,orderDict);
// print response
System.out.println(reponse);
```
