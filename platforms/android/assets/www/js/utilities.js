var configuartion = {
  
    events : {
        userselect : 'click',
    },
    
    deviceProps : {
        
        getHeight : function () {
               
        }
        
    },
    
};

var flatUIColors = {
    
    count : 0,
    
    colors : ['#BF55EC','#F22613','#E87E04','#95A5A6','#1BBC9B','#19B5FE'],
    
    getNextColors : function () {
        return this.colors[this.count++ % 6];
    },
};

var moduleLoader = {
    
    loadModule : function (moduleName) {
        
        var def = new $.Deferred();
        
        var ajax = $.ajax({
            url : 'templates/' + moduleName + '.html',
            method : 'GET'
        }).done(function (resp) {
            def.resolve(resp);
        }).fail(function () {
            def.resolve(-1);
        });
        
        return def.promise();
    },
};
