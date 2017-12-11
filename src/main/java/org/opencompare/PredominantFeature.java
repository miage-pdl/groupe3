package org.opencompare;


import org.opencompare.api.java.*;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;

import java.io.*;
import java.util.*;

public class PredominantFeature {




    ArrayList<HashMap<String, HashMap<String, Integer>>> allPredo = new ArrayList<>();
    HashMap<String, Integer> typeMape = new HashMap<>();

    HashMap<String, HashMap<String, Integer>> predo = new HashMap<>();
    HashMap<String, Integer> type = new HashMap<>();

    public static final String PCM_OBJECT_NAME = "org.opencompare.api.java.impl.value.";

    int countPcm = 0;


    public void  getPredonimantFeatures(String directory) throws IOException {


        List<File> files = new ArrayList<>();

        // Define a file representing a PCM to load

        File repertoire = new File(directory);

        // Collections.addAll(files, repertoire.listFiles()  ) ;
        files = (List<File>) PcmUtils.getPCMFiles(repertoire);


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

                    predo.put(feature.getName(), (HashMap<String, Integer>) type.clone());


                    allPredo.add((HashMap<String, HashMap<String, Integer>>) predo.clone());

                    type.clear();
                    predo.clear();

                }


            }
        }
        type.clear();
        affiche();
    }

    private void affiche() throws IOException {

        System.out.println("Resultat Similarite: " );
        File fout = new File( "typePredoFeatures.csv");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fout);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));



        for (HashMap<String, HashMap<String, Integer>> hashMapHashMap : allPredo) {

            String featuretab = hashMapHashMap.keySet().toString();
        //   printA(featuretab.substring(1,featuretab.length()-1));
            if (null != hashMapHashMap.values() ){

                Collection<HashMap<String, Integer>> typss =  hashMapHashMap.values();
                for (HashMap<String, Integer> typs : typss) {

                    String line = ""+ featuretab.substring(1,featuretab.length()-1) ;

                    for (String s:typs.keySet()){
                      line += " , "+s+" , "+ typs.get(s);
                        //printA(", "+s+" , "+typs.get(s));
                    }
                    bw.write(line);
                    bw.newLine();


                }
                type.clear();
            }
            System.out.println("");



        }
        bw.close();
    }
    /**
     * cree de paire par ligne
     *
     * @param content
     */
    public void generalCountCellsType( String content) {

        type.computeIfAbsent(content, val -> 0);
        type.computeIfPresent(content, (key, oldVal) -> oldVal + 1);


    }

}


