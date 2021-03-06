package scalan.graph.examples

import scala.collection.mutable.{ListBuffer, BitSet}
import scala.io.Source
import scala.collection.mutable.Stack

/**
 * Strongly Connected Components
 */
object Scc {

  type AdjacencyList = Array[ListBuffer[Int]]
  type SCC = ListBuffer[Int]
  type SCCs = ListBuffer[SCC]

  class Graph(original: AdjacencyList, reversed: AdjacencyList) {
    val length = original.length

    class State() {
      private val visited = new BitSet
      def setVisited(node: Int) = visited += node
      def isNotVisited(node: Int): Boolean = !visited(node)
    }

    def stronglyConnectedComponents: SCCs = {
      def dfs(graph: AdjacencyList, state: State = new State)(i: Int, acc: List[Int] = Nil): List[Int] = {
        state.setVisited(i)
        i :: graph(i).foldLeft[List[Int]](acc)((acc, j) => {
          if (state.isNotVisited(j)) dfs(graph, state)(j, acc) else acc
        })
      }
      def dfs2(graph: AdjacencyList, state: State = new State)(start: Int): SCC = {
        val stack = Stack(start)
        val head = ListBuffer.empty[Int]
        val tail = ListBuffer.empty[Int]
        while (!stack.isEmpty) {
          val i = stack.pop
          state.setVisited(i)
          val ins = graph(i).foldLeft(List.empty[Int])((list, j) => {
            if (state.isNotVisited(j)) j :: list else list
          })
          if (ins.isEmpty) i +=: tail else {stack pushAll ins; head += i}
        }
        head ++= tail
      }
      println("Start DFS Pass #1")
      val state1 = new State
      val dfsPass1 = dfs2(reversed, state1) _
      val finished = (length - 1 to 0 by -1).foldLeft(ListBuffer.empty[Int])((list, i) => {
        if (state1.isNotVisited(i)) dfsPass1(i) ++= list else list
      })
      println("Start DFS Pass #2")
      val state2 = new State
      val dfsPass2 = dfs2(original, state2) _
      for (i <- finished if state2.isNotVisited(i)) yield dfsPass2(i)
    }
  }

  object Graph {
    private val Pattern = "(\\d+) (\\d+)".r("out", "in")
    private def extract(line: String) = Pattern.findFirstMatchIn(line) match {
      case Some(m) => (m.group("out").toInt, m.group("in").toInt)
      case None => throw new IllegalArgumentException("Malformed row")
    }
    def load(filename: String): Graph = {
      val resource = getClass.getClassLoader.getResource(filename)
      val size = Source.fromURL(resource).getLines().foldLeft(0)((max, line) => {
        Math.max(extract(line)._1, max)
      })
      val original = Array.fill[ListBuffer[Int]](size) {ListBuffer.empty}
      val reversed = Array.fill[ListBuffer[Int]](size) {ListBuffer.empty}
      Source.fromURL(resource).getLines().foreach(line => {
        val (out, in) = extract(line)
        original(out - 1) += in - 1
        reversed(in - 1) += out - 1
      })
      new Graph(original, reversed)
    }
  }

}
