//IMPORTS
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.text.html.*;
import javax.swing.text.*;
import java.io.*;
import java.awt.dnd.*;


class ChatDialog extends JDialog
                    implements ActionListener, WindowListener, DropTargetListener
    {
//-------------------DATA MEMBERS ------------------------------------
    ConnectionToServer      cts;
    Buddy                   currentlyChattingBuddy;
    JEditorPane             chatLog;
    JTextArea               textBox;
    DefaultListModel<Buddy> dlm;
//--------------------------------------------------------------------
    ChatDialog(ConnectionToServer cts,Buddy currentlyChattingBuddy,DefaultListModel<Buddy> dlm)
        {
        this.dlm = dlm;
        this.cts = cts;
        this.currentlyChattingBuddy = currentlyChattingBuddy;

        //MAIN PANEL
        JPanel mainPanel;
        mainPanel =  new JPanel(new BorderLayout());

        //CHAT LOG
        chatLog = new JEditorPane();
        chatLog.setContentType("text/html");
        chatLog.setEditable(false);

        //CHAT LOG SCROLL PANE
        JScrollPane chatLogScrollPane;
        chatLogScrollPane = new JScrollPane(chatLog);
        mainPanel.add(chatLogScrollPane,BorderLayout.CENTER);

        //TEXT BOX PANEL
        JPanel textBoxPanel;
        textBoxPanel = new JPanel();
        mainPanel.add(textBoxPanel,BorderLayout.SOUTH);

        //TEXT BOX
        textBox = new JTextArea(3,31);
        textBoxPanel.add(textBox);

        //SEND BUTTON
        JButton sendButton;
        sendButton = new JButton("Send");
        sendButton.setActionCommand("SEND");
        sendButton.addActionListener(this);
        textBoxPanel.add(sendButton);
        chatLog.setText("Welcome to the chat!");
        this.add(mainPanel);

        this.addWindowListener(this);

        setupMainFrame("Chatting with " + currentlyChattingBuddy.buddysUsername);
        }
/*=======================================================================================
=============================== ACTION PERFRORMED =======================================
=======================================================================================*/
    public void actionPerformed(ActionEvent cmd)
        {
        if(cmd.getActionCommand().equals("SEND"))
            {
            try
                {

                String tempString;
                tempString = textBox.getText();
                addText("<div align = \"right\"><font color=\"red\">" + tempString + "</div>");
                tempString = tempString.replace('\n','\1');

                cts.talker.send("MESSAGE_SEND " + currentlyChattingBuddy.buddysUsername + " " + tempString);
                textBox.setText("");
                textBox.requestFocus();
                }
            catch(IOException ioe)
                {
                ioe.printStackTrace();
                }
            }
        }
// THIS IS HERE JUST INCASE I NEED IT
/*=======================================================================================
=============================== WINDOW ACTIVATED ========================================
=======================================================================================*/
    public void windowActivated(WindowEvent we)
        {
        }
/*=======================================================================================
=============================== WINDOW CLOSED ===========================================
=======================================================================================*/
    public void windowClosed(WindowEvent we)
        {
        System.out.println("SETTING CHAT DIALOG TO NULL");

        dlm.elementAt(dlm.indexOf(currentlyChattingBuddy,0)).chatDialog = null;
        //System.out.println();
        //wrong thing to set to null
        //set the dlms pointer to this to null
        }
/*=======================================================================================
=============================== WINDOW CLOSING ==========================================
=======================================================================================*/
    public void windowClosing(WindowEvent we)
        {
        System.out.println("WINDOW CLOSING");
        }
/*=======================================================================================
=============================== WINDOW DEACTIVATED ======================================
=======================================================================================*/
    public void windowDeactivated(WindowEvent we)
        {
        System.out.println("WINDOW DEACTIVATED");
        }
/*=======================================================================================
=============================== WINDOW DEICONIFIED ======================================
=======================================================================================*/
    public void windowDeiconified(WindowEvent we)
        {
        System.out.println("WINDOW DEICONIFIED");
        }
/*=======================================================================================
=============================== WINDOW ICONIFIED ========================================
=======================================================================================*/
    public void windowIconified(WindowEvent we)
        {
        }
/*=======================================================================================
=============================== WINDOW OPENED ===========================================
=======================================================================================*/
    public void windowOpened(WindowEvent we)
        {
        }
/*=======================================================================================
=============================== DRAG ENTER ==============================================
=======================================================================================*/
    public void dragEnter(DropTargetDragEvent dtde)
        {
        }
/*=======================================================================================
=============================== DRAG EXIT ===============================================
=======================================================================================*/
    public void dragExit(DropTargetEvent dte)
        {
        }
/*=======================================================================================
=============================== DRAG OVER ===============================================
=======================================================================================*/
    public void dragOver(DropTargetDragEvent dtde)
        {

        }
/*=======================================================================================
=============================== DROP ====================================================
=======================================================================================*/
    public void drop(DropTargetDropEvent dtdre)
        {
        System.out.println("got a drop");
        /*//File fileToDrop;
        Transferable transferableData;

        transferableData = dtde.getTransferable();

        FileInputStream fis;
        DataInputStream dis;

        try
            {
            if(transferableData.isDataFlavorSupported(DataFlavor.))//???????
                {
                dtdre.acceptDrop(DnDConstants.ACTION_COPY);
                //fileToDrop = (File)(transferableData.getTransferData(DataFlavor.))
                //send a message with the filename size and your name
                //cts.talker.send("FILE_TRANSFER REQUEST " + recipient + " "+ fileToDrop.length() + " " + fileToDrop.getName());

                }
            }
        catch(IOException ioe)
            {
            ioe.printStackTrace();
            System.out.println("IOException !!!");
            }
        catch(UnsupportedFlavorException ufe)
            {
            System.out.println("unsupported flavor found!!!");
            }*/
        }
/*=======================================================================================
=============================== DROP ACTION CHANGED =====================================
=======================================================================================*/
    public void dropActionChanged(DropTargetDragEvent dtde)
        {
        }
/*==============================================================================================================
======================================== ADD MESSAGE ===========================================================
==============================================================================================================*/
   public void addText(String messageToBeAdded)
        {
        HTMLDocument doc;
        Element html;
        Element body;


        doc = (HTMLDocument)chatLog.getDocument();
        html = doc.getRootElements()[0];
        body = html.getElement(1);

        try
            {
            doc.insertBeforeEnd(body,messageToBeAdded);
            chatLog.setCaretPosition(chatLog.getDocument().getLength());
            }
        catch(Exception e)
            {
            System.out.println("Caught an exception when inserting text");
            }
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
        setSize(d.width/4,d.height/4);
        setLocation(d.width/2,d.height/2);
        setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);

        setTitle(title);
        setVisible(true);
        }
    }