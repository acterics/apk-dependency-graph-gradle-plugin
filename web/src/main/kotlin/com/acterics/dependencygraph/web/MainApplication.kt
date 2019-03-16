package com.acterics.dependencygraph.web

import com.acterics.dependencygraph.web.base.Application
import com.acterics.dependencygraph.web.base.require
import org.w3c.dom.get
import kotlin.browser.document
import kotlin.browser.window

external val dependencies: dynamic
private val d3 = require("d3")

class MainApplication: Application() {
    override val stateKeys = listOf<String>()

    private var simulation: dynamic = null
    private var link: dynamic = null
    private var node: dynamic = null
    private var text: dynamic = null

    private var chargeMultiplier = 200
    private var defaultLinkDistance = 100
    private var defaultLinkStrength = 0.7
    private var defaultMaxTextLength = 20

    override fun start(state: Map<String, Any>) {
        println("Hello world with hot reload!!")

        val graph = Graph.build(dependencies.links)
        val d3Graph = graph.d3Graph()

        val element = document.documentElement
        val body = document.getElementsByTagName("body")[0]
        val x: Int = window.innerWidth.nonZero() ?: element?.clientWidth.nonZero() ?: body?.clientWidth.nonZero() ?: 0
        val y: Int = window.innerHeight.nonZero() ?: element?.clientHeight.nonZero() ?: body?.clientHeight.nonZero() ?: 0


        val container = d3.select(".chart").append("svg")
            .attr("width", x)
            .attr("height", y)


        val zoomContainer = container.append("rect")
             .attr("width", x)
             .attr("height", y)
             .attr("fill", "none")
             .style("pointer-events", "all")

        val graphContainer = container.append("g")
                .attr("width", x)
                .attr("height", y)

        zoomContainer.call(d3.zoom().on("zoom") {
                 graphContainer.attr("transform", "${d3.event.transform}")
             })


        simulation = d3.forceSimulation()
                .force("link", d3.forceLink().id { d -> d.id })
                .force("charge", d3.forceManyBody().strength { d ->
                    -d.weight * chargeMultiplier
                })
                .force("collision", d3.forceCollide().radius { d -> radius(d) })
                .force("center", d3.forceCenter(x / 2, y / 2))

        link = graphContainer.append("g")
                .attr("class", "links")
                .selectAll("line")
                .data(d3Graph.links)
                .enter().append("line")

        node = graphContainer.append("g")
                .attr("class", "nodes")
                .selectAll("circle")
                .data(d3Graph.nodes)
                .enter().append("circle")
                .attr("r") { d -> radius(d) }
                .attr("fill") { d -> color(d) }
                .call(d3.drag()
                        .on("start", this::dragStarted)
                        .on("drag", this::dragged)
                        .on("end", this::dragEnded)
                )




        node.append("title")
                .text { d -> d.id }

        simulation.nodes(d3Graph.nodes)
                .on("tick", this::ticked)

        text = graphContainer.append("g")
                .selectAll("text")
                .data(simulation.nodes())
                .enter().append("text")
                .text { d -> d.name.substring(0, defaultMaxTextLength) }

        simulation.force("link")
                .links(d3Graph.links)
                .distance { l ->
                    radius(l.source) + radius(l.target) + defaultLinkDistance
                }
                .strength { l ->
                    defaultLinkStrength
                }          
    }

    private fun radius(node: Node): Int {
        return DEFAULT_RADIUS + DEFAULT_RADIUS * node.source / 10
    }

    private fun transform(node: dynamic): String {
        return "translate(${node.x}, ${node.y})"
    }

    private fun dragStarted(d: dynamic) {
        if (!d3.event.active) {
            simulation?.alphaTarget(0.3).restart()
        }
        d.fx = d.x
        d.fy = d.y
    }

    private fun dragged(d: dynamic) {
        d.fx = d3.event.x
        d.fy = d3.event.y
    }

    private fun dragEnded(d: dynamic) {
        if (!d3.event.active) simulation.alphaTarget(0)
        d.fx = null
        d.fy = null
    }

    private fun color(node: Node): String {
        return "#FF0000"
    }

    private fun ticked() {
        link
                .attr("x1") { d -> d.source.x }
                .attr("y1") { d -> d.source.y }
                .attr("x2") { d -> d.target.x }
                .attr("y2") { d -> d.target.y }

        node
                .attr("cx") { d -> d.x }
                .attr("cy") { d -> d.y }

        text.attr("transform", this::transform)
    }

    

    private fun Int?.nonZero(): Int? {
        return if (this == 0) {
            null
        } else {
            this
        }
    }

    override fun dispose() = mapOf<String, Any>()

    companion object {
        private const val DEFAULT_RADIUS = 10
    }
}