package org.opencompare;


import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.Product;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;

import java.io.*;
import java.util.*;

public class CountPairs {

    //hahMap for count binome
    HashMap<String, HashMap<String, Integer>> binomeMaster = new HashMap<>();
    HashMap<String, Integer> binome = new HashMap<>();
    HashMap<String, Integer> binomeAux = new HashMap<>();

    int countPcm = 0 ;

    /**
     * Main method for the count pairs values in columns
     * @param directory
     * @throws IOException
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

                countPcm++ ;
                for (int p = 0; p < 1 ; p++) {
                    for (int i = 0; i <  pcm.getConcreteFeatures().size() ; i++){
                       for (int j = 1; j <  pcm.getProducts().size(); j++) {
                           if (! pcm.getProducts().get(p).findCell(pcm.getConcreteFeatures().get(i)).getContent().toLowerCase().trim().contentEquals(pcm.getProducts().get(j).findCell(pcm.getConcreteFeatures().get(i)).getContent().toLowerCase().trim())) {
                               generalCountCellsBinomeAux(
                                       pcm.getProducts().get(p).findCell(pcm.getConcreteFeatures().get(i)).getContent().toLowerCase()
                                       , pcm.getProducts().get(j).findCell(pcm.getConcreteFeatures().get(i)).getContent().toLowerCase()
                               );
                           }
                        }
                        for (String s : binomeAux.keySet()) {
                            String[] keys = s.split(",");
                            generalCountCellsBinome(keys[0], keys[1]);


                        }
                        binomeAux.clear();
                    }
                }


            }
        }
        PcmUtils.createFile(binome,"CountPairs");
    }


    /**
     * Add the number of times a pair of values has appeared in the inout data set
     * @param keyA
     * @param keyB
     */
    public void generalCountCellsBinome( String keyA, String keyB) {
        if (!binome.containsKey(keyA+"+"+keyB)&&!binome.containsKey(keyB+"+"+keyA)){
            binome.computeIfAbsent(keyA+"+"+keyB, val -> 1);
        }else if(binome.containsKey(keyA+"+"+keyB)||binome.containsKey(keyB+"+"+keyA)){
            if (binome.containsKey(keyA+"+"+keyB)){
                binome.computeIfPresent(keyA+"+"+keyB, (key, oldVal) -> oldVal + 1);
            }else if (binome.containsKey(keyB+"+"+keyA)){
                binome.computeIfPresent(keyB+"+"+keyA, (key, oldVal) -> oldVal + 1);
            }
        }

    }

    /**
     * Counts the times of a pair of values appears in a colummn, if the pairs has already appeared the method does not count that pair because is already counted
     * @param content
     * @param value
     */
    public void generalCountCellsBinomeAux( String content , String value) {
        if (!binomeAux.containsKey(content+","+value)&&!binomeAux.containsKey(value+","+content))
            binomeAux.computeIfAbsent(content +","+value, val -> 0);


    }
}
