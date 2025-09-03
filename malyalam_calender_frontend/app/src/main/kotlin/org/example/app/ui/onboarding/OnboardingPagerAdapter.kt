package org.example.app.ui.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.example.app.R

class OnboardingPagerAdapter : RecyclerView.Adapter<OnboardingPagerAdapter.PageViewHolder>() {
    
    private val pages = listOf(
        OnboardingPage(
            R.drawable.ic_calendar,
            R.string.onboarding_calendar_title,
            R.string.onboarding_calendar_desc
        ),
        OnboardingPage(
            R.drawable.ic_leaves,
            R.string.onboarding_leaves_title,
            R.string.onboarding_leaves_desc
        ),
        OnboardingPage(
            R.drawable.ic_astrology,
            R.string.onboarding_astrology_title,
            R.string.onboarding_astrology_desc
        )
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        return PageViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_onboarding_page, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.bind(pages[position])
    }

    override fun getItemCount(): Int = pages.size

    class PageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.imageView)
        private val titleText: TextView = view.findViewById(R.id.titleText)
        private val descriptionText: TextView = view.findViewById(R.id.descriptionText)

        fun bind(page: OnboardingPage) {
            imageView.setImageResource(page.imageResId)
            titleText.setText(page.titleResId)
            descriptionText.setText(page.descriptionResId)
        }
    }

    data class OnboardingPage(
        val imageResId: Int,
        val titleResId: Int,
        val descriptionResId: Int
    )
}
