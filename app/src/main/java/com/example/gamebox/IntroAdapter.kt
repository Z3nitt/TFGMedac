package com.example.gamebox

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable


class IntroAdapter(
    private val slides: List<IntroSlide>,
    private val viewPager: ViewPager2
) : RecyclerView.Adapter<IntroAdapter.SlideViewHolder>() {

    inner class SlideViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.slideTitle)
        val animation: LottieAnimationView = view.findViewById(R.id.slideAnimation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slide, parent, false)
        return SlideViewHolder(view)
    }

    override fun getItemCount(): Int = slides.size

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        val slide = slides[position]
        holder.title.text = slide.title
        holder.animation.setAnimation(slide.lottieRes)
        holder.animation.repeatCount = LottieDrawable.INFINITE
        holder.animation.playAnimation()

        holder.animation.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                val currentPosition = holder.adapterPosition
                if (currentPosition == RecyclerView.NO_POSITION) return
                val nextPosition = if (currentPosition == slides.lastIndex) 0 else currentPosition + 1
                viewPager.setCurrentItem(nextPosition, true)
            }
        })

    }
}

// Data class
data class IntroSlide(val title: String, val lottieRes: Int)
