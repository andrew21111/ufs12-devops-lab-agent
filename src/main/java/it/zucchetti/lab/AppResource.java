package it.zucchetti.lab;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.InetAddress;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * L'applicazione del corso. /info e /health sono pronti.
 * Gli esercizi sono descritti in docs/ESERCIZI.md e segnati qui sotto con TODO.
 */
@Path("/")
public class AppResource {

    private static final Instant STARTED_AT = Instant.now();

    /** Letto dalla variabile d'ambiente MESSAGE (vedi application.properties). */
    @ConfigProperty(name = "app.message")
    String message;

    /** Letto dalla variabile d'ambiente APP_VERSION. */
    @ConfigProperty(name = "app.version")
    String version;

    /** false durante i test: /crash non spegne davvero l'app. */
    @ConfigProperty(name = "app.crash.exit", defaultValue = "true")
    boolean crashExit;

    // TODO ESERCIZIO 1 - pagina principale
    // Restituisci una pagina HTML che mostri message, hostname() e version.
    // Usa escape(...) su ogni valore che inserisci nell'HTML.
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String home() {
        return "<p>Esercizio 1 da completare</p>";
    }

    /** Le informazioni sull'istanza, in JSON. */
    @GET
    @Path("info")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> info() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("hostname", hostname());
        info.put("version", version);
        info.put("startedAt", STARTED_AT.toString());
        // TODO ESERCIZIO 3 (facoltativo) - aggiungi "uptimeSeconds": da quanti secondi l'app è accesa
        return info;
    }

    /** Kubernetes interroga questo indirizzo per sapere se l'app è viva. */
    @GET
    @Path("health")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }

    // TODO ESERCIZIO 2 - POST /crash
    // Scrivi un metodo che risponda a POST /crash con stato 202 e corpo {"status":"crashing"},
    // e che chiami spegniTraPoco() per spegnere l'app subito dopo la risposta.

    // TODO ESERCIZIO 4 (facoltativo) - GET /saluto?nome=Mario
    // Risponde {"saluto":"Ciao Mario!"}; senza nome risponde {"saluto":"Ciao mondo!"}.

    /** Spegne la JVM dopo 200 ms, così la risposta HTTP fa in tempo a partire. Nei test non spegne nulla. */
    void spegniTraPoco() {
        if (!crashExit) {
            return;
        }
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.exit(1);
        });
        t.start();
    }

    /** In Kubernetes l'hostname del container coincide con il nome del Pod. */
    static String hostname() {
        String fromEnv = System.getenv("HOSTNAME");
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv;
        }
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "sconosciuto";
        }
    }

    /** Rende sicuro un testo da inserire in una pagina HTML. */
    static String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
