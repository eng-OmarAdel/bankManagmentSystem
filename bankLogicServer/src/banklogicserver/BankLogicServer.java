/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banklogicserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


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
            //Here the server initiates a new connection with the DB server with each
            //new thread so that each client has its own connection to the DB
            //(acting as a client)
            Scanner sc = new Scanner(System.in);
            //1.connect
            //2. create socket
            System.out.println("Insert the DBS's IP: x.x.x.x");
            //Here the user should insert the Logic server IP as it will never
            //interact with the DB server
            String ip = sc.nextLine();
            System.out.println("Insert the DB app's port number: >1024");
            int port = Integer.parseInt(sc.nextLine());
            Socket dbSocket = new Socket(ip, port);
            
            
            //Here the server creates IO streams with the client
            DataOutputStream dos = new DataOutputStream(c.getOutputStream());
            DataInputStream dis = new DataInputStream(c.getInputStream());

            //4.perform IO with client
            while (true)
            {
                //Handle the requests from the client and request needed data
                //from the DB server
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

public class BankLogicServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            //Here the server waits for the client to initiate connection with it
            //(acting as a server)
            
            //1.Listen 
            //2.accept
            //3.create socket (I/O) with client
            ServerSocket s = new ServerSocket(1234);
            while (true)
            {
                
                Socket client = s.accept();
                
                System.out.println("Client Arrived");
                clientHandler ch = new clientHandler(client);
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
