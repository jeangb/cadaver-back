package com.cadaverback.service;

import com.cadaverback.model.Phrase;
import com.cadaverback.model.dto.UserDTO;

import javax.mail.MessagingException;

public interface IMailService {
    /**
     *
     * @param phrase  {@link Phrase} avec tous ses mots settés
     * @throws MessagingException
     */
    void sendCompletePhraseByMailToUsers(Phrase phrase);

    void sendRegistrationMail(UserDTO user);
}
