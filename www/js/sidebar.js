(function (){
    var elem = $('#sidebar');
    
    var touchHandler = {};
    touchHandler.curX = undefined;
    touchHandler.prevX = undefined;
    touchHandler.timer = undefined;
    
    /*$('body').on('touchstart' , function(event){
        var x = event.originalEvent.changedTouches[0].clientX;
            //var x = event.clientX;
        touchHandler.prevX = touchHandler.curX;
        touchHandler.curX = x;
            
        if(touchHandler.timer){
            clearTimeout(touchHandler.timer);
            touchHandler.timer = undefined;
        }
            
        touchHandler.timer = setTimeout(function(){
            touchHandler.curX = undefined;
            touchHandler.prevX = undefined;
            touchHandler.timer = undefined;
        },1000);
            
        if(touchHandler.curX && touchHandler.prevX){
            
            if(touchHandler.curX >= touchHandler.prevX){//scroll right
                displayScrollRight();
            }else{
                displayScrollLeft();
            }
            updateScrollInfo();
        }
            
        event.preventDefault();
    }); */
    
    $('body').on('swiperight' , function (){
        showSidebar();
    });
    
    $('body').on('swipeleft' , function (){
        hideSidebar();
    });
    
    var showSidebar = function () {
        //alert(1);
        elem.show('slow');
        //alert(1);
        
    };
    
    var hideSidebar = function () {
        
        elem.hide('slow');
        //alert(0);
        
    };
    
    $('body').on('showSidebar' , function (){
        showSidebar();
    });
    
    $('body').on('hideSidebar' , function (){
        hideSidebar();
    });
    
})();