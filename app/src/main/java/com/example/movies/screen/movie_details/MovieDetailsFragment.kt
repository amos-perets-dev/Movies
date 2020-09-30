package com.example.movies.screen.movie_details

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.movies.R
import com.example.movies.screen.base_movies_activity.BaseMoviesActivity
import com.example.movies.utils.ImageUtil
import com.example.movies.utils.SwipeUtil
import com.example.movies.utils.TouchListener
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_base_movies_list.*
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.fragment_movie_details.view.*
import org.koin.android.viewmodel.ext.android.viewModel


open class MovieDetailsFragment() :
    AppCompatDialogFragment() {

    private val movieDetailsViewModel: MovieDetailsViewModel by viewModel()

    private val compositeDisposable = CompositeDisposable()

    init {
        changeFragmentState(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_movie_details, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        closeFragmentBySwipe(view.movie_name_text_view)

        poster_image_view.alpha = 0F

        description_text_view.movementMethod = ScrollingMovementMethod()



        compositeDisposable.addAll(

            movieDetailsViewModel.getMovieName(viewLifecycleOwner)
                .subscribe(movie_name_text_view::setText),

            movieDetailsViewModel.getMovieOverview(viewLifecycleOwner)
                .subscribe(description_text_view::setText),

            movieDetailsViewModel.getMovieRating(viewLifecycleOwner)
                .subscribe(rating_text_view::setText),

            movieDetailsViewModel.getMovieReleaseDate(viewLifecycleOwner)
                .subscribe(release_year_text_view::setText),

            movieDetailsViewModel.getMovieImage(viewLifecycleOwner)
                .doOnNext(poster_image_view::setImageBitmap)
                .map { ImageUtil().blurBitmapFromBitmap(it) }
                .doOnNext(background_poster_image_view::setImageBitmap)
                .subscribe {
                    poster_image_view.animate().alpha(1F).setDuration(1500).start()
                },

            movieDetailsViewModel.isMovieAllReadySaved(viewLifecycleOwner)
                .map { resources.getDrawable(it) }
                .subscribe(save_fav_list_image_view::setImageDrawable)

        )


        save_fav_list_image_view
            .setOnTouchListener(TouchListener(View.OnClickListener {

                val icon = movieDetailsViewModel.onClickSave()

                save_fav_list_image_view.setImageDrawable(resources.getDrawable(icon))

            }, 0.6f))

    }

    private fun closeFragmentBySwipe(view: View) {
        view.setOnTouchListener(SwipeUtil(context, this::closeFragment))
    }

    private fun closeFragment() {
        dismissAllowingStateLoss()
    }

    override fun onStart() {
        super.onStart()
        changeWindowSize()
    }

    override fun onDismiss(dialog: DialogInterface) {

        changeFragmentState(false)

        super.onDismiss(dialog)
    }

    private fun changeWindowSize() {
        if (dialog != null) {
            val window = dialog!!.window
            if (window != null) {

                val movieNameHeader = activity?.movie_name_header
                val height =
                    dialog!!.context.resources.displayMetrics.heightPixels - (movieNameHeader?.top
                        ?: 1)

                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, height)
                window.setWindowAnimations(R.style.PickerFragmentShowHideStyle)
                window.setGravity(Gravity.BOTTOM)
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }

    private fun changeFragmentState(isOpen: Boolean) {
        if(activity is BaseMoviesActivity){
            (activity as BaseMoviesActivity).isPickerOpen = isOpen
        }
    }

}