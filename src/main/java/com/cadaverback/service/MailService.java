package com.cadaverback.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cadaverback.model.Phrase;
import com.cadaverback.model.User;
import com.sun.istack.NotNull;

@Service
public class MailService implements IMailService
{
    final protected static String senderMail = "";

    @Override
    public void sendCompletePhraseByMailToUsers(@NotNull final Phrase phrase)
    {

        System.out.println("ENVOI D'un mail");
        final List<User> authors = new ArrayList<>();
        authors.add(phrase.getSubject().getUser());
        authors.add(phrase.getVerb().getUser());
        authors.add(phrase.getDirectObject().getUser());
        authors.add(phrase.getCircumstantialObject().getUser());

        final List<String> distinctsUsersMails = authors.stream().map(a -> a.getEmail()).filter(m -> StringUtils.isNotEmpty(m)).distinct().collect(Collectors.toList());
        String mailCommaSeparated = String.join(",", distinctsUsersMails);
        final String host = "smtp.gmail.com";
        final String port = "587";
        final String from = "cadavreexquis.noreply@gmail.com";
        final String user = from;
        final String password = "mytruite25";
        Properties properties = new Properties();
        // properties.put("mail.smtps.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);
        properties.setProperty("mail.smtp.user", user);
        properties.setProperty("mail.smtp.password", password);
        properties.put("mail.smtp.auth", "true");

        // properties.setProperty("mail.smtp.starttls.enable", "true");
        try
        {

            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication()
                {
                    return new PasswordAuthentication(user, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailCommaSeparated));
            message.setSubject("Mail Subject");

            String msg = "This is my first email using JavaMailer";

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport transport = session.getTransport("smtp");
            System.out.println("Got Transport" + transport);
            transport.connect(host, user, password);
            transport.sendMessage(message, message.getAllRecipients());

            // Transport transport = session.getTransport("smtp");
            // transport.connect(host, from, password);
            //
            // transport.sendMessage(message, message.getAllRecipients());
            // transport.close();

        } catch (Exception e)
        {

            System.out.println(e.getMessage());
            // TODO: handle exception
        }

    }

}
