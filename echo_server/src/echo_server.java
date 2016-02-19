import java.io.*;
import java.net.*;
import java.lang.Thread;
import java.util.Timer;
import java.util.TimerTask;

// Main EchoServer class listens on port 5000 - accepts a client connection 
// Spawns a ClientEchoHandler thread to handle the client echo messages
// Schedules a timer immediately for 20 sec inactivity
public class echo_server {
  public static Timer timer;
  public static void main (String[] args) {
    try {
      ServerSocket server = new ServerSocket(5000);
      while (true) {
        Socket client = server.accept();
        System.out.println("Thread:### " + Thread.currentThread().getName() + " ###received client connection..");
        timer = new Timer("Timer");
        ClientEchoHandler handler = new ClientEchoHandler(client, timer);
        handler.start();
        timer.schedule(new MyTimerTask(client), 20000); 
      }
    }
    catch (Exception e) {
      System.err.println("Exception caught:" + e);
    }
  }
}


// The timer task class that is executed by the Timer thread
// Send 'bye' to close the client connection
class MyTimerTask extends TimerTask {
    Socket client;
    MyTimerTask(Socket passedClient) {
        this.client = passedClient;
    }
    public void run() {
      System.out.println("Thread:### " + Thread.currentThread().getName() + " ###");
      System.out.println("Client inactive for 20 seconds.. Terminating server and closing client..");
      try {
          PrintWriter writer = new PrintWriter(client.getOutputStream(), true); 
          writer.println("bye");
          System.exit(0); 
      } catch (Exception e) {
          System.err.println("Exception in socket....");
      }
    }
}

// ClientEchoHandler Thread that echoes the messages sent by the client
// It resets the timer in the timer thread
class ClientEchoHandler extends Thread {
  Socket client;
  Timer timer;
  ClientEchoHandler (Socket client, Timer timer) {
    this.client = client;
    this.timer = timer;
  }

  public void run () {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
      PrintWriter writer = new PrintWriter(client.getOutputStream(), true);

      while (true) {
        String line = reader.readLine();
        writer.println("[echo] " + line);
        System.out.println("Thread: ### " + Thread.currentThread().getName() + " ### received echo.. resetting timer..");
        timer.cancel();
        timer = new Timer("Timer");
        timer.schedule(new MyTimerTask(client), 20000);
      }
    }
    catch (Exception e) {
      System.err.println("Exception caught: client disconnected.");
    }
    finally {
      try { client.close(); }
      catch (Exception e ){ ; }
    }
  }
}