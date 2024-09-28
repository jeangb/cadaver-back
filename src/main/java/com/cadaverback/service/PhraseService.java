package com.cadaverback.service;

import com.cadaverback.dao.CircumstantialObjectRepository;
import com.cadaverback.dao.DirectObjectRepository;
import com.cadaverback.dao.SubjectRepository;
import com.cadaverback.dao.VerbRepository;
import org.springframework.stereotype.Service;

@Service
public class PhraseService implements IPhraseService {
    private final SubjectRepository subjectRepository;
    private final VerbRepository verbRepository;
    private final DirectObjectRepository directObjectRepository;
    private final CircumstantialObjectRepository circumstantialObjectRepository;

    public PhraseService(SubjectRepository subjectRepository, VerbRepository verbRepository, DirectObjectRepository directObjectRepository, CircumstantialObjectRepository circumstantialObjectRepository) {
        this.subjectRepository = subjectRepository;
        this.verbRepository = verbRepository;
        this.directObjectRepository = directObjectRepository;
        this.circumstantialObjectRepository = circumstantialObjectRepository;
    }

    @Override
    public String getRandomlyGeneratedPhrase() {
        StringBuilder myPhrase = new StringBuilder()
                .append(subjectRepository.findRandom().getLibelle()).append(" ")
                .append(verbRepository.findRandom().getLibelle()).append(" ")
                .append(directObjectRepository.findRandom().getLibelle()).append(" ")
                .append(circumstantialObjectRepository.findRandom().getLibelle());
        return beautifyPhrase(myPhrase);
    }

    /**
     * Ajoute une majuscule au début de la phrase ainsi qu'un point à la fin.
     */
    private String beautifyPhrase(StringBuilder phrase) {
        phrase.append(".");
        return phrase.substring(0, 1).toUpperCase() + phrase.substring(1);
    }
}
