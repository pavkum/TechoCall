var showAndRemainders = (function (){
    var elem = $('#workarea');
    
    var user = {};
    
    var template = $("<div class='remainderItem'> <div class='remainderMessagePreview'> </div>" +
                    "<div class='remainderStatus'> <div class='statusIcon'> </div>  </div> " +
                    "<div style='clear:both'> </div>" + 
                    "</div>");
    
    var loadTemplate = function (def ) {
        //if(elem.length === 0) elem = $('#content');
        elem = $('#workarea');
        var promise = moduleLoader.loadModule('showRemainders');
        
        promise.done(function (data){
            elem.html(data);
            def.resolve();
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
    
    var loadRemaindersError = function (error){
        
        $('#error').show();  
    };
    
    var loadRemainders = function (contactID) {
        
        techoStorage.getAllRemaindersById(loadRemaindersSuccess, loadRemaindersError, [contactID]);
    };
    
    var deleteSuccess = function (remainderIds) {
        for(var i=0; i<remainderIds.length; i++){
            $('#' + remainderIds[i]).remove();
        }
        notification('Delete successfull'); 
    };
    
    var deleteError = function () {
        notification('An error occured while deleting remainders'); 
    };
    
    var updateSidebar = function () {
        
        var template = $('<div><img height="70%"  style="position:relative;top:15%"/></div>');
        
        var addNewRemainder = template.clone().find('img').attr('src' , 'img/notepad.png');
        addNewRemainder.on(configuartion.events.userselect , function (){
            $('#dummyInput').trigger(configuartion.events.userselect);
        });
        
        var showUserInfo = template.clone().find('img').attr('src' , 'img/userinfo.png');
        showUserInfo.on(configuartion.events.userselect , function (){
            $('body').trigger('toUserInfo' , [user]);
        });
        
        var deleteSelectedRemainder = template.clone().find('img').attr('src' , 'img/trash.png');
        deleteSelectedRemainder.on(configuartion.events.userselect , function (){
            
            var selectedElements = $('.remainderItem[selected=selected]');
            
            if(selectedElements.length === 0){
                notification('No elements selected to delete'); 
                return;
            }
            
            var remainderIds = {};
            remainderIds.remainderIds = [];
            
            for(var i=0; i<selectedElements.length; i++){
                remainderIds.remainderIds.push(selectedElements.eq(i).attr('id'));
            }
            
            techoStorage.deleteRemainder(deleteSuccess , deleteError , [remainderIds]);
        });
        
        var upperStack = [addNewRemainder , showUserInfo , deleteSelectedRemainder];
                
        $('body').trigger('updateTopStack' , [upperStack]);
        
        
    };
    
    
    $('body').on('showRemainders',function (event , registeredUser){
        
        var def = new $.Deferred();
        
        user = JSON.parse(registeredUser);
        
        $('body').trigger('headerMiddle' , [user.name]);
        $('body').trigger('headerRight' , ['<img src="' + user.photo+ '" height="100%" >']);
        
        updateSidebar();
        
        loadTemplate(def);
        
        def.done(function (){
            
            loadRemainders(user.id);
            
        });
        
    });
    
    // events
    
     $('body').on(configuartion.events.userselect , '.remainderItem' , function (event ){
         
         // original user obj
         $('body').trigger('addToHistory',['showRemainders' [JSON.stringify(user)]]);
            
         //$('#list').hide();
         //$('#text').show();
         
         var target = $(event.currentTarget);
         
         var remainder = JSON.parse( target.data('remainder') );
         
         user.readOnly = true;
         user.remainder = remainder;
         
         if(target.find('.statusIcon').hasClass('done')){
            $('body').trigger('readRemainder', [JSON.stringify(user)]);
         }else{
            $('body').trigger('note', [JSON.stringify(user)]);    
         }
         
         
            
         //$(this).off(event);
        
    });
    
    $('body').on(configuartion.events.userselect , '#dummyInput' , function (event ){
        $('body').trigger('addToHistory',['showRemainders' [JSON.stringify(user)]]);
         
        user.readOnly = false;
         
        $('body').trigger('note', [JSON.stringify(user)]);
            
        //$(this).off(event);
        
    });
    
    $('body').on('toUserInfo' , function (){
            
        $('body').trigger('addToHistory',['showRemainders' [JSON.stringify(user)]]);
            
        $('body').trigger('userInfo' , [JSON.stringify(user)]);
    });
    
    $('body').on('taphold' , '.remainderItem' , function (event){
        
        var target = event.currentTarget;
        target = $(target);
            
        var selected = target.attr('selected');
        
        if(selected){
            target.removeAttr('selected');
            target.removeClass('selected');  
        }else{
            target.attr('selected' , 'selected');
            target.addClass('selected');
        }
        
        var selectedElements = $('.remainderItem[selected=selected]');
        
        console.log(target[0].outerHTML);
        
        if(selectedElements.length === 0){
            $('body').trigger('hideSidebar'); 
        }else{
            $('body').trigger('showSidebar');  
        }
        
    });
    
    
    var registerEvents = function (contactID , displayName , photo , phoneNumber) {
        $('body').on('showNote',function (event , contactID , displayName , photo , isNew , remainderObj){ // proxy to be called from other areas as per screen so that required info can be passed
        
            $('#list').hide();
            $('#text').show();
            
            $('body').trigger('note', [contactID , displayName , photo , isNew , remainderObj]);
            
            $(this).off(event);
            
        });
        
        $('#dummyInput').on(configuartion.events.userselect , function (){
            
            $('body').trigger('addToHistory',['showRemainders' [contactID , displayName , photo , phoneNumber]]);
            
            $('body').trigger('showNote' , [contactID , displayName , photo , true]);
        });
        
        $('.remainderItem').on(configuartion.events.userselect , function (event){
            var remainder = JSON.parse( $(event.currentTarget).data('remainder') );
            
            //$('body').trigger('addToHistory',['showRemainders' [contactID , displayName , photo , phoneNumber]]);
            
            //$('body').trigger('showNote' , [contactID , displayName , photo , false , remainder]);
        });
        
        $('body').on('toUserInfo' , function (){
            
            $('body').trigger('addToHistory',['showRemainders' [contactID , displayName , photo , phoneNumber]]);
            
            $('body').trigger('userInfo' , [contactID , displayName , photo , phoneNumber]);
        });
        
        $('.remainderItem').on('taphold' ,  function (event){
            var target = event.currentTarget;
            
            
            var selected = target.attr('selected');
            
            if(selected && selected == '1'){
                target.attr('selected' , 0);

            }else{
                
                target.attr('selected' , 1);
                
                target.css('background' , '#C5EFF7'); 
            }
            
            event.preventDefault();
            event.stopPropagation();
            
            return false;
        });
        
    };
    
    
    
})();