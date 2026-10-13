# AGENTS.md

Istruzioni per gli assistenti di intelligenza artificiale che lavorano su questo repository.
Chi lo usa sta imparando: rispondi in italiano e spiega ogni modifica in modo semplice.

Se l'assistente non vede i file (chat nel browser), il testo da incollare è in `docs/CONTESTO.md`.

## Il progetto

- Applicazione web Java con Quarkus 3 (estensioni Quarkus REST, REST Jackson e SmallRye OpenAPI per Swagger UI), package `it.zucchetti.lab`.
- Gira con Java 17 e con Java 21, ma compila con `maven.compiler.release` `17`: non usare funzionalità introdotte dopo Java 17.
- Build con il Maven wrapper: `./mvnw` (su Windows `mvnw.cmd`).
- Un'unica classe di risorse: `AppResource`. Gli esercizi sono in `docs/ESERCIZI.md` e segnati nel codice con `TODO`.

## Regole

1. Modifica solo le parti segnate con `TODO`. Non riscrivere i metodi che funzionano già (`info()`, `health()`, `spegniTraPoco()`).
2. Non creare né modificare `Dockerfile`, `.dockerignore` e la cartella `docs/`: li scrive lo studente.
3. Niente database, niente file scritti su disco, niente nuove dipendenze.
4. Usa le annotazioni `jakarta.ws.rs`, non `javax`.
5. Non chiamare `System.exit` direttamente: per spegnere l'app usa `spegniTraPoco()`, che nei test non spegne nulla.
6. Non scrivere password, token o chiavi nel codice o nella configurazione.
7. Alla fine elenca i file modificati e spiega in due o tre frasi cosa fa ogni modifica.
