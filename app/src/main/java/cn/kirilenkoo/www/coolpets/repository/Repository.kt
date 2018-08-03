package cn.kirilenkoo.www.coolpets.repository

import io.reactivex.Observable

/**
 * Created by huangzilong on 2018/3/22.
 */
@Deprecated("not use rx for fetch data anymore")
interface Repository {
    fun fetchData(params: HashMap<String, Any>): Observable<*>

    //    override fun fetchData(params: HashMap<String, Any>): Observable<*> {
//        val srcObservable: Observable<List<Tag>> = NetworkDataSource.fetchTags().share()
//        srcObservable.subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribe (
//                        {result -> Timber.d("size %s tags save to db",result.size)},
//                        {error -> Timber.e(error.message)}
//                )
//        return srcObservable
//    }
}