package com.acterics.dependencygraph.web.app

import com.acterics.dependencygraph.web.core.Graph
import com.acterics.dependencygraph.web.core.Node
import com.acterics.dependencygraph.web.core.visualizer.GraphVisualizer
import com.acterics.dependencygraph.web.d3
import com.acterics.dependencygraph.web.dependencies
import com.acterics.dependencygraph.web.x
import com.acterics.dependencygraph.web.y
import org.w3c.dom.get
import kotlin.browser.document
import kotlin.browser.window



class MainApplication: Application() {
    override val stateKeys = listOf<String>()

    private var simulation: dynamic = null
    private var link: dynamic = null
    private var node: dynamic = null
    private var text: dynamic = null

    private var chargeMultiplier = 10
    private var defaultLinkDistance = 100
    private var defaultLinkStrength = 0.7
    private var defaultMaxTextLength = 20
    private var defaultRadius = 2.5

    override fun start(state: Map<String, Any>) {
        println("Hello world with hot reload!!")

        val graph = Graph.build(dependencies.links)
        val d3Graph = graph.d3Graph()



        val container = d3.select(".chart").append("svg")
                .attr("width", x)
                .attr("height", y)
                .style("overflow", "hidden")

        val svg = container.append("g")
        val visualizer = GraphVisualizer(svg, d3Graph)
        visualizer.setupZoom(container)



//        val zoomContainer = container.append("rect")
//             .attr("width", x)
//             .attr("height", y)
//             .attr("fill", "none")
//             .style("pointer-events", "all")
//
//        val graphContainer = container.append("g")
//                .attr("width", x)
//                .attr("height", y)
//
//        zoomContainer.call(d3.zoom().on("zoom") {
//                 graphContainer.attr("transform", "${d3.event.transform}")
//             })
//
//
//        simulation = d3.forceSimulation()
//                .force("link", d3.forceLink().id { d -> d.id })
//                .force("charge", d3.forceManyBody().strength { d ->
//                    -d.weight * chargeMultiplier
//                })
//                .force("collision", d3.forceCollide().radius { d -> radius(d) })
//                .force("center", d3.forceCenter(x / 2, y / 2))
//
//        link = graphContainer.append("g")
//                .attr("class", "links")
//                .selectAll("line")
//                .data(d3Graph.links)
//                .enter().append("line")
//
//        node = graphContainer.append("g")
//                .attr("class", "nodes")
//                .selectAll("circle")
//                .data(d3Graph.nodes)
//                .enter().append("circle")
//                .attr("r") { d -> radius(d) }
//                .attr("fill") { d -> color(d) }
//                .call(d3.drag()
//                        .on("start", this::dragStarted)
//                        .on("drag", this::dragged)
//                        .on("end", this::dragEnded)
//                )
//
//
//
//
//        node.append("title")
//                .text { d -> d.id }
//
//        simulation.nodes(d3Graph.nodes)
//                .on("tick", this::ticked)
//
//        // text = graphContainer.append("g")
//        //         .selectAll("text")
//        //         .data(simulation.nodes())
//        //         .enter().append("text")
//        //         .text { d -> d.name.substring(0, defaultMaxTextLength) }
//
//        simulation.force("link")
//                .links(d3Graph.links)
//                .distance { l ->
//                    radius(l.source) + radius(l.target) + defaultLinkDistance
//                }
//                .strength { l ->
//                    defaultLinkStrength
//                }
    }

    private fun radius(node: Node): Double {
        return defaultRadius + defaultRadius * node.source.toDouble() / 10
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

    



    override fun dispose() = mapOf<String, Any>()


}