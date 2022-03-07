/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package argframework;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Marmota
 */
public abstract class Semantic {
    
    protected int[][] matrix;
    protected int argStart;
    protected int[] activeArray;
    protected int[] returnedSemantic;
    
    public abstract int[] checkSemantic();
    public abstract boolean propostaAccepted();
    
    
    public int[] arrayListToArray(ArrayList al){
    
        int[] intArray = new int[al.size()];
        
        for(int i=0;i<al.size();i++){
            if (al.get(i) != null) {
                intArray[i] = (int)al.get(i);
            }
        }
        
        return intArray;
    }
    
    public int[] addIntArray(int[] array, int element){
        
        array = Arrays.copyOf(array, array.length + 1);
        array[array.length - 1] = element;
        return array;
        
    }
    
    public boolean isComplexAttacking(int argA, int argB, int[] actArray){
    
        //verifica se existe uma cadeia de ataque para suporte ou vice versa
        for(int i : actArray){
            
            //se A ataca B diretamente
            if(matrix[argA][argB] < 0){
                return true;
            }

            //se A ataca X
            if(matrix[argA][i] < 0){
            //e se X suporta B, então retorna true
                if(matrix[i][argB] > 0){
                    return true;
                }
            }
            
            //se A suporta X
            if(matrix[argA][i] > 0){
            //e se X ataca B, então retorna true
                if(matrix[i][argB] < 0){
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public boolean checkConflictOnActive(int[] array){
        
        //Verifica se existe um conflito com peso(não flat) na array
        //true =  tem conflito
        //false =  não tem conflito
        //combinatorial
        int x,y;
        boolean complexAttack = false;
        for(int i=0;i<array.length ;i++){
            x = array[i];
            x = activeArray[x];
            for(int j=i;j<array.length;j++){
                y = activeArray[array[j]];
                complexAttack = isComplexAttacking(y,x, activeArray); // soma dos ataques e suportes em X
                    if(complexAttack){
                        return true;
                    }
            }
        }
        
        return false;
    }
    
}
