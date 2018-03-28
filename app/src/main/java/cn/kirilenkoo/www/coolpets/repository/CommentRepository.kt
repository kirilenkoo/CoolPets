package cn.kirilenkoo.www.coolpets.repository

import cn.kirilenkoo.www.coolpets.api.NetworkDataSource
import cn.kirilenkoo.www.coolpets.model.Comment
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by huangzilong on 2018/3/22.
 */
class CommentRepository: Repository {
    override fun fetchData(params: HashMap<String, Any>): Observable<*> {
        val srcObservable: Observable<List<Comment>> = NetworkDataSource.fetchPostComments(params["postId"] as String?).share()
        srcObservable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe (
                        {result -> Timber.d("size %s comments save to db",result.size)},
                        {error -> Timber.e(error.message)}
                )
        return srcObservable
    }
}