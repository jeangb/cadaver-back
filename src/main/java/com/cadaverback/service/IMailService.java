package com.cadaverback.service;

import javax.mail.MessagingException;

import com.cadaverback.model.Phrase;

public interface IMailService
{

    /**
     * 
     * @param phrase
     *            {@link Phrase} avec tous ses mots settés
     * @throws MessagingException
     */
    void sendCompletePhraseByMailToUsers(Phrase phrase);

}
