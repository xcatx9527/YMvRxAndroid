package com.yzy.example.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.yzy.example.utils.QMUIRoundButtonDrawable
import com.yzy.example.utils.QMUIViewHelper


class RoundFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        val bg = QMUIRoundButtonDrawable.fromAttributeSet(context, attrs, defStyleAttr)
        QMUIViewHelper.setBackgroundKeepingPadding(this, bg)
    }
}