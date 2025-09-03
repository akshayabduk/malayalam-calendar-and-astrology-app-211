package org.example.app.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.example.app.ui.fragments.AstrologyFragment
import org.example.app.ui.fragments.CalendarFragment
import org.example.app.ui.fragments.LeavesFragment

/**
 * Adapter for managing the main tab fragments
 */
class MainTabsAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CalendarFragment()
            1 -> LeavesFragment()
            2 -> AstrologyFragment()
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }
}
