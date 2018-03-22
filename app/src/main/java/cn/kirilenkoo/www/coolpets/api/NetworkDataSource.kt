package cn.kirilenkoo.www.coolpets.api

import android.util.Log
import cn.kirilenkoo.www.coolpets.model.Tag
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVQuery
import io.reactivex.Observable

/**
 * Created by huangzilong on 2018/3/22.
 */
class NetworkDataSource {
    companion object {
        fun fectchTags():Observable<List<Tag>>{
            return Observable.create<List<Tag>> {
                val query = AVQuery<AVObject>("Tag")
                val avList = query.find()
                val tagList = mutableListOf<Tag>()
                for( v in avList){
                    v.getString("name")?.let {
                        tagList.add(Tag(it))
                    }
                }
                Log.d("fetchTags","tagfetched")
                it.onNext(
                    tagList
                )
            }
        }
    }
}