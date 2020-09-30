package com.example.movies.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

public open class ImageData(@PrimaryKey var moveId : Long = 0L,
                     var encode : String = "") : RealmObject() {
}