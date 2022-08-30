package com.rusty.welcomeapp_sabic.conversation

import com.aldebaran.qi.Future
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.`object`.conversation.*
import com.aldebaran.qi.sdk.`object`.conversation.Chat
import com.aldebaran.qi.sdk.`object`.locale.Language
import com.aldebaran.qi.sdk.`object`.locale.Locale
import com.aldebaran.qi.sdk.`object`.locale.Region
import com.aldebaran.qi.sdk.builder.ChatBuilder
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder
import com.aldebaran.qi.sdk.builder.TopicBuilder
import com.rusty.welcomeapp_sabic.listeners.BookmarkReachedListener
import com.rusty.welcomeapp_sabic.utils.BookmarkName
import com.rusty.welcomeapp_sabic.utils.Constants
import timber.log.Timber
import java.lang.ref.WeakReference

class QiChatConversation(private val qiContext: QiContext) {

    private lateinit var topic: Topic
    var chatBot: QiChatbot? = null
    private lateinit var chat: Chat
    var chatFuture: Future<Void>? = null
    private var listener: WeakReference<BookmarkReachedListener>? = null
    private var isSpeakFinished = true

    fun initAndStartChat(topicFile: String) {
        topic = TopicBuilder.with(qiContext).withAsset(topicFile).build()
        chatBot = QiChatbotBuilder.with(qiContext).withTopic(topic).withLocale(getLocale(topicFile))
            .build()
        chat = ChatBuilder.with(qiContext).withChatbot(chatBot).withLocale(getLocale(topicFile))
            .build()
        startChat()
    }

    private fun startChat() {
        executeAnimationExecutor()
        chatFuture = chat.async().run()

        chat.addOnStartedListener {
            Timber.e("Chat Started")
            addOnHumanChangedListener()
        }

        chatBot?.addOnBookmarkReachedListener {
            when(it.name){
                BookmarkName.WELCOME_MESSAGE ->{
                    isSpeakFinished = false
                    Timber.e("Welcome Message $isSpeakFinished")
                }
                BookmarkName.SPEAK_FINISHED ->{
                    isSpeakFinished = true
                    Timber.e("Speak Finished $isSpeakFinished")
                }
            }
        }
    }

    private fun executeAnimationExecutor() {
        val animationExecutor = HashMap<String, QiChatExecutor>()
        animationExecutor["animExecutor"] = AnimationExecutor(qiContext)
        chatBot?.executors = animationExecutor
    }

    fun gotoBookmark(bookmarkName: String) {
        val bookmark = topic.bookmarks[bookmarkName]
        chatBot?.goToBookmark(
            bookmark,
            AutonomousReactionImportance.HIGH,
            AutonomousReactionValidity.IMMEDIATE
        )
    }

    fun addListener(bookmarkReachedListener: BookmarkReachedListener) {
        listener = WeakReference(bookmarkReachedListener)
    }

    fun stopChat() {
        chatFuture?.requestCancellation()
        chatBot?.removeAllOnBookmarkReachedListeners()
    }

    private fun getLocale(fileName: String): Locale {
        return if (fileName == Constants.EN_TOPIC)
            Locale(Language.ENGLISH, Region.UNITED_STATES)
        else
            Locale(Language.ARABIC, Region.SAUDI_ARABIA)
    }

    private fun addOnHumanChangedListener() {
        qiContext.let { context ->
            context.humanAwareness.addOnHumansAroundChangedListener { humansAround ->
                humansAround?.let {
                    Timber.e("Humans Around : ${it.size}")
                    if (isSpeakFinished) {
                        isSpeakFinished = false
                        Timber.e("Bookmark Called : ${BookmarkName.WELCOME_MESSAGE}")
                        gotoBookmark(BookmarkName.WELCOME_MESSAGE)
                    }
                }
            }
        }
    }
}