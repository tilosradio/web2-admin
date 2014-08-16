package hu.tilos.radio.backend.streamer;

import hu.tilos.radio.backend.StreamController;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class LocalBackend implements Backend {

    private String root = "/home/elek/projects/tilos/archive-files/online";

    /**
     * Limit in Kbyte/sec.
     */
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


    public long size(StreamController.Mp3File file) {
        long size = new File(root + file.getName()).length();
        if (file.getEndOffset() < size) {
            size = file.getEndOffset();
        }
        return size - file.getStartOffset();
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public void setThrottleLimit(int throttleLimit) {
        this.throttleLimit = throttleLimit;
    }
}
