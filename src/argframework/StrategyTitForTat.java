/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package argframework;

import java.util.Arrays;

/**
 *
 * @author Marmota
 */
public class StrategyTitForTat extends Strategy{
    
    int agNumber;//agente 1 ou 2
    int[][] matrix;
    int choice;
    int receivedArg;
    int argStart;
    int ownArgStart;
    int argAmount;
    int objStart;
    
    int currentCounter;
    int[] usedCounters;
    
    int failedCounterCounter;
    
    public StrategyTitForTat(int agNumber, int[][] matrix, int argStart, int argAmount) {
        this.agNumber = agNumber;
        this.matrix = matrix;
        this.argStart = argStart;
        this.argAmount = argAmount;
        if(agNumber == 1){
            this.objStart = 1; 
            this.ownArgStart = argStart;
        } else if(agNumber == 2){
            this.objStart = 1 + (argStart - 1)/2;
            this.ownArgStart = argStart + argAmount/2;
        }
    }
    
    @Override
    public int firstArgument(){
    
        choice = ownArgStart;//primeira recompensa do agente
        
        return choice;
    }
    
    @Override
    public void counterChoices(int argOpo){
        
        counterChoices = new int[0];
        boolean isThreat = false;
        
        receivedArg = argOpo;
        
        for(int i =objStart; i<objStart+((argStart - 1)/2);i++){
            if(matrix[argOpo][i] < 0){
                isThreat = true;
                //System.out.println("threat:" + argOpo + " " + i + ": " + matrix[argOpo][i]);
            }
        }
        
        if(isThreat){
            //ameaças
            for(int i = (ownArgStart + argAmount/4);i<(ownArgStart + argAmount/2);i++){
                
                counterChoices = addIntArray(counterChoices, i);
                
            }
        } else{
            //recompensas
            for(int i = (ownArgStart);i<(ownArgStart + argAmount/4 );i++){
                
                counterChoices = addIntArray(counterChoices, i);
                
            }
        }
        
        counterChoices = removeUsedFromArray(counterChoices);
       
    }
    
    @Override
    public int counter(){
        
        if(failedCounterCounter >= counterChoices.length){
            return -1;
        }
        //System.out.println(agNumber + ": failed counter: " + failedCounterCounter);
        currentCounter = counterChoices[failedCounterCounter];
        return currentCounter;
    }
    
    @Override
    public void failedCounter(){
    
        failedCounterCounter++;
    }
    
    @Override
    public void successfulCounter(){
    
        failedCounterCounter = 0;
        
        usedCounters = addIntArray(usedCounters, currentCounter);
    }
    
    public int[] addIntArray(int[] array, int element){
        
        if(array == null){
            array = new int[1];
            array[0] = element;
            
            return array;
        }
        
        array = Arrays.copyOf(array, array.length + 1);
        array[array.length - 1] = element;
        return array;
        
    }
    
    public int[] removeUsedFromArray(int[] array){
        //remove used counters
        int[] array2 = new int[0];
        boolean used;
        
        if(usedCounters == null){
            return array;
        }
        
        for(int i: array){
            
            used = false;
            
            for(int j: usedCounters){
                if(i == j){
                    used = true;
                }
            }
            
            if(used == false){
                array2 = addIntArray(array2, i);
            }
            
        }
        
        return array2;
        
    }
    
}
