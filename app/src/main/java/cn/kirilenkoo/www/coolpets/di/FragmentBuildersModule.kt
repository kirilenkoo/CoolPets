package cn.kirilenkoo.www.coolpets.di

import cn.kirilenkoo.www.coolpets.ui.fragments.MainFragment
import cn.kirilenkoo.www.coolpets.ui.fragments.PostDetailFragment
import cn.kirilenkoo.www.coolpets.ui.fragments.PostListFragment
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
}