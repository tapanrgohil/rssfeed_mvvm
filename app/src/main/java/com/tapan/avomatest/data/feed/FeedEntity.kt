package com.tapan.avomatest.data.feed

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "FEED",
    indices = [Index(value = ["LINK"], unique = true)]
)
data class FeedEntity(
    @ColumnInfo(name = "LINK")
    var link: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "INDEX", index = true)
    var index: Int? = null
}