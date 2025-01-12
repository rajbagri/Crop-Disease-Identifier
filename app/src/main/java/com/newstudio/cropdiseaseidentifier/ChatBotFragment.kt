package com.newstudio.cropdiseaseidentifier

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.newstudio.cropdiseaseidentifier.databinding.FragmentChatBotBinding


class ChatBotFragment : Fragment() {
    private lateinit var chatBotBinding: FragmentChatBotBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        chatBotBinding = FragmentChatBotBinding.inflate(inflater, container, false);
        return chatBotBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}