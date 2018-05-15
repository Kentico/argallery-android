package com.sumera.argallery.domain

import com.sumera.argallery.domain.base.BaseCompletabler
import com.sumera.argallery.injection.PerFragment
import com.sumera.argallery.tools.UnityImageSaver
import io.reactivex.Completable
import javax.inject.Inject

@PerFragment
class UnityImagePrepareCompletabler @Inject constructor(
        private val unityImageSaver: UnityImageSaver
) : BaseCompletabler() {

    private lateinit var imageUrl: String

    fun init(imageUrl: String) = apply {
        this.imageUrl = imageUrl
    }

    override fun create(): Completable {
        return unityImageSaver.saveImage(imageUrl)
    }
}