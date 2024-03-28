package kotlin_experiment7.utilities

import java.io.FileInputStream
import kotlin.streams.toList

//val FN = "/home/andy/repos/gtd/gtd.org"
val FN = "/home/andy/temp.org"

sealed class Node() {
    val children: MutableList<Node> = mutableListOf()

    abstract val depth : Int

    abstract val parent : Node
}

class RootNode() : Node() {
    override val depth = 0

    override val parent : Node by lazy {
        require(false) {"Shouldn't be here!!!"}
        this
    }
}

data class HeadingNode(val heading: String, override val parent: Node) : Node() {
    override val depth = heading.takeWhile { it == '*' }.length
}

class StringNode(override val parent: Node) : Node() {
    var strings: MutableList<String> = mutableListOf()
    override val depth = parent!!.depth + 1
}

fun main() {
    //data class State(val currentParent : Node.ParentNode, val childNode : Node)
    //var currentHeading : HeadingNode? = null
    var rootNode: RootNode = RootNode()
    var currentNode: Node = rootNode
    val lines = FileInputStream(FN).bufferedReader().lines()!!.toList()

    for (line in lines) {
        if (line.startsWith("*")) {
            //println("Heading depth=${line.depth()} $line")
            //val asHeading = currentNode as HeadingNode
            val newNode = HeadingNode(heading = line, parent = currentNode)
            when (currentNode) {
                is RootNode -> {
                    println("Adding to root ${(newNode as HeadingNode).heading}")
                    currentNode.children.add(newNode)
                    currentNode = newNode
                }

                is HeadingNode -> {
                    println("depth ${newNode.depth} vs ${currentNode.depth}")
                    if (newNode.depth>currentNode.depth) {
                        println("Adding deeper node $line")
                        currentNode.children.add(newNode)
                    } else {
                        val target = run {
                            var current : Node = newNode
                            do {
                                current = current.parent!!
                                println("Current ${current.depth} newNode=${newNode.depth}")
                            } while ( current.depth != newNode.depth -1 )
                            current
                        }
                        //print("Target is ${(target as HeadingNode).heading} newNode is ${newNode.heading}")
                        //println("Add to parent ${(currentNode!!.parent as HeadingNode).heading} $line")
                        target.children.add(newNode)
                    }
                    currentNode = newNode
                }

                is StringNode -> {
                    println("Stringnode $line")
                    if (currentNode.parent != null) {
                        currentNode.parent!!.children.add(newNode)
                    } else {
                        currentNode.children.add(newNode)
                    }
                    currentNode = newNode
                }
            }
        } else {
            when (currentNode) {
                is StringNode -> (currentNode as StringNode).strings.add(line)
                else -> {
                    StringNode(currentNode).also {
                        currentNode.children.add(it)
                        it.strings.add(line)
                        currentNode = it }
                }
            }
        }
    }

    println("root is $rootNode")
}

