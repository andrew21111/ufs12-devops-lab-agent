# DevOps & Containerizzazione · Giorno 1

Oggi costruiamo una piccola applicazione Java, la chiudiamo in un container e la pubblichiamo su un registry.
Domani la stessa immagine girerà su Kubernetes.

```
Repository → App Java → Dockerfile → Immagine → Registry → Kubernetes (domani)
```

Nel pomeriggio completi un'app già avviata, con l'aiuto di un assistente di intelligenza artificiale nel browser. L'assistente non vede il codice: gli incolli tu quello che serve.

## Chi ha lavorato su questo repository

<!-- Lab 1: aggiungi qui il tuo nome con una Pull Request -->

## Prerequisiti

| Strumento | Verifica |
| --- | --- |
| Git | `git --version` |
| JDK 17 o 21 (Eclipse Temurin) | `java -version` |
| Docker Desktop | `docker run hello-world` |
| VS Code + Extension Pack for Java | apri questa cartella |
| Account GitHub e Docker Hub | `docker login` |

Maven non serve: il progetto contiene il wrapper `./mvnw` (su Windows `mvnw.cmd`), che scarica Maven da solo.

## Cosa c'è nel repository

| File | A cosa serve |
| --- | --- |
| `README.md` | queste istruzioni |
| `docs/ESERCIZI.md` | gli esercizi del Lab 3, con la verifica di ognuno |
| `docs/CONTESTO.md` | il testo da incollare nell'assistente web come primo messaggio |
| `AGENTS.md` | le regole per gli assistenti di IA |
| `src/main/java/.../AppResource.java` | l'app: `/info` e `/health` pronti, gli esercizi segnati con `TODO` |
| `src/test/java/...` | i test: quelli degli esercizi sono spenti con `@Disabled` |
| `pom.xml`, `mvnw`, `.mvn/` | progetto Quarkus e Maven wrapper |
| `.env.example` | le variabili d'ambiente dell'app: copialo in `.env` |
| `.dockerignore` | cosa escludere dalla build dell'immagine |
| `docs/pipeline-demo.yaml` | la pipeline mostrata in aula (non viene eseguita) |

Comandi utili:

```bash
cp .env.example .env   # una volta sola: le variabili per la dev mode
./mvnw quarkus:dev     # avvia l'app in modalità sviluppo, con ricarica automatica
./mvnw test            # esegue i test
./mvnw package         # crea target/*-runner.jar
```

Con l'app avviata, apri `http://localhost:8080/q/swagger-ui`: vedi tutti gli endpoint e li provi dal browser.

---

## Lab 1 · Clone, branch, Pull Request (30')

1. Su GitHub premi **Use this template → Create a new repository** e crea la tua copia.
2. Clonala e crea un branch:
```bash
   git clone <URL della tua copia>
   cd <nome repo>
   git switch -c feature/mio-nome
```
3. Aggiungi il tuo nome nella sezione *Chi ha lavorato su questo repository*.
4. Salva il lavoro e pubblicalo:
```bash
   git add README.md
   git commit -m "docs: aggiungo il mio nome"
   git push -u origin feature/mio-nome
```
5. Su GitHub apri la Pull Request (**Compare & pull request**).
6. Un compagno legge la modifica e la approva, poi fai **Merge**.
7. Torna su `main` e aggiorna: `git switch main && git pull`

## Lab 2 · Il tuo primo container (30')

```bash
docker run -d --name web -p 8080:80 nginx:1.27-alpine
docker ps
# apri http://localhost:8080
docker logs web
docker exec -it web sh
  cat /etc/os-release
  exit
docker stop web && docker rm web
```

**Sfida:** avvia un secondo nginx. Cosa succede se usi di nuovo la porta 8080? Come risolvi?

**Bonus:** Java senza installarlo: `docker run --rm eclipse-temurin:21-jre java -version`

## Lab 3 · Gli esercizi e il container (60')

1. **Crea un branch:** `git switch -c feature/esercizi`
2. **Avvia l'app** e controlla che funzioni prima di toccare qualcosa:
```bash
   cp .env.example .env
   ./mvnw quarkus:dev
   curl localhost:8080/info
   curl localhost:8080/health
```
3. **Apri l'assistente web** e incolla il testo di `docs/CONTESTO.md` come primo messaggio.
4. **Fai gli esercizi** di `docs/ESERCIZI.md`: l'1 e il 2 sono obbligatori, il 3 e il 4 facoltativi.
   Per ognuno incolla nella chat il testo dell'esercizio e il codice su cui lavori.
5. **Verifica:** cancella la riga `@Disabled` sopra il test dell'esercizio e lancia `./mvnw test`.

### Il Dockerfile

Il Dockerfile lo scrivi tu, nella cartella principale del progetto:

```dockerfile
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/*-runner.jar app.jar
ENV PORT=8080
EXPOSE 8080
USER 1001
CMD ["java", "-jar", "app.jar"]
```

### Immagine e container

```bash
./mvnw package
docker build -t mia-app:1.0 .
docker run -d --name app -p 8080:8080 -e APP_VERSION=1.0 -e MESSAGE="Ciao!" mia-app:1.0
curl localhost:8080/info
curl -X POST localhost:8080/crash
docker ps -a      # il container è Exited: nessuno lo riavvia
docker rm app
```

Infine commit, push e Pull Request, come nel Lab 1. Ferma la dev mode prima di avviare il container: usano la stessa porta.

## Lab 4 · Il tuo registry locale (40')

Un registry è il magazzino delle immagini. Usiamo l'immagine ufficiale `registry`, il registry open source
(progetto CNCF *Distribution*): parla la stessa API di Docker Hub.

```bash
# 1. Avvia il registry: è anch'esso un container
docker run -d -p 5001:5000 --name registry registry:2

# 2. Tagga l'immagine con l'indirizzo del registry e pubblicala
docker tag mia-app:1.0 localhost:5001/mia-app:1.0
docker push localhost:5001/mia-app:1.0
curl localhost:5001/v2/_catalog

# 3. Cancella la copia locale e riscaricala dal registry
docker rmi mia-app:1.0 localhost:5001/mia-app:1.0
docker pull localhost:5001/mia-app:1.0
docker run -d --name app -p 8080:8080 localhost:5001/mia-app:1.0
curl localhost:8080/info
```

**Sfida:** cambia il messaggio di default, ricostruisci il JAR, crea `mia-app:2.0` con `APP_VERSION` a `2.0` e pubblicala.
Alla fine `curl localhost:5001/v2/mia-app/tags/list` deve mostrare `1.0` e `2.0`.

```
Immagine: localhost:5001/mia-app (tag 1.0 e 2.0)
```

## Checklist per domani

- [ ] App e `Dockerfile` nel repository, Pull Request fatta
- [ ] `/health` e `/info` rispondono
- [ ] L'app ascolta su `0.0.0.0:8080`
- [ ] `POST /crash` spegne il container
- [ ] `mia-app:1.0` e `mia-app:2.0` sono su `localhost:5001`
- [ ] Il container `registry` e le immagini restano sul PC

## Problemi frequenti

| Sintomo | Causa e soluzione |
| --- | --- |
| `port is already allocated` | la porta è usata da un altro container o dalla dev mode: `docker ps -a`, `docker rm -f <nome>` |
| il browser non raggiunge l'app nel container | l'app ascolta su `localhost`: serve `quarkus.http.host=0.0.0.0` |
| `COPY failed: no source files were specified` | manca il JAR: esegui `./mvnw package` prima di `docker build` |
| `docker build` chiede un argomento | manca il punto finale: `docker build -t mia-app:1.0 .` |
| `./mvnw: Permission denied` (macOS/Linux) | `chmod +x mvnw` |
| `mvnw` non parte su Windows | usa `mvnw.cmd` |
| errore di compilazione dopo aver incollato codice | parentesi graffe spaiate o `import` mancanti: incolla l'errore completo nell'assistente |
| `cannot find symbol` su `Response` o `POST` | manca l'`import` da `jakarta.ws.rs` |
| con Podman il push fallisce per HTTPS | `podman push --tls-verify=false localhost:5001/mia-app:1.0` |
