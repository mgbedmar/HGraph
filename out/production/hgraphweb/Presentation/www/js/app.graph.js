(function() {
    'use strict';
    
    if (typeof app === 'undefined' || app.HGraph === 'undefined')
        throw 'Error de dependenciaes';

    //Private
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


    app.graph.drawGraph = function(cb){
        var g = {
            nodes: [],
            edges: []
        };

        var nodes = app.HGraph.getNodesOfType("paper");

        //TODO
        for (var i = 0; i < Math.sqrt(nodes.size()); i++) {
            var pos = _getCircleRandomPos();

            g.nodes.push({
                id: String(nodes.get(i)[0]),
                label: String(nodes.get(i)[1]),
                x: pos.x,
                y: pos.y
            });
        }

        //TODO: zoom, size, threshold
        var s = new sigma({
            graph: g,
            container: 'graph-container',
            settings: {

                minNodeSize: 1,
                minEdgeSize: 0.2,
                maxEdgeSize: 0.5,
                zoomMin: 0.01,
                zoomMax: 0.5,
                eventsEnabled: true,
                labelThreshold: 25

            }
        });
        //TODO
        sigma.plugins.relativeSize(s, 0.5);
        cb();
    };
}).call(window);




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
        var pos = getCircleRandomPos();
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