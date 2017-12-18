package org.opencompare;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TestPredo {



    @Test
    public void test() {
        ArrayList<HashMap<String, HashMap<String, Integer>>> predo = null;
        PredominantFeature predominantFeature = new PredominantFeature() ;
        try {
            predominantFeature.getPredonimantFeatures("pcmTest");
             predo = predominantFeature.allPredo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap result = predo.get(0);
        HashMap compare = (HashMap) result.get("MediaPhoenix/ShowShifter");
        assertEquals(10,compare.get("BooleanValueImpl"));

    }

}