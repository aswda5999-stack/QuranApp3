package com.example.qurannative.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

// العلامات المرجعية
@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahName: String,
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

// عداد التسبيح
@Entity(tableName = "tasbih")
data class TasbihEntity(
    @PrimaryKey
    val id: Int = 1,
    val count: Long = 0,
    val target: Long = 100,
    val updatedAt: Long = System.currentTimeMillis()
)

// آخر موضع للقراءة
@Entity(tableName = "reading_state")
data class ReadingStateEntity(
    @PrimaryKey
    val id: Int = 1,
    val surahNumber: Int = 1,
    val ayahNumber: Int = 1,
    val pageNumber: Int = 1,
    val updatedAt: Long = System.currentTimeMillis()
)

// عمليات العلامات المرجعية
@Dao
interface BookmarkDao {

    @Query("SELECT * FROM bookmarks ORDER BY createdAt DESC")
    fun getAll(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM bookmarks")
    suspend fun deleteAll()
}

// عمليات التسبيح
@Dao
interface TasbihDao {

    @Query("SELECT * FROM tasbih WHERE id = 1")
    fun getCounter(): Flow<TasbihEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(counter: TasbihEntity)

    @Query(
        """
        UPDATE tasbih
        SET count = count + 1,
            updatedAt = :time
        WHERE id = 1
        """
    )
    suspend fun increment(time: Long = System.currentTimeMillis())

    @Query(
        """
        UPDATE tasbih
        SET count = 0,
            updatedAt = :time
        WHERE id = 1
        """
    )
    suspend fun reset(time: Long = System.currentTimeMillis())
}

// عمليات آخر موضع قراءة
@Dao
interface ReadingStateDao {

    @Query("SELECT * FROM reading_state WHERE id = 1")
    fun getLastPosition(): Flow<ReadingStateEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(state: ReadingStateEntity)
}

// قاعدة البيانات
@Database(
    entities = [
        BookmarkEntity::class,
        TasbihEntity::class,
        ReadingStateEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class QuranDatabase : RoomDatabase() {

    abstract fun bookmarkDao(): BookmarkDao

    abstract fun tasbihDao(): TasbihDao

    abstract fun readingStateDao(): ReadingStateDao
}
