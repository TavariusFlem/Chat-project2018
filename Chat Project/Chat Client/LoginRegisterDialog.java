//IMPORTS
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;



class LoginRegisterDialog extends JDialog
              implements ActionListener
    {
//======================= DATA MEMBERS =====================

    JTextField          usernameTF;
    JPasswordField      passwordField;
    ConnectionToServer  cts;
    ChatClient          client;
    JCheckBox           showPasswordCheckBox;

//---------
    LoginRegisterDialog(ChatClient client)//this will contain a pointer to a cts for the client to use
        {
        this.client = client;

        //USERNAME LABEL
        JLabel usernameLabel;
        usernameLabel = new JLabel("Username: ");

        //USERNAME TEXTFIELD
        usernameTF = new JTextField(32);

        //PASSWORD LABEL
        JLabel passwordLabel;
        passwordLabel = new JLabel("Password: ");

        //PASSWORDFIELD
        passwordField = new JPasswordField(32);

        //SHOW PASSWORD CHECKBOX

        showPasswordCheckBox = new JCheckBox(" Show password ");
        showPasswordCheckBox.setActionCommand("SHOW_PASSWORD");
        showPasswordCheckBox.addActionListener(this);

        //REGISTER BUTTON
        JButton registerButton;
        registerButton = new JButton("Register");
        registerButton.setActionCommand("REGISTER");
        registerButton.addActionListener(this);

        //LOGIN BUTTON
        JButton loginButton;
        loginButton = new JButton("Login");
        loginButton.setActionCommand("LOGIN");
        loginButton.addActionListener(this);

        //MAIN PANEL
        JPanel mainPanel;
        mainPanel = new JPanel();
        this.add(mainPanel);
        mainPanel.add(usernameLabel);
        mainPanel.add(usernameTF);
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);
        mainPanel.add(showPasswordCheckBox);
        mainPanel.add(loginButton);
        mainPanel.add(registerButton);


        //isChecked off


        setupMainFrame("Login Dialog");
        }
/*=============================================================================
======================= ACTION PERFORMED ======================================
=============================================================================*/
    public void actionPerformed(ActionEvent cmd)
        {
        if(cmd.getActionCommand().equals("LOGIN"))
            {
            String password;
            password = new String(passwordField.getPassword());
            if(usernameTF.getText().contains(" "))
                {
                JOptionPane.showMessageDialog(this.getParent(),"Error loging in or registering","Error",JOptionPane.ERROR_MESSAGE);
                }
            else if(usernameTF.getText().length()<3)
                {
                JOptionPane.showMessageDialog(this.getParent(),"Error loging in or registering","Error",JOptionPane.ERROR_MESSAGE);
                }
            else if(password.contains(" "))
                {
                JOptionPane.showMessageDialog(this.getParent(),"Error loging in or registering","Error",JOptionPane.ERROR_MESSAGE);
                }
            else if(password.length()<6)
                {
                JOptionPane.showMessageDialog(this.getParent(),"Error loging in or registering","Error",JOptionPane.ERROR_MESSAGE);
                }
            else
                {
                String messageToBeSent;
                messageToBeSent = "LOGIN " + usernameTF.getText() +  " " + password;
                client.cts = new ConnectionToServer(6789,"127.0.0.1",messageToBeSent,client);
                }
            }
        else if(cmd.getActionCommand().equals("REGISTER"))
            {
            String password;
            password = new String(passwordField.getPassword());
            if(usernameTF.getText().contains(" "))
                {
                JOptionPane.showMessageDialog(this.getParent(),"Error loging in or registering","Error",JOptionPane.ERROR_MESSAGE);
                }
            else if(usernameTF.getText().length()<3)
                {
                JOptionPane.showMessageDialog(this.getParent(),"Error loging in or registering","Error",JOptionPane.ERROR_MESSAGE);
                }
            else if(password.contains(" "))
                {
                JOptionPane.showMessageDialog(this.getParent(),"Error loging in or registering","Error",JOptionPane.ERROR_MESSAGE);
                }
            else if(password.length()<6)
                {
                JOptionPane.showMessageDialog(this.getParent(),"Error loging in or registering","Error",JOptionPane.ERROR_MESSAGE);
                }
            else
                {
                String messageToBeSent;
                messageToBeSent = "REGISTER " + usernameTF.getText() + " " + password;
                client.cts = new ConnectionToServer(6789,"127.0.0.1",messageToBeSent,client);
                }
            }
        else if(cmd.getActionCommand().equals("SHOW_PASSWORD"))
            {
            if(showPasswordCheckBox.isSelected())
                {
                passwordField.setEchoChar((char)0);
                }
            else
                {
                passwordField.setEchoChar('*');
                }
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
        setSize(d.width/4,d.height/8);
        setLocation(d.width/2,d.height/2);
        setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);

        setTitle(title);
        setVisible(true);
        }
    }