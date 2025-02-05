package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerClient {
    private ServerSocket server;
    private Socket clientHandler;
    private PrintWriter out;
    private BufferedReader in;
    private String postMessage = "";
    private String tempPost = "";

    public void start(int port) {
        try {
            server = new ServerSocket(port);
            clientHandler = server.accept();
            out = new PrintWriter(clientHandler.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientHandler.getInputStream()));
            out.println("You have connected to Fredcoin");
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.toLowerCase().equals("stop")) {
                    break;
                }

                //out.println("echo: " + inputLine);
                validateRequest(inputLine);
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
        new ServerClient().start(8080);
    }

    private void validateRequest(String input) {
        if (input.contains("POST /echo")) {
            out.println(postEcho(input.replace("POST /echo", "")));
        }
        else if (input.contains("GET /hello")) {
            out.println(getHello());
        }
        else if (input.contains("GET /echo")) {
            out.println(getEcho());
        }
        else if (input.contains("GET /index.html")) {
            out.println(getIndexHtml());
        }
        else if (input.contains("POST /")) {
            out.println(post(input));
        }
        else if (input.contains("GET /")) {
            if (input.replace("GET /", "")
                    .equals(tempPost.replace("POST /", ""))) {
                out.println(getTempPost());
            }
            else {
                out.println(404);
            }
        }
        else {
            out.println(404);
        }
    }

    private String getHello() {        
        return metaData() + "\r\n" + "Hello, World!\r\n";
    }

    private String getIndexHtml() {
        return metaData() + "\r\n" + """
                \r\n<!doctype html>
                \r\n<html>
                \r\n<head>
                \r\n    <title>Example Domain</title>
                \r\n
                \r\n    <meta charset="utf-8" />
                \r\n    <meta http-equiv="Content-type" content="text/html; charset=utf-8" />
                \r\n    <meta name="viewport" content="width=device-width, initial-scale=1" />
                \r\n    <style type="text/css">
                \r\n    body {
                \r\n        background-color: #f0f0f2;
                \r\n        margin: 0;
                \r\n        padding: 0;
                \r\n        font-family: -apple-system, system-ui, BlinkMacSystemFont, "Segoe UI", "Open Sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
                \r\n
                \r\n    }
                \r\n    div {
                \r\n        width: 600px;
                \r\n        margin: 5em auto;
                \r\n        padding: 2em;
                \r\n        background-color: #fdfdff;
                \r\n        border-radius: 0.5em;
                \r\n        box-shadow: 2px 3px 7px 2px rgba(0,0,0,0.02);
                \r\n    }
                \r\n    a:link, a:visited {
                \r\n        color: #38488f;
                \r\n        text-decoration: none;
                \r\n    }
                \r\n    @media (max-width: 700px) {
                \r\n        div {
                \r\n            margin: 0 auto;
                \r\n            width: auto;
                \r\n        }
                \r\n    }
                \r\n    </style>
                \r\n</head>
                \r\n
                \r\n<body>
                \r\n<div>
                \r\n    <h1>Example Domain</h1>
                \r\n    <p>This domain is for use in illustrative examples in documents. You may use this
                \r\n    domain in literature without prior coordination or asking for permission.</p>
                \r\n    <p><a href="https://www.iana.org/domains/example">More information...</a></p>
                \r\n</div>
                \r\n</body>
                \r\n</html>
                """;
    }

    private String getEcho() {
        return metaData() + "\r\n" + postMessage;
    }

    private String getTempPost() {
        return metaData() + "\r\n" + """
                <!doctype html>\r\n
                <html>\r\n
                <head>\r\n
                </head>\r\n
                <body>\r\n
                <div>%s</div>\r\n
                </body>\r\n
                </html>\r\n
                """.formatted(tempPost.replace("POST /", ""));
    }

    private String post(String input) {
        this.tempPost = input;

        return metaData();
    }

    private String postEcho(String input) {
        this.postMessage = input;

        return metaData();
    }
    
    private String metaData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
        String data = "HTTP/1.1 200 OK\r\n";
        data += "Date: " + LocalDateTime.now().format(formatter) + "\r\n";
        data += "Server: Fredcoin\r\n";
        data += "Content-Type: text/html; charset=UTF-8\r\n";
        data += "Content-Length: 12345\r\n";
        data += "\r\n";
        
        return data;
    }
}


