package com.acterics.dependencygraph.web

import org.w3c.dom.get
import kotlin.browser.document
import kotlin.browser.window

external fun require(name: String): dynamic

external val dependencies: dynamic

val d3: dynamic = require("d3")!!

fun main(args: Array<String>) {
    println("Hello world!!!")
    println("dependencies: ${dependencies.links}")

    val graph = Graph.build(dependencies.links)
    val d3jsGraph = graph.d3jsGraph()

    val element = document.documentElement
    val body = document.getElementsByTagName("body")[0]
    val x = window.innerWidth.nonZero() ?: element?.clientWidth.nonZero() ?: body?.clientWidth.nonZero()
    val y = window.innerHeight.nonZero() ?: element?.clientHeight.nonZero() ?: body?.clientHeight.nonZero()
    

    val container = d3.select("#chart").append("svg")
        .attr("width", x)
        .attr("height", y)
        .style("overflow", "hidden")
    
    val svg = container.append("g")
    
    val actions = GraphActions(svg, graph)

    svg.append("defs").selectAll("marker")
        .data(listOf("default", "dependency", "dependants"))
        .enter().append("marker")
        .attr("id") { d -> d }
        .attr("viewBox", "0 -5 10 10")
        .attr("refX", 10)
        .attr("refY", 0)
        .attr("markerWidth", 10)
        .attr("markerHeight", 10)
        .attr("orient", "auto")
        .attr("class", "marker")
        .append("path")
        .attr("d", "M0,-5L10,0L0,5")

    container.append("rect")
        .attr("width", x)
        .attr("height", y)
        .attr("fill", "none")
        .style("pointer-events", "all")
        .call(d3.behavior.zoom().on("zoom") {
            svg.attr("transform", "translate(${d3.event.translate})")
        })

}

private fun Int?.nonZero(): Int? {
    return if (this == 0) {
        null
    } else {
        this
    }
}