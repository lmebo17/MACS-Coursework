import com.sun.javafx.PlatformUtil;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class WebWorker extends Thread {

    private long passedTime;
    private int index;
    private WebFrame.Launcher ThreadLauncher;
    private WebFrame frame;
    private String url;
    public WebWorker(WebFrame.Launcher launcher, WebFrame frame, int index, String url){
        this.ThreadLauncher = launcher;
        this.frame = frame;
        this.index = index;
        this.url = url;
        this.passedTime = 0;
    }

    @Override
    public void run(){
        ThreadLauncher.changeCurrentThreadCount("PLUS");
        ThreadLauncher.updateGUI(0);
        download(this.url);
    }

    void checkInterruption(){
        if(ThreadLauncher.isInterrupted()){
            frame.updateStatus("Interrupted", index);
        }
    }
    public void download(String urlString) {
        InputStream input = null;
        StringBuilder contents = null;
        long StartTime = System.currentTimeMillis();
        try {
            frame.updateStatus("err", index);
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            checkInterruption();
            // Set connect() to throw an IOException
            // if connection does not succeed in this many msecs.
            connection.setConnectTimeout(5000);

            connection.connect();
            checkInterruption();
            input = connection.getInputStream();

            checkInterruption();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            boolean flag = false;
            char[] array = new char[1000];
            int len;
            contents = new StringBuilder(1000);
            while ((len = reader.read(array, 0, array.length)) > 0) {
                if(ThreadLauncher.isInterrupted()){
                    frame.updateStatus("Interrupted", index);
                    break;
                }
                contents.append(array, 0, len);
                Thread.sleep(100);
            }
            checkInterruption();
            // Successful download if we get here

            if(!ThreadLauncher.isInterrupted()){
                long EndTime = System.currentTimeMillis();
                this.passedTime = EndTime - StartTime;
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                frame.updateStatus(dateFormat.format(new Date()) + "   "+ this.passedTime + "ms   " + contents.length() + " bytes", index);
            }
            ThreadLauncher.changeCompletedThreadCount("PLUS");

        }
        // Otherwise control jumps to a catch...
        catch (MalformedURLException ignored) {
        } catch (InterruptedException exception) {
            frame.updateStatus("Interrupted", index);
        } catch (IOException ignored) {
        }
        // "finally" clause, to close the input stream
        // in any case
        finally {
            try {
                if (input != null) input.close();
            } catch (IOException ignored) {
            }
            ThreadLauncher.changeCurrentThreadCount("MINUS");
            ThreadLauncher.updateGUI(this.passedTime);
            ThreadLauncher.semaphore.release();
        }
    }



}
