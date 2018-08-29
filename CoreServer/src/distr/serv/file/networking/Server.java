package distr.serv.file.networking;

import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable
{
    protected static final int DEFAULTPORT= -1;
    protected String host;
    protected int port;

    private ServerSocket listener;
    private boolean running;

    public Server(int port)
    {
        this.port = port;
    }

    public void run()
    {
        try
        {
            listener = new ServerSocket(port);
            while(running)
            {
                try
                {
                    Socket client = listener.accept();

                }
                catch(Exception ex)
                {
                    System.err.println("Error accepting client socket.");
                    ex.printStackTrace();
                }
            }
        }
        catch(Exception ex)
        {
            System.err.println("Error starting a server socket.");
            ex.printStackTrace();
        }

    }
}
