/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.lab.based.practical.mock.solutions;

/*
 * ----------------
 * Please import libraries
 * ----------------
*/
import java.io.*;
import java.net.*;
import java.util.logging.*;

 // Please define a class named 'ClientServer' that will contain both the server and client logic.
 // This demonstrates a simple client-server interaction within a single class.
public class ClientServer{

    // Please create a logger and call it as logger
    private static final Logger logger = Logger.getLogger(ClientServer.class.getName());

    // Please define a constant for the port number the server will listen on.
    // Using a constant makes it easy to change the port in one place.
    private static final int PORT = 1234;

    /*
     * ----------------
     * Please define the main method, the entry point of the application.
     * This method creates and starts the server and client threads.
     * ----------------
    */
    public static void main(String[] args) {

        // Please create a new thread for the server and start it.
        // The ServerSocket logic is encapsulated within the Server class.
 
        Thread serverThread = new Thread(new Server());
        serverThread.start();

        // Please create a new thread for the client and start it.
        // The client logic is encapsulated within the Client class.

        Thread clientThread = new Thread(new Client());
        clientThread.start();
        
    }


    /*
     * ----------------
     * Please define a nested class named 'Server' that implements the Runnable interface.
     * This class encapsulates the server's functionality and allows it to run in a separate thread.
     * ----------------
    */
    static class Server implements Runnable { 

        /*
         * ----------------
         * Please override the run method from the Runnable interface.
         * This method contains the main logic for the server.
         * ----------------
        */
        @Override
        public void run(){
        
            /*
             * ----------------
             * Please create a ServerSocket to listen for incoming client connections.
             * Use try-with-resources to ensure the ServerSocket is closed properly.
             * Inside the try block please please use informational logger to log "Server started on port 12345"
             * Handle potential IOExceptions during ServerSocket creation.
             * ----------------
            */
            try {
                ServerSocket serverSocket = new ServerSocket(PORT);
                logger.log(Level.INFO, "Server started on port: " + PORT);

                // Please loop indefinitely, listening for and accepting client connections.
                while (true) {
 
                    // Please accept an incoming client connection uaing serverSocket object and store it inside an object of the Socket class called clientSocket. 
                    // Please print "Client connected" plus the clientSocket object

                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket);

                    // Please create a new ClientHandler thread to handle communication with the newly connected client.
                    // Pass the client's socket to the ClientHandler. Start the handler thread.
                    
                    new Thread(new ClientHandler(clientSocket)).start();
                    }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Server error: {0}", e.getMessage());
            }
        }


        /*
         * ----------------
         * Please define a nested class named 'ClientHandler' that implements the Runnable interface.
         * This class handles the interaction with a single connected client.
         * ----------------
        */
        static class ClientHandler implements Runnable {
            private final Socket clientSocket;

            // Please define a constructor for the ClientHandler class.
            // This constructor takes the client's socket as a parameter.

            public ClientHandler(Socket clientSocket) {
                this.clientSocket = clientSocket;
            }

            // Please override the run method of the Runnable interface.
            // This method contains the logic for communicating with the client.

            @Override
            public void run(){

                // Use try-with-resources to ensure resources are closed automatically.
                // Handle potential IOExceptions.
 
                try{
                    /*
                         * ----------------
                         * Please create a BufferedReader to read text data from the client socket's input stream.
                         * BufferedReader buffers the input for efficient reading of characters, lines, and arrays.
                         * Wrap the getInputStream() (which provides raw byte stream) with an InputStreamReader
                         * to convert the byte stream into a character stream.
                         * ----------------
                        */
      
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
 
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    
                    /*
                     * ----------------
                     * Please read messages from the client in a loop and store it inside a string varibale called inputLine.
                     * Continue reading until the client sends "exit" or the connection is closed.
                     * print the inputLine plus a message like "Server received: "
                     * ----------------
                    */
                    
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("Server received: " + inputLine);
                        
                        /*
                         * --------------
                         * Please check if the client has sent the "exit" command.
                         * --------------
                        */
                        
                        if (inputLine.equalsIgnoreCase("exit")) {
                            
                            /*
                             * --------------
                             * If "exit" is received, send a "Bye" message to the client and break out of the loop.
                             * The println() method of PrintWriter writes a string followed by a newline character to the output stream.
                             * Then break the operation
                             * otherwise
                             * --------------
                            */
                            out.println("Bye");
                            break;
                        }
                        
                            /*
                             * --------------
                             * send the inputLine plus "Echo: "
                             *  
                             * --------------
                            */
                        out.println("Echo: " + inputLine);
                    }
                } catch (IOException e) {
                    System.err.println("ClientHandler error: " + e.getMessage());
                } finally {
                    /*
                    * --------------
                    * create a try block and close the clientSocket and catch io exception                
                    * --------------
                    */
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }     
        }
    }    
                



    /*
     * ----------------
     * Please define a nested class named 'Client' that implements the Runnable interface.
     * This class encapsulates the client's functionality and allows it to run in a separate thread.
     * ----------------
    */
    static class Client implements Runnable {
    


        /*
         * ----------------
         * Please override the run method from the Runnable interface.
         * This method contains the main logic for the client.
         * ----------------
        */
        @Override
        public void run(){   
            try {
                    /*
                     * --------------------
                     * Please create a Socket object, establishing a connection to the server at the specified host 
                       ("localhost") and port (PORT).
                     * This initiates the connection to the server's ServerSocket.
                     * --------------------
                    */
                
                Socket socket = new Socket("localhost", PORT);
                
                    /*
                     * ----------------
                     * Please create a PrintWriter to send formatted text data to the server through the socket's output stream.
                     * PrintWriter simplifies sending text-based data.
                     * 'true' for auto-flush: ensures data is sent immediately without buffering delays.
                     * Wrap socket.getOutputStream() to provide character-based output.
                     * ----------------
                    */
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                
                    /*
                     * ----------------
                     * Please Create a BufferedReader to read text data from the server's responses through the socket's input stream.
                     * BufferedReader provides efficient reading of characters, lines, and arrays by buffering the input.
                     * Wrap socket.getInputStream() with InputStreamReader to convert bytes to characters.
                     * ----------------
                    */
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                    /*
                     * -------------------
                     * Please create a BufferedReader to read text input from the console (System.in).
                     * This allows the client to read user input.
                     * Wrap System.in (which is an InputStream) with an InputStreamReader to handle character encoding.
                     * -------------------
                    */
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            
                System.out.println("Connected to server. Type messages below:");
                
                /*
                 * ----------------
                 * Please read input from the console  in a loop.
                 * ----------------
                */
                String userInput;
                while ((userInput = stdIn.readLine()) != null) {
                    
                    /*
                     * ----------------
                     * Please send the user's input to the server using the PrintWriter.
                     * ----------------
                    */
                    out.println(userInput);

                    /*
                     * ----------------
                     * Please read the server's response using the BufferedReader and store it inside a string variable called receivedMessage .
                     * check if the receivededMessage is null
                     * Then print "Server closed the connection."
                     * exit the loop
                     * ----------------
                    */
                    String receivedMessage = in.readLine();
                    if (receivedMessage == null) {
                        System.out.println("Server closed the connection.");
                        break;
                    }
                    
                    /*
                     * ---------------
                     * Print Server Response 
                     * ---------------
                    */
                    System.out.println("Server response: " + receivedMessage);
                    
                    /*
                     * ----------------
                     * Please check if the user typed "exit". If so, exit the loop.
                     * ----------------
                    */
                    if (userInput.equalsIgnoreCase("exit")) {
                        break;
                    }
                }
            } catch (IOException e) {
                System.err.println("Client error: " + e.getMessage());
            }          
        }
    }
}
                
