package cn.kirilenkoo.www.coolpets.model

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

class PostWithContents {
    @Embedded
    lateinit var post: Post
    @Relation(parentColumn = "postId", entityColumn = "postId", entity = PostContent::class)
    lateinit var contentList: List<PostContent>
}