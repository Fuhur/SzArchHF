package archhazi.spaceshooter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;

import archhazi.spaceshooter.Communication.ServerProxy;


public class SettingsActivity extends Activity {

    private ServerProxy serverProxy = new ServerProxy();

    private SharedPreferences settings;
    private EditText nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getActionBar().hide();

        settings = getSharedPreferences(MainMenuActivity.USER_INFO, 0);
        nameText = (EditText) findViewById(R.id.name_input);

        String name = settings.getString(MainMenuActivity.PLAYER_NAME_KEY, "Player");
        nameText.setText(name);
    }

    public void saveName(View view) {
        String name = nameText.getText().toString();

        SharedPreferences.Editor editor = settings.edit();
        editor.putString(MainMenuActivity.PLAYER_NAME_KEY, name);
        editor.commit();

        serverProxy.saveName(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID), name);

        Intent intent = new Intent();
        intent.putExtra(MainMenuActivity.NAME_SETTING_KEY, name);
        setResult(RESULT_OK, intent);
        finish();

        finish();
    }
}
