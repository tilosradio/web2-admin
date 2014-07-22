package hu.tilos.radio.backend.episode;


import hu.tilos.radio.backend.data.EpisodeData;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MergerTest {

    @Test
    public void testMerge() {
        //given
        Merger m = new Merger();

        List<EpisodeData> a = new ArrayList<>();

        EpisodeData d = new EpisodeData();
        d.setPersistent(true);
        d.setPlannedFrom(100000);
        a.add(d);

        d = new EpisodeData();
        d.setPersistent(true);
        d.setPlannedFrom(200000);
        a.add(d);


        List<EpisodeData> b = new ArrayList<>();
        d = new EpisodeData();
        d.setPersistent(false);
        d.setPlannedFrom(100000);
        b.add(d);

        d = new EpisodeData();
        d.setPersistent(false);
        d.setPlannedFrom(300000);
        b.add(d);


        //when
        List<EpisodeData> result = m.merge(a, b);

        //then
        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.get(0).isPersistent());
        Assert.assertTrue(result.get(1).isPersistent());
        Assert.assertFalse(result.get(2).isPersistent());

        Assert.assertEquals(100000,result.get(0).getPlannedFrom());
        Assert.assertEquals(200000,result.get(1).getPlannedFrom());

    }
}