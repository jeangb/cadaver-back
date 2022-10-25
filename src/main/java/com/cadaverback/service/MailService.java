package com.cadaverback.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cadaverback.model.Phrase;
import com.sun.istack.NotNull;

@Service
public class MailService implements IMailService
{

    @Value("${mail.host}")
    private String mailHost;

    @Value("${mail.post}")
    private String mailPort;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    @Override
    public void sendCompletePhraseByMailToUsers(@NotNull final Phrase phrase)
    {
        System.out.println("ENVOI D'un mail");

        Properties prop = new Properties();
        prop.put("mail.smtp.host", mailHost);
        prop.put("mail.smtp.port", mailPort);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(username, password);
            }
        });

        try
        {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(phrase.getMailsAuteursSepparatedByComma()));
            message.setSubject("CadavreExquis.fr - Une phrase à la quelle vous avez contribué est complète !");
            message.setText(getBodyMail(phrase));

            Transport.send(message);

        } catch (MessagingException e)
        {
            e.printStackTrace();
        }

    }

    private String getBodyMail(final Phrase phrase)
    {
        return "La phrase suivante (id=" + phrase.getId() + ") est complète :" + "\n\n" + phrase.getContenu() + " \n\n Les différentes auteurs sont "
                + phrase.getAuteursUsernamesSepparatedByComma();
    }

}
