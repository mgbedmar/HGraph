package groovy

import Domain.Graph.Graph
import spock.lang.Specification

class GraphTest extends Specification {
    /*TODO: Graph & node pull

    def"adds a node, retrieves with name"(){
        given:
            def g = new Graph()
            g.afegirNode(name,type)
        expect:
            g.getNode(name, type).getName() == name

        where:
            name | type
            "a1" | "author"
            "p1" | "paper"

    }
    def"adds a node, retrieves with returned id"(){
        given:
            def g = new Graph()
            def a = g.afegirNode(name,type)
        expect:
            g.getNode(a, type).getName() == name

        where:
            name | type
            "a1" | "author"
            "p1" | "paper"

    }

    def"erases a node"(){
        given:
            def g = new Graph()

        when: "adding 1 node"
            g.afegirNode("autor1", "author");
        and: "erasing it"
            g.esborrarNode("autor1", "author")
        then: "set is size 0"
            g.getSetOfNodes("author").size() == 0


        when: "adding 3 nodes"
            g.afegirNode("asdf", "author");
            g.afegirNode("asdf1", "paper");
            g.afegirNode("asdf2", "term");
        and: "erasing 1"
            g.esborrarNode("asdf", "author")
        then: "set is size 2"
            g.getSetOfNodes("author").size() == 2
    }

    def"gets a set of all the nodes in graph"(){
        given:
            def g = new Graph()

        when: "graph's empty"
        then:
            g.getSetOfNodes().size() == 0

        when: "adding 1 node"
            g.afegirNode("autor1", "author");

        then: "set is size 1"
            g.getSetOfNodes("author").size() == 1


        when: "adding 3 nodes"
            g.afegirNode("asdf", "author");
            g.afegirNode("asdf1", "paper");
            g.afegirNode("asdf2", "term");

        then: "set is size 3"
            g.getSetOfNodes("author").size() == 3
    }

    def"gets a set of a given type nodes"(){
        given:
            def g = new Graph()
        when: "there are no nodes of given type"
            g.afegirNode("asdf", "author");

        then:
            g.setOfNodes("paper").size() == 0

        when: "adding 1 paper, 1 author"
            g.afegirNode("asdf", "author");
            g.afegirNode("asdf1", "paper");

        then: "set is size 1"
            g.getSetOfNodes("paper").size() == 1
    }
*/
}
