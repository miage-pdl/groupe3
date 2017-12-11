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

    int countPcm = 0 ;

    public void getCountOfPaireValues(String directory) throws IOException {



        binomeMaster.put("mypaire", binome);

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

                countPcm++ ;
                System.out.println(ConsoleColors.CYAN_BACKGROUND +" <- "+countPcm+" -> New pcm "+ pcm.getConcreteFeatures().size() +ConsoleColors.RESET);
                for (Product product : pcm.getProducts()) {
                    for (int i = 0; i < pcm.getConcreteFeatures().size() -1 ; i++) {

                        for (int j = i + 1; j < pcm.getConcreteFeatures().size(); j++) {

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
        System.out.println("Resultat Similarite: " );
        File fout = new File( "CounPaire.csv");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fout);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        int i = 0;
        for (String b : binomeMaster.get("mypaire").keySet()) {

            String line  =  b + " , " + binome.get(b) ;
            bw.write(line);
            bw.newLine();
            System.out.println("<- "+ i +" -> "+ b.replaceAll("\n","") + " --> Count = " +binome.get(b)  );
            i++;
        }

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
