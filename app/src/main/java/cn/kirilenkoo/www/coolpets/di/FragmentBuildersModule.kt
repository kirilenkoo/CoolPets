package cn.kirilenkoo.www.coolpets.di

import cn.kirilenkoo.www.coolpets.ui.fragments.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributePostListFragment(): PostListFragment
    @ContributesAndroidInjector
    abstract fun contributePostDetailFragment(): PostDetailFragment
    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): MainFragment
    @ContributesAndroidInjector
    abstract fun contributePostEditFragment(): PostEditFragment
    @ContributesAndroidInjector
    abstract fun contributePostPreviewFragment(): PostPreviewFragment
}