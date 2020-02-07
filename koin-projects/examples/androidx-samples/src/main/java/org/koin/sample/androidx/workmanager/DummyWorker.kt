package org.koin.sample.androidx.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/**
 * @author : Fabio de Matos
 * @since : 16/02/2020
 **/
class DummyWorker(private val dummyService: DummyService, appContext: Context,
                  params: WorkerParameters) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        dummyService.setAnswer(42)

        return Result.success()
    }

}