package cn.kirilenkoo.www.coolpets.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.base.BaseActivity
import cn.kirilenkoo.www.coolpets.model.Tag
import cn.kirilenkoo.www.coolpets.repository.TagRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.HashMap

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rep = TagRepository()
        val ob: Observable<List<Tag>> = rep.fetchData(HashMap()) as Observable<List<Tag>>
        Log.d("activity","observable created")
        ob.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result -> Log.d("result", result.size.toString())}
                )
    }
}
