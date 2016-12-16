//package firstgraphics;

import java.net.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author User
 */
class matter implements Serializable {

    int initial_prize;
    int current_max_prize;
    String name;
    Calendar setcal;
    long set_time;

    public matter(String name, int prize, int day, int hour, int minute, int second) {
        initial_prize = prize;
        current_max_prize = initial_prize;
        this.name = name;
        setcal = Calendar.getInstance();
        setcal.set(2016, 11, day, hour, minute, second);
        set_time = setcal.getTimeInMillis();
    }

    void set_max_prize(int prize) {
        if (prize > current_max_prize) {
            current_max_prize = prize;
            // current_max_prize;
        } else {
            //return -1;
        }

    }

    long isrunnable() {
        Calendar cal = Calendar.getInstance();
        //cal.set(2016, 11, 14, 18, 00, 00);
        long current_time = cal.getTimeInMillis();
        return set_time - current_time;
    }
}

public class Server {

    public static void main(String[] args) throws Exception {
        int n = 100;
        int total = 4;
        String[][] address_store = new String[100][2];
        int size = 0;
        InetAddress[] adress = new InetAddress[100];//adrs=adresses
        //String sender = args[0];//givers=senders
        String sender = "cse";
        int count = 0;
        int p[] = new int[100];//p=ports
        String[] store = new String[n];
        String[] pass = new String[n];
        int store_idx = 0;
        DatagramSocket sk = new DatagramSocket(5051);
        DatagramSocket sk2 = new DatagramSocket();
        byte dt[] = new byte[1024];
        matter[] matter_array = new matter[total];
        for (int i = 0; i < total; i++) {
            matter_array[i] = new matter("matter" + (i + 1), 100, 16, 15, 11 + i, 49 + i);
        }
        while (true) {
            // System.out.println("" + lists);

            byte bf[] = new byte[1024];
            DatagramPacket packet = new DatagramPacket(bf, bf.length);
            int a, b, c = 0, d;
            sk.receive(packet);
            InetAddress adrs = packet.getAddress();
            //System.out.println("port of client: "+packet.getPort());
            String rc = new String(packet.getData(), 0, packet.getLength());

            System.out.println(adrs);
            System.out.println("" + rc);//rc=recieved
            if (rc.contains("Port: ") == true) {
                a = rc.indexOf("Via: ");
                b = rc.indexOf("\n");
                d = rc.indexOf("\nPort: ");
                String msn = "cse";//rc.substring(a + 5, b);
                String man = rc.substring(a + 5, b);
                String port = rc.substring(d + 7, rc.length());
                int cport = Integer.parseInt(port);
                int i;
                for (i = 0; i < size; i++) {
                    if (address_store[i][0].equals(adrs) && address_store[i][1].equals(port)) {
                        break;
                    }
                    if(i==size-1)
                    {
                        address_store[size][0]=adrs.toString();
                        address_store[size][1]=port;
                        size++;
                    }
                }
                if(size==i)
                    {
                        address_store[size][0]=adrs.toString();
                        address_store[size][1]=port;
                        size++;
                    }
                for (i = 0; i < size; i++) {
                    
                     System.out.println("adress of "+address_store[i][0]+" "+address_store[i][1]);
                        
                    }
                
                //msn=message server name
                System.out.println("" + man + " hi");
                System.out.println("gone");
                if (sender.equals(man) == false) {
                    System.out.println("Warning: Server name mismatch. Message dropped.");
                } else if (sender.equals(man) == true) {
                    if (rc.contains("\nregister: ") == true) {
                        c = rc.indexOf("\nregister: ");
                        String password = rc.substring(b + 11, c);
                        System.out.println(4 + port + 6);
                        String register = rc.substring(c + 11, d);
                        //int i = 0;
                        for (i = 0; i < store_idx; i++) {
                            System.out.println("" + store[i] + " 23");
                            System.out.println("" + register + " 23");
                            System.out.println("" + pass[i] + " 23");
                            System.out.println("" + password + " 23");
                            System.out.println("" + i);
                            if ((store[i].equals(register) == true)) {
                                break;
                            }
                        }
                        if (i != store_idx && store_idx != 0) {
                            String warning = new String("Usernsme already used");
                            byte[] bt = new byte[1024];
                            bt = warning.getBytes();
                            DatagramPacket msg = new DatagramPacket(bt, bt.length, adrs, cport);
                            sk2.send(msg);

                        } else {
                            store[store_idx] = register;
                            pass[store_idx] = password;
                            store_idx++;
                            adress[count] = packet.getAddress();
                            p[count] = cport;
                            //givers[count] = giver;
                            count++;
                            String matter_list = new String();
                            for (i = 0; i < 4; i++) {
                                if (matter_array[i].isrunnable() > 0) {
                                    System.out.println("matter " + i + " " + matter_array[i].isrunnable());
                                    matter_list += matter_array[i].name + "\n";
                                }
                            }
                            System.out.println("matter list:" + matter_list);
                            System.out.println("initial msg from " + register);
                            String real_msgrc = "registration successful \n;" + matter_list;
                            bf = real_msgrc.getBytes();
                            System.out.println("" + adress[count - 1]);
                            System.out.println("" + p[count - 1]);
                            System.out.println("as");
                            DatagramPacket msg = new DatagramPacket(bf, bf.length,adrs,cport );
                            sk2.send(msg);
//InetAddress.getByName(address_store[0][0].substring(1,address_store[0][0].length())),Integer.parseInt(address_store[0][1])
                            System.out.println("sent");
                            System.out.println("" + p[count - 1]);
                        }
                    } else if (rc.contains("\nlogin: ") == true) {
                        //int a,b,c,d;
                        // sk.receive(packet);
                        //String rc = new String(packet.getData(), 0, packet.getLength());
                        a = rc.indexOf("Via: ");
                        b = rc.indexOf("\npassword: ");
                        c = rc.indexOf("\nlogin: ");
                        d = rc.indexOf("\nPort: ");
                        String login = rc.substring(c + 8, d);
                        //msn=message server name
                        String password = rc.substring(b + 11, c);
                        //String port = rc.substring(d + 7, d + 12);
                        //int cport = Integer.parseInt(port);
                        // else {
                        //int i;
                        //b = rc.indexOf("To: ");
                        //rpt = rc.substring(b + 4, c);
                        System.out.println("" + store_idx);
                        for (i = 0; i < store_idx; i++) {
                            System.out.println("" + store[i] + " 23");
                            System.out.println("" + login + " 23");
                            System.out.println("" + pass[i] + " 23");
                            System.out.println("" + password + " 23");
                            System.out.println("" + i);
                            if ((store[i].equals(login) == true) && (pass[i].equals(password) == true)) {
                                break;
                            }
                        }
                        System.out.println("" + store_idx);
                        String real_msg;
                        if (i == store_idx) {
                            System.out.println("Warning: Unknown recipient. Message dropped.");
                            real_msg = "invalid login";

                        } else {
                            // DatagramSocket cl = new DatagramSocket();
                            String matter_list = new String();
                            for (i = 0; i < 4; i++) {
                                if (matter_array[i].isrunnable() > 0) {
                                    System.out.println("matter " + i + " " + matter_array[i].isrunnable());
                                    matter_list += matter_array[i].name + "\n";
                                }
                            }
                            System.out.println("matter list:" + matter_list);
                            real_msg = "login successful \n;" + matter_list;
                        }
                        byte[] bt = new byte[1024];
                        bt = real_msg.getBytes();
                        DatagramPacket msg = new DatagramPacket(bt, bt.length, adrs, cport);
                        sk2.send(msg);
                        //sk2.send(pak);

                    } else if (rc.contains("Body") == true) {
                        String matter_name = rc.substring(rc.indexOf(';') + 1, rc.indexOf("\nPort"));
                        System.out.println(matter_name + "right");
                        DatagramSocket sk3 = new DatagramSocket();
                        int prize = 0, max_prize = 0;
                        long end_time = 0;
                        int t = 0;
                        for (t = 0; t < total; t++) {
                            if (matter_name.equals(matter_array[t].name) == true) {
                                prize = matter_array[t].initial_prize;
                                max_prize = matter_array[t].current_max_prize;
                                end_time = matter_array[t].set_time;
                                System.out.println(prize);
                                break;
                            }

                        }
                        String finaltime = Long.toString(end_time);
                        String initialvalue = Integer.toString(prize);
                        String maxvalue = Integer.toString(max_prize);
                        String combined = "Initial Prize=" + initialvalue + "\n" + "Current Maximum Prize=" + maxvalue + "matter name" + matter_array[t].name + ";" + finaltime;
                        System.out.println(2 + "Initial value=" + initialvalue + 3 + combined);
                        byte[] newmsg = new byte[1024];
                        newmsg = combined.getBytes();
                        DatagramPacket pack = new DatagramPacket(newmsg, newmsg.length, adrs, cport);
                        System.out.println(cport);
                        System.out.println(adrs);
                        sk3.send(pack);

                    } else if (rc.contains("Prize") == true) {
                        try {
                            String initialvalue = new String();
                            String maxvalue = new String();
                            System.out.println("ok");
                            String matter_prize = rc.substring(rc.indexOf("Prize:") + 6, rc.indexOf('&'));
                            String matter_name = rc.substring(rc.indexOf('&') + 1, rc.indexOf("\nPort"));
                            System.out.println(2 + matter_prize + 5);
                            System.out.println(7 + matter_name + 8);
                            int userprize = Integer.parseInt(matter_prize);
                            int t = 0;
                            long end_time = 0;
                            for (t = 0; t < total; t++) {
                                if (matter_name.equals(matter_array[t].name) == true) {
                                    matter_array[t].set_max_prize(userprize);
                                    initialvalue = Integer.toString(matter_array[t].initial_prize);
                                    maxvalue = Integer.toString(matter_array[t].current_max_prize);
                                    end_time = matter_array[t].set_time;
                                    // System.out.println(prize);
                                    System.out.println(userprize);
                                    break;
                                }

                            }
                            String finaltime = Long.toString(end_time);
                            System.out.println(userprize);
                            String combinedmsg = "Initial Prize=" + initialvalue + "\n" + "Current Maximum Prize=" + maxvalue + "matter name" + matter_array[t].name + ";" + finaltime;
                            System.out.println(combinedmsg);
                            byte[] updateprize = new byte[1024];
                            updateprize = combinedmsg.getBytes();
                            DatagramPacket pack4 = new DatagramPacket(updateprize, updateprize.length, adrs, cport);
                            DatagramSocket sk4 = new DatagramSocket();
                            sk4.send(pack4);
                        } catch (Exception e) {
                            System.out.println("null pointer");
                        }
                    }
                }

            }

        }

    }

}
//java Client a 12345 127.0.0.1 cse
