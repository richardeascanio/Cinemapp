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
import androidx.recyclerview.widget.LinearLayoutManager
import com.richard.cinemapp.adapters.NowPlayingMoviesAdapter
import com.richard.cinemapp.adapters.UpcomingMoviesAdapter
import com.richard.cinemapp.databinding.FragmentMoviesBinding
import com.richard.cinemapp.utils.Constants.DEFAULT_REGION
import com.richard.cinemapp.utils.Constants.USA_REGION
import com.richard.cinemapp.utils.NetworkListener
import com.richard.cinemapp.utils.NetworkResult
import com.richard.cinemapp.utils.SliderTransformer
import com.richard.cinemapp.viewModels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MoviesViewModel
    private val upcomingAdapter by lazy { UpcomingMoviesAdapter() }
    private val nowPlayingAdapter by lazy { NowPlayingMoviesAdapter() }
    private val topRatedAdapter by lazy { NowPlayingMoviesAdapter() }
    private val popularAdapter by lazy { NowPlayingMoviesAdapter() }

    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MoviesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)

        // Upcoming Movies
        setUpViewPager()

        // Set up recycler views
        setUpNowPlayingRecyclerView()
        setUpTopRatingRecyclerView()
        setUpPopularRecyclerView()

        // Subscribe observers
        subscribeObservers()

        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    viewModel.networkStatus = status
                    viewModel.showNetworkStatus()
                    getUpcomingMovies()
                    getNowPlayingMovies()
                    getTopRatedMovies()
                    getPopularMovies()
                }
        }

        return binding.root
    }

    private fun setUpViewPager() = binding.upcomingMoviesViewPager.apply {
        val offscreenLimit = 3
        adapter = upcomingAdapter
        offscreenPageLimit = offscreenLimit
        setPageTransformer(SliderTransformer(offscreenLimit))
    }

    private fun setUpNowPlayingRecyclerView() = binding.nowPlayingRecyclerView.apply {
        adapter = nowPlayingAdapter
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setUpTopRatingRecyclerView() = binding.topRatedRecyclerView.apply {
        adapter = topRatedAdapter
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setUpPopularRecyclerView() = binding.popularRecyclerView.apply {
        adapter = popularAdapter
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun subscribeObservers() {
        // BACK ONLINE
        viewModel.readBackOnline.observe(viewLifecycleOwner) {
            viewModel.backOnline = it
        }
    }

    private fun getUpcomingMovies() {
        lifecycleScope.launch {
            viewModel.getUpcomingMovies(viewModel.applyQueries(USA_REGION))
            viewModel.upcomingMoviesResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        response.data?.let {
                            upcomingAdapter.setData(it.movies)
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
                    }
                }
            }
        }
    }

    private fun getNowPlayingMovies() {
        lifecycleScope.launch {
            viewModel.getNowPlayingMovies(viewModel.applyQueries(USA_REGION))
            viewModel.nowPlayingMoviesResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        response.data?.let {
                            Log.d("debug", "getNowPlayingMovies: ${it.movies}")
                            nowPlayingAdapter.setData(it.movies)
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
                    }
                }
            }
        }
    }

    private fun getTopRatedMovies() {
        lifecycleScope.launch {
            viewModel.getTopRatedMovies(viewModel.applyQueries(DEFAULT_REGION))
            viewModel.topRatedMoviesResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        response.data?.let {
                            Log.d("debug", "getTopRatedMovies: ${it.movies}")
                            topRatedAdapter.setData(it.movies)
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
                    }
                }
            }
        }
    }

    private fun getPopularMovies() {
        lifecycleScope.launch {
            viewModel.getPopularMovies(viewModel.applyQueries(DEFAULT_REGION))
            viewModel.popularMoviesResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        response.data?.let {
                            Log.d("debug", "getPopularMovies: ${it.movies}")
                            popularAdapter.setData(it.movies)
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
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}