package apriori;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.firebase.client.*;
import java.util.HashMap;
import java.util.Map;

public class ARFModel {
   /* public static void main(String... args){
        
        Integer[] array = {1, 2, 3, 4, 5};
        Integer[] list1 = {2, 4 ,3};
        Integer[] list2 = {1, 5, 2};
        ARFModel mod = new ARFModel(Arrays.asList(array));
        mod.addColumn(Arrays.asList(list1));
        mod.addColumn(Arrays.asList(list2));
        System.out.println(mod);
        
        System.out.println(mod.createTMPFile().canWrite());
        while(true){
        }
    }*/
    List<Integer> attributes = new ArrayList<>();
    String model;
    public ARFModel(List<Integer> attributes){
        
        String tmpModel = "@relation dados\n";
        for (Integer at : attributes) {
            tmpModel += "@attribute " + "at" +  at.toString() + " { !}\n";
            this.attributes.add(at);
        }
        tmpModel+="@data";
        this.model = tmpModel;
        
        
    }
    
    public File createTMPFile(){
        File temp = null;
        try{

    	   //create a temp file
           temp = File.createTempFile("ARFFdados", ".arff");

    	   System.out.println("Temp file : " + temp.getAbsolutePath());
           FileWriter fileWriter = new FileWriter(temp, true);
           System.out.println(temp.getName());
           BufferedWriter bw = new BufferedWriter(fileWriter);
           System.out.println(temp.getName());
           bw.write(model);
           
           bw.close();
    	}catch(IOException e){
            System.out.println(e);
    	}
        return temp;
    }
    public void addColumn(List<Integer> column){
        if(column.isEmpty()){
            return;
        }
        model += "\n";
        for (int i = 0; i < attributes.size(); i++) {
                String tmp = (i == 0) ? "" : ","; 
                if(column.contains(this.attributes.get(i))){
                    model += tmp + "!";
                }
                else{
                    model += tmp + "?";
                }
        }
    }
    
    @Override
    public String toString(){
        return this.model;
    }
    
    
     public static void main(String... args) throws InterruptedException{
        
        FirebaseThread t = new FirebaseThread();
        t.start();

    }
    
    
}
