package org.opencompare;

import org.opencompare.api.java.*;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

public class ComparePcm {



    /*
    mision : 1 Compare two pcm by her features{
        2 list and give pourcent of similarity }
     */

    HashMap<Feature, Integer> featuresPCMA = new HashMap<>();
    HashMap<Feature, Integer> featuresPCMB = new HashMap<>();

    /*
    recuperation des pcn a comparer faire une fonction pour ca apres
     */




    /*
    recuperation des features
     */


    public void compareFeature(PCM pcmA,PCM pcmB){


    int ei = 0 ;

        Boolean Aissmall = true ;
        for (Feature feature : pcmA.getConcreteFeatures() ) {
            featuresPCMA.put(feature,0) ;
        }
        for (Feature feature : pcmB.getConcreteFeatures() ) {
            featuresPCMB.put(feature,0) ;
        }
        if (featuresPCMA.size() >= featuresPCMB.size() ){
            Aissmall = false ;
            for (Feature feature : featuresPCMB.keySet() ){
                featuresPCMA.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
                featuresPCMB.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
            }
        }else {
            for (Feature feature : featuresPCMA.keySet() ){
                featuresPCMB.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
                featuresPCMA.computeIfPresent(feature, (key, oldVal) -> oldVal + 1);
            }
        }

        // Affichage des resultat
        if (Aissmall){
            for (int i = 0 ; i< featuresPCMB.size()+30 ; i++){
                System.out.print("__");
            }
        }else {
            for (int i = 0 ; i< featuresPCMA.size()+30 ; i++){
                System.out.print("__");
            }
        }
        System.out.println("");

        System.out.print("| Features of PCM A : => " );
        for (Feature feature : featuresPCMA.keySet() ){
            if (featuresPCMA.get(feature) ==  0 ){
                ei++ ;
                System.out.print( feature + " | ");
            }

        }
        System.out.println(" Count = " + ei);
        ei = 0 ;
        if (Aissmall){
            for (int i = 0 ; i< featuresPCMB.size()+30 ; i++){
                System.out.print("__");
            }
        }else {
            for (int i = 0 ; i< featuresPCMA.size()+30 ; i++){
                System.out.print("__");
            }
        }
        System.out.println("");
        System.out.print("| Features of PCM B : => " );
        for (Feature feature : featuresPCMB.keySet() ){
            if (featuresPCMB.get(feature) ==  0 ){
                ei++;
                System.out.print( feature + " | ");
            }
        }
        System.out.println(" Count = " + ei);
        ei = 0 ;
        if (Aissmall){
            for (int i = 0 ; i< featuresPCMB.size()+30 ; i++){
                System.out.print("__");
            }
        }else {
            for (int i = 0 ; i< featuresPCMA.size()+30 ; i++){
                System.out.print("__");
            }
        }
        System.out.println("");
        if (Aissmall){
            System.out.print("| Features of PCM A & PCM B : => " );
            for (Feature feature : featuresPCMB.keySet() ){
                if (featuresPCMB.get(feature) ==  1 ){
                    ei ++ ;
                    System.out.print( feature + " | ");
                }
            }
        }else {
            System.out.print("| Features of PCM A & PCM B : => " );
            for (Feature feature : featuresPCMA.keySet() ){
                if (featuresPCMA.get(feature) ==  1 ){
                    ei++ ;
                    System.out.print( feature + " | ");
                }
            }
        }
        System.out.println("");
        System.out.println(" Count = " + ei);
        System.out.println("");
        if (Aissmall){
            for (int i = 0 ; i< featuresPCMB.size()+30 ; i++){
                System.out.print("__");
            }
        }else {
            for (int i = 0 ; i< featuresPCMA.size()+30 ; i++){
                System.out.print("__");
            }
        }


    }



}
