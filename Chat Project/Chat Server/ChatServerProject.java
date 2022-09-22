//IMPORTS
import java.net.*;
import java.util.*;
import java.io.*;

public class ChatServerProject
    {
    public static void main(String[] args)
        {
        new ChatServer();
        }
    }
class ChatServer
    {
//============================== DATA MEMBERS ======================================
    ChatServer()
        {
        ServerSocket            serverSocket;
        Socket                  normalSocket;
        ConnectionToClient      ctc;
        Talker                  talker;
        ServerInfoTable         serverInfoTable;
        File                    serverInfoFile;

        serverInfoFile = new File("Server_Information.txt");
//-----------------------------------------
        try
            {
            if(serverInfoFile.exists())
                {
                FileInputStream fis;
                DataInputStream dis;

                fis = new FileInputStream("Server_Information.txt");
                dis = new DataInputStream(fis);
                serverInfoTable = new ServerInfoTable(dis);
                }
            else
                {
                serverInfoTable = new ServerInfoTable();
                }

            System.out.println("THIS IS THE SERVER");
            serverSocket = new ServerSocket(6789);

            while(true)
                {
                normalSocket = serverSocket.accept();

                ctc = new ConnectionToClient(normalSocket,serverInfoTable);
                }

            }
        catch(IOException ioe)
            {
            ioe.printStackTrace();
            }
        }
    }//end of class