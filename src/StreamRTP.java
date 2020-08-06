import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.list.MediaListPlayer;
import uk.co.caprica.vlcj.player.list.MediaListPlayerEventAdapter;
import uk.co.caprica.vlcj.player.list.MediaListPlayerMode;

/**
 * 
 * RTP Streaming using VLCJ
 * @author Mohamed AIT MANSOUR
 *
 */
public class StreamRTP {
    private MediaPlayerFactory factory;
    private MediaListPlayer mediaListPlayer;
    private MediaList playList;
    
    public StreamRTP(String address, int port) throws Exception {
        boolean found = new NativeDiscovery().discover();
        if (found) {
        	System.out.println(LibVlc.INSTANCE.libvlc_get_version());
            String[] mediaOptions = formatRtpStream(address, port, "demo");
            factory = new MediaPlayerFactory(mediaOptions);
            mediaListPlayer = factory.newMediaListPlayer();
            mediaListPlayer.addMediaListPlayerEventListener(new MediaListPlayerEventAdapter() {
                @Override
                public void nextItem(MediaListPlayer mediaListPlayer, libvlc_media_t item, String itemMrl) {
                    System.out.println("Playing next item: " + itemMrl + " (" + item + ")");
                }
            });
            playList = factory.newMediaList();
		}else {
			throw new Exception("Error During construction, please check NativeDiscovery");
		}
    }
   
    @Override
    public void finalize() {
        // Stop processing.
        mediaListPlayer.stop();
        // Finish program.
        mediaListPlayer.release();
        factory.release();
    }
        
    /**
     * 
     * @param serverAddress
     * @param serverPort
     * @param id
     * @return  mediaOptions for rtp stream wih vlc
     */
    private String[] formatRtpStream(String serverAddress, int serverPort, String id) {
        // https://wiki.videolan.org/VLC_command-line_help
        return new String[]{
            ":sout=#rtp{sdp=rtsp://" + serverAddress +":"+serverPort+"/"+id+",proto=tcp}",
                ":no-sout-rtp-sap", // SAP announcing (default disabled)
                ":no-sout-standard-sap", // SAP announcing (default disabled)
                ":sout-all", //Enable streaming of all ES (default enabled) Stream all elementary streams
                ":rtsp-caching=100",
                ":sout-keep",
                ":rtsp-frame-buffer-size=1000"
        };
    }

    /**
     * Start streaming of music by adding music to the playList
     * 
     * @param address
     * @param port
     * @param music
     * @throws Exception
     */
     void start(String address, int port, String music) throws Exception {
        mediaListPlayer = factory.newMediaListPlayer();
//        String[] mediaOptions = formatRtpStream(address, port, "demo");
        playList.addMedia("music/" + music);
        // Attach the play-list to the media list player
        mediaListPlayer.setMediaList(playList);
        mediaListPlayer.setMode(MediaListPlayerMode.LOOP);
        // Finally, start the media player
        mediaListPlayer.play();
        System.out.println("Streaming started at rtsp://" + address + ":" + port + "/demo");

    }

}