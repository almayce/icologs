package io.almayce.dev.icologs.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.almayce.dev.icologs.model.ICOcollection
import io.almayce.dev.icologs.global.PrefHelper
import io.almayce.dev.icologs.model.ICO
import io.almayce.dev.icologs.model.DatabaseReader
import io.almayce.dev.icologs.view.MainView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * Created by almayce on 02.09.17.
 */
@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {
    fun checkRate() {
        var calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        // если среда или суббота
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY ||
                calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            // и если пользователь еще не нажимал "проголосовать"
            if (!PrefHelper.checkRate())
                // то показываем диалог с предложением проголосовать
                launch(UI) {
                    // через 3 минуты после старта приложения
                    delay(3, TimeUnit.MINUTES)
                    viewState.showRateDialog()
                }
        }
    }

    fun read() = DatabaseReader.read()
    fun confirmRate() = PrefHelper.confirmRate()
    fun getStringList() : ArrayList<String> = ICOcollection.getStringArrayList()
    fun getList():ArrayList<ICO> = ICOcollection.instance
}