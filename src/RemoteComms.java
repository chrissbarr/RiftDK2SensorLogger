import java.lang.*;
import java.io.*;
import java.net.*;

public class RemoteComms {
	
	static ServerSocket srvr;
	static Socket skt;
	static PrintWriter out;
	
	public static void initComms(int port) {
		System.out.println("Initialising comms...");
		String data = "Toobie ornaught toobie\n";
		try {
	         srvr = new ServerSocket(port);
	         skt = srvr.accept();
	         System.out.print("Server has connected!\n");
	         out = new PrintWriter(skt.getOutputStream(), true);
	         System.out.println("Connected\n");
	         out.print(data);
	      }
	      catch(Exception e) {
	         System.out.print("Whoops! It didn't work!\n");
	      }
	}
	
	public static void closeComms() {
		try {
			System.out.print("Disconnecting\n");
	        out.print("Disconnecting");
	        out.close();
	        skt.close();
	        srvr.close();
		}
	      catch(Exception e) {
	         System.out.print("Whoops! It didn't work!\n");
	    }
	}
	
	public static boolean transmit(String data) {
		try {
			System.out.print(data);
	        out.print(data);
	        return true;

		}
	      catch(Exception e) {
	         System.out.print("Whoops! It didn't work!\n");
	         return false;
	    }
	}
}
