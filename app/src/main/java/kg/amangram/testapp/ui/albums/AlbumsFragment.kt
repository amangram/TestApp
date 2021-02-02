package kg.amangram.testapp.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kg.amangram.testapp.Constants
import kg.amangram.testapp.R
import kg.amangram.testapp.adapters.AlbumsAdapter
import kg.amangram.testapp.data.State
import kg.amangram.testapp.databinding.FragmentAlbumsBinding
import org.koin.android.viewmodel.ext.android.viewModel

class AlbumsFragment : Fragment() {

    private val viewModel: AlbumsViewModel by viewModel()
    private var binding: FragmentAlbumsBinding? = null
    private lateinit var albumAdapter: AlbumsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_albums, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAlbumsBinding.bind(view)
        setAdapter()
        getAlbums()
    }

    private fun setAdapter() {
        albumAdapter = AlbumsAdapter ({
            val bundle = bundleOf(Constants.ALBUM to it)
            findNavController().navigate(R.id.action_navigation_albums_to_photosFragment, bundle)
        },{})
        val mLinearLayout = LinearLayoutManager(activity)
        binding?.rvAlbums?.apply {
            adapter = albumAdapter
            layoutManager = mLinearLayout
        }
    }

    private fun getAlbums() {
        viewModel.albums.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is State.Success -> {
                    binding?.swipeRefreshAlbums?.isRefreshing = false
                    albumAdapter.swapData(state.data)
                }
                is State.Loading->{
                    binding?.swipeRefreshAlbums?.isRefreshing = true
                }
                is State.Failed->{
                    Toast.makeText(context,state.message,Toast.LENGTH_SHORT).show()
                    binding?.swipeRefreshAlbums?.isRefreshing = false
                }
            }
        })
    }


}