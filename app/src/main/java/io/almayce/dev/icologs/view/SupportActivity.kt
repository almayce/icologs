package io.almayce.dev.icologs.view

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import io.almayce.dev.icologs.R
import io.almayce.dev.icologs.databinding.ActivitySupportBinding

/**
 * Created by almayce on 29.08.17.
 */
class SupportActivity : MvpAppCompatActivity() {

    private lateinit var bn: ActivitySupportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bn = DataBindingUtil.setContentView(this, R.layout.activity_support)
        bn.btBack.setOnClickListener({onBackPressed()})
        bn.btConfirm.setOnClickListener({send()})
    }


    private fun send() {
        val to = "support@icologs.com"
        val email = Intent(Intent.ACTION_SEND)
        email.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        email.putExtra(Intent.EXTRA_SUBJECT, bn.etName.text)
        email.putExtra(Intent.EXTRA_TEXT, bn.etMessage.text)

        //для того чтобы запросить email клиент устанавливаем тип
        email.type = "message/rfc822"

        startActivity(Intent.createChooser(email, "Send email to $to via:"))
    }
}