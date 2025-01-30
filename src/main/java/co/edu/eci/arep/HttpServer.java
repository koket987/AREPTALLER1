package co.edu.eci.arep;

import java.net.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class HttpServer {
    private static List<String> users = new ArrayList<>(); // Lista de usuarios

    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = new ServerSocket(35000);
        System.out.println("Servidor iniciado en el puerto 35000...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(() -> handleClient(clientSocket)).start(); // Manejo concurrente de clientes
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream dataOut = clientSocket.getOutputStream();
        ) {
            String inputLine;
            String method = "";
            String file = "";
            int contentLength = 0;

            while ((inputLine = in.readLine()) != null && !inputLine.isEmpty()) {
                System.out.println("Received: " + inputLine);

                if (inputLine.startsWith("GET") || inputLine.startsWith("POST")) {
                    String[] requestParts = inputLine.split(" ");
                    method = requestParts[0];
                    file = requestParts[1];
                }
                if (inputLine.toLowerCase().startsWith("content-length:")) {
                    contentLength = Integer.parseInt(inputLine.split(":")[1].trim());
                }
            }

            URI resourceUri = new URI(file);
            System.out.println("URI: " + resourceUri);

            if (method.equals("GET") && resourceUri.getPath().startsWith("/app/hello")) {
                String outputLine = helloRestService(resourceUri.getQuery());
                sendResponse(out, dataOut, outputLine);
            } else if (method.equals("POST") && resourceUri.getPath().startsWith("/app/hello")) {
                StringBuilder requestBody = new StringBuilder();
                for (int i = 0; i < contentLength; i++) {
                    requestBody.append((char) in.read());
                }

                System.out.println("POST Body: " + requestBody);
                String outputLine = postRestService(requestBody.toString());
                sendResponse(out, dataOut, outputLine);
            } else {
                serveStaticFile(resourceUri.getPath(), out, dataOut);
            }

            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String helloRestService(String query) {
        System.out.println("Query: " + query);
        String name = query != null && query.contains("name=") ? query.split("=")[1] : "Unknown";
        String message = users.contains(name) ? "Hola, " + name + ". ¡Bienvenido de nuevo!" : "El usuario " + name + " no está registrado.";
        return jsonResponse(message);
    }

    static String postRestService(String body) {
        System.out.println("POST Body: " + body);
        String name = body.contains("name=") ? body.split("=")[1] : "Unknown";
        if (!users.contains(name)) {
            users.add(name);
        }
        return jsonResponse("Usuario " + name + " registrado correctamente.");
    }

    private static void serveStaticFile(String path, PrintWriter out, OutputStream dataOut) throws IOException {
        if (path.equals("/")) {
            path = "/index.html";
        }

        File file = new File("www" + path);
        if (file.exists() && !file.isDirectory()) {
            String contentType = Files.probeContentType(file.toPath());
            byte[] fileData = Files.readAllBytes(file.toPath());

            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: " + contentType);
            out.println("Content-Length: " + fileData.length);
            out.println();
            out.flush();

            dataOut.write(fileData, 0, fileData.length);
            dataOut.flush();
        } else {
            send404(out);
        }
    }

    private static void send404(PrintWriter out) {
        String response = "HTTP/1.1 404 Not Found\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<html><body><h1>404 Not Found</h1></body></html>";
        out.println(response);
        out.flush();
    }

    private static void sendResponse(PrintWriter out, OutputStream dataOut, String response) throws IOException {
        out.println(response);
        out.flush();
        dataOut.flush();
    }

    private static String jsonResponse(String message) {
        String jsonResponse = "{\"message\":\"" + message + "\"}";
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: application/json\r\n"
                + "Content-Length: " + jsonResponse.length() + "\r\n"
                + "\r\n"
                + jsonResponse;
    }
}
