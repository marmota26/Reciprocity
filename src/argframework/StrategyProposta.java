/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package argframework;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Marmota
 */
public class StrategyProposta extends Strategy{

    int agNumber;//agente 1 ou 2
    int[][] matrix;
    int choice;
    int receivedArg;
    int argStart;
    int ownArgStart;
    int argAmount;
    int objStart;
    
    int[] counterChoices = new int[0];
    int[] choiceStrength = new int[0];
    int[] importance;
    
    int strength;
    int strongest;
    
    int currentCounter;
    int[] usedCounters;
    
    int failedCounterCounter;

    public StrategyProposta(int agNumber, int[][] matrix, int argStart, int argAmount, int[] importance) {
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
        this.importance = importance;
    }
    

    @Override
    public int firstArgument() {
        strongest = -1;
        strength = 0;
        for(int i = ownArgStart;i<(ownArgStart + argAmount/2);i++){//para cada argumento disponível
                if(Math.abs(matrix[i][0]) > strength){
                    strength = Math.abs(matrix[i][0]);
                    strongest = i;
                }
        }
        
        return strongest;
        
    }

    @Override
    public void counterChoices(int arg) {
        failedCounterCounter = 0;
        counterChoices = null;
        //sorting
        for(int i = ownArgStart; i< (ownArgStart + argAmount/2);i++){// coloca todos os argumentos em counterChoices
            counterChoices = addIntArray(counterChoices, i);
        }
        
        choiceStrength = new int[counterChoices.length];
        
        //strength check
        for(int i = 0;i<counterChoices.length;i++){// para cada argumento em choices
           
           strength = 0;
               if(Math.abs(matrix[counterChoices[i]][0]) > strength){
                   strength = Math.abs(matrix[counterChoices[i]][0]);
               }
           
           choiceStrength[i] = strength;
           
        }
        
        //bubblesort
        for(int i = 0;i<counterChoices.length;i++){//
            for (int j = i; j < counterChoices.length; j++)
                if ((j+1) != (counterChoices.length) && choiceStrength[j] < choiceStrength[j+1])
                {
                    // swap arr[j+1] and arr[j]
                    int temp = choiceStrength[j];
                    choiceStrength[j] = choiceStrength[j+1];
                    choiceStrength[j+1] = temp;
                    
                    int temp2 = counterChoices[j];
                    counterChoices[j] = counterChoices[j+1];
                    counterChoices[j+1] = temp2;
                    
                }
        }

        counterChoices = removeUsedFromArray(counterChoices);
        
    }

    @Override
    public int counter() {
        
        if(failedCounterCounter >= counterChoices.length){
            return -1;
        }
        currentCounter = counterChoices[failedCounterCounter];
        return currentCounter;
        
    }

    @Override
    public void failedCounter() {
        failedCounterCounter++; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void successfulCounter() {
        usedCounters = addIntArray(usedCounters, currentCounter);
        failedCounterCounter = 0; //To change body of generated methods, choose Tools | Templates.
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
