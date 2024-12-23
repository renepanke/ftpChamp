package io.github.renepanke.ftpchamp;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Server server = new Server();
        try {
            server.start();
        }
        finally {
            server.stop();
        }
    }
}
