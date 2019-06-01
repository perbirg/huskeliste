package no.pbe.huskeliste;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HuskelisteRestController {

    @Autowired
    private HuskelisteService huskelisteService;

    @RequestMapping(value = "/huskeliste")
    public ResponseEntity<Object> hentHuskelister() {
        return new ResponseEntity<>(huskelisteService.alleHuskelister(), HttpStatus.OK);
    }

    @RequestMapping(value = "/huskeliste/{id}/oppgave")
    public ResponseEntity<Object> hentOppgaver(
            @PathVariable("id") String id
    ) {
        List<Oppgave> oppgaver = huskelisteService.alleOppgaver(id);

        return new ResponseEntity<>(oppgaver, HttpStatus.OK);
    }

    @RequestMapping(value = "/huskeliste/{id}/oppgave", method = RequestMethod.POST)
    public ResponseEntity<Object> leggTilOppgave(
            @PathVariable("id") String id,
            @RequestBody Oppgave oppgave) {
        huskelisteService.leggTilOppgave(id, oppgave);
        return new ResponseEntity<>("Oppgave er lagt til", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/huskeliste/{id}/oppgave/{tittel}", method = RequestMethod.PUT)
    public ResponseEntity<Object> endreOppgave(
            @PathVariable("id") String id,
            @PathVariable("tittel") String tittel,
            @RequestBody Oppgave oppgave) {
        huskelisteService.endreOppgave(id, tittel, oppgave);
        return new ResponseEntity<>("Oppgaven er endret", HttpStatus.OK);
    }

    @RequestMapping(value = "/huskeliste/{id}/oppgave{tittel}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> fjernOppgave(
            @PathVariable("id") String id,
            @PathVariable("tittel") String tittel) {
        huskelisteService.fjernOppgave(id, tittel);
        return new ResponseEntity<>("Oppgaven er endret", HttpStatus.OK);
    }

}
