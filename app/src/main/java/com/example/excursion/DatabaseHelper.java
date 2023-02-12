package com.example.excursion;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DB_PATH = ""; // полный путь к базе данных
    private static final String DB_NAME = "excursion.db"; // имя файла бызы даннх
    private static final int DB_VERSION = 10; // версия базы данных
    private static final String SIGHTS_TABLE_NAME = "sights"; // название таблицы в бд
    private static final String ROUTES_TABLE_NAME = "routes";
    private SQLiteDatabase database;
    private final Context context;
    private boolean needUpdate = false;

    DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            assert context != null;
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            assert context != null;
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }

//        assert context != null;
//        DB_PATH = context.getFilesDir().getPath() + DB_NAME;

        this.context = context;
        copyDataBase();
        this.getReadableDatabase();
    }

    void updateDataBase() throws IOException {
        if (needUpdate) {
            File dbFile = new File(DB_PATH + DB_NAME);
            if (dbFile.exists())
                dbFile.delete();

            copyDataBase();
            needUpdate = false;
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private void copyDBFile() throws IOException {
        InputStream mInput = context.getAssets().open(DB_NAME);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        database = SQLiteDatabase.openDatabase(
                DB_PATH + DB_NAME,
                null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return database != null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            needUpdate = true;
    }

    Cursor readAllSightsData() {
        String querySights = "SELECT * FROM " + SIGHTS_TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(querySights, null);
        }

        return cursor;
    }

    Cursor readAllRoutesData() {
        String queryRoutes = "SELECT * FROM " + ROUTES_TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(queryRoutes, null);
        }

        return cursor;
    }
}




