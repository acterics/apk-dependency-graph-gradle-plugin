package com.acterics.dependencygraph.web.core

data class Link(val source: Int,
                val target: Int,
                val sourceNode: Node,
                val targetNode: Node)