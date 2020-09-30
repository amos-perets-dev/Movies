package com.example.movies.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ImagesDataList(@PrimaryKey var idList : Int = 0) : RealmObject() {
    lateinit var imagesList : RealmList<ImageData>
}