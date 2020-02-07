package org.koin.androidx.workmanager

import android.content.Context
import android.util.Log
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters

/**
 *
 * Stores a collection of lambdas capable of creating [ListenableWorker] objects.
 * Such lambdas would get invoked at the time the worker object is about to run.
 *
 * @author : Fabio de Matos
 * @since : 16/02/2020
 **/
abstract class AbstractWorkerFactory : WorkerFactory() {

    private val TAG = "AbstractWorkerFactory"

    private val map by lazy {
        mutableMapOf<String?, WorkerFactoryCreatorBlock>()
    }


    /**
     * Add one lambda to internal data structure capable of creating a [ListenableWorker] object from class
     * [clazz]
     *
     * @param clazz registers the class [clazz] and associates it with the [factoryBlock]
     * @param factoryBlock lambda capable of creating the worker object
     */
    fun addCreator(
            clazz: Class<out ListenableWorker>,
            factoryBlock: WorkerFactoryCreatorBlock
    ) {

        map[clazz.canonicalName ?: ""] = factoryBlock
    }

    override fun createWorker(
            applicationContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
    ): ListenableWorker? {

        return try {
            map[workerClassName]
                    ?.invoke(applicationContext, workerParameters)
        } catch (e: Exception) {
            Log.e(TAG, "Error creating ListenableWorker")
            null
        }
    }

}

typealias WorkerFactoryCreatorBlock = Function2<Context, WorkerParameters, ListenableWorker>