package hu.tilos.radio.backend.streamer;

import hu.tilos.radio.backend.StreamController;

import java.io.OutputStream;
import java.net.MalformedURLException;

public interface Backend {

    public void stream(StreamController.ResourceCollection collection, int startOffset, int endPosition, OutputStream out) throws Exception;

    public int getSize(StreamController.ResourceCollection collection);

}
