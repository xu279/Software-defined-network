import java.io.*;
import java.net.*;

// Main echoclient class that connects to a server in localhost and port 5000
// After connecting, waits for userinput and sends the message to the server
// If receive 'bye' from server, client terminates 
public class echo_client {
    public static void main(String[] args) throws IOException {

        String serverHostname = new String ("127.0.0.1");

        if (args.length > 0)
           serverHostname = args[0];
        System.out.println ("Attemping to connect to host " +
		serverHostname + " on port 5000.");

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            echoSocket = new Socket(serverHostname, 5000);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                                        echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverHostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to: " + serverHostname);
            System.exit(1);
        }

	BufferedReader stdIn = new BufferedReader(
                                   new InputStreamReader(System.in));
	String userInput;

        System.out.print ("input: ");
	while ((userInput = stdIn.readLine()) != null) {
	    out.println(userInput);
            String msg = in.readLine();
            if(msg.trim().equals("bye")) {
                break;
            }
	    System.out.println("echo: " + msg);
            System.out.print ("input: ");
	}

	out.close();
	in.close();
	stdIn.close();
	echoSocket.close();
    }
}
