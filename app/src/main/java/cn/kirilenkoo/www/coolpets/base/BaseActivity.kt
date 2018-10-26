package cn.kirilenkoo.www.coolpets.base

import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by huangzilong on 2018/3/22.
 */
open class BaseActivity: AppCompatActivity(){
    override fun onDestroy() {
        super.onDestroy()
        Timber.d("activity destroyed")
    }
}