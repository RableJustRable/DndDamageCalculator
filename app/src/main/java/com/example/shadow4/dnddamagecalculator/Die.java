package com.example.shadow4.dnddamagecalculator;

/**
 * Created by Shadow4 on 2/7/2018.
 */

//represents a single die. a die with 0 sides is a constant number and is not rerolled when roll is called
public class Die{
    int sides;
    int currentRoll;
    boolean wasRerolled;

    public Die(int sides){
        this.sides = sides;
        this.wasRerolled=false;
        roll();
    }
    //use this for constants.
    public Die(int sides,int currentRoll){
        this.sides=sides;
        this.currentRoll=currentRoll;
        this.wasRerolled=false;
    }

    //determines if a die should be rerolled according to the reroll list
    public boolean needsToBeReRolled(int[] rerolls){
        if (rerolls!=null){
            for (int i:rerolls){
                if (currentRoll==i){
                    return true;
                }
            }
        }
        return false;
    }

    public void roll(){
        if (sides>0){
            int max=sides,min=1;
            int range = Math.abs(max - min) + 1;
            currentRoll = (int)(Math.random() * range) + min;
        }
    }

    public void reRoll(){
        if (sides>0){
            roll();
            wasRerolled=true;
        }
    }

    //returns the roll followed by what size die rolled it (if not a constant
    public String toString(){
        String output = null;
        if(sides>0){
				/*
				output = currentRoll+"(d"+sides+")";
				if (currentRoll<0){
					output = "("+currentRoll+")"+"(d"+sides+")";
				}
				//*/
            StringBuilder sb = new StringBuilder();
            if(currentRoll<0) sb.append("(");
            sb.append(currentRoll);
            if(currentRoll<0) sb.append(")");
            sb.append("(d");
            sb.append(sides);
            if(wasRerolled) sb.append("r");
            sb.append(")");
            output = sb.toString();

        }
        //if the die is a constant
        if(sides==0){
            if (currentRoll>=0){
                output = currentRoll+"";
            }
            if (currentRoll<0){
                output = "("+currentRoll+")";
            }
        }
        //if the die was rerolled add a (rr) to the end
			/*
			if(this.wasRerolled){
				output=output+"(rr)";
			}//*/
        return output;
    }
}

