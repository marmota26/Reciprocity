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
public class Stable extends Semantic {
    
    ArrayList<int[]> conflictfree = new ArrayList<int[]>();
    ArrayList<int[]> inAttacksOut = new ArrayList<int[]>();
    Random rand = new Random();

    public Stable(int[][] matrix, int[] activeArray, int argStart) {
        this.matrix = matrix;
        this.activeArray = activeArray;
        this.argStart = argStart;
    }
    
    public int[] checkSemantic(){
        conflictfree = new ArrayList<int[]>();
        inAttacksOut = new ArrayList<int[]>();
        //System.out.println("ConflictFree");
        ConflictFreeSets();
        
        //printConflictFree();
        inAttacksOut();
        //printConflictFree();
        
        sortObjectives();
        chooseInAttacksOut();
        
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
            if(conflict == false){
                conflictfree.add(testingIndexes);
                RecursiveConflictFreeSets(testingIndexes);
            } else{
                //break;
            }
        }
    }
    
    public void inAttacksOut(){
    //verifica quais dos conjuntos livre de conflito os que estão in atacam todos os out
    
    boolean inAtkAllOuts = true; 
    boolean kIsOut = false;
    boolean kIsAttacked = false;
    
    //para cada extensão conflict free
    for(int i=0;i<conflictfree.size();i++){
        inAtkAllOuts = true;
        for(int k=0;k<activeArray.length;k++){//para cada argumento do array ativo
            kIsOut = true;
            
            for(int j=0;j<conflictfree.get(i).length;j++){  
                
                if(conflictfree.get(i)[j] == k){//checa se o k está fora ou dentro
                    kIsOut = false;
                }
                
            }
            
            if(kIsOut){//se o k estiver fora
                kIsAttacked = false;
                for(int h=0;h<conflictfree.get(i).length;h++){ //verifica se está sendo atacado por qualquer IN
                
                    if(isComplexAttacking(activeArray[conflictfree.get(i)[h]], activeArray[k], conflictfree.get(i))){
                        //se algum dentro o estiver atacando
                        kIsAttacked = true;
                    }
                }
                if(!kIsAttacked){//se o k não for atacado, essa extensão não ataca todos
                    inAtkAllOuts = false;
                }
            }
            
        }
        
        if(inAtkAllOuts){
            inAttacksOut.add(conflictfree.get(i));
        }
    
    }
        //System.out.println("Qtd de inAtkOUts: " + inAttacksOut.size());
    //
        
    
    }

    public void sortObjectives(){
    //verifica objetivos válidos
        int sum;
        
        for(int i=1;i<argStart;i++){//para cada objetivo
            
            
            for(int j = 0;j<inAttacksOut.size();j++){//para cada maximal
                
                sum = 0;
                
                for(int k= 0; k< inAttacksOut.get(j).length;k++){//itera um maximal
                    //recebe o valor considerando os ataques do maximal contra o i
                    sum = sum + matrix[inAttacksOut.get(j)[k]][i];
                    
                }
                
                if(sum >= 0){
                    //se não tiver sido atacado ao total, adiciona ao maximal
                    inAttacksOut.set(j, addIntArray(inAttacksOut.get(j), i));
                }
                
            }
            
        }
    }
    
    
    public void chooseInAttacksOut(){
        
        
        int total = 0;
        int highest = 0;
        int chosen = 0;
        //para cada inAttacksOut
        //soma todos os argumentos que atacam e ajudam a proposta
        //mantém o maximal que tem o maior total
        for(int i=0; i<inAttacksOut.size();i++){
            total = 0;
            for(int j=0; j<inAttacksOut.get(i).length;j++){
                total = Math.abs(matrix[inAttacksOut.get(i)[j]][0]);
                
            }
            if(total > highest){
                highest = total;
                chosen = i;
            }
        }
        if(!inAttacksOut.isEmpty()){
            returnedSemantic = inAttacksOut.get(chosen);
        }
        
        
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
    