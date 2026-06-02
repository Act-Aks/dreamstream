package com.dreamstream.core.database.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.dreamstream.core.database.entity.BookmarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM Bookmarks ORDER BY created_at DESC")
    fun observeAll(): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM Bookmarks WHERE category = :category ORDER BY created_at DESC")
    fun observeByCategory(category: String): Flow<List<BookmarkEntity>>

    @Query("SELECT COUNT(*) > 0 FROM Bookmarks WHERE url = :url AND provider_id = :providerId")
    suspend fun isBookmarked(url: String, providerId: String): Boolean

    @Query("SELECT * FROM Bookmarks WHERE url = :url AND provider_id = :providerId")
    suspend fun getByUrl(url: String, providerId: String): BookmarkEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: BookmarkEntity)

    @Query("UPDATE Bookmarks SET category = :category WHERE id = :id")
    suspend fun updateCategory(id: String, category: String)

    @Query("DELETE FROM Bookmarks WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM Bookmarks")
    suspend fun deleteAll()

    @Query("SELECT category, COUNT(*) as count FROM Bookmarks GROUP BY category")
    suspend fun countByCategory(): List<CategoryCount>

    data class CategoryCount(val category: String, val count: Int)
}
