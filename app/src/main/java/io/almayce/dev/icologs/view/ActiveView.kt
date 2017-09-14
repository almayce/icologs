package io.almayce.dev.icologs.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

/**
 * Created by almayce on 29.08.17.
 */

@StateStrategyType(AddToEndSingleStrategy::class)
interface ActiveView : MvpView {
}