/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BankDBServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author OmarAdel
 * @author Ahmed Gomaa
 */

class clientHandler implements Runnable
{

    Socket c;
    Connection myConn;

    public clientHandler(Socket c,Connection myConn)
    {
        this.c = c;
        this.myConn=myConn;
    }

    @Override
    public void run()
    {
        try
        {
            DataOutputStream dos = new DataOutputStream(c.getOutputStream());
            DataInputStream dis = new DataInputStream(c.getInputStream());
            int choice=dis.readInt();
            float balance=1000;
            //4.perform IO with client
            while (choice!=9)
            {
                  if(choice==5)
                  {
                    dos.writeFloat(balance);
                    String result=dis.readUTF();
                    if(result.equals("good")){balance=dis.readFloat();}
                  }
                  if(choice==6)
                  {
                      int destination=dis.readInt();//compare id with stored id's
                      String result;
                      result="good"; //assuming user is always there
                      dos.writeUTF(result);
                      if(result.equals("good"))
                      {
                           dos.writeFloat(balance);
                           result=dis.readUTF();
                           if(result.equals("good")){balance=dis.readFloat();}
                      }
                  }
                  if(choice==7)
                  {
                           dos.writeFloat(balance);
                           String result=dis.readUTF();
                           if(result.equals("good")){balance=dis.readFloat();}
                      
                  
                  }
                  if(choice==8)
                  {
                      String history="Your History Is bla bla bla ";
                      dos.writeUTF(history);
                  
                  }
                   if(choice==55) //specialcase
                  {
                   int Destination=dis.readInt();
                   dos.writeUTF("good");
                   balance=balance+dis.readFloat();
                   }
                choice=dis.readInt();
                /*
                //Handle the requests from the Logic server to store and
                //retrieve data from the database.
                Statement myStmt = null;
                ResultSet myRs = null;
                try {
                        // 2. Create a statement
                        myStmt = myConn.createStatement();

                        // 3. Execute SQL query
                        myRs = myStmt.executeQuery("select * from employees");

                        // 4. Process the result set
                        while (myRs.next()) 
                            {
                                System.out.println(myRs.getString("last_name") + ", " + myRs.getString("first_name"));
                            }
                } 
                catch (Exception exc)
                {
                    exc.printStackTrace();
                }
                finally 
                {
                    if (myRs != null) {
                        myRs.close();
                    }

                    if (myStmt != null) {
                        myStmt.close();
                    }

                    if (myConn != null) {
                        myConn.close();
                    }
                }*/
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
           Scanner sc = new Scanner(System.in);
           System.out.println("enter port: ");
            int port = sc.nextInt();
            ServerSocket s = new ServerSocket(port);
            Connection myConn = null;
            
            try {
                // 1. Get a connection to database
                myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbName", "dbUsername",
                        "dbPassword");
                
            } catch (SQLException ex) {
                Logger.getLogger(BankDBServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            while (true)
            {
                
                
                Socket c = s.accept();
                
                System.out.println("Logic server Arrived!");
                clientHandler ch = new clientHandler(c,myConn);
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
