package org.opencompare;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;


import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class WikipediaMatrixAnalysisTest {
	List<File> files = new ArrayList<>();
	PcmInspector pcmInspectorTest;
	private static FilenameFilter pcmlFileFilter = (dir, name) -> name.endsWith(".pcm");

	@BeforeAll
	public void chargePcmToTest(){

		//files = (List<File>) PcmUtils.getPCMFiles(new File("pcms/"));
		File repertoire = new File("pcms/");
		Collections.addAll(files, repertoire.listFiles(pcmlFileFilter));


	}

	@Test
	public void testgetFeatureFrequeancies() {
		pcmInspectorTest = new PcmInspector();
		System.out.println(files.size());
		for (File file:files) {
			File pcmFile = null;
			try {
				pcmFile = new File(file.getCanonicalPath());
				PCMLoader loader = new KMFJSONLoader();
				List<PCMContainer> pcmContainers = loader.load(pcmFile);
				for (PCMContainer pcmContainer : pcmContainers) {
					PCM pcm = pcmContainer.getPcm();
					pcmInspectorTest.getFeatureFrequeancies(pcm);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}


		}
		HashMap<String, Integer> frequenciesFeatures = pcmInspectorTest.frequenciesFeatures;
		//assertEquals(1,frequenciesFeatures.get("DVR").intValue());
	}
	@Test
	public void testgetProoductsAndCellFrequeancies() {
		pcmInspectorTest = new PcmInspector();
		System.out.println(files.size());
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
