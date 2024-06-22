package utilidades;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pm1e1_0048_1064_0788.MainActivity;
import com.example.pm1e1_0048_1064_0788.R;

import java.util.ArrayList;
import java.util.List;

public class ContactoAdapter extends ArrayAdapter<Contactos> {
  private List<Contactos> contactosList;
  private Context context;

  public ContactoAdapter(@NonNull Context context, ArrayList<Contactos> list) {
    super(context, 0, list);
    this.context = context;
    this.contactosList = list;
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    View listItem = convertView;

    if (listItem == null) {
      listItem = LayoutInflater.from(context).inflate(R.layout.item_contacto, parent, false);
    }

    Contactos currentContacto = contactosList.get(position);

    // Nombre del contacto
    TextView nombre = listItem.findViewById(com.example.pm1e1_0048_1064_0788.R.id.textNombre);
    nombre.setText(currentContacto.getNombre());

    // Numero del contacto
    TextView numero = listItem.findViewById(R.id.textNumero);
    numero.setText(currentContacto.getNumero());

    // Actualizar
    Button buttonActualizar = listItem.findViewById(R.id.buttonActualizar);
    buttonActualizar.setOnClickListener(v -> {
      Intent intent = new Intent(context, MainActivity.class);
      intent.putExtra("id", currentContacto.getId());
      context.startActivity(intent);
    });

    // Eliminar
    Button buttonEliminar = listItem.findViewById(R.id.buttonEliminar);
    buttonEliminar.setOnClickListener(v -> {
      eliminarContacto(currentContacto.getId());
      contactosList.remove(position);
      notifyDataSetChanged();
    });

    // Compartir
    Button buttonCompartir = listItem.findViewById(R.id.buttonCompartir);
    buttonCompartir.setOnClickListener(v -> {
      Intent shareIntent = new Intent(Intent.ACTION_SEND);
      shareIntent.setType("text/plain");
      // Compartir solo el nombre
      shareIntent.putExtra(Intent.EXTRA_TEXT, currentContacto.getNombre());
      context.startActivity(Intent.createChooser(shareIntent, "Compartir contacto"));
    });

    // Llamar
    Button buttonLlamar = listItem.findViewById(R.id.buttonLlamar);
    buttonLlamar.setOnClickListener(v -> {
      Intent callIntent = new Intent(Intent.ACTION_DIAL);
      callIntent.setData(Uri.parse("tel:" + currentContacto.getNumero()));
      context.startActivity(callIntent);
    });

    return listItem;
  }

  private void eliminarContacto(int id) {
    SQLiteConexion conexion = new SQLiteConexion(context, Trans.NameDatabase, null, 1);
    SQLiteDatabase db = conexion.getWritableDatabase();

    db.delete(Trans.tablacontactos, "id = ?", new String[]{String.valueOf(id)});
    db.close();

    Toast.makeText(context, "Contacto eliminado", Toast.LENGTH_SHORT).show();
  }
}
