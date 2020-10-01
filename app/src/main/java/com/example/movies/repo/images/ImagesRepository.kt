package com.example.movies.repo.images

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.movies.model.ImageData
import com.example.movies.model.movie.MovieDetailsListResponse
import com.example.movies.movie_app.shared.ISharedPerf
import com.example.movies.network.base.BaseNetworkManager
import com.example.movies.network.movies.validator.IValidator
import com.example.movies.utils.ImageUtil
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.realm.Realm
import java.util.*
import kotlin.collections.HashMap

class ImagesRepository(
    validator: IValidator,
    private val imageUtil: ImageUtil,
    private val sharedPerf: ISharedPerf,
    private val defaultRealm: Realm,
    private val loaderImageHelper: LoaderImageHelper,
    private val context: Context

) : IImagesRepository {

    private val imagesDataNotifier = BehaviorSubject.create<Map<String, Bitmap>>()

    private val imagesData: HashMap<String, Bitmap> = HashMap()

    init {
        if (validator.isNeedUpdateImages()) {
            defaultRealm.executeTransaction { realm ->
                realm
                    .where(ImageData::class.java)
                    .findAll()
                    .deleteAllFromRealm()
            }
        }
    }

    private fun addImage(imagePath: String, resource: Bitmap) {

        if (imagesData[imagePath] != null) return

        imagesData[imagePath] = resource

        imagesDataNotifier.onNext(imagesData)

        addLocal(imagePath, resource)

    }

    override fun loadPicture(movieDetailsListResponse: MovieDetailsListResponse) {
        movieDetailsListResponse.moviesList
            ?.map { it.imagePath }
            ?.filter { imagePath -> imagesData[imagePath] == null }
            ?.toList()
            ?.forEach { imagePath ->

                loaderImageHelper.preloadImage(
                    context,
                    "${BaseNetworkManager.BASE_IMAGE_URL}$imagePath",
                    Consumer { addImage(imagePath.toString(), it) })
            }
    }

    private fun addLocal(movieId: String, resource: Bitmap) {
        defaultRealm.executeTransaction { realm ->

            val encodeBase64Image = imageUtil.encodeBase64Image(resource)
            val imageData = ImageData(movieId, encodeBase64Image.toString())

            realm.insertOrUpdate(imageData)
            sharedPerf.addLastUpdate(Date().time)

        }
    }

    override fun initImages(): Observable<Boolean> {

        val defaultInstance = Realm.getDefaultInstance()

        return Observable.create<Boolean> { emiiter ->
            defaultInstance.executeTransaction { realm ->

                val imagesDataList = realm.where(ImageData::class.java).findAll()

                if (imagesDataList.isNotEmpty()) {
                    imagesDataList?.forEach {
                        val image = imageUtil.decodeBase64(it.encode)
                        if (image != null) {
                            imagesData[it.moveId] = image
                        }
                    }
                }
                imagesDataNotifier.onNext(imagesData)
                emiiter.onNext(true)
            }

        }
    }

    override fun getImagesAsync(): Observable<Map<String, Bitmap>>? =
        imagesDataNotifier.hide()
            .subscribeOn(Schedulers.io())

}

