package cn.kirilenkoo.www.coolpets.di

import cn.kirilenkoo.www.coolpets.ui.activities.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity
    // can add more activity
}