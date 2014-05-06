(function (){
    var elem = $('#sidebar');
    
    var topStack = elem.find('.topStack');
    var bottomStack = elem.find('.bottomStack');
    
    var template = $('<div class="sidebarElem"></div>');
    
    $('body').on('swiperight' , function (){
        showSidebar();
    });
    
    $('body').on('swipeleft' , function (){
        hideSidebar();
    });

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
        topStack.empty();
        
        for(var i=0; i<topStackImages.length; i++){
            var clone = template.clone();
            clone.html(topStackImages[i]);
            
            topStack.append(clone);
            
            
        }
        
    });
    
    $('body').on('updateBottomStack' , function (event , bottomStackImages){
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
        stack.height(elemHeight - headerHeight);
        $(this).off(event);
    });
    
})();