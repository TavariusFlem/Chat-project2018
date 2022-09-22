//IMPORTS
import java.lang.*;
import java.net.*;
import java.io.*;
import java.util.*;

class ConnectionToClient
        implements Runnable
    {
//============================ DATA MEMBERS =======================================
    Talker                  talker;
    String                  message;
    String[]                messageParts;
    ServerInfoTable         serverInfoTable;
    Thread                  t;
    String                  username;
    boolean                 isLoggedIn;
                            //REMEMBER THAT THIS IS HERE
//--------------------------
    ConnectionToClient(Socket normalSocket,ServerInfoTable serverInfoTable)
        {
        isLoggedIn = true;
        this.serverInfoTable = serverInfoTable;
        talker = new Talker(normalSocket);
        t = new Thread(this);
        t.start();
        }

/*=======================================================================================
================================= RUN ===================================================
=======================================================================================*/
    public void run()
        {
        try
            {
            while(isLoggedIn)
                {
                message = talker.receive();
                messageParts = message.split(" ");

                if(messageParts[0].equals("LOGIN"))
                    {
                    if(!serverInfoTable.containsKey(messageParts[1]))
                        {
                        talker.send("DENIED");
                        t.interrupt();
                        }
                    else if(!(serverInfoTable.get(messageParts[1]).password.equals(messageParts[2])))
                        {
                        talker.send("DENIED");
                        t.interrupt();
                        }
                    else
                        {

                        System.out.println("im in the else for the login");
                        username = messageParts[1];
                        if(serverInfoTable.get(username).ctc != null)
                            {
                            serverInfoTable.get(username).ctc.interruptThread();
                            }
                        serverInfoTable.get(username).ctc = this;
                        talker.send("LOGIN_ACCEPTED " + messageParts[1]);
                        String buddysName;
                        //serverInfoTable.get(username).ctc.talker.send("CLEAR_BUDDY_LIST");
                        for(int x=0;x<serverInfoTable.get(username).buddyList.size();x++)
                            {
                            buddysName = serverInfoTable.get(username).buddyList.elementAt(x);

                            if(serverInfoTable.get(buddysName).ctc != null)
                                {
                                serverInfoTable.get(buddysName).ctc.talker.send("SET_ONLINE "+username);
                                serverInfoTable.get(username).ctc.talker.send("ADD_BUDDY "+ buddysName);
                                serverInfoTable.get(username).ctc.talker.send("SET_ONLINE " + buddysName);
                                }
                            else
                                {
                                serverInfoTable.get(username).ctc.talker.send("ADD_BUDDY "+ buddysName);
                                serverInfoTable.get(username).ctc.talker.send("SET_OFFLINE "+ buddysName);
                                }
                            }
                        while(true)
                            {
                            message = talker.receive();
                            //check all of the cases
                            messageParts = message.split(" ");

                            if(messageParts[0].equals("BUDDY_REQUEST_SEND"))
                                {
                                handleSendingFriendRequest(messageParts[1]);
                                }
                            else if(messageParts[0].equals("BUDDY_REQUEST_CONFIRMATION"))
                                {
                                handleFriendConfirmation(messageParts[1]);
                                serverInfoTable.saveServerInfo();
                                }
                            else if(messageParts[0].equals("CHAT_REQUEST_SEND"))
                                {
                                handleSendingChatRequest(messageParts[1]);
                                }
                            else if(messageParts[0].equals("CHAT_REQUEST_CONFIRMATION"))
                                {
                                handleChatConfirmation(messageParts[1]);
                                }
                            else if(message.startsWith("MESSAGE_SEND"))
                                {
                                message = message.substring(messageParts[0].length()+1 + messageParts[1].length()+1,message.length());
                                handleSendingMessage(messageParts[1],message);
                                System.out.println("CTC" + message );
                                }
                            }
                        }
                    }
                else if(messageParts[0].equals("REGISTER"))
                    {
                    if(serverInfoTable.containsKey(messageParts[1]))
                        {
                        talker.send("DENIED");
                        t.interrupt();
                        }
                     else
                        {
                        username = messageParts[1];

                        User userToBeRegistered;
                        userToBeRegistered = new User(messageParts[1],messageParts[2],this);
                        serverInfoTable.put(messageParts[1],userToBeRegistered);
                        talker.send("LOGIN_ACCEPTED " + messageParts[1]);
                        serverInfoTable.saveServerInfo();
                        while(true)
                            {
                            message = talker.receive();
                            //check all of the cases
                            messageParts = message.split(" ");

                            if(messageParts[0].equals("BUDDY_REQUEST_SEND"))
                                {
                                handleSendingFriendRequest(messageParts[1]);
                                }
                            else if(messageParts[0].equals("BUDDY_REQUEST_CONFIRMATION"))
                                {
                                handleFriendConfirmation(messageParts[1]);
                                serverInfoTable.saveServerInfo();
                                }
                            else if(messageParts[0].equals("CHAT_REQUEST_SEND"))
                                {
                                handleSendingChatRequest(messageParts[1]);
                                }
                            else if(messageParts[0].equals("CHAT_REQUEST_CONFIRMATION"))
                                {
                                handleChatConfirmation(messageParts[1]);
                                }
                            else if(message.startsWith("MESSAGE_SEND"))
                                {
                                message = message.substring(messageParts[0].length()+1 + messageParts[1].length()+1,message.length());
                                handleSendingMessage(messageParts[1],message);
                                System.out.println("CTC" + message );
                                }
                            }
                        }
                    }
                }
            }
        catch(Exception e)
            {
            e.printStackTrace();
            try
                {
                System.out.println("EXCEPTION CAUGHT");
                System.out.println("NULL THIS CTC and tell all buddies they the user is offline");
                for(int x = 0; x<serverInfoTable.get(username).buddyList.size();x++)
                    {
                    String usernameOfBuddy = serverInfoTable.get(username).buddyList.elementAt(x);

                    if(serverInfoTable.get(usernameOfBuddy).ctc != null)
                        {
                        serverInfoTable.get(usernameOfBuddy).ctc.talker.send("SET_OFFLINE "+ username);
                        }
                    }
                serverInfoTable.get(username).ctc = null;

                //e.printStackTrace();
                }
            catch(IOException ioe)
                {
                System.out.println("caught an io exception!");
                }
            }
        }//end of RUN
/*=======================================================================================
=========================== HANDLE SENDING FRIEND REQUEST ===============================
=======================================================================================*/
    void handleSendingFriendRequest(String recipient) throws IOException
        {
        //map the whole structure out

        if(!serverInfoTable.containsKey(recipient))
            {
            talker.send("ERROR user does not exist");
            System.out.println("user doesnt exist");
            }
        else if(serverInfoTable.get(recipient).ctc == null)
            {
            talker.send("ERROR user is not online");
            System.out.println("user isnt online");
            }

        else
            {
            System.out.println("handleSendingFriendRequest");
            serverInfoTable.get(recipient).ctc.talker.send("BUDDY_REQUEST_RECIEVE " + username);
            }
        }
/*=======================================================================================
=============================HANDLE FRIEND CONFIRMATION =================================
=======================================================================================*/
    void handleFriendConfirmation(String sender)throws IOException
        {
        serverInfoTable.get(username).buddyList.addElement(sender);

        serverInfoTable.get(sender).buddyList.addElement(username);
        if(serverInfoTable.get(username).ctc !=null)
            {
            serverInfoTable.get(sender).ctc.talker.send("BUDDY_REQUEST_UPDATE " + username);
            }



        //saveHashtable();

        }
/*=======================================================================================
============================= HANDLE SENDING CHAT REQUEST ===============================
=======================================================================================*/
    void handleSendingChatRequest(String recipient) throws IOException
        {
        serverInfoTable.get(recipient).ctc.talker.send("CHAT_REQUEST_RECIEVE "+ username);
        //basically bouncing it to the other user
        }
/*=======================================================================================
============================= HANDLE CHAT CONFIRMATION ==================================
=======================================================================================*/
    void handleChatConfirmation(String sender) throws IOException
        {
        serverInfoTable.get(sender).ctc.talker.send("CHAT_REQUEST_UPDATE " + username);

        }
/*=======================================================================================
============================= HANDLE SENDING MESSAGE ====================================
=======================================================================================*/
    void handleSendingMessage(String recipient,String messageToSend) throws IOException
        {
        if(serverInfoTable.get(recipient).ctc!= null)
            {
            serverInfoTable.get(recipient).ctc.talker.send("MESSAGE_RECIEVE " + username + " " + messageToSend);
            }
        else
            {
            talker.send("CHAT_CLOSE " + recipient);
            //or maybe alert them????
            }
        }
/*=======================================================================================
============================= INTERRUPT THREAD ==========================================
=======================================================================================*/
    void interruptThread()
        {
        System.out.println("INTERRUPTING THREAD");
        serverInfoTable.get(username).ctc = null;
        isLoggedIn = false;
        t.interrupt();
        }

//-------------------
    }//end of class
