package cn.kirilenkoo.www.coolpets.base

import android.app.Activity
import android.app.Application
import cn.kirilenkoo.www.coolpets.di.AppInjector
import cn.kirilenkoo.www.coolpets.di.DaggerAppComponent
import cn.kirilenkoo.www.coolpets.model.Pet
import com.avos.avoscloud.AVOSCloud
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by huangzilong on 2018/3/22.
 */
class BaseApplication: Application(), HasActivityInjector {
    //dagger android
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    override fun activityInjector() = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
        init3party()
        initDagger()
        Timber.plant(Timber.DebugTree())
    }

    private fun initDagger() {
        AppInjector.init(this)
    }

    private fun init3party(){
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"YgW3MyhV2i9C1dTQwutYMiPO-gzGzoHsz","nBhyhlrlMlJ3e3c6cQsBQDTx")
        AVOSCloud.setDebugLogEnabled(true)
    }
}