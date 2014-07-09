package hu.tilos.radio.backend;

import hu.tilos.radio.backend.streamer.Backend;
import hu.tilos.radio.backend.streamer.LocalBackend;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StreamController extends HttpServlet {

    private static SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");

    private static SimpleDateFormat FILE_NAME_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmm");

    Backend backend = new LocalBackend();

    private static Pattern RANGE_PATTERN = Pattern.compile("bytes=(\\d+)-(\\d+)?");


    private String serverUrl;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp, resp.getOutputStream());
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp, OutputStream output) throws ServletException, IOException {
        Segment segment = null;
        try {
            segment = parse(req.getRequestURI());
        } catch (ParseException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            output.write(("Error on parsing url pattern" + e.getMessage()).getBytes());
        }
        if (segment.duration > 360) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            output.write(("Too long duration").getBytes());
        }
        try {
            ResourceCollection collection = getMp3Links(segment.start, segment.duration);
            int size = backend.getSize(collection);

            if (req.getRequestURI().endsWith("m3u")) {
                generateM3u(req, resp, segment, size);
                return;
            }

            if (req.getHeader("Range") != null) {
                String range = req.getHeader("Range");
                System.out.println(range);
                Matcher m = RANGE_PATTERN.matcher(range);
                if (m.matches()) {
                    int start = Integer.valueOf(m.group(1));
                    int to = size;
                    if (m.group(2) != null) {
                        to = Integer.parseInt(m.group(2)) + 1;
                    }


                    resp.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                    resp.setHeader("Accept-Ranges", "bytes");
                    resp.setHeader("Content-Length", "" + (to - start)); // The size of the range
                    resp.setHeader("Content-Range", "bytes=" + start + "-" + (to - 1) + "/" + size); // The size of the range
                    backend.stream(collection, start, size, output);
                } else {
                    throw new RuntimeException("Unknown range request");
                }

            } else {
                resp.setHeader("Content-Length", "" + size);
                resp.setHeader("Content-Type", "audio/mpeg");
                resp.setHeader("Content-Disposition", "inline; filename=\"filename.mp3\"");

                resp.setHeader("Accept-Ranges", "bytes");
                backend.stream(collection, 0, size, output);
            }
        } catch (Exception e) {
            resp.setHeader("Content-Length", "" + e.getMessage().length());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            output.write(e.getMessage().getBytes());
            e.printStackTrace();
        }
    }

    private void generateM3u(HttpServletRequest req, HttpServletResponse resp, Segment segment, int size) throws IOException {
        String filename = "tilos-" + FILE_NAME_FORMAT.format(segment.start) + "-" + segment.duration;
        resp.setHeader("Content-Type", "audio/x-mpegurl; charset=utf-8");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + filename + ".m3u\"");
        resp.getOutputStream().write("#EXTM3U\n".getBytes());
        resp.getOutputStream().write(("#EXTINF:" + size + ", Tilos Rádió - " + FILE_NAME_FORMAT.format(segment.start) + "\n").getBytes());
        resp.getOutputStream().write((serverUrl + req.getRequestURI().toString().replaceAll("\\.m3u", ".mp3")).getBytes());

    }

    protected Segment parse(String requestURI) throws ParseException {
        Segment s = new Segment();
        //mp3/20140505/120300/140400.mp3

        Matcher m = Pattern.compile("^/mp3/tilos-(\\d+)-(\\d+)-(\\d+).*$").matcher(requestURI);
        if (m.matches()) {

            s.start = SDF.parse(m.group(1) + m.group(2));
            Date end = SDF.parse(m.group(1) + m.group(3));
            s.duration = Math.round((end.getTime() - s.start.getTime()) / (1000 * 60));
            return s;

        } else {
            m = Pattern.compile("^/mp3/(\\d+)/(\\d+)/(\\d+).*$").matcher(requestURI);
            if (m.matches()) {
                s.start = SDF.parse(m.group(1) + m.group(2));
                Date end = SDF.parse(m.group(1) + m.group(3));
                s.duration = Math.round((end.getTime() - s.start.getTime()) / (1000 * 60));
                return s;
            } else {
                m = Pattern.compile("^/mp3/tilos-(\\d+)-(\\d+).*$").matcher(requestURI);
                if (m.matches()) {
                    s.start = new Date();
                    s.start.setTime(Long.valueOf(m.group(1)));
                    s.duration = Integer.valueOf(m.group(2));
                    return s;
                }
            }
        }

        return null;
    }

    public Date getPrevHalfHour(Date date) {
        Date result = new Date();
        result.setTime(date.getTime() / 1000 * 1000);
        result.setSeconds(0);
        if (result.getMinutes() >= 30) {
            result.setMinutes(30);
        } else {
            result.setMinutes(0);
        }
        return result;
    }

    public ResourceCollection getMp3Links(Date start, int duration) {
        ResourceCollection collection = new ResourceCollection();
        Date from = getPrevHalfHour(start);

        Date end = new Date();
        end.setTime(start.getTime() + 60 * 1000 * duration);

        Date i = new Date();
        i.setTime(from.getTime());
        while (i.compareTo(end) < 0) {

            SimpleDateFormat d = new SimpleDateFormat("'/'yyyy'/'MM'/'dd'/tilosradio-'yyyMMdd'-'HHmm'.mp3'");
            collection.add(new Mp3File(d.format(i)));
            i.setTime(i.getTime() + 60 * 30 * 1000);
        }

        int offset = (int) ((start.getTime() - from.getTime()) / 1000);
        collection.getCollection().get(0).setStartOffset((int) Math.round(offset * 38.28125 * 836));
        return collection;
    }

    public void setBackend(Backend backend) {
        this.backend = backend;
    }

    public static class ResourceCollection {

        private List<Mp3File> collection = new ArrayList();

        public void add(Mp3File mp3File) {
            collection.add(mp3File);
        }

        public List<Mp3File> getCollection() {
            return collection;
        }

        public void setCollection(List<Mp3File> collection) {
            this.collection = collection;
        }


    }

    public static class Mp3File {

        private String name;

        private int startOffset = 0;

        private int endOffset = Integer.MAX_VALUE;

        public Mp3File(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStartOffset() {
            return startOffset;
        }

        public void setStartOffset(int startOffset) {
            this.startOffset = startOffset;
        }

        public int getEndOffset() {
            return endOffset;
        }

        public void setEndOffset(int endOffset) {
            this.endOffset = endOffset;
        }
    }

    public static class Segment {
        Date start;
        int duration;
    }
    public void setServerUrl(String url){
        this.serverUrl = url;
    }

}
