package com.acterics.dependencygraph.sample.data.repository

import com.acterics.dependencygraph.sample.domain.model.Profile
import com.acterics.dependencygraph.sample.domain.repository.ProfileRepository

class MockProfileRepository: ProfileRepository {

    private val mockProfiles = mutableListOf<Profile>()

    override fun getProfiles(): List<Profile> {
        return mockProfiles
    }

    override fun getProfile(id: Long): Profile? {
        return mockProfiles.firstOrNull { it.id == id }
    }

    override fun changeProfile(profile: Profile) {
        mockProfiles.indexOfFirst { it.id == profile.id }.also { index ->
            if (index != -1) {
                mockProfiles[index] = profile
            }
        }
    }
}