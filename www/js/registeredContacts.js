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
        notification('Error loading contacts');
    };
    
    var loadContactInfo = function () {
        techoStorage.getAllContacts(loadContactInfoSuccess,loadContactInfoError,[]);
    };

    var deleteSuccess = function (contactIds) {
        for(var i=0; i<contactIds.length; i++){
            $('#' + contactIds[i]).remove();
        }
        notification('Delete successfull'); 
    };
    
    var deleteError = function (error) {
        notification('An error occured while deleting contacts : ' + error); 
    };
    
    var updateSidebar = function () {
        
        var template = $('<div><img height="70%"  style="position:relative;top:15%"/></div>');

        var addNewContact , deleteSelectedContacts;
        
        addNewContact = template.clone().find('img').attr('src' , 'img/contacts.png');
        addNewContact.on(configuartion.events.userselect , function (event){
            $('body').trigger('getAllContacts');
        });
        
        deleteSelectedContacts = template.clone().clone().find('img').attr('src' , 'img/trash.png');//.css('background-color' , '#D91E18');
        deleteSelectedContacts.on(configuartion.events.userselect , function (event){
            var selectedElements = $('.contact[selected=selected]');
            
            if(selectedElements.length === 0){
                notification('No elements selected to delete'); 
                return;
            }
            
            var contactIds = {};
            contactIds.contactIds = [];
            
            for(var i=0; i<selectedElements.length; i++){
                contactIds.contactIds.push(selectedElements.eq(i).attr('id'));
            }
            
            techoStorage.deleteContact(deleteSuccess , deleteError , [contactIds]);
        });
/*
    
        var triggerEvent = function (eventName){
            addNewContact.unbind(configuartion.events.userselect);
            deleteSelectedContacts.unbind(configuartion.events.userselect);
            
            $('body').trigger(eventName);
            
        };
*/
        
        var settings = template.clone().find('img').attr('src' , 'img/settings.png');//.css('background-color' , '#1E8BC3');
        settings.on(configuartion.events.userselect , function (){
            $('body').trigger('settings');
        });
        
        var quit = template.clone().find('img').attr('src' , 'img/shutdown.png');//.css('background-color' , '#F22613');
        quit.on(configuartion.events.userselect , function (){
            navigator.app.exitApp();
        });
        
        var upperStack = [addNewContact , deleteSelectedContacts];
        var bottomStack = [settings , quit];
        
        $('body').trigger('updateTopStack' , [upperStack]);
        $('body').trigger('updateBottomStack' , [bottomStack]);
    };
    
    var attachEventHandlers = function () {
        
        $('body').on(configuartion.events.userselect , '.contact' , function (event){
            
            var target = $(event.currentTarget);
            
            var id = target.attr('id');
            var name = target.find('.name').text();
            
            var photo = target.find('img').attr('src');
            
            var phoneNumber = target.data('phoneNumber');
            phoneNumber = JSON.parse(phoneNumber);
            //$('body').trigger('showUser' , [id,name , photo]);
            
            $('body').trigger('addToHistory',['showTechoContacts']);
            
            var user = {};
            
            user.id = id;
            user.name = name;
            user.photo = photo;
            user.phoneNumber = phoneNumber;
            
            $('body').trigger('showRemainders', [JSON.stringify(user)]);
            
        });
    };
    
    $('body').on('showTechoContacts',function (){
        
        $('body').trigger('headerMiddle',['Next Time']);
        $('body').trigger('headerRight',['<img src="img/contacts.png" />','getAllContacts']);// local event
        
        updateSidebar();
        
        var def = new $.Deferred();
        loadTemplate(def);
        def.done(function (){
            
            elem.show();
            loadContactInfo();
            //attachEventHandlers();
            
        });
        
    });
    
    $('body').on('taphold' , '.contact' , function (event){
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
        
        var selectedElements = $('.contact[selected=selected]');
        
        if(selectedElements.length === 0){
            $('body').trigger('hideSidebar'); 
        }else{
            $('body').trigger('showSidebar');  
        }

        
    });
    
    $('body').on(configuartion.events.userselect , '.contact' , function (event){
            
        var target = $(event.currentTarget);
            
        var id = target.attr('id');
        var name = target.find('.name').text();
        
        var photo = target.find('img').attr('src');
            
        var phoneNumber = target.data('phoneNumber');
        phoneNumber = JSON.parse(phoneNumber);
            //$('body').trigger('showUser' , [id,name , photo]);
            
        $('body').trigger('addToHistory',['showTechoContacts']);
            
        var user = {};
            
        user.id = id;
        user.name = name;
        user.photo = photo;
        user.phoneNumber = phoneNumber;
            
        $('body').trigger('showRemainders', [JSON.stringify(user)]);
            
    });
    
});

$('body').on('RegisteredContactsReady',function(event){
    registeredContacts();
    
    $('body').trigger('showTechoContacts');
    
    $( this ).off( event ); // similar jquery one event
});