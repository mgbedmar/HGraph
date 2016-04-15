package groovy

import Domain.Graph.Author
import Domain.Graph.Graph
import Domain.Graph.Paper
import Domain.MatriuHeteSim
import spock.lang.Specification

class MatriuHeteSimTest extends Specification {

    def"creates empty matrix"(){
        given:
            def m = new MatriuHeteSim();
        expect:
            m.numRows() == 0
            m.numCols() == 0
    }


/* TODO: Graph & node
    def"generates HeteSim matrix"(){
        expect:
            m.numRows() == nr
            m.numCols() == nc
            m.value(i, j) == ij

        where:
        m                                                    | nr | nc | i | j | ij
        new MatriuHeteSim(false, "author", "paper", graf1()) | 2  | 2  | 0 | 0 | 0.5
        new MatriuHeteSim(false, "author", "paper", graf1()) | 2  | 2  | 0 | 1 | 0.5
        new MatriuHeteSim(false, "author", "paper", graf1()) | 2  | 2  | 1 | 0 | 0.5
        new MatriuHeteSim(false, "author", "paper", graf1()) | 2  | 2  | 1 | 1 | 0.5
    }

    private Graph graf1(){
        Author a1 = Stub()
        Author a2 = Stub()
        Set<Author> sa
        sa.add(a1)
        sa.add(a2)
        Paper p1 = Stub()
        Paper p2 = Stub()
        Set<Paper> sp
        sp.add(p1)
        sp.add(p2)
        Graph g = Stub()
        g.getSetOfNodes("author") >> sa
        g.getSetOfNodes("paper") >> sp
        g.getNeighbours(a1, "paper") >> sp
        g.getNeighbours(a2, "paper") >> sp
        g.getNeighbours(p1, "author") >> sa
        g.getNeighbours(p2, "author") >> sa
        return g
    }
    */
}
