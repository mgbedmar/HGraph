(function() {
    'use strict';

    if (typeof app === 'undefined' || app.HGraph === 'undefined')
        throw 'Error de dependenciaes';

    //Private
    //Domain layer nodes/edges, separated in Java's ArrayList by type
    var _nodes;
    var _edges;
    var _settings = {
        graph:{
            minNodeSize: 1,
            minEdgeSize: 0.2,
            maxEdgeSize: 0.5,
            zoomMin: 0.01,
            zoomMax: 0.5,
            eventsEnabled: true,
            labelThreshold: 25
        },
        relativeSize:0.5,
        nooverlap:false
    };
    //Sigma instance
    var _s;
    //Sigma graph
    var _g;

    //populates _g with a sigmajs graph using _nodes and _edges
    function _createGraph(){
        //For each type in _nodes
        for (var type in _nodes)
        {
            //Check if type is a property of _nodes
            if (_nodes.hasOwnProperty(type))
            {
                //TODO: calculate radius and position and color of types
                //Add nodes of type to the graph
                for (var i = 0; i < Math.sqrt(_nodes.size()); i++)
                {
                    var pos = _getCircleRandomPos();

                    _g.nodes.push({
                        id: String(_nodes.get(i)[0]),
                        label: String(_nodes.get(i)[1]),
                        x: pos.x,
                        y: pos.y
                    });
                }

            }
        }
        //TODO: edges
    }
    function _getCircleRandomPos(){
        var t = 2*Math.PI*Math.random();
        var u = Math.random()+Math.random();
        var r = u;
        if(u>1)
            r = 2-u;
        return {x: Math.cos(t)*r, y: Math.sin(t)*r};
    }

    function _setNodes(nodes){
        _nodes = nodes;
    }
    function _setEdges(edges){
        _edges = edges;
    }

    //Public
    app.graph = {};

    app.graph.setGraph = function(nodes, edges){
        _setNodes(nodes || {});
        _setEdges(edges || {});
    };

    app.graph.update = function(){
        _createGraph();
        s.graph = _g;
        _s.refresh();
    };

    //Draws a graph with a big number of nodes but without edges
    app.graph.drawNodesOnlyGraph = function(cb){
        //TODO
    };
    //Draws a bigraph representing a table. Each connected component must be < maxNodes
    app.graph.drawTableBasedGraph = function(cb){
        //TODO
    };

    //Draws a normal graph
    app.graph.drawGraph = function(cb){

        _createGraph();

        //TODO: zoom, size, threshold
        _s = new sigma({
            graph: _g,
            container: 'graph-container',
            settings: _settings.graph
        });

        if(typeof _settings.relativeSize !== 'undefined')
            sigma.plugins.relativeSize(s, _settings.relativeSize);
        //TODO
        if(typeof _settings.nooverlap !== 'undefined')
        {
            // Configure the noverlap layout:
            var noverlapListener = s.configNoverlap({
                nodeMargin: 0.05,
                scaleNodes: 0.9,
                gridSize: 400,
                speed:5
            });
            // Bind the events:
            noverlapListener.bind('start stop interpolate', function(e) {
                console.log(e.type);
                if(e.type === 'start') {
                    console.time('noverlap');
                }
                if(e.type === 'stop') {
                    console.timeEnd('noverlap');
                    cb();
                }
            });
            s.startNoverlap();
        }
        else
            cb();
    };

    /*
    app.graph.drawGraph = function(cb){
        var i,
            s,
            o,
            N = 2000,
            E = 0,
            C = 4,
            d = 0.5,
            cs = [],
            g = {
                nodes: [],
                edges: []
            };

// Generate the graph:
        for (i = 0; i < C; i++)
            cs.push({
                id: i,
                nodes: [],
                color: '#' + (
                    Math.floor(Math.random() * 16777215).toString(16) + '000000'
                ).substr(0, 6)
            });

        for (i = 0; i < N; i++) {
            o = cs[(Math.random() * C) | 0];
            var pos = _getCircleRandomPos();
            g.nodes.push({
                id: 'n' + i,
                label: 'Node' + i,
                x: pos.x,
                y: pos.y,
                size: Math.random()*30,
                color: o.color
            });
            o.nodes.push('n' + i);
        }

        for (i = 0; i < E; i++) {
            if (Math.random() < 1 - d)
                g.edges.push({
                    id: 'e' + i,
                    source: 'n' + ((Math.random() * N) | 0),
                    target: 'n' + ((Math.random() * N) | 0),
                    size:Math.random()*10,
                    hidden: true
                });
            else {
                o = cs[(Math.random() * C) | 0]
                g.edges.push({
                    id: 'e' + i,
                    source: o.nodes[(Math.random() * o.nodes.length) | 0],
                    target: o.nodes[(Math.random() * o.nodes.length) | 0],
                    size:Math.random()*10,
                    hidden: true
                });
            }
        }

        s = new sigma({
            graph: g,
            container: 'graph-container',
            settings: {
                minNodeSize: 2,
                //maxNodeSize: 2,
                //minEdgeSize: 1,
                //maxEdgeSize: 1,
                eventsEnabled: false
            }
        });

        sigma.plugins.relativeSize(s, 4);

        //cb();

// Configure the noverlap layout:
        var noverlapListener = s.configNoverlap({
            nodeMargin: 0.05,
            scaleNodes: 0.9,
            gridSize: 400,
            speed:5
        });
// Bind the events:
        noverlapListener.bind('start stop interpolate', function(e) {
            console.log(e.type);
            if(e.type === 'start') {
                console.time('noverlap');
            }
            if(e.type === 'stop') {
                console.timeEnd('noverlap');
                cb();
            }
        });
// Start the layout:
        s.startNoverlap();

    };*/
}).call(window);
