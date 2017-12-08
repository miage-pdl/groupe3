package org.opencompare;

import org.junit.Test;
import org.junit.Test.*;

import java.io.File;
import java.io.IOException;

import org.opencompare.api.java.*;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;

import static org.junit.Assert.*;

public class ComparePcmTest {

    //PCM A
    File pcmFileA = new File("pcms/Comparison_of_file_comparison_tools_2.pcm");
    //PCM B
    File pcmFileB = new File("pcms/example.pcm");

    PCM pcmA = null;
    PCM pcmB = null;

    PCMLoader loader = new KMFJSONLoader();

    @Test
    public void compareFeature() throws Exception {
        try {
            pcmA = loader.load(pcmFileA).get(0).getPcm();
            pcmB = loader.load(pcmFileA).get(0).getPcm();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ComparePcm comparePcm = new ComparePcm();
        comparePcm.compareFeature(pcmA,pcmB);

    }

}