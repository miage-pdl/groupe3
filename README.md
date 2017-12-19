 # Wikipedia Matrix Analysis Group 3

##Objective 
The goal of this project is to help in the understanding of the properties of tabular data  inside Wikipedia.
<br/><br/> With that being said through the analysis of data taken from wikipedia we search to answer a set of questions that will help to achieve our main goal.
<br/><br/>The software here in this repository is the first step to answer all this questions, through the use of frequency counts and percentages, we can generate different outputs in csv extension, that can be used as an input to all kind of sort of statistical methods. 

## Results
Les responses de chaque quest````ion ici!


##Software license
  Apache License version 2.0
  
## Third party libraries 
The following libraries are used for this software:
 <ul>
 <li>
 OpenCompare - Apache Software License version 2.0,used to read the pcm format and get information out of it, like Cell, Features and Products values and types, the libraries are open source.

 <li>
 Apache Commons io - Apache Software License version 2.0, free distribution
 
 <li>
   Apache Commons poi - Apache Software License version 2.0, free distribution
</ul>  

  ## Development Tools
  <ul>
   <li> Maven - Apache Software Licence version 2.0, free distribution
    <li> Intellij
   </ul>  

  
## Software Architecture
The software will be package as .jar file, this jar will be executed from the command line
and will set the csv files outpust in the same root where it's located.

There are 5 classes in the project, the main class is PcmInspector, this class is the entry point
of the software, it istanciates the classes ComparePcm, CountPaurs and PredominatFeature, the fith class
is PcmUtils and its use is only to centralize utility methods.
![alt text](/opencompare.png)
### Project execution 
To execute this project its needed to have a valid Maven distribution installed in the computer.

For get the jar file, it's necessary to go inside the command line to the path where resides the project,
once located there, type the following command **mvn package**. The jar will be located inside the target folder.

It is suggested to copy the jar file and paste it in the project's directory root, if the default folder of PCM will be used, if it's not the case, you can skip the suggetion.

For running the program inside the command line, type the following command  **java -jar WikipediaMatrixAnalysis-0.1.jar**.