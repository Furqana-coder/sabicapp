package com.rusty.welcomeapp_sabic

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import com.rusty.welcomeapp_sabic.conversation.PepperConversation
import com.rusty.welcomeapp_sabic.databinding.ActivityMainBinding
import com.rusty.welcomeapp_sabic.listeners.UIEventListener
import com.rusty.welcomeapp_sabic.utils.Constants
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : RobotActivity(),RobotLifecycleCallbacks,UIEventListener {

    private var pepperConversation: PepperConversation? = null
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        Timber.plant(Timber.DebugTree())
        initNav()
    }

    override fun onRobotFocusGained(qiContext: QiContext?) {
        qiContext?.let {
            pepperConversation = PepperConversation(it).apply {
                this.initChat(Constants.AR_TOPIC)
            }
        }
    }

    override fun onRobotFocusLost() {
        QiSDK.unregister(this,this)
    }

    override fun onRobotFocusRefused(reason: String?) {
        reason?.let {
            Timber.e("RobotFocusRefused : $it")
        }
    }

    private fun initNav(){
        val navHostFragment = supportFragmentManager.findFragmentById(mBinding.container.id) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onResume() {
        super.onResume()
        QiSDK.register(this,this)
    }

    override fun onDestroy() {
        super.onDestroy()
        QiSDK.unregister(this,this)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun callGotoBookmark(bookmarkName: String?) {
        bookmarkName?.let {
            GlobalScope.launch(Dispatchers.IO) {
                pepperConversation?.gotoBookmark(it)
            }
        }
    }
}