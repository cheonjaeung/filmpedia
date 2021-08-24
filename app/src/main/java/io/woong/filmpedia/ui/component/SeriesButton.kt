package io.woong.filmpedia.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import io.woong.filmpedia.R
import io.woong.filmpedia.data.movie.Movie
import io.woong.filmpedia.databinding.LayoutSeriesButtonBinding
import io.woong.filmpedia.util.DimensionUtil
import io.woong.filmpedia.util.UriUtil
import io.woong.filmpedia.util.isNotNullOrBlank

class SeriesButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 1
) : ConstraintLayout(context, attrs, defStyle), View.OnClickListener {

    companion object {
        val DEFAULT_RATIO: Pair<Int, Int> = Pair(4, 3)
    }

    private var _binding: LayoutSeriesButtonBinding? = null
    private val binding: LayoutSeriesButtonBinding
        get() = _binding!!

    var series: Movie.Collection? = null
        set(value) {
            field = value
            loadSeriesInfo()
        }

    private var listener: OnSeriesButtonClickListener? = null

    private var ratio: Pair<Int, Int> = Pair(0, 0)

    fun setOnSeriesButtonClickListener(listener: OnSeriesButtonClickListener) {
        this.listener = listener
    }

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        _binding = LayoutSeriesButtonBinding.inflate(inflater, this, true)

        binding.button.setOnClickListener(this)

        applyAttributes(attrs, defStyle)
    }

    private fun applyAttributes(attribute: AttributeSet?, defStyle: Int) {
        val attrs = context.obtainStyledAttributes(
            attribute,
            R.styleable.SeriesButton,
            defStyle,
            0
        )

        try {
            val ratioAttr = attrs.getString(R.styleable.SeriesButton_ratio)
            ratio = if (ratioAttr.isNotNullOrBlank()) {
                try {
                    ratioAttr as String
                    val split = ratioAttr.split(":")
                    Pair(split[0].toInt(), split[1].toInt())
                } catch (e: Throwable) {
                    DEFAULT_RATIO
                }
            } else {
                DEFAULT_RATIO
            }
        } finally {
            attrs.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = MeasureSpec.getSize(widthMeasureSpec)

        val unit = width.toFloat() / ratio.first
        val height = unit * ratio.second

        setMeasuredDimension(width, height.toInt())
    }

    private fun loadSeriesInfo() {
        series?.let { s ->
            val radiusDp = DimensionUtil.dpToPx(4, context.resources.displayMetrics)

            Glide.with(this)
                .load(UriUtil.getImageUrl(s.backdropPath))
                .apply(RequestOptions.bitmapTransform(RoundedCorners(radiusDp)))
                .placeholder(R.drawable.placeholder_backdrop)
                .into(binding.backdrop)

            binding.title.text = s.name
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == binding.button.id) {
            listener?.onSeriesButtonClick(series)
        }
    }

    interface OnSeriesButtonClickListener {
        fun onSeriesButtonClick(series: Movie.Collection?)
    }
}
