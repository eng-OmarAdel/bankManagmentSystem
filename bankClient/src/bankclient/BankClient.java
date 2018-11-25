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
           int choice =100,accountNumber=-1;
            boolean logged_in=false;
            //3. perform IO
            while (choice!=0)
            {
                 if(!logged_in)
                 {
                       System.out.println("1 : Create New Account");
                       System.out.println("2 : Login");
                       System.out.println("0 : Exit ------------------------- ");
                       System.out.println(" ------------------------- ");
                       choice = Integer.parseInt(sc.nextLine());
                        if (choice == 1)
                       {
                           dos.writeInt(choice);
                           System.out.println("please Enter your Full Name: ");
                           String name = sc.nextLine();
                           dos.writeUTF(name);
                           System.out.println("please Enter password: ");
                           String pass = sc.nextLine();
                           dos.writeUTF(pass);
                           accountNumber=dis.readInt();
                           System.out.println("\n Your Account ID is: "+Integer.toString(accountNumber));
                           logged_in=true;
                       }
                      if (choice == 2)
                      {
                          dos.writeInt(choice);
                          System.out.println("please Enter your Account ID: ");
                          int acc_id = Integer.parseInt(sc.nextLine());
                          dos.writeInt(acc_id);
                          String check = dis.readUTF();
                          if (check.equals("error"))
                           {
                              System.out.println("Invalid Account!");
                           }
                          else 
                          {
                              System.out.println("please Enter password: ");
                              String pass = sc.nextLine();
                              dos.writeUTF(pass);
                              check = dis.readUTF();
                              if (check.equals("error")) {System.out.println("Wrong Password!"); }
                              else {System.out.println("you are logged in");logged_in=true;}
                          }
                      }
                 }
                 else{
                        System.out.println("\n please choose operation: \n");
                        System.out.println("1 : Check Current Balance");
                        System.out.println("2 : Deposit Cash");
                        System.out.println("3 : Withdraw Money");
                        System.out.println("4 : Transfer Money To Another Local Account");
                        System.out.println("5 : Transfer Money To Another External Account");
                        System.out.println("6 : View Transaction History");
                        System.out.println("0 : Exit \n\n ------------------------- ");
                        choice = sc.nextInt();
                        if(choice!=0){choice=choice+2;}
                        if(choice==3)
                            {
                                dos.writeInt(choice);
                                System.out.println("Your balance is: " + dis.readFloat());
                            }
                        if(choice==4)
                            {
                                dos.writeInt(choice);
                                System.out.println("please Enter Amount Of money: ");
                                float amount = sc.nextFloat();
                                dos.writeFloat(amount);
                                String checkvalue=dis.readUTF();
                                if(checkvalue.equals("error"))
                                    {
                                        System.out.println("wrong value");
                                    }
                                else
                                    {
                                        System.out.println("good, complete");
                                    }
                            }
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
                              System.out.println("Please specify bank name: ");
                              String bankname=sc.nextLine();
                              System.out.println("please enter destination account number");
                              int destination=Integer.parseInt(sc.nextLine());
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
