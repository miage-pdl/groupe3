package org.opencompare;


import org.opencompare.api.java.*;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

public class PredominantF {




    ArrayList<HashMap<String, HashMap<String, Integer>>> allPredo = new ArrayList<>();
    HashMap<String, Integer> typeMape = new HashMap<>();

    HashMap<String, HashMap<String, Integer>> predo = new HashMap<>();
    HashMap<String, Integer> type = new HashMap<>();

    public static final String PCM_OBJECT_NAME = "org.opencompare.api.java.impl.value.";

    int countPcm = 0;


    public void predominantF() throws IOException {


        ArrayList<File> files = new ArrayList<>();

        // Define a file representing a PCM to load

        File repertoire = new File("pcms/");

        // Collections.addAll(files, repertoire.listFiles()  ) ;
        Collections.addAll(files, repertoire.listFiles(pcmlFileFilter));


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
                System.out.println(ConsoleColors.CYAN_BACKGROUND + " <- " + countPcm + " -> New pcm " + pcm.getConcreteFeatures().size() + ConsoleColors.RESET);

                for (Feature feature : pcm.getConcreteFeatures()) {



                  //  printA(feature.getName());
                    for (Product product : pcm.getProducts()) {
                        try {
                            // Calculate frequencies by type
                            Value vl = product.findCell(feature).getInterpretation();

                            if (vl != null) {
                                generalCountCellsType( vl.getClass().getName().replace(PCM_OBJECT_NAME, ""));
                             //   printA(vl.getClass().getName().replace(PCM_OBJECT_NAME, ""));
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }

                    }
                for (String types : type.keySet()) {
               // printA(types);
              //  printA(type.get(types).toString());
           //     printA(", "+types);
            }

                    predo.put(feature.getName(), (HashMap<String, Integer>) type.clone());


                    allPredo.add((HashMap<String, HashMap<String, Integer>>) predo.clone());

                    type.clear();
                    predo.clear();

                }


            }
        }
        type.clear();
        Affiche();
    }

    private void Affiche() {

        for (HashMap<String, HashMap<String, Integer>> hashMapHashMap : allPredo) {

            String featuretab = hashMapHashMap.keySet().toString();
            printA(featuretab);
            if (null != hashMapHashMap.values() ){
                printA(",=>  " );
                Collection<HashMap<String, Integer>> typss =  hashMapHashMap.values();


                for (HashMap<String, Integer> typs : typss) {
                    //  HashMap<String, Integer> types = it.next();
                    String typeles = String.valueOf(typs);
                    printA(" - " + typeles + " - ");
                /*   for (int i = 0 ; i < it.next().keySet().size() ; i++ ){
              //         printA(" - " + it.next().keySet().spliterator() + " - " );

                   }*/
                }
                type.clear();
            }
            System.out.println("");



        }
    }


    private static FilenameFilter pcmlFileFilter = (dir, name) -> name.endsWith(".pcm");

    /**
     * cree de paire par ligne
     *
     * @param content
     */
    public void generalCountCellsType( String content) {

        type.computeIfAbsent(content, val -> 0);
        type.computeIfPresent(content, (key, oldVal) -> oldVal + 1);


    }

    private void printA(String s) {
        System.out.print("" + s + " ");
    }
}


