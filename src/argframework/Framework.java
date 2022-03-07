/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package argframework;
import java.lang.Math;
import java.util.Random;

/**
 *
 * @author Marmota
 */
    
public class Framework {
    // Nodes
    int proposta, objAmount,argAmount;
    int total;
    int[][] matrix;
    Random random = new Random();
    int[] activeArgs;
    int[] importance;
    int percentage = 25;

    public Framework(int proposta, int objAmount, int argAmount) {
        this.proposta = proposta;
        this.objAmount = objAmount;
        this.argAmount = argAmount;
        this.total = proposta + objAmount + argAmount;
        this.importance = new int[objAmount];
        matrix =  new int[total][total];
    }
    
    public void RandomizerFull(){
        
        for(int i=0;i<total;i++){
            for(int j=0;j<total;j++){
                //fill matrix with -100 ~ 100
                matrix[i][j]= random.nextInt(201) - 100;
                if(i==j){//no argument attacks or helps itself
                    matrix[i][j] = 0;
                }
            }
        }
    }
    
    public void RandomizerRuleset1(){
        //argumentos totais devem ser divisíveis por 4
        for(int i=(proposta + objAmount);i<total;i++){
            for(int j=(proposta + objAmount);j<total;j++){
                
                //only 10% chance of interacting
                if(random.nextInt(10) == 1){
                    matrix[i][j]= random.nextInt(201) - 100;
                }
                
                if(i==j){
                //no argument attacks or helps itself
                    matrix[i][j] = 0;
                }
                if(i<proposta+objAmount){
                //no objective attacks or helps objectives
                    matrix[i][j] = 0;
                }
                if(j<proposta + objAmount && j>0){
                    
                    matrix[i][j] = 0;
                }
                
                
            }
        }
        int x,y;
        //proposta ajuda um objetivo próprio e...
        x = 0; //linha da proposta
        y = random.nextInt((objAmount/2)) + proposta;//um objetivo do agente 1 (1~5)
        matrix[x][y] =  random.nextInt(101);
        
        //... ataca um objetivo do oponente
        x = 0;
        y = random.nextInt((objAmount/2)) + (objAmount/2) + proposta;
        matrix[x][y] =  random.nextInt(100) - 100;
        
        //Cada argumento de cada agente ataca ou ajuda a proposta
        y = 0;//Relação com a proposta
        for(x=(proposta + objAmount);x<(proposta+ objAmount + argAmount/2);x++){
            matrix[x][y] = random.nextInt(101);
        }
        for(x=(proposta + objAmount + argAmount/2);x<total;x++){
            matrix[x][y] = random.nextInt(100) - 100;
        }
        
        //Cada agente tem metade dos argumentos ameaças e metade recompensa
        //Para cada Argumento
        for(x = (proposta + objAmount);x<total;x++){
            //Se o argumento for do proponente
            if(x<(proposta + objAmount + argAmount/4)){
                //metade recompensa, afeta positivamente um objetivo do oponente
                y = random.nextInt(objAmount/2) + proposta + objAmount/2;
                matrix[x][y] = random.nextInt(101);
            } else if(x<(proposta + objAmount + argAmount/2)){
                //metade ameaça
                y = random.nextInt(objAmount/2) + proposta + objAmount/2;
                matrix[x][y] = random.nextInt(100) - 100;
            } else if(x<(proposta + objAmount + 3*argAmount/4)){
                //Se o argumento for do oponente  
                //metade recompensa, afeta positivamente um objetivo do proponente
                y = random.nextInt(objAmount/2) + proposta;
                matrix[x][y] = random.nextInt(101);
            } else{
                //metade ameaça
                y = random.nextInt(objAmount/2) + proposta;
                matrix[x][y] = random.nextInt(100) - 100;
            }
        }
        
        for(int i = 0; i< importance.length;i++){
            importance[i] = random.nextInt(100);
        }
        
    }
    
    public void RandomizerRuleset2(){
        //argumentos totais devem ser divisíveis por 4
        for(int i=(proposta + objAmount);i<total;i++){
            for(int j=(proposta + objAmount);j<total;j++){
                
                matrix[i][j]= random.nextInt(201) - 100;
                
                if(i==j){
                //no argument attacks or helps itself
                    matrix[i][j] = 0;
                }
                if(i<proposta+objAmount){
                //no objective attacks or helps objectives
                    matrix[i][j] = 0;
                }
                if(j<proposta + objAmount && j>0){
                    
                    matrix[i][j] = 0;
                }
                
                
            }
        }
        int x,y;
        //proposta ajuda um objetivo próprio e...
        x = 0; //linha da proposta
        y = random.nextInt((objAmount/2)) + proposta;//um objetivo do agente 1 (1~5)
        matrix[x][y] =  random.nextInt(101);
        
        //... ataca um objetivo do oponente
        x = 0;
        y = random.nextInt((objAmount/2)) + (objAmount/2) + proposta;
        matrix[x][y] =  random.nextInt(100) - 100;
        
        //Cada argumento de cada agente ataca ou ajuda a proposta
        y = 0;//Relação com a proposta
        for(x=(proposta + objAmount);x<(proposta+ objAmount + argAmount/2);x++){
            matrix[x][y] = random.nextInt(101);
        }
        for(x=(proposta + objAmount + argAmount/2);x<total;x++){
            matrix[x][y] = random.nextInt(100) - 100;
        }
        
        //Cada agente tem metade dos argumentos ameaças e metade recompensa
        //Para cada Argumento
        for(x = (proposta + objAmount);x<total;x++){
            //Se o argumento for do proponente
            if(x<(proposta + objAmount + argAmount/4)){
                //metade recompensa, afeta positivamente um objetivo do oponente
                y = random.nextInt(objAmount/2) + proposta + objAmount/2;
                matrix[x][y] = random.nextInt(101);
            } else if(x<(proposta + objAmount + argAmount/2)){
                //metade ameaça
                y = random.nextInt(objAmount/2) + proposta + objAmount/2;
                matrix[x][y] = random.nextInt(100) - 100;
            } else if(x<(proposta + objAmount + 3*argAmount/4)){
                //Se o argumento for do oponente  
                //metade recompensa, afeta positivamente um objetivo do proponente
                y = random.nextInt(objAmount/2) + proposta;
                matrix[x][y] = random.nextInt(101);
            } else{
                //metade ameaça
                y = random.nextInt(objAmount/2) + proposta;
                matrix[x][y] = random.nextInt(100) - 100;
            }
        }
        
        for(int i = 0; i< importance.length;i++){
            importance[i] = random.nextInt(100);
        }
        
    }
    
    public void RandomizerRuleset3(){
        //argumentos totais devem ser divisíveis por 4
        for(int i=(proposta + objAmount);i<total;i++){
            for(int j=(proposta + objAmount);j<total;j++){
                
                if(i<=(proposta + objAmount + argAmount/2 - 1) && j<=(proposta + objAmount + argAmount/2 - 1)){
                    matrix[i][j]= random.nextInt(100)+1;
                } else if(i>(proposta + objAmount + argAmount/2 - 1) && j>(proposta + objAmount + argAmount/2 - 1)){
                    matrix[i][j]= random.nextInt(100)+1;
                } else{
                    matrix[i][j]= random.nextInt(100)-100;
                }
                
                
                if(i==j){
                //no argument attacks or helps itself
                    matrix[i][j] = 0;
                }
                if(i<proposta+objAmount){
                //no objective attacks or helps objectives
                    matrix[i][j] = 0;
                }
                if(j<proposta + objAmount && j>0){
                    
                    matrix[i][j] = 0;
                }
                
                
            }
        }
        int x,y;
        //proposta ajuda um objetivo próprio e...
        x = 0; //linha da proposta
        y = random.nextInt((objAmount/2)) + proposta;//um objetivo do agente 1 (1~5)
        matrix[x][y] =  random.nextInt(101);
        
        //... ataca um objetivo do oponente
        x = 0;
        y = random.nextInt((objAmount/2)) + (objAmount/2) + proposta;
        matrix[x][y] =  random.nextInt(100) - 100;
        
        //Cada argumento de cada agente ataca ou ajuda a proposta
        y = 0;//Relação com a proposta
        for(x=(proposta + objAmount);x<(proposta+ objAmount + argAmount/2);x++){
            matrix[x][y] = random.nextInt(101);
        }
        for(x=(proposta + objAmount + argAmount/2);x<total;x++){
            matrix[x][y] = random.nextInt(100) - 100;
        }
        
        //Cada agente tem metade dos argumentos ameaças e metade recompensa
        //Para cada Argumento
        for(x = (proposta + objAmount);x<total;x++){
            //Se o argumento for do proponente
            if(x<(proposta + objAmount + argAmount/4)){
                //metade recompensa, afeta positivamente um objetivo do oponente
                y = random.nextInt(objAmount/2) + proposta + objAmount/2;
                matrix[x][y] = random.nextInt(101);
            } else if(x<(proposta + objAmount + argAmount/2)){
                //metade ameaça
                y = random.nextInt(objAmount/2) + proposta + objAmount/2;
                matrix[x][y] = random.nextInt(100) - 100;
            } else if(x<(proposta + objAmount + 3*argAmount/4)){
                //Se o argumento for do oponente  
                //metade recompensa, afeta positivamente um objetivo do proponente
                y = random.nextInt(objAmount/2) + proposta;
                matrix[x][y] = random.nextInt(101);
            } else{
                //metade ameaça
                y = random.nextInt(objAmount/2) + proposta;
                matrix[x][y] = random.nextInt(100) - 100;
            }
        }
        
        for(int i = 0; i< importance.length;i++){
            importance[i] = random.nextInt(100);
        }
        
    }
    
    public void RandomizerRuleset4(){
        //argumentos totais devem ser divisíveis por 4
        for(int i=(proposta + objAmount);i<total;i++){
            for(int j=(proposta + objAmount);j<total;j++){
                if(random.nextInt(100) < percentage){
                    if(i<=(proposta + objAmount + argAmount/2 - 1) && j<=(proposta + objAmount + argAmount/2 - 1)){
                        matrix[i][j]= random.nextInt(100)+1;
                    } else if(i>(proposta + objAmount + argAmount/2 - 1) && j>(proposta + objAmount + argAmount/2 - 1)){
                        matrix[i][j]= random.nextInt(100)+1;
                    } else{
                        matrix[i][j]= random.nextInt(100)-100;
                    }
                }
                
                if(i==j){
                //no argument attacks or helps itself
                    matrix[i][j] = 0;
                }
                if(i<proposta+objAmount){
                //no objective attacks or helps objectives
                    matrix[i][j] = 0;
                }
                if(j<proposta + objAmount && j>0){
                    
                    matrix[i][j] = 0;
                }
                
                
            }
        }
        int x,y;
        //proposta ajuda um objetivo próprio e...
        x = 0; //linha da proposta
        y = random.nextInt((objAmount/2)) + proposta;//um objetivo do agente 1 (1~5)
        matrix[x][y] =  random.nextInt(101);
        
        //... ataca um objetivo do oponente
        x = 0;
        y = random.nextInt((objAmount/2)) + (objAmount/2) + proposta;
        matrix[x][y] =  random.nextInt(100) - 100;
        
        //Cada argumento de cada agente ataca ou ajuda a proposta
        y = 0;//Relação com a proposta
        for(x=(proposta + objAmount);x<(proposta+ objAmount + argAmount/2);x++){
            matrix[x][y] = random.nextInt(101);
        }
        for(x=(proposta + objAmount + argAmount/2);x<total;x++){
            matrix[x][y] = random.nextInt(100) - 100;
        }
        
        //Cada agente tem metade dos argumentos ameaças e metade recompensa
        //Para cada Argumento
        for(x = (proposta + objAmount);x<total;x++){
            //Se o argumento for do proponente
            if(x<(proposta + objAmount + argAmount/4)){
                //metade recompensa, afeta positivamente um objetivo do oponente
                y = random.nextInt(objAmount/2) + proposta + objAmount/2;
                matrix[x][y] = random.nextInt(101);
            } else if(x<(proposta + objAmount + argAmount/2)){
                //metade ameaça
                y = random.nextInt(objAmount/2) + proposta + objAmount/2;
                matrix[x][y] = random.nextInt(100) - 100;
            } else if(x<(proposta + objAmount + 3*argAmount/4)){
                //Se o argumento for do oponente  
                //metade recompensa, afeta positivamente um objetivo do proponente
                y = random.nextInt(objAmount/2) + proposta;
                matrix[x][y] = random.nextInt(101);
            } else{
                //metade ameaça
                y = random.nextInt(objAmount/2) + proposta;
                matrix[x][y] = random.nextInt(100) - 100;
            }
        }
        
        for(int i = 0; i< importance.length;i++){
            importance[i] = random.nextInt(100);
        }
        
    }
    
    public void RandomizerRuleset5(){
        //argumentos totais devem ser divisíveis por 4
        for(int i=(proposta + objAmount);i<total;i++){
            for(int j=(proposta + objAmount);j<total;j++){
                if(random.nextInt(100) < 100){
                    
                    matrix[i][j]= random.nextInt(201)-100;
                    
                }
                
                if(i==j){
                //no argument attacks or helps itself
                    matrix[i][j] = 0;
                }
                if(i<proposta+objAmount){
                //no objective attacks or helps objectives
                    matrix[i][j] = 0;
                }
                if(j<proposta + objAmount && j>0){
                    
                    matrix[i][j] = 0;
                }
                
                
            }
        }
        int x,y;
        //proposta ajuda um objetivo próprio e...
        x = 0; //linha da proposta
        y = random.nextInt((objAmount/2)) + proposta;//um objetivo do agente 1 (1~5)
        matrix[x][y] =  random.nextInt(101);
        
        //... ataca um objetivo do oponente
        x = 0;
        y = random.nextInt((objAmount/2)) + (objAmount/2) + proposta;
        matrix[x][y] =  random.nextInt(100) - 100;
        
        //Cada argumento de cada agente ataca ou ajuda a proposta
        y = 0;//Relação com a proposta
        for(x=(proposta + objAmount);x<(proposta+ objAmount + argAmount/2);x++){
            matrix[x][y] = random.nextInt(101);
        }
        for(x=(proposta + objAmount + argAmount/2);x<total;x++){
            matrix[x][y] = random.nextInt(100) - 100;
        }
        
        //Cada agente tem metade dos argumentos ameaças e metade recompensa
        //Para cada Argumento
        for(x = (proposta + objAmount);x<total;x++){
            //Se o argumento for do proponente
            if(x<(proposta + objAmount + argAmount/4)){
                //metade recompensa, afeta positivamente um objetivo do oponente
                y = random.nextInt(objAmount/2) + proposta + objAmount/2;
                matrix[x][y] = random.nextInt(101);
            } else if(x<(proposta + objAmount + argAmount/2)){
                //metade ameaça
                y = random.nextInt(objAmount/2) + proposta + objAmount/2;
                matrix[x][y] = random.nextInt(100) - 100;
            } else if(x<(proposta + objAmount + 3*argAmount/4)){
                //Se o argumento for do oponente  
                //metade recompensa, afeta positivamente um objetivo do proponente
                y = random.nextInt(objAmount/2) + proposta;
                matrix[x][y] = random.nextInt(101);
            } else{
                //metade ameaça
                y = random.nextInt(objAmount/2) + proposta;
                matrix[x][y] = random.nextInt(100) - 100;
            }
        }
        
        for(int i = 0; i< importance.length;i++){
            importance[i] = random.nextInt(100);
        }
        
    }
    
     public void RandomizerRuleset6(){
        //incluir argumentos ruins(argumentos com peso zero, e argumentos que atacam o próprio agente)
        //por enquanto somente argumentos inúteis(peso zero), 20%
        //argumentos totais devem ser divisíveis por 4
        for(int i=(proposta + objAmount);i<total;i++){
            for(int j=(proposta + objAmount);j<total;j++){
                if(random.nextInt(100) < percentage){
                    if(i<=(proposta + objAmount + argAmount/2 - 1) && j<=(proposta + objAmount + argAmount/2 - 1)){
                        matrix[i][j]= random.nextInt(100)+1;
                    } else if(i>(proposta + objAmount + argAmount/2 - 1) && j>(proposta + objAmount + argAmount/2 - 1)){
                        matrix[i][j]= random.nextInt(100)+1;
                    } else{
                        matrix[i][j]= random.nextInt(100)-100;
                    }
                }
                
                if(i==j){
                //no argument attacks or helps itself
                    matrix[i][j] = 0;
                }
                if(i<proposta+objAmount){
                //no objective attacks or helps objectives
                    matrix[i][j] = 0;
                }
                if(j<proposta + objAmount && j>0){
                    
                    matrix[i][j] = 0;
                }
                
                
            }
        }
        int x,y;
        //proposta ajuda um objetivo próprio e...
        x = 0; //linha da proposta
        y = random.nextInt((objAmount/2)) + proposta;//um objetivo do agente 1 (1~5)
        matrix[x][y] =  random.nextInt(101);
        
        //... ataca um objetivo do oponente
        x = 0;
        y = random.nextInt((objAmount/2)) + (objAmount/2) + proposta;
        matrix[x][y] =  random.nextInt(100) - 100;
        
        //Cada argumento de cada agente ataca ou ajuda a proposta
        y = 0;//Relação com a proposta
        for(x=(proposta + objAmount);x<(proposta+ objAmount + argAmount/2);x++){
            matrix[x][y] = random.nextInt(101);
        }
        for(x=(proposta + objAmount + argAmount/2);x<total;x++){
            matrix[x][y] = random.nextInt(100) - 100;
        }
        
        //Cada agente tem metade dos argumentos ameaças e metade recompensa
        //Para cada Argumento
        for(x = (proposta + objAmount);x<total;x++){
            //Se o argumento for do proponente
            if(x<(proposta + objAmount + argAmount/4)){
                //metade recompensa, afeta positivamente um objetivo do oponente
                y = random.nextInt(objAmount/2) + proposta + objAmount/2;
                matrix[x][y] = random.nextInt(101);
            } else if(x<(proposta + objAmount + argAmount/2)){
                //metade ameaça
                y = random.nextInt(objAmount/2) + proposta + objAmount/2;
                matrix[x][y] = random.nextInt(100) - 100;
            } else if(x<(proposta + objAmount + 3*argAmount/4)){
                //Se o argumento for do oponente  
                //metade recompensa, afeta positivamente um objetivo do proponente
                y = random.nextInt(objAmount/2) + proposta;
                matrix[x][y] = random.nextInt(101);
            } else{
                //metade ameaça
                y = random.nextInt(objAmount/2) + proposta;
                matrix[x][y] = random.nextInt(100) - 100;
            }
        }
        
        for(int i = 0; i< importance.length;i++){
            importance[i] = random.nextInt(100);
        }
        
        //gerador de argumentos irrelevantes
        for(int i=(proposta + objAmount);i<total;i++){//para cada argumento
            
            if(random.nextInt(100)<=20){//com x por cento de chances
                
                for(int j=0;j<total;j++){//torna o argumento irrelevante
                    
                    matrix[i][j] = 0;
                }
            }
        }
        
    }
    
    
    public int[] initializeActiveArray(int prop, int objA){
        
        activeArgs = new int[0];
        
        for(int i=0;i<(prop);i++){//NOTA: tirei os objetivos dos ativos
            addActiveArg(i);
        }
        return activeArgs;
    }
    
    public void addActiveArg(int arg){
    
        int[] auxArgs = new int[activeArgs.length + 1];
        
        System.arraycopy(activeArgs, 0, auxArgs, 0, activeArgs.length);
        
        auxArgs[activeArgs.length] = arg;
        
        activeArgs = auxArgs;
    }
    
    public void printMatrix(){
        
        for(int i=0;i<total;i++){
            for(int j=0;j<total;j++){
                
                System.out.format("%+4d ", matrix[i][j]);
                
            }
            System.out.println();
        }
    }
    
    public void printActiveArgs(){
        System.out.println("");
        for(int i=0;i<activeArgs.length;i++){
            System.out.print(activeArgs[i] + " ");
        }
        
    }
}
