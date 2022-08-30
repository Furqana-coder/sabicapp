package com.rusty.welcomeapp_sabic.conversation

import com.aldebaran.qi.sdk.QiContext
import com.rusty.welcomeapp_sabic.listeners.BookmarkReachedListener
import timber.log.Timber

class PepperConversation(qiContext: QiContext) {

    private var qiChatConversation: QiChatConversation? = null

    init {
        qiChatConversation = QiChatConversation(qiContext)
    }

    fun initChat(topicFileResource : String) {
        qiChatConversation?.stopChat()
        qiChatConversation?.initAndStartChat(topicFileResource)
    }

    fun gotoBookmark(bookMarkName : String){
        try {
            qiChatConversation?.gotoBookmark(bookMarkName)
        }catch (e : Exception){
            Timber.e("Failed to go to bookmark : ${e.message}")
        }
    }

    fun addListener(bookmarkReachedListener : BookmarkReachedListener){
        qiChatConversation?.addListener(bookmarkReachedListener)
    }
}