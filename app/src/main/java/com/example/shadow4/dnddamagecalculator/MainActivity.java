package com.example.shadow4.dnddamagecalculator;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

//TODO saving/loading profiles
//TODO pass parameters to diceHandler and get damage when button pressed
//TODO alert dialog showing damage

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    Button calculateButton,saveButton,loadButton;
    CheckBox savageAttack,extraDice,gwf;
    EditText name, damEditText, extraDiceEditText, gwfEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //start up spinner
        Spinner presetDropdown = (Spinner)findViewById(R.id.presetSpinner);

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
                String alertOutput = "";
                try{
                    String damageFormula = damEditText.getText().toString();
                    String extraDicFormula = extraDiceEditText.getText().toString();
                    String gwfRerolls = gwfEditText.getText().toString();
                    //sanatize inputs
                    damageFormula = damageFormula.replaceAll("[^0-9d+-]", "");
                    extraDicFormula = extraDicFormula.replaceAll("[^0-9d+-]", "");
                    gwfRerolls = gwfRerolls.replaceAll("[^0-9,]", "");
                    //TODO REmove this debug shit
                    alertOutput = damageFormula+"\n"+extraDicFormula+"\n"+gwfRerolls;

                    
                }
                catch(Exception e){
                    alertOutput = "There was a problem reading one or more fields";
                    e.printStackTrace();
                }


                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Hello")
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
                //TODO save presets to shared preferences here
            }
        });

        loadButton = (Button)findViewById(R.id.loadPresetButton);
        loadButton.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v){
                //TODO load preset here
            }
        });

        //TODO load preset names from file here
        //load.dewit

        //TODO delete this once you implement preset loading
        String[] presetNames = new String[]{"1","2","3"};

        //Adapt the array and apply to spinner
        ArrayAdapter<String> presetAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,presetNames);
        presetDropdown.setAdapter(presetAdapter);

    }
}
