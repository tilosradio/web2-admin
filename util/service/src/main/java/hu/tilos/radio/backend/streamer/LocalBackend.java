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
        //InputStream is = new ThrottledInputStream(new LimitedInputStream(new CombinedInputStream(streams),startOffset,endPosition),1024*1024*10);
        InputStream is = new LimitedInputStream(new CombinedInputStream(streams), startOffset, endPosition);
        try {
            while ((r = is.read(b)) != -1) {
                out.write(b, 0, r);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
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
        return new File(root + file.getName()).length();
    }

    public void setRoot(String root) {
        this.root = root;
    }
}
