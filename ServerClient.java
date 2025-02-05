package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerClient {
    private ServerSocket server;
    private Socket clientHandler;
    private PrintWriter out;
    private BufferedReader in;


    public void start() {
        try {
            server = new ServerSocket(8080);
            clientHandler = server.accept();
            out = new PrintWriter(clientHandler.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientHandler.getInputStream()));
            out.println("You have connected to Fredcoin");
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.toLowerCase().equals("stop")) {
                    break;
                }

                out.println("echo: " + inputLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            stop();


        }
    }
    public void stop() {
        try {
            in.close();
            out.close();
            clientHandler.close();
            server.close();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ServerClient().start();
    }

}


