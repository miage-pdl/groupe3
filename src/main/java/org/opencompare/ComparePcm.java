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
    String namepcmA = "" ;
    String namepcmB = "" ;


    public static final String PCM_OBJECT_NAME = "org.opencompare.api.java.impl.value.";

    private boolean isAll = false ;

    /*
    recuperation des features
     */

    public void compareAll(String directory) throws IOException {
        String iuu =  "nom pcm A , nom pcm A , Nb Features A ,  Nb Features B  ,  Nb Prodcut A  , Nb Prodcut B ,Nb Same Feature A , Nb Same Product , Symilarite " ;
        tabU.put(""+0,iuu);

        isAll = true ;
        List<File> files = new ArrayList<>();

        // Define a file representing a PCM to load

        File repertoire = new File(directory);

        Collections.addAll(files, repertoire.listFiles()  ) ;
       // files = (List<File>) PcmUtils.getPCMFiles(repertoire);


        // Create a loader that can handle the file format
        PCMLoader loader = new KMFJSONLoader();
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
        int ij = 1 ;
        for ( Integer is : Similarite.keySet() ) {
            String u =  nameA.get(is)+" , "+nameB.get(is)+" , "+featuresA.get(is)+" , "+featuresB.get(is)+" , "+productsA.get(is)+" , "+productsB.get(is)+" , "+sameFeatures.get(is)+" , "+sameProducts.get(is) +" , "+ Similarite.get(is) ;
            tabU.put(""+ij,u);
            ij++ ;
        }

        PcmUtils.createFile(tabU, "CompareaAll" );

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
        namepcmA = pcmA.getName() ;
        namepcmB = pcmB.getName() ;
        nameA.put(intt,pcmA.getName());
        nameB.put(intt,pcmB.getName());
        featuresA.put(intt ,""+pcmA.getConcreteFeatures().size());
        featuresB.put(intt ,""+pcmB.getConcreteFeatures().size());

        productsA.put(intt, ""+pcmA.getProducts().size()) ;
        productsB.put(intt, ""+pcmB.getProducts().size()) ;

        compare.put(" Ensemble ",featuresPCMAB.size());



    }

    public void findPcmAndCompare(String directory, String pcm1, String pcm2){
        String iuu =  "nom pcm A , nom pcm A , Nb Features A ,  Nb Features B  ,  Nb Prodcut A  , Nb Prodcut B ,Nb Same Feature A , Nb Same Product , Symilarite " ;
        tabU.put(""+0,iuu);
        List<File> files = new ArrayList<>();
        PCM pcmA = null ;
        PCM pcmB = null ;
        int bt = 0 ;
        boolean a = false ;
        boolean b = false ;

        // Define a file representing a PCM to load

        File repertoire = new File(directory);

        Collections.addAll(files, repertoire.listFiles()  ) ;
        // files = (List<File>) PcmUtils.getPCMFiles(repertoire);


        // Create a loader that can handle the file format
        PCMLoader loader = new KMFJSONLoader();

        // Load the file
        // A loader may return multiple PCM containers depending on the input format
        // A PCM container encapsulates a PCM and its associated metadata
        int i = 0 ;
        for (File pcmFile : files) {
            for (int ipcm = i+1 ; ipcm < files.size() ; ipcm++ ){
               if(!a) {try {
                    PCM pcm = loader.load(pcmFile).get(0).getPcm();
                    if(pcm.getName().equals(pcm1)){
                        pcmA = pcm ;bt++;


                        a=true ;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }}
               if (!b){try {
                    PCM pcm= loader.load(pcmFile).get(0).getPcm();
                    if(pcm.getName().equals(pcm1)){
                        pcmB = pcm ;bt++;

                        b=true ;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }}
            }
        }

       if (bt>=2)try {
            this.compareProduit(pcmA,pcmB);
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
           int ij = 1 ;
           for ( Integer is : Similarite.keySet() ) {
               String u =  nameA.get(is)+" , "+nameB.get(is)+" , "+featuresA.get(is)+" , "+featuresB.get(is)+" , "+productsA.get(is)+" , "+productsB.get(is)+" , "+sameFeatures.get(is)+" , "+sameProducts.get(is) +" , "+ Similarite.get(is) ;
               tabU.put(""+ij,u);
               ij++ ;
           }

           PcmUtils.createFile(tabU, "CompareaAll" );
        } catch (IOException e) {
            e.printStackTrace();
        }


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

                        if ( 0 == compareTwoProducts(i1,i2,product,product1,localfeaturesPCMAB.keySet())){


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
                        if ( 0 == compareTwoProducts(i1, i2, product,product1,localfeaturesPCMAB.keySet())){
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
    public int compareTwoProducts(int i1, int i2, Product productA, Product productB, Set<Feature> features) {
        int i = 0;
        /*
        pour ameliorer on peut retirer les produits deja trouver
        Mais si un produit existe en deux examplaires ??? on fais quoi ??
        ??? probleme d'unicite des product
         */
        for (Feature feature : features) {

            try{
                productA.findCell(feature).getInterpretation().toString() ;
            }catch (NullPointerException e){
                return 2;
            }
            try{
                productB.findCell(feature).getInterpretation().toString() ;
            }catch (NullPointerException e){
                return 2;
            }

            try{
            if (productA.findCell(feature).equals(productB.findCell(feature))) {
                i++;

            }
            }catch (NullPointerException e){

            }
        }
        if (i != features.size()) {
            if (i >= (features.size() / 2)) {
            } else {
            }
        } else {


            return 0;
        }

        return 1;

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
            s1 =""+ productA.findCell(feature).getInterpretation() ;
            ss1 = s1.split("@");
            if (s1.length()>4){
            }
            s2 =""+ productB.findCell(feature).getInterpretation() ;
            ss2 = s2.split("@");
            if (ss2[0]!=null || ss2[0]!="null"){

            }

            if ((s1!=null) || (s2!=null)){
                if (
                        (productA.findCell(feature).getInterpretation())
                                ==(
                                (productB.findCell(feature).getInterpretation()))) {
                    i++;
                }
            }
        }

        return false;

    }


}
