package com.example.workshophub.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.workshophub.models.Workshop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Workshop::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val workshopDao : WorkshopDao

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var workshopDao = database.workshopDao

                    // Delete all content here.
                    workshopDao.deleteAll()

                    // Add sample words.
                    var word = Workshop("Coding Workshop","65e8082d95e3868901a4234d","06/03/2024",false)
                    workshopDao.insert(word)
                    word = Workshop("Academics Workshop","65e807f795e3868901a42344","06/03/2024",false)
                    workshopDao.insert(word)
                    word = Workshop("Technical Workshop","65e807e595e3868901a42341","06/03/2024",false)
                    workshopDao.insert(word)
                    word = Workshop("Robotics","65e8090495e3868901a42356","06/03/2024",false)
                    workshopDao.insert(word)
                    word = Workshop("Blockchain","65e808ea95e3868901a42353","06/03/2024",false)
                    workshopDao.insert(word)
                    word = Workshop("AI Workshop","65e8093695e3868901a4235c","06/03/2024",false)
                    workshopDao.insert(word)
                }
            }
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "workshop_db"
                )
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}