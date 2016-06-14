package asrs.Serial;

import asrs.Controllers.MainController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by Eldin on 5/14/2015.
 */
public class Serial{

    //IO between server and client
    private Socket sock;
    private PrintStream toServer;
    private BufferedReader fromServer;
    private BufferedReader fromServerProces;

    //status booleans
    private boolean connected = false;
    private boolean sanyaUtilized = false;
    private boolean ericaUtilized = false;
    private boolean sMessageReceived = false;
    private boolean eMessageReceived = false;

    //global threads
    private Thread readServer;
    private Thread checkServer;
    private Thread runClient;

    //server process
    private Process server;

    private String[] cmd;

    //IMPORTANT: the class name YourClass should be renamed to the class where you initiate this class!
    //IMPORTANT: this class cannot run in  (Main.java)!
    //IMPORTANT: both functions on line 111 & 116 are located in YourClass class, you could see them as event triggers
    public Serial(final MainController yourClass){

        //server setup, PYTHON 3.4 IS NEEDED!

        //server connection and read/write
        runClient = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try{

                        System.out.println("Connected to server!");
                        sock = new Socket("localhost", 12366);
                        connected = true;

                        try {
                            toServer = new PrintStream(sock.getOutputStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {
                            fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                        } catch (IOException e) {
                            System.out.println("COULD NOT CREATE BUFFERED READER: " + e.toString());
                        }

                        readServer.start();

                        break;

                    } catch (Exception networkEx){
                        System.out.println("Could not connect TEST! : " + networkEx.toString());

                        sleep(50);
                    }
                }
            }
        });

        readServer = new Thread(new Runnable() {
            @Override
            public void run() {

                while(true){

                    if(connected){
                        try {

                            String received = fromServer.readLine();
                            //System.out.println("SERVER: " + received);
                            if(received.contains("01|")){
                                if(received.contains("SANYA: READY.")){
                                sanyaUtilized = true;
                                }
                                if(received.contains("ERICA: INITIALIZING")){
                                    ericaUtilized = true;
                                }
                                if(received.contains("SANYA:")){
                                    sMessageReceived = true;
                                    yourClass.messageFromSanya(received);
                                }
                                if(received.contains("ERICA:")){
                                    eMessageReceived = true;
                                    yourClass.messageFromErica(received);
                                }
                            } else if (received.contains("02|")) {
                                if(received.contains("CONNECTED TO SANYA")){
                                    yourClass.sanyaStatus("CONNECTED TO SANYA");
                                }
                                if(received.contains("CONNECTED TO ERICA")){
                                    yourClass.ericaStatus("CONNECTED TO ERICA");
                                }
                                if(received.contains("ERROR: NOT CONNECTED TO ERICA")){
                                    yourClass.ericaStatus(received);
                                }
                                if(received.contains("ERROR: NOT CONNECTED TO SANYA")){
                                    yourClass.sanyaStatus(received);
                                }
                            }

                        } catch (IOException e) {
                            System.out.println("Could not retreive message from server: " + e.toString());
                            break;
                        }
                    } else {
                        System.out.println("Not Connected");
                        break;
                    }

                }

                System.out.println("Reconnecting:");
                runClient.start();
            }
        });

        //Needed to keep server alive, somehow when running within a runtime it stops sending serial data after 300 lines of data send
        //this somehow keeps it alive.
        checkServer = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    fromServerProces = new BufferedReader(new InputStreamReader(server.getInputStream()));
                    try {
                        String log = fromServerProces.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    sleep(100);
                }
            }
        });

        try{
            if(server.isAlive()){
                runClient.start();
            } else {
                System.out.println("Server is not running!");
            }
        } catch (NullPointerException e){
            System.out.println("Could not check if server is running: " + e.toString());
            System.out.println("Assuming server is being runned seperatly!");
            runClient.start();
        }

    }


    //Sends a message to the server, not meant for communicating with arduino's!
    public void sendMessage(String message) {
        try{
            toServer.println(message);
            toServer.flush();
            System.out.println("Message send: " + message);
        } catch (Exception notSend) {
            System.out.println("Your message cannot be send to server, either you are not connected or: " + notSend.toString());
        }
    }

    //Sends message to Sanya(Defined Arduino)
    public void sendMessageToSanya(String message){
        System.out.println("Trying to send message to sanya: " + message);
        while(!sanyaUtilized || !sMessageReceived){
            sleep(10);
        }

        try{
            toServer.println("SayToSanya: " + message);
            toServer.flush();
            sMessageReceived = false;
            System.out.println("Message send: " + message);
        } catch (Exception notSend) {
            System.out.println("Your message cannot be send to server, either you are not connected or: " + notSend.toString());
        }

       sleep(1);
    }

    public void runServer(){

        //server setup, PYTHON 3.4 IS NEEDED!
        String pythonScriptPath = System.getProperty("user.dir") + "\\server.py";
        System.out.println(pythonScriptPath);

        //determines os, python launches in a different way on mac
        String osNameMatch = System.getProperty("os.name");

        if(osNameMatch.contains("mac os") || osNameMatch.contains("macos") || osNameMatch.contains("darwin")){
            cmd = new String [] {"open", "-a", "python ", pythonScriptPath};
        } else {
            cmd = new String[2];
            cmd[0] = "C:\\Python34\\python.exe";
            cmd[1] = pythonScriptPath;
        }

        //create runtime to execute external command
        Runtime rt = Runtime.getRuntime();
        try {
            server = rt.exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread closePython = new Thread(new Runnable() {
            @Override
            public void run() {
                server.destroyForcibly();
            }
        });

        Runtime.getRuntime().addShutdownHook(closePython);


        checkServer.start();
    }

    //Sends message to Erica(Defined Arduino)
    public void sendMessageToErica(String message){
        System.out.println("Trying to send message to erica: " + message);
        while(!ericaUtilized || !eMessageReceived){
            sleep(10);
        }

        try{
            toServer.println("SayToErica: " + message);
            toServer.flush();
            System.out.println("Message send: " + message);
            eMessageReceived = false;
        } catch (Exception notSend) {
            System.out.println("Your message cannot be send to server, either you are not connected or: " + notSend.toString());
        }

        sleep(1);
    }

    public boolean isConnected() {
        return connected;
    }

    public void  setupArduino(String arduino, String com, int baud){
        if(arduino == "Sanya"){
            while(true){
                if(connected){
                    sendMessage("SANYASETUP-" + com + "-" + baud);
                    break;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            while(true){
                if(connected){
                    sendMessage("ERICASETUP-" + com + "-" + baud);
                    break;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sleep(int delay){
        try {

            //System.out.println("SLEEP:  " + delay );
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("CANNOT SLEEP: " + e.toString());
        }
    }


}
