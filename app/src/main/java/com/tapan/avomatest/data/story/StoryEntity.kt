package com.tapan.avomatest.data.story

import androidx.room.*
import androidx.room.ForeignKey.NO_ACTION
import androidx.room.ForeignKey.SET_DEFAULT
import com.tapan.avomatest.data.feed.FeedEntity

@Entity(
    tableName = "STORY",
    indices = [Index(value = ["FEED_URL"]), Index(value = ["LINK"], unique = true)],
    foreignKeys = [ForeignKey(
        entity = FeedEntity::class,
        parentColumns = ["LINK"],
        childColumns = ["FEED_URL"],
        onDelete = SET_DEFAULT,
        onUpdate = SET_DEFAULT
    )]
)
data class StoryEntity(
    @ColumnInfo(name = "TITLE")
    var title: String? = null,
    @ColumnInfo(name = "LINK")
    var link: String? = null,
    @ColumnInfo(name = "IMAGE")
    var image: String? = null,
    @ColumnInfo(name = "PUBLISH_DATE")
    var publishDate: String? = null,
    @ColumnInfo(name = "DESCRIPTION")
    var description: String? = null,
    @ColumnInfo(name = "FEED_URL")
    var feedUrl: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "INDEX", index = true)
    var index: Int? = null
}