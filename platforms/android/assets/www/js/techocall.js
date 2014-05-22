var techocall = (function (){

    var historyList = [];
    
    var headerHeight = 0;
    
    var initialize = function () {
        
        var width = $(window).width();
        var height = $(window).height();
        
        var displayHeight = $('header').outerHeight();
        
        headerHeight = $('header').height();
        
        var workarea = $('#workarea');
        
        workarea.outerHeight(height - displayHeight);
    };
    
    $('body').on('addToHistory' , function (event, eventName, eventArgs){
        var historyObj = {};
        historyObj.eventName = eventName;
        historyObj.eventArgs = eventArgs ? eventArgs : [];
                
        historyList.push(historyObj);
        
        $('body').trigger('showBackButton');
        
        console.log("addToHistory" + historyObj);
    });
    
    $('body').on('triggerHistory' , function (){
        
        if(historyList.length > 0){
            var historyObj = historyList.pop();
            console.log("triggerHistory" + historyObj);
            $('body').trigger(historyObj.eventName , historyObj.eventArgs);    
            
            if(historyList.length === 0) {
                $('body').trigger('hideBackButton');   
            }
        }
    });
    
    $('body').on('clearHistory' , function (){
        historyList = [];
        $('body').trigger('hideBackButton'); 
    });
    
    
    $(document).on('backbutton' , function (event){
        
        if(historyList.length != 0) {
            $('body').trigger('triggerHistory');    
            event.stopPropagation();
        }else{
            navigator.app.exitApp();   
        }
    });
    
    initialize();
    
    $(window).on('resize' , function (event){
        event.preventDefault();
        event.stopImmediatePropagation();
        $('header').height(headerHeight);
        return false;
    });
});
//$(document).ready(function (){
$(document).on('deviceready',function (){
    
    FastClick.attach(document.body);
    
    //Logger.off();
    
    techocall();
    // load all at once - no AMD
    $('body').trigger('headerReady');
    $('body').trigger('RegisteredContactsReady');
    //$('body').trigger('RegisteredUserReady');
    $('body').trigger('ContactsReady');
    
    $('body').trigger('initializeSidebar');
    
    //alert(window.devicePixelRatio + " : " + window.height + " : " + $('header').height());
});