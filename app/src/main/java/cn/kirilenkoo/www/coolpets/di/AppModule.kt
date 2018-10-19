package cn.kirilenkoo.www.coolpets.di

import android.app.Application
import androidx.room.Room
import cn.kirilenkoo.www.coolpets.db.*
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

    @Singleton
    @Provides
    fun providePostReplyDao(db: CoolPetDb): PostReplyDao{
        return db.postReplyDao()
    }
}