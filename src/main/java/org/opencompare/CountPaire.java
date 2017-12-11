package org.opencompare;


import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMContainer;
import org.opencompare.api.java.Product;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;

import java.io.*;
import java.util.*;

public class CountPaire {

    //hahMap for count binome
    HashMap<String, HashMap<String, Integer>> binomeMaster = new HashMap<>();
    HashMap<String, Integer> binome = new HashMap<>();

    int countPcm = 0 ;

    public void testCountPaire() throws IOException {



        binomeMaster.put("mypaire", binome);

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

                countPcm++ ;
                System.out.println(ConsoleColors.CYAN_BACKGROUND +" <- "+countPcm+" -> New pcm "+ pcm.getConcreteFeatures().size() +ConsoleColors.RESET);
                for (Product product : pcm.getProducts()) {
                    //   System.out.println(ConsoleColors.RED +" size =>  "+ pcm.getConcreteFeatures().size() +ConsoleColors.RESET);
                    for (int i = 0; i < pcm.getConcreteFeatures().size() -1 ; i++) {
                        //     System.out.println(ConsoleColors.BLUE +" poition =  "+ i +ConsoleColors.RESET);
                        for (int j = i + 1; j < pcm.getConcreteFeatures().size(); j++) {
              /*              System.out.print(" <-- " + product.findCell(pcm.getConcreteFeatures().get(i)).getContent()) ;
                            System.out.print(" <> " + product.findCell(pcm.getConcreteFeatures().get(j)).getContent()) ;
                            System.out.print(" --> ") ;
                            System.out.println();*/

                            generalCountCellsBinome(
                                    "mypaire",(
                                            " "+product.findCell(pcm.getConcreteFeatures().get(i)).getContent()
                                                    +" , "+product.findCell(pcm.getConcreteFeatures().get(j)).getContent())
                            );

                        }

                    }
                }


            }
        }
      /*  int i = 0;
        for (String features : mapOfFrequencies.get("frequenciesFeatures").keySet()) {

            System.out.println("<> " + i + " - " + features + " ===> " + frequenciesFeatures.get(features));
            i++;
        }*/
        int i = 0;
        for (String b : binomeMaster.get("mypaire").keySet()) {

            System.out.println("<- "+ i +" -> "+ b.replaceAll("\n","") + " --> Count = " +binome.get(b)  );
            i++;
        }

        System.out.println("Resultat Count Paire : " );
        File fout = new File( "CountPaire.csv");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fout);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        for (String b : binomeMaster.get("mypaire").keySet()) {
            String line = "" +b.replaceAll("\n","") + " , " +binome.get(b)  ;
            bw.write(line);
            bw.newLine();
        }

        bw.close();

    }


    private static FilenameFilter pcmlFileFilter = (dir, name) -> name.endsWith(".pcm");

    /**
     * cree de paire par ligne
     *
     * @param binomme
     * @param content
     */
    public void generalCountCellsBinome(String binomme, String content) {

        binomeMaster.get(binomme).computeIfAbsent(content, val -> 0);
        binomeMaster.get(binomme).computeIfPresent(content, (key, oldVal) -> oldVal + 1);

    }
}
