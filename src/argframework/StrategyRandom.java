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
public class StrategyRandom extends Strategy{
    
    int agNumber;//agente 1 ou 2
    int[][] matrix;
    int choice;
    int receivedArg;
    int argStart;
    int ownArgStart;
    int argAmount;
    int objStart;
    Random rand =  new Random();
    int[] deck;
    
    int currentCounter;
    int[] usedCounters;
    
    int failedCounterCounter;

    public StrategyRandom(int agNumber, int argStart, int argAmount) {
        this.agNumber = agNumber;
        this.argStart = argStart;
        this.argAmount = argAmount;
        if(agNumber == 1){
            this.objStart = 1; 
            this.ownArgStart = argStart;
        } else if(agNumber == 2){
            this.objStart = 1 + (argStart - 1)/2;
            this.ownArgStart = argStart + argAmount/2;
        }
        deck = new int[argAmount/2];
        
        for(int i = 0; i<deck.length;i++){
            deck[i] = i;
        }
        
        counterChoices(0);
        
    }
    

    @Override
    public int firstArgument() {
        
        return ownArgStart + deck[0]; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void counterChoices(int arg) {
        failedCounterCounter = 0;
        for(int i = 0;i<deck.length;i++){
            
            int index = rand.nextInt(deck.length - i);
            int aux = deck[deck.length - i - 1];
            deck[deck.length -1 -i] = deck[index];
            deck[index] = aux;
        }
         //To change body of generated methods, choose Tools | Templates.
         
        deck = removeUsedFromArray(deck);
    }

    @Override
    public int counter() {
        
        if(failedCounterCounter >= deck.length){
            return -1;
        }
        
        currentCounter = deck[failedCounterCounter];
        return ownArgStart + currentCounter;
        //To change body of generated methods, choose Tools | Templates.
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
