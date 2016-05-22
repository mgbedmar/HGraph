(function() {
    'use strict';

    if (typeof app === 'undefined' || app.HGraph === 'undefined')
        throw 'Error de dependenciaes';

    //Private
    //Array of sigma instances
    var _sarr;
    //True if graph.nodes > maxNodes
    var _largeGraph;
    var _settings = {
        graph:{
            minNodeSize: 1,
            minEdgeSize: 0.2,
            maxEdgeSize: 0.5,
            zoomMin: 0.01,
            zoomMax: 2,
            eventsEnabled: true,
            labelThreshold: 25
        },
        relativeSize:0.5,
        nooverlap:false
    };

    function _applySettings(s, cb){
        if(typeof _settings.relativeSize !== 'undefined')
            sigma.plugins.relativeSize(s, _settings.relativeSize);
        //TODO adjust nooverlap
        if(typeof _settings.nooverlap !== 'undefined' && _settings.nooverlap)
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
    }

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

                for (var i = 0; i < nodes[type].size(); i++)
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
        var u = Math.random()*100+Math.random()*100+2;
        var r = u;
        if(u>1)
            r = 2-u;
        return {x: Math.cos(t)*r, y: Math.sin(t)*r};
    }


    //Public
    app.graph = {};

    app.graph.init = function(large){
        _largeGraph = large;
    };

    app.graph.isLarge = function(){
        return _largeGraph;
    };


    //Draws a graph with a big number of nodes but without edges, nodes = JavaArrayList
    app.graph.drawNodesOnlyGraph = function(nodes, cb){
        var g={nodes:[]};
        var i = 0;
        if(typeof _sarr === 'undefined')
            _sarr = [];
        //Start empty graph (for the camera)
        var s=new sigma({
            graph:g,
            settings:_settings.graph
        });
        var cam = s.addCamera();
        s.addRenderer({
            container: 'graph-container',
            type: 'canvas',
            camera: cam
        });
        s.refresh();
        _sarr.push(s);
        //n Callbacks
        var ncb=0;
        //For each node
        while(i < nodes.size())
        {
            //TODO: rings
            var pos = _getCircleRandomPos();

            g.nodes.push({
                id: String(nodes.get(i)[0]),
                label: String(nodes.get(i)[1]),
                x: pos.x,
                y: pos.y
            });
            //If graph is % maxNodes, start a new one
            if(i % app.settings.maxNodes == 0)
            {
                s=new sigma({
                    graph:g,
                    settings:_settings.graph
                });
                s.addRenderer({
                    container: 'graph-container',
                    type: 'canvas',
                    camera: cam
                });
                ncb++;
                _applySettings(s, function(){
                    ncb--;
                });
                s.refresh();
                _sarr.push(s);
                g = {nodes:[]};
            }
        }
        //If nodes remaining, start a new graph
        if(g.nodes.length > 0)
        {
            s=new sigma({
                graph:g,
                settings:_settings.graph
            });
            ncb++;
            _applySettings(s, function(){
                ncb--;
            });
            s.addRenderer({
                container: 'graph-container',
                type: 'canvas',
                camera: cam
            });
            s.refresh();
            _sarr.push(s);
        }

        //Polling callbacks number. Ends when ncb is 0
        var interval = setInterval(function(){
            if(ncb == 0)
            {
                clearInterval(interval);
                cb();
            }
        },1000);
    };
    //Draws a bigraph representing a table. Each connected component must be < maxNodes
    app.graph.drawTableBasedGraph = function(nodes, edges, cb){
        //TODO
    };

    //Draws a normal graph, nodes = {author:JavaArrayList, paper:...}
    app.graph.drawGraph = function(nodes, edges, cb){
        var g = _createGraph(nodes, edges);
        if(typeof _sarr === 'undefined')
        {
            //TODO: zoom, size, threshold
            _sarr = [new sigma({
                graph: g,
                settings: _settings.graph
            })];
        }
        else
        {
            var s0 =_sarr[0];
            _sarr.forEach(function(s, i){
                //this gets rid of all the ndoes and edges
                s0.graph.clear();
                s0.graph.kill();
                s0.refresh();
            });
            s0.graph = g;
            s0.refresh();
            _sarr = [s0];


        }



        _sarr[0].addRenderer({
            container: 'graph-container',
            type:'canvas',
            settings: {
                batchEdgesDrawing: true,
                hideEdgesOnMove: true
            }
        });
        _sarr[0].refresh();
        _applySettings(_sarr[0], cb);
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
