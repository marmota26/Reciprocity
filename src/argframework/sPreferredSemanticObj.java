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
public class sPreferredSemanticObj extends Semantic{
    
    //int[][] matrix;
    //int argStart;
    //int[] activeArray;//proposta e objetivos começam sempre ativos
    //int[] returnedSemantic;
    ArrayList<int[]> conflictfree = new ArrayList<int[]>();
    ArrayList<int[]> defended = new ArrayList<int[]>();
    ArrayList<int[]> maximal = new ArrayList<int[]>();
    ArrayList<int[]> safe = new ArrayList<int[]>();
    
    Random rand = new Random();

    public sPreferredSemanticObj(int[][] matrix, int[] actArray, int argStart) {
        this.matrix = matrix;
        this.activeArray = actArray;
        this.argStart = argStart;
    }
    
    public int[] checkSemantic(){
        conflictfree = new ArrayList<int[]>();
        safe = new ArrayList<int[]>();
        defended = new ArrayList<int[]>();
        maximal = new ArrayList<int[]>();
        //System.out.println("ConflictFree");
        ConflictFreeSets();
        //printConflictFree();
        SafeSets();
        //System.out.println("Defended");
        DefendedSetsFromSafe();
        //System.out.println("Maximal");
        
        
        defendedIndexToArgNumber();
        sortObjectives();
        MaximalSetsFromDefended();
        //System.out.println("Sorting");
        
        
        //choose Maximal -> escolhe o primeiro
        
        //System.out.println(conflictfree.size());
        //System.out.println(defended.size());
        //preferred = new int[maximal];
        //preferred = maximal.get(maximal.size() - 1);
        chooseMaximal();
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
    
    public void SafeSets(){
    //entre os conjuntos conflict free, determinar os que são seguros
    //seguro(depois de separado os conflict free):  checa se um argumento fora é atacado e suportado ao mesmo tmepo
    boolean setIsSafe = true;
    //para cada argumento do conflictedset, checa todos os seus suportes
    for(int i = 0;i<conflictfree.size();i++){//para cada extensão
        
        for(int j = 0;j<conflictfree.get(i).length;j++){//para cada argumento da extensão j=indice
            
            for(int m = 0;m<matrix[0].length;m++){//para cada relação de j 
                //m=arg number
                if(matrix[activeArray[conflictfree.get(i)[j]]][m] > 0){//se há um suporte de j* para m
                    //verifica se há um ataque complexo vindo da mesma extensão
                    for(int n=0;n<conflictfree.get(i).length;n++){//para cada argumento da mesma extensão
                        //verifica se existe um ataque complexo de n para m
                        if(isComplexAttacking(n,m, activeArray)){
                            //essa extensão não é safe
                            setIsSafe = false;
                        }
                    }
                    
                }
            }
           
        }
        
        if(setIsSafe){
        safe.add(conflictfree.get(i));
        }
        setIsSafe = true;
    }
    
    
    //para cada argumento/objetivo suportado, verifica se algum dos argumentos ataca ele()
    
    
    
    }
    
    
    public void DefendedSetsFromSafe(){
        
        boolean def;
        
        for(int i=0;i<safe.size();i++){
        
            def = checkDefenseOnActive(safe.get(i));
            if(def == true){
                defended.add(safe.get(i));
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
        int sumA;//soma dos ataques
        int sumD;//soma das defesas
        
        for(int i: array){//para cada argumento da array
            
            sumA = 0;
            sumD = 0;
            
            for(int k = 0;k < activeArray.length;k++){//para cada argumento ativo
                
                if(matrix[activeArray[k]][i] < 0){ //se algum argumento ativo ataca o argumento
                    sumA = sumA + Math.abs(matrix[activeArray[k]][activeArray[i]]);
                    
                    for(int j: array){//interação de um nível com argumentos de dentro
                
                        if(matrix[j][activeArray[k]] < 0){//soma os ataques ao argumento atacante como defesa
                            sumD = sumD + Math.abs(matrix[activeArray[j]][activeArray[k]]);
                    
                        } else if(matrix[j][activeArray[k]] > 0){//e suportes ao argumento atacante como ataque
                            sumA = sumA + Math.abs(matrix[activeArray[j]][activeArray[k]]);
                        }
                
                
                    }
                    
                    
                }
                
            }
            
            /*
            for(int k = 0;k < matrix[0].length;k++){//soma os ataques de fora e dentro
                
                if(matrix[k][activeArray[i]] < 0){// se esse argumento ataca
                    sumA = sumA + Math.abs(matrix[k][activeArray[i]]);
                    
                    for(int j: array){//interação de um nível com argumentos de dentro
                
                        if(matrix[activeArray[j]][k] < 0){//soma os ataques ao argumento atacante como defesa
                            sumD = sumD + Math.abs(matrix[activeArray[j]][k]);
                    
                        } else if(matrix[activeArray[j]][k] > 0){//e suportes ao argumento atacante como ataque
                            sumA = sumA + Math.abs(matrix[activeArray[j]][k]);
                        }
                
                
                    }
                }
                
            }*/
            
            for(int l: array){//soma as defesas de dentro
                
                if(matrix[activeArray[l]][activeArray[i]] > 0){
                    sumD = sumD + Math.abs(matrix[activeArray[l]][activeArray[i]]);
                }
            }
            
            if(sumA - sumD > 0){//se os ataques forem maiores que o suportes
                return false;
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
        
        for(int i=1;i<argStart;i++){//para cada objetivo
            
            
            for(int j = 0;j<defended.size();j++){//para cada maximal
                
                sum = 0;
                
                for(int k= 0; k< defended.get(j).length;k++){//itera um maximal
                    //recebe o valor considerando os ataques do maximal contra o i
                    sum = sum + matrix[defended.get(j)[k]][i];
                    
                }
                
                if(sum >= 0){
                    //se não tiver sido atacado ao total, adiciona ao maximal
                    defended.set(j, addIntArray(defended.get(j), i));
                }
                
            }
            
        }
    }
    
    public void chooseMaximal(){
        int total = 0;
        int highest = 0;
        int chosen = 0;
        //para cada maximal
        //soma todos os argumentos que atacam e ajudam a proposta
        //mantém o maximal que tem o maior total
        for(int i=0; i<maximal.size();i++){
            total = 0;
            for(int j=0; j<maximal.get(i).length;j++){
                total = Math.abs(matrix[maximal.get(i)[j]][0]);
                
            }
            if(total > highest){
                highest = total;
                chosen = i;
            }
        }
        if(!maximal.isEmpty()){
            returnedSemantic = maximal.get(chosen);
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
