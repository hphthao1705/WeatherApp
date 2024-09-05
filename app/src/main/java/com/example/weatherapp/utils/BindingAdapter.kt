package com.example.weatherapp.utils

import android.graphics.Bitmap
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.utils.common.LoadImage
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener

@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: MutableLiveData<Int>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && visibility != null) {
        visibility.observe(
            parentActivity,
            Observer { value -> view.visibility = value ?: View.VISIBLE })
    }
}

@BindingAdapter("mutableEnabled")
fun setMutableEnabled(view: View, enabled: MutableLiveData<Boolean>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && enabled != null) {
        enabled.observe(
            parentActivity,
            Observer { value -> view.isEnabled = value ?: false })
    }
}

@BindingAdapter("mutableFocusable")
fun setMutableFocusable(view: View, enabled: MutableLiveData<Boolean>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && enabled != null) {
        enabled.observe(
            parentActivity,
            Observer { value -> view.isFocusable = value ?: false })
    }
}

@BindingAdapter("mutableChecked")
fun setMutableCheck(view: AppCompatCheckBox, visibility: MutableLiveData<Boolean>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer { value -> view.isChecked = value ?: false })
    }
}

@BindingAdapter("mutableEditText")
fun setMutableEditText(view: EditText, text: MutableLiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value -> view.setText(value) })
    }
}

@BindingAdapter("mutableImage", "imageDefault", requireAll = false)
fun setMutableImageView(
    viewImage: ImageView,
    text: MutableLiveData<String>?,
    imageDefault: MutableLiveData<String>?
) {
    val parentActivity: AppCompatActivity? = viewImage.getParentActivity()

    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value ->
            LoadImage.getImageLLoader().displayImage(
                value,
                viewImage,
                LoadImage.getImageLOptions(),
                object : ImageLoadingListener {
                    override fun onLoadingStarted(imageUri: String?, view: View?) {
                    }

                    override fun onLoadingFailed(
                        imageUri: String?,
                        view: View?,
                        failReason: FailReason?
                    ) {
                        LoadImage.getImageLLoader().displayImage(
                            imageDefault?.value,
                            viewImage,
                            LoadImage.getImageLOptions()
                        )
                    }

                    override fun onLoadingComplete(
                        imageUri: String?,
                        view: View?,
                        loadedImage: Bitmap?
                    ) {
                    }

                    override fun onLoadingCancelled(imageUri: String?, view: View?) {
                    }
                })
        })
    }
}

@BindingAdapter("mutableImageLocal")
fun setMutableImageLocal(view: ImageView, image: MutableLiveData<Int>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()

    if (parentActivity != null && image != null) {
        image.observe(parentActivity, Observer { value ->
            view.setImageResource(value)
        })
    }
}

@BindingAdapter("mutableText")
fun setMutableText(view: TextView, text: MutableLiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text?.value != null) {
        text.observe(parentActivity, Observer { value -> view.text = value ?: "" })
    }
}

@BindingAdapter("mutableHint")
fun setMutableHint(view: TextView, text: MutableLiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value -> view.hint = value ?: "" })
    }
}

@BindingAdapter("mutableInputType")
fun setMutableInputType(view: EditText, isShowPW: MutableLiveData<Boolean>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    parentActivity?.let {
        isShowPW?.observe(it) { value ->
            if (value) {
                view.transformationMethod = null
            } else {
                view.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            view.setSelection(view.length())
        }
    }
}
/*@BindingAdapter("mutableBackground")
fun setMutableBackground(view: TextView, background: MutableLiveData<Int>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && background != null) {
        background.observe(parentActivity, Observer { value ->
            view.setBackgroundResource(value)
        })
    }
}*/
@BindingAdapter("mutableTextColor")
fun setMutableTextColor(view: TextView, background: MutableLiveData<Int>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && background != null) {
        background.observe(parentActivity, Observer { value ->
            view.setTextColor(ContextCompat.getColor(parentActivity, value))
        })
    }
}

@BindingAdapter("mutableBackground")
fun setMutableBackground(view: TextView, text: MutableLiveData<Int>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value ->
            view.background = ContextCompat.getDrawable(parentActivity, value)
        })
    }
}

@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>?) {
    view.adapter = adapter
}

@BindingAdapter("onScrollListener")
fun addOnScrollListener(view: RecyclerView, listener: RecyclerView.OnScrollListener) {
    view.addOnScrollListener(listener)
}

//@BindingAdapter("mutableHtmlText")
//fun setMutableHtmlText(view: TextView, text: MutableLiveData<String>?) {
//    val parentActivity: AppCompatActivity? = view.getParentActivity()
//    if (parentActivity != null && text != null) {
//        text.observe(parentActivity, Observer { value ->
//            view.setText(HtmlUtils.fromHtml(value))
//        })
//    }
//
//}
//
//@BindingAdapter("mutableHtmlWebview")
//fun setMutableHtmlWebview(view: WebView, text: MutableLiveData<String>?) {
//    val parentActivity: AppCompatActivity? = view.getParentActivity()
//    if (parentActivity != null && text != null) {
//        text.observe(parentActivity, Observer { value ->
//            view.setBackgroundColor(Color.TRANSPARENT)
//            view.loadDataWithBaseURL(null, HtmlUtils.customTag(value), "text/html", "utf-8", null)
//
//        })
//    }
//
//}

@BindingAdapter("mutableBackgroundViewGroup")
fun setMutableBackgroundViewGroup(view: ViewGroup, text: MutableLiveData<Int>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value ->
            view.background = ContextCompat.getDrawable(parentActivity, value)
        })
    }
}

@BindingAdapter("mutableButtonBackground")
fun setMutableBackground(view: AppCompatButton, text: MutableLiveData<Int>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value ->
            view.background = ContextCompat.getDrawable(parentActivity, value)
        })
    }
}