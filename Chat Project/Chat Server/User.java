//IMPORTS
import java.util.*;
import java.io.*;
class User
    {
//=========================== DATA MEMBERS ========================================

    String              userName;       //this will be set later
    Vector<String>      buddyList;      //initially empty
    String              password;       //this will be set later
    ConnectionToClient  ctc;
    //boolean             isOnline;   //this will be set later
    //dont think i need this since i can figure out if its online or not if i check if the ctc is null

//====================== DEFAULT CONSTRUCTOR ===================================
    User(String userName,String password,ConnectionToClient ctc)
        {
        //construct a ctc

        this.userName = userName;
        this.buddyList = buddyList;
        this.password = password;
        this.ctc = ctc;
        buddyList = new Vector<String>();
        }
//==================== LOADING CONSTRUCTOR ==========================
    User(DataInputStream dis) throws IOException
        {
        System.out.println("in the constructor for a loaded user");
        buddyList = new Vector<String>();

        userName = dis.readUTF();
        System.out.println(userName + "");
        int numberOfBuddies;
        numberOfBuddies = dis.readInt();
        for(int x = 0;x<numberOfBuddies;x++)
            {
            System.out.println("number of loops: "+ x);
            buddyList.addElement(dis.readUTF());
            }
        password = dis.readUTF();
        }
/*===================================================================
========================== STORE USER ===============================
===================================================================*/
    void storeUser(DataOutputStream dos) throws IOException
        {
        dos.writeUTF(userName);
        dos.writeInt(buddyList.size());
        for(int x = 0;x<buddyList.size();x++)
            {
            dos.writeUTF(buddyList.elementAt(x));
            }
        dos.writeUTF(password);
        }
    }