package io.woong.filmpedia.ui.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import io.woong.filmpedia.data.people.PersonSummary

object RecyclerViewBindingAdapters {

    @JvmStatic
    @BindingAdapter("list_item")
    fun RecyclerView.bindListItem(list: List<Any>?) {
        when (this.adapter) {
            is io.woong.filmpedia.ui.page.people.PeopleListAdapter -> {
                val adapter = this.adapter as io.woong.filmpedia.ui.page.people.PeopleListAdapter
                if (list != null) {
                    adapter.people = list as List<PersonSummary>
                }
            }
        }
    }
}
