package com.tapan.avomatest.data.model

import android.os.Parcelable
import com.tapan.avomatest.data.story.StoryEntity
import kotlinx.parcelize.Parcelize
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element


@Parcelize
data class Story constructor(
    var title: String? = null,
    var link: String? = null,
    var image: String? = null,
    var publishDate: String? = null,
    var description: String? = null,
    var feedUrl: String? = null,
    var isBookMarked: Boolean = false
) : Parcelable {
    fun toStoryEntity() = StoryEntity(title, link, image, publishDate, description, feedUrl)


    fun parseImage(): String {
        image?.let {
            return it
        } ?: kotlin.run {
            val doc: Document = Jsoup.parse(description.orEmpty())
            val imageElement: Element? = doc.select("img")?.first()

            val absoluteUrl: String? = imageElement?.absUrl("src") //absolute URL on src
            val srcValue: String? = imageElement?.attr("src")
            return srcValue.orEmpty();
        }
    }
}