package com.example.weatherapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

object ImageBlurUtil {
    fun blur(context: Context, image: Bitmap, radius: Float): Bitmap {
        val bitmap = image.copy(image.config, true)

        val renderScript = RenderScript.create(context)
        val input = Allocation.createFromBitmap(renderScript, bitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SCRIPT)
        val output = Allocation.createTyped(renderScript, input.type)
        val script = ScriptIntrinsicBlur.create(renderScript, input.element)

        script.setInput(input)
        script.setRadius(radius)
        script.forEach(output)
        output.copyTo(bitmap)

        renderScript.destroy()

        return bitmap
    }
}