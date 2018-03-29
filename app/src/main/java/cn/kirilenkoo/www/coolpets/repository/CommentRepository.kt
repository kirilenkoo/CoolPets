package cn.kirilenkoo.www.coolpets.repository

import android.util.Log
import cn.kirilenkoo.www.coolpets.api.NetworkDataSource
import cn.kirilenkoo.www.coolpets.model.Comment
import com.jakewharton.rx.replayingShare
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by huangzilong on 2018/3/22.
 */
class CommentRepository{
    /*override */fun fetchData(params: HashMap<String, Any>): Single<*> {
        val srcObservable: Single<List<Comment>> = NetworkDataSource.fetchPostComments(params["postId"] as String?).cache()
        srcObservable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe (
                        {result -> Log.d("save","size %s comments save to db"+result.size)},
                        {error -> Log.d("error",error.message)}
                )
        return srcObservable
    }
}