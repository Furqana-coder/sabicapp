package com.rusty.welcomeapp_sabic.conversation

import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.`object`.conversation.BaseQiChatExecutor

class AnimationExecutor(qiContext: QiContext) : BaseQiChatExecutor(qiContext) {

    private val pepperAnimationBuilder = PepperAnimationBuilder(qiContext)

    override fun runWith(params: MutableList<String>?) {
        params?.get(0)?.let {
            if(it.isNotEmpty())
                pepperAnimationBuilder.invokeAnimation(it)
        }
    }

    override fun stop() {
    }
}