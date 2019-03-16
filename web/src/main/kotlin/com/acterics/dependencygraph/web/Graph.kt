package com.acterics.dependencygraph.web

import com.acterics.dependencygraph.web.interop.D3JsGraph

class Graph {
    private val nodes: MutableList<Node> = mutableListOf()
    private val links: MutableList<Link> = mutableListOf()
    private val nodesSet: MutableMap<String, Node> = mutableMapOf()
    private var nodeIndex: Int = 0


    fun addLink(link: DependencyLink) {
        println("Add link: ${link.source} -> ${link.dest}")

        val sourceNode = getNode(link.source)
        sourceNode.source++

        val destNode = getNode(link.dest)
        destNode.dest++

        links.add(Link(sourceNode.id, destNode.id, sourceNode, destNode))
    }

    fun getNode(nodeName: String): Node {
        var node = nodesSet[nodeName]
        if (node == null) {
            val index = nodeIndex
            node = Node(index, nodeName, 1, 0)
            nodesSet[nodeName] = node
            nodeIndex++
        }
        return node
    }

    fun updateNodes(function: (Node) -> Unit) {
        nodes.forEach(function)
    }

    fun d3Graph(): D3JsGraph {
        val nodes = nodesSet.values.sortedBy { it.id }
        println("d3 graph with nodes: ${nodes.size}, links: ${links.size}")
        return D3JsGraph(nodes.toTypedArray(), links.toTypedArray())
    }


    companion object {

        fun build(dependencies: dynamic): Graph {
            val graph = Graph()

            dependencies.forEach { link ->
                graph.addLink(DependencyLink(link.source as String, link.dest as String))
            }

            graph.updateNodes { node ->
                node.weight = node.source
            }
            
            return graph
        }

    }


}

data class DependencyLink(val source: String, val dest: String)

data class Node(val id: Int,
                val name: String,
                var source: Int,
                var dest: Int,
                var weight: Int = 0)

data class Link(val source: Int,
                val target: Int,
                val sourceNode: Node,
                val targetNode: Node)