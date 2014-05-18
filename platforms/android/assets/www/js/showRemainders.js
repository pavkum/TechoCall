var showAndRemainders = (function (){
    var elem = $('#workarea');
    
    var template = $("<div class='remainderItem'> <div class='remainderMessagePreview'> </div>" +
                    "<div class='remainderStatus'> <div class='statusIcon'> </div>  </div> " +
                    "<div style='clear:both'> </div>" + 
                    "</div>");
    
    var loadTemplate = function (def , contactID) {
        //if(elem.length === 0) elem = $('#content');
        elem = $('#workarea');
        var promise = moduleLoader.loadModule('showRemainders');
        
        promise.done(function (data){
            elem.html(data);
            def.resolve(contactID);
        });
    };
    
    var loadRemaindersSuccess = function (data){
        
        if(data && data.userRemainders){
            
            
            if(data.userRemainders.length == 0){
                $('#nodata').show();
                return;
            }
            
            var remainderWrapper = $('.remainders').show();
            
            for(var i=0; i<data.userRemainders.length; i++){
                var remainder = data.userRemainders[i];
                var clone = template.clone();
                
                clone.data('remainder' , JSON.stringify(remainder));
                clone.attr('id' , remainder.remainderId);
                clone.find('.remainderMessagePreview').text(remainder.remainderMessage);
                
                if(remainder.isRemainded){
                    clone.find('.statusIcon').addClass('done').text('D');   
                }else{
                    clone.find('.statusIcon').addClass('pending').text('P');
                }
                
                remainderWrapper.append(clone);
                
            }
        }else{
            loadRemaindersError('Nothing to display');   
        }
    };
    
    var updateSidebar = function () {
        
        var template = $('<div><img height="70%"  style="position:relative;top:15%"/></div>');
        
        var addNewRemainder = template.clone().find('img').attr('src' , 'img/notepad.png');
        
        addNewRemainder.on(configuartion.events.userselect , function (){
            $('#dummyInput').trigger(configuartion.events.userselect);
        });
        
        var showUserInfo = template.clone().find('img').attr('src' , 'img/userinfo.png');
        showUserInfo.on(configuartion.events.userselect , function (){
            $('body').trigger('toUserInfo');
        });
        
        var deleteSelectedRemainder = template.clone().find('img').attr('src' , 'img/trash.png');
        
        var upperStack = [addNewRemainder , showUserInfo , deleteSelectedRemainder];
                
        $('body').trigger('updateTopStack' , [upperStack]);
        
        
    };
    
    var loadRemaindersError = function (error){
        
        $('#error').show();  
    };
    
    var loadRemainders = function (contactID) {
        
        techoStorage.getAllRemaindersById(loadRemaindersSuccess, loadRemaindersError, [contactID]);
    };
    
    $('body').on('showRemainders',function (event , contactID , displayName , photo , phoneNumber){
        var def = new $.Deferred();
        
        $('body').trigger('addToHistory',['showTechoContacts']);
        $('body').trigger('headerMiddle' , [displayName]);
        $('body').trigger('headerRight' , ['<img src="' + photo+ '" height="100%" >']);
        
        updateSidebar();
        
        loadTemplate(def , contactID);
        
        def.done(function (contactID){
            loadRemainders(contactID);
            registerEvents(contactID , displayName , photo , phoneNumber);
            //$('body').trigger('showNote'); // testing
        });
        
    });
    
    var registerEvents = function (contactID , displayName , photo , phoneNumber) {
        $('body').on('showNote',function (event , contactID , displayName , photo , isNew , remainderObj){ // proxy to be called from other areas as per screen so that required info can be passed
        
            $('#list').hide();
            $('#text').show();
            
            $('body').trigger('note', [contactID , displayName , photo , isNew , remainderObj]);
            
            $(this).off(event);
            
        });
        
        $('#dummyInput').on(configuartion.events.userselect , function (){
            $('body').trigger('showNote' , [contactID , displayName , photo , true]);
        });
        
        $('.remainderItem').on(configuartion.events.userselect , function (event){
            var remainder = JSON.parse( $(event.currentTarget).data('remainder') );
            $('body').trigger('showNote' , [contactID , displayName , photo , false , remainder]);
        });
        
        $('body').on('toUserInfo' , function (){
            $('body').trigger('userInfo' , [contactID , displayName , photo , phoneNumber]);
        });
    };
    
    
})();