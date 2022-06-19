package better.text.protext.ui_copiedtexts.services

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import better.text.protext.base.baseScreens.BaseForegroundWindowService
import better.text.protext.base.baseScreens.UIEvent
import better.text.protext.base.utils.NotificationAction
import better.text.protext.base.utils.NotificationData
import better.text.protext.base.utils.copyToClipboard
import better.text.protext.interactors.copiedTexts.AddCopiedTextUseCase
import better.text.protext.ui_copiedtexts.R
import better.text.protext.ui_copiedtexts.databinding.CopiedTextServiceLayoutBinding
import better.text.protext.ui_copiedtexts.viewmodels.CopiedTextServiceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CopiedTextService : BaseForegroundWindowService<CopiedTextServiceLayoutBinding>(
    windowGravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
    windowAnimations = android.R.style.Animation_InputMethod
) {
    override val windowManager: WindowManager
        get() = getSystemService(WINDOW_SERVICE) as WindowManager

    override fun inflater(inflater: LayoutInflater): CopiedTextServiceLayoutBinding {
        return CopiedTextServiceLayoutBinding.inflate(inflater)
    }

    @Inject
    lateinit var addCopiedTextUseCase: AddCopiedTextUseCase

    private lateinit var notificationData: NotificationData
    private val viewModel: CopiedTextServiceViewModel by lazy {
        CopiedTextServiceViewModel(addCopiedTextUseCase = addCopiedTextUseCase)
    }

    companion object {
        const val EDIT_ACTION = "better.text.protext.ui_copiedtexts.services.ACTION_EDIT_COPIED_TEXT"
        const val DISMISS_ACTION = "better.text.protext.ui_copiedtexts.services.ACTION_DISMISS_COPIED_TEXT"
        const val SAVE_ACTION = "better.text.protext.ui_copiedtexts.services.ACTION_SAVE_COPIED_TEXT"
        const val BREAK_ACTION = "better.text.protext.ui_copiedtexts.services.ACTION_COPIED_TEXT_ADD_BREAK"
    }

    override fun onCreate(binding: CopiedTextServiceLayoutBinding) {
        initVariables()
        initViews()
        initViewListeners()
        initObservables()
    }

    override fun onStartCommand(intent: Intent?) {
        intent?.let {
            handleIntent(it)
        }
        startForeground(2, createNotification(notificationData))
    }

    private fun initViews() {
        binding.includedEditLayout.apply {
            titleText.text = getString(R.string.add_copied_text)
            btnAction.text = getString(better.text.protext.base.R.string.edit)
            btnAction.isEnabled = false
        }
    }

    private fun initObservables() {
        lifecycleScope.launch {
            viewModel.eventsFlow.flowWithLifecycle(lifecycle).collectLatest {
                when (it) {
                    is UIEvent.GoBack -> close()
                    is UIEvent.ShowToast -> Toast.makeText(this@CopiedTextService, it.message, it.duration).show()
                    else -> {}
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViewListeners() {
        binding.apply {
            root.setOnTouchListener { _, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_OUTSIDE -> {
                        try {
                            windowManager.removeView(binding.root)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            includedEditLayout.apply {
                inputCopiedText.doOnTextChanged { _, _, _, _ ->
                    btnAction.isEnabled = (viewModel.copiedText != inputCopiedText.text.toString())
                }
                btnAction.setOnClickListener {
                    viewModel.copiedText = inputCopiedText.text.toString()
                    notificationData = notificationData.copy(
                        content = getProcessedString()
                    )
                    startForegroundService(Intent(this@CopiedTextService, CopiedTextService::class.java))
                    try {
                        windowManager.removeView(binding.root)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun initVariables() {
        val editIntent = Intent(this, CopiedTextService::class.java).apply {
            action = EDIT_ACTION
        }
        val saveIntent = Intent(this, CopiedTextService::class.java).apply {
            action = SAVE_ACTION
        }
        val dismissIntent = Intent(this, CopiedTextService::class.java).apply {
            action = DISMISS_ACTION
        }
        val breakIntent = Intent(this, CopiedTextService::class.java).apply {
            action = BREAK_ACTION
        }
        val actions = listOf(
            NotificationAction(
                actionTitle = "Break",
                actionIcon = better.text.protext.base.R.drawable.ic_edit,
                action = PendingIntent.getService(this, 0, breakIntent, 0)
            ),
            NotificationAction(
                actionTitle = "Save",
                actionIcon = R.drawable.ic_save,
                action = PendingIntent.getService(this, 0, saveIntent, 0)
            ),
            NotificationAction(
                actionTitle = "Dismiss",
                actionIcon = R.drawable.ic_cancel,
                action = PendingIntent.getService(this, 0, dismissIntent, 0)
            ),
        )
        notificationData = NotificationData(
            title = "Add a Copied Text",
            content = "",
            iconId = R.drawable.ic_copy,
            notificationId = 1234,
            channelId = getString(R.string.copied_text_channel_id),
            channelName = getString(R.string.copied_text_channel_name),
            channelDescription = getString(R.string.copied_text_channel_description),
            actions = actions,
            clickAction = PendingIntent.getService(this, 0, editIntent, 0)
        )
    }

    private fun handleIntent(intent: Intent) {
        Log.d("Protext", "Howdy ${intent.action}")
        when (intent.action) {
            BREAK_ACTION -> updateCopiedText("\n")
            EDIT_ACTION -> {
                setContentText()
                windowManager.addView(binding.root, layoutParams)
            }
            DISMISS_ACTION -> close()
            SAVE_ACTION -> {
                copyToClipboard(viewModel.copiedText)
                viewModel.addCopiedText()
            }
            Intent.ACTION_PROCESS_TEXT -> {
                val text = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT)
                text?.let {
                    if (it.isEmpty()) return
                    updateCopiedText(it.trim { char -> char == ' ' })
                }
            }
        }
    }

    private fun setContentText() {
        binding.includedEditLayout.apply {
            inputCopiedText.setText(viewModel.copiedText)
        }
    }

    private fun updateCopiedText(text: String) {
        val prevText = viewModel.copiedText.trim { it == ' ' }

        viewModel.copiedText = if (prevText.endsWith("\n")) {
            "$prevText$text"
        } else {
            "$prevText $text"
        }.trim { it == ' ' }
        notificationData = notificationData.copy(
            content = getProcessedString()
        )
    }

    private fun getProcessedString(): String {
        val words = viewModel.copiedText.trim().count { it == ' ' } + 1
        val lines = viewModel.copiedText.trim().count { it == '\n' } + 1
        return getString(R.string.copied_text_notification_text, words, lines)
    }
}
