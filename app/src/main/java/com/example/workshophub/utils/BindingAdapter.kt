package com.example.workshophub.utils

import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.workshophub.R

@BindingAdapter("imageId")
fun loadImage(view: ImageView, imageId: String) {
    Glide.with(view.context).load(Constant.IMAGE_URL + imageId).placeholder(R.mipmap.ic_launcher).into(view)

}

@BindingAdapter("status")
fun getStatus(view: Button, status: Boolean){
    if(status){
        view.setText(R.string.withdraw)
    }else{
        view.setText(R.string.apply)
    }
    Log.d("TAG", "getStatus: "+status)
}