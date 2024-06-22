package utilidades;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteConexion extends SQLiteOpenHelper {

  public SQLiteConexion(Context context, String dbname, SQLiteDatabase.CursorFactory factory, int version) {
    super(context, dbname, factory, version);
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(Trans.CreateDBContactos);
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    sqLiteDatabase.execSQL(Trans.DropTableContactos);
  }
}
