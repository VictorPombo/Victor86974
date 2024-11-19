package com.theavengers.victorpombo86974;

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DicaDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dicas.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_DICAS = "dicas"
        const val COLUMN_ID = "id"
        const val COLUMN_TITULO = "titulo"
        const val COLUMN_DESCRICAO = "descricao"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_DICAS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITULO TEXT NOT NULL,
                $COLUMN_DESCRICAO TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DICAS")
        onCreate(db)
    }

    fun insertDica(titulo: String, descricao: String) {
        val db = writableDatabase


        val cursor = db.query(
            TABLE_DICAS,
            arrayOf(COLUMN_ID),
            "$COLUMN_TITULO = ?",
            arrayOf(titulo),
            null,
            null,
            null
        )

        if (!cursor.moveToFirst()) {
            val values = ContentValues().apply {
                put(COLUMN_TITULO, titulo)
                put(COLUMN_DESCRICAO, descricao)
            }
            db.insert(TABLE_DICAS, null, values)
        }
        cursor.close()
    }

    fun removeDuplicatas() {
        val db = writableDatabase
        db.execSQL("""
            DELETE FROM $TABLE_DICAS 
            WHERE $COLUMN_ID NOT IN (
                SELECT MIN($COLUMN_ID) 
                FROM $TABLE_DICAS 
                GROUP BY $COLUMN_TITULO
            )
        """)
    }

    fun deleteDica(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_DICAS, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    fun getAllDicas(): List<Dica> {
        val db = readableDatabase
        val dicas = mutableListOf<Dica>()
        val cursor = db.query(TABLE_DICAS, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val titulo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO))
            val descricao = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRICAO))
            dicas.add(Dica(id, titulo, descricao))
        }
        cursor.close()
        return dicas
    }
}
