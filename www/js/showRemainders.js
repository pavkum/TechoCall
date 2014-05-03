var showAndRemainders = (function (){
    var elem = $('#content');
    
    var template = $("<div class='remainderItem'> <div class='remainderMessagePreview'> </div>" +
                    "<div class='remainderStatus'> <div class='statusIcon'> </div>  </div> " +
                    "<div style='clear:both'> </div>" + 
                    "</div>");
    
    var loadTemplate = function (def , contactID) {
        //if(elem.length === 0) elem = $('#content');
        elem = $('#content');
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
    
    var loadRemaindersError = function (error){
        
        $('#error').show();  
    };
    
    var loadRemainders = function (contactID) {
        
        techoStorage.getAllRemaindersById(loadRemaindersSuccess, loadRemaindersError, [contactID]);
    };
    
    $('body').on('showRemainders',function (event,contactID){
        var def = new $.Deferred();
        loadTemplate(def , contactID);
        
        def.done(function (contactID){
            loadRemainders(contactID);
            registerEvents(contactID);
            //$('body').trigger('showNote'); // testing
        });
        
    });
    
    var registerEvents = function (contactID) {
        $('body').on('showNote',function (event , contactID , isNew , remainderObj){ // proxy to be called from other areas as per screen so that required info can be passed
        
            $('#list').hide();
            $('#text').show();
            
            $('body').trigger('note', [contactID , isNew , remainderObj]);
            
            $(this).off(event);
            
        });
        
        $('#dummyInput').on(configuartion.events.userselect , function (){
            $('body').trigger('showNote' , [contactID , true]);
        });
        
        $('.remainderItem').on(configuartion.events.userselect , function (event){
            var remainder = JSON.parse( $(event.currentTarget).data('remainder') );
            $('body').trigger('showNote' , [contactID , false , remainder]);
        });
    };
    
    
})();