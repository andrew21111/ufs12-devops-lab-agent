# Contesto da incollare nell'assistente

L'assistente nel browser non vede il progetto. Copia il blocco qui sotto e incollalo come **primo messaggio** della chat.
Poi, per ogni domanda, incolla: il testo dell'esercizio, il file su cui lavori e cosa ti serve.

```text
Sto facendo un esercizio di un corso. Lavoro su un progetto Java con Quarkus 3,
estensioni Quarkus REST e REST Jackson, build con il Maven wrapper (./mvnw).
Il codice deve compilare con Java 17: non usare funzionalità introdotte dopo Java 17.
Tu non vedi il progetto: ti incollo io i file che servono.

Il progetto ha una sola classe di risorse REST, AppResource, nel package it.zucchetti.lab,
con le annotazioni jakarta.ws.rs (NON javax). Le risposte JSON sono Map restituite dai metodi.
La configurazione è in src/main/resources/application.properties e arriva da variabili d'ambiente.
I test sono in src/test/java e usano @QuarkusTest e RestAssured.

Regole per le tue risposte:
- dammi solo il codice da aggiungere o cambiare, non la classe intera;
- dimmi esattamente dove inserirlo e quali import aggiungere;
- usa solo le librerie già presenti, niente nuove dipendenze;
- niente database e niente file scritti su disco;
- dopo il codice, spiegami in tre frasi cosa fa.
```

## Come fare una buona domanda

1. **Incolla il pezzo giusto.** Il metodo o la classe su cui stai lavorando, non tutto il progetto.
2. **Chiedi il pezzo che manca.** "Completa il metodo home()" funziona; "fammi un'app" no.
3. **Se qualcosa non va, incolla l'errore completo.** Non un riassunto: l'errore contiene quasi sempre la risposta.
4. **Leggi prima di incollare.** Se non capisci una riga, chiedi di spiegartela. Il codice lo committi tu.
5. **Mai password, token o chiavi** nella chat.
