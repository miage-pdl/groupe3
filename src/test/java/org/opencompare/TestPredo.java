package org.opencompare;

import org.junit.Test;

import java.io.IOException;

public class TestPredo {



    @Test
    public void test() {
        PredominantF predominantF = new PredominantF() ;
        try {
            predominantF.prédominantF();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}