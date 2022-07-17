package com.cadaverback.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.cadaverback.model.Phrase;
import com.sun.istack.NotNull;

@Service
public class MailService implements IMailService
{

    @Override
    public void sendCompletePhraseByMailToUsers(@NotNull final Phrase phrase)
    {
        System.out.println("ENVOI D'un mail");

        final String username = "cadavreexquis.noreply@gmail.com";
        final String password = "awmmljxfzinyoryp";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); // TLS

        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(username, password);
            }
        });

        try
        {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("cadavreexquis.noreply@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(phrase.getMailsAuteursSepparatedByComma()));
            message.setSubject("CadavreExquis.fr - Une phrase � la quelle vous avez contribu� est compl�te !");
            message.setText(getBodyMail(phrase));

            Transport.send(message);

        } catch (MessagingException e)
        {
            e.printStackTrace();
        }

    }

    private String getBodyMail(final Phrase phrase)
    {
        return "La phrase suivante (id=" + phrase.getId() + ") est compl�te :" + "\n\n" + phrase.getContenu() + " \n\n Les diff�rentes auteurs sont "
                + phrase.getAuteursUsernamesSepparatedByComma();
    }

}
