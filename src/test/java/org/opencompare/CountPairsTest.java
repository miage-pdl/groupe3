package org.opencompare;


import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class CountPairsTest {
    @Test
    public void testCountPaire() throws Exception {
        CountPairs countPairs = new CountPairs();
        countPairs.getCountOfPaireValues("pcmTest");
       HashMap<String, Integer> prueba = countPairs.binome;
    }

    @Test
    public void generalCountCellsBinome() throws Exception {
    }

}