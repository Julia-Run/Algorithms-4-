/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

//  java -cp .:.lift/algs4.jar PrimMst tinyEWG.txt
public class PrimMst {
    // api: prim(), edges(), weight();
    // start from one vertex; for all vertexes: keep logging min edge && distance to it
    private Edge[] edgeTo;
    private double[] disTo;
    private IndexMinPQ<Double> pq;
    private boolean[] marked;

    public PrimMst(EdgeWeightedGraph g) {
        marked = new boolean[g.V()];
        edgeTo = new Edge[g.V()];
        pq = new IndexMinPQ<Double>(g.V());
        disTo = new double[g.V()];
        for (int x = 0; x < g.V(); ++x)
            disTo[x] = Double.POSITIVE_INFINITY;  // max distance at first;
        disTo[0] = 0.0;
        pq.insert(0, 0.0);  // start
        while (!pq.isEmpty()) {
            int v = pq.delMin();  // key;
            visit(g, v);
        }
    }

    public void visit(EdgeWeightedGraph g, int v) {
        marked[v] = true;
        for (Edge e : g.adj(v)) {
            int w = e.other(v);
            if (marked[w]) continue;
            if (e.weight() < disTo[w]) { // renew edgeTo and disTo ----min
                edgeTo[w] = e;
                disTo[w] = e.weight();
                if (pq.contains(w)) pq.changeKey(w, disTo[w]);  // keep min in pq;
                else pq.insert(w, disTo[w]);
            }
        }
    }

    // public Iterable<Edge> edges() {
    //     Queue<Edge> mst = new Queue<Edge>();
    //     for (int v = 0; v < edgeTo.length; v++) {
    //         Edge e = edgeTo[v];
    //         if (e != null) {  // seems the edgeTo[0]=null
    //             mst.enqueue(e);
    //         }
    //     }
    //     return mst;
    // }
    public Iterable<Edge> edges() {
        Queue<Edge> mst = new Queue<Edge>();
        for (int v = 1; v < edgeTo.length; v++) {  // seems the edgeTo[0]=null
            Edge e = edgeTo[v];
            mst.enqueue(e);
        }
        return mst;
    }

    public double weight() {
        double s = 0.0;
        for (Edge e : edges())
            s += e.weight();
        return s;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        PrimMst mst = new PrimMst(G);
        for (Edge e : mst.edges()) {
            StdOut.println(e);
        }
        StdOut.printf("%.5f\n", mst.weight());
        // StdOut.println(mst.edgeTo.length);
        // for (Edge e: mst.edgeTo) StdOut.println(e);  // seems the edgeTo[0]=null
    }
}
