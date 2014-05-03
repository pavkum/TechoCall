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
            
            wrapper.append(clone);
            
            
        }
        
    };
    
    var loadContactInfoError = function (error){
        Logger.error('contacts',error);
    };
    
    var loadContactInfo = function () {
        techoStorage.getAllContacts(loadContactInfoSuccess,loadContactInfoError,[]);
    };
    
    var attachEventHandlers = function () {
        
        $('body').on(configuartion.events.userselect , '.contact' , function (event){
            
            $(this).off(event);
            
            var target = $(event.currentTarget);
            
            var id = target.attr('id');
            var name = target.find('.name').text();
            
            var photo = target.find('img').attr('src');
            
            $('body').trigger('showUser' , [id,name , photo]);
            
            
        });
    };
    
    $('body').on('showTechoContacts',function (){
        
        $('body').trigger('headerMiddle',['Techo Call']);
        $('body').trigger('headerRight',['+','getAllContacts']);// local event
        
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