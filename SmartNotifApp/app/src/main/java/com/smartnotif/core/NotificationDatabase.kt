package com.smartnotif.core

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// ── DAO ───────────────────────────────────────────────────────────────────
@Dao
interface NotificationDao {

    @Insert
    suspend fun insert(entity: NotificationEntity): Long

    @Query("SELECT * FROM notifications ORDER BY timestampMs DESC")
    fun observeAll(): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications ORDER BY timestampMs DESC LIMIT :limit")
    suspend fun getRecent(limit: Int = 100): List<NotificationEntity>

    @Query("SELECT COUNT(*) FROM notifications")
    suspend fun totalCount(): Int

    @Query("SELECT COUNT(*) FROM notifications WHERE isImportant = 1")
    suspend fun importantCount(): Int

    @Query("SELECT AVG(importanceScore) FROM notifications")
    suspend fun avgScore(): Float?

    @Query("SELECT COUNT(*) FROM notifications WHERE source = :src")
    suspend fun countBySource(src: String): Int

    @Query("DELETE FROM notifications")
    suspend fun clearAll()

    @Query("DELETE FROM notifications WHERE id = :id")
    suspend fun deleteById(id: Long)
}

// ── Database ──────────────────────────────────────────────────────────────
@Database(entities = [NotificationEntity::class], version = 1, exportSchema = false)
abstract class NotificationDatabase : RoomDatabase() {
    abstract fun dao(): NotificationDao

    companion object {
        @Volatile private var INSTANCE: NotificationDatabase? = null

        fun getInstance(context: android.content.Context): NotificationDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    NotificationDatabase::class.java,
                    "smart_notif_db"
                ).build().also { INSTANCE = it }
            }
    }
}
