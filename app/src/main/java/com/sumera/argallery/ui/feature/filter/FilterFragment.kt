package com.sumera.argallery.ui.feature.filter

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.view.animation.TranslateAnimation
import androidx.view.doOnPreDraw
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.checkedChanges
import com.sumera.argallery.R
import com.sumera.argallery.data.store.ui.model.Filter
import com.sumera.argallery.tools.SimpleAnimatorListener
import com.sumera.argallery.tools.extensions.setVisibile
import com.sumera.argallery.tools.observables.rangeChanges
import com.sumera.argallery.ui.base.BaseFragment
import com.sumera.argallery.ui.feature.filter.contract.CloseEvent
import com.sumera.argallery.ui.feature.filter.contract.FilterState
import com.sumera.argallery.ui.feature.filter.contract.OnBackButtonClicked
import com.sumera.argallery.ui.feature.filter.contract.OnCloseAreaClicked
import com.sumera.argallery.ui.feature.filter.contract.OnCloseButtonClicked
import com.sumera.argallery.ui.feature.filter.contract.OnFirstCategoryStateChanged
import com.sumera.argallery.ui.feature.filter.contract.OnPriceRangeChanged
import com.sumera.argallery.ui.feature.filter.contract.OnResetButtonClicked
import com.sumera.argallery.ui.feature.filter.contract.OnSecondCategoryStateChanged
import com.sumera.argallery.ui.feature.filter.contract.OnThirdCategoryStateChanged
import com.sumera.argallery.ui.feature.filter.contract.OnYearRangeChanged
import com.sumera.koreactor.reactor.MviReactor
import com.sumera.koreactor.reactor.data.MviEvent
import com.sumera.koreactor.util.extension.getChange
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_filter.*
import javax.inject.Inject

class FilterFragment : BaseFragment<FilterState>() {

    @Inject lateinit var reactorFactory: FilterReactorFactory

    companion object {
        val animationDuration = 300L

        fun newInstance(): FilterFragment {
            return FilterFragment()
        }
    }

    override val layoutRes = R.layout.fragment_filter

    override fun createReactor(): MviReactor<FilterState> {
        return getReactor(reactorFactory, FilterReactor::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filter = Filter.createDefault()
        filter_year.tickStart = filter.minYear.toFloat()
        filter_year.tickEnd = filter.maxYear.toFloat()
        filter_price.tickStart = filter.minPrice.toFloat()
        filter_price.tickEnd = filter.maxPrice.toFloat()

        filter_root.clicks()
                .map { OnCloseButtonClicked }
                .bindToReactor()

        filter_back.clicks()
                .map { OnCloseAreaClicked }
                .bindToReactor()

        filter_reset.clicks()
                .map { OnResetButtonClicked }
                .bindToReactor()

        filter_price.rangeChanges().skip(1)
                .map { OnPriceRangeChanged(it.first, it.second) }
                .bindToReactor()

        filter_year.rangeChanges().skip(1)
                .map { OnYearRangeChanged(it.first, it.second) }
                .bindToReactor()

        filter_firstCategory.checkedChanges().skip(1)
                .map { OnFirstCategoryStateChanged(it) }
                .bindToReactor()

        filter_secondCategory.checkedChanges().skip(1)
                .map { OnSecondCategoryStateChanged(it) }
                .bindToReactor()

        filter_thirdCategory.checkedChanges().skip(1)
                .map { OnThirdCategoryStateChanged(it) }
                .bindToReactor()
    }

    override fun onResume() {
        super.onResume()

        showBackground()
        showContent()
    }

    fun close() {
        sendAction(OnBackButtonClicked)
    }

    override fun bindToState(stateObservable: Observable<FilterState>) {
        stateObservable.getChange { it.currentFilter }
                .observeState { filter ->
                    val minPriceChanged = filter_price.leftPinValue.toInt() != filter.minPrice
                    val maxPriceChanged = filter_price.rightPinValue.toInt() != filter.maxPrice
                    if (minPriceChanged || maxPriceChanged) {
                        filter_price.post {
                            filter_price.setRangePinsByValue(filter.minPrice.toFloat(), filter.maxPrice.toFloat())
                        }
                    }

                    val minYearChanged = filter_year.leftPinValue.toInt() != filter.minYear
                    val maxYearChanged = filter_year.rightPinValue.toInt() != filter.maxYear
                    if (minYearChanged || maxYearChanged) {
                        filter_year.post {
                            filter_year.setRangePinsByValue(filter.minYear.toFloat(), filter.maxYear.toFloat())
                        }
                    }

                    filter_firstCategory.isChecked = filter.firstCategoryEnabled
                    filter_secondCategory.isChecked = filter.secondCategoryEnabled
                    filter_thirdCategory.isChecked = filter.thirdCategoryEnabled
                }
    }

    override fun bindToEvent(eventsObservable: Observable<MviEvent<FilterState>>) {
        eventsObservable.observeEvent { event ->
            when(event) {
                CloseEvent -> {
                    hideBackground()
                    hideContentAndRemoveFragment()
                }
            }
        }
    }

    private fun showBackground() {
        val animator = ObjectAnimator.ofInt(filter_root, "backgroundColor", Color.parseColor("#00000000"), Color.parseColor("#88000000"));
        animator.duration = FilterFragment.animationDuration
        animator.setEvaluator(ArgbEvaluator())
        animator.start()
    }

    private fun showContent() {
        filter_content.doOnPreDraw {
            val translateAnimation = TranslateAnimation(filter_content.x, filter_content.x, -filter_content.height.toFloat(), 0f)
            translateAnimation.duration = FilterFragment.animationDuration
            translateAnimation.interpolator = OvershootInterpolator()
            filter_content.startAnimation(translateAnimation)

            filter_overshootFilledSpace.postDelayed({
                filter_overshootFilledSpace.setVisibile(true)
            }, 50)
        }
    }

    private fun hideBackground() {
        val translateAnimation = TranslateAnimation(filter_content.x, filter_content.x,
                0f, -filter_content.height.toFloat())
        translateAnimation.duration = FilterFragment.animationDuration
        translateAnimation.interpolator = AccelerateDecelerateInterpolator()
        filter_content.startAnimation(translateAnimation)
    }

    private fun hideContentAndRemoveFragment() {
        val animator = ObjectAnimator.ofInt(filter_root, "backgroundColor", Color.parseColor("#88000000"), Color.parseColor("#00000000"));
        animator.duration = FilterFragment.animationDuration
        animator.setEvaluator(ArgbEvaluator())
        animator.addListener(object : SimpleAnimatorListener() {
            override fun onAnimationEnd(p0: Animator?) {
                fragmentManager?.beginTransaction()?.remove(this@FilterFragment)?.commit()
            }
        })
        animator.start()

        filter_overshootFilledSpace.setVisibile(false)
    }
}