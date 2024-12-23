package io.github.renepanke.ftpchamp;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
//        System.setProperty("javax.net.debug", "all");
        Server server = new Server();
        try {
            server.start();
        }
        finally {
            server.stop();
        }
    }
}
