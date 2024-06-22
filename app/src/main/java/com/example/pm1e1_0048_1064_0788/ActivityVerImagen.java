package com.example.pm1e1_0048_1064_0788;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityVerImagen extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ver_imagen);

    ImageView imageView = findViewById(R.id.imageViewImagen);

    String imageBase64 = getIntent().getStringExtra("imageBase64");

    Button regresar = findViewById(R.id.buttonRegresar);
    regresar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // El intent nos llevara de nuevo al ActivityVerContactos.
        Intent intent = new Intent(ActivityVerImagen.this, ActivityVerContactos.class);
        startActivity(intent);
      }
    });

    // Convertir imagen base64 a bitmap.
    if (imageBase64 != null) {
      Bitmap bitmap = convertBase64ToBitmap(imageBase64);
      if (bitmap != null) {
        imageView.setImageBitmap(bitmap);
      }
    }
  }

  // Funcion para convertir base64 a bitmap
  private Bitmap convertBase64ToBitmap(String base64Str) {
    try {
      byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);

      return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    } catch (Exception e) {
      e.printStackTrace();

      return null;
    }
  }
}