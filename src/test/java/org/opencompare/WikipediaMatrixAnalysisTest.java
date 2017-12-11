package org.opencompare;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
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
	@BeforeAll
	public void chargePcmToTest(){

		files = (List<File>) PcmUtils.getPCMFiles(new File("/pcmTest/"));

	}

	@Test
	public void testgetFeatureFrequeancies() {
		pcmInspectorTest = new PcmInspector();
		files = (List<File>) PcmUtils.getPCMFiles(new File("pcmTest"));
		for (File file:files) {
			File pcmFile = null;
			try {
				pcmInspectorTest.intialiceMaps();
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
		assertEquals(1,frequenciesFeatures.get("dvr").intValue());
	}
	@Test
	public void testgetProoductsAndCellFrequeancies() {
		pcmInspectorTest = new PcmInspector();
		for (File file:files) {
			File pcmFile = null;
			try {
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
		HashMap<String, Integer> frequenciesFeatures = pcmInspectorTest.frequenciesFeatures;
	}

}
