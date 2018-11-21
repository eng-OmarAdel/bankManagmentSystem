/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package    BankLogicServer;

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
            
            int choice=dis.readInt();
            dos1.writeInt(choice);
            //4.perform IO with client
            while (choice!=9)
            {
                System.out.println("server inside while");
                System.out.println(choice);
                if(choice==5)
                    {
                        System.out.println("Logical server inside withdraw");
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
                          if(!(result.equals("error")))
                          {
                            float amount=dis.readFloat();
                            if(amount>0)
                            {
                               result ="okay";
                               dos.writeUTF(result);
                               float balance=dis1.readFloat();
                               if(amount<=balance)
                                    {
                                        result="good";
                                        dos1.writeUTF(result);
                                        balance=balance-amount;
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
                    }
                 if(choice==7)
                    {
                        System.out.println("Logical server inside external transfer ");
                        int bankid=dis.readInt(); // define ip and port upon bankid from some list
                        //----------
                        System.out.println("before");
                         String external_Ip = "localhost";
                         int external_port = 3333; //needs changing
                         Socket externalSocket = new Socket(external_Ip, external_port);
                         DataOutputStream dos2 = new DataOutputStream(externalSocket.getOutputStream());
                         DataInputStream dis2 = new DataInputStream(externalSocket.getInputStream());
                        //----------
                        System.out.println("after");
                                  dos2.writeInt(55); //x
                                  int destination=dis.readInt();
                                  dos2.writeInt(destination);
                                  System.out.println("sent");
                                  String result=dis2.readUTF();
                                  dos.writeUTF(result);
                                  if(!(result.equals("error")))
                                    {
                                         float amount=dis.readFloat();
                                           if(amount>0)
                                                {
                                                   result ="okay";
                                                   dos.writeUTF(result);
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
                        dos.writeUTF(dis1.readUTF());
                     }
                  if(choice==55) //specialcase
                  {
                          System.out.println("Logical server inside special case");
                          int destination=dis.readInt();
                          dos1.writeInt(destination);
                          String result;
                          result=dis1.readUTF();
                          dos.writeUTF(result);
                          if(!(result.equals("error")))
                          {
                              dos1.writeFloat(dis.readFloat());//transferrenig cash to my account db
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
            int port = sc.nextInt();
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
