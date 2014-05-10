var note = (function (){

    var elem = $('#workarea');
    var contactId = undefined;
    var isNew = false;
    var remainder = undefined;
    
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
    
    $('body').on('note' ,  function (event , id  , displayName , photo , createMode , remainderObj){
        if(elem.length === 0)
            elem = $('#workarea');
        
        $('body').trigger('addToHistory',['showRemainders' , [id , displayName , photo]]);
        
        contactId = id;
        isNew = createMode;
        remainder = remainderObj;
        
        
        
        var def = new $.Deferred();
        loadTemplate(def);
        
        def.done(function (){
            //showNote();
            
            if(isNew) {
                
                updateSideBar('new');
                
                $('#delete').hide();
                
                $('#edit').hide();
                
                $('#saveOrUpdate').show();
                $('#cancel').show();
            
            }else{
                
                updateSideBar('edit');
                
                $('#delete').show();
                
                $('#edit').show();
                
                $('#saveOrUpdate').hide();
                $('#cancel').hide();
                
                $('#note').attr('readonly' , true);
                
                $('#note').val(remainder.remainderMessage);
            }
        });
    });
    
    var saveSuccess = function (data) {
        
        $('body').trigger('showRemainders' , [contactId]);
    };
    
    var saveError = function (error) {
        $('#error').text('Error creating...').show();
    };
    
    var updateSuccess = function (data) {
        
        $('body').trigger('showRemainders' , [contactId]);
    };
    
    var updateError = function (error) {
        
        $('#error').text('Error updating...').show();
    };
    
    var deleteSuccess = function (data) {
        $('body').trigger('showRemainders' , [contactId]);
    };
    
    var deleteError = function () {
        $('#error').text('Error deleting...').show();
    };
    
    $('body').on(configuartion.events.userselect, '#saveOrUpdate' , function (){
        var content = $('#note').val();
        
        if(isNew) {
            remainder = {};
        
            remainder.remainderId = new Date().getTime();
            remainder.contactId = contactId;    
            //only call or only message or both - 0 : all, 1 - only call, 2 - only message
            remainder.remainderType = 0;
        
            remainder.remainderMessage = content;
            
            techoStorage.addRemainder(saveSuccess , saveError , [remainder]);
            
        }else{
            
            remainderTemp = {};
            remainderTemp.remainderId = remainder.remainderId;
            remainderTemp.contactId = remainder.contactId;
            
            //remainderTemp.remainderType = remainder.remainderType;
            
            remainderTemp.remainderMessage = content;
            
            techoStorage.updateRemainder(updateSuccess , updateError , [remainderTemp]);
        }
        
    });
    
    $('body').on(configuartion.events.userselect, '#cancel' , function (){
        if(isNew) {
            
            updateSideBar('new');
            
            $('body').trigger('showRemainders' , [contactId]);
        }else{
            
            updateSideBar('edit');
            
            $('#edit').show();
                
            $('#saveOrUpdate').hide();
            $('#cancel').hide();
            
            $('#note').attr('readonly' , true);
        }
    });
    
    $('body').on(configuartion.events.userselect, '#edit' , function (){
        
        updateSideBar('update');
        
        $('#edit').hide();
                
        $('#saveOrUpdate').text('Update').show();
        $('#cancel').show();
        
        $('#note').attr('readonly' , false);
        
        
    });
    
    $('body').on(configuartion.events.userselect, '#delete' , function (){
        var remainderIds = {};
        remainderIds.remainderId = remainder.remainderId;
        techoStorage.deleteRemainder(deleteSuccess , deleteError , [remainderIds]);
    });
    
})();