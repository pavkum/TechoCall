var readRemainder = (function(){
    
    var elem = $('#workarea');
    
    var user = {};
    var remainder = undefined;
    
    var loadTemplate = function (def) {
        var promise = moduleLoader.loadModule('readRemainder');
        
        promise.done(function (data){
            elem.html(data);
            def.resolve();
        });
    };
    
    var updateSidebar = function (){
        var template = $('<div></div>');

        var upperStack = [];
        
        var update = template.clone().text('Up').css('background-color' , '#19B5FE');
        
        update.on(configuartion.events.userselect , function (){
            $('#remaindAgain').trigger(configuartion.events.userselect);
        });
        
        upperStack = [update];  
        $('body').trigger('updateTopStack' , [upperStack]);  
    };
    
    $('body').on('readRemainder' , function (event , userObj){
        var def = new $.Deferred ();
        
        $('body').trigger('addToHistory',['showRemainders' , [userObj]]);
        
        updateSidebar();
        
        console.log('userObj : ' + userObj);
        
        user = JSON.parse(userObj);
        remainder = user.remainder;
        
        loadTemplate(def);
        
        def.done(function (){
            
            console.log(remainder.remainderMessage);
            
            elem.find('#message').text(remainder.remainderMessage);
            
            var date = new Date(remainder.remaindedOn);
            
            var remaindedOn = date.getHours() + ' : ' + date.getMinutes() + ' : ' + date.getSeconds () + ' - ' + date.toDateString();
            
            elem.find('#when').text(remaindedOn);
            
            
            
            // 0 - incoming call, 1- outgoing call, 2 - incoming msg, 3 - outgoing message
            var how = elem.find('#how');
            if(remainder.remaindedUsing == 0){
                how.find('img').attr('src' , 'img/incoming.png');
                how.find('#text').text('Incoming Call');
            }else if(remainder.remaindedUsing == 1){
                how.find('img').attr('src' , 'img/outgoing.png');
                how.find('#text').text('Outgoing Call');
            }
        });
        
    });
    
    var updateSuccess = function () {
        $('body').trigger('showRemainders' , [JSON.stringify(user)]);  
    };
    
    var updateError = function (error) {
        console.log(error);
        notification('Error updating');  
    };
    
    $('body').on(configuartion.events.userselect , '#remaindAgain' , function (event){
        
        remainderTemp = {};
        remainderTemp.remainderId = remainder.remainderId;
        remainderTemp.remainderMessage = remainder.remainderMessage;
        
        remainderTemp.isRemainded = false;
        remainderTemp.remaindedOn = 0;
        remainderTemp.remaindedUsing = -1;
        
        
        techoStorage.updateRemainder(updateSuccess , updateError , [remainderTemp]);
        
    });
    
})();