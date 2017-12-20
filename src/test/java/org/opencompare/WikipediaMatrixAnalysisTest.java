package org.opencompare;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class WikipediaMatrixAnalysisTest {
	List<File> files;
	PcmInspector pcmInspectorTest;
	@BeforeEach
	public void chargePcmToTest(){

		files = (List<File>) PcmUtils.getPCMFiles(new File("pcmTest"));

	}

	@Test
	public void testGetFeatureFrequeancies() {
		pcmInspectorTest = new PcmInspector();
		for (File file:files) {
			File pcmFile = null;
			try {
				pcmInspectorTest.intializeMaps();
				pcmFile = new File(file.getCanonicalPath());
				PCMLoader loader = new KMFJSONLoader();
				List<PCMContainer> pcmContainers = loader.load(pcmFile);
				for (PCMContainer pcmContainer : pcmContainers) {
					PCM pcm = pcmContainer.getPcm();
					pcmInspectorTest.getFeatureFrequencies(pcm);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}


		}
		HashMap<String, Integer> frequenciesFeatures = pcmInspectorTest.mapOfFrequencies.get("frequenciesFeatures");
		int result = frequenciesFeatures.get("\"dvr\"").intValue();
		assertEquals(1,result);
	}
	@Test
	public void testGetProoductsAndCellFrequeancies() throws  Exception {
		pcmInspectorTest = new PcmInspector();
		for (File file:files) {
			File pcmFile = null;
			try {
                pcmInspectorTest.intializeMaps();
				pcmFile = new File(file.getCanonicalPath());
				PCMLoader loader = new KMFJSONLoader();
				List<PCMContainer> pcmContainers = loader.load(pcmFile);
				for (PCMContainer pcmContainer : pcmContainers) {
					PCM pcm = pcmContainer.getPcm();
					pcmInspectorTest.getCellandProductFrequencies(pcm);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}


		}
        HashMap<String, Integer> frequenciesCells = pcmInspectorTest.mapOfFrequencies.get("frequenciesCells");
		HashMap <String, Integer> frequenciesProducts = pcmInspectorTest.mapOfFrequencies.get("frequenciesProducts");
		int resultProdut = frequenciesProducts.get("\"power management\"");
		int resultCellsYes = frequenciesCells.get("\"yes\"");
		assertAll("Assertion", () -> assertTrue(83 == resultCellsYes),
                () -> assertTrue(1 ==resultProdut));
	}
    @Test
	public void testGetSizeOfMatrix() throws Exception{
        pcmInspectorTest = new PcmInspector();
        for (File file:files) {
            File pcmFile = null;
            try {
                pcmInspectorTest.intializeMaps();
                pcmFile = new File(file.getCanonicalPath());
                PCMLoader loader = new KMFJSONLoader();
                List<PCMContainer> pcmContainers = loader.load(pcmFile);
                for (PCMContainer pcmContainer : pcmContainers) {
                    PCM pcm = pcmContainer.getPcm();
                    int sizeInt = pcmInspectorTest.getCellandProductFrequencies(pcm) * pcmInspectorTest.getFeatureFrequencies(pcm);
                    String size = Integer.toString(sizeInt);
                    pcmInspectorTest.calculateMatrixSize(pcm.getName(),pcmInspectorTest.getCellandProductFrequencies(pcm),pcmInspectorTest.getFeatureFrequencies(pcm),size);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        HashMap<String, String> sizePcm = pcmInspectorTest.matrixSize;
             assertEquals("16X13", sizePcm.get("Comparison_of_PVR_software_packages - Digital/Personal Video Recorder (DVR/PVR) features"));

    }
}
