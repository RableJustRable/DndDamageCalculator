package com.example.shadow4.dnddamagecalculator;

import java.util.ArrayList;

/**
 * Created by Shadow4 on 2/7/2018.
 */

public class diceHandler {

    public static void main(String[] args) {
        //random tests of the system cause i am too lazy to implement junit

        String input = "2d6+6";

        int[] rerolls = {1,2};

        Die[] testRoll = calculateDamage(input,rerolls);


        //Die[] testRoll = genAndRollDice(1,5);
        for(int i=0;i<testRoll.length;i++){
            System.out.print(testRoll[i].toString());
            if (i<testRoll.length-1){
                System.out.print("+");
            }
        }
		/*
		input = "1+1+1+1+1+1+1";
		testRoll = calculateDamage(input,rerolls);
		for(int i=0;i<testRoll.length;i++){
			System.out.print(testRoll[i].toString());
			if (i<testRoll.length-1){
				System.out.print("+");
			}
		}
		//*/


    }


    //Takes damage formula as a string and returns an array of integers representing the unsumed total
    //Ex1: 2d6 would return a two integer array representing two rolls of a d6
    //Ex2: 2d6+6 would return a 3 integer array, the first two being the same as Ex1 and the third integer simply being 6
    public static Die[] calculateDamage(String formula,int[] rerolls){
        //sanatize input data
        //the regex specified here gets rid of anything that isnt a number, the letter d, or the characters +or-
        formula = formula.replaceAll("[^0-9d+-]", "");
        //System.out.println(formula);
        //^^ for testing

        //look for negative numbers and add a + in front of them so they can be parsed correctly
        formula = formula.replaceAll("-", "+-");
        //split formula into separate strings using + as regex
        String splitFormula[] = formula.split("\\+");

        //make the rolls
        ArrayList<Die> allRolls = new ArrayList<Die>();
        for (String s:splitFormula){
            //continue if string is empty and add no new dies to list
            if (s.equals("")) continue;
            //if the string has a d in it its a dice roll, if not its a constant
            if (s.contains("d")){
                //split string by d to get the sides and quantity, then roll and add to allRolls
                String[] split = s.split("d");
                int sides = Integer.parseInt(split[0]);
                int num = Integer.parseInt(split[1]);
                Die[] rolls = genAndRollDice(sides,num);
                for (Die d:rolls){
                    allRolls.add(d);
                }
                System.out.println("Rolled "+split[0]+"d"+split[1]);
            }
            if (!s.contains("d")){
                allRolls.add(new Die(0,Integer.parseInt(s)));
                //TODO make a constant here
                System.out.println("Constant "+s);
            }
        }
        //do the rerolls for Great Weapon Fighting if any were defined
        if (rerolls!=null){
            for (Die d:allRolls){
                if(d.needsToBeReRolled(rerolls)){
                    d.reRoll();
                    System.out.println("Did a reroll!");
                }
            }
        }
        return allRolls.toArray(new Die[0]);

    }

    public static Die[] genAndRollDice(int num,int sides){
        //check if negative, if negative then multiply all dice rolls by -1
        boolean isNegative = false;
        if (num<0){
            isNegative = true;
            num*=-1;
        }

        Die[] output = new Die[num];
        for (int i=0;i<num;i++){
            output[i]=new Die(sides);
        }

        if(isNegative){
            for(Die d:output){
                d.currentRoll*=-1;
            }
        }
        return output;
    }





}

