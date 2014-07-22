package hu.tilos.radio.backend.episode;

import hu.tilos.radio.backend.data.EpisodeData;

import java.util.*;

public class Merger {

    public List<EpisodeData> merge(List<EpisodeData> first, List<EpisodeData> second) {
        List<EpisodeData> result = new ArrayList<>();
        result.addAll(first);
        result.addAll(second);
        Collections.sort(result, new Comparator<EpisodeData>() {
            @Override
            public int compare(EpisodeData o1, EpisodeData o2) {
                int val = Long.valueOf(o1.getPlannedFrom()).compareTo(Long.valueOf(o2.getPlannedFrom()));
                if (val != 0) return val;
                if (o1.isPersistent() != o2.isPersistent()) {
                    if (o1.isPersistent()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }

                return 0;
            }
        });

        EpisodeData prev = null;
        Iterator<EpisodeData> it = result.iterator();
        while (it.hasNext()) {
            EpisodeData curr = it.next();
            if (prev != null && prev.getPlannedFrom() == curr.getPlannedFrom()) {
                it.remove();
            }
            prev = curr;
        }
        return result;
    }
}
