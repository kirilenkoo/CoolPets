package cn.kirilenkoo.www.coolpets.repository

import io.reactivex.Observable

/**
 * Created by huangzilong on 2018/3/22.
 */
interface Repository {
    fun fetchData(params: HashMap<String, Any>): Observable<*>
}