package cn.kirilenkoo.www.coolpets.di

import android.app.Application
import cn.kirilenkoo.www.coolpets.base.BaseApplication
import cn.kirilenkoo.www.coolpets.ui.activities.MainActivity
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidInjectionModule::class
            , MainActivityModule::class
        ]
)
interface AppComponent {
    //TODO: unknow this builder
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
    fun inject(app: BaseApplication)
}