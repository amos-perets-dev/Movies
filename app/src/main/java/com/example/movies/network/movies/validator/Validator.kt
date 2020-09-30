package com.example.movies.network.movies.validator

import com.example.movies.movie_app.shared.ISharedPerf
import com.example.movies.utils.DateUtil
import org.jetbrains.annotations.TestOnly

class Validator(private val sharedPerf: ISharedPerf) : IValidator{
    override fun isValidSizePage(totalPage: Int): Boolean = totalPage > 1

    var time = 0L

    @TestOnly
    fun isNeedUpdate(lastUpdate : Long): Boolean {
        if (lastUpdate != 0L) {
            val lastUpdatePlus24Hours = DateUtil().getCurrDatePlus24Hours(lastUpdate)

            return lastUpdatePlus24Hours <= System.currentTimeMillis()
        }

        return true
    }

    override fun isNeedUpdateImages(): Boolean {
        val lastUpdate = sharedPerf.getLastUpdate()

        if (lastUpdate != null && lastUpdate != 0L) {
            val lastUpdatePlus24Hours = DateUtil().getCurrDatePlus24Hours(lastUpdate)

            return lastUpdatePlus24Hours <= System.currentTimeMillis()

        }

        return true
    }
}