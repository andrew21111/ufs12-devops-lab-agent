# Esercizi del Lab 3

L'app è già avviata: `/info` e `/health` funzionano. Gli esercizi 1 e 2 sono obbligatori, perché domani servono a Kubernetes.
Il 3 e il 4 sono facoltativi.

Ogni esercizio ha un test in `src/test/java/it/zucchetti/lab/EserciziTest.java`, spento con `@Disabled`.
Quando finisci un esercizio, cancella la riga `@Disabled` sopra il suo test e lancia `./mvnw test`.

Lavora con l'app accesa in dev mode (`./mvnw quarkus:dev`): ogni modifica salvata si vede subito.

## Esercizio 1 · La pagina principale (obbligatorio)

**Dove:** metodo `home()` in `AppResource.java`, segnato con `TODO ESERCIZIO 1`.

**Cosa deve fare:** `GET /` restituisce una pagina HTML con tre informazioni:
il messaggio (`message`), l'hostname (`hostname()`) e la versione (`version`).

**Perché:** domani, con tre copie dell'app su Kubernetes, ricaricando la pagina vedrai cambiare l'hostname.

**Come verifichi:**
- apri `http://localhost:8080` nel browser;
- riavvia con un altro messaggio: `MESSAGE="Ciao!" ./mvnw quarkus:dev` (oppure modifica `.env`);
- abilita il test `esercizio1_paginaPrincipale`.

**Suggerimento:** in Java un blocco di testo `"""…"""` con `.formatted(...)` è comodo per l'HTML.
Passa ogni valore da `escape(...)`.

## Esercizio 2 · Spegnere l'app (obbligatorio)

**Dove:** in `AppResource.java`, al posto del commento `TODO ESERCIZIO 2`.

**Cosa deve fare:** `POST /crash` risponde con stato `202` e corpo `{"status":"crashing"}`,
poi chiama `spegniTraPoco()`, che spegne l'app dopo 200 millisecondi.

**Perché:** oggi il container resterà fermo. Domani Kubernetes lo riaccenderà da solo.

**Come verifichi:**
```bash
curl -i -X POST localhost:8080/crash
```
La risposta deve essere `202` e subito dopo l'app si spegne.
Abilita il test `esercizio2_crash`: nei test l'app non si spegne davvero (`app.crash.exit=false`).

**Suggerimento:** servono le annotazioni `@POST`, `@Path` e `@Produces`, e la classe `Response` (`jakarta.ws.rs.core.Response`).

## Esercizio 3 · Da quanto è accesa (facoltativo)

**Dove:** metodo `info()`, segnato con `TODO ESERCIZIO 3`.

**Cosa deve fare:** aggiungere a `/info` il campo `uptimeSeconds`, cioè i secondi trascorsi da `STARTED_AT`.

**Come verifichi:** `curl localhost:8080/info` e il test `esercizio3_uptime`.

**Suggerimento:** `java.time.Duration.between(...)`.

## Esercizio 4 · Un saluto (facoltativo)

**Dove:** un nuovo metodo in `AppResource.java`, al posto del commento `TODO ESERCIZIO 4`.

**Cosa deve fare:** `GET /saluto?nome=Mario` risponde `{"saluto":"Ciao Mario!"}`; senza `nome` risponde `{"saluto":"Ciao mondo!"}`.

**Come verifichi:** `curl "localhost:8080/saluto?nome=Mario"` e il test `esercizio4_saluto`.

**Suggerimento:** `@QueryParam` e `@DefaultValue`.
