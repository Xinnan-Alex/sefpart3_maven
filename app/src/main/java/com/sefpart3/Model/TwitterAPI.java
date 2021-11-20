package com.sefpart3.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import twitter4j.JSONObject;
import twitter4j.JSONTokener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterAPI {

    private Twitter twitter = new TwitterFactory().getInstance();

    public TwitterAPI() {
        String filepath =  "../Resources/twitterSecret.json";
        InputStream is = TwitterAPI.class.getResourceAsStream(filepath);
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        JSONTokener tokener = new JSONTokener(in);
        JSONObject twitterSecret = new JSONObject(tokener);

        twitter.setOAuthConsumer(twitterSecret.getString("apikey"), twitterSecret.getString("apisecret"));
    }

    public Twitter getTwitterInstance(){
        return twitter;
    }

    public static void tweet(AccessToken accessToken, String message_tweet) throws TwitterException{
            try {
                String filepath =  "../Resources/twitterSecret.json";
                InputStream is = TwitterAPI.class.getResourceAsStream(filepath);
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                JSONTokener tokener = new JSONTokener(in);
                JSONObject twitterSecret = new JSONObject(tokener);

                String tweet = message_tweet;

                OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\n    \"text\": \""+ tweet + "\"\n}");
                Request request = new Request.Builder()
                    .url("https://api.twitter.com/2/tweets")
                    .method("POST", body)
                    .addHeader("Authorization", "OAuth oauth_consumer_key=\"" + twitterSecret.getString("apikey")+ "\",oauth_token=\""+ accessToken.getToken() + "\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"1637339984\",oauth_nonce=\"ZeFI5pxfTcd\",oauth_version=\"1.0\",oauth_signature=\"ivBcDljd9y4T7IiVsSpI8Q8WeLY%3D\"")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Cookie", "guest_id=v1%3A163733738963023157; guest_id_ads=v1%3A163733738963023157; guest_id_marketing=v1%3A163733738963023157; personalization_id=\"v1_BgxFvKG2DN2LZMIOTzCvMA==\"")
                    .build();
                Response response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    

}
