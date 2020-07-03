/*
 * Copyright 2017-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.koin.android.viewmodel.compat

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelStoreOwner
import org.koin.android.viewmodel.scope.getViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope

/**
 * Scope functions to help for ViewModel in Java
 *
 * @author Jeziel Lago
 */
object ScopeCompat {

    /**
     * Lazy get a viewModel instance
     *
     * @param scope
     * @param qualifier - Koin BeanDefinition qualifier (if have several ViewModel beanDefinition of the same type)
     * @param parameters - parameters to pass to the BeanDefinition
     * @param clazz
     */
    @JvmOverloads
    @JvmStatic
    fun <T : ViewModel> viewModel(
        scope: Scope,
        owner: ViewModelStoreOwner,
        clazz: Class<T>,
        qualifier: Qualifier? = null,
        parameters: ParametersDefinition? = null
    ): Lazy<T> = lazy(LazyThreadSafetyMode.NONE) { scope.getViewModel(owner, clazz.kotlin, qualifier, parameters) }


    /**
     * Get a viewModel instance
     *
     * @param clazz - Class of the BeanDefinition to retrieve
     * @param qualifier - Koin BeanDefinition qualifier (if have several ViewModel beanDefinition of the same type)
     * @param parameters - parameters to pass to the BeanDefinition
     */
    @JvmOverloads
    @JvmStatic
    fun <T : ViewModel> getViewModel(
        scope: Scope,
        owner: ViewModelStoreOwner,
        clazz: Class<T>,
        qualifier: Qualifier? = null,
        parameters: ParametersDefinition? = null
    ): T {
        return scope.getViewModel(owner, clazz.kotlin, qualifier, parameters)
    }
}