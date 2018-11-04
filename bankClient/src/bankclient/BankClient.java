/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
/**
 *
 * @author OmarAdel
 */
public class BankClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            Scanner sc = new Scanner(System.in);
            //1.connect
            //2. create socket
            System.out.println("Insert the Host's IP: x.x.x.x");
            //Here the user should insert the Logic server IP as it will never
            //interact with the DB server
            String ip = sc.nextLine();
            System.out.println("Insert the Application's port number: >1024");
            int port = Integer.parseInt(sc.nextLine());
            Socket c = new Socket(ip, port);
            DataOutputStream dos = new DataOutputStream(c.getOutputStream());
            DataInputStream dis = new DataInputStream(c.getInputStream());

            //3. perform IO
            while (true)
            {
                //Do some stuff then break
            }

            //4.close comm
            dos.close();
            dis.close();
            c.close();
        } catch (IOException ex)
        {

        }
    }
    
}
