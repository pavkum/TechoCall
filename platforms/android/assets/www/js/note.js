var note = (function (){

    var elem = $('#workarea');
    
    var remainder = undefined;
    
    var user = {};
    
    var loadTemplate = function (def) {
        var promise = moduleLoader.loadModule('note');
        
        promise.done(function (data){
            elem.html(data);
            def.resolve();
        });
    };
    
    var showNote = function (){
        var width = elem.width();
        var height = elem.height();

        //$('#note').height(height*0.4);
    };
    
    var keyboardFix = function () {
        var button = elem.find('.control > button');
        
        var buttonHeight = button.height();
        
        button.height(buttonHeight);
        button.css('line-height' , buttonHeight + 'px');
        
        var note = elem.find('#note');
        
        var noteHeight = note.height();
        
        note.height(noteHeight);
        
    };
    
    var updateSideBar = function (mode) {
        var template = $('<div></div>');

        var upperStack = [];
        
        if(mode === 'new') {
            
            var saveRemainder = template.clone().text('Sa').css('background-color' , '#16a085');
            upperStack = [saveRemainder];
            
        }else if(mode === 'edit'){
            
            var editRemainder = template.clone().text('Ed').css('background-color' , '#16a085');
            var deleteRemainder = template.clone().text('De').css('background-color' , '#D91E18');
            
            upperStack = [editRemainder , deleteRemainder];
            
        }else{ // update mode
            
            var updateRemainder = template.clone().text('Up').css('background-color' , '#16a085');
            var deleteRemainder = template.clone().text('De').css('background-color' , '#D91E18');
            
            upperStack = [updateRemainder , deleteRemainder];
            
        }
               
        $('body').trigger('updateTopStack' , [upperStack]);  
    };
    
    $('body').on('note' ,  function (event , userObj){
        
        $('body').trigger('addToHistory',['showRemainders' , [userObj]]);
        
        user = JSON.parse(userObj);
        remainder = user.remainder;
        
        var def = new $.Deferred();
        loadTemplate(def);
        
        def.done(function (){
            //showNote();
            
            keyboardFix();
            
            if(user.readOnly) {
                
                updateSideBar('edit');
                
                $('#delete').show();
                
                $('#edit').show();
                
                $('#saveOrUpdate').hide();
                $('#cancel').hide();
                
                $('#note').attr('readonly' , true);
                
                $('#note').val(remainder.remainderMessage);
            
            }else{
                updateSideBar('new');
                
                $('#delete').hide();
                
                $('#edit').hide();
                
                $('#note').attr('readonly' , false);
                
                $('#saveOrUpdate').show();
                $('#cancel').show();
                
                $('#note').focus();
                
            }
        });
    });
    
    var saveSuccess = function (data) {
        
        $('body').trigger('showRemainders' , [JSON.stringify(user)]);
    };
    
    var saveError = function (error) {
        $('#error').text('Error creating...').show();
    };
    
    var updateSuccess = function (data) {
        
        $('body').trigger('showRemainders' , [JSON.stringify(user)]);
    };
    
    var updateError = function (error) {
        
        $('#error').text('Error updating...').show();
    };
    
    var deleteSuccess = function (data) {
        $('body').trigger('showRemainders' , [JSON.stringify(user)]);
    };
    
    var deleteError = function () {
        $('#error').text('Error deleting...').show();
    };
    
    $('body').on(configuartion.events.userselect, '#saveOrUpdate' , function (){
        var content = $('#note').val();
        
        if(user.readOnly) {
            
            remainderTemp = {};
            remainderTemp.remainderId = remainder.remainderId;
            remainderTemp.contactId = user.id;
            
            //remainderTemp.remainderType = remainder.remainderType;
            
            remainderTemp.remainderMessage = content;
            
            remainderTemp.isRemainded = remainder.isRemainded;
            remainderTemp.remaindedOn = remainder.remaindedOn;
            remainderTemp.remaindedUsing = remainder.remaindedUsing;
            
            techoStorage.updateRemainder(updateSuccess , updateError , [remainderTemp]);
            
        }else{
            
            remainder = {};
        
            remainder.remainderId = new Date().getTime();
            remainder.contactId = user.id;    
            //only call or only message or both - 0 : all, 1 - only call, 2 - only message
            remainder.remainderType = 0;
            
            remainder.isRemainded = false;
            remainder.remaindedOn = 0;
            remainder.remaindedUsing = -1;
        
            remainder.remainderMessage = content;
            
            techoStorage.addRemainder(saveSuccess , saveError , [remainder]);
            
        }
        
    });
    
    $('body').on(configuartion.events.userselect, '#cancel' , function (){
        if(user.readOnly) {

            updateSideBar('edit');
            
            $('#edit').show();
                
            $('#saveOrUpdate').hide();
            $('#cancel').hide();
            
            $('#note').attr('readonly' , true);
            
        }else{
            updateSideBar('new');
            
            $('body').trigger('showRemainders' , [user]);

        }
    });
    
    $('body').on(configuartion.events.userselect, '#edit' , function (){
        
        updateSideBar('update');
        
        $('#edit').hide();
                
        $('#saveOrUpdate').text('Update').show();
        $('#cancel').show();
        
        $('#note').attr('readonly' , false);
        
        $('#note').focus();
        
    });
    
    $('body').on(configuartion.events.userselect, '#delete' , function (){
        
        var remainderIds = {};
        remainderIds.remainderIds = [remainder.remainderId];
        
        techoStorage.deleteRemainder(deleteSuccess , deleteError , [remainderIds]);
    });
    
})();