package org.opencompare;

import java.io.*;

import java.util.*;

import org.opencompare.api.java.*;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;

public class ComparePcm {
    public static final String PCM_OBJECT_NAME = "org.opencompare.api.java.impl.value.";
    private HashMap<Feature, Integer> featuresPCMA = new HashMap<>();
    private HashMap<Feature, Integer> featuresPCMB = new HashMap<>();
    private HashMap<Feature, Integer> featuresPCMAB = new HashMap<>();
    private HashMap<String, Integer> compare = new HashMap<>();
    private String[][] tabUnion = null;
    private boolean isAll = false;

    /**
     * Method to recover the feature from the PCMs
     * @param directory A path directory
     * @throws IOException 
     */
    public void compareAll(String directory) throws IOException {
        isAll = true;

        // Define a file representing a PCM to load
        File repertoire = new File(directory);
        List<File> files = (List<File>) PcmUtils.getPCMFiles(repertoire);

        // Create a loader that can handle the file format
        PCMLoader loader = new KMFJSONLoader();

        // Load the file
        // A loader may return multiple PCM containers depending on the input format
        // A PCM container encapsulates a PCM and its associated metadata
        int i = 0;

        for (File pcmFile : files) {
            for (int ipcm = i + 1; ipcm < files.size(); ipcm++) {
                PCM pcmA = loader.load(pcmFile).get(0).getPcm();
                PCM pcmB = loader.load(files.get(ipcm)).get(0).getPcm();

                compareProduit(pcmA, pcmB);
                compare.clear();
            }

            i++;
        }

        isAll = false;
    }

    /**
     * @param pcmA One of the PCMs to compare
     * @param pcmB A different PCM than pcmA that is compared with the first PCM (pcmA)
     */
    public void compareFeature(PCM pcmA, PCM pcmB) {
        featuresPCMAB.clear();

        int ei = 0;

        for (Feature feature : pcmA.getConcreteFeatures()) {
            featuresPCMA.put(feature, 0);
        }

        for (Feature feature : pcmB.getConcreteFeatures()) {
            featuresPCMB.put(feature, 0);
        }

        if (featuresPCMA.size() >= featuresPCMB.size()) {
            compare.put("Features A > B ", ei);

            for (Feature feature : featuresPCMB.keySet()) {
                if (featuresPCMA.computeIfPresent(feature, (key, oldVal) -> oldVal + 1) != null) {
                    featuresPCMAB.putIfAbsent(feature, 0);
                    featuresPCMAB.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
                    featuresPCMB.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
                }
            }
        } else {
            compare.put("Features A < B ", ei);

            for (Feature feature : featuresPCMA.keySet()) {
                if (featuresPCMB.computeIfPresent(feature, (key, oldVal) -> oldVal + 1) != null) {
                    featuresPCMAB.putIfAbsent(feature, 0);
                    featuresPCMAB.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
                    featuresPCMA.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
                }
            }
        }

        compare.put(" Size A ", featuresPCMA.size());
        compare.put(" Size B ", featuresPCMB.size());

        for (Feature feature : featuresPCMA.keySet()) {
            if (featuresPCMA.get(feature) == 0) {
                ei++;
            }
        }

        compare.put(pcmA.getName(), ei);
        ei = 0;

        for (Feature feature : featuresPCMB.keySet()) {
            if (featuresPCMB.get(feature) == 0) {
                ei++;
            }
        }

        compare.put(pcmB.getName(), ei);
        compare.put("Ensemble ", featuresPCMAB.size());
    }

    /**
     * @param pcmA One of the PCMs to compare
     * @param pcmB A different PCM than pcmA that is compared with the first PCM (pcmA)
     */
    public void compareProduit(PCM pcmA, PCM pcmB) throws IOException {
        compareFeature(pcmA, pcmB);

        List<Product> productsA;
        List<Product> productsB;
        HashMap<Feature, Integer> localfeaturesPCMAB = (HashMap<Feature, Integer>) featuresPCMAB.clone();

        /*
         * verifier i les pcm on des features en commun avant de commencer
         */
        if (localfeaturesPCMAB.size() != 0) {
            productsA = pcmA.getProducts();
            productsB = pcmB.getProducts();

            int i1 = 0;
            int i2 = 0;

            if (productsA.size() >= productsB.size()) {
                compare.putIfAbsent("Product A < B", 0);

                for (Product product : productsB) {
                    compare.putIfAbsent(product.getKeyContent(), 0);
                    i1++;
findbreak:
                    for (Product product1 : productsA) {
                        i2++;

                        if (compareTwoProducts(i1, i2, product, product1, localfeaturesPCMAB.keySet())) {
                            compare.computeIfPresent(product.getKeyContent(), (key, oldVal) -> oldVal + 1);

                            break;    // all analys
                        }
                    }

                    i2 = 0;
                }
            } else {
                compare.putIfAbsent("Product A  > B", 0);

                for (Product product : productsA) {
                    compare.putIfAbsent(product.getKeyContent(), 0);
                    i1++;
findbreak:
                    for (Product product1 : productsB) {
                        i2++;

                        if (compareTwoProducts(i1, i2, product, product1, localfeaturesPCMAB.keySet())) {
                            compare.computeIfPresent(product.getKeyContent(), (key, oldVal) -> oldVal + 1);

                            break;
                        }
                    }

                    i2 = 0;
                }
            }
        }

        for (String s : compare.keySet()) {
            System.out.println("KEy : " + s + " - Value => " + compare.get(s));
        }

        if (!isAll) {
            PcmUtils.createFile(compare, pcmA.getName() + "-" + pcmB.getName());
        }
    }

    /**
     * Compare two products by the type of cell 
     *
     * @param i1
     * @param i2
     * @param productA product A
     * @param productB product B
     * @param features Feature List
     * @return true if exists a correspondence between them
     */
    public Boolean compareTwoProducByTypeOfCell(int i1, int i2, Product productA, Product productB,
                                                Set<Feature> features) {
        int i = 0;

        /*
         * pour ameliorer on peut retirer les produits deja trouver
         * Mais si un produit existe en deux examplaires ??? on fais quoi ??
         * ??? probleme d'unicite des product
         */
        String s1 = "";
        String s2 = "" + null;
        String[] ss1 = null;
        String[] ss2 = null;

        for (Feature feature : features) {
            System.out.println(" features  -> " + feature + " size : " + features.size());
            s1 = "" + productA.findCell(feature).getInterpretation();
            System.out.println(" ?  -> " + s1.length());
            ss1 = s1.split("@");

            if (s1.length() > 4) {
                System.out.println(" ?  -> " + ss1[0]);
                System.out.println(" features  -> " + feature + " type : " + ss1[0].replace(PCM_OBJECT_NAME,
                                                                                            "") + " - " + ss1[1]);
            }

            s2 = "" + productB.findCell(feature).getInterpretation();
            ss2 = s2.split("@");

            if ((ss2[0] != null) || (ss2[0] != "null")) {
                System.out.println(" features  -> " + feature + " type : " + ss2[0].replace(PCM_OBJECT_NAME,
                                                                                            "") + " - " + ss2[1]);
            }

            if ((s1 != null) || (s2 != null)) {
                if ((productA.findCell(feature).getInterpretation())
                        == ((productB.findCell(feature).getInterpretation()))) {
                    i++;
                }
            }
        }

        return false;
    }

    /**
     * Compare two products by the key value for items who share the same feature 
     *
     * @param i1
     * @param i2
     * @param productA product A
     * @param productB product A
     * @param features Feature List
     * @return true if exists a correspondence between them
     */
    public Boolean compareTwoProducts(int i1, int i2, Product productA, Product productB, Set<Feature> features) {
        int i = 0;

        /*
         * pour ameliorer on peut retirer les produits deja trouver
         * Mais si un produit existe en deux examplaires ??? on fais quoi ??
         * ??? probleme d'unicite des product
         */
        for (Feature feature : features) {
            if (productA.findCell(feature) == (productB.findCell(feature))) {
                i++;
            }
        }

        if (i == features.size()) {
            return true;
        }

        return false;
    }
}
