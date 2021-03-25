package com.richard.cinemapp.ui.fragments.movies

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.richard.cinemapp.adapters.UpcomingAdapter
import com.richard.cinemapp.databinding.FragmentMoviesBinding
import com.richard.cinemapp.utils.Constants.API_KEY
import com.richard.cinemapp.utils.NetworkResult
import com.richard.cinemapp.utils.SliderTransformer
import com.richard.cinemapp.utils.observeOnce
import com.richard.cinemapp.viewModels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MoviesViewModel
    private val mAdapter by lazy { UpcomingAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MoviesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)

        initViewPager()

        lifecycleScope.launchWhenStarted {
            viewModel.getUpcomingMovies(API_KEY)
        }

        viewModel.upcomingMoviesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    Log.i("debug", "observe: success ${response.data}")
                    response.data?.let {
                        mAdapter.setData(it.movies)
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    Log.i("debug", "observe: isLoading")
                }
            }
        }

        return binding.root
    }

    private fun initViewPager() = binding.upcomingMoviesViewPager.apply {
        val offscreenLimit = 3
        adapter = mAdapter
        offscreenPageLimit = offscreenLimit
        setPageTransformer(SliderTransformer(offscreenLimit))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}