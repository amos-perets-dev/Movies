package com.example.movies.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ImageData(
    @PrimaryKey var moveId: String = "",
    var encode: String = ""
) : RealmObject()