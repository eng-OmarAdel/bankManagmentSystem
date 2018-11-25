/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankdbserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import static java.lang.Math.abs;
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
class clientHandler implements Runnable {

    Socket c;
    Connection myConn;

    public clientHandler(Socket c, Connection myConn) {
        this.c = c;
        this.myConn = myConn;
    }

    @Override
    public void run() {
        Statement myStmt = null;
        ResultSet myRs = null;
        try {
            DataOutputStream dos = new DataOutputStream(c.getOutputStream());
            DataInputStream dis = new DataInputStream(c.getInputStream());
            int choice = dis.readInt();
            float balance;
            //4.perform IO with client
            while (choice != 0) {
                if (choice == 1) {
                    String name = dis.readUTF();
                    String pass = dis.readUTF();
                    // 2. Create a statement
                    myStmt = myConn.createStatement();

                    // 3. Execute SQL query
                    myStmt.executeUpdate("INSERT INTO users (name, password)"
                            + "VALUES ('" + name + "', '" + pass + "');");
                    myRs = myStmt.executeQuery("SELECT MAX(id) AS id FROM Users;");
                    myRs.next();
                    int id = myRs.getInt("id");
                    //int id=newUserId();
                    dos.writeInt(id);
                }
                if (choice == 2) {
                    int acc_id = dis.readInt();
                    // 2. Create a statement
                    myStmt = myConn.createStatement();
                    // 3. Execute SQL query
                    myRs = myStmt.executeQuery("SELECT password FROM Users WHERE id='" + Integer.toString(acc_id) + "'");
                    dos.writeUTF("okay");
                    myRs.next();
                    String dbPass = myRs.getString("password");
                    dos.writeUTF(dbPass);
                }
                if (choice == 3) {
                    int accountNumber = dis.readInt();
                    /*get balance from MySQL server*/
                    // 2. Create a statement
                    myStmt = myConn.createStatement();
                    // 3. Execute SQL query
                    myRs = myStmt.executeQuery("SELECT balance FROM Users WHERE id='" + Integer.toString(accountNumber) + "'");
                    myRs.next();
                    balance = myRs.getFloat("balance");
                    dos.writeFloat(balance);
                }
                if (choice == 4) {
                    int accountNumber = dis.readInt();
                    // 2. Create a statement
                    myStmt = myConn.createStatement();
                    // 3. Execute SQL query
                    myRs = myStmt.executeQuery("SELECT balance FROM Users WHERE id=" + Integer.toString(accountNumber));
                    myRs.next();
                    balance = myRs.getFloat("balance");
                    dos.writeFloat(balance);

                    balance = dis.readFloat();
                    /*insert balance to MySQL server*/
                    myStmt.executeUpdate("UPDATE Users SET balance=" + Float.toString(balance) + " WHERE id=" + Integer.toString(accountNumber));
                }
                if (choice == 5) {
                    int accountNumber = dis.readInt();
                    // 2. Create a statement
                    myStmt = myConn.createStatement();
                    // 3. Execute SQL query
                    myRs = myStmt.executeQuery("SELECT balance FROM Users WHERE id=" + Integer.toString(accountNumber));
                    myRs.next();
                    balance = myRs.getFloat("balance");
                    dos.writeFloat(balance);

                    String result = dis.readUTF();
                    if (result.equals("good")) {
                        balance = dis.readFloat();
                        myStmt.executeUpdate("UPDATE Users SET balance=" + Float.toString(balance) + " WHERE id=" + Integer.toString(accountNumber));
                    }
                }
                if (choice == 6) {

                    int destination = dis.readInt();//compare id with stored id's
                    String result;
                    result = "good"; //assuming user is always there
                    dos.writeUTF(result);
                    if (result.equals("good")) {
                        int accountNumber = dis.readInt();
                        // 2. Create a statement
                        myStmt = myConn.createStatement();
                        // 3. Execute SQL query
                        myRs = myStmt.executeQuery("SELECT balance FROM Users WHERE id=" + Integer.toString(accountNumber));
                        myRs.next();
                        balance = myRs.getFloat("balance");
                        float oldBalance = balance;
                        dos.writeFloat(balance);

                        myStmt = myConn.createStatement();
                        // 3. Execute SQL query
                        myRs = myStmt.executeQuery("SELECT balance FROM Users WHERE id=" + Integer.toString(destination));
                        myRs.next();
                        float balance2 = myRs.getFloat("balance");
                        dos.writeFloat(balance2);

                        result = dis.readUTF();
                        if (result.equals("good")) {
                            balance = dis.readFloat();
                            balance2 = dis.readFloat();

                            myStmt.executeUpdate("INSERT INTO Trans (fromID, toID, amount)"
                                    + "VALUES (" + Integer.toString(accountNumber) + ", "
                                    + Integer.toString(destination) + ", "
                                    + Float.toString(abs(oldBalance - balance)) + ");");
                            myStmt.executeUpdate("UPDATE Users SET balance=" + Float.toString(balance) + " WHERE id=" + Integer.toString(accountNumber));
                            myStmt.executeUpdate("UPDATE Users SET balance=" + Float.toString(balance2) + " WHERE id=" + Integer.toString(destination));

                        }
                    }
                }
                if (choice == 7) {

                    int accountNumber = dis.readInt();
                    int destination = dis.readInt();
                    // 2. Create a statement
                    myStmt = myConn.createStatement();
                    // 3. Execute SQL query
                    myRs = myStmt.executeQuery("SELECT balance FROM Users WHERE id=" + Integer.toString(accountNumber));
                    myRs.next();
                    balance = myRs.getFloat("balance");
                    float oldBalance = balance;
                    dos.writeFloat(balance);
                    String result = dis.readUTF();
                    if (result.equals("good")) {
                        balance = dis.readFloat();

                        myStmt.executeUpdate("INSERT INTO Trans (fromID, toID, amount)"
                                + "VALUES (" + Integer.toString(accountNumber) + ", "
                                + Integer.toString(destination) + ", "
                                + Float.toString(abs(oldBalance - balance)) + ");");
                        myStmt.executeUpdate("UPDATE Users SET balance=" + Float.toString(balance) + " WHERE id=" + Integer.toString(accountNumber));

                    }

                }
                if (choice == 8) {
                    int accountNumber = dis.readInt();
                    myStmt = myConn.createStatement();
                    // 3. Execute SQL query
                    myRs = myStmt.executeQuery("SELECT * FROM Trans WHERE fromID=" + Integer.toString(accountNumber)
                            + " OR toID=" + Integer.toString(accountNumber));
                    myRs.next();
                    String history = "From account: " + Integer.toString(myRs.getInt("fromID"))
                            + " To account: " + Integer.toString(myRs.getInt("toID"))
                            + " amount: " + Float.toString(myRs.getFloat("amount"));

                    while (myRs.next()) {
                        history += "\nFrom account: " + Integer.toString(myRs.getInt("fromID"))
                                + " To account: " + Integer.toString(myRs.getInt("toID"))
                                + " amount: " + Float.toString(myRs.getFloat("amount"));
                    }

                    dos.writeUTF(history);

                }
                if (choice == 55) //specialcase
                {
                    int Destination = dis.readInt();
                    int source = dis.readInt();
                    myStmt = myConn.createStatement();
                    // 3. Execute SQL query
                    myRs = myStmt.executeQuery("SELECT balance FROM Users WHERE id=" + Integer.toString(Destination));
                    myRs.next();
                    balance = myRs.getFloat("balance");
                    float oldBalance=balance;
                    dos.writeFloat(balance);

                    dos.writeUTF("good");

                    balance = dis.readFloat();
                    myStmt.executeUpdate("UPDATE Users SET balance=" + Float.toString(balance) + " WHERE id=" + Integer.toString(Destination));

                    myStmt.executeUpdate("INSERT INTO Trans (fromID, toID, amount)"
                            + "VALUES (" + Integer.toString(source) + ", "
                            + Integer.toString(Destination) + ", "
                            + Float.toString(abs(oldBalance - balance)) + ");");
                   
                }
                choice = dis.readInt();
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
        } catch (Exception e) {
            System.out.println("Something went wrong ");
        }

    }

}

public class BankDBServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //Here the DB module should connect with the SQL DB

            //1.Listen 
            //2.accept
            //3.create socket (I/O) with client
            Scanner sc = new Scanner(System.in);
            System.out.println("enter port: ");
            int port = Integer.parseInt(sc.nextLine());
            ServerSocket s = new ServerSocket(port);
            Connection myConn = null;

            try {
                // 1. Get a connection to database
                myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test" + Integer.toString(port),
                         "root",
                         "@Root!");

            } catch (SQLException ex) {
                Logger.getLogger(BankDBServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            while (true) {

                Socket c = s.accept();

                System.out.println("Logic server Arrived!");
                clientHandler ch = new clientHandler(c, myConn);
                //ch.run();//wait till run finish its code

                //handle client in parrallel
                Thread t = new Thread(ch);
                t.start();
                //create new light weight process 
                //and run in parallel and the main thread
                //continues

            }

            //s.close();
        } catch (IOException ex) {
            System.out.println("Something went wrong");
        }

    }

}
