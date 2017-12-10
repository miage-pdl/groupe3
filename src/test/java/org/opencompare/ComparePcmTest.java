package org.opencompare;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import org.opencompare.api.java.*;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;

public class ComparePcmTest {



    //PCM
    File pcmFileA = new File("pcms/Comparison_of_file_comparison_tools_2.pcm");
    File pcmFileB = new File("pcms/List_of_Nvidia_graphics_processing_units_29.pcm");
    File pcmFileC = new File("pcms/List_of_Nvidia_graphics_processing_units_35.pcm");
    File pcmFileD = new File("pcms/List_of_Nvidia_graphics_processing_units_34.pcm");
    File pcmFileE = new File("pcms/List_of_Nvidia_graphics_processing_units_39.pcm");
    File pcmFileF = new File("pcms/Comparison_of_file_comparison_tools_2.pcm");
    File pcmFileG = new File("pcms/Comparison_of_VoIP_software_1.pcm");


    PCM pcmA = null;
    PCM pcmB = null;

    PCMLoader loader = new KMFJSONLoader();

    @Test
    public void compareFeature() throws Exception {
        try {
            pcmA = loader.load(pcmFileB).get(0).getPcm();
            pcmB = loader.load(pcmFileC).get(0).getPcm();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ComparePcm comparePcm = new ComparePcm();
        comparePcm.compareFeature(pcmA,pcmB);

    }

    @Test
    public void compareProduit() throws Exception {
        try {
            pcmA = loader.load(pcmFileD).get(0).getPcm();
            pcmB = loader.load(pcmFileE).get(0).getPcm();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ComparePcm comparePcm = new ComparePcm();
        comparePcm.compareProduit(pcmA,pcmB);

    }



}