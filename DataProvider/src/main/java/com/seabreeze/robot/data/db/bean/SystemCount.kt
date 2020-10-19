package com.seabreeze.robot.data.db.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * User: milan
 * Time: 2020/4/9 9:17
 * Des:
 * 数据库示例
 */
@Entity(
    tableName = "system_count",
    indices = [Index(value = ["count"])]
)
data class SystemCount(
    //名称
    @PrimaryKey
    @ColumnInfo(name = "title")
    val title: String,

    //个数
    @ColumnInfo(name = "count")
    var count: Long = 0,

    //年月日
    @ColumnInfo(name = "year")
    val year: Int = 0,
    @ColumnInfo(name = "month")
    val month: Int = 0,
    @ColumnInfo(name = "day")
    val day: Int = 0
) {
    override fun toString(): String {
        return "VoiceCount(title='$title', count=$count, year=$year, month=$month, day=$day)"
    }
}