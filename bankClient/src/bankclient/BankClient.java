/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BankClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
/**
 * 
 * @author  OmarAdel
 * @author Ahmed Gomaa
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
            int choice = 0;
            //3. perform IO
            while (choice!=9)
            {
                
                 System.out.println("\n please choose operation: \n");
                 System.out.println("5 : Withdraw Money");
                 System.out.println("6 : Transfer Money To Another Local Account");
                 System.out.println("7 : Transfer Money To Another External Account");
                 System.out.println("8 : Transfer Money To View Transaction History");
                 choice = sc.nextInt();
                 if(choice==5)
                 {
                    dos.writeInt(choice);
                    System.out.println("please Enter Amount Of money: ");
                    float amount = sc.nextFloat();
                    dos.writeFloat(amount);
                    String checkvalue=dis.readUTF();
                    if(checkvalue.equals("error")){System.out.println("wrong value");}
                    else
                    {
                                    checkvalue=dis.readUTF();
                                    if(checkvalue.equals("error1")){System.out.println("No enough money");}
                                    else{System.out.println("good, complete");}
                    }
                  }
                  if(choice==6)
                   {
                       dos.writeInt(choice);
                       System.out.println("please enter destination account number");
                       int destination=sc.nextInt();
                       dos.writeInt(destination);
                       String checkacc=dis.readUTF();
                        if(checkacc.equals("error")){System.out.println("wrong account");}
                        else
                        {
                           System.out.println("please Enter Amount Of money: ");
                           float amount = sc.nextFloat();
                           dos.writeFloat(amount);
                           String checkvalue=dis.readUTF();
                           if(checkvalue.equals("error")){System.out.println("wrong value");}
                           else
                            {
                                    checkvalue=dis.readUTF();
                                    if(checkvalue.equals("error1")){System.out.println("No enough money");}
                                    else{System.out.println("good, complete");}
                             }
                          }
                   }
                  if(choice==7)
                  {
                      dos.writeInt(choice);
                      System.out.println("Please specify bank ");
                      int bankid=sc.nextInt();
                      dos.writeInt(bankid);
                      System.out.println("please enter destination account number");
                      int destination=sc.nextInt();
                      dos.writeInt(destination);
                      String checkacc=dis.readUTF();
                      if(checkacc.equals("error")){System.out.println("wrong account");}
                      else
                        {
                           System.out.println("please Enter Amount Of money: ");
                           float amount = sc.nextFloat();
                           dos.writeFloat(amount);
                           String checkvalue=dis.readUTF();
                           if(checkvalue.equals("error")){System.out.println("wrong value");}
                           else
                            {
                                    checkvalue=dis.readUTF();
                                    if(checkvalue.equals("error1")){System.out.println("No enough money");}
                                    else{System.out.println("good, complete");}
                             }
                          }
                      
                  }
                  if(choice==8)
                  {
                      dos.writeInt(choice);
                      System.out.println(dis.readUTF());
                  }
            }

            //4.close comm
            dos.close();
            dis.close();
            c.close();
        } 
        catch (IOException ex)
        {
            System.out.println("couldn't connect");
        }
    }
    
}
