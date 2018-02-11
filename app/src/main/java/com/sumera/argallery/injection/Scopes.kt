package com.sumera.argallery.injection

import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerActivity()

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerFragment()

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerChildFragment()