package org.example.app.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import org.example.app.R
import org.example.app.ui.MainActivity

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var skipButton: Button
    private lateinit var nextButton: Button
    private lateinit var indicatorContainer: ViewGroup

    private val slides = listOf(
        OnboardingSlide(
            R.drawable.onboarding_calendar,
            "Malayalam Calendar",
            "Track your important dates with our Malayalam calendar"
        ),
        OnboardingSlide(
            R.drawable.onboarding_leaves,
            "Leave Management",
            "Easily manage your leaves and holidays"
        ),
        OnboardingSlide(
            R.drawable.onboarding_astrology,
            "Daily Astrology",
            "Get daily astrology updates and predictions"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        skipButton = findViewById(R.id.skipButton)
        nextButton = findViewById(R.id.nextButton)
        indicatorContainer = findViewById(R.id.indicatorContainer)

        setupViewPager()
        setupButtons()
        setupIndicators()
    }

    private fun setupViewPager() {
        viewPager.adapter = OnboardingAdapter(slides)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateIndicators(position)
                updateButtons(position)
            }
        })
    }

    private fun setupButtons() {
        skipButton.setOnClickListener { finishOnboarding() }
        nextButton.setOnClickListener {
            if (viewPager.currentItem == slides.lastIndex) {
                finishOnboarding()
            } else {
                viewPager.currentItem = viewPager.currentItem + 1
            }
        }
    }

    private fun setupIndicators() {
        val indicators = Array(slides.size) {
            ImageView(this).apply {
                val size = resources.getDimensionPixelSize(R.dimen.indicator_size)
                val params = ViewGroup.MarginLayoutParams(size, size)
                val margin = resources.getDimensionPixelSize(R.dimen.indicator_margin)
                params.setMargins(margin, 0, margin, 0)
                layoutParams = params
            }
        }

        indicators.forEach { indicatorContainer.addView(it) }
        updateIndicators(0)
    }

    private fun updateIndicators(position: Int) {
        repeat(indicatorContainer.childCount) { index ->
            val indicator = indicatorContainer.getChildAt(index) as ImageView
            indicator.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    if (index == position) R.drawable.indicator_active
                    else R.drawable.indicator_inactive
                )
            )
        }
    }

    private fun updateButtons(position: Int) {
        nextButton.text = if (position == slides.lastIndex) getString(R.string.get_started)
                         else getString(R.string.next)
    }

    private fun finishOnboarding() {
        getSharedPreferences("prefs", MODE_PRIVATE)
            .edit()
            .putBoolean("onboarding_completed", true)
            .apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

data class OnboardingSlide(
    val imageResId: Int,
    val title: String,
    val description: String
)

class OnboardingAdapter(private val slides: List<OnboardingSlide>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<OnboardingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_onboarding, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(slides[position])
    }

    override fun getItemCount() = slides.size

    class ViewHolder(view: android.view.View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.imageView)
        private val titleText: TextView = view.findViewById(R.id.titleText)
        private val descriptionText: TextView = view.findViewById(R.id.descriptionText)

        fun bind(slide: OnboardingSlide) {
            imageView.setImageResource(slide.imageResId)
            titleText.text = slide.title
            descriptionText.text = slide.description
        }
    }
}
