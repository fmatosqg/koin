package org.koin.sample.androidx.workmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.workmanager_activity.*
import kotlinx.coroutines.*
import org.junit.Assert
import org.koin.android.ext.android.inject
import org.koin.sample.android.R
import org.koin.sample.androidx.mvp.MVPActivity
import org.koin.sample.androidx.utils.navigateTo

/**
 * @author : Fabio de Matos
 * @since : 16/02/2020
 **/
class WorkManagerActivity : AppCompatActivity() {

    private val dummyService: DummyService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO)
                .launch {

                    OneTimeWorkRequestBuilder<DummyWorker>()
                            .build()
                            .also {
                                WorkManager.getInstance(this@WorkManagerActivity)
                                        .enqueueUniqueWork(
                                                DummyWorker::class.java.canonicalName.toString(),
                                                ExistingWorkPolicy.KEEP,
                                                it
                                        )
                            }

                }

        CoroutineScope(Dispatchers.Main)
                .launch {

                    val answer = try {
                        withTimeout(2_000) {
                            dummyService.getAnswer()
                        }
                    } catch (e: CancellationException) {
                        throw RuntimeException(e) // taking too long to receive 42 means WorkManager failed somehow
                    }

                    Assert.assertEquals(42, answer)
                    workmanager_message.text = "Work Manager completed!"


                    workmanager_button.isEnabled = true
                }


        setContentView(R.layout.workmanager_activity)
        title = "Android Work Manager"

        workmanager_button.setOnClickListener {
            navigateTo<MVPActivity>(isRoot = true)
        }
    }
}