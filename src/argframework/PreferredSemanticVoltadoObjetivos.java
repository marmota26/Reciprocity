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
 * input: int[][] matrix, int[] activeArray, int argStart
 * return: int[] preferred
 */
public class PreferredSemanticVoltadoObjetivos extends Semantic{
    
    //int[][] matrix;
    //int argStart;
    //int[] activeArray;//proposta e objetivos começam sempre ativos
    //int[] returnedSemantic;
    ArrayList<int[]> conflictfree = new ArrayList<int[]>();
    ArrayList<int[]> defended = new ArrayList<int[]>();
    ArrayList<int[]> maximal = new ArrayList<int[]>();
    Random rand = new Random();

    public PreferredSemanticVoltadoObjetivos(int[][] matrix, int[] actArray, int argStart) {
        this.matrix = matrix;
        this.activeArray = actArray;
        this.argStart = argStart;
    }
    
    public int[] checkSemantic(){
        conflictfree = new ArrayList<int[]>();
        defended = new ArrayList<int[]>();
        maximal = new ArrayList<int[]>();
        //System.out.println("ConflictFree");
        ConflictFreeSets();
        //System.out.println("Defended");
        DefendedSetsFromConflictFree();
        //System.out.println("Maximal");
        
        defendedIndexToArgNumber();
        sortObjectives();
        //System.out.println("Sorting");
        
        MaximalSetsFromDefended();
        
        
        //sortObjectives();
        //chooseMaximal();
        chooseMaximal();
        
        //choose Maximal -> escolhe o primeiro
        
        //System.out.println(conflictfree.size());
        //System.out.println(defended.size());
        //preferred = new int[maximal];
        //preferred = maximal.get(maximal.size() - 1);
        //System.out.println("Qtd de Maximais: " + maximal.size());
        //printConflictFree();
        //printDefended();
        //printMaximal();
        
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
    
    public void DefendedSetsFromConflictFree(){
        
        boolean def;
        
        for(int i=0;i<conflictfree.size();i++){
        
            def = checkDefenseOnActive(conflictfree.get(i));
            if(def == true){
                defended.add(conflictfree.get(i));
            }
        }
        
    }
    /* Versão sem Bipolaridade
    public boolean checkDefenseOnActive(int[] array){
    
        //Verifica se os membros do array são defendidos
        //true =  defendidos
        //false =  não defendidos
        
        int x,y;
        boolean defendido = true;
        
        for(int i=0;i<matrix[0].length ;i++){//para cada argumento
            for(int j=i;j<array.length;j++){
                y = array[j];
                y = activeArray[y];
                if(matrix[i][y] < 0){
                    //se ele ataca algum argumento da array
                    //verifica se algum argumento o defende
                    defendido = false;
                    for(int k = 0; k<array.length;k++){
                        if(matrix[k][i] < 0){
                            //Defendido
                            defendido = true;
                            break;
                        }
                        
                    }
                }
                
                
            }
        }
        
        return defendido;
    }
    */
    
    public boolean checkDefenseOnActive(int[] array){
    
        //Verifica se os membros do array são defendidos
        //true =  defendidos
        //false =  não defendidos
        
        int x,y;
        boolean isNotDefended = false;//soma dos ataques
        
        for(int i: array){//para cada argumento da array
            
            
            for(int k = 0;k < activeArray.length;k++){//para cada argumento ativo
                
                if(isComplexAttacking(activeArray[k], activeArray[i], activeArray)){//se algum argumento o estiver atacando
                    isNotDefended = true;
                    for(int j: array){//checa se algum argumento da extensão o defende
                        
                        if(isComplexAttacking(activeArray[j], activeArray[k], activeArray)){
                            isNotDefended = false;
                        }
                    }
                    if(isNotDefended){
                        return false;
                    }
                }
                
                
            }
          
        }
        
        return true;
    }
    
    
    public void MaximalSetsFromDefended(){
        
        int max = 0;
        for(int i=0;i<defended.size();i++){
            
            if(defended.get(i).length > max){
                max = defended.get(i).length;
            }
        }
        for(int j=0;j<defended.size();j++){
            
            if(defended.get(j).length == max){
                maximal.add(defended.get(j));
            }
        }
        
    }
    
    public void defendedIndexToArgNumber(){
        //
        int[] argNumbers;
        
        for(int i=0; i<defended.size();i++){
            argNumbers = new int[defended.get(i).length];
            for(int j=0;j<defended.get(i).length;j++){
                
                argNumbers[j] = activeArray[defended.get(i)[j]];
            }
            defended.set(i, argNumbers);
        
        }
    
    }
    
    
    public void sortObjectives(){
    //verifica objetivos válidos
    //adiciona os objetivos não atacados ao maximal
        int sum;
        boolean complexAttack = false;
        
        for(int i=1;i<argStart;i++){//para cada objetivo
            
            
            for(int j = 0;j<defended.size();j++){//para cada maximal
                
                sum = 0;
                
                for(int k= 0; k< defended.get(j).length;k++){//itera um maximal
                    
                    //complex
                    complexAttack = isComplexAttacking(k,i, activeArray);
                    if(complexAttack){
                        break;
                    }
                }
                
                //se não tiver sido atacado, adiciona ao maximal
                if(!complexAttack){
                    defended.set(j, addIntArray(defended.get(j), i));
                }
            }
        }
    }
    
    public void chooseMaximal(){
        int total = 0;
        int highest = 0;
        int cancelAmount = 0;
        int lowestCancelAmount = 9999;
        int chosen = 0;
        //para cada maximal
        //mantém o maximal que tem a menor quantidade de cancelamentos
        for(int i=0; i<maximal.size();i++){//para cada maximal
            
            total = 0;
            cancelAmount = 0;
            
            for(int j=0; j<maximal.get(i).length;j++){//para cada argumento do maximal
                
                
                for(int k=1;k<argStart;k++){//para cada objetivo
                    if(isComplexAttacking(maximal.get(i)[j], k, activeArray)){//se ele ataca este objetivo adiciona à lista de ataques
                        cancelAmount++;
                    }
                }
                
            }
            
            if(cancelAmount < lowestCancelAmount){
                    lowestCancelAmount = cancelAmount;
                    chosen = i;
                }
            
        }
        
        returnedSemantic = maximal.get(chosen);
    }
    
    @Override
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
    
    public void printDefended(){
        int[] aux;
        System.out.println("defended size print: " + defended.size());
        for(int i = 0;i<defended.size();i++){
            
            System.out.println("");
            aux = defended.get(i);
            for(int j: aux){
                System.out.print(j + " ");
            }
        }
        System.out.println("");
    }
    
    public void printMaximal(){
        int[] aux;
        System.out.println("max size print: " + maximal.size());
        for(int i = 0;i<maximal.size();i++){
            
            System.out.println("");
            aux = maximal.get(i);
            for(int j: aux){
                System.out.print(j + " ");
            }
        }
    }
    
}
