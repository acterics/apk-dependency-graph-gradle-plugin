package com.acterics.dependencygraph.web.app

import com.acterics.dependencygraph.web.*
import com.acterics.dependencygraph.web.core.Graph
import com.acterics.dependencygraph.web.core.visualizer.GraphVisualizer
import kotlin.browser.window



class MainApplication: Application() {

    override val stateKeys = listOf<String>()

    private lateinit var visualizer: GraphVisualizer

    override fun start(state: Map<String, Any>) {
        println("Hello world with hot reload!!")

        updateWindowSize()

        val graph = Graph.build(dependencies.links)
        val d3Graph = graph.d3Graph()

        val container = d3.select(".chart").append("svg")
                .attr("width", x)
                .attr("height", y)
                .style("overflow", "hidden")

        val svg = container.append("g")
        visualizer = GraphVisualizer(svg, d3Graph)
        visualizer.setupZoom(container)

        visualizer.nodes
                .on("click", this::onNodeClicked)
                .on("contextmenu", this::onNodeContextMenu)

        window.onresize = {
            updateWindowSize()
            container.attr("width", x).attr("height", y)
            visualizer.updateCenter(x.toDouble() / 2, y.toDouble() / 2)
        }

        d3.selectAll("input").on("change") { onInputChange(js("this"))}

    }

    override fun dispose() = mapOf<String, Any>()


    private fun onNodeClicked(node: dynamic) {
        if (d3.event.defaultPrevented as Boolean) { return }
        println("on $node clicked")
    }

    private fun onNodeContextMenu(node: dynamic) {
        if (d3.event.defaultPrevented as Boolean) { return }
        d3.event.preventDefault()
        println("on $node context menu")
    }

    private fun onInputChange(input: dynamic) {
        println("name: ${input.name}")
        when (input.name) {
            "circle-size" -> onCircleSizeChanged(input.value)
            "charge-multiplier" -> onChargeMultiplierChanged(input.value)
            "link-strength" -> onLinkStrengthChanged(input.value / 10)
            "show-texts-near-circles" -> onTextVisibleChanged(input.checked)
        }
    }

    private fun onCircleSizeChanged(value: Double) {
        visualizer.updateRadiuses(value.toInt())
    }

    private fun onChargeMultiplierChanged(value: Double) {
        visualizer.updateCharge(value.toInt())
    }

    private fun onLinkStrengthChanged(value: Double) {
        visualizer.updateLinkStrength(value)
    }

    private fun onTextVisibleChanged(visible: Boolean) {
        visualizer.updateTextVisibility(visible)
    }
}

