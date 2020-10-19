package com.seabreeze.robot.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.seabreeze.robot.data.db.bean.SystemCount
import com.seabreeze.robot.data.db.dao.SystemCountDao

/**
 * User: milan
 * Time: 2020/4/9 9:01
 * Des:
 */
@Database(
    entities = [
        SystemCount::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun systemCount(): SystemCountDao
}