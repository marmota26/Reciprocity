/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package argframework;

/**
 *
 * @author Marmota
 */
public abstract class Strategy {
    
    int agNumber;//agente 1 ou 2
    int[][] matrix;
    int choice;
    int receivedArg;
    int[] counterChoices;

    public abstract int firstArgument();
    public abstract void counterChoices(int arg);
    public abstract int counter();
    public abstract void failedCounter();
    public abstract void successfulCounter();
    
}
