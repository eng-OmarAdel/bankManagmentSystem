/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankdbserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author OmarAdel
 */

class clientHandler implements Runnable
{

    Socket c;

    public clientHandler(Socket c)
    {
        this.c = c;
    }

    @Override
    public void run()
    {
        try
        {
            DataOutputStream dos = new DataOutputStream(c.getOutputStream());
            DataInputStream dis = new DataInputStream(c.getInputStream());

            //4.perform IO with client
            while (true)
            {
                //Handle the requests from the Logic server to store and
                //retrieve data from the database.
            }

            //5. close comm with client
            dos.close();
            dis.close();
            c.close();
        } catch (Exception e)
        {
            System.out.println("Something went wrong ");
        }

    }

}

public class BankDBServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            //Here the DB module should connect with the SQL DB
            
            
            //1.Listen 
            //2.accept
            //3.create socket (I/O) with client
            ServerSocket s = new ServerSocket(2222);
            while (true)
            {
                
                Socket c = s.accept();
                
                System.out.println("Logic server Arrived!");
                clientHandler ch = new clientHandler(c);
                //ch.run();//wait till run finish its code

                //handle client in parrallel
                Thread t = new Thread(ch);
                t.start();
                //create new light weight process 
                //and run in parallel and the main thread
                //continues

            }

            //s.close();
        } catch (IOException ex)
        {
            System.out.println("Something went wrong");
        }

    }
    
}
