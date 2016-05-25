/*
         settings: {

-            minNodeSize: 1,

-            maxNodeSize: 4,

+            minNodeSize: 0.5,

+            maxNodeSize: 3,

             minEdgeSize: 0.2,

             maxEdgeSize: 0.5,

-            eventsEnabled: true,

-            minRatio: 10, // How far can we zoom out?

-            maxRatio: 20, // How far can we zoom in?

+            eventsEnabled: false,

+            minRatio: 0, // How far can we zoom out?

+            maxRatio: 0, // How far can we zoom in?

             defaultLabelColor: "#000",

-            defaultLabelSize: 14,

+            defaultLabelSize: 15,

             defaultLabelBGColor: "#ddd",

             defaultHoverLabelBGColor: "#002147",

             defaultLabelHoverColor: "#fff",

-            labelThreshold: 10,

+            labelThreshold: 18,

             defaultEdgeType: "curve",

-            hoverFontStyle: "bold",

             fontStyle: "bold",

             activeFontStyle: "bold"

         }
         */

(function() {
    'use strict';

    if (typeof app === 'undefined' || app.HGraph === 'undefined')
        throw 'Error de dependencies';

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
            zoomMax: 200,
            eventsEnabled: true,
            labelThreshold: 25,
            defaultEdgeType: "curve",
            autoRescale:false, //TODO no va be, posar a true
            enableHovering:false //etiquetes: posades no funciona be

        },
        relativeSize:0.5,
        nooverlap:false

    };

    //For graph layout
    var _radius = 0.06;
    var _angle = 0;

    function _applySettings(s){
        if(typeof _settings.relativeSize !== 'undefined')
            sigma.plugins.relativeSize(s, _settings.relativeSize);
        //TODO adjust nooverlap
        if(typeof _settings.nooverlap !== 'undefined' && _settings.nooverlap)
        {
            // Configure the noverlap layout:
            var noverlapListener = s.configNoverlap({
                nodeMargin: 0.05,
                scaleNodes: 1,
                gridSize: 100,
                speed:10
            });
            // Bind the events:
            noverlapListener.bind('start stop interpolate', function(e) {
                console.log(e.type);
                if(e.type === 'start') {
                    console.time('noverlap');
                }
                if(e.type === 'stop') {
                    console.timeEnd('noverlap');

                }
            });
            s.startNoverlap();
        }

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

                    var pos = _getCircleRandomPos(i, i); //en els petits queda millor aixo
                    //var pos = _getNextPosition();

                    g.nodes.push({
                        id: String(nodes[type].get(i)[0])+type,
                        label: String(nodes[type].get(i)[1]),
                        x: pos.x,
                        y: pos.y
                    });
                }

            }
        }
        //TODO: peta molt wtf

        for (type in edges) {
            if (edges.hasOwnProperty(type))
            {
                for (var i = 0; i < edges[type].size(); i++)
                {
                    g.edges.push({
                        id: type+i,
                        source: String(edges[type].get(i)[0]+"paper"),
                        target: String(edges[type].get(i)[1])+type
                    })
                }
            }
        }

        return g;
    }

    //TODO
    function _getCircleRandomPos(radius, gap){
        var t = 2*Math.PI*Math.random();
        var u = Math.random()*(radius+10)+Math.random()*(radius+10)+gap;
        var r = u;
        if(u>1)
            r = 2-u;
        return {x: Math.cos(t)*r, y: Math.sin(t)*r};
    }

    function _getNextPosition() {
        var tol = 0.000001;
        var pos = {x: Math.cos(2*Math.PI*_angle)*_radius, y: Math.sin(Math.PI*2*_angle)*_radius};
        _angle = _angle + 0.01/(_radius); //inversament proporcional
        if (_angle > (1 - tol) || _angle < tol) {
            _angle = 0;
            _radius += 0.05; //valors que mes o menys van: 0.000001, 0.01, 0, 0.05
        }

        return pos;
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
    app.graph.drawNodesOnlyGraph = function(nodes){
        var g={nodes:[]};
        var i = 0;
        if(typeof _sarr === 'undefined')
            _sarr = [];
        var s;
        var pos_centre = {x:0, y:0}
        var pos;
        var colors = ["#FF0000", "#00FF00", "#0000FF"];
        //For each node
        var c = 0;
        while(i < nodes.size())
        {

            //TODO: rings
            //var pos = _getCircleRandomPos(i*40,i*20);
            pos = _getNextPosition();

            g.nodes.push({
                id: String(nodes.get(i)[0]),
                label: String(nodes.get(i)[1]),
                x: pos.x+pos_centre.x,
                y: pos.y+pos_centre.y,
                color: colors[c]
            });
            //If graph is % maxNodes, start a new one
            if(i % app.settings.maxNodes == 0)
            {
                c = (c+1) %3;
                s=new sigma({
                    container: 'graph-container',
                    graph:g,
                    settings:_settings.graph
                });


                _applySettings(s);
                _sarr.push(s);
                g = {nodes:[]};
            }
            i++;
        }
        //If nodes remaining, start a new graph
        if(g.nodes.length > 0)
        {
            s=new sigma({
                container: 'graph-container',
                graph:g,
                settings:_settings.graph
            });

            _applySettings(s);


            _sarr.push(s);
        }

        s = new sigma({
            container: 'graph-container',
            graph:{nodes:[]}
        });

        s.camera.bind('coordinatesUpdated', app.debounce(function(){
            _sarr.forEach(function(si){
                si.camera.x = s.camera.x;
                si.camera.y = s.camera.y;
                si.camera.ratio = s.camera.ratio;
                si.refresh();
            })
        }, 5));

    };
    //Draws a bigraph representing a table. Each connected component must be < maxNodes
    app.graph.drawTableBasedGraph = function(nodes, edges, cb){
        //TODO
    };

    //Draws a normal graph, nodes = {author:JavaArrayList, paper:...}
    app.graph.drawGraph = function(nodes, edges){
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
                batchEdgesDrawing: false
            }
        });
        _sarr[0].refresh();
        _applySettings(_sarr[0]);
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
