/*
 * Copyright (c) Treebolic 2023. Bernard Bou <1313ou@gmail.com>
 */
package org.treebolic.wordnet

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import org.treebolic.AppCompatCommonPreferenceActivity.Companion.ARG_FRAGMENT
import org.treebolic.download.BaseDownloadActivity
import java.io.IOException
import java.io.InputStream
import org.treebolic.download.R as DownloadR

/**
 * WordNet download activity
 *
 * @author Bernard Bou
 */
class DownloadActivity : BaseDownloadActivity() {

    /**
     * Whether stream is tar.gz (zip otherwise)
     */
    private var asTarGz = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        downloadUrl = Settings.getStringPref(this, Settings.PREF_DOWNLOAD)
        if (downloadUrl == null || downloadUrl!!.isEmpty()) {
            Toast.makeText(this, DownloadR.string.error_null_download_url, Toast.LENGTH_SHORT).show()
             val intent = Intent(this, SettingsActivity::class.java).apply {
                putExtra(ARG_FRAGMENT, SettingsActivity.DownloadPreferenceFragment::class.java.name)
            }
            startActivity(intent)
            finish()
        }
    }

    public override fun start() {
        asTarGz = downloadUrl!!.endsWith(".tar.gz")
        super.start(R.string.wordnet)
    }

    override fun doProcessing(): Boolean {
        return true
    }

    @Throws(IOException::class)
    override fun process(inputStream: InputStream): Boolean {
        Deployer(filesDir).process(inputStream, asTarGz)
        return true
    }
}
