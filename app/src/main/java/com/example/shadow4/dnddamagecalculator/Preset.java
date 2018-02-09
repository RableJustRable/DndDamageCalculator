package com.example.shadow4.dnddamagecalculator;

/**
 * Created by Shadow4 on 2/7/2018.
 * Holds a preset profile for a weapon and includes methnonds for retreving it from shared preferences
 */

public class Preset {
    //entries on the screen
    String name,damFormula,extraDiceFormula,gwfRerolls;
    Boolean savageAttack,useExtraDice,gwf;

    public Preset(String name, String damFormula, String extraDiceFormula, String gwfRerolls, Boolean savageAttack, Boolean useExtraDice, Boolean gwf) {
        this.name = name;
        this.damFormula = damFormula;
        this.extraDiceFormula = extraDiceFormula;
        this.gwfRerolls = gwfRerolls;
        this.savageAttack = savageAttack;
        this.useExtraDice = useExtraDice;
        this.gwf = gwf;
    }


    private void fillPreset(String name, String damFormula, String extraDiceFormula, String gwfRerolls, Boolean savageAttack, Boolean useExtraDice, Boolean gwf) {
        this.name = name;
        this.damFormula = damFormula;
        this.extraDiceFormula = extraDiceFormula;
        this.gwfRerolls = gwfRerolls;
        this.savageAttack = savageAttack;
        this.useExtraDice = useExtraDice;
        this.gwf = gwf;
    }

    //for when its grabbed straight from shared preferences
    public Preset(String value, String regex) throws NullPointerException{
        String[] valueSplit = value.split(regex);
        fillPreset(valueSplit[0],valueSplit[1],valueSplit[2],valueSplit[3],Boolean.parseBoolean(valueSplit[4]),Boolean.parseBoolean(valueSplit[5]),Boolean.parseBoolean(valueSplit[6]));
    }

    public String toString(String regex) {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(regex);
        sb.append(damFormula);
        sb.append(regex);
        sb.append(extraDiceFormula);
        sb.append(regex);
        sb.append(gwfRerolls);
        sb.append(regex);
        sb.append(savageAttack);
        sb.append(regex);
        sb.append(useExtraDice);
        sb.append(regex);
        sb.append(gwf);
        return sb.toString();
    }
    //Default values for initial creation
    public Preset(){
        this.name = "Empty";
        this.damFormula = "2d8";
        this.extraDiceFormula = "1d6";
        this.gwfRerolls = "1,2";
        this.savageAttack = false;
        this.useExtraDice = false;
        this.gwf = false;
    }
}
