package me.lucky.wasted.trigger.panic
import me.lucky.wasted.Preferences


import android.content.pm.PackageManager
import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import info.guardianproject.panic.PanicResponder
import me.lucky.wasted.MainActivity
import me.lucky.wasted.R

class PanicConnectionActivity : MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (PanicResponder.checkForDisconnectIntent(this)) {
            finish()
            return
        }
        val sender = PanicResponder.getConnectIntentSender(this)
        val packageName = PanicResponder.getTriggerPackageName(this)
        if (sender != "" && sender != packageName) {
            showOptInDialog()
        }
    }

    private fun showOptInDialog() {
        var app: CharSequence = getString(R.string.panic_app_unknown_app)
        val packageName = callingActivity?.packageName
        if (packageName != null) {
            try {
                app = packageManager
                    .getApplicationLabel(packageManager.getApplicationInfo(packageName, 0))
            } catch (exc: PackageManager.NameNotFoundException) {}
        }
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.panic_app_dialog_title)
            .setMessage(getString(R.string.panic_app_dialog_message, app))
            .setNegativeButton(R.string.allow) { _, _ ->
                PanicResponder.setTriggerPackageName(this@PanicConnectionActivity)
                setResult(RESULT_OK)
            }
            .setPositiveButton(android.R.string.cancel) { _, _ ->
                setResult(RESULT_CANCELED)
                finish()
            }
            .show()
    }
}