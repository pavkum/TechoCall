var header = (function (){
    
    var logo = $('#logo');
    
    var backbutton = $('#backbutton');
    
    var middleColumn = $('#middleColumn');
    
    var rightColumn = $('#rightColumn');
    
    var initialize = function () {
        //logo.html('C') // will be replaced by icon of size as per device density
        updateMiddleColumn('Next Time');
        updateRightColumn('+','getAllContacts');
        
        logo.on(configuartion.events.userselect,function (){
            $('body').trigger('triggerHistory');
        });
    };
    
    var updateMiddleColumn = function (html) {
        middleColumn.html(html);
    };
    
    var updateRightColumn = function (html,eventName) {
        rightColumn.html(html);
        
        rightColumn.off(); // remove attached events
        
        if(eventName){
            rightColumn.on(configuartion.events.userselect,function(){
                $('body').trigger(eventName);
            });
        }
    };
    
    initialize();
    
    $('body').on('showBackButton' , function (){
        backbutton.css('visibility','visible');
    });
    
    $('body').on('hideBackButton' , function (){
        backbutton.css('visibility','hidden');
    });
    
    $('body').on('headerMiddle',function (event,data){
        Logger.info('photo ',data);
        updateMiddleColumn(data);
    });
    
    $('body').on('headerRight',function (event,data,eventName){
        updateRightColumn(data,eventName);
        
    });
    
});

$('body').on('headerReady', function (event){
    header();
    $(this).off(event);
});