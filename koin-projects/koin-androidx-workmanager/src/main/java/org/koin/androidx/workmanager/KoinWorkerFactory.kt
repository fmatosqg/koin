package org.koin.androidx.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent.getKoin

/**
 * Provides an implementation of [AbstractWorkerFactory] that ties into Koin DI.
 *
 * It has access to an [WorkerParameters] object, and  creates a local scope with access to it.
 * Then such scope is used to instantiate an object of [ListenableWorker] through koin.
 *
 * @author : Fabio de Matos
 * @since : 16/02/2020
 **/
class KoinWorkerFactory : AbstractWorkerFactory() {

    companion object {

        private val TAG = "KoinWorkerFactory"
        private val INSTANCE = KoinWorkerFactory()
        fun getInstance(): KoinWorkerFactory {
            return INSTANCE
        }
    }


    /**
     * Creates the lambda that will get stored on [AbstractWorkerFactory] and will get called
     * when the app needs a [ListenableWorker].
     *
     */
    fun addWorker(
        clazz: Class<out ListenableWorker>,
        createWorkerLambda: Function1<Scope, ListenableWorker>
    ) {
        addCreator(clazz) { _, params ->

            val scope = getKoin()
                .getOrCreateScope(
                    "androidWorkerScope",
                    named<AbstractWorkerFactory>()
                )

            scope.declare(params)

            createWorkerLambda(scope)
                .also { scope.close() }
        }

    }


    private var isInitialized = false

    @Synchronized
    fun init(context: Context) {

        if (!isInitialized) {
            isInitialized = true


            Configuration.Builder()
                .setWorkerFactory(this)
                .build()
                .let { WorkManager.initialize(context.applicationContext, it) }

        }

    }
}

