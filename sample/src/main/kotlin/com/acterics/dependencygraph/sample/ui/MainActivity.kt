package com.acterics.dependencygraph.sample.ui

import android.support.v7.app.AppCompatActivity
import com.acterics.dependencygraph.sample.data.repository.MockProfileRepository
import com.acterics.dependencygraph.sample.domain.repository.ProfileRepository

class MainActivity: AppCompatActivity() {
    val profileRepository: ProfileRepository = MockProfileRepository()
}