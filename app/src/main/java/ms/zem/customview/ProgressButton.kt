package ms.zem.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import ms.zem.customview.databinding.ProgressButtonBinding

class ProgressButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr){

    private var texto: String? = null
    private var loading: String? = null

    private val binding = ProgressButtonBinding
        .inflate(LayoutInflater.from(context), this, true)

    private var state : ProgressButtonState = ProgressButtonState.Normal
        set(value) {
            field = value
            refreshState()
        }

    init {
        setLayout(attrs)
        refreshState()
    }

    private fun setLayout(attrs: AttributeSet?) {
        attrs?.let { attributeSet ->
            val attributes = context.obtainStyledAttributes(
                attributeSet,
                R.styleable.ProgressButton
            )
            setBackgroundResource(R.drawable.progress_button_backgroung)
            val textResId = attributes.getResourceId(R.styleable.ProgressButton_progress_button_text, 0)
            if (textResId > 0){
                texto = context.getString(textResId)
            }
            val loadingTextResId = attributes.getResourceId(R.styleable.ProgressButton_progress_button_loading_text, 0)
            if (loadingTextResId > 0) {
                loading = context.getString(loadingTextResId)
            }
            attributes.recycle()
        }
    }

    private fun refreshState() {
        isEnabled = state.isEnabled
        isClickable = state.isEnabled
        refreshDrawableState()

        binding.textButton.run {
            isEnabled = state.isEnabled
            isClickable = state.isEnabled
        }
        binding.progressButton.visibility = state.progressVisibility

        when (state) {
            ProgressButtonState.Normal -> binding.textButton.text = texto
            ProgressButtonState.Loading -> binding.textButton.text = loading
        }
    }

    fun setLoading() {
        state = ProgressButtonState.Loading
    }

    fun setNormal() {
        state = ProgressButtonState.Normal
    }

    sealed class ProgressButtonState(val isEnabled: Boolean, val progressVisibility: Int) {
        object Normal : ProgressButtonState(true, View.GONE)
        object Loading : ProgressButtonState(false, View.VISIBLE)
    }
}