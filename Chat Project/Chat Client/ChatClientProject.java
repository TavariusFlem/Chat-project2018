//IMPORTS
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.event.*;
import java.io.*;


public class ChatClientProject
    {
    public static void main(String[] args)
        {
        new ChatClient();
        }
    }
class ChatClient extends JFrame
                 implements ActionListener, MouseListener
    {
//================ DATA MEMBERS ======================
    LoginRegisterDialog         loginRegisterDialog;
    ConnectionToServer          cts;
    JList<Buddy>                friendsJList;
    DefaultListModel<Buddy>     dlm;
    String                      username;
//--------------
    ChatClient()
        {

        //MAIN PANEL
        JPanel mainPanel;
        mainPanel = new JPanel();
        this.add(mainPanel);

        //FRIENDS LIST LABEL
        JLabel friendsListLabel;
        friendsListLabel = new JLabel("Friends list");

        //FRIENDS LIST MODEL
        dlm = new DefaultListModel<Buddy>();

        //FRIENDS JLIST
        friendsJList = new JList<Buddy>(dlm);
        friendsJList.addMouseListener(this);


        //SETTINGS BUTTON
        JButton changeLoginButton;
        changeLoginButton = new JButton("Change Login");
        changeLoginButton.setActionCommand("CHANGE_LOGIN");
        changeLoginButton.addActionListener(this);

        //ADD NEW FRIEND BUTTON
        JButton addNewFriendButton;
        addNewFriendButton = new JButton("Add new friend");
        addNewFriendButton.setActionCommand("ADD");
        addNewFriendButton.addActionListener(this);

        //ADDITIONS TO THE PANEL
        mainPanel.add(friendsListLabel);
        mainPanel.add(friendsJList);
        mainPanel.add(changeLoginButton);
        mainPanel.add(addNewFriendButton);

        setupMainFrame("TITLE");

        loginRegisterDialog = new LoginRegisterDialog(this);
        setTitle(username);
        }
/*=======================================================================
============================= ACTION PERFORMED ==========================
=======================================================================*/
    public void actionPerformed(ActionEvent cmd)
        {
        if(cmd.getActionCommand().equals("ADD"))
            {
            String buddyToBeAdded;
            buddyToBeAdded = JOptionPane.showInputDialog(this,"Please enter a username of the buddy you would like to add");
            System.out.println(" " + buddyToBeAdded);
            boolean isBuddy = false;
            try
                {
                for(int x = 0;x<dlm.size();x++)
                    {
                    if(dlm.elementAt(x).buddysUsername.equals(buddyToBeAdded))
                        {

                        isBuddy = true;
                        }
                    }
                if(!username.equals(buddyToBeAdded)&&!isBuddy)
                    {
                    cts.talker.send("BUDDY_REQUEST_SEND " + buddyToBeAdded);
                    }

                }
             catch(IOException ioe)
                {
                System.out.println("CAUGHT AN IOEXCEPTION");
                }
            }
        else if(cmd.getActionCommand().equals("CHANGE_LOGIN"))
            {
            //i think just reopen the dialog
            //cts.isLoggedIn = false;
            loginRegisterDialog = new LoginRegisterDialog(this);
            }
        }
/*==============================================================================================================
========================================MOUSE CLICKED ==========================================================
==============================================================================================================*/
    public void mouseClicked(MouseEvent me)
        {
        try
            {
            int index;
            if((me.getClickCount() >= 2)&& (me.getButton() == MouseEvent.BUTTON1))
                {
                index = friendsJList.locationToIndex(me.getPoint());
                //dlm.elementAt(index).chatDialog = new ChatDialog(cts,dlm.elementAt(index));
                cts.talker.send("CHAT_REQUEST_SEND " + dlm.elementAt(index).buddysUsername);
                }
            }
        catch(IOException ioe)
            {
            ioe.printStackTrace();
            }
        }
/*==============================================================================================================
========================================MOUSE ENTERED ==========================================================
==============================================================================================================*/

    public void mouseEntered(MouseEvent me)
        {
        }

/*==============================================================================================================
========================================MOUSE EXITED ===========================================================
==============================================================================================================*/
    public void mouseExited(MouseEvent me)
        {
        }
/*==============================================================================================================
======================================== MOUSE PRESSED =========================================================
==============================================================================================================*/
    public void mousePressed(MouseEvent me)
        {
        }
/*==============================================================================================================
======================================== MOUSE RELEASED ========================================================
==============================================================================================================*/
    public void mouseReleased(MouseEvent me)
        {
        }
/*==============================================================================================================
========================================SET UP MAIN FRAME=======================================================
==============================================================================================================*/
    void setupMainFrame(String title)
        {
        Toolkit tk;
        Dimension d;

        tk = Toolkit.getDefaultToolkit();
        d =tk.getScreenSize();
        setSize(d.width/12,d.height/2);
        setLocation(d.width/2,d.height/3);
        setDefaultCloseOperation(this.EXIT_ON_CLOSE);

        setTitle(title);
        setVisible(true);
        }
    }