package utilidades;

public class Trans {
  public static final String NameDatabase = "PM1E1004810640788";
  public static final String tablacontactos = "contactos";

  public static final String CreateDBContactos =
          "CREATE TABLE contactos (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, pais TEXT, numero TEXT, nota TEXT, image TEXT)";

  public static final String DropTableContactos = "DROP TABLE IF EXISTS contactos";
}
