package com.hci.electric.services.impl;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.hci.electric.services.MailService;

@Service
public class MailServiceImpl implements MailService {
    @Override
    public boolean sendMail(String receiver){
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        try{
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("nguyenductri04042001@gmail.com", "Admin"));
            msg.addRecipient(Message.RecipientType.TO,
                   new InternetAddress(receiver, "User"));
            msg.setSubject("Your Example.com account has been activated");
            msg.setText("This is a test");
            Transport.send(msg);

            return true;
        }
        catch(Exception exception){
            exception.printStackTrace();
            return false;
        }
    }
}
