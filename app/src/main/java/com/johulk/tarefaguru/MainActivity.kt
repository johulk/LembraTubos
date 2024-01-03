package com.johulk.tarefaguru

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.johulk.tarefaguru.databinding.ActivityMainBinding
import com.google.android.material.color.DynamicColors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DynamicColors.applyToActivityIfAvailable(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)

        navController = navHostFragment.navController

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.setupWithNavController(navController)
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            if (menuItem.itemId == binding.bottomNavigationView.selectedItemId) {
                // Double-clicked on the currently selected item
                // Navigate back to the top level of the current fragment
                navController.navigateUpToTopLevel(menuItem.itemId)
            } else {
                // Regular item selection behavior
                NavigationUI.onNavDestinationSelected(menuItem, navController)
                return@setOnItemSelectedListener true
            }
            true
        }
        binding.fab.setOnClickListener {
            navController.navigate(R.id.addClienteFragment2)
        }
    }
}

fun NavController.navigateUpToTopLevel(destinationId: Int) {
    while (currentDestination?.id != destinationId && currentDestination?.parent?.id != destinationId) {
        popBackStack()
    }
}
