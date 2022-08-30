package com.rusty.welcomeapp_sabic.presentation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rusty.welcomeapp_sabic.R
import com.rusty.welcomeapp_sabic.databinding.FragmentDashboardBinding
import com.rusty.welcomeapp_sabic.listeners.UIEventListener
import com.rusty.welcomeapp_sabic.utils.BookmarkName

class DashboardFragment : Fragment() {

    private var uiEventListener : UIEventListener? = null
    private lateinit var binding : FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            provenLogo.setOnClickListener {
                uiEventListener?.callGotoBookmark(BookmarkName.WELCOME_MESSAGE)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        uiEventListener = context as UIEventListener
    }

    override fun onDetach() {
        super.onDetach()
        uiEventListener = null
    }
}