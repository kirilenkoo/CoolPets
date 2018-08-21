package cn.kirilenkoo.www.coolpets.di

import android.app.Application
import android.arch.persistence.room.Room
import cn.kirilenkoo.www.coolpets.db.CoolPetDb
import cn.kirilenkoo.www.coolpets.db.PostContentDao
import cn.kirilenkoo.www.coolpets.db.PostDao
import cn.kirilenkoo.www.coolpets.db.PostWithContentsDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideDb(app: Application): CoolPetDb {
        return Room
                .databaseBuilder(app, CoolPetDb::class.java, "coolpet.db")
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun providePostDao(db: CoolPetDb): PostDao {
        return db.postDao()
    }

    @Singleton
    @Provides
    fun providePostWithContentsDao(db: CoolPetDb): PostWithContentsDao {
        return db.postWithContentsDao()
    }

    @Singleton
    @Provides
    fun providePostContentDao(db: CoolPetDb): PostContentDao {
        return db.postContentDao()
    }
}