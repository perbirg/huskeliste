Huskeliste
==========

Huskeliste er en web-applikasjon for administrasjon av huskelister.

GIT-prosjektet inneholder alt av java-moduler og funksjonell dokumentasjon relatert til applikasjonen.


### Hvor finner jeg dokumentasjonen for Huskeliste?

- [Bruks- og metodikkdokumentasjon](dokumentasjon/readme.md)


Hvordan bygge huskeliste
========================

    mvn clean verify

Hvordan kjøre Huskeliste
========================

Huskeliste kan kjøres fra IntelliJ ved å kjøre klassen HuskelisteApplication.

Hvordan verifisere den kjørende dokumentasjonen til Huskeliste fra IntelliJ?
============================================================================

Ettersom IntelliJ har integrert støtte for å kjøre Cucumber-features kan dokumentasjonen til Panda eksekveres og verifiseres
direkte fra IntelliJ.

For å kjøre dokumentasjonen via Cucumber-støtten til IntelliJ må du sette opp en run-configuration av type **Cucumber java**. 

Følgende innstillinger gir et eksempel på hvordan den typisk må settes opp for å fungere:

Main class : **cucumber.api.cli.Main** <br>
Glue : **no.pbe.huskeliste** <br> 
Feature or folder path : **C:\Users\pbe\gitproj\huskeliste\dokumentasjon\** <br>
Program arguments :  **--plugin org.jetbrains.plugins.cucumber.java.run.CucumberJvm3SMFormatter --tags "not @ignore"** <br>
Working directory : **C:\Users\pbe\gitproj\huskeliste** <br>
Use classpath of module : **huskeliste-dokumentasjon**

