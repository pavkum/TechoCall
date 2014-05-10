var registeredContacts = (function (){
    
    var elem = $('#workarea');

    var template = $('<div class="contact"> ' +
                    '<div class="image"> <img > </img> </div> ' +
                    '<div class="name"> </div> ' +
                    '<div style="clear:both"> </div> ' +
                   '</div>');
    
    var loadTemplate = function (def) {
        var promise = moduleLoader.loadModule('registeredContacts');
        
        promise.done(function (data){
            elem.html('');
            elem.html(data);
            elem.hide();
            def.resolve();
        });
        
        return def.promise();
    };
    
    var loadContactInfoSuccess = function (data){
        
        if(data.contacts.length === 0) {
            $('#nodata').show();
            return;
        }
        
        var wrapper = elem.find('.wrapper');
        
        for(var i=0; i<data.contacts.length; i++){
            
            var clone = template.clone();
            
            var contact = data.contacts[i];
            
            clone.attr('id',contact.id);
            
            var name = clone.find('.name');
            name.text(contact.displayName);
            
            var photo = clone.find('img');
            photo.attr('src',contact.photo);
            
            clone.data('phoneNumber' , contact.phoneNumber);
            
            wrapper.append(clone);
            
            
        }
        
    };
    
    var loadContactInfoError = function (error){
        Logger.error('contacts',error);
    };
    
    var loadContactInfo = function () {
        techoStorage.getAllContacts(loadContactInfoSuccess,loadContactInfoError,[]);
    };

    
    var updateSidebar = function () {
        
        var template = $('<div></div>');

        var addNewContact , deleteSelectedContacts;
        
        addNewContact = template.clone().text('A').css('background-color' , '#16a085');
        addNewContact.on(configuartion.events.userselect , function (event){
            $('body').trigger('getAllContacts');
        });
        
        deleteSelectedContacts = template.clone().text('D').css('background-color' , '#D91E18');
        deleteSelectedContacts.on(configuartion.events.userselect , function (event){
            // not yet implemented
        });
/*
    
        var triggerEvent = function (eventName){
            addNewContact.unbind(configuartion.events.userselect);
            deleteSelectedContacts.unbind(configuartion.events.userselect);
            
            $('body').trigger(eventName);
            
        };
*/
        
        var settings = template.clone().text('S').css('background-color' , '#1E8BC3');
        var quit = template.clone().text('Q').css('background-color' , '#F22613');
        
        var upperStack = [addNewContact , deleteSelectedContacts];
        var bottomStack = [settings , quit];
        
        $('body').trigger('updateTopStack' , [upperStack]);
        $('body').trigger('updateBottomStack' , [bottomStack]);
    };
    
    var attachEventHandlers = function () {
        
        $('body').on(configuartion.events.userselect , '.contact' , function (event){
            
            $(this).off(event);
            
            var target = $(event.currentTarget);
            
            var id = target.attr('id');
            var name = target.find('.name').text();
            
            var photo = target.find('img').attr('src');
            
            var phoneNumber = target.data('phoneNumber');
            
            //$('body').trigger('showUser' , [id,name , photo]);
            
            $('body').trigger('showRemainders', [id , name , photo , phoneNumber]);
        });
    };
    
    $('body').on('showTechoContacts',function (){
        
        $('body').trigger('headerMiddle',['Next Time']);
        $('body').trigger('headerRight',['+','getAllContacts']);// local event
        
        updateSidebar();
        
        var def = new $.Deferred();
        loadTemplate(def);
        def.done(function (){
            
            elem.show();
            loadContactInfo();
            attachEventHandlers();
            
        });
        
    });
    
});

$('body').on('RegisteredContactsReady',function(event){
    registeredContacts();
    
    $('body').trigger('showTechoContacts');
    
    $( this ).off( event ); // similar jquery one event
});