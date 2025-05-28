package com.example.gamebox

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class IntroActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)


        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // Si el usuario ya esta logueado anteriormente, pasa directamente a la pantalla principal
            startActivity(Intent(this, MainScreenActivity::class.java))
            finish()
            return
        }

        val viewPager = findViewById<ViewPager2>(R.id.introViewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabDots)


        val realSlides = listOf(
            IntroSlide(getString(R.string.intro_slide_text_1), R.raw.anim1),
            IntroSlide(getString(R.string.intro_slide_text_2), R.raw.anim2),
            IntroSlide(getString(R.string.intro_slide_text_3), R.raw.anim3)
        )

        val slides = listOf(realSlides.last()) + realSlides + listOf(realSlides.first())
        tabLayout.removeAllTabs()
        repeat(realSlides.size) {
            val tab = tabLayout.newTab()
            tab.setCustomView(R.layout.tab_dot)
            tabLayout.addTab(tab)
        }


        viewPager.adapter = IntroAdapter(slides, viewPager)
        viewPager.setCurrentItem(1, false)
        val initialDot = tabLayout.getTabAt(0)?.customView?.findViewById<ImageView>(R.id.dot)
        initialDot?.setImageResource(R.drawable.tab_indicator_selected)



        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    when (viewPager.currentItem) {
                        0 -> viewPager.setCurrentItem(slides.size - 2, false)
                        slides.size - 1 -> viewPager.setCurrentItem(1, false)
                    }
                }
            }

            override fun onPageSelected(position: Int) {
                val realPosition = when (position) {
                    0 -> realSlides.size - 1
                    slides.size - 1 -> 0
                    else -> position - 1
                }

                for (i in 0 until tabLayout.tabCount) {
                    val tab = tabLayout.getTabAt(i)
                    val dot = tab?.customView?.findViewById<ImageView>(R.id.dot)
                    val drawable = if (i == realPosition) R.drawable.tab_indicator_selected else R.drawable.tab_indicator_default
                    dot?.setImageResource(drawable)
                }
            }

        })

        val handler = Handler(Looper.getMainLooper())
        val delayMillis = 9000L
        val runnable = object : Runnable {
            override fun run() {
                val nextItem = viewPager.currentItem + 1
                viewPager.setCurrentItem(nextItem, true)
                handler.postDelayed(this, delayMillis)
            }
        }
        handler.postDelayed(runnable, delayMillis)

        findViewById<Button>(R.id.createAccountButton).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("isLogin", false)
            startActivity(intent)
        }

        findViewById<Button>(R.id.loginButton).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("isLogin", true)
            startActivity(intent)
        }
    }
}
