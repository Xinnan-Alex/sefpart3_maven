package com.sefpart3.Model;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.codec.binary.Base64;

import org.json.JSONObject;

public class GmailAPI {
    private static final String APPLICATION_NAME = "Gmail API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String user = "me";
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static String filepath =  "../Resources/credentials.json";
    private static final String REFRESHTOKEN_STRING = "1//0gviPVKHBIb62CgYIARAAGBASNwF-L9IrtF-XHZ3_JAG0Ew86We6uSOVvlengEPWHE6RI2cBDJIl2qhUS93HtNQGUpn07Iu0dgy4";
    


    public static Gmail getGmailAPIService() throws Exception{
        InputStream in = GmailAPI.class.getResourceAsStream(filepath);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        Credential authorise = new GoogleCredential.Builder().setTransport(GoogleNetHttpTransport.newTrustedTransport())
        .setJsonFactory(JSON_FACTORY)
        .setClientSecrets(clientSecrets.getDetails().getClientId().toString(),clientSecrets.getDetails().getClientSecret().toString())
        .build().setAccessToken(getAccessToken()).setRefreshToken(REFRESHTOKEN_STRING);

        final NetHttpTransport hTTP_Transport = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(hTTP_Transport, JSON_FACTORY, authorise).setApplicationName(APPLICATION_NAME).build();

        return service;

    }

    public static void sendVerificationCode(String email, String verificationcode) throws Exception {
        Gmail service = getGmailAPIService();

        MimeMessage mimeMessage = createEmail(email, user, "I am fine notification system verification code", "This is your verification code:" + verificationcode + "\n This code is only valid for 5minutes.");
        Message message = createMessageWithEmail(mimeMessage);

        service.users().messages().send(user, message).execute();
    }

    public static MimeMessage createEmail(String to,String from,String subject,String bodyText)throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO,new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    public static Message createMessageWithEmail(MimeMessage emailContent)throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    } 

    public Message sendMessage(Gmail service,
                                      String userId,
                                      MimeMessage emailContent)
            throws MessagingException, IOException {
        Message message = createMessageWithEmail(emailContent);
        message = service.users().messages().send(userId, message).execute();

        System.out.println("Message id: " + message.getId());
        System.out.println(message.toPrettyString());
        return message;
    }


    private static String getAccessToken() throws IOException {
        InputStream in = GmailAPI.class.getResourceAsStream(filepath);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		try {
			Map<String, Object> params = new LinkedHashMap<>();
			params.put("grant_type", "refresh_token");
			params.put("client_id", clientSecrets.getDetails().getClientId().toString()); 
			params.put("client_secret", clientSecrets.getDetails().getClientSecret().toString());
			params.put("refresh_token", REFRESHTOKEN_STRING);

			StringBuilder postData = new StringBuilder();
			for (Map.Entry<String, Object> param : params.entrySet()) {
				if (postData.length() != 0) {
					postData.append('&');
				}
				postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
				postData.append('=');
				postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
			}
			byte[] postDataBytes = postData.toString().getBytes("UTF-8");

			URL url = new URL("https://accounts.google.com/o/oauth2/token");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.getOutputStream().write(postDataBytes);

			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuffer buffer = new StringBuffer();
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				buffer.append(line);
			}

			JSONObject json = new JSONObject(buffer.toString());
			String accessToken = json.getString("access_token");
			return accessToken;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
    
}
