package com.example.shadow4.dnddamagecalculator;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

//TODO saving/loading profiles
//TODO pass parameters to diceHandler and get damage when button pressed
//TODO alert dialog showing damage

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    Button calculateButton,saveButton,loadButton;
    public static final String PRESET_FILE_NAME = "PresetSaves";
    CheckBox savageAttack,extraDice,gwf;
    EditText name, damEditText, extraDiceEditText, gwfEditText;
    Spinner presetDropdown;
    static final int numPresets = 8;
    static final String sharedPreferencesRegex = ":";
    String[] presetNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //start up spinner
        presetDropdown = (Spinner)findViewById(R.id.presetSpinner);

        //start up checkboxes
        savageAttack = (CheckBox)findViewById(R.id.savageattackcheckbox);
        extraDice = (CheckBox)findViewById(R.id.extradicecheckbox);
        gwf = (CheckBox)findViewById(R.id.gwfrerollscheckbox);

        //start up editTexts
        name = (EditText)findViewById(R.id.presetname);
        damEditText = (EditText)findViewById(R.id.damageformulaentry);
        extraDiceEditText = (EditText)findViewById(R.id.extradiceentry);
        gwfEditText = (EditText)findViewById(R.id.gwfrerolls);

        //start all buttons and link them to respective functions
        calculateButton = (Button)findViewById(R.id.calculatedamagebutton);
        calculateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v){
                //TODO calculate damage and show alert here
                String alertTitle = "Hello";
                String alertOutput = "";
                try{
                    String damageFormula = damEditText.getText().toString();
                    String extraDicFormula = extraDiceEditText.getText().toString();
                    if (!extraDice.isChecked())extraDicFormula = "";
                    String gwfRerolls = gwfEditText.getText().toString();
                    if (!gwf.isChecked()) gwfRerolls = "";
                    StringBuilder sb = new StringBuilder();
                    //sanatize inputs
                    damageFormula = damageFormula.replaceAll("[^0-9d+-]", "");
                    extraDicFormula = extraDicFormula.replaceAll("[^0-9d+-]", "");
                    gwfRerolls = gwfRerolls.replaceAll("[^0-9,]", "");
                    //TODO REmove this debug shit
                    //alertOutput = damageFormula+"\n"+extraDicFormula+"\n"+gwfRerolls;
                    sb.append(genReadableDamage(damageFormula,extraDicFormula,gwfRerolls));
                    if(savageAttack.isChecked()){
                        sb.append("\nOR\n");
                        sb.append(genReadableDamage(damageFormula,extraDicFormula,gwfRerolls));
                    }
                    alertOutput = sb.toString();
                    alertTitle = "Damage Done!";


                }
                catch(Exception e){
                    alertTitle = "Error!";
                    alertOutput = "There was a problem reading one or more fields";
                    e.printStackTrace();
                }


                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(alertTitle)
                        .setMessage(alertOutput)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //doesnt really need to do much
                            }
                        })
                        .setNegativeButton("", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_dialer)
                        .show();
            }
        });

        saveButton = (Button)findViewById(R.id.savePresetButton);
        saveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                saveProfile();
                //TODO save presets to shared preferences here
            }
        });

        loadButton = (Button)findViewById(R.id.loadPresetButton);
        loadButton.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v){
                loadProfile();
                //TODO load preset here
            }
        });


        //load.dewit

        //TODO delete this once you implement preset loading
        //presetNames = new String[]{"1","2","3"};


        //check to see if the preset files exist, if not create standard boring ones
        SharedPreferences presets = getSharedPreferences(PRESET_FILE_NAME,0);
        SharedPreferences.Editor presetEditor = presets.edit();
        for(int i=0;i<numPresets;i++){
            String temp = presets.getString(i+"","NONE");
            if (temp.equals("NONE")){
                Preset tempPreset = new Preset();
                presetEditor.putString(i+"",tempPreset.toString(sharedPreferencesRegex));
            }
        }
        presetEditor.commit();
        updateSpinner();

    }

    private void updateSpinner(){
        //TODO load preset names from file here
        SharedPreferences presets = getSharedPreferences(PRESET_FILE_NAME,0);
        presetNames = new String[numPresets];
        for (int i=0;i<numPresets;i++){
            String preset = presets.getString(i+"","ERROR TALK TO SAM");
            String[] presetSplit = preset.split(sharedPreferencesRegex);
            int actualNum = i+1;
            presetNames[i] = actualNum+": "+presetSplit[0];
        }
        ArrayAdapter<String> presetAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,presetNames);
        presetDropdown.setAdapter(presetAdapter);
    }

    private void loadProfile(){
        int slot = presetDropdown.getSelectedItemPosition();
        slot = slot+0;

        SharedPreferences presets = getSharedPreferences(PRESET_FILE_NAME,0);
        String presetString = presets.getString(slot+"","FAIL");
        Preset loadedProfile = new Preset(presetString,sharedPreferencesRegex);
        name.setText(loadedProfile.name);
        damEditText.setText(loadedProfile.damFormula);
        extraDiceEditText.setText(loadedProfile.extraDiceFormula);
        gwfEditText.setText(loadedProfile.gwfRerolls);
        savageAttack.setChecked(loadedProfile.savageAttack);
        extraDice.setChecked(loadedProfile.useExtraDice);
        gwf.setChecked(loadedProfile.gwf);

    }

    private void saveProfile(){
        SharedPreferences presets = getSharedPreferences(PRESET_FILE_NAME,0);
        SharedPreferences.Editor presetEditor = presets.edit();

        //(String name, String damFormula, String extraDiceFormula, String gwfRerolls, Boolean savageAttack, Boolean useExtraDice, Boolean gwf)
        Preset savePreset = new Preset(
                name.getText().toString(),
                damEditText.getText().toString(),
                extraDiceEditText.getText().toString(),
                gwfEditText.getText().toString(),
                savageAttack.isChecked(),
                extraDice.isChecked(),
                gwf.isChecked()
        );
        int slot = presetDropdown.getSelectedItemPosition();
        presetEditor.putString(slot+"",savePreset.toString(sharedPreferencesRegex));

        presetEditor.commit();
        updateSpinner();
        presetDropdown.setSelection(slot);
    }

    private String genReadableDamage(String damFormula, String extraDiceFormula, String rerolls){
        StringBuilder sb = new StringBuilder();

        //parse reroll numbers
        int[] rerollNums;
        if (rerolls.equals("")){
            rerollNums = null;
        }
        else{
            String[] rrsplit = rerolls.split(",");
            rerollNums = new int[rrsplit.length];
            for (int i=0;i<rrsplit.length;i++){
                rerollNums[i] = Integer.parseInt(rrsplit[i]);
            }
        }

        //calculate the damage!
        ArrayList<Die> diceRolled = new ArrayList<Die>();
        Die[] tempDieHolder = diceHandler.calculateDamage(damFormula,rerollNums);
        for (Die d: tempDieHolder){
            diceRolled.add(d);
        }
        //extra damage now
        if (!extraDiceFormula.equals("")){
            tempDieHolder = diceHandler.calculateDamage(extraDiceFormula,rerollNums);
            for (Die d: tempDieHolder){
                diceRolled.add(d);
            }
        }
        //format as string and tally up damage
        int totalDamage = 0;
        for (Die d:diceRolled){
            sb.append(d.toString());
            sb.append("+");
            totalDamage+=d.currentRoll;
        }

        //remove trailing + and tack on the total damage
        sb.deleteCharAt(sb.length()-1);
        sb.append("=");
        sb.append(totalDamage);
        sb.append(" Total Damage");

        return sb.toString();
    }
}
