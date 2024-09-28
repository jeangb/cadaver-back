package com.cadaverback.service;

import com.cadaverback.model.Phrase;
import com.cadaverback.model.dto.UserDTO;
import com.sun.istack.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService implements IMailService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender emailSender;
    private final String username;
    private final String urlProd;

    public MailService(JavaMailSender emailSender,
                       @Value("${mail.username}") String username,
                       @Value("${url.prod}") String urlProd) {
        this.emailSender = emailSender;
        this.username = username;
        this.urlProd = urlProd;
    }

    @Override
    public void sendCompletePhraseByMailToUsers(@NotNull final Phrase phrase) {
        LOGGER.info("ENVOI D'un mail");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(phrase.getMailsAuteurs().toArray(new String[0]));
        message.setSubject("CadavreExquis.fr - Une phrase à laquelle vous avez contribué est complète !");
        message.setText(getBodyMailFinishedPhrase(phrase));
        emailSender.send(message);
    }

    private String getBodyMailFinishedPhrase(final Phrase phrase) {
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

    private String getLinkToSiteAndFocusToPhrase(final String idPhrase) {
        StringBuilder sb = new StringBuilder("<a href=\"");
        sb.append(urlProd).append("?id_phrase=").append(idPhrase).append("\" target=\"_blank\">cliquez ici</a>");
        return sb.toString();
    }

    private String getUrlSite() {
        return "<a href=\"" + urlProd + " target=\"_blank\">CadavreExquis.fr</a>";
    }

    private String getBodyMailRegistration(final UserDTO user) {
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
    public void sendRegistrationMail(UserDTO user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(user.getEmail());
        message.setSubject("CadavreExquis.fr - création d'un compte");
        message.setText(getBodyMailRegistration(user));
        emailSender.send(message);
    }
}
