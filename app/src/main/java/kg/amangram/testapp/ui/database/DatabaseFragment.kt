package kg.amangram.testapp.ui.database

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kg.amangram.testapp.R
import kg.amangram.testapp.adapters.AlbumsAdapter
import kg.amangram.testapp.databinding.FragmentDatabaseBinding
import org.koin.android.viewmodel.ext.android.viewModel

class DatabaseFragment : Fragment() {

    private val viewModel: DatabaseViewModel by viewModel()
    private var binding: FragmentDatabaseBinding? = null
    private lateinit var albumsAdapter: AlbumsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_database, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDatabaseBinding.bind(view)
        setAdapter()
        getData()
    }

    private fun setAdapter() {
        albumsAdapter = AlbumsAdapter ({},{
            viewModel.delete(it)
        })
        albumsAdapter.setIsDatabase(true)
        val mLinearLayout = LinearLayoutManager(activity)
        binding?.rvAlbums?.apply {
            adapter = albumsAdapter
            layoutManager = mLinearLayout
        }
    }

    private fun getData() {
        viewModel.albums.observe(viewLifecycleOwner, Observer {
            albumsAdapter.swapData(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}