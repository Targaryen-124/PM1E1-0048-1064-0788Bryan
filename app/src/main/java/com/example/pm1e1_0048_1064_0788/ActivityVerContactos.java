package com.example.pm1e1_0048_1064_0788;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import utilidades.ContactoAdapter;
import utilidades.Contactos;
import utilidades.SQLiteConexion;
import utilidades.Trans;

public class ActivityVerContactos extends AppCompatActivity {

  private ArrayList<Contactos> contactosList;
  private ListView listViewContactos;
  private ContactoAdapter adapter;
  private SQLiteConexion conexion;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ver_contactos);

    conexion = new SQLiteConexion(this, Trans.NameDatabase, null, 1);
    listViewContactos = findViewById(R.id.listViewContactos);
    contactosList = new ArrayList<>();

    cargarContactos();

    adapter = new ContactoAdapter(this, contactosList);
    listViewContactos.setAdapter(adapter);

    Button regresar = findViewById(R.id.buttonRegresar);
    regresar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // El intent nos llevara de nuevo al ActivityMain.
        Intent intent = new Intent(ActivityVerContactos.this, MainActivity.class);
        startActivity(intent);
      }
    });
  }

  private void cargarContactos() {
    SQLiteDatabase db = conexion.getReadableDatabase();
    Cursor cursor = db.rawQuery("SELECT * FROM " + Trans.tablacontactos, null);

    if (cursor.moveToFirst()) {
      do {
        Contactos contacto = new Contactos();
        contacto.setId(cursor.getInt(0));
        contacto.setNombre(cursor.getString(1));
        contacto.setPais(cursor.getString(2));
        contacto.setNumero(cursor.getString(3));
        contacto.setNota(cursor.getString(4));
        contacto.setImagen(cursor.getString(5));
        contactosList.add(contacto);
      } while (cursor.moveToNext());
    }
    cursor.close();
  }
}