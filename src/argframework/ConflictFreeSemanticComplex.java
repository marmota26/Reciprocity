/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package argframework;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Marmota
 */
public class ConflictFreeSemanticComplex extends Semantic {
    
    ArrayList<int[]> conflictfree = new ArrayList<int[]>();
    Random rand = new Random();

    public ConflictFreeSemanticComplex(int[][] matrix, int[] activeArray, int argStart) {
        this.matrix = matrix;
        this.activeArray = activeArray;
        this.argStart = argStart;
    }
    
    public int[] checkSemantic(){
        conflictfree = new ArrayList<int[]>();
        //System.out.println("ConflictFree");
        ConflictFreeSets();
        
        //printConflictFree();
        
        sortObjectives();
        chooseConflictFree();
        
        //System.out.println("Qtd de conflictFree: " + conflictfree.size());
        //printConflictFree();
        
        return returnedSemantic;
    }
    
    //Conflict-free
    //Acceptable
    
    public void ConflictFreeSets(){
        
        //coloca todos os sets sem conflito no conflictFree
        for(int i = 0; i< activeArray.length;i++){
            int[] aux = {i};
            conflictfree.add(aux);
            RecursiveConflictFreeSets(aux);
            
        }
        
    }
    
    public void RecursiveConflictFreeSets(int[] currentIndexes){
        
        /*
        System.out.println("");
        for(int i: currentIndexes)
        System.out.print(i + " ");
        */
        //currentIndexes é 0,1,2,3,4,etc;
        
            int max = 0;
            boolean conflict;
            int [] testingIndexes = currentIndexes;
            
            //find the last element
            for(int k : testingIndexes){
                if(k > max){
                    max = k;
                }
            }
            
            //check if the array is over
            if(max >= activeArray.length){
                //chegou no último elemento
                //System.out.println("Chegou no limite!");
                return;
            }
            
        //add one by one the next element
        for(int i = (max + 1); i<activeArray.length; i++ ){
            //System.out.println("i é: " + i);
            //find the last element
            for(int k : testingIndexes){
                if(k > max){
                    max = k;
                }
            }
            //System.out.println(max);
            //check if the array is over
            if(max >= activeArray.length){
                //chegou no último elemento
                //System.out.println("Chegou no limite!");
                break;
            }
            
            testingIndexes = addIntArray(currentIndexes, i);
            conflict = checkConflictOnActive(testingIndexes);
            /*
            System.out.println("Testando: ");
            for(int l:testingIndexes)
                System.out.print(l + " ");
            
            System.out.println(conflict);
            */
            
            //test conflict, if conflict free, reiterate
            //System.out.println("conflict: " + conflict);
            if(conflict == false){
                conflictfree.add(testingIndexes);
                RecursiveConflictFreeSets(testingIndexes);
            } else{
                break;
            }
        }
    }
    
    /**
    int x,y;
        boolean complexAttack = false;
        for(int i=0;i<array.length ;i++){
            x = array[i];
            x = activeArray[x];
            for(int j=i;j<array.length;j++){
                y = activeArray[array[j]];
                complexAttack = isComplexAttacking(y,x); // soma dos ataques e suportes em X
            }
            if(complexAttack){
                return true;
            }
            complexAttack = false;
        }
        
        return false;
    }
    
    * 
    * 
    int x,y;
        int sum = 0;
        for(int i=0;i<array.length ;i++){
            x = array[i];
            x = activeArray[x];
            for(int j=i;j<array.length;j++){
                y = activeArray[array[j]];
                sum = matrix[y][x]; // soma dos ataques e suportes em X
            }
            if(sum < 0){
                return true;
            }
            sum = 0;
        }
        
        return false;
    }
**/
    public void sortObjectives(){
    //verifica objetivos válidos
        int sum;
        
        for(int i=1;i<argStart;i++){//para cada objetivo
            
            
            for(int j = 0;j<conflictfree.size();j++){//para cada maximal
                
                sum = 0;
                
                for(int k= 0; k< conflictfree.get(j).length;k++){//itera um maximal
                    //recebe o valor considerando os ataques do maximal contra o i
                    sum = sum + matrix[conflictfree.get(j)[k]][i];
                    
                }
                
                if(sum >= 0){
                    //se não tiver sido atacado ao total, adiciona ao maximal
                    conflictfree.set(j, addIntArray(conflictfree.get(j), i));
                }
                
            }
            
        }
    }
    
    
    public void chooseConflictFree(){
    //escolhe uma extensão aleatoriamente
    //o grupo dos que recusam a proposta costumam ser maiores(?)
        returnedSemantic = conflictfree.get(rand.nextInt(conflictfree.size()));
    }

    public boolean propostaAccepted(){
        
        boolean accepted = false;
        for(int i=0;i<returnedSemantic.length;i++){
            if(returnedSemantic[i] == 0){
                accepted = true;
            }
        }
        return accepted;
    }
    
    public void printConflictFree(){
        int[] aux;
        System.out.println("conflictfree size print: " + conflictfree.size());
        for(int i = 0;i<conflictfree.size();i++){
            
            System.out.println("");
            aux = conflictfree.get(i);
            for(int j: aux){
                System.out.print(j + " ");
            }
        }
        System.out.println("");
    }
}
    