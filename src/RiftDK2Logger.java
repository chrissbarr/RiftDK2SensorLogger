
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.oculusvr.capi.Hmd;

import static com.oculusvr.capi.OvrLibrary.ovrTrackingCaps.*;

import com.oculusvr.capi.OvrQuaternionf;
import com.oculusvr.capi.OvrVector3f;
import com.oculusvr.capi.TrackingState;

public class RiftDK2Logger {
	
	private static SensorData latestData = new SensorData(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	private static boolean run = true;
	private static final long TIME_BETWEEN_SAMPLES = 25;
	
	public static void main(String[] args) throws UnknownHostException {
		 
        Hmd.initialize();
 
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
 
        Hmd hmd = Hmd.create(0);
 
        if (hmd == null) {
            throw new IllegalStateException("Unable to initialize HMD");
        }
 
        hmd.configureTracking(ovrTrackingCap_Orientation
                | ovrTrackingCap_MagYawCorrection
                | ovrTrackingCap_Position, 0);
 
        Thread t1 = new Thread(new SensorFetcher(hmd));
        t1.start();
 
        
 
        System.out.println("Press 'q' to quit..");
        System.out.println("");
        Scanner sc = new Scanner(System.in);
        while (run) {
            if (sc.nextLine().trim().equals("q")) {
                run = false;
            }
        }
        
 
        try {
            t1.join();
        } catch (InterruptedException ex) {
            //
        }
 
        hmd.destroy();
        Hmd.shutdown();
    }
	
	 private static class SensorFetcher implements Runnable {
		 
	        private final Hmd hmd;
	        private long startTime = System.currentTimeMillis();
	        private long lastReadingTime = startTime;
	 
	        public SensorFetcher(Hmd hmd) {
	            this.hmd = hmd;
	        }
	 
	        @Override
	        public void run() {
	        	
	        	String outputFileName = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()) + ".txt"; 
	        	
	        	PrintWriter printWriter = null;
	        	
	        	try {
	        		printWriter = new PrintWriter(outputFileName, "UTF-8");
	        	} catch (FileNotFoundException e) {
	        		
	        	} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	            while (run) {
	                TrackingState sensorState = hmd.getSensorState(Hmd.getTimeInSeconds());
	                
	               
	 
	                OvrVector3f pos = sensorState.HeadPose.Pose.Position;
	                OvrQuaternionf quat = sensorState.HeadPose.Pose.Orientation;
	                OvrVector3f accel = sensorState.HeadPose.AngularAcceleration;
	                OvrVector3f veloc = sensorState.HeadPose.AngularVelocity;
	                
	                double px = pos.x;
	                double py = pos.y;
	                double pz = pos.z;
	 
	                double qx = quat.x;
	                double qy = quat.y;
	                double qz = quat.z;
	                double qw = quat.w;
	                
	                double ax = accel.x;
	                double ay = accel.y;
	                double az = accel.z;
	                
	                double vx = veloc.x;
	                double vy = veloc.y;
	                double vz = veloc.z;
	 
	                latestData = new SensorData(System.currentTimeMillis(), px, py, pz, qx, qy, qz, qw, ax, ay, az, vx, vy, vz);
	                System.out.println(latestData.toString());
	                
	                if(printWriter != null)
	                {
	                	long timeSinceStart = System.currentTimeMillis() - startTime;
	                	printWriter.println(timeSinceStart + "\t" + latestData.toString());
	                }
	                
	                try {
	                	while(System.currentTimeMillis() - lastReadingTime < TIME_BETWEEN_SAMPLES) 
	                	{
	                		Thread.sleep(1);
	                	}
	                	
	                	lastReadingTime = System.currentTimeMillis();
	                	
	                } catch (InterruptedException ex) {
	                    //
	                }
	            }
	            
	            printWriter.close();
	            
	        }
	    }
	
	
	
	private static class SensorData {
		 
        private final long id;
        private final double px, py, pz, qx, qy, qz, qw, ax, ay, az, vx, vy, vz;
        private double pitch, roll, yaw;
 
        public SensorData(long id, double px, double py, double pz, double qx, double qy, double qz, double qw, double ax, double ay, double az, double vx, double vy, double vz) {
            this.id = id;
            this.px = px;
            this.py = py;
            this.pz = pz;
            this.qx = qx;
            this.qy = qy;
            this.qz = qz;
            this.qw = qw;
            
            this.ax = ax;
            this.ay = ay;
            this.az = az;
            
            this.vx = vx;
            this.vy = vy;
            this.vz = vz;
            
            
            
        }
 
        public long getId() {
            return id;
        }
 
        public double[] asArray() {
            return new double[]{id, px, py, pz, qx, qy, qz, qw};
        }
 
        @Override
        public String toString() {
            return String.format("%.3f\t%.3f\t%.3f\t%.3f\t%.3f\t%.3f\t%.3f\t%.3f\t%.3f\t%.3f\t%.3f\t%.3f\t%.3f", px, py, pz, qx, qy, qz, qw,ax,ay,az,vx,vy,vz);
        	//return String.format("Position: %.3f  %.3f  %.3f | Quat:  %.3f  %.3f  %.3f  %.3f", px, py, pz, qx, qy, qz, qw);
        }
    }

}


