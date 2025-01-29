package co.edu.eci.arep;

import java.net.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class HttpServer {
    private static List<String> users = new ArrayList<>(); // Lista de usuarios

    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;

            boolean isFirstLine = true;
            String file = "";
            String method = "";

            while ((inputLine = in.readLine()) != null) {
                if (isFirstLine) {
                    String[] requestParts = inputLine.split(" ");
                    method = requestParts[0];
                    file = requestParts[1];
                    isFirstLine = false;
                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }

            URI resourceuri = new URI(file);
            System.out.println("URI: " + resourceuri);

            if (method.equals("GET") && resourceuri.getPath().startsWith("/app/hello")) {
                outputLine = helloRestService(resourceuri.getPath(), resourceuri.getQuery());
                out.println(outputLine);
            } else if (method.equals("POST") && resourceuri.getPath().startsWith("/app/hello")) {
                StringBuilder requestBody = new StringBuilder();
                while (in.ready()) {
                    requestBody.append((char) in.read());
                }
                System.out.println("POST Body: " + requestBody);

                outputLine = postRestService(resourceuri.getPath(), requestBody.toString());
                out.println(outputLine);
            } else {
                serveStaticFile(resourceuri.getPath(), out, clientSocket.getOutputStream());
            }

            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    private static String helloRestService(String path, String query) {
        System.out.println("Query: " + query);
        String name = query != null && query.contains("name=") ? query.split("=")[1] : "Unknown";
        String message = users.contains(name) ? "El usuario " + name + " existe." : "El usuario " + name + " no existe.";
        String jsonResponse = "{\"message\":\"" + message + "\"}";
        String response = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: application/json\r\n"
                + "Content-Length: " + jsonResponse.length() + "\r\n"
                + "\r\n"
                + jsonResponse;
        return response;
    }

    private static String postRestService(String path, String body) {
        System.out.println("POST Body: " + body);
        String name = body.contains("name=") ? body.split("=")[1] : "Unknown";
        if (!users.contains(name)) {
            users.add(name); // Agrega el usuario si no existe
        }
        String jsonResponse = "{\"message\":\"Usuario " + name + " registrado.\"}";
        String response = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: application/json\r\n"
                + "Content-Length: " + jsonResponse.length() + "\r\n"
                + "\r\n"
                + jsonResponse;
        return response;
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
    }
}