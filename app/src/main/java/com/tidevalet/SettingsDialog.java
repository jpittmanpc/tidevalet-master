package com.tidevalet;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Justin on 4/25/16.
 */
public class SettingsDialog extends Dialog implements View.OnClickListener {
    private static final int ACTION_SAVE = 1;
    private static final int ACTION_CANC = 0;
    private boolean Saved;
    private Context context;
    private EditText editUsername;
    private EditText editPassword;
    private EditText editURL;
    private Button btnSave;
    public SettingsDialog(Context context) {
        super(context);
        initializeUI(context);
    }
    private void initializeUI(Context context) {
        this.context = context;
        setContentView(R.layout.settings_activity);
        editURL = (EditText) findViewById(R.id.editUrl);
        editUsername = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);
        Button btnSave = (Button) findViewById(R.id.btnSave);
        //noinspection ResourceType
        btnSave.setId(ACTION_SAVE);
        btnSave.setOnClickListener(this);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setId(ACTION_CANC);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case ACTION_CANC:
                Saved = false;
                dismiss();
                break;
            case ACTION_SAVE:
                if (TextUtils.isEmpty(editURL.getText().toString().trim())) {
                    Toast.makeText(context, "Enter URL",
                            Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(editUsername.getText().toString()
                        .trim())) {
                    Toast.makeText(context, "Enter Username",
                            Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(editPassword.getText().toString()
                        .trim())) {
                    Toast.makeText(context, "Enter Password",
                            Toast.LENGTH_LONG).show();
                } else {
                        SessionManager utils = new SessionManager(context);
                        utils.setDefUsr(editUsername.getText().toString().trim());
                        utils.setDefPwd(editPassword.getText().toString().trim());
                        utils.setDefUrl(editURL.getText().toString().trim());
                    }
                Saved = true;
                dismiss();
                break;
        }
    }
}
