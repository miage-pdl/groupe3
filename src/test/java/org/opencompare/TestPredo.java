package org.opencompare;

import org.junit.Test;

import java.io.IOException;

public class TestPredo {



    @Test
    public void test() {
        PredominantFeature predominantFeature = new PredominantFeature() ;
        try {
            predominantFeature.getPredonimantFeatures("");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}