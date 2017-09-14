package io.almayce.dev.icologs.global

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import io.reactivex.subjects.PublishSubject

/**
 * Created by almayce on 02.09.17.
 */
object PrefHelper {
    private lateinit var prefPin: SharedPreferences
    private lateinit var prefRate: SharedPreferences
    val onPinned = PublishSubject.create<Boolean>()
    var set = HashSet<String>()
    private val TAG_PINNED = "pinned"
    private val TAG_RATED = "rated"

    fun initPref(context: Context) {
        prefPin = context.getSharedPreferences(TAG_PINNED, Context.MODE_PRIVATE)
        prefRate = context.getSharedPreferences(TAG_RATED, Context.MODE_PRIVATE)
        loadPref()
    }

    fun loadPref() {
        set = prefPin.getStringSet(TAG_PINNED, HashSet<String>()) as HashSet<String>
        Log.d("loadPref", "SET SIZE: ${set.size}")
    }

    fun setPin(id: String) {
        Log.d("setPin", "SET ADD: ${id}")
        set.add(id)
        Log.d("setPin", "SET SIZE BEFORE ADD: ${set.size}")

        val ed = prefPin.edit()
        ed.remove(TAG_PINNED)
        ed.apply()

        ed.putStringSet(TAG_PINNED, set)
        ed.apply()
    }

    fun unsetPin(id: String) {
        Log.d("setPin", "SET ADD: ${id}")
        set.remove(id)
        Log.d("setPin", "SET SIZE BEFORE REMOVE: ${set.size}")

        val ed = prefPin.edit()
        ed.remove(TAG_PINNED)
        ed.apply()

        ed.putStringSet(TAG_PINNED, set)
        ed.apply()
    }

    fun updatePin(id: String) {
        if (set.contains(id)) {
            unsetPin(id)
            onPinned.onNext(false)
        } else {
            setPin(id)
            onPinned.onNext(true)
        }
    }

    fun confirmRate() {
        val ed = prefPin.edit()
        ed.remove(TAG_PINNED)
        ed.apply()

        ed.putBoolean(TAG_RATED, true)
        ed.apply()
    }

    fun checkRate(): Boolean {
       return prefPin.getBoolean(TAG_RATED, false)
    }
}