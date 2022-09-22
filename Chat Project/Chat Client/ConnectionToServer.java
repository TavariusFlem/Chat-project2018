//IMPORTS
import java.lang.*;
import java.io.*;
import javax.swing.*;

class ConnectionToServer
                        implements Runnable
    {
//=============== DATA MEMBERS ============================
    Talker      talker;
    Thread      t;
    String      message;
    ChatClient  client;
    String[]    messageParts;
    //boolean     isLoggedIn;
//---------------------
    ConnectionToServer(int portNumber,String ip,String message,ChatClient client)
        {
        //isLoggedIn = true;
        this.client = client;
        this.message = message;
        talker = new Talker(portNumber,ip);

        t = new Thread(this);
        t.start();

        }
/*=================================================================================
================================ RUN ==============================================
=================================================================================*/
    public void run()
        {
        try
            {
            talker.send(message);
            message = talker.receive();
            if(message.equals("DENIED"))
                {
                SwingUtilities.invokeLater(
                    new Runnable()
                        {
                        public void run()
                            {
                            JOptionPane.showMessageDialog(null,"Error loging in or registering","Error",JOptionPane.ERROR_MESSAGE);
                            }//end of run
                        }//end of runnable
                        );
                t.interrupt();
                }
            else
                {
                client.loginRegisterDialog.setVisible(false);
                messageParts = message.split(" ");
                client.username = messageParts[1];
                while(true)
                    {
                    message = talker.receive();
                    messageParts = message.split(" ");
                    if(messageParts[0].equals("BUDDY_REQUEST_RECIEVE"))
                        {
                        handleRecievingFriendRequest(messageParts[1]);
                        }
                    else if(messageParts[0].equals("BUDDY_REQUEST_UPDATE"))
                        {
                        System.out.println("JERE");
                        handleFriendRequestUpdate(messageParts[1]);
                        }
                    else if(message.startsWith("ERROR"))
                        {
                        handleErrorMessage(message.substring(5,message.length()));
                        }
                    else if(messageParts[0].equals("CHAT_REQUEST_RECIEVE"))
                        {
                        handleRecievingChatRequest(messageParts[1]);
                        }
                    else if(messageParts[0].equals("CHAT_REQUEST_UPDATE"))
                        {
                        handleChatRequestUpdate(messageParts[1]);
                        }
                    else if(messageParts[0].equals("CHAT_CLOSE"))
                        {
                        handleChatClose(messageParts[1]);
                        }
                    else if(message.startsWith("MESSAGE_RECIEVE"))
                        {
                        message = message.substring(messageParts[0].length()+1 + messageParts[1].length()+1,message.length());
                        handleRecievingMessage(messageParts[1],message);
                        System.out.println("CTS" + message);
                        }
                    else if(messageParts[0].equals("SET_OFFLINE"))
                        {
                        handleSetOffline(messageParts[1]);
                        }
                    else if(messageParts[0].equals("SET_ONLINE"))
                        {
                        handleSetOnline(messageParts[1]);
                        }
                    else if(messageParts[0].equals("ADD_BUDDY"))
                        {
                        handleAddBuddy(messageParts[1]);
                        }
                    }
                }
            }
        catch(IOException ioe)
            {
            SwingUtilities.invokeLater(
                new Runnable()
                    {
                    public void run()
                        {
                        JOptionPane.showMessageDialog(null,"Lost connection to the server try logging in again","Error",JOptionPane.ERROR_MESSAGE);
                        client.dispose();
                        }//end of run
                    }//end of runnable
                    );
            }
        }
/*=================================================================================
================================ HANDLE RECIEVING FRIEND REQUESTS =================
=================================================================================*/
    void handleRecievingFriendRequest(String sender) throws IOException
        {
        SwingUtilities.invokeLater(
            new Runnable()
                {
                public void run()
                    {
                    try
                        {
                        String temp;
                        temp = "Would you like to be friends with " + sender + "?";
                        int result = JOptionPane.showConfirmDialog(null, temp ,"Friend Request",JOptionPane.YES_NO_CANCEL_OPTION);

                        if(result == JOptionPane.YES_OPTION)
                            {
                            talker.send("BUDDY_REQUEST_CONFIRMATION " + sender);
                            Buddy tempBuddy;
                            tempBuddy = new Buddy(sender,true);
                            client.dlm.addElement(tempBuddy);
                            }
                        }
                    catch(IOException ioe)
                        {
                        System.out.println("caught an io exception");
                        }
                    }//end of run
                }//end of runnable
                );
        }
/*=================================================================================
================================ HANDLE FRIEND REQUEST UPDATE =====================
=================================================================================*/
    void handleFriendRequestUpdate(String recipient)
        {
        Buddy tempBuddy;
        tempBuddy = new Buddy(recipient,true);
        client.dlm.addElement(tempBuddy);
        }
/*=================================================================================
================================ HANDLE ERROR MESSAGE =============================
=================================================================================*/
    void handleErrorMessage(String errorMessage) throws IOException
        {
        SwingUtilities.invokeLater(
            new Runnable()
                {
                public void run()
                    {
                    JOptionPane.showMessageDialog(null,errorMessage,"Error",JOptionPane.ERROR_MESSAGE);
                    }//end of run
                }//end of runnable
                );
        }
/*=======================================================================================
======================== HANDLE RECEIVING CHAT REQUEST ==================================
=======================================================================================*/
    void handleRecievingChatRequest(String sender) throws IOException
        {
        ConnectionToServer tempCTS;
        tempCTS = this;
        SwingUtilities.invokeLater(
            new Runnable()
                {
                public void run()
                    {
                    try
                        {
                        for(int x = 0; x<client.dlm.size();x++)
                            {
                            if(client.dlm.elementAt(x).buddysUsername.equals(sender))
                                {
                                if(client.dlm.elementAt(x).chatDialog == null)
                                    {
                                    String temp;
                                    temp = "Would you like to chat with "+ sender + "?";
                                    int result = JOptionPane.showConfirmDialog(null, temp ,"Chat Request",JOptionPane.YES_NO_CANCEL_OPTION);

                                    if(result == JOptionPane.YES_OPTION)
                                        {
                                        talker.send("CHAT_REQUEST_CONFIRMATION " + sender);
                                        client.dlm.elementAt(x).chatDialog = new ChatDialog(tempCTS,client.dlm.elementAt(x),client.dlm);

                                        }
                                    }
                                else
                                    {
                                    System.out.println("maybe find a way to move the dialog to the front");
                                    }
                                }
                            }
                        }
                    catch(IOException ioe)
                        {
                        System.out.println("caught an io exception");
                        }
                    }//end of run
                }//end of runnable
                );
        }
/*=======================================================================================
======================= HANDLE CHAT REQUEST UPDATE ======================================
=======================================================================================*/
    void handleChatRequestUpdate(String recipient)
        {
        ConnectionToServer tempCTS;
        tempCTS = this;

        SwingUtilities.invokeLater(
            new Runnable()
                {
                public void run()
                    {

                    for(int x = 0; x<client.dlm.size();x++)
                        {
                        if(client.dlm.elementAt(x).buddysUsername.equals(recipient))
                            {
                            if(client.dlm.elementAt(x).chatDialog == null)
                                {
                                client.dlm.elementAt(x).chatDialog = new ChatDialog(tempCTS,client.dlm.elementAt(x),client.dlm);
                                }
                            }
                        }
                    }//end of run
                }//end of runnable
                );
        }
/*=======================================================================================
======================= HANDLE RECIEVING MESSAGE ========================================
=======================================================================================*/
    void handleRecievingMessage(String sender,String messageToBeConverted)
        {

        messageToBeConverted = messageToBeConverted.replace('\1','\n');

        final String temp = messageToBeConverted;
        ConnectionToServer tempCts;
        tempCts = this;
        SwingUtilities.invokeLater(
            new Runnable()
                {
                public void run()
                    {
                    for(int x = 0;x<client.dlm.size();x++)
                        {
                        if(client.dlm.elementAt(x).buddysUsername.equals(sender))
                            {
                            if(client.dlm.elementAt(x).chatDialog != null)
                                {
                                client.dlm.elementAt(x).chatDialog.addText("<div align = \"left\"><font color=\"blue\">" + temp + "</div>");
                                }
                            else
                                {
                                client.dlm.elementAt(x).chatDialog = new ChatDialog(tempCts,client.dlm.elementAt(x),client.dlm);
                                client.dlm.elementAt(x).chatDialog.addText("<div align = \"left\"><font color=\"blue\">" + temp + "</div>");
                                }
                            }
                        }
                    }//end of run
                }//end of runnable
                );
        }
/*=======================================================================================
======================= HANDLE CHAT CLOSE ===============================================
=======================================================================================*/
    void handleChatClose(String recipient)
        {
        SwingUtilities.invokeLater(
            new Runnable()
                {
                public void run()
                    {
                    for(int x = 0; x<client.dlm.size();x++)
                        {
                        if(client.dlm.elementAt(x).buddysUsername.equals(recipient))
                            {
                            JOptionPane.showMessageDialog(null,"User "+recipient+" went offline.","ERROR",JOptionPane.ERROR_MESSAGE);
                            client.dlm.elementAt(x).chatDialog.dispose();
                            //????? is this cool?
                            }
                        }
                    }//end of run
                }//end of runnable
                );

        }
/*=======================================================================================
======================= HANDLE LOGIN ACCEPT =============================================
=======================================================================================*/
    void handleLoginAccept()
        {
        client.loginRegisterDialog.setVisible(false);
        }
/*=======================================================================================
======================= HANDLE SET OFFLINE ==============================================
=======================================================================================*/
    void handleSetOffline(String userToSetOffline)
        {
        SwingUtilities.invokeLater(
            new Runnable()
                {
                public void run()
                    {
                    for(int x = 0;x<client.dlm.size();x++)
                        {
                        if(client.dlm.elementAt(x).buddysUsername.equals(userToSetOffline))
                            {
                            client.dlm.elementAt(x).isOnline = false;
                            client.friendsJList.repaint();
                            }
                        }
                    }
                }
            );
        }
/*=======================================================================================
======================= HANDLE SET ONLINE ===============================================
=======================================================================================*/
    void handleSetOnline(String userToSetOnline)
        {
        SwingUtilities.invokeLater(
            new Runnable()
                {
                public void run()
                    {
                    for(int x = 0;x<client.dlm.size();x++)
                        {
                        if(client.dlm.elementAt(x).buddysUsername.equals(userToSetOnline))
                            {
                            client.dlm.elementAt(x).isOnline = true;
                            client.friendsJList.repaint();
                            }
                        }
                    }
                }
            );
        }
/*=======================================================================================
======================= HANDLE ADD BUDDY ================================================
=======================================================================================*/
    void handleAddBuddy(String buddyToAdd)
        {
        SwingUtilities.invokeLater(
            new Runnable()
                {
                public void run()
                    {
                    Buddy tempBuddy;
                    tempBuddy = new Buddy(buddyToAdd,false);
                    client.dlm.addElement(tempBuddy);
                    }
                }
            );
        }
    }//end of Connection To Server