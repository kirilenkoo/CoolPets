package cn.kirilenkoo.www.coolpets.base

import android.app.Application
import com.avos.avoscloud.AVOSCloud

/**
 * Created by huangzilong on 2018/3/22.
 */
class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"YgW3MyhV2i9C1dTQwutYMiPO-gzGzoHsz","nBhyhlrlMlJ3e3c6cQsBQDTx");
        AVOSCloud.setDebugLogEnabled(true);
    }
}