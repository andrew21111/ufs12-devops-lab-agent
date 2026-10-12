# Prompt del Lab 3 · versione di riferimento

## Contesto

Progetto Quarkus 3 con Java 17, estensioni Quarkus REST e REST Jackson, package `it.zucchetti.lab`.
La build usa il Maven wrapper (`./mvnw`) e produce un uber-jar, già configurato.

## Obiettivo

Una web app con quattro endpoint, pronta per girare in un container e poi su Kubernetes.

## Requisiti

1. `GET /` restituisce una pagina HTML con il messaggio, l'hostname e la versione.
2. `GET /info` restituisce JSON con `hostname`, `version` e `startedAt` (istante di avvio in formato ISO-8601).
3. `GET /health` restituisce `200` con `{"status":"ok"}`.
4. `POST /crash` risponde `202` con `{"status":"crashing"}` e, circa 200 ms dopo la risposta, spegne l'app con `System.exit(1)`.
5. Il messaggio arriva dalla variabile `MESSAGE` (default `Ciao dal corso DevOps!`), la versione da `APP_VERSION` (default `1.0`).
6. La porta arriva da `PORT` (default `8080`) e l'app ascolta su `0.0.0.0`.
7. L'hostname si legge dalla variabile `HOSTNAME`; se manca, dal sistema.

## Vincoli

- Una sola classe di risorse, nessuna nuova dipendenza, niente database, niente file su disco.
- Non toccare `Dockerfile`, `.dockerignore`, `docs/` e `prompts/`.
- I valori inseriti nell'HTML vanno resi sicuri (escape di `<`, `>`, `&`, `"`).

## Verifica

- Scrivi test `@QuarkusTest` con RestAssured per `/`, `/info`, `/health` e `/crash`.
- Esegui `./mvnw test` e riporta l'esito.

## Formato della risposta

Elenca i file creati o modificati e spiega in due frasi cosa fa ognuno.
