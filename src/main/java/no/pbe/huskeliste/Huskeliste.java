package no.pbe.huskeliste;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Huskeliste {
    private String id;

    private HashMap<String, Oppgave> oppgaver = new HashMap<>();

    public Huskeliste(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public List<Oppgave> getOppgaver() {
        return new ArrayList<>(oppgaver.values());
    }

    public void leggTilOppgave(Oppgave oppgave) {
        oppgaver.put(oppgave.getTittel(), oppgave);
    }

    public void fjernOppgave(final String oppgaveTittel) {
        oppgaver.remove(oppgaveTittel);
    }

}
