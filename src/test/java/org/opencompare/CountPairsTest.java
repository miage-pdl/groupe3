package org.opencompare;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;

public class CountPairsTest {


    /**
     * Test for verify if two pair of values appeared the way is expected
     * @throws Exception
     */
    @Test
    public void testCountPaire() throws Exception {
        CountPairs countPairs = new CountPairs();
        countPairs.getCountOfPaireValues("pcmTest");
       HashMap<String, Integer> prueba = countPairs.binome;
       assertEquals(new Integer(8),prueba.get('"'+"yes+no"+'"'));
       assertEquals(new Integer(8),prueba.get('"'+"yes+?"+'"'));
    }

}