package com.tidevalet;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class App extends Application {

    private static App sInstance;
    private static Context mContext;

    public static App getInstance() {
        return sInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        App.mContext = getApplicationContext();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
    }
    public static Context getAppContext() {
        return App.mContext;
    }
    public static String getSiteUrl() {
        return getAppContext().getResources().getString(R.string.tide_default_url);
    }
}
     /*   super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        SessionManager utils = new SessionManager(App.this);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        username = (TextView) findViewById(R.id.editUsername);
        password = (TextView) findViewById(R.id.editPassword);
        remember = (CheckBox) findViewById(R.id.rememberCheckbox);
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.setDefUsr(username.getText().toString().trim());
                utils.setDefPwd(password.getText().toString().trim());
                utils.setDefUrl("tide.jpitt.xyz");
                //TODO: check loginFragment
                Intent intent = new Intent(App.this, Report.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            settingsDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void settingsDialog() {
        SettingsDialog dialog = new SettingsDialog(this);
        dialog.setTitle("Default Settings");
        dialog.show();
    }
}*/
