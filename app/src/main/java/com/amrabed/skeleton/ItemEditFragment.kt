package com.amrabed.skeleton

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.amrabed.skeleton.databinding.ItemEditFragmentBinding
import com.bumptech.glide.Glide

class ItemEditFragment : Fragment() {
    private var _binding: ItemEditFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ItemViewModel by lazy {
        ViewModelProvider(activity as ViewModelStoreOwner).get(ItemViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ItemEditFragmentBinding.inflate(inflater, parent, false)
        viewModel.getSelected().observe(viewLifecycleOwner, Observer { item ->
            binding.title.setText(item.title)
            binding.summary.setText(item.summary)
            Glide.with(this).load(item.image).centerCrop().placeholder(R.drawable.placeholder)
                .into(binding.image)

            binding.done.setOnClickListener {
                item.title = binding.title.text.toString()
                item.summary = binding.summary.text.toString()
                parentFragmentManager.popBackStack()
            }
        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}