/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package argframework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Marmota
 */
public class ArgFramework {

    /**
     * @param args the command line arguments
     */
    static int proposta = 1;
    static int objectiveAmount = 10;//total for both agents
    static int argAmount = 20;//total for both agents
    static Framework fa;
    static Semantic ps;
    static int[] currentSemantic;
    static int currentArgument;
    static int[] chosenSet;
    static boolean print;
    static int mode;
    static int selection;
    static int agentTypeAmount = 4;
    static int semanticTypeAmount = 4;
    static int argTries = 3;
    static int interactionChance = 25;
    static int[][] multiTestMatrix;
    static int caseNumber = 0;
    
    static boolean testing;
    static boolean stop;
    static boolean successCase;
    static int propSuccess = 0;
    static boolean accepted;
    static int[] activeArgs;
    static int[] testingArg;
    static int[] oldActiveArray;
    static Scanner scan = new Scanner(System.in);
    static boolean useSavedMatrix = false;
    static boolean useFixedMatrix = false;
    
    static StrategyTitForTat stt1, stt2;
    static StrategyStrongest ss1, ss2;
    static Strategy s1, s2;
    static int agType1;
    static int agType2;
    static int semanticType;
    
    static ArrayList<int[][]> matrixes = new ArrayList<int[][]>();
    static ArrayList<int[]> activeArrays = new ArrayList<int[]>();
    static ArrayList<int[]> returneds = new ArrayList<int[]>();
    static ArrayList<int[]> activeReturneds = new ArrayList<int[]>();
    static ArrayList<int[]> importances = new ArrayList<int[]>();
    static ArrayList<int[][]> fixedMatrixSet = new ArrayList<int[][]>();
    
    static int totalDamage;
    static int totalDamagedObjectivesAg1;
    static int totalDamagedObjectivesAg2;
    static int totalImportanceDamageAg1;
    static int totalImportanceDamageAg2;
    static int averageDamage;
    static int cases;
    static int totalArgExchanged;
    static int usedCounter = 0;
    static int failedCounterAg1 = 0;
    static int failedCounterAg2 = 0;
    
    public static void main(String[] args) {
        
        cases = 1000;
        print = false;
        
        //somente para o one shot e interaction
        //0 = Tit for Tat
        //1 = Strongest
        //2 = Random
        //3 = Strongest 2 (Proposta)
        agType1 = 3;
        agType2 = 3;
        
        //0 = Dung Preferred
        //1 = Safe Preferred
        //2 = Side Preferred
        //3 = Stable
        //4 = Conflict Free
        //5 = Conflict Free Complex
        semanticType = 3;
        
        //0 = One Shot
        //1 = 4x4 persuasion
        //2 = 4x4 total damaged objectives Ag1
        //3 = 4x4 total damaged objectives Ag2
        //4 = 4x4 total argumentos trocados
        //5 = 4x4 total cancelled objectives total
        
        //6 = 1x1 1~100 interaction chance test Persuasion
        //7 = 1x1 1~100 interaction chance test cancelled ag1
        //8 = 1x1 1~100 interaction chance test cancelled ag2
        //9 = 1x1 1~100 interaction chance test argumentos trocados
        //10 = 1x1 1~100 interaction chance test cancelled total
        
        //3 = 4x4 total damaged importance
        //4 = 4x4 total damaged objective to agent selection
        //5 = 4x4 total damaged importance to agent selection
        //6 = 4x4 total args exchanged
        //7 = 5x5 total failed args for agent selection
        mode = 10;
        selection = 0;
        
        switch(mode){
            case 0:
                oneShot();
                break;
            case 1:
                multiTesting();
                break;
            case 2:
                multiTesting();
                break;
            case 3:
                multiTesting();
                break;
            case 4:
                multiTesting();
                break;
            case 5:
                multiTesting();
                break;
            case 6:
                interactionTesting();
                break;
            case 7:
                interactionTesting();
                break;
            case 8:
                interactionTesting();
                break;
            case 9:
                interactionTesting();
                break;
            case 10:
                interactionTesting();
                break;
            case 11:
                
                semanticTesting();
                
                break;
            case 12:
                semanticTesting2();//persuasion
                break;
            case 13:
                semanticTesting2();//cancelled ag1
                break;
            case 14:
                semanticTesting2();//cancelled ag2
                break;
            case 15:
                semanticTesting2();//args exchanged
                break;
            case 16:
                semanticTesting2();//total cancelled obj
                break;
        }
        
    }
    
    public static void oneShot(){
        
        for(int i = 0;i<cases;i++){
            if(print){System.out.println("Sorting case number #" + i);}
            if(print){System.out.println("setup...");}
        setup();
            if(print){System.out.println("setup Done!");}
            if(print){System.out.println("debating...");}
        debate();
            if(print){System.out.println("debating Done!");}
            if(print){System.out.println("saving...");}
        saving();
            if(print){System.out.println("saving Done!");}
        
        successCase = false;
            for(int j = 0;j<ps.returnedSemantic.length;j++){//para cada maximal
                
                if(ps.returnedSemantic[j] == 0){
                    successCase = true;
                }
            }
            if(successCase == true){
            propSuccess++;
            }
           
        }
        if(print){System.out.println("evaluating...");}
        evaluate();
        if(print){System.out.println("evaluating Done!");}
        
        System.out.println(propSuccess + " / " + cases);
        
        // Choose Parameters:
        // Framework type: Bipolar Directional Framework
        // Constructing the Framework
        
        // Semantic
        // Strategy types for each player
        
        // Simulate Argumentation
        // Evaluate Framework
        if(print){fa.printMatrix();}
    
    }
    
    public static void multiTesting(){
        
        for(int m1 = 0;m1<agentTypeAmount;m1++){//para cada tipo de agente 1
            agType1 = m1;
            
            for(int m2 = 0;m2<agentTypeAmount;m2++){//para cada tipo de agente 2
                agType2 = m2;
                matrixes = new ArrayList<int[][]>();
                activeArrays = new ArrayList<int[]>();
                returneds = new ArrayList<int[]>();
                activeReturneds = new ArrayList<int[]>();
                importances = new ArrayList<int[]>();
                
                for(int i = 0;i<cases;i++){
                    setup();
                    debate();
                    saving();
        
                    successCase = false;
                        for(int j = 0;j<ps.returnedSemantic.length;j++){//para cada extensão final escolhida
                
                            if(ps.returnedSemantic[j] == 0){//se o zero estiver lá, então a proposta foi aceita
                                successCase = true;
                            }
                        }
                        if(successCase == true){
                        propSuccess++;
                        }
           
                }
                
                evaluate();
                switch(mode){
                    case 1:
                        System.out.print(propSuccess + " ");
                        propSuccess = 0;
                        break;
                    case 2:
                        System.out.print(totalDamagedObjectivesAg1 + " ");
                        break;
                    case 3:
                        System.out.print(totalDamagedObjectivesAg2 + " ");
                        break;
                    case 4:
                        System.out.print(totalArgExchanged + " ");
                        break;
                    case 5:
                        System.out.print(totalDamagedObjectivesAg1 + totalDamagedObjectivesAg2 + " ");
                        break;    
                        
                }
                
            }
            System.out.println();
        }
        
    }
    
    public static void interactionTesting(){
        
         
            for(int z = 0;z<101;z++){//para cada chance
                interactionChance = z;
                matrixes = new ArrayList<int[][]>();
                activeArrays = new ArrayList<int[]>();
                returneds = new ArrayList<int[]>();
                activeReturneds = new ArrayList<int[]>();
                importances = new ArrayList<int[]>();
                
                for(int i = 0;i<cases;i++){
                    setup();
                    debate();
                    saving();
        
                    successCase = false;
                        for(int j = 0;j<ps.returnedSemantic.length;j++){//para cada extensão final escolhida
                
                            if(ps.returnedSemantic[j] == 0){//se o zero estiver lá, então a proposta foi aceita
                                successCase = true;
                            }
                        }
                        if(successCase == true){
                        propSuccess++;
                        }
           
                }
                
                evaluate();
                switch(mode){
                    case 6:
                        System.out.println(propSuccess);
                        propSuccess = 0;
                        break;
                    case 7:
                        System.out.println(totalDamagedObjectivesAg1 + " ");
                        break;
                    case 8:
                        System.out.println(totalDamagedObjectivesAg2 + " ");
                        break;
                    case 9:
                        System.out.println(totalArgExchanged + " ");
                        break;
                    case 10:
                        System.out.println(totalDamagedObjectivesAg1 + totalDamagedObjectivesAg2 + " ");
                        break;    
                        
                }
                
            }
        
        
    }
    
    public static void semanticTesting(){
        useSavedMatrix = true;
        int[] multiPropSuccess = new int[semanticTypeAmount];
        
        for(int i = 0;i<cases;i++){ //para cada caso
            
            fa = new Framework(proposta, objectiveAmount, argAmount);
            fa.RandomizerRuleset6();
            multiTestMatrix = fa.matrix;
            
            for(int s = 0;s<semanticTypeAmount;s++){//para cada semântica
                semanticType = s;
                matrixes = new ArrayList<int[][]>();
                activeArrays = new ArrayList<int[]>();
                returneds = new ArrayList<int[]>();
                activeReturneds = new ArrayList<int[]>();
                importances = new ArrayList<int[]>();
                
                
                setup();
                debate();
                saving();
        
                successCase = false;
                    for(int j = 0;j<ps.returnedSemantic.length;j++){//para cada extensão final escolhida
                
                        if(ps.returnedSemantic[j] == 0){//se o zero estiver lá, então a proposta foi aceita
                            successCase = true;
                        }
                    }
                    if(successCase == true){
                        multiPropSuccess[s]++;
                    }
           
                
                
                evaluate();
                
                
            }
        }
        
        for(int k = 0;k<semanticTypeAmount;k++){
            System.out.print(multiPropSuccess[k] + " ");
        }
        System.out.println("");
    }
    
    public static void semanticTesting2(){
            
        useFixedMatrix = true;
        
        for(int i=0;i<cases;i++){//generate matrixes for testing
            fa = new Framework(proposta, objectiveAmount, argAmount);
            fa.percentage = interactionChance;
            fa.RandomizerRuleset6();
            fixedMatrixSet.add(fa.matrix);
        }
        
        for(int s = 0;s<semanticTypeAmount;s++){//for each semantic amount
            semanticType = s;
            
            for(int ag1 = 0;ag1<agentTypeAmount;ag1++){//for each agent amount
                agType1 = ag1;
                
                for(int ag2 = 0;ag2<agentTypeAmount;ag2++){//for each semantic amount
                    agType2 = ag2;
                    
                    matrixes = new ArrayList<int[][]>();
                    activeArrays = new ArrayList<int[]>();
                    returneds = new ArrayList<int[]>();
                    activeReturneds = new ArrayList<int[]>();
                    importances = new ArrayList<int[]>();
                
                    caseNumber = 0;
                    for(int i = 0;i<cases;i++){
                        caseNumber = i;
                        setup();
                        debate();
                        saving();
        
                        successCase = false;
                            for(int j = 0;j<ps.returnedSemantic.length;j++){//para cada extensão final escolhida
                
                                if(ps.returnedSemantic[j] == 0){//se o zero estiver lá, então a proposta foi aceita
                                    successCase = true;
                                }
                            }
                            if(successCase == true){
                                propSuccess++;
                            }
           
                    }
           
                
                
                    evaluate();
                    switch(mode){
                    case 12:
                        System.out.print(propSuccess + " ");
                        propSuccess = 0;
                        break;
                    case 13:
                        System.out.print(totalDamagedObjectivesAg1 + " ");
                        break;
                    case 14:
                        System.out.print(totalDamagedObjectivesAg2 + " ");
                        break;
                    case 15:
                        System.out.print(totalArgExchanged + " ");
                        break;
                    case 16:
                        System.out.print(totalDamagedObjectivesAg1 + totalDamagedObjectivesAg2 + " ");
                        break;    
                    
                    }
                    
                }
                System.out.println();
            }
            System.out.println("");
        }
        
        
    }
    
    public static void argAmountTesting(){
            //not used yet
        useFixedMatrix = true;
        
        for(int i=0;i<cases;i++){//generate matrixes for testing
            fa = new Framework(proposta, objectiveAmount, argAmount);
            fa.percentage = interactionChance;
            fa.RandomizerRuleset6();
            fixedMatrixSet.add(fa.matrix);
        }
        
        for(int s = 0;s<semanticTypeAmount;s++){//for each semantic amount
            semanticType = s;
            
            for(int ag1 = 0;ag1<agentTypeAmount;ag1++){//for each agent amount
                agType1 = ag1;
                
                for(int ag2 = 0;ag2<agentTypeAmount;ag2++){//for each agent amount
                    agType2 = ag2;
                    
                    matrixes = new ArrayList<int[][]>();
                    activeArrays = new ArrayList<int[]>();
                    returneds = new ArrayList<int[]>();
                    activeReturneds = new ArrayList<int[]>();
                    importances = new ArrayList<int[]>();
                
                    caseNumber = 0;
                    for(int i = 0;i<cases;i++){
                        caseNumber = i;
                        setup();
                        debate();
                        saving();
        
                        successCase = false;
                            for(int j = 0;j<ps.returnedSemantic.length;j++){//para cada extensão final escolhida
                
                                if(ps.returnedSemantic[j] == 0){//se o zero estiver lá, então a proposta foi aceita
                                    successCase = true;
                                }
                            }
                            if(successCase == true){
                                propSuccess++;
                            }
           
                    }
           
                
                
                    evaluate();
                    switch(mode){
                    case 12:
                        System.out.print(propSuccess + " ");
                        propSuccess = 0;
                        break;
                    case 13:
                        System.out.print(totalDamagedObjectivesAg1 + " ");
                        break;
                    case 14:
                        System.out.print(totalDamagedObjectivesAg2 + " ");
                        break;
                    case 15:
                        System.out.print(totalArgExchanged + " ");
                        break;
                    case 16:
                        System.out.print(totalDamagedObjectivesAg1 + totalDamagedObjectivesAg2 + " ");
                        break;    
                    
                    }
                    
                }
                System.out.println();
            }
            System.out.println("");
        }
        
        
    }
    
    public static void setup(){
        fa = new Framework(proposta, objectiveAmount, argAmount);
        
        //Ruleset 1
        //Ruleset 2 é o 1, mas com máximo de interação entre argumentos
        //Ruleset 3 é o 2, mas sempre ataca o oponente, e suporta um aliado
        //Ruleset 4 é o 3, mas com X%
        //Ruleset 5 
        fa.percentage = interactionChance;
        fa.RandomizerRuleset6();
        //fa.printMatrix();
        
        if(useFixedMatrix){
            fa.matrix = fixedMatrixSet.get(caseNumber);
        }
        
        activeArgs = fa.initializeActiveArray(proposta, objectiveAmount);
        //fa.printActiveArgs();
        
        //ps = new PreferredSemantic(fa.matrix, fa.activeArgs, (proposta+objectiveAmount));
        //int[] aux = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30};
        //int[] aux = {0,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30};
        
        switch(semanticType){
            case 0: 
                ps = new PreferredSemantic(fa.matrix, activeArgs, (proposta+objectiveAmount));
                break;
            case 1:
                ps = new sPreferredSemantic(fa.matrix, activeArgs, (proposta+objectiveAmount));
                break;
            case 2:
                ps = new sidePreferredSemantic(fa.matrix, activeArgs, (proposta+objectiveAmount));
                break;
            case 3:
                ps = new Stable(fa.matrix, activeArgs, (proposta+objectiveAmount));
                break;
            case 4:
                ps = new ConflictFreeSemantic(fa.matrix, activeArgs, (proposta+objectiveAmount));
                break;
            case 5:
                ps = new ConflictFreeSemanticComplex(fa.matrix, activeArgs, (proposta+objectiveAmount));
                break;
        }
        
        
        ps.checkSemantic();
        //ps.printConflictFree();
        //ps.printDefended();
        //ps.printMaximal();
    }
    
    public static void debate(){
        
        switch(agType1){
            case 0:
                s1 = new StrategyTitForTat(1, fa.matrix, (proposta + objectiveAmount), argAmount);
                break;
            case 1:
                s1 = new StrategyStrongest(1, fa.matrix, (proposta + objectiveAmount), argAmount, fa.importance);
                break;
            case 2:
                s1 = new StrategyRandom(1, (proposta + objectiveAmount), argAmount);
                break;
            case 3:
                s1 = new StrategyProposta(1, fa.matrix, (proposta + objectiveAmount), argAmount, fa.importance);
                break;
        }
        
        switch(agType2){
            case 0:
                s2 = new StrategyTitForTat(2, fa.matrix, (proposta + objectiveAmount), argAmount);
                break;
            case 1:
                s2 = new StrategyStrongest(2, fa.matrix, (proposta + objectiveAmount), argAmount, fa.importance);
                break;
            case 2:
                s2 = new StrategyRandom(2, (proposta + objectiveAmount), argAmount);
                break; 
            case 3:
                s2 = new StrategyProposta(2, fa.matrix, (proposta + objectiveAmount), argAmount, fa.importance);
                break;
        }
        
        boolean argueing = true;
        int count = 0;
        stop = false;
        
        currentArgument = s2.firstArgument();
        //System.out.println("current argmumnet" + currentArgument);
        ps.activeArray = addIntArray(ps.activeArray, currentArgument);
        if(print){System.out.println("First Argument out! Arg: " + currentArgument);}
        
        while(argueing){
            count++;
            if(print){System.out.println("argue round " + count);}
            
            ag1Argue();
            //System.out.println(currentArgument);
            if(stop == true){
                break;
            }
            if(print){System.out.println("Proponent Countered with: " + currentArgument);}
            
            ag2Argue();     
            
            if(stop == true){
                break;
            }
            if(print){System.out.println("Opponent Countered with: " + currentArgument);}
        }
        currentSemantic = ps.checkSemantic();
        
    }
    
    public static void ag1Argue(){
        boolean used = false;
        int roundFailedArgs1 = 0;
        
        s1.counterChoices(currentArgument);//agente 1 junta seus argumentos
            testing = true;
            
            while(testing){//agente 1 tenta argumentar
                currentArgument = s1.counter();
                if(print){System.out.println("Trying arg: " + currentArgument);}
                if(currentArgument == -1){//se o agente não possuir mais argumentos, falha
                    stop = true;
                    testing = false;
                    break;
                }
                //checa se o argumento já foi usado
                used = false;
                for(int m : ps.activeArray){
                    if(currentArgument == m){
                        used = true;
                        usedCounter ++;
                        failedCounterAg1 ++;
                        roundFailedArgs1 ++;
                        
                    }
                }
                
                if(roundFailedArgs1 >= argTries){currentArgument = -1;stop = true;testing = false;break;}
                
                oldActiveArray = ps.activeArray;
                ps.activeArray = addIntArray(ps.activeArray, currentArgument);
                currentSemantic = ps.checkSemantic();
                
                if(used){//se o argumento já foi usado, falha
                    s1.failedCounter();
                    ps.activeArray = oldActiveArray;
                    if(print){System.out.println("used");}
                } else if(ps.propostaAccepted() == true){//se o agente mudou a proposta de OUT para IN
                    //ps.activeArray = addIntArray(ps.activeArray, currentArgument);
                    
                    testing = false;
                    s1.successfulCounter();
                    if(print){System.out.println("success");}
                } else{
                    s1.failedCounter();
                    failedCounterAg1 ++;
                    roundFailedArgs1 ++;
                    ps.activeArray = oldActiveArray;
                    
                    if(print){System.out.println("fail");}
                }
                
                
                if(roundFailedArgs1 >= argTries){currentArgument = -1;stop = true;testing = false;break;}
            }
            
            
            //checa semântica novamente
            //pega argumento do proponente
            //checa se altera a semântica
            
    }
    
    public static void ag2Argue(){
        boolean used = false;
        int roundFailedArgs2 = 0;
        
        s2.counterChoices(currentArgument);//agente 2 junta seus argumentos
            testing = true;
            
            while(testing){//agente 2 tenta argumentar
                
                
                currentArgument = s2.counter();
                if(print){System.out.println("Trying arg: " + currentArgument);}
                
                if(currentArgument == -1){//checa se o agente não tem mais argumentos
                    stop = true;
                    testing = false;
                    break;
                } 
                //checa se o argumento já foi usado
                used = false;
                
                for(int m : ps.activeArray){
                    if(currentArgument == m){
                        used = true;
                        usedCounter ++;
                        failedCounterAg2 ++;
                        roundFailedArgs2 ++;
                        
                    }
                }
                if(roundFailedArgs2 >= argTries){currentArgument = -1;stop = true;testing = false;break;}
                
                oldActiveArray = ps.activeArray;
                ps.activeArray = addIntArray(ps.activeArray, currentArgument);
                currentSemantic = ps.checkSemantic();
                
                if(used){//se for usado, falha
                    s2.failedCounter();
                    ps.activeArray = oldActiveArray;
                    if(print){System.out.println("used");}
                } else if(ps.propostaAccepted() == false){//se a proposta mudou de IN para OUT
                    //ps.activeArray = addIntArray(ps.activeArray, currentArgument);
                    
                    testing = false;
                    s2.successfulCounter();
                    if(print){System.out.println("success");}
                } else {
                    s2.failedCounter();
                    failedCounterAg2 ++;
                    roundFailedArgs2 ++;
                    ps.activeArray = oldActiveArray;
                    
                    if(print){System.out.println("fail");}
                }
                
                if(roundFailedArgs2 >= argTries){currentArgument = -1;stop = true;testing = false;break;}
            }
            //checa semântica
            //pega argumento do oponente
            //checa se altera a semântica
    }
    
    public static void saving(){
        
        matrixes.add(ps.matrix);
        activeArrays.add(ps.activeArray);
        returneds.add(ps.returnedSemantic);
        importances.add(fa.importance);
    }
    
    public static void evaluate(){
        
        totalDamage = 0;
        totalDamagedObjectivesAg1 = 0;
        totalDamagedObjectivesAg2 = 0;
        totalImportanceDamageAg1 = 0;
        totalImportanceDamageAg2 = 0;
        for(int i = 0; i<matrixes.size();i++){//para cada caso
            for(int j = 0; j < returneds.get(i).length;j++ ){//para cada argumento da extensão retornada
                
                for(int k = 1; k< (proposta + objectiveAmount); k++){//para cada objetivo
                    
                    if(matrixes.get(i)[returneds.get(i)[j]][k] < 0){
                        if(semanticType == 3){
                            totalDamage += matrixes.get(i)[activeArrays.get(i)[returneds.get(i)[j]]][k];
                        } else  {
                            totalDamage += matrixes.get(i)[returneds.get(i)[j]][k];
                        }
                    }
                }
            }
        }
        int damagedObjective;
        for(int i = 0; i<matrixes.size();i++){//para cada caso
            for(int k = 1; k< (proposta + objectiveAmount/2); k++){//para cada objetivo
                damagedObjective = 0;
                int checkingArg = -1;//separa os args iniciais dos objetivos, stable devolve os índices ao inves dos args
                int checkingArg2 = proposta + objectiveAmount + argAmount;//separa os args dos objetivos
                
                for(int j = 0; j < returneds.get(i).length;j++ ){//para cada argumento da extensão
                    
                    if(semanticType==3){
                        if(checkingArg >= returneds.get(i)[j]){
                            break;
                        }
                        checkingArg = returneds.get(i)[j];
                    
                        if(ps.isComplexAttacking(activeArrays.get(i)[returneds.get(i)[j]], k, returneds.get(i))){
                            damagedObjective++;
                        } 
                    } else{
                        if(checkingArg2 < returneds.get(i)[j]){
                            break;
                        }
                        checkingArg2 = returneds.get(i)[j];
                        
                        if(ps.isComplexAttacking(returneds.get(i)[j], k, returneds.get(i))){
                            damagedObjective++;
                        } 
                    }
                }
                if(damagedObjective > 0){
                    totalDamagedObjectivesAg1++;
                    totalImportanceDamageAg1 += importances.get(i)[k-1];
                }
            }
        }
        
        /**
         * int damagedObjective;
        for(int i = 0; i<matrixes.size();i++){//para cada caso
            for(int k = 1; k< proposta + objectiveAmount; k++){//para cada objetivo
                damagedObjective = 0;
                for(int j = 0; j < returneds.get(i).length;j++ ){//para cada argumento ativo
                    damagedObjective = damagedObjective + matrixes.get(i)[returneds.get(i)[j]][k];
                    
                } 
                if(damagedObjective < 0){
                totalDamagedObjectivesAg1++;
                totalImportanceDamageAg1 += importances.get(i)[k-1];
                }
            }
        }
         * 
         */
        
        
        for(int i = 0; i<matrixes.size();i++){//para cada caso
            for(int k = proposta + (objectiveAmount/2); k< (proposta + objectiveAmount); k++){//para cada objetivo
                damagedObjective = 0;
                int checkingArg = -1;
                int checkingArg2 = proposta + objectiveAmount + argAmount;
                
                for(int j = 0; j < returneds.get(i).length;j++ ){//para cada argumento da extensão
                    
                    if(semanticType==3){//regra separada para a semântica stable
                        if(checkingArg >= returneds.get(i)[j]){
                            break;
                        }
                        checkingArg = returneds.get(i)[j];
                    
                        if(ps.isComplexAttacking(activeArrays.get(i)[returneds.get(i)[j]], k, returneds.get(i))){
                            damagedObjective++;
                        }
                    } else{//para todas as demais semânticas
                        if(checkingArg2 < returneds.get(i)[j]){
                            break;
                        }
                        checkingArg2 = returneds.get(i)[j];
                        
                        if(ps.isComplexAttacking(returneds.get(i)[j], k, returneds.get(i))){
                            damagedObjective++;
                        }
                    }
                }
                
                if(damagedObjective > 0){
                    totalDamagedObjectivesAg2++;
                    totalImportanceDamageAg2 += importances.get(i)[k-1];
                }
            }
        }
        
        
        totalArgExchanged = 0;
        for(int i = 0; i<matrixes.size();i++){//para cada caso
            for(int j = 0; j < activeArrays.get(i).length;j++ ){//para cada argumento ativo
                int temp = activeArrays.get(i)[j];
                if(temp > 10){
                    totalArgExchanged++;
                }
                //Abilite o print de baixo se quiser ver os arrays ativos
                if(print){System.out.print(activeArrays.get(i)[j] + " ");}
            }
            if(print){System.out.println("");}
        }
        
        for(int i=0;i<returneds.size();i++){// para cada preferido
            
            for(int j = 0; j<returneds.get(i).length;j++){
                if(print){System.out.print(returneds.get(i)[j] + " ");}
            }
            if(print){System.out.println("");}
        }
        
        if(print){System.out.println("Total Damage: " + totalDamage);}
        //System.out.println("Average Damage: " + totalDamage/cases);
        if(print){System.out.println("Total Damaged Ag1 Objectives: " + totalDamagedObjectivesAg1);}
        //System.out.println("Average Damaged Ag1 Objectives: " + totalDamagedObjectivesAg1/cases);
        if(print){System.out.println("Total Damaged Ag1 Importance: " + totalImportanceDamageAg1);}
        //System.out.println("Average Damaged Ag1 Importance: " + totalImportanceDamageAg1/cases);
        if(print){System.out.println("Total Damaged Ag2 Objectives: " + totalDamagedObjectivesAg2);}
        //System.out.println("Average Damaged Ag2 Objectives: " + totalDamagedObjectivesAg2/cases);
        if(print){System.out.println("Total Damaged Ag2 Importance: " + totalImportanceDamageAg2);}
        //System.out.println("Average Damaged Ag2 Importance: " + totalImportanceDamageAg2/cases);
        if(print){System.out.println("Total Damaged Objectives: " + (totalDamagedObjectivesAg2 + totalDamagedObjectivesAg1));}
        //System.out.println("Average Damaged Objectives: " + (totalDamagedObjectivesAg2 + totalDamagedObjectivesAg1)/cases);
        if(print){System.out.println("Total Damaged Importance: " + (totalImportanceDamageAg2 + totalImportanceDamageAg1));}
        //System.out.println("Average Damaged Importance: " + (totalImportanceDamageAg2 + totalImportanceDamageAg1)/cases);
        if(print){System.out.println("Total Args Exchanged: " + totalArgExchanged);}
        if(print){System.out.println("Average Args Exchanged: " + totalArgExchanged/cases);}
        if(print){System.out.println("Total Ag1 Failed Args: " + failedCounterAg1);}
        if(print){System.out.println("Total Ag2 Failed Args: " + failedCounterAg2);}
    }
    
    public static boolean semanticChange(int suggestion){
        if(print){System.out.println("Semantic Change");}
        PreferredSemantic ps2;
        int[] activeArgs2 = addIntArray(ps.activeArray, suggestion);
        int[] semanticTest;
        
        ps2 = new PreferredSemantic(fa.matrix, activeArgs2, (proposta+objectiveAmount));
        semanticTest = ps2.checkSemantic();
        
        if(currentSemantic == semanticTest){
            return false;
        } else {
            return true;
        }
    }
    
    public static int[] addIntArray(int[] array, int element){
        
        array = Arrays.copyOf(array, array.length + 1);
        array[array.length - 1] = element;
        return array;
        
    }
}
