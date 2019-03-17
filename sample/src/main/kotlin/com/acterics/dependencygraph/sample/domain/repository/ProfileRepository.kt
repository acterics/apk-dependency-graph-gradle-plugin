package com.acterics.dependencygraph.sample.domain.repository

import com.acterics.dependencygraph.sample.domain.model.Profile

interface ProfileRepository {

    fun getProfiles(): List<Profile>
    fun getProfile(id: Long): Profile?
    fun changeProfile(profile: Profile)

}