package com.example.movies.repo.images

import android.graphics.Bitmap
import android.util.Log
import com.example.movies.model.ImageData
import com.example.movies.model.ImagesDataList
import com.example.movies.movie_app.shared.ISharedPerf
import com.example.movies.network.movies.manager.IMoviesNetworkManager
import com.example.movies.network.movies.validator.IValidator
import com.example.movies.utils.ImageUtil
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.realm.Realm
import io.realm.RealmList
import java.util.*
import kotlin.collections.HashMap

class ImagesRepository(
    private val validator: IValidator,
    private val moviesNetworkManager: IMoviesNetworkManager,
    private val imageUtil: ImageUtil,
    private val sharedPerf: ISharedPerf
) : IImagesRepository {

    private val imagesData = BehaviorSubject.create<Map<Long, Bitmap>>()

    override fun initImages(): Observable<Boolean> {

        val defaultInstance = Realm.getDefaultInstance()
        val imagesMap = HashMap<Long, Bitmap>()

        return Observable.create<Boolean> { emiiter ->
            defaultInstance.executeTransaction { realm ->

                val findAll = realm.where(ImagesDataList::class.java).findAll()

                if (findAll.isNotEmpty()) {
                    val imagesDataList = findAll.first()

                    imagesDataList?.imagesList?.forEach {
                        val image = imageUtil.decodeBase64(it.encode)
                        imagesMap[it.moveId] = image!!
                    }
                }
                imagesData.onNext(imagesMap)
                emiiter.onNext(true)
            }
        }
    }

    override fun getImagesAsync(): Observable<Map<Long, Bitmap>>? {

        val imagesServer = moviesNetworkManager.getImagesDataAsync()
        val imagesDb = imagesData.hide()

        return if (validator.isNeedUpdateImages()) imagesServer else imagesDb

    }


    override fun addImages(): Observable<Boolean>? {

        if (validator.isNeedUpdateImages().not()) return Observable.just(true)

        return moviesNetworkManager
            .completeImages()
            .flatMap { imagesData ->

                val defaultInstance = Realm.getDefaultInstance()
                val realmList = RealmList<ImageData>()

                val imagesList = imagesData.map {
                    val encodeBase64Image = imageUtil.encodeBase64Image(it.value)
                    ImageData(it.key, encodeBase64Image.toString())
                }
                    .toList()

                realmList.addAll(imagesList)

                val imagesDataList = ImagesDataList(111)
                imagesDataList.imagesList = realmList

                return@flatMap Observable.create<Boolean> { emitter ->

                    defaultInstance.executeTransaction { realm ->

                        realm.insertOrUpdate(imagesDataList)

                        emitter.onNext(true)
                    }
                }
            }
            ?.doOnNext { sharedPerf.addLastUpdate(Date().time) }
            ?.map { true }
    }

}