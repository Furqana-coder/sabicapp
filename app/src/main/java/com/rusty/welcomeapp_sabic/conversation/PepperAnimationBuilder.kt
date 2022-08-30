package com.rusty.welcomeapp_sabic.conversation

import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.builder.AnimateBuilder
import com.aldebaran.qi.sdk.builder.AnimationBuilder
import com.rusty.welcomeapp_sabic.R
import timber.log.Timber

class PepperAnimationBuilder(private val qiContext: QiContext) {

    fun invokeAnimation(animationName: String){
        val animationId = animationParser(animationName)
        if (animationId != null){
            val animation = AnimationBuilder.with(qiContext).withResources(animationId).build()
            val animateBuilder = AnimateBuilder.with(qiContext).withAnimation(animation).build()
            animateBuilder.async().run()
        }
    }


    private fun animationParser(animationName: String): Int?{
        return when(animationName){
            "bowing_b001" -> R.raw.bowing_b001
            else -> { null }
        }
    }
}