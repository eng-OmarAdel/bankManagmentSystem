/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package    banklogicserver;

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
            DataOutputStream dos1 = new DataOutputStream(dbSocket.getOutputStream());
            DataInputStream dis1 = new DataInputStream(dbSocket.getInputStream());
            
            int accountNumber=-1;
            int choice=dis.readInt();
            dos1.writeInt(choice);
            //4.perform IO with client
            while (choice!=0)
            {
                System.out.println("server inside while");
                System.out.println(choice);
                 if(choice == 1)
                {
                    System.out.println("Logical server inside Sign up");
                    String name = dis.readUTF();
                    dos1.writeUTF(name);
                    String pass = dis.readUTF();
                    dos1.writeUTF(pass);
                    accountNumber=dis1.readInt();
                    dos.writeInt(accountNumber);
                }
                if(choice == 2)
                {
                    System.out.println("Logical server inside login");
                    int acc_id = dis.readInt();
                    accountNumber=acc_id;
                    dos1.writeInt(acc_id);
                    String result=dis1.readUTF();
                    dos.writeUTF(result);
                    if (result.equals("okay"))
                    {
                         String pass = dis.readUTF();
                         String localpass=dis1.readUTF();
                         if (pass.equals(localpass))
                         {
                             result = "good";
                             dos.writeUTF(result);
                         }
                         else {
                             System.out.println(pass);
                             result="error";
                             dos.writeUTF(result);
                         }
                    }
                }
                if(choice==3)
                {
                    System.out.println("logical server inside current balance");
                    if(accountNumber==-1){
                        /*logIn();*/
                    }
                    else{
                        dos1.writeInt(accountNumber);
                        float balance=dis1.readFloat();
                        dos.writeFloat(balance);
                    }
                    
                }
                if(choice==4)
                {
                    System.out.println("Logical server inside deposit");
                    if(accountNumber==-1){
                        /*logIn()*/
                    }
                    else{
                        dos1.writeInt(accountNumber);
                        float amount=dis.readFloat();
                        String result;
                        if(amount>0)
                        {
                            result ="okay";
                            float balance =dis1.readFloat();
                            balance=balance+amount;
                            dos1.writeFloat(balance);
                            dos.writeUTF(result);
                        }
                        else
                        {
                        result="error";
                        dos.writeUTF(result);
                        }
                    }
                    
                }
                if(choice==5)
                    {
                        System.out.println("Logical server inside withdraw");
                        dos1.writeInt(accountNumber);
                        float amount=dis.readFloat();
                        String result;
                            if(amount>0)
                            {
                               result ="okay";
                               dos.writeUTF(result);
                               float balance =dis1.readFloat();
                                if(amount<=balance)
                                    {
                                        result="good";
                                        balance=balance-amount;
                                        dos1.writeUTF(result);
                                        dos1.writeFloat(balance);
                                    }
                                    else
                                    {
                                        result="error1";
                                        dos1.writeUTF(result);
                                    }
                               dos.writeUTF(result);
                            }
                            else
                            {
                                result="error";
                                dos.writeUTF(result);
                            }   
                    }
                 if(choice==6)
                    {
                         System.out.println("Logical server inside local transfer");
                          int destination=dis.readInt();
                          dos1.writeInt(destination);
                           String result;
                           result=dis1.readUTF();
                          dos.writeUTF(result);
                          dos1.writeInt(accountNumber);
                          if(!(result.equals("error")))
                          {
                            float amount=dis.readFloat();
                            if(amount>0)
                            {
                               result ="okay";
                               dos.writeUTF(result);
                               float balanceFrom=dis1.readFloat();
                               float balanceTo=dis1.readFloat();
                               if(amount<=balanceFrom)
                                    {
                                        result="good";
                                        dos1.writeUTF(result);
                                        balanceFrom=balanceFrom-amount;
                                        balanceTo=balanceTo+amount;
                                        dos1.writeFloat(balanceFrom);
                                        dos1.writeFloat(balanceTo);
                                    }
                            else
                                    {
                                        result="error1";
                                        dos1.writeUTF(result);
                                    }
                               dos.writeUTF(result);
                            }
                            else
                            {
                                result="error";
                                dos.writeUTF(result);
                            }   
                              
                          }
                    }
                 if(choice==7)
                    {
                        System.out.println("Logical server inside external transfer ");
                        // define ip and port upon bankid from some list
                        //----------
                        System.out.println("before");
                        String external_Ip = "localhost";
                        int external_port = dis.readInt(); //needs changing
                        Socket externalSocket = new Socket(external_Ip, external_port);
                        DataOutputStream dos2 = new DataOutputStream(externalSocket.getOutputStream());
                        DataInputStream dis2 = new DataInputStream(externalSocket.getInputStream());
                        
                        //----------
                        System.out.println("after");
                                  dos2.writeInt(55); //x
                                  int destination=dis.readInt();
                                  
                                  dos2.writeInt(destination);
                                  
                                  System.out.println("sent");
                                  /*Sent destination account and waiting*/
                                  String result=dis2.readUTF();
                                  dos.writeUTF(result);
                                  if(!(result.equals("error")))
                                    {
                                         float amount=dis.readFloat();
                                           if(amount>0)
                                                {
                                                   result ="okay";
                                                   dos.writeUTF(result);
                                                   dos1.writeInt(accountNumber);
                                                   dos1.writeInt(destination);
                                                   float balance=dis1.readFloat();
                                                   if(amount<=balance)
                                                        {
                                                            result="good";
                                                            dos1.writeUTF(result);
                                                            balance=balance-amount;
                                                            dos1.writeFloat(balance);
                                                            dos2.writeFloat(amount);
                                                        }
                                                else
                                                        {
                                                            result="error1";
                                                            dos1.writeUTF(result);
                                                        }
                                                   dos.writeUTF(result);
                                                }
                                    }
                     }
                  if(choice==8)
                    {
                        System.out.println("logical server inside View Transaction History ");
                        dos1.writeInt(accountNumber);
                        dos.writeUTF(dis1.readUTF());
                     }
                  if(choice==55) //specialcase
                  {
                          System.out.println("Logical server inside special case");
                          int destination=dis.readInt();
                          dos1.writeInt(destination);
                          float balance=dis1.readFloat();
                          String result;
                          result=dis1.readUTF();
                          dos.writeUTF(result);
                          if(!(result.equals("error")))
                          {
                              dos1.writeFloat(balance+dis.readFloat());//transferrenig cash to my account db
                          }
                      
                  }
                  
                choice=dis.readInt();
            dos1.writeInt(choice);
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
/**
 *
 * @author Ahmed Gomaa
 */
public class  BankLogicServer {

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
             Scanner sc = new Scanner(System.in);
             System.out.println("enter port: ");
            int port = Integer.parseInt(sc.nextLine());
            ServerSocket s = new ServerSocket(port);
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
