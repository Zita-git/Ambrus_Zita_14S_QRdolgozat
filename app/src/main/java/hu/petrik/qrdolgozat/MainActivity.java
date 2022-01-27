package hu.petrik.qrdolgozat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button scanButton, kiirButton;
    private TextView kozepreTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.setPrompt("QR code olvasása");
                intentIntegrator.setCameraId(0);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.initiateScan();


            }
        });
        kiirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (kozepreTextView.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Nem lehet üres", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        beiras(kozepreTextView.getText().toString());
                        Toast.makeText(MainActivity.this, "Sikeres fájlba írás", Toast.LENGTH_SHORT).show();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents()==null){
                Toast.makeText(this, "Kilépés", Toast.LENGTH_SHORT).show();
            }else{
                kozepreTextView.setText(result.getContents());
            }
        }


        super.onActivityResult(requestCode, resultCode, data);

    }

    public void beiras(String adat) throws IOException {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formazottDatum = dateFormat.format(date);

        String sor= String.format("%s %s", adat, formazottDatum);

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory(), "scannedCodes.csv");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.append(sor);
            writer.append(System.lineSeparator());
            writer.close();
        }
    }
    public void init(){

        scanButton = findViewById(R.id.scanButton);
        kiirButton = findViewById(R.id.kiirButton);
        kozepreTextView = findViewById(R.id.kozepreTextView);



    }
}