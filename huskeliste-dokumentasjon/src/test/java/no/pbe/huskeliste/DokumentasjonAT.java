package no.pbe.huskeliste;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Suite som verifiserer all dokumentasjon under dokumentasjonskatalogen til huskeliste vha
 * Cucumber.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"json:target/cucumber/dokumentasjonAT.json"},
        features = {"../dokumentasjon"},
        glue = {"no.pbe.huskeliste"},
        tags = {"not @ignore"},
        strict = true
)
public class DokumentasjonAT {
}
