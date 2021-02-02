package kg.amangram.testapp.ui.photos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kg.amangram.testapp.Constants
import kg.amangram.testapp.R
import kg.amangram.testapp.adapters.PhotoAdapter
import kg.amangram.testapp.data.State
import kg.amangram.testapp.data.model.AlbumItem
import kg.amangram.testapp.databinding.PhotosFragmentBinding
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PhotosFragment : Fragment() {

    private val viewModel: PhotosViewModel by viewModel {
        parametersOf(arguments?.getParcelable<AlbumItem>(Constants.ALBUM)?.id)
    }
    private var binding: PhotosFragmentBinding? = null
    private lateinit var albumItem: AlbumItem
    private lateinit var photoAdapter: PhotoAdapter
    private var isExists = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.photos_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PhotosFragmentBinding.bind(view)
        albumItem = arguments?.getParcelable(Constants.ALBUM)!!
        binding?.tvTitle?.text = albumItem.title
        setAdapter()
        getData()
        observeSaving()
        binding?.ibSave?.setOnClickListener {
            if (isExists)
                viewModel.delete(albumItem)
            else
                viewModel.save(albumItem)
        }
    }

    private fun setAdapter() {
        photoAdapter = PhotoAdapter { }
        val mLinearLayout = LinearLayoutManager(activity)
        binding?.rvPhotos?.apply {
            adapter = photoAdapter
            layoutManager = mLinearLayout
        }
    }

    private fun observeSaving() {
        viewModel.isExist.observe(viewLifecycleOwner, {
            isExists = it
            if (it) {
                binding?.ibSave?.setImageResource(R.drawable.ic_baseline_bookmark_24)
            } else {
                binding?.ibSave?.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
            }
        })
    }

    private fun getData() {
        viewModel.getPhotos().observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is State.Success -> {
                    photoAdapter.swapData(state.data)
                }
                is State.Failed ->{
                    Toast.makeText(context,state.message,Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}