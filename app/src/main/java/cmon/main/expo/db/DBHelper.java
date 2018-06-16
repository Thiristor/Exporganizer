package cmon.main.expo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.main.expo.beans.Categoria;
import com.main.expo.beans.Evento;
import com.main.expo.beans.Item;

/**
 * Created by Sergio on 05/02/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    private String sqlCreate = "CREATE TABLE " + Categoria.TABLE_NAME
            + " (" + Categoria._ID + " INTEGER PRIMARY KEY,"
            + Categoria.COLUMN_NAME_TITLE + " TEXT,"
            + Categoria.COLUMN_NAME_DESCRIPTION + " TEXT,"
            + Categoria.COLUMN_NAME_IMAGE + " TEXT)";

    private String sqlCreate2 = "CREATE TABLE " + Item.TABLE_NAME
            + " (" + Item._ID + " INTEGER PRIMARY KEY,"
            + Item.COLUMN_NAME_TITLE + " TEXT,"
            + Item.COLUMN_NAME_DESCRIPTION + " TEXT,"
            + Item.COLUMN_NAME_IMAGE + " INTEGER,"
            + Item.COLUMN_NAME_QUANTITY + " INTEGER,"
            + Item.COLUMN_NAME_SOLD + " INTEGER,"
            + Item.COLUMN_NAME_PRICE + " FLOAT,"
            + Item.COLUMN_NAME_SERIES + " TEXT,"
            + Item.COLUMN_NAME_CATEGORY + " INTEGER)";

    private String sqlCreateEvent = "CREATE TABLE " + Evento.TABLE_NAME
            + " (" + Evento._ID + " INTEGER PRIMARY KEY,"
            + Evento.COLUMN_NAME_TITLE + " TEXT,"
            + Evento.COLUMN_NAME_DESCRIPTION + " TEXT,"
            + Evento.COLUMN_NAME_IMAGE + " INTEGER,"
            + Evento.COLUMN_NAME_INVENTORY + " INTEGER)";

    public DBHelper(Context contexto, String nombre,
                    SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
        db.execSQL(sqlCreate2);
        db.execSQL(sqlCreateEvent);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS " + Categoria.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Item.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Evento.TABLE_NAME);

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
        db.execSQL(sqlCreate2);
        db.execSQL(sqlCreateEvent);
    }
}
