package com.amrabed.skeleton

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.amrabed.skeleton.core.Item
import com.amrabed.skeleton.databinding.ItemListElementBinding
import com.amrabed.skeleton.databinding.ItemListFragmentBinding
import com.amrabed.skeleton.swipe.DragListener
import com.amrabed.skeleton.swipe.SwipeHandler
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

/**
 * List Fragment using RecyclerView to display list of Items loaded using ViewModel.
 * List supports the following operations:
 *  - Add new item
 *  - Swipe to refresh
 *  - Swipe items left and right to delete and hide
 *  - Drag items to move in list
 */
class ItemListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private var _binding: ItemListFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ItemViewModel by lazy {
        ViewModelProvider(activity as ViewModelStoreOwner).get(ItemViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = ItemListFragmentBinding.inflate(inflater, container, false)
        loadItems()
        binding.swipeToRefresh.setOnRefreshListener(this)
        binding.addFab.setOnClickListener(this)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRefresh() {
        _binding?.swipeToRefresh?.isRefreshing = true
        loadItems()
        _binding?.swipeToRefresh?.isRefreshing = false
    }

    /**
     * Called when the FAB is clicked -> Add new item
     */
    override fun onClick(view: View?) {
        val item = Item()
        viewModel.select(item)
        parentFragmentManager.beginTransaction().replace(R.id.fragment, ItemEditFragment())
            .addToBackStack(null).commit()
        viewModel.add(item)
    }

    private fun loadItems() {
        viewModel.itemList.observe(viewLifecycleOwner, Observer { items ->
            binding.listView.adapter = ItemListAdapter(items)
        })
    }

    inner class ItemListAdapter(private val items: ArrayList<Item>) :
        RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>(), DragListener {
        private val touchHelper: ItemTouchHelper =
            ItemTouchHelper(SwipeHandler(requireContext(), this))

        init {
            touchHelper.attachToRecyclerView(binding.listView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val itemBinding = ItemListElementBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ItemViewHolder(itemBinding)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val item = items[position]
            holder.binding.title.text = item.title
            holder.binding.summary.text = item.summary

            Glide.with(this@ItemListFragment).load(item.image).placeholder(R.drawable.placeholder)
                .circleCrop()
                .into(holder.binding.icon)

            holder.itemView.setOnClickListener {
                selectItem(item)
            }

            holder.itemView.setOnLongClickListener {
                onDrag(holder)
                false
            }
        }

        override fun onDrag(holder: RecyclerView.ViewHolder) {
            touchHelper.startDrag(holder)
        }

        override fun onItemMoved(
            source: RecyclerView.ViewHolder,
            destination: RecyclerView.ViewHolder
        ) {
            val fromPos = source.bindingAdapterPosition
            val toPos = destination.bindingAdapterPosition
            if (fromPos != RecyclerView.NO_POSITION && toPos != RecyclerView.NO_POSITION) {
                notifyItemMoved(fromPos, toPos)
            }
        }

        override fun onItemRemoved(holder: RecyclerView.ViewHolder) {
            val position = holder.bindingAdapterPosition
            if (position == RecyclerView.NO_POSITION) return
            val item = items[position]
            items.removeAt(position)
//            viewModel.delete(position)
            notifyItemRemoved(position)
            // Show undo message to allow user to undo deletion
            Snackbar.make(binding.root, R.string.itemRemoved, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo) {
                    items.add(position, item)
//                    viewModel.add(item)
                    notifyItemInserted(position)
                }
                .show()
        }

        override fun onItemHidden(holder: RecyclerView.ViewHolder) {
            val position = holder.bindingAdapterPosition
            if (position == RecyclerView.NO_POSITION) return
            items.removeAt(position)
            notifyItemRemoved(position)
        }

        private fun selectItem(item: Item) {
            viewModel.select(item)
            parentFragmentManager.beginTransaction().replace(R.id.fragment, ItemDetailsFragment())
                .addToBackStack(null).commit()
        }

        inner class ItemViewHolder(val binding: ItemListElementBinding) :
            RecyclerView.ViewHolder(binding.root)
    }
}
