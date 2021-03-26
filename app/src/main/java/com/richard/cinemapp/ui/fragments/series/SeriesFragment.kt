package com.richard.cinemapp.ui.fragments.series

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.richard.cinemapp.adapters.PopularSeriesAdapter
import com.richard.cinemapp.databinding.FragmentSeriesBinding
import com.richard.cinemapp.utils.Constants.API_KEY
import com.richard.cinemapp.utils.NetworkListener
import com.richard.cinemapp.utils.NetworkResult
import com.richard.cinemapp.utils.SliderTransformer
import com.richard.cinemapp.viewModels.SeriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SeriesFragment : Fragment() {

    private var _binding: FragmentSeriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SeriesViewModel
    private val popularAdapter by lazy { PopularSeriesAdapter() }

    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SeriesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSeriesBinding.inflate(inflater, container, false)

        // Popular movies
        setUpViewPager()

        // Subscribe observers
        subscribeObservers()

        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    viewModel.networkStatus = status
                    viewModel.showNetworkStatus()
                    getPopularSeries()
                }
        }

        return binding.root
    }

    private fun setUpViewPager() = binding.popularSeriesViewPager.apply {
        val offscreenLimit = 3
        adapter = popularAdapter
        offscreenPageLimit = offscreenLimit
        setPageTransformer(SliderTransformer(offscreenLimit))
    }

    private fun subscribeObservers() {
        // BACK ONLINE
        viewModel.readBackOnline.observe(viewLifecycleOwner) {
            viewModel.backOnline = it
        }
    }

    private fun getPopularSeries() {
        lifecycleScope.launch {
            viewModel.getPopularSeries(API_KEY)
            viewModel.popularSeriesResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        response.data?.let {
                            Log.d("debug", "getPopularSeries: ${it.series}")
                            popularAdapter.setData(it.series)
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