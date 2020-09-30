package com.example.movies.utils

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import io.reactivex.Observable
import io.reactivex.internal.functions.Functions
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class Typewriter : AppCompatTextView {
    private var mText: CharSequence? = null
    private var mIndex = 0
    private var mDelay: Long = 500 //Default 500ms delay

    private val isFinishAnimate = PublishSubject.create<Boolean>()

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    )

    private val mHandler = Handler()
    private val characterAdder: Runnable = object : Runnable {
        override fun run() {
            text = mText!!.subSequence(0, mIndex++)
            if (mIndex <= mText!!.length) {
                mHandler.postDelayed(this, mDelay)
            }
            isFinishAnimate.onNext(text == mText.toString())
        }
    }

    fun animateText(text: CharSequence?) {
        mText = text
        mIndex = 0
        setText("")
        mHandler.removeCallbacks(characterAdder)
        mHandler.postDelayed(characterAdder, mDelay)
    }

    fun setCharacterDelay(millis: Long) {
        mDelay = millis
    }

    fun isFinishAnimate(): Observable<Boolean>? =
        isFinishAnimate.hide()
            ?.distinctUntilChanged()
            ?.subscribeOn(Schedulers.io())
            ?.filter(Functions.equalsWith(true))
}