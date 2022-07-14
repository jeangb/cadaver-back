package com.cadaverback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cadaverback.dao.CircumstantialObjectRepository;
import com.cadaverback.dao.DirectObjectRepository;
import com.cadaverback.dao.SubjectRepository;
import com.cadaverback.dao.VerbRepository;

@Service
public class PhraseRandomGeneratorService implements IPhraseRandomGeneratorService
{
    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    VerbRepository verbRepository;

    @Autowired
    DirectObjectRepository directObjectRepository;

    @Autowired
    CircumstantialObjectRepository circumstantialObjectRepository;

    @Override
    public String getRandomlyGeneratedPhrase()
    {
        StringBuilder myPhrase = new StringBuilder()

                .append(subjectRepository.findRandom().getLibelle()).append(" ")

                .append(verbRepository.findRandom().getLibelle()).append(" ")

                .append(directObjectRepository.findRandom().getLibelle()).append(" ")

                .append(circumstantialObjectRepository.findRandom().getLibelle());
        return beautifyPhrase(myPhrase);
    }

    /**
     * Ajoute une majuscule au début de la phrase ainsi qu'un point à la fin.
     * 
     * @param phrase
     * @return
     */
    private String beautifyPhrase(StringBuilder phrase)
    {
        phrase.append(".");
        return phrase.substring(0, 1).toUpperCase() + phrase.substring(1);
    }

}
