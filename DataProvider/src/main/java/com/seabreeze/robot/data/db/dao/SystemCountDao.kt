package com.seabreeze.robot.data.db.dao

import androidx.room.*
import com.seabreeze.robot.data.db.bean.SystemCount
import io.reactivex.Flowable

/**
 * User: milan
 * Time: 2020/4/9 10:51
 * Des:
 */
@Dao
interface SystemCountDao {
    //增
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: SystemCount): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(objs: List<SystemCount>)

    //删
    @Delete
    fun delete(obj: SystemCount): Int

    @Query("DELETE FROM 'system_count' WHERE title= :title")
    fun delete(title: String): Int

    @Query("DELETE FROM 'system_count'")
    fun deleteAll()

    //改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(obj: SystemCount): Int

    //查
    @Query("SELECT * FROM 'system_count'")
    suspend fun loadAll(): List<SystemCount>

    @Query("SELECT * FROM 'system_count'")
    fun findAll(): Flowable<List<SystemCount>>

    @Query("SELECT * FROM 'system_count' WHERE year= :year & month = :month & day = :day")
    fun queryVoiceGivenDay(year: Int, month: Int, day: Int): List<SystemCount>

    @Query("SELECT * FROM 'system_count' WHERE title= :voiceTitle & year= :year & month = :month & day = :day")
    fun queryVoiceTitleGivenDay(voiceTitle: String, year: Int, month: Int, day: Int): SystemCount?

    @Query("SELECT * FROM 'system_count' WHERE title Like '%'||:title||'%'")
    suspend fun queryLikeByKey(title: String): List<SystemCount>
}