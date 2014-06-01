(function (){
    var elem = $('#sidebar');
    
    var topStack = elem.find('.topStack');
    var bottomStack = elem.find('.bottomStack');
    
    var template = $('<div class="sidebarElem"></div>');
    
    var minimumSwipeLength = 10;
    
    var touchHandler = {};
    touchHandler.startX = undefined;
    touchHandler.curX = undefined;
    touchHandler.timer = undefined;
    
    $('body').on(configuartion.events.userselect ,  '#sidebar' , function (){
        hideSidebar();
    });
    
    $('body').on('touchmove' , function (event){
            var x = event.originalEvent.changedTouches[0].clientX;
            //var x = event.clientX;
            touchHandler.curX = x;
            
            if( touchHandler.timer){
                clearTimeout(touchHandler.timer);
                touchHandler.timer = undefined;
            }
            
            if(!touchHandler.startX){
                touchHandler.startX = x;
            }
            
            touchHandler.timer = setTimeout(function(){
                touchHandler.curX = undefined;
                touchHandler.startX = undefined;
                touchHandler.timer = undefined;
            },1000);
            
            if(touchHandler.curX && touchHandler.startX){
            
                var diff = touchHandler.curX - touchHandler.startX;
                
                if(diff > 0 && diff >= minimumSwipeLength){
                    showSidebar();
                }else if(diff < 0 && (-1 * diff) >= minimumSwipeLength ){
                    hideSidebar();
                }else{
                    event.preventDefault();        
                }
                
            }else{
                event.preventDefault();   
            }
        
            //event.preventDefault();
    });
    
    
    /*$('body').on('swiperight' , function (){
        showSidebar();
    });
    
    $('body').on('swipeleft' , function (){
        hideSidebar();
    });*/

    var showSidebar = function () {
        elem.show('fast');
    };
    
    var hideSidebar = function () {
        elem.hide('fast');
    };
    
    $('body').on('showSidebar' , function (){
        showSidebar();
    });
    
    $('body').on('hideSidebar' , function (){
        hideSidebar();
    });

    $('body').on('updateTopStack' , function (event , topStackImages){
        
        var children = topStack.children();
        
        for(var i=0; i<children.length; i++){
            var child = children.eq(i);
            child.unbind(configuartion.events.userselect);
        }
        
        topStack.empty();
        
        for(var i=0; i<topStackImages.length; i++){
            var clone = template.clone();
            clone.html(topStackImages[i]);
            
            topStack.append(clone);
        }
        
    });
    
    $('body').on('updateBottomStack' , function (event , bottomStackImages){
        
        var children = bottomStack.children();
        
        for(var i=0; i<children.length; i++){
            var child = children.eq(i);
            child.unbind(configuartion.events.userselect);
        }
        
        bottomStack.empty();
        
        for(var i=0; i<bottomStackImages.length; i++){
            var clone = template.clone();
            clone.html(bottomStackImages[i]);
            
            bottomStack.append(clone);
        }
    });
    
    $('body').on('initializeSidebar' , function (event){
        
        var elemHeight = elem.height();
        
        var headerHeight = $('header').height();
        var stack = elem.find('.stack');
        //alert(elemHeight + ":" + headerHeight);
        stack.height(elemHeight - headerHeight);
        $(this).off(event);
    });
    
    
    
})();