package hu.tilos.radio.backend.streamer;

import hu.tilos.radio.backend.StreamController;
import org.apache.deltaspike.core.api.config.ConfigProperty;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

@Named
public class LocalBackend implements Backend {

    @Inject
    @ConfigProperty(name = "archive.root")
    private String root;

    /**
     * Limit in Kbyte/sec.
     */
    @Inject
    @ConfigProperty(name = "throttle.limit")
    private int throttleLimit = 0;

    public LocalBackend(String root) {
        this.root = root;
    }

    public LocalBackend() {
    }

    @Override
    public void stream(StreamController.ResourceCollection collection, int startOffset, int endPosition, OutputStream out) throws Exception {
        InputStream[] streams = new InputStream[collection.getCollection().size()];

        int i = 0;
        for (StreamController.Mp3File file : collection.getCollection()) {
            streams[i++] = new LimitedInputStream(new FileInputStream(root + file.getName()), file.getStartOffset(), file.getEndOffset());
        }
        byte[] b = new byte[4096];
        int r;
        InputStream is = new LimitedInputStream(new CombinedInputStream(streams), startOffset, endPosition);
        if (throttleLimit > 0) {
            is = new ThrottledInputStream(is, throttleLimit * 1024);
        }


        try {
            while ((r = is.read(b)) != -1) {
                out.write(b, 0, r);
            }
            out.flush();
            out.close();
        } catch (Exception ex) {
            if (!ex.getClass().getName().contains("EofException")) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        } finally {
            is.close();
        }


    }

    @Override
    public int getSize(StreamController.ResourceCollection collection) {
        int size = 0;
        for (StreamController.Mp3File file : collection.getCollection()) {
            size += size(file);
        }
        return size;
    }

    @Override
    public File getLocalFile(StreamController.Mp3File mp3File) {
        return new File(root, mp3File.getName());
    }


    public long size(StreamController.Mp3File file) {
        long size = new File(root + file.getName()).length();
        if (file.getEndOffset() < size) {
            size = file.getEndOffset();
        }
        return size - file.getStartOffset();
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public int getThrottleLimit() {
        return throttleLimit;
    }

    public void setThrottleLimit(int throttleLimit) {
        this.throttleLimit = throttleLimit;
    }
}
