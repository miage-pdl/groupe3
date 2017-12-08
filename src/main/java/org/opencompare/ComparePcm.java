package org.opencompare;

import org.opencompare.api.java.*;

import java.util.*;

public class ComparePcm {



    /*
    mision : 1 Compare two pcm by her features{
        2 list and give pourcent of similarity }
     */

    private HashMap<Feature, Integer> featuresPCMA = new HashMap<>();
    private HashMap<Feature, Integer> featuresPCMB = new HashMap<>();

    private HashMap<Feature, Integer> featuresPCMAB = new HashMap<>();

    /*
    recuperation des pcn a comparer faire une fonction pour ca apres
     */




    /*
    recuperation des features
     */


    public void compareFeature(PCM pcmA, PCM pcmB) {

        featuresPCMAB.clear();
        int ei = 0;

        Boolean Aissmall = true;
        for (Feature feature : pcmA.getConcreteFeatures()) {
            featuresPCMA.put(feature, 0);
        }
        for (Feature feature : pcmB.getConcreteFeatures()) {
            featuresPCMB.put(feature, 0);
        }
        if (featuresPCMA.size() >= featuresPCMB.size()) {
            Aissmall = false;
            for (Feature feature : featuresPCMB.keySet()) {
                if (featuresPCMA.computeIfPresent(feature, (key, oldVal) -> oldVal + 1) != null) {
                    featuresPCMAB.putIfAbsent(feature, 0);
                    featuresPCMAB.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
                    featuresPCMB.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
                }
            }
        } else {
            for (Feature feature : featuresPCMA.keySet()) {
                if (featuresPCMB.computeIfPresent(feature, (key, oldVal) -> oldVal + 1) != null) {
                    featuresPCMAB.putIfAbsent(feature, 0);
                    featuresPCMAB.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
                    featuresPCMA.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
                }
            }
        }

        // Affichage des resultat
        if (Aissmall) {
            for (int i = 0; i < featuresPCMB.size() + 30; i++) {
                System.out.print("__");
            }
        } else {
            for (int i = 0; i < featuresPCMA.size() + 30; i++) {
                System.out.print("__");
            }
        }
        System.out.println("");

        System.out.print("| Features of PCM A : Sieze A = " + featuresPCMA.size() + " => ");
        for (Feature feature : featuresPCMA.keySet()) {
            if (featuresPCMA.get(feature) == 0) {
                ei++;
                System.out.print(feature + " | ");
            }

        }
        System.out.println("");
        System.out.println(" Count = " + ei);
        ei = 0;
        if (Aissmall) {
            for (int i = 0; i < featuresPCMB.size() + 30; i++) {
                System.out.print("__");
            }
        } else {
            for (int i = 0; i < featuresPCMA.size() + 30; i++) {
                System.out.print("__");
            }
        }
        System.out.println("");
        System.out.print("| Features of PCM B : Sieze B = " + featuresPCMB.size() + " => ");
        for (Feature feature : featuresPCMB.keySet()) {
            if (featuresPCMB.get(feature) == 0) {
                ei++;
                System.out.print(feature + " | ");
            }
        }
        System.out.println("");
        System.out.println(" Count = " + ei);

        if (Aissmall) {
            for (int i = 0; i < featuresPCMB.size() + 30; i++) {
                System.out.print("__");
            }
        } else {
            for (int i = 0; i < featuresPCMA.size() + 30; i++) {
                System.out.print("__");
            }
        }
        System.out.println("");
        if (Aissmall) {
            System.out.print("| Features of PCM A & PCM B (A small than B): => ");
            for (Feature feature : featuresPCMAB.keySet()) {
                System.out.print(feature + " -> " + featuresPCMAB.get(feature) + " | ");
            }
        } else {
            System.out.print("| Features of PCM A & PCM B (B small than A) : => ");
            for (Feature feature : featuresPCMAB.keySet()) {
                System.out.print(feature + " -> " + featuresPCMAB.get(feature) + " | ");
            }
        }
        System.out.println("");
        System.out.println(" Count = " + featuresPCMAB.size());
        if (featuresPCMAB.size() == 0 ){
            System.out.println(ConsoleColors.BLUE +" Count = " + featuresPCMAB.size() + " Ces desux PCM n'ont  aucune Correspondence of Feature "+ ConsoleColors.RESET);
        }
        System.out.println("");
        if (Aissmall) {
            for (int i = 0; i < featuresPCMAB.size() + 30; i++) {
                System.out.print("__");
            }
        } else {
            for (int i = 0; i < featuresPCMA.size() + 30; i++) {
                System.out.print("__");
            }
        }


    }

    public void compareProduit(PCM pcmA, PCM pcmB) {
        Boolean AiSmall = true;
        compareFeature(pcmA, pcmB);

        List<Product> productsA = new ArrayList<>();
        List<Product> productsB = new ArrayList<>();

        HashMap<Feature, Integer> featuresPCMA = new HashMap<>();
        HashMap<Feature, Integer> featuresPCMB = new HashMap<>();

        HashMap<Feature, Integer> localfeaturesPCMAB = (HashMap<Feature, Integer>) featuresPCMAB.clone();

        /*
        verifier i les pcm on des features en commun avant de commencer
         */
        if (localfeaturesPCMAB.size() != 0){
        productsA = pcmA.getProducts();
        productsB = pcmB.getProducts();
        System.out.println("");
        System.out.println(" - A pr Sieze = " + productsA.size());
        System.out.println(" - B pr Sieze = " + productsB.size());
        int i1 = 0 ;
        int i2 = 0 ;
        if (productsA.size() >= productsB.size()) {
            AiSmall = false;
            for (Product product:productsB){
                i1++;
                findbreak:for (Product product1: productsA){
                    i2++;
                  if ( compareTwoProduc(i1,i2,product,product1,localfeaturesPCMAB.keySet())){
                      break findbreak;
                  }
                }
                i2 = 0 ;
            }

        }else {
            for (Product product:productsA){
                i1++;
                findbreak:for (Product product1: productsB){
                    i2++;
                    if ( compareTwoProduc(i1, i2, product,product1,localfeaturesPCMAB.keySet())){
                        break findbreak;
                    }
                }
                i2 = 0 ;
            }
        }


        System.out.println("");
        for (Product product1 : pcmA.getProducts()) {
            System.out.println(" - " + product1.getCells() + " - ");
            System.out.println("");
        }


        System.out.println("");
        for (Product product1 : pcmB.getProducts()) {
            System.out.println(" - " + product1.getCells() + " - ");
            System.out.println("");
        }
        }else {
            System.out.println("");
            System.out.println(ConsoleColors.BLUE + " Ces desux PCM n'ont  aucune Correspondence of Product by Feature " + ConsoleColors.RESET);

        }

    }

    public Boolean compareTwoProduc(int i1, int i2, Product productA, Product productB, Set<Feature> features) {
        int i = 0;
/*
pour ameliorer on peut retirer les produits deja trouver
Mais si un produit existe en deux examplaires ??? on fais quoi ??
??? probleme d'unicite des product
 */
        for (Feature feature : features) {
            if (productA.findCell(feature).equals(productB.findCell(feature))) {
                i++;
            }
        }
        if (i != features.size()){
            if (i>=(features.size()/2)){
                System.out.println(ConsoleColors.BLUE + "taux  Correspondence Produit "+ i1+" Produit ="+ i2+" => " + i + " / " + features.size() + ConsoleColors.RESET);
            }else {
                System.out.println(ConsoleColors.RED + "taux  Correspondence Produit "+ i1+" Produit ="+ i2+" => " + i + " / " + features.size() + ConsoleColors.RESET);
            }
        }else {
            System.out.println(ConsoleColors.GREEN+ "taux  Correspondence Produit Parfait "+ i1+" Produit ="+ i2+" => " + i + " / " + features.size() + ConsoleColors.RESET);
            return true ;
        }

        return false ;

    }

}
