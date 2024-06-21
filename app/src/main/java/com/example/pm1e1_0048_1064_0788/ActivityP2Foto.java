package com.example.pm1e1_0048_1064_0788;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivityP2Foto extends AppCompatActivity {

    Button regresar;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_p2_foto);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        regresar = (Button) findViewById(R.id.btnRegresarF);
        img = (ImageView) findViewById(R.id.imgFoto);

        Intent intent = getIntent();
        String foto = intent.getStringExtra("foto");
        String nombre = intent.getStringExtra("nombre");
        getSupportActionBar().setTitle("Ver Foto de " + nombre);

        if (foto == null){
            img.setImageResource(R.drawable.usuario);
        }else{
            Bitmap b = BitmapFactory.decodeFile(foto);
            img.setImageBitmap(b);
        }

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityP2Foto.this, ActivityP2Lista.class);
                startActivity(intent);
                finish();
            }
        });
    }
}