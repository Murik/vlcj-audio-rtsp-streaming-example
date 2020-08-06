/**
 * 
 * RTP Streaming using VLCJ - Main APP
 * @author Mohamed AIT MANSOUR
 *
 */
public class MainApplication {
	   /**
	    * Main execution
	    * @param args
	    */
	    public static void main(String[] args) {
	    	try {
	    		
	    		System.out.println("Begin Streaming APP");
				StreamRTP rtp = new StreamRTP("127.0.0.1", 5000);
				//rtp.start("YOUR_LOCAL_IP_HERE", PORT, "sample.mp3");
				rtp.start("127.0.0.1", 5000, "AAAtVintAAA!.mp3");

	    		System.out.println("End Streaming APP");
	    		
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
}
