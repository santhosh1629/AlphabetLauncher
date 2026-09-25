package com.example.alphabetlauncher.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alphabetlauncher.data.model.AppInfo
import com.example.alphabetlauncher.databinding.ItemAppBinding

class AppAdapter(
    private val onAppClick: (AppInfo) -> Unit
) : RecyclerView.Adapter<AppAdapter.AppViewHolder>() {

    private var apps: List<AppInfo> = emptyList()

    fun submitList(newApps: List<AppInfo>) {
        apps = newApps
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppViewHolder {

        val binding = ItemAppBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return AppViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AppViewHolder,
        position: Int
    ) {
        holder.bind(apps[position])
    }

    override fun getItemCount(): Int {
        return apps.size
    }

    inner class AppViewHolder(
        private val binding: ItemAppBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(appInfo: AppInfo) {

            binding.tvAppName.text = appInfo.appName
            binding.ivAppIcon.setImageDrawable(appInfo.icon)

            binding.root.setOnClickListener {
                onAppClick(appInfo)
            }
        }
    }
}