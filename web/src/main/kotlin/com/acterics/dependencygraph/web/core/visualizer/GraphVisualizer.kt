package com.acterics.dependencygraph.web.core.visualizer

import com.acterics.dependencygraph.web.core.D3Graph
import com.acterics.dependencygraph.web.d3
import com.acterics.dependencygraph.web.x
import com.acterics.dependencygraph.web.y
import kotlin.math.abs
import kotlin.math.sqrt

class GraphVisualizer(private val svg: dynamic,
                      private var d3Graph: D3Graph) {
    data class Config(var linkDistance: Int = 5,
                      var linkStrength: Double = 0.7,
                      var circleRadius: Int = 15,
                      var showCircleText: Boolean = false,
                      var maxTextLength: Int = 100,
                      var chargeMultiplier: Int = 100)

    var simutalion: dynamic = null
    var color: dynamic = null



    var links: dynamic = null
    var nodes: dynamic = null
    var textNode: dynamic = null

    private val config = Config()

    init {
        initialize()
    }

    fun setupZoom(container: dynamic) {
        val zoom = d3.zoom()
                .on("zoom") { svg.attr("transform", d3.event.transform) }

        container.append("rect")
                .attr("width", x)
                .attr("height", y)
                .style("fill", "none")
                .style("pointer-events", "all")
                .lower()
                .call(zoom)
    }

    fun updateGraph(d3Graph: D3Graph) {
        simutalion.stop()

        svg.selectAll(".node")
                .data(emptyArray<dynamic>())
                .exit().remove()
        svg.select("g").selectAll("path")
                .data(emptyArray<dynamic>())
                .exit().remove()
        svg.selectAll(".link")
                .data(emptyArray<dynamic>())
                .exit().remove()
        svg.selectAll("text")
                .data(emptyArray<dynamic>())
                .exit().remove()
        svg.append("defs").selectAll("marker")
                .data(emptyArray<dynamic>())
                .exit().remove()
        svg.append("g").selectAll(".node")
                .data(emptyArray<dynamic>())
                .exit().remove()
        svg.append("g").selectAll(".structNode")
                .data(emptyArray<dynamic>())
                .exit().remove()

        this.d3Graph = d3Graph
        initialize()

        updateTextVisibility(config.showCircleText)
    }

    fun updateColors(colorRegexes: dynamic) {

    }

    fun updateRadiuses(defaultRadius: Int) {
        config.circleRadius = defaultRadius
        nodes.transition().attr("r", this::nodeRadius)
        updateMarkers(defaultRadius.toDouble() / 3)
        simutalion.alphaTarget(0.3).restart()
    }

    fun updateTextVisibility(visible: Boolean) {
        config.showCircleText = visible
        textNode.attr("visibility", if (visible) "visible" else "hidden")
        simutalion.alphaTarget(0.3).restart()
    }

    fun updateCenter(x: Double, y: Double) {
        simutalion.force("center", d3.forceCenter(x / 2, y / 2))
    }

    fun updateChargeAndLinks() {
        updateCharge(config.chargeMultiplier)
        updateLinkStrength(config.linkStrength)
    }

    fun updateCharge(chargeMultiplier: Int) {
        config.chargeMultiplier = chargeMultiplier
        simutalion.force("charge", d3.forceManyBody().strength(this::nodeChargeStrength))
        simutalion.alphaTarget(0.3).restart()
    }

    fun updateLinkStrength(linkStrength: Double) {
        config.linkStrength = linkStrength
        simutalion.force("links", d3.forceLink(d3Graph.links)
                .distance(this::linkDistance)
                .strength(this::linkStrength)
        )
        simutalion.alphaTarget(0.3).restart()

    }

    fun updateMarkers(size: Double) {

        svg.selectAll("marker")
                .transition()
                .attr("viewBox", viewBox(0.0, -size, size * 2, size * 2))
                .attr("refX", size * 2)
                .attr("refY", 0)
                .attr("markerWidth", size * 2)
                .attr("markerHeight", size * 2)

        svg.selectAll("marker path")
                .transition()
                .attr("d", arrow(size))


    }


    private fun nodeRadius(node: dynamic): Double {
        return config.circleRadius * (1 + ((node.source as Double) / 10))
    }

    private fun nodeChargeStrength(node: dynamic): Double {
        return (-(node.weight as Double)* config.chargeMultiplier)
    }

    private fun linkDistance(link: dynamic): Double {
        return nodeRadius(link.source) + nodeRadius(link.target) + config.linkDistance
    }

    private fun linkStrength(link: dynamic): Double {
        return config.linkStrength
    }

    private fun transform(data: dynamic): String {
        return "translate(${data.x}, ${data.y})"
    }

    private fun linkLine(data: dynamic): String {
        val dx: Double = (data.target.x - data.source.x) as Double
        val dy: Double = (data.target.y - data.source.y) as Double
        val dr = sqrt(dx * dx + dy * dy)
        if (abs(dr) <= EPS) {
            return "M0,0L0,0"
        }

        val rSource = nodeRadius(data.sourceNode) / dr
        val rDest = nodeRadius(data.targetNode) / dr

        val startX = data.source.x + dx * rSource
        val startY = data.source.y + dy * rSource

        val endX = data.target.x - dx * rDest
        val endY = data.target.y - dy * rDest

        return "M$startX,${startY}L$endX,$endY"
    }

    private fun onNodeDragStarted(node: dynamic) {
        if (!d3.event.active as Boolean) {
            simutalion.alphaTarget(0.3).restart()
        }
        node.fx = node.x
        node.fy = node.y
    }

    private fun onNodeDragged(node: dynamic) {
        node.fx = d3.event.x
        node.fy = d3.event.y
    }

    private fun onNodeDragEnded(node: dynamic) {
        if (!d3.event.active as Boolean) {
            simutalion.alphaTarget(0)
        }
        if (node.fixed as? Boolean == false) {
            node.fx = null
            node.fy = null
        }
    }



    private fun initialize() {
        initColors()
        initMarkers(config.circleRadius.toDouble() / 3)
        initLinks()
        initNodes()
        initSimulation()
        initTexts()
        initDragging()
        initAllNodes()
    }

    private fun initColors() {
        color = d3.scaleOrdinal(d3.schemeCategory10)
    }

    private fun initMarkers(size: Double) {
        svg.append("defs").selectAll("marker")
                .data(arrayOf("default", "dependency", "dependants"))
                .enter().append("marker")
                .attr("id") { data -> data }
                .attr("orient", "auto")
                .attr("class", "marker")
                .append("path")
        updateMarkers(size)
    }

    private fun initLinks() {
        svg.append("g").selectAll("path")
                .data(d3Graph.links)
                .enter().append("path")
                .attr("class", "link")
                .attr("marker-end", "url(#default)")
                .style("stroke-width") { 1.0 }
        links = svg.selectAll("path.link")
    }

    private fun initNodes() {
        svg.append("g").selectAll(".node")
                .data(d3Graph.nodes)
                .enter().append("circle")
                .attr("class", "node")
                .attr("r", this::nodeRadius)
                .style("fill", "#AA0000")
                .attr("source") { data -> data.source }
                .attr("dest") { data -> data.dest }

        nodes = svg.selectAll(".node")
    }

    private fun initSimulation() {
        simutalion = d3.forceSimulation(d3.values(d3Graph.nodes))
                .force("x", d3.forceX())
                .force("y", d3.forceY())
                .force("center", d3.forceCenter(x / 2, y / 2))
                .force("charge", d3.forceManyBody().strength(this::nodeChargeStrength))
                .force("links", d3.forceLink(d3Graph.links)
                        .distance(this::linkDistance)
                        .strength(this::linkStrength)
                )
                .on("tick", this::simulationTick)
    }

    private fun initTexts() {
        svg.append("g").selectAll("text")
                .data(simutalion.nodes())
                .enter().append("text")
                .attr("visibility", "hidden")
                .text { data -> data.name.substring(0, config.maxTextLength) }

        textNode = svg.selectAll("text")
    }

    private fun initDragging() {
        nodes.call(d3.drag()
                .on("start", this::onNodeDragStarted)
                .on("drag", this::onNodeDragged)
                .on("end", this::   onNodeDragEnded))

    }

    private fun initAllNodes() {

    }

    private fun simulationTick() {
        links.attr("d", this::linkLine)
        nodes.attr("transform", this::transform)
        if (config.showCircleText) {
            textNode.attr("transform", this::transform)
        }
    }

    companion object {
        const val EPS = 0.0001

        private fun viewBox(x: Double, y: Double, w: Double, h: Double) = "$x $y $w $h"
        private fun moveTo(x: Double, y: Double) = "M$x,$y"
        private fun lineTo(x: Double, y: Double) = "L$x,$y"

        private fun arrow(size: Double): String {
            return "${moveTo(0.0, -size)}${lineTo(size * 2, 0.0)}${lineTo(0.0, size)}"
        }


    }
}