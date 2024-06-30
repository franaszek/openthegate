package eu.franaszek.openthegate;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
        } else {
            sendSms();
        }

        toastIt(getString(R.string.main_activity_gate_opening_toast), Toast.LENGTH_SHORT);
        finish();
    }

    private void sendSms() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String phoneNumber = sharedPreferences.getString("phone_number", "");
        String smsContent = sharedPreferences.getString("sms_content", "");

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, smsContent, null, null);
        } catch (Exception e) {
            Log.e(TAG, "error while sending sms", e);
            toastIt(getString(R.string.main_activity_gate_opening_error_toast), Toast.LENGTH_LONG);
        }
    }

    private void toastIt(String message, int length) {
        Toast.makeText(getApplicationContext(), message, length).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSms();
            } else {
                toastIt(getString(R.string.main_activity_no_permission_toast), Toast.LENGTH_SHORT);
            }
        }
    }
}
