package cn.kirilenkoo.www.coolpets.util

import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImgUploadController @Inject constructor(){
    @Inject
    lateinit var appExecutors: AppExecutors

}