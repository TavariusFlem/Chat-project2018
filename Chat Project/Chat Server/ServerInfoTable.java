//IMPORTS
import java.io.*;
import java.util.*;

class ServerInfoTable extends Hashtable<String,User>
    {
    ServerInfoTable()
        {
        }
    ServerInfoTable(DataInputStream dis)
        {
        User        userToBeLoadedIn;
        try
            {
            //this is the issue
            int size = dis.readInt();
            for(int n = 0; n< size;n++)
                {
                userToBeLoadedIn = new User(dis);
                this.put(userToBeLoadedIn.userName,userToBeLoadedIn);
                }
            }
        catch(IOException ioe)
            {
            ioe.printStackTrace();
            System.out.println("Caught an io exception in server constructor");
            }
        }
/*=====================================================================================
=======================================================================================
=====================================================================================*/
    void saveServerInfo()
        {
        Enumeration<User> enumeration;
        DataOutputStream dos;
        try
            {
            dos = new DataOutputStream(new FileOutputStream("Server_Information.txt"));
            enumeration = this.elements();
            dos.writeInt(size());
            while(enumeration.hasMoreElements())
                {
                enumeration.nextElement().storeUser(dos);
                }
            }
        catch(IOException ioe1)
            {
            System.out.println("Caught an io exception when storing");
            }
        }//end of saveServerInfo
    }//end of class