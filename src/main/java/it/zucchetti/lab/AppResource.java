package it.zucchetti.lab;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("/")
public class AppResource {

    private static final Instant STARTED_AT = Instant.now();

    @ConfigProperty(name = "app.message")
    String message;

    @ConfigProperty(name = "app.version")
    String version;

    @ConfigProperty(name = "app.crash.exit", defaultValue = "true")
    boolean crashExit;

    /** Pagina principale: messaggio, hostname e versione. */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String home() {
        return """
                <!doctype html>
                <html lang="it">
                <head><meta charset="utf-8"><title>mia-app</title></head>
                <body style="font-family: sans-serif; margin: 3rem">
                  <h1>%s</h1>
                  <p>Host: <b>%s</b></p>
                  <p>Versione: <b>%s</b></p>
                </body>
                </html>
                """.formatted(escape(message), escape(hostname()), escape(version));
    }

    /** Le stesse informazioni in JSON, più da quanti secondi l'app è accesa. */
    @GET
    @Path("info")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> info() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("hostname", hostname());
        info.put("version", version);
        info.put("startedAt", STARTED_AT.toString());
        info.put("uptimeSeconds", Duration.between(STARTED_AT, Instant.now()).toSeconds());
        return info;
    }

    /** Kubernetes interroga questo indirizzo per sapere se l'app è viva. */
    @GET
    @Path("health")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }

    /** Spegne l'app dopo aver risposto. */
    @POST
    @Path("crash")
    @Produces(MediaType.APPLICATION_JSON)
    public Response crash() {
        spegniTraPoco();
        return Response.accepted(Map.of("status", "crashing")).build();
    }

    /** Saluta per nome: /saluto?nome=Mario */
    @GET
    @Path("saluto")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> saluto(@QueryParam("nome") @DefaultValue("mondo") String nome) {
        return Map.of("saluto", "Ciao " + nome + "!");
    }

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

    static String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
