package no.pbe.huskeliste;

import java.util.List;

public interface HuskelisteService {
    List<String> alleHuskelister();

    void leggTilOppgave(String id, Oppgave oppgave);

    void endreOppgave(String id, String tittel, Oppgave oppgave);

    void fjernOppgave(String id, String tittel);

    List<Oppgave> alleOppgaver(String id);

    Huskeliste finnHuskeliste(String id);
}
