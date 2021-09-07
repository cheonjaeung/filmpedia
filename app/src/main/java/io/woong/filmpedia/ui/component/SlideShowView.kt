package io.woong.filmpedia.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import io.woong.filmpedia.R
import io.woong.filmpedia.databinding.ComponentSlideShowBinding
import io.woong.filmpedia.databinding.ComponentSlideShowItemBinding
import io.woong.filmpedia.util.UriUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SlideShowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private var _binding: ComponentSlideShowBinding? = null
    private val binding: ComponentSlideShowBinding
        get() = _binding!!

    private var _adapter: Adapter? = null
    private val adapter: Adapter
        get() = _adapter!!

    var slides: List<String>
        get() = adapter.items
        set(value) {
            adapter.items = value
        }

    val itemCount: Int
        get() = adapter.itemCount

    var slideDelay: Long = 5000

    init {
        _binding = ComponentSlideShowBinding.inflate(LayoutInflater.from(context), this, true)
        _adapter = Adapter()

        binding.apply {
            viewPager.apply {
                this.adapter = this@SlideShowView.adapter
                registerOnPageChangeCallback(AutoSlideCallback())
            }
        }
    }

    private inner class Adapter : RecyclerView.Adapter<Adapter.ViewHolder>() {

        private var _items: MutableList<String> = mutableListOf()
        var items: List<String>
            get() = _items
            set(value) {
                _items.apply {
                    clear()
                    addAll(value)
                }
                notifyDataSetChanged()
            }

        override fun getItemCount(): Int = slides.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ComponentSlideShowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (position != RecyclerView.NO_POSITION) {
                val imagePath = slides[position]

                Glide.with(holder.itemView)
                    .load(UriUtil.getImageUrl(imagePath))
                    .placeholder(R.drawable.placeholder_backdrop)
                    .into(holder.binding.slideImage)
            }
        }

        inner class ViewHolder(val binding: ComponentSlideShowItemBinding) : RecyclerView.ViewHolder(binding.root)
    }

    private inner class AutoSlideCallback : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            if (position != RecyclerView.NO_POSITION) {
                setSlideCount(position)

                if (position < slides.lastIndex) {
                    slideAfter(slideDelay) {
                        binding.viewPager.currentItem = position + 1
                    }
                } else {
                    slideAfter(slideDelay) {
                        binding.viewPager.currentItem = 0
                    }
                }
            }
        }

        private fun setSlideCount(currentPosition: Int) {
            val maxCount = itemCount
            val counterString = "${currentPosition + 1} / $maxCount"
            binding.slideCounter.text = counterString
        }

        private fun slideAfter(milliseconds: Long, postDelayed: () -> Unit) = CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.Default).launch {
                delay(milliseconds)
            }.join()
            postDelayed()
        }
    }
}
