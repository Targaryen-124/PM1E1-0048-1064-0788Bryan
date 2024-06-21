package com.example.pm1e1_0048_1064_0788;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pm1e1_0048_1064_0788.Transacciones.Contactos;
import com.example.pm1e1_0048_1064_0788.Transacciones.Transacciones;
import com.example.pm1e1_0048_1064_0788.conexion.SQLiteConexion;

import java.util.ArrayList;

public class ActivityP2Lista extends AppCompatActivity {

    SQLiteConexion conexion;
    Button regresar, verimagen, actualizar, eliminar, compartir, llamar;
    ListView contactos;
    ArrayList<Contactos> listaContactos;
    ArrayList<String> arregloContactos;
    ArrayAdapter adp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_p2_lista);
        conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        regresar = (Button) findViewById(R.id.btnRegresar);
        verimagen = (Button) findViewById(R.id.btnImagenC);
        contactos = ( ListView ) findViewById(R.id.lstContactos);
        actualizar = (Button) findViewById(R.id.btnActualizarC);
        eliminar = (Button) findViewById(R.id.btnEliminarC);
        compartir = (Button) findViewById(R.id.btnCompartirC);
        llamar = (Button) findViewById(R.id.btnLlamarC);


        ObtenerListaContactos();

        adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arregloContactos);
        contactos.setAdapter(adp);

        contactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int color = Color.parseColor("#aed1f2");
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
                if (seleccionado != ListView.INVALID_POSITION){
                    String nombre = listaContactos.get(seleccionado).getNombre();
                    String foto = listaContactos.get(seleccionado).getImagen();
                    Intent intent = new Intent(ActivityP2Lista.this, ActivityP2Foto.class);
                    intent.putExtra("foto", foto);
                   intent.putExtra("nombre", nombre);
                    startActivity(intent);
                }
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int seleccionado = contactos.getCheckedItemPosition();
                if (seleccionado != ListView.INVALID_POSITION){
                    int id = listaContactos.get(seleccionado).getId();
                    Actualizar(id);
                }
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int seleccionado = contactos.getCheckedItemPosition();
                if (seleccionado != ListView.INVALID_POSITION){
                    int id = listaContactos.get(seleccionado).getId();
                    Eliminar(id);
                    ObtenerListaContactos();
                    adp = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, arregloContactos);
                    contactos.setAdapter(adp);
                }
            }
        });

        compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int seleccionado = contactos.getCheckedItemPosition();
                if (seleccionado != ListView.INVALID_POSITION){
                    int id = listaContactos.get(seleccionado).getId();
                    String nombre = listaContactos.get(seleccionado).getNombre();
                    String numero = listaContactos.get(seleccionado).getNumero();
                    String nota = listaContactos.get(seleccionado).getNota();
                    Compartir(id, nombre, numero, nota);
                }
            }
        });

        llamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int seleccionado = contactos.getCheckedItemPosition();
                if (seleccionado != ListView.INVALID_POSITION){
                    String nombre = listaContactos.get(seleccionado).getNombre();
                    String numero = listaContactos.get(seleccionado).getNumero();
                    mostrarDialog(nombre, numero);
                }
            }
        });
    }

    public void Actualizar(int id){
        Intent intent = new Intent(ActivityP2Lista.this, MainActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void Eliminar(int id){
        SQLiteDatabase db = conexion.getReadableDatabase();
        String[] argumentos = { String.valueOf(id) };
        String condicion = "id = ?";
        db.delete(Transacciones.tablacontactos, condicion, argumentos);
        Toast.makeText(this, "Registro Eliminado Correctamente", Toast.LENGTH_SHORT).show();
        db.close();
    }

    public void ObtenerListaContactos()
    {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Contactos contacto = null;
        listaContactos = new ArrayList<Contactos>();

        // Cursor
        Cursor cursor = db.rawQuery("SELECT * FROM contactos", null );

        while(cursor.moveToNext())
        {
            contacto = new Contactos();

            contacto.setId(cursor.getInt(0));
            contacto.setNombre(cursor.getString(1));
            contacto.setPais(cursor.getString(2));
            contacto.setNumero(cursor.getString(3));
            contacto.setNota(cursor.getString(4));
            contacto.setImagen(cursor.getString(5));

            listaContactos.add(contacto);
        }

        Toast.makeText(this, "Contactos en Total: " + listaContactos.size(), Toast.LENGTH_SHORT).show();

        cursor.close();
        llenarLista();
    }

    public void llenarLista()
    {
        arregloContactos = new ArrayList<String>();
        for(int i = 0; i < listaContactos.size(); i++)
        {
            arregloContactos.add(listaContactos.get(i).getId() + " | "+
                    listaContactos.get(i).getNombre() + " | "+
                    listaContactos.get(i).getNumero() + " | " +
                    listaContactos.get(i).getPais());
        }
    }

    public void Compartir(int id, String nombre, String numero, String nota){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, id  + " | " + nombre +  " | " + numero + " | " + nota);
        startActivity(Intent.createChooser(intent, "Compartir por:"));
    }

    public void Llamar(String numero){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel: " + numero.substring(4)));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            return;
        }
        startActivity(intent);
    }

    public void mostrarDialog(String nombre, String numero){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Desea llamar a: " + nombre + "?");
        builder.setMessage(numero);
        builder.setPositiveButton("Llamar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Llamar(numero);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog confirmacion = builder.create();
        confirmacion.show();
    }

}