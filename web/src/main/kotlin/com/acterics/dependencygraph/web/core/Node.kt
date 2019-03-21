package com.acterics.dependencygraph.web.core

data class Node(val id: Int,
                val name: String,
                var source: Int,
                var dest: Int,
                var weight: Int = 1)