package com.example.pm1e1_0048_1064_0788;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ActivityP2Lista extends AppCompatActivity {

    Button regresar, verimagen;
    ListView contactos;
    //ArrayList<Contactos> listaContactos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_p2_lista);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        regresar = (Button) findViewById(R.id.btnRegresar);
        verimagen = (Button) findViewById(R.id.btnImagenC);
        contactos = ( ListView ) findViewById(R.id.lstContactos);

        contactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int color = Color.parseColor("#abaaad");
                view.setBackgroundColor(color);
            }
        });
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityP2Lista.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        verimagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int seleccionado = contactos.getCheckedItemPosition();
                //if (seleccionado != ListView.INVALID_POSITION){
                //    String nombre = listaContactos.get(seleccionado).getNombre();
                //    String foto = listaContactos.get(seleccionado).getImagen();
                    Intent intent = new Intent(ActivityP2Lista.this, ActivityP2Foto.class);
                //    intent.putExtra("foto", foto);
                //   intent.putExtra("nombre", nombre);
                    startActivity(intent);
                //}
            }
        });
    }
}