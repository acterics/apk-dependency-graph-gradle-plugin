package com.acterics.dependencygraph.web

import com.acterics.dependencygraph.web.base.Application
import org.w3c.dom.get
import kotlin.browser.document
import kotlin.browser.window


class MainApplication: Application() {
    override val stateKeys = listOf<String>()

    override fun start(state: Map<String, Any>) {
        println("Hello world with hot reload!!")

        val graph = Graph.build(dependencies.links)
        val d3Graph = graph.d3Graph()

        val element = document.documentElement
        val body = document.getElementsByTagName("body")[0]
        val x = window.innerWidth.nonZero() ?: element?.clientWidth.nonZero() ?: body?.clientWidth.nonZero()
        val y = window.innerHeight.nonZero() ?: element?.clientHeight.nonZero() ?: body?.clientHeight.nonZero()
        var svg: dynamic = null

        val container = d3.select(".chart").append("svg")
            .attr("width", x)
            .attr("height", y)
            .style("overflow", "hidden")
        
        container.append("rect")
            .attr("width", x)
            .attr("height", y)
            .attr("fill", "none")
            .style("pointer-events", "all")
            .call(d3.zoom().on("zoom") {
                svg.attr("transform", "${d3.event.transform}")
            })

        svg = container.append("g")
        
        val actions = GraphActions(svg, graph)

        svg.append("defs").selectAll("marker")
            .data(listOf("default", "dependency", "dependants"))
            .enter().append("marker")
            .attr("id", { d -> d })
            .attr("viewBox", "0 -5 10 10")
            .attr("refX", 10)
            .attr("refY", 0)
            .attr("markerWidth", 10)
            .attr("markerHeight", 10)
            .attr("orient", "auto")
            .attr("class", "marker")
            .append("path")
            .attr("d", "M0,-5L10,0L0,5")

        

        val link = svg.append("g").selectAll("path")
            .data(d3Graph.links)
            .enter().append("path")
            .attr("class", "link")
            .attr("marker-end", "url(#default)")
            .style("stroke-width", { 1 })

        val node = svg.append("g").selectAll("circle.node")
            .data(d3Graph.nodes)
            .enter().append("circle")
            .attr("r", this::radius)
            .attr("class", "node")
            .style("fill", this::color)
            .attr("source", { node -> node.source })
            .attr("dest", { node -> node.dest })
            


    }

    private fun radius(node: Node): Int {
        return DEFAULT_RADIUS + DEFAULT_RADIUS * node.source / 10
    }

    private fun color(node: Node): String {
        return "#FF0000"
    }

    override fun dispose() = mapOf<String, Any>()

    private fun Int?.nonZero(): Int? {
        return if (this == 0) {
            null
        } else {
            this
        }
    }

    companion object {
        private const val DEFAULT_RADIUS = 10
    }
}