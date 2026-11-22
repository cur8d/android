package com.amrabed.skeleton

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.amrabed.skeleton.databinding.ItemDetailsFragmentBinding
import com.bumptech.glide.Glide

/**
 * Fragment to show the details of the selected item from the item list
 */
class ItemDetailsFragment : Fragment() {
    private var _binding: ItemDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ItemViewModel by lazy {
        ViewModelProvider(activity as ViewModelStoreOwner).get(ItemViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ItemDetailsFragmentBinding.inflate(inflater, parent, false)
        viewModel.getSelected().observe(viewLifecycleOwner, Observer { item ->
            binding.title.text = item.title
            binding.summary.text = item.summary
            Glide.with(this).load(item.image).centerCrop().placeholder(R.drawable.placeholder)
                .into(binding.image)

            binding.edit.setOnClickListener {
                parentFragmentManager.beginTransaction().replace(R.id.fragment, ItemEditFragment())
                    .addToBackStack(null).commit()
            }
        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
