/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.*;

/**
 *
 * @author rohan
 */
public class ExeterChallenge {

    /**
     * @param args the command line arguments
     */
    static long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
    static long start = System.currentTimeMillis();
    
    static Hashtable <String,String> dict = new Hashtable<>(); // to store the value of french_dcitonary.csv as a key value pair
    static Hashtable <String,Integer> freq = new Hashtable<>();// to store the frquency of occurance of words 
    static FileWriter writerobj;
    static String number_of_times;
    static void count_simiilar() throws Exception
    {
         String line="";  
         FileReader file1 = new FileReader("./src/Test/t8.shakespeare.txt");  
         File myObj = new File("./src/Test/t8.shakespeare.translated.txt");
         myObj.createNewFile();
         FileWriter myWriter = new FileWriter("./src/Test/t8.shakespeare.translated.txt");
         BufferedReader brs = new BufferedReader(file1); 
         while((line = brs.readLine())!= null) //this will help us loop through the document line by line
         {
              String words[] = line.split(" "); // this will seprate line into words
             for(int i =0;i<words.length;i++)
             {
                 if(dict.containsKey((words[i].toLowerCase()).replaceAll("[^a-zA-Z]",""))) // this will check for words that are present in line as well as in my hastable 
                 {
                    if(words[i].charAt(0)>=65 && words[i].charAt(0)<=90) // this whill check if the first character is captial or not 
                     {
                         
                         if(words[i].charAt(1)>=65 && words[i].charAt(1)<=90) // if the first character is captial then check for seconf if it's captial then whole word can be considered captial 
                         {   
                             
                             line=line.replace(words[i], dict.get((words[i].toLowerCase()).replaceAll("[^a-zA-Z]","")).toUpperCase()); // this does the replacement of word in line.
                             
                         }
                         else 
                         {
                             // this String cap conatins the modified french words first charachter as captial
                             String cap = dict.get((words[i].toLowerCase()).replaceAll("[^a-zA-Z]","")).substring(0, 1).toUpperCase()+dict.get(words[i].toLowerCase().replaceAll("[^a-zA-Z]","")).substring(1);
                             
                             line=line.replace(words[i], cap); // this does the replacement of word in line.
                         }
                     }
                     else
                     {
                         line = line.replace(words[i], dict.get((words[i].toLowerCase()).replaceAll("[^a-zA-Z]","")));
                     }
                  
                 }
                 // now we'll add the words and it's frequency in oure 2nd hash table 
                 if(!freq.containsKey((words[i].toLowerCase()).replaceAll("[^a-zA-Z]",""))) // this checks if the words is not present , and if not present it adds it to the table with intial value 1
                 {
                     freq.put((words[i].toLowerCase()).replaceAll("[^a-zA-Z]",""), 1);
                 }
                 else // this modifies the value of frequency 
                 {
                     int val = freq.get((words[i].toLowerCase()).replaceAll("[^a-zA-Z]",""));
                     val++;
                     freq.put((words[i].toLowerCase()).replaceAll("[^a-zA-Z]",""), val);
                     
                 }
             }
              String x = line + System.lineSeparator(); // this line helps us process lines and store it in form of line
              myWriter.append(x);
                    
         }
         create_file_CSV(freq);// calling the function that will  create a frequency.csv file
         myWriter.close();
    }
    static void create_file_CSV(Hashtable <String ,Integer>eng_freq) throws Exception
    {
       
       File myObj = new File("./src/Test/frequency.csv");
       myObj.createNewFile(); //creating new file
       writerobj = new FileWriter("./src/Test/frequency.csv");
         Enumeration<String> e = eng_freq.keys();
         String heading = "English Word, French Word, Frequency"+System.lineSeparator(); //heading for our columns
         writerobj.append(heading); //adding ti to the file
          System.out.println("!----------------List of Unique EnglishWords Replaced with FrenchWord-------------!");
         while (e.hasMoreElements()) {
            String key = e.nextElement();
            if(dict.get((key.toLowerCase()).replaceAll("[^a-zA-Z]",""))!=null)
            {
            String csv = key +", "+dict.get((key.toLowerCase()).replaceAll("[^a-zA-Z]",""))+", "+eng_freq.get(key)+System.lineSeparator();
            writerobj.append(csv);
           
            System.out.println(key);
            number_of_times+= key+" --> "+eng_freq.get(key)+System.lineSeparator();
            }
         }
         System.out.println("!---------------Number Of Times a word was Replaced-------------!");
         System.out.println(number_of_times);
      writerobj.close();
    }
    static void create_performance() throws Exception
    {
        File PerObj = new File("./src/Test/performance.txt");
        PerObj.createNewFile();
        writerobj = new FileWriter("./src/Test/performance.txt");
        long end = System.currentTimeMillis();
        long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        int sec = (int)(end - start)/1000;
        int min = (int)sec/60;
        long actual_mem_used =(afterUsedMem-beforeUsedMem)/1000000;
        System.out.println("Time to process: "+min+" minutes "+sec+" seconds"+System.lineSeparator());
        System.out.println("Memory used:"+actual_mem_used +" MB");
        writerobj.append("Time to process: "+min+" minutes "+sec+" seconds"+System.lineSeparator());
        writerobj.append("Memory used:"+actual_mem_used +" MB");
        writerobj.close();
    }
    public static void main(String[] args) throws Exception {
    
      String line="";  
      String line1 = "";  
      String splitBy = ",";  
      FileReader file = new FileReader("./src/Test/french_dictionary.csv");  
      FileReader f1 = new FileReader("./src/Test/find_words.txt");
      BufferedReader br = new BufferedReader(file); 
       BufferedReader brs = new BufferedReader(f1); 
      while ((line1 = br.readLine()) != null && (line = brs.readLine())!=null)   //returns a Boolean value  
       {  
            String[] dic_val = line1.split(splitBy);   
            //System.out.println(dic_val[0]+","+dic_val[1]); //debug statement
            if(dic_val[0].equals(line)) // to store all the words that are present in find_words.txt and french_dictionary
            dict.put(dic_val[0], dic_val[1]); //inserting values to our Hashtable.
      }
      
        count_simiilar(); //calling the function that'll replace the words and count the number of replacements done 
     
      
        create_performance();
        
    }
    
}
