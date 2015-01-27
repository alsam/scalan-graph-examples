package scalan

import scala.collection.mutable.{Queue,Set,ListBuffer}

trait BFS {

  type Adj = List[Int]
  type Graph = Array[Adj]

  //final val NoParent = -1

  def bfs(g: Graph, start: Int): List[Int] = {
    val nodes = ListBuffer(start)
    val frontier = Queue(start)
    val visited = Set[Int]()
    while (!frontier.isEmpty) {
      val node = frontier.dequeue
      visited += node
      val neighbors = g(node)
      for (n <- neighbors) {
        //println(n)
        if (!visited(n)) {
          visited += n
          nodes += n
        }
      }
    }
    nodes.toList
  }


}

class BFSdriver extends BFS

object TestBFS {
  def main(args: Array[String]) {
    println("Hello, world!")

    val graph500_s3 = Array(
      List(1, 2, 4, 6, 7),
      List(0, 2, 3, 4, 6, 7),
      List(0, 1, 4, 7),
      List(1, 4, 5, 7),
      List(0, 1, 2, 3, 5, 6, 7),
      List(3, 4, 6),
      List(0, 1, 4, 5),
      List(0, 1, 2, 3, 4)
    )


    val bfs_driver = new BFSdriver
    val bfs_nodes = bfs_driver.bfs(graph500_s3, 1)

    println("bfs_nodes: " + bfs_nodes)
  }
}

