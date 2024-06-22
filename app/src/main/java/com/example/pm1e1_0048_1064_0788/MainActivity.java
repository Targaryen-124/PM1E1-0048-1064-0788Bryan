package com.example.pm1e1_0048_1064_0788;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import utilidades.SQLiteConexion;
import utilidades.Trans;

public class MainActivity extends AppCompatActivity {
  static final int peticionAccesoCamera = 101;
  static final int peticionCapturaImagen = 102;
  private EditText nombre, numero, nota;
  String currentPhotoPath, image64;
  private SQLiteConexion conexion;
  Button btnCaptura, verContactos;
  private int contactoId = -1;
  ImageView ObjetoImagen;
  private Button guardar;
  private Spinner pais;

  // Opcional (reemplazo al startActivityForResult)
  ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
    @Override
    public void onActivityResult(ActivityResult result) {
      if (result.getResultCode() == Activity.RESULT_OK) {
        // There are no request codes
        Intent data = result.getData();
      }
    }
  });

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });

    conexion = new SQLiteConexion(this, Trans.NameDatabase, null, 1);

    ObjetoImagen = (ImageView) findViewById(R.id.imageView);
    btnCaptura = (Button) findViewById(R.id.tomarFoto);

    nombre = findViewById(R.id.nombre);
    numero = findViewById(R.id.numero);
    nota = findViewById(R.id.nota);
    pais = findViewById(R.id.pais);

    verContactos = findViewById(R.id.verContactos);
    guardar = findViewById(R.id.guardar);


    if (getIntent().hasExtra("id")) {
      contactoId = getIntent().getIntExtra("id", -1);
      cargarContacto(contactoId);
    }

    // Boton guardar contacto
    guardar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (contactoId == -1) {
          guardarContacto();
        } else {
          actualizarContacto(contactoId);
        }
      }
    });

    // Boton permisos para tomar fotos
    btnCaptura.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        permisos();
      }
    });

    // Boton ver contactos
    verContactos.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, ActivityVerContactos.class);
        startActivity(intent);
      }
    });
  }

  private void cargarContacto(int id) {
    SQLiteDatabase db = conexion.getReadableDatabase();

    Cursor cursor = db.rawQuery("SELECT * FROM " + Trans.tablacontactos + " WHERE id = ?", new String[]{String.valueOf(id)});

    if (cursor.moveToFirst()) {
      nombre.setText(cursor.getString(1));
      pais.setSelection(((ArrayAdapter<String>) pais.getAdapter()).getPosition(cursor.getString(2)));
      numero.setText(cursor.getString(3));
      nota.setText(cursor.getString(4));
      image64 = cursor.getString(5);

      Bitmap bitmap = convertBase64ToBitmap(image64);

      if (bitmap != null) {
        ObjetoImagen.setImageBitmap(bitmap);
      }
    }
    cursor.close();
  }

  private void actualizarContacto(int id) {
    String nombreString = nombre.getText().toString();
    String paisString = pais.getSelectedItem().toString();
    String numeroString = numero.getText().toString();
    String notaString = nota.getText().toString();

    if (!nombreString.isEmpty() && !paisString.isEmpty() && !numeroString.isEmpty() && !notaString.isEmpty()) {
      SQLiteDatabase db = conexion.getWritableDatabase();

      ContentValues values = new ContentValues();
      values.put("nombre", nombreString);
      values.put("pais", paisString);
      values.put("numero", numeroString);
      values.put("nota", notaString);
      values.put("image", image64);

      db.update(Trans.tablacontactos, values, "id = ?", new String[]{String.valueOf(id)});
      db.close();

      Toast.makeText(this, "Contacto actualizado.", Toast.LENGTH_SHORT).show();

      Intent intent = new Intent(MainActivity.this, ActivityVerContactos.class);
      startActivity(intent);
    } else {
      Toast.makeText(this, "No debe haber campos vacios.", Toast.LENGTH_SHORT).show();
    }
  }

  private void guardarContacto() {
    String nombreString = nombre.getText().toString();
    String paisString = pais.getSelectedItem().toString();
    String numeroString = numero.getText().toString();
    String notaString = nota.getText().toString();

    if (!nombreString.isEmpty() && !paisString.isEmpty() && !numeroString.isEmpty() && !notaString.isEmpty()) {
      SQLiteDatabase db = conexion.getWritableDatabase();

      ContentValues values = new ContentValues();
      values.put("nombre", nombreString);
      values.put("pais", paisString);
      values.put("numero", numeroString);
      values.put("nota", notaString);
      values.put("image", image64);

      db.insert(Trans.tablacontactos, null, values);
      db.close();

      Toast.makeText(this, "Contacto guardado", Toast.LENGTH_SHORT).show();

      // "Limpiar los datos", lleva a ActivityVerContactos
      Intent intent = new Intent(MainActivity.this, ActivityVerContactos.class);
      startActivity(intent);
    } else {
      Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
    }
  }

  private void permisos() {
    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, peticionAccesoCamera);
    } else {
      dispatchTakePictureIntent();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (requestCode == peticionAccesoCamera) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        dispatchTakePictureIntent();
      } else {
        Toast.makeText(getApplicationContext(), "Acceso Denegado!", Toast.LENGTH_LONG).show();
      }
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == peticionCapturaImagen && resultCode == RESULT_OK) {
      // Añade la imagen al ImageView
      setPic();

      galleryAddPic();

      Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);

      if (bitmap != null) {
        image64 = convertImageTo64(bitmap);
        ObjetoImagen.setImageBitmap(bitmap);
      } else {
        Log.e("Error", "Bitmap nulo.");
      }
    }
  }

  private File createImageFile() throws IOException {
    // Create an image file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

    String imageFileName = "JPEG_" + timeStamp + "_";

    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

    File image = File.createTempFile(imageFileName,  /* prefix */
            ".jpg",         /* suffix */
            storageDir      /* directory */);

    // Save a file: path for use with ACTION_VIEW intents
    currentPhotoPath = image.getAbsolutePath();
    return image;
  }

  private void dispatchTakePictureIntent() {
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    // Ensure that there's a camera activity to handle the intent
    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
      // Create the File where the photo should go
      File photoFile = null;

      try {
        photoFile = createImageFile();
      } catch (IOException ex) {
        // Error occurred while creating the File
      }

      // Continue only if the File was successfully created
      if (photoFile != null) {
        Uri photoURI = FileProvider.getUriForFile(this, "com.example.pm1e1_0048_1064_0788.fileprovider", photoFile);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

        startActivityForResult(takePictureIntent, peticionCapturaImagen);
      }
    }
  }

  private void galleryAddPic() {
    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    File f = new File(currentPhotoPath);
    Uri contentUri = Uri.fromFile(f);
    mediaScanIntent.setData(contentUri);
    this.sendBroadcast(mediaScanIntent);
  }

  private void setPic() {
    // Get the dimensions of the View
    int targetW = ObjetoImagen.getWidth();
    int targetH = ObjetoImagen.getHeight();

    // Get the dimensions of the bitmap
    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
    bmOptions.inJustDecodeBounds = true;

    BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

    int photoW = bmOptions.outWidth;
    int photoH = bmOptions.outHeight;

    // Determine how much to scale down the image
    int scaleFactor = Math.max(1, Math.min(photoW / targetW, photoH / targetH));

    // Decode the image file into a Bitmap sized to fill the View
    bmOptions.inJustDecodeBounds = false;
    bmOptions.inSampleSize = scaleFactor;
    bmOptions.inPurgeable = true;

    Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
    ObjetoImagen.setImageBitmap(bitmap);
  }

  private String convertImageTo64(Bitmap bitmap) {
    ByteArrayOutputStream byteImage = new ByteArrayOutputStream();

    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteImage);

    byte[] byteArray = byteImage.toByteArray();

    return Base64.encodeToString(byteArray, Base64.DEFAULT);
  }

  // Convierte de Base 64 a Bitmap
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