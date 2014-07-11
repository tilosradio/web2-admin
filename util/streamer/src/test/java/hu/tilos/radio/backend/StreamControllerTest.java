package hu.tilos.radio.backend;


import hu.tilos.radio.backend.streamer.LocalBackend;
import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.Segment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class StreamControllerTest {

    public static SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmm");

    @Test
    public void testDoGet() throws IOException, ServletException {
        //given
        StreamController controller = new StreamController() {
            @Override
            public ResourceCollection getMp3Links(Date start, int duration) {
                ResourceCollection c = new ResourceCollection();
                c.add(new StreamController.Mp3File("/a.txt"));
                c.add(new StreamController.Mp3File("/b.txt"));
                return c;
            }
        };
        controller.setBackend(new LocalBackend("src/test/resources/"));
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);

        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getRequestURI()).thenReturn("/mp3/tilos-20120405-100000-120000.mp3");

        new File("target").mkdirs();
        //when
        controller.doGet(req, resp, new FileOutputStream("target/test.out"));

        //then


    }

    @Test
    public void testDoGetPartial() throws IOException, ServletException {
        //given
        StreamController controller = new StreamController() {
            @Override
            public ResourceCollection getMp3Links(Date start, int duration) {
                ResourceCollection c = new ResourceCollection();
                c.add(new StreamController.Mp3File("/a.txt"));
                c.add(new StreamController.Mp3File("/b.txt"));
                return c;
            }
        };
        controller.setBackend(new LocalBackend("src/test/resources/"));
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        Mockito.when(req.getRequestURI()).thenReturn("/mp3/tilos-20120405-100000-120000.mp3");
        Mockito.when(req.getHeader("Range")).thenReturn("bytes=18-");

        new File("target").mkdirs();
        //when
        controller.doGet(req, resp, new FileOutputStream("target/test.out"));

        //then


    }

    @Test
    public void testParse() throws ParseException {
        //given
        StreamController controller = new StreamController();
        //when
        StreamController.Segment segment = controller.parse("/mp3/tilos-20131012-200000-230000.mp3");

        //then
        Assert.assertEquals(SDF.parse("201310122000"), segment.start);
        Assert.assertEquals(180, segment.duration);
    }

    @Test
    public void testParseOldFormat() throws ParseException {
        //given
        StreamController controller = new StreamController();
        //when
        StreamController.Segment segment = controller.parse("/mp3/1404763200-135.mp3");

        //then
        Assert.assertEquals(SDF.parse("201407072200"), segment.start);
        Assert.assertEquals(135, segment.duration);
    }

    @Test
    public void testGetPrevHalfHour() throws Exception {
        //given
        StreamController controller = new StreamController();
        Date start = SDF.parse("201406011234");
        start.setTime(start.getTime() + 100);
        //when
        Date date = controller.getPrevHalfHour(start);

        //then
        Assert.assertEquals(SDF.parse("201406011230"), date);

    }

    @Test
    public void testGetPrevHalfHourExact() throws Exception {
        //given
        StreamController controller = new StreamController();
        Date start = SDF.parse("201406011230");
        //when
        Date date = controller.getPrevHalfHour(start);

        //then
        Assert.assertEquals(SDF.parse("201406011230"), date);

    }

    @Test
    public void testGetPrevHalfHourExactHour() throws Exception {
        //given
        StreamController controller = new StreamController();
        Date start = SDF.parse("201406011200");
        //when
        Date date = controller.getPrevHalfHour(start);

        //then
        Assert.assertEquals(SDF.parse("201406011200"), date);

    }

    @Test
    public void testGetPrevHalfHour2() throws Exception {
        //given
        StreamController controller = new StreamController();
        Date start = SDF.parse("201406011229");
        start.setTime(start.getTime() + 100);
        //when
        Date date = controller.getPrevHalfHour(start);

        //then
        Assert.assertEquals(SDF.parse("201406011200"), date);

    }

    @Test
    public void stream() throws ParseException {
        StreamController controller = new StreamController();
        StreamController.ResourceCollection resources = controller.getMp3Links(SDF.parse("201406041005"), 90);
        for (StreamController.Mp3File f : resources.getCollection()) {
            System.out.println(f.getName());
        }
    }


}