package nefersky.notepadpreferencesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private static final String FILENAME = "Sample.txt";
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = (EditText)findViewById(R.id.editText);

        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.OnSharedPreferenceChangeListener listener = (new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals("cat_food")) {
                    String catFood = sharedPreferences.getString(key, "");
                    catFood = catFood + "\n";

                    String myText = mEditText.getText().toString();
                    Toast.makeText(getApplicationContext(), myText, Toast.LENGTH_SHORT).show();
                    myText = myText + catFood;
                    Toast.makeText(getApplicationContext(), myText, Toast.LENGTH_SHORT).show();
                    mEditText.setText(myText);
                    Toast.makeText(getApplicationContext(), mEditText.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPreferences.getBoolean(getString(R.string.pref_open_mode), false)){
            openFile(FILENAME);
        }

        float fSize = Float.parseFloat(sharedPreferences.getString(getString(R.string.pref_size), "20"));
        mEditText.setTextSize(fSize);

        String regular = sharedPreferences.getString(getString(R.string.pref_style), "");
        int typeface = Typeface.NORMAL;
        if(regular.contains("Полужирный")){
            typeface += Typeface.BOLD;
        }
        if(regular.contains("Курсив")){
            typeface += Typeface.ITALIC;
        }
        mEditText.setTypeface(null, typeface);

        int color = Color.BLACK;
        if(sharedPreferences.getBoolean("text_color_black", false)){
            color += Color.BLACK;
        }
        if(sharedPreferences.getBoolean("text_color_red", false)){
            color += Color.RED;
        }
        if(sharedPreferences.getBoolean("text_color_blue", false)){
            color += Color.BLUE;
        }
        if(sharedPreferences.getBoolean("text_color_green", false)){
            color += Color.GREEN;
        }
        mEditText.setTextColor(color);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuOpen:
                openFile(FILENAME);
                return true;
            case R.id.mnuSave:
                saveFile(FILENAME);
                return true;
            case R.id.action_settings:
                Intent intent = new Intent();
                intent.setClass(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return true;
        }
    }

    private void openFile(String fileName){
        try{
            InputStream inputStream = openFileInput(fileName);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                StringBuilder stringBuilder = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

                inputStream.close();
                mEditText.setText(stringBuilder.toString());
            }
        }
        catch (Throwable t){
            Toast.makeText(getApplicationContext(), "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveFile(String fileName){
        try{
            OutputStream outputStream = openFileOutput(fileName, 0);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(mEditText.getText().toString());
            outputStreamWriter.close();
        }
        catch (Throwable t){
            Toast.makeText(getApplicationContext(), "Exception: " + t.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
