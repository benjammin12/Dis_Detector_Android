package com.example.benjaminxerri.dis_detector.model;

import android.content.Intent;
import android.util.Log;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import static android.content.ContentValues.TAG;

/**
 * Created by benjaminxerri on 9/30/17.
 */

public class GmailClient extends javax.mail.Authenticator {

    private String gmailImap = "imap.gmail.com";
    private String outLookHost = "imap-mail.outlook.com";
    private Session session;
    private Store store;
    private String email;
    private String password;

    public GmailClient(String email, String password){


        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");

        Log.d(TAG, "Transport: "+props.getProperty("mail.transport.protocol"));
        Log.d(TAG, "Store: "+props.getProperty("mail.store.protocol"));
        Log.d(TAG, "Host: "+props.getProperty("mail.imap.host"));
        Log.d(TAG, "Authentication: "+props.getProperty("mail.imap.auth"));
        Log.d(TAG, "Port: "+props.getProperty("mail.imap.port"));

            this.email = email;
            this.password = password;

            try {
                session = Session.getDefaultInstance(props, null);
                store = session.getStore("imaps");
                store.connect(gmailImap, email, password);
                Log.i(TAG, "Store: "+store.toString());
            } catch (NoSuchProviderException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (MessagingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public synchronized String readMail() throws Exception {
        try {
            Folder folder = store.getFolder("Inbox");
            folder.open(Folder.READ_ONLY);

            Message[] totalMessages = folder.getMessages();
            Message lastMessage = folder.getMessage(totalMessages.length-1);
            Multipart message =(Multipart) lastMessage.getContent();
            BodyPart body = message.getBodyPart(0);
            return body.getContent().toString();

        } catch (Exception e) {
            Log.e("readMail", e.getMessage(), e);
            return null;
        }
    }
}



