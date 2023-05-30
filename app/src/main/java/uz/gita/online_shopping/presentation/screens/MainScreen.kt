package uz.gita.online_shopping.presentation.screens

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.online_shopping.R
import uz.gita.online_shopping.databinding.ScreenMainBinding
import uz.gita.online_shopping.presentation.viewmodels.MainViewModel
import uz.gita.online_shopping.presentation.viewmodels.impl.MainViewModelImpl
import uz.gita.online_shopping.utils.extensions.*

// Created by Jamshid Isoqov an 10/10/2022
@AndroidEntryPoint
class MainScreen : Fragment(R.layout.screen_main) {

    private val viewBinding: ScreenMainBinding by viewBinding()

    private var oldSum = 0.0

    private lateinit var valueAnimator: ValueAnimator

    private val viewModel: MainViewModel by viewModels<MainViewModelImpl>()


    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewBinding.mainPager.adapter = MainPagerAdapter(requireActivity())
        viewBinding.mainPager.isUserInputEnabled = false

        viewBinding.bottomNavMain.setOnItemSelectedListener {
            val page = when (it.itemId) {
                R.id.homeScreen -> {
                    0
                }
                R.id.ordersScreen -> {
                    1
                }
                else -> {
                    2
                }
            }
            viewBinding.mainPager.setCurrentItem(page, true)
            true
        }

        viewModel.loadingLiveData.observe(viewLifecycleOwner) {
            if (it)
                showProgress()
            else
                hideProgress()
        }

        viewModel.basketFlow.observe(viewLifecycleOwner) {
            if (it == 0.0) {
                viewBinding.containerBasket.gone()
            } else {
                viewBinding.containerBasket.visible()
            }
            tvPriceAnimator(it.toFloat())
        }

        viewBinding.containerBasket.setOnClickListener {
            viewModel.navigateBasket()
        }
    }

    @SuppressLint("Recycle")
    private fun tvPriceAnimator(value: Float) {
        valueAnimator = ValueAnimator.ofFloat(oldSum.toFloat(), value).apply {
            addUpdateListener {
                viewBinding.tvProductsPrice.text =
                    (it.animatedValue as Float).toDouble().getFinanceType()
            }
            duration = 1000L
            start()
        }
        oldSum = value.toDouble()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (valueAnimator.isRunning)
            valueAnimator.cancel()
    }
}