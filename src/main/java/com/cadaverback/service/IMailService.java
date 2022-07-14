package com.cadaverback.service;

import javax.mail.MessagingException;

import com.cadaverback.model.Phrase;

public interface IMailService
{

    /**
     * 
     * @param phrase
     *            {@link Phrase} avec tous ses mots sett�s
     * @throws MessagingException
     */
    void sendCompletePhraseByMailToUsers(Phrase phrase);

}
