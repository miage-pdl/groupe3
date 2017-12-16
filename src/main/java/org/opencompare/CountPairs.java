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
                 for (Product product : pcm.getProducts()) {
                    for (int i = 0; i < pcm.getConcreteFeatures().size() -1 ; i++) {

                        for (int j = i + 1; j < pcm.getConcreteFeatures().size(); j++) {

                            generalCountCellsBinomeAux(
                                            product.findCell(pcm.getConcreteFeatures().get(i)).getContent().toLowerCase()
                                                    +" + "+product.findCell(pcm.getConcreteFeatures().get(j)).getContent().toLowerCase()+" "
                            );
                            for (String s:binomeAux.keySet()){
                                generalCountCellsBinome(s.replaceAll("\n"," "));
                            }
                            binomeAux.clear();

                        }

                    }
                }


            }
        }
          PcmUtils.createFile(binome,"CountPairs");
        }



    /**
     * cree de paire par ligne
     *
     * @param content
     */
    public void generalCountCellsBinome( String content) {

        binome.computeIfAbsent(content, val -> 1);
        binome.computeIfPresent(content, (key, oldVal) -> oldVal + 1);

    }

    public void generalCountCellsBinomeAux( String content) {

        binomeAux.computeIfAbsent(content, val -> 0);


    }
}
