package org.opencompare;

import org.opencompare.api.java.*;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;

import java.io.*;
import java.util.*;

public class ComparePcm {



    private HashMap<Feature, Integer> featuresPCMA = new HashMap<>();
    private HashMap<Feature, Integer> featuresPCMB = new HashMap<>();
    private HashMap<Feature, Integer> featuresPCMAB = new HashMap<>();

    private HashMap<String, Integer> compare = new HashMap<>();
    private HashMap<String, Integer> compareVal = new HashMap<>();


    //Hunter
    private HashMap<Integer, String> nameA = new HashMap<>();
    private HashMap<Integer, String> nameB = new HashMap<>();
    private HashMap<Integer, String> featuresA = new HashMap<>();
    private HashMap<Integer, String> featuresB = new HashMap<>();
    private HashMap<Integer, String> productsA = new HashMap<>();
    private HashMap<Integer, String> productsB = new HashMap<>();
    private HashMap<Integer, String> sameFeatures = new HashMap<>();
    private HashMap<Integer, Integer> sameProducts = new HashMap<>();
    private HashMap<Integer, String> Similarite = new HashMap<>();

    private HashMap<String, String> tabU = new HashMap<>();
    private ArrayList<String> tabUnion = new ArrayList<>() ;
    int intt  =  1 ;


    public static final String PCM_OBJECT_NAME = "org.opencompare.api.java.impl.value.";

    private boolean isAll = false ;

    /*
    recuperation des features
     */

    public void compareAll(String directory) throws IOException {
        isAll = true ;
        List<File> files = new ArrayList<>();

        // Define a file representing a PCM to load

        File repertoire = new File(directory);

        Collections.addAll(files, repertoire.listFiles()  ) ;
       // files = (List<File>) PcmUtils.getPCMFiles(repertoire);


        // Create a loader that can handle the file format
        PCMLoader loader = new KMFJSONLoader();
        System.out.println("Sixe = " +files.size());
        // Load the file
        // A loader may return multiple PCM containers depending on the input format
        // A PCM container encapsulates a PCM and its associated metadata
        int i = 0 ;
        for (File pcmFile : files) {
            for (int ipcm = i+1 ; ipcm < files.size() ; ipcm++ ){

                PCM pcmA = loader.load(pcmFile).get(0).getPcm();
                PCM pcmB = loader.load(files.get(ipcm)).get(0).getPcm();
                compareProduit(pcmA,pcmB);

                if (Integer.valueOf(sameFeatures.get(intt)) != 0 ){
                    if (pcmA.getFeatures().size() == pcmB.getFeatures().size() ){
                        if (pcmA.getProducts().size() == pcmB.getProducts().size()){
                            if (Integer.valueOf(sameProducts.get(intt)) == pcmB.getProducts().size() ){
                                Similarite.put(intt , "similary") ;
                            }else {
                                Similarite.put(intt , "different") ;
                            }
                        }else
                        if (pcmA.getProducts().size() > pcmB.getProducts().size()){
                            if (Integer.valueOf(sameProducts.get(intt)) == pcmB.getProducts().size() ){
                                Similarite.put(intt , "B is included in A ") ;
                            }else {
                                Similarite.put(intt , "different") ;
                            }
                        }else {
                            Similarite.put(intt , "different") ;
                        }
                    }else
                        if (pcmA.getFeatures().size() > pcmB.getFeatures().size() ){
                            if (pcmA.getProducts().size() == pcmB.getProducts().size()){
                                if (Integer.valueOf(sameProducts.get(intt)) == pcmB.getProducts().size() ){
                                    Similarite.put(intt , "B is included in A on features of B ") ;
                                }else {
                                    Similarite.put(intt , "different") ;
                                }
                            }else
                            if (pcmA.getProducts().size() > pcmB.getProducts().size()){
                                if (Integer.valueOf(sameProducts.get(intt)) == pcmB.getProducts().size() ){
                                    Similarite.put(intt , "B is included in A ") ;
                                }else {
                                    Similarite.put(intt , "different") ;
                                }
                            }else {
                                Similarite.put(intt , "different") ;
                            }
                    } else
                        if (pcmA.getFeatures().size() < pcmB.getFeatures().size() ){
                            if (pcmA.getProducts().size() <= pcmB.getProducts().size()){
                                if (Integer.valueOf(sameProducts.get(intt)) == pcmA.getProducts().size() ){
                                    Similarite.put(intt , "A is included in B ") ;
                                }else {
                                    Similarite.put(intt , "different") ;
                                }
                            }else {
                                Similarite.put(intt , "different") ;
                            }
                        }


                }else {
                    Similarite.put(intt , "different") ;
                }


                compare.clear();
                intt++;
            }
            i++;
        }
        isAll = false ;
        for ( Integer is : Similarite.keySet() ) {
            System.out.println( is +" , " + nameA.get(is)+" , "+nameB.get(is)+" , "+featuresA.get(is)+" , "+featuresB.get(is)+" , "+productsA.get(is)+" , "+productsB.get(is)+" , "+sameFeatures.get(is)+" , "+sameProducts.get(is) +" , "+ Similarite.get(is));
        }
    }


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

            compare.put("Features A > B ",ei);
            for (Feature feature : featuresPCMB.keySet()) {
                if (featuresPCMA.computeIfPresent(feature, (key, oldVal) -> oldVal + 1) != null) {
                    featuresPCMAB.putIfAbsent(feature, 0);
                    featuresPCMAB.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
                    featuresPCMB.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
                }
            }
        } else {
            compare.put("Features A < B ",ei);
            for (Feature feature : featuresPCMA.keySet()) {
                if (featuresPCMB.computeIfPresent(feature, (key, oldVal) -> oldVal + 1) != null) {
                    featuresPCMAB.putIfAbsent(feature, 0);
                    featuresPCMAB.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
                    featuresPCMA.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
                }
            }
        }
        compare.put(" Size A ",featuresPCMA.size());
        compare.put(" Size B ",featuresPCMB.size());

        for (Feature feature : featuresPCMA.keySet()) {
            if (featuresPCMA.get(feature) == 0) {
                ei++;

            }

        }
        compare.put(pcmA.getName(),ei);
        ei = 0;
        for (Feature feature : featuresPCMB.keySet()) {
            if (featuresPCMB.get(feature) == 0) {
                ei++;
            }
        }
        compare.put(pcmB.getName(),ei);
        nameA.put(intt,pcmA.getName());
        nameB.put(intt,pcmB.getName());
     //   System.out.println("-------------->"+pcmB.getName());
        featuresA.put(intt ,""+pcmA.getConcreteFeatures().size());
        featuresB.put(intt ,""+pcmB.getConcreteFeatures().size());

        productsA.put(intt, ""+pcmA.getProducts().size()) ;
        productsB.put(intt, ""+pcmB.getProducts().size()) ;

        compare.put(" Ensemble ",featuresPCMAB.size());



    }


    public void compareProduit(PCM pcmA, PCM pcmB) throws IOException {

        compareFeature(pcmA, pcmB);

        List<Product> productsA ;
        List<Product> productsB ;


        HashMap<Feature, Integer> localfeaturesPCMAB = (HashMap<Feature, Integer>) featuresPCMAB.clone();
        sameFeatures.put(intt , ""+localfeaturesPCMAB.size() ) ;
        sameProducts.putIfAbsent(intt, 0);
        /*
        verifier i les pcm on des features en commun avant de commencer
         */
        if (localfeaturesPCMAB.size() != 0) {
            productsA = pcmA.getProducts();
            productsB = pcmB.getProducts();
            int i1 = 0;
            int i2 = 0;
            sameProducts.putIfAbsent(intt, 0);
            if (productsA.size() >= productsB.size()) {

                compare.putIfAbsent("Product A < B", 0);
                for (Product product : productsB) {
                    compareVal.putIfAbsent(product.getKeyContent(), 0);
                    i1++;

                    for (Product product1 : productsA) {
                        i2++;

                        if ( compareTwoProducts(i1,i2,product,product1,localfeaturesPCMAB.keySet())){


                            sameProducts.computeIfPresent(intt, (key, oldVal) -> oldVal + 1);

                            compareVal.computeIfPresent(product.getKeyContent(), (key, oldVal) -> oldVal + 1);
                            break; // all analys

                        }
                    }
                    i2 = 0;
                }

            } else {
                compare.putIfAbsent("Product A  > B", 0);
                for (Product product : productsA) {
                    compareVal.putIfAbsent(product.getKeyContent(), 0);
                    i1++;

                    for (Product product1 : productsB) {
                        i2++;
                        if ( compareTwoProducts(i1, i2, product,product1,localfeaturesPCMAB.keySet())){
                            compareVal.computeIfPresent(product.getKeyContent(), (key, oldVal) -> oldVal + 1);


                            sameProducts.computeIfPresent(intt, (key, oldVal) -> oldVal + 1);
                            break;
                        }
                    }
                    i2 = 0;
                }
            }



        }
        String value = "" ;
        for (String s:compare.keySet()){

            value += "" + s +", "+ compare.get(s) + ", " ;

        }

        tabU.put(""+intt,value) ;


        tabUnion.add(value) ;
      if (!isAll) PcmUtils.createFile(compare, pcmA.getName()+"-"+pcmB.getName() );
    }

    /**
     * Compare deux produits par la valeur des cells pour la quelle il partage le meme feature
     *
     * @param i1
     * @param i2
     * @param productA produit A
     * @param productB produit A
     * @param features liste des features
     * @return true si les produit Correspondence
     */
    public Boolean compareTwoProducts(int i1, int i2, Product productA, Product productB, Set<Feature> features) {
        int i = 0;
        /*
        pour ameliorer on peut retirer les produits deja trouver
        Mais si un produit existe en deux examplaires ??? on fais quoi ??
        ??? probleme d'unicite des product
         */
        for (Feature feature : features) {

            if (productA.findCell(feature).equals(productB.findCell(feature))) {
                i++;
             //   System.out.println(" -> " + i);
            }
        }
        if (i != features.size()) {
            if (i >= (features.size() / 2)) {
                // System.out.println(ConsoleColors.BLUE + "taux  Correspondence Produit " + i1 + " Produit =" + i2 + " => " + i + " / " + features.size() + ConsoleColors.RESET);
            } else {
                // System.out.println(ConsoleColors.RED + "taux  Correspondence Produit " + i1 + " Produit =" + i2 + " => " + i + " / " + features.size() + ConsoleColors.RESET);
            }
        } else {
            // System.out.println(ConsoleColors.GREEN + "taux  Correspondence Produit Parfait " + i1 + " Produit =" + i2 + " => " + i + " / " + features.size() + ConsoleColors.RESET);

            return true;
        }

        return false;

    }


    public Boolean compareTwoProducByTypeOfCell(int i1, int i2, Product productA, Product productB, Set<Feature> features) {
        int i = 0;

        /*
        pour ameliorer on peut retirer les produits deja trouver
        Mais si un produit existe en deux examplaires ??? on fais quoi ??
        ??? probleme d'unicite des product
         */
        String s1 = "" ;
        String s2 = ""+null ;
        String[] ss1 = null ;
        String[] ss2 = null ;
        for (Feature feature : features) {
            System.out.println(" features  -> "+feature+" size : " +features.size());
            s1 =""+ productA.findCell(feature).getInterpretation() ;
            System.out.println(" ?  -> "+s1.length());
            ss1 = s1.split("@");
            if (s1.length()>4){
                System.out.println(" ?  -> "+ss1[0]);
                System.out.println(" features  -> "+feature+" type : " +ss1[0].replace(PCM_OBJECT_NAME, "")+ " - "+ss1[1]);
            }
            s2 =""+ productB.findCell(feature).getInterpretation() ;
            ss2 = s2.split("@");
            if (ss2[0]!=null || ss2[0]!="null"){

                System.out.println(" features  -> "+feature+" type : " +ss2[0].replace(PCM_OBJECT_NAME, "")+ " - "+ss2[1]);
            }

            if ((s1!=null) || (s2!=null)){
                if (
                        (productA.findCell(feature).getInterpretation())
                                ==(
                                (productB.findCell(feature).getInterpretation()))) {
                    // System.out.println(i1 + " -> " + productA.findCell(feature).getInterpretation().getClass().getName().replace(PCM_OBJECT_NAME, "") + " | " + i2 + " -> " + productB.findCell(feature).getInterpretation().getClass().getName().replace(PCM_OBJECT_NAME, ""));
                    i++;
                }
            }
        }

        return false;

    }


}
