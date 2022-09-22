//imports
import java.net.*;
import java.io.*;


class Talker
    {
//===========DATA MEMBERS============================

    DataOutputStream dos;
    BufferedReader br;

//===========CLIENT SIDE CONTRUCTOR==================

    Talker(int portNumber,String ip)
        {
        try
            {
            Socket socket;

            socket = new Socket(ip,portNumber);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dos = new DataOutputStream(socket.getOutputStream());

            }
        catch(IOException ioe)
            {
            System.out.println("caught an io exception");
            }
        catch(Exception e)
            {
            System.out.println("caught an exception");
            e.printStackTrace();
            }
        }

//===========SERVER SIDE CONTRUCTOR==================

    Talker(Socket normalSocket)
        {
        try
            {
            br = new BufferedReader(new InputStreamReader(normalSocket.getInputStream()));
            dos = new DataOutputStream(normalSocket.getOutputStream());
            //maybe should have a test message for the start
            }
        catch(IOException ioe)
            {
            System.out.println("caught an ioexception");
            ioe.printStackTrace();
            }
        }

/*==================================================================
======================== SEND ======================================
==================================================================*/

    public void send(String message) throws IOException
        {
        //use a get text before calling this method
        if(!(message.endsWith('\n' + "")))
            {
            message = message + '\n';
            }
        System.out.println("Sending: " + message +"");
        dos.writeBytes(message + "");
        }

/*==================================================================
======================== RECEIVE ===================================
==================================================================*/

    public String receive() throws IOException
        {
        String tempMessageString;
        tempMessageString = br.readLine();

        System.out.println("Receiving: " + tempMessageString);
        return tempMessageString;
        }
    }