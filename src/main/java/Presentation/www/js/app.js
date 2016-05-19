document.addEventListener("DOMContentLoaded", function(){
    checkDev();
    init();
});

function checkDev(){
    if(window.HGraph === undefined)
    {
        window.HGraph = {
            getProjects: function(){
                return ["stub1", "stub2", "sutb3"]
            },
            getNodesOfType: function(type){
                if(type == "paper")
                {
                    return [
                        ["0", "patata"],
                        ["1", "zanahoria"],
                        ["2", "berenjena"],
                        ["3", "tomate"]
                    ];
                }
            }
        };
    }
}

function init(){
    document.querySelector("#welcomePage .buttons a[data-action=newGraph]").addEventListener("click", newGraph);
    document.querySelector("#welcomePage .buttons a[data-action=loadGraph]").addEventListener("click", loadGraph);
    document.querySelector("#loadGraphPage a[data-action=welcome]").addEventListener("click", loadGoToWelcome);
    document.querySelector("#mainPage a[data-action=welcome]").addEventListener("click", mainGoToWelcome);

    setTimeout(function(){
        show("#welcomePage");
    }, 500);
}


function hide(selector, cb){
    document.querySelector(selector).classList.remove("show");
    setTimeout(function(){
        document.querySelector(selector).classList.remove("active");
        if(cb) cb();
    }, 200);

}

function show(selector){
    document.querySelector(selector).classList.add("active");
    setTimeout(function(){
        document.querySelector(selector).classList.add("show");
    }, 1);
}

function loadGoToWelcome(){
    hide("#loadGraphPage", function(){
        show("#welcomePage");
    });
}
function mainGoToWelcome(){
    hide("#mainPage", function(){
        show("#welcomePage");
    });
}
function loadGoToMain() {
    hide("#loadGraphPage", function(){
            show("#mainPage");
    });
}
function newGraph(){
    show("#loading");
    hide("#welcomePage", function(){
        show("#mainPage");
        drawGraph(function(){
            hide("#loading");
        });
    });

}

function loadGraph(){
    hide("#welcomePage", function(){
        initLoadPage();
        show("#loadGraphPage");
    });

}

function initLoadPage(){
    var myList = document.getElementById('projectList');
    //myList.innerHTML = '';
    var projects = window.HGraph.getProjects();
    projects.forEach(function(e){
        var child = document.createElement("li");
        child.innerHTML = e;
        child.addEventListener("click", function() {
            window.HGraph.loadProject(e);
            loadGoToMain();
        });
        //TODO que vagi a la pagina correcta
        myList.appendChild(child);
    });
}

function drawGraph(cb){
    var g = {
            nodes: [],
            edges: []
        };
    alert("asdf");
    var nodes = window.HGraph.getNodesOfType("paper");
    alert(nodes);
    nodes.forEach(function(e){
        g.nodes.push({
            id: e[0],
            label: e[1],
            x: Math.random()/10,
            y: Math.random()/10,
            size: Math.random(),
            color: "red"
        });
    });

    s = new sigma({
        graph: g,
        container: 'graph-container',
        settings: {
            minNodeSize: 4,
            maxNodeSize: 2,
            minEdgeSize: 1,
            maxEdgeSize: 1,
            eventsEnabled: false
        }
    });
    cb();
}


function drawGraphDebug(cb){
    var i,
        s,
        o,
        N = 2500,
        E = 100,
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
        g.nodes.push({
            id: 'n' + i,
            label: 'Node' + i,
            x: Math.random()*100,
            y: Math.random()*100,
            size: Math.random(),
            color: o.color
        });
        o.nodes.push('n' + i);
        if(i%1024) console.log("+1024");
    }

    for (i = 0; i < E; i++) {
        if (Math.random() < 1 - d)
            g.edges.push({
                id: 'e' + i,
                source: 'n' + ((Math.random() * N) | 0),
                target: 'n' + ((Math.random() * N) | 0)
            });
        else {
            o = cs[(Math.random() * C) | 0]
            g.edges.push({
                id: 'e' + i,
                source: o.nodes[(Math.random() * o.nodes.length) | 0],
                target: o.nodes[(Math.random() * o.nodes.length) | 0]
            });
        }
    }

    s = new sigma({
        graph: g,
        container: 'graph-container',
        settings: {
            minNodeSize: 4,
            maxNodeSize: 2,
            minEdgeSize: 1,
            maxEdgeSize: 1,
            eventsEnabled: false
        }
    });
    cb();
/*
// Configure the noverlap layout:
    var noverlapListener = s.configNoverlap({
        nodeMargin: 0.2,
        scaleNodes: 1.05,
        gridSize: 200,
        easing: 'quadraticInOut', // animation transition function
        duration: 1000   // animation duration. Long here for the purposes of this example only
    });
// Bind the events:
    noverlapListener.bind('start stop interpolate', function(e) {
        console.log(e.type);
        if(e.type === 'start') {
            console.time('noverlap');
        }
        if(e.type === 'interpolate') {
            console.timeEnd('noverlap');
        }
    });
// Start the layout:
//s.startNoverlap();
*/
}
