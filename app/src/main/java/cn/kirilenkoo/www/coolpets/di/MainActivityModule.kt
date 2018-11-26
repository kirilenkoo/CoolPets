package cn.kirilenkoo.www.coolpets.di

import cn.kirilenkoo.www.coolpets.ui.activities.MainActivity
import cn.kirilenkoo.www.coolpets.ui.activities.SignInActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeSignInActivity(): SignInActivity
    // can add more activity
}