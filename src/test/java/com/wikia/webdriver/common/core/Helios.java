package com.wikia.webdriver.common.core;

import com.wikia.webdriver.common.core.annotations.User;
import com.wikia.webdriver.common.logging.PageObjectLogging;
import com.wikia.webdriver.common.properties.HeliosConfig;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ludwik on 2015-08-05.
 */
public class Helios {

  private Helios() {
  }

  public static String getAccessToken(User user) {
    return getAccessToken(user.getUserName(), user.getPassword());
  }

  public static String getAccessToken(String userName, String password) {

    String clientId = HeliosConfig.getClientId();
    String clientSecret = HeliosConfig.getClientSecret();
    String heliosBaseUrl = HeliosConfig.getUrl(HeliosConfig.HeliosController.TOKEN);

    CloseableHttpClient httpClient = HttpClients.createDefault();

    HttpPost httpPost = new HttpPost(heliosBaseUrl);
    List<NameValuePair> nvps = new ArrayList<>();

    nvps.add(new BasicNameValuePair("grant_type", HeliosConfig.GrantType.PASSWORD.getGrantType()));
    nvps.add(new BasicNameValuePair("client_id", clientId));
    nvps.add(new BasicNameValuePair("client_secret", clientSecret));
    nvps.add(new BasicNameValuePair("username", userName));
    nvps.add(new BasicNameValuePair("password", password));

    CloseableHttpResponse response = null;
    String token = "";
    httpPost.setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8));
    try {
      response = httpClient.execute(httpPost);

      HttpEntity entity = response.getEntity();
      JSONObject responseValue = new JSONObject(EntityUtils.toString(entity));

      EntityUtils.consume(entity);

      PageObjectLogging.log("LOGIN HEADERS: ", response.toString(), true);
      PageObjectLogging.log("LOGIN RESPONSE: ", responseValue.toString(), true);

      token = responseValue.getString("access_token");
    } catch (JSONException e) {
      PageObjectLogging.log("JSON EXCEPTION", ExceptionUtils.getStackTrace(e), false);
    } catch (ClientProtocolException e) {
      PageObjectLogging.log("CLIENT PROTOCOL EXCEPTION", ExceptionUtils.getStackTrace(e), false);
    } catch (IOException e) {
      PageObjectLogging.log("IO EXCEPTION", "PLEASE CHECK IF YOUR VPN IS ENABLED" +
                                            ExceptionUtils.getStackTrace(e), false);
    }

    return token;
  }
}