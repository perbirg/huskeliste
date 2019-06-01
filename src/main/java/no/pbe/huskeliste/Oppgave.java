package no.pbe.huskeliste;

import java.util.Objects;

/**
 * Oppgave som ligger i en no.pbe.huskeliste.
 */
public class Oppgave {
    private String huskelisteId;

    private String tittel;
    private String beskrivelse;
    private boolean ferdig;

    private Oppgave(String huskelisteId, String tittel, String beskrivelse) {
        this.huskelisteId = huskelisteId;
        this.tittel = tittel;
        this.beskrivelse = beskrivelse;
        this.ferdig = false;
    }

    public static Oppgave oppgave(String huskelisteId, String tittel, String beskrivelse) {

        return new Oppgave(huskelisteId, tittel, beskrivelse);
    }

    public String getTittel() {
        return tittel;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public boolean isFerdig() {
        return ferdig;
    }

    public void setFerdig(boolean ferdig) {
        this.ferdig = ferdig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Oppgave oppgave = (Oppgave) o;
        return ferdig == oppgave.ferdig &&
                tittel.equals(oppgave.tittel) &&
                beskrivelse.equals(oppgave.beskrivelse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tittel, beskrivelse, ferdig);
    }
}
