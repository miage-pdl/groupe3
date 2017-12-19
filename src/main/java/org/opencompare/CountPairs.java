package org.opencompare;

import java.io.*;

import java.util.*;

import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;

/**
 * Class to analyze pair-to-pair the cell values, the products and features
 *
 * @author Group #3 PDL
 * @version 1.0
 * @since 2017-10-04
 */
public class CountPairs {

    // hahMap for count binome
    HashMap<String, HashMap<String, Integer>> binomeMaster = new HashMap<>();
    HashMap<String, Integer> binome = new HashMap<>();
    HashMap<String, Integer> binomeAux = new HashMap<>();
    int countPcm = 0;

    /**
     * Add the number of times a pair of values has appeared in the inout data set
     * @param keyA Key to compare with the second key (key pair-to-pair comparison)
     * @param keyB Key to compare with the first key (key pair-to-pair comparison)
     */
    public void generalCountCellsBinome(String keyA, String keyB) {
        if (!binome.containsKey('"' + keyA.trim() + "+" + keyB.trim()+'"') &&!binome.containsKey('"' + keyB.trim() + "+" + keyA.trim()+'"')) {
            binome.computeIfAbsent('"'+keyA.trim() + "+" + keyB.trim()+'"', val -> 1);
        } else if (binome.containsKey('"'+keyA.trim() + "+" + keyB.trim()+'"') || binome.containsKey('"'+keyB.trim() + "+" + keyA.trim()+'"')) {
            if (binome.containsKey('"'+keyA.trim() + "+" + keyB.trim()+'"')) {
                binome.computeIfPresent('"'+keyA.trim() + "+" + keyB.trim()+'"', (key, oldVal) -> oldVal + 1);
            } else if (binome.containsKey('"'+keyB.trim() + "+" + keyA.trim()+'"')) {
                binome.computeIfPresent('"'+keyB.trim() + "+" + keyA.trim()+'"', (key, oldVal) -> oldVal + 1);
            }
        }
    }

    /**
     * Counts the times of a pair of values appears in a colummn, if the pairs has already appeared the method does not count that pair because is already counted
     * @param content Content of the cell (cell pair-to-pair comparison)
     * @param value Content of a different cell (cell pair-to-pair comparison)
     */
    public void generalCountCellsBinomeAux(String content, String value) {
        if (!binomeAux.containsKey(content.trim() + "~" + value) &&!binomeAux.containsKey(value.trim() + "~" + content.trim())) {
            binomeAux.computeIfAbsent(content.trim() + "~" + value.trim(), val -> 0);
        }
    }

    /**
     * Main method for the count pairs values in columns
     * @param directory Path 
     * @throws IOException when the files are not accessibles
     */
    public void getCountOfPaireValues(String directory) throws IOException {
        binomeMaster.put("mypaire", binome);

        // Define a file representing a PCM to load
        File repertoire = new File(directory);
        List<File> files = (List<File>) PcmUtils.getPCMFiles(repertoire);

        // Create a loader that can handle the file format
        PCMLoader loader = new KMFJSONLoader();

        // Load the file
        // A loader may return multiple PCM containers depending on the input format
        // A PCM container encapsulates a PCM and its associated metadata
        for (File pcmFile : files) {
            List<PCMContainer> pcmContainers = loader.load(pcmFile);

            for (PCMContainer pcmContainer : pcmContainers) {

                // Get the PCM
                PCM pcm = pcmContainer.getPcm();

                // Browse the cells of the PCM
                countPcm++;

                for (int p = 0; p < 1; p++) {
                    for (int i = 0; i < pcm.getConcreteFeatures().size(); i++) {
                        for (int j = 1; j < pcm.getProducts().size(); j++) {
                            if (pcm.getProducts().get(p).findCell(pcm.getConcreteFeatures().get(i)) != null) {
                                if (!pcm.getProducts()
                                        .get(p)
                                        .findCell(pcm.getConcreteFeatures().get(i))
                                        .getContent()
                                        .toLowerCase()
                                        .trim()
                                        .contentEquals(pcm.getProducts()
                                                          .get(j)
                                                          .findCell(pcm.getConcreteFeatures().get(i))
                                                          .getContent()
                                                          .toLowerCase()
                                                          .trim())) {
                                    generalCountCellsBinomeAux(pcm.getProducts()
                                                                  .get(p)
                                                                  .findCell(pcm.getConcreteFeatures().get(i))
                                                                  .getContent()
                                                                  .toLowerCase().trim().replace("\n", "").replace("\r",""),
                                                               pcm.getProducts()
                                                                  .get(j)
                                                                  .findCell(pcm.getConcreteFeatures().get(i))
                                                                  .getContent()
                                                                  .toLowerCase().trim().replace("\n", "").replace("\r",""));
                                }
                            }
                        }

                        for (String s : binomeAux.keySet()) {
                            String[] keys = s.split("~");

                            if (keys.length == 1) {
                                String[] temporal = new String[keys.length + 1];

                                temporal[0] = keys[0];
                                temporal[1] = "null";
                                keys = temporal;
                            }

                            generalCountCellsBinome(keys[0], keys[1].replace("\n", "").replace("\r",""));
                        }

                        binomeAux.clear();
                    }
                }
            }
        }

        PcmUtils.createFile(binome, "CountPairs");
    }
}
