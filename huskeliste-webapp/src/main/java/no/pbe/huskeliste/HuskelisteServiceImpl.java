package no.pbe.huskeliste;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
public class HuskelisteServiceImpl implements HuskelisteService {
    private final Map<String, Huskeliste> huskeliste = new HashMap<>();

    public HuskelisteServiceImpl() {
        Huskeliste handleliste = new Huskeliste("Handleliste");
        handleliste.leggTilOppgave(Oppgave.oppgave("Handleliste", "Handle", "Handle melk"));

        huskeliste.put("Handleliste", handleliste);
        huskeliste.put("Gjøreoppgaver", new Huskeliste("Gjøreoppgaver"));
    }

    @Override
    public List<String> alleHuskelister() {
        List<String> huskelister = huskeliste
                .values()
                .stream()
                .map(Huskeliste::getId).collect(toList());

        return huskelister;
    }

    @Override
    public void leggTilOppgave(String id, Oppgave oppgave) {
        huskeliste.get(id).leggTilOppgave(oppgave);
    }

    @Override
    public void endreOppgave(String id, String tittel, Oppgave oppgave) {
        Huskeliste huskelisteFunnet = huskeliste.get(id);

        if (huskelisteFunnet == null) {
            throw new IllegalArgumentException("Huskelisten finnes ikke");
        }

        Oppgave oppgaveFunnet = huskelisteFunnet
                .getOppgaver()
                .stream()
                .filter(o -> o.getTittel().equalsIgnoreCase(tittel))
                .findFirst().orElseThrow(() -> new IllegalStateException("Oppgaven finnes ikke"));

        oppgaveFunnet.setBeskrivelse(oppgave.getBeskrivelse());
        oppgaveFunnet.setFerdig(oppgave.isFerdig());
    }

    @Override
    public void fjernOppgave(String id, String tittel) {
        Huskeliste huskeliste = finnHuskeliste(id);

        Oppgave oppgaveFunnet = huskeliste
                .getOppgaver()
                .stream()
                .filter(o -> o.getTittel().equalsIgnoreCase(tittel))
                .findFirst().orElseThrow(() -> new IllegalStateException("Oppgaven finnes ikke"));

        huskeliste.fjernOppgave(oppgaveFunnet.getTittel());
    }

    @Override
    public List<Oppgave> alleOppgaver(String id) {
        Huskeliste huskeliste = finnHuskeliste(id);

        return huskeliste.getOppgaver();
    }

    @Override
    public Huskeliste finnHuskeliste(String id) {
        Huskeliste huskelisteFunnet = huskeliste.get(id);

        if (huskelisteFunnet == null) {
            throw new IllegalArgumentException("Huskelisten finnes ikke");
        }

        return huskelisteFunnet;
    }
}
