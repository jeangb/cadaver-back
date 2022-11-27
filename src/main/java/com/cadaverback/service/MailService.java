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
import com.cadaverback.model.dto.UserDTO;
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

    @Value("${url.prod}")
    private String urlProd;

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
            message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(phrase.getMailsAuteursSepparatedByComma()));
            message.setSubject("CadavreExquis.fr - Une phrase à laquelle vous avez contribué est complète !");
            message.setContent(getBodyMailFinishedPhrase(phrase), "text/html; charset=UTF-8");

            Transport.send(message);

        } catch (MessagingException e)
        {
            e.printStackTrace();
        }

    }

    private String getBodyMailFinishedPhrase(final Phrase phrase)
    {
        final String strIdPhrase = Long.toString(phrase.getId());

        StringBuilder sb = new StringBuilder("<html>").append("<h2>CadavreExquis.fr</h2>");
        sb.append("<span>La phrase suivante (id=").append(phrase.getId()).append(") est complète :</span>");
        sb.append("</br></br>");
        sb.append("Pour découvrir la phrase, ").append(getLinkToSiteAndFocusToPhrase(strIdPhrase)).append(" !");
        // sb.append("<span style=\"font-style:italic;font-size:large\">").append(phrase.getContenu()).append("</span>");
        sb.append("</br></br>");
        sb.append("<span>Les différents auteurs sont ").append(phrase.getAuteursUsernamesSepparatedByComma()).append(".</span>");
        sb.append("</br></br>");
        sb.append("<span>A bientôt sur ").append(getUrlSite()).append("</span>");
        sb.append("</html>");
        return sb.toString();
    }

    private String getLinkToSiteAndFocusToPhrase(final String idPhrase)
    {
        StringBuilder sb = new StringBuilder("<a href=\"");
        sb.append(urlProd).append("?id_phrase=").append(idPhrase).append("\" target=\"_blank\">cliquez ici</a>");
        return sb.toString();
    }

    private String getUrlSite()
    {
        return "<a href=\"" + urlProd + " target=\"_blank\">CadavreExquis.fr</a>";
    }

    private String getBodyMailRegistration(final UserDTO user)
    {
        StringBuilder sb = new StringBuilder("<html>").append("<h2>CadavreExquis.fr</h2>");
        sb.append("<span>Bienvenu ").append(user.getUsername()).append(".</span>");
        sb.append("</br></br>");
        sb.append("<span>Votre mot de passe est ").append(user.getPassword()).append("</span>");
        sb.append("</br></br>");
        sb.append("<span>A bientôt sur ").append(getUrlSite()).append("</span>");
        sb.append("</html>");

        return sb.toString();
    }

    @Override
    public void sendRegistrationMail(UserDTO user)
    {
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
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
            message.setSubject("CadavreExquis.fr - création d'un compte");
            message.setContent(getBodyMailRegistration(user), "text/html; charset=UTF-8");

            Transport.send(message);

        } catch (MessagingException e)
        {
            e.printStackTrace();
        }
    }

}
