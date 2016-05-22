(function() {
    'use strict';

    if (typeof app === 'undefined' || app.HGraph === 'undefined')
        throw 'Error de dependenciaes';

    //Private
    var _s;
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

    function _createGraph(nodes, edges){
        var g = {nodes:[], edges:[]};

        //For each type in nodes
        for (var type in nodes)
        {

            //Check if type is a property of nodes
            if (nodes.hasOwnProperty(type))
            {
                //TODO: calculate radius and position and color of types
                //Add nodes of type to the graph
                app.HGraph.log(type);
                for (var i = 0; i < Math.sqrt(nodes[type].size()); i++)
                {
                    var pos = _getCircleRandomPos();

                    g.nodes.push({
                        id: String(nodes[type].get(i)[0]),
                        label: String(nodes[type].get(i)[1]),
                        x: pos.x,
                        y: pos.y
                    });
                }

            }
        }
        //TODO: edges

        return g;
    }
    function _getCircleRandomPos(){
        var t = 2*Math.PI*Math.random();
        var u = Math.random()+Math.random();
        var r = u;
        if(u>1)
            r = 2-u;
        return {x: Math.cos(t)*r, y: Math.sin(t)*r};
    }


    //Public
    app.graph = {};


    //Draws a graph with a big number of nodes but without edges
    app.graph.drawNodesOnlyGraph = function(nodes, cb){
        //TODO
        app.graph.drawGraph(nodes, {}, function(){cb()});
    };
    //Draws a bigraph representing a table. Each connected component must be < maxNodes
    app.graph.drawTableBasedGraph = function(nodes, edges, cb){
        //TODO
    };

    //Draws a normal graph
    app.graph.drawGraph = function(nodes, edges, cb){
        var g = _createGraph(nodes, edges);
        if(typeof _s === 'undefined')
        {
            //TODO: zoom, size, threshold
            _s = new sigma({
                graph: g,
                container: 'graph-container',
                settings: _settings.graph
            });
        }
        else
        {
            //this gets rid of all the ndoes and edges
            _s.graph.clear();
            _s.graph.kill();
            _s.graph = g;
            _s.refresh();
        }

        if(typeof _settings.relativeSize !== 'undefined')
            sigma.plugins.relativeSize(_s, _settings.relativeSize);
        //TODO adjust nooverlap
        if(typeof _settings.nooverlap !== 'undefined')
        {
            // Configure the noverlap layout:
            var noverlapListener = _s.configNoverlap({
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
            _s.startNoverlap();
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
