package co.edu.eci.arep;


import java.net.MalformedURLException;
import java.net.URL;

public class URLparser {
    public static void main(String[] args) throws MalformedURLException{
        URL site = new URL("https://ldbn.is.escuelaing.edu.co:8765/index.html?v=456ty=67#eventos");
        System.out.println("protocol:" + site.getProtocol());
        System.out.println("Authority:" + site.getAuthority());
        System.out.println("Host:" + site.getHost());
        System.out.println("port:" + site.getPort());
        System.out.println("Path:" + site.getPath());
        System.out.println("Query:" + site.getQuery());
        System.out.println("File:" + site.getFile());
        System.out.println("Ref:" + site.getRef());
    }
}