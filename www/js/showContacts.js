/*var contacts = (function (){

    var elem = $('#workarea');
    
    var template = undefined;
    
    var dummyElement = undefined;
    
    var loadTemplate = function (def) {
        var promise = moduleLoader.loadModule('allContacts');
        
        promise.done(function (data){
            elem.html(data);
            def.resolve();
        });
        
        return def.promise();
    };
    
    var getAllContacts = function() {
        var options      = new ContactFindOptions();
        options.multiple = true;
        var fields       = ["*"];
        navigator.contacts.find(fields, onSuccess, onError, options);    
    };
    
    var message = function (text) {
        var message = $('#message');
        message.height(elem.height());
        message.css('line-height',elem.height() + 'px');
        message.text(text);
        message.show();
    };
    
    var onSuccess = function(contacts) {
            if(contacts.length === 0){
                message('Nothing to display');
            }else{
                message('Loading...');
                var clone = $('#clone');
                var contactsWrapper = $('#contacts');
                
                var size = elem.height() / 10 * 0.96;
                
                $('.image img').height(size);
                $('.image img').width(size);
                $('.image').height(size);
                $('.image img').css('border-radius', size +'px');
                
                $('.name').css('line-height', size +'px');
                
                for(var i=0; i<contacts.length; i++){
                    var newElem = clone.clone();
                    
                    if(contacts[i].photos){
                    
                        var imgURL = contacts[i].photos[0];
                        
                        //img.css('border-radius', elem.height() / 10 * 0.96 + 'px');
                        var img = newElem.find('img');
                        img.attr('src',imgURL);
                    }
                    var name = newElem.find('.name');
                    name.text(contacts[i].displayName);
                    
                    var wrapper = newElem.find('.wrapper');
                    wrapper.css('border-left','5px solid '+flatUIColors.getNextColors());
                    
                    contactsWrapper.append(newElem);
                }
                $('#message').hide();
                $('.row').show();
                //elem.html(dummyElement);
            }
        
    };
    
    var onError = function(contactError) {
        alert('onError!');
    };
    
    $('body').on('getAllContacts',function (){
        
        $('body').trigger('headerMiddle',['Contacts']);
        
        var def = new $.Deferred ();
        loadTemplate(def);
        def.done(function (){
            //getAllContacts(); 
            
            var c = {};
            c.displayName = "Pavan";
            
            var ph = ['https://scontent-b-cdg.xx.fbcdn.net/hphotos-prn2/t1.0-9/1458566_768090913201444_94017057_n.jpg'];
            c.photos = ph;
            
            var d = {};
            d.displayName = "Pavan";
            
            var ph = ['https://scontent-b-cdg.xx.fbcdn.net/hphotos-prn2/t1.0-9/1458566_768090913201444_94017057_n.jpg'];
            d.photos = ph;
            
            //onSuccess([c,d]);
        });
    });
    
});

$('body').on('ContactsReady',function (event){
    contacts();
    $( this ).off( event ); // similar jquery one event
});*/

var contacts = (function (){
    var parent = $('#workarea');
    var elem = undefined;
    var search = undefined;
    
    var resultTemplate = "<div class='row'> </div>";
    
    var loadTemplate = function (def) {
        var promise = moduleLoader.loadModule('allContacts');
        
        promise.done(function (data){
            parent.html(data);
            def.resolve();
        });
        
        return def.promise();
    };
    
    var initialize = function () {
        var def = new $.Deferred ();
        
        $('body').trigger('addToHistory',['showTechoContacts']);
        
        $('body').trigger('headerMiddle',['Add Contact']);
        $('body').trigger('headerRight',['<img src="img/next.png" />','toUser']);// local event
        
        loadTemplate(def);
        
        def.done(function (){
            elem = $('.wrapper');
            elem.height(parent.height() * 0.96);
            
            var height = elem.height();
            
            search = $('.search');
            search.css('max-height',height/10 * 5 + 'px');
            
            $('#contact').height(height / 10);
            
            resultTemplate = $(resultTemplate);
            resultTemplate.css('line-height',height/10 + 'px');
            resultTemplate.height(height/10)
            
            filterResult('No Result');
            
            $('#contact').focus();
            
            $('body').on(configuartion.events.userselect, '.row' ,function (event){
                var target = $(event.target);
                
                if(target.attr('id')){
                    $('#contact').val(target.text());
                    $('#contact').data('contactID',target.attr('id'));
                    $('#contact').data('phoneNumber' , target.data('phoneNumber'));
                    var photo = target.find('img');
                    if(photo.length != 0)
                        $('#contact').data('photo',photo.attr('src'));
                }
            });
            
            $('#contact').keyup(function (event){
                var val = event.target.value;
                clearResults(); 
                if(val.length != 0 && val.length >= 3){
                    filterResult('Loading...','loading');
                    getAllContacts(val);    
                }else{
                    filterResult('No Result','noresult');
                }
            });
            
        });
    };
    
    var getAllContacts = function(search) {
        var options      = new ContactFindOptions();
        options.filter = search;
        options.multiple = true;
        var fields       = ['displayName', 'photos', 'id' , 'phoneNumbers'];
        navigator.contacts.find(fields, onSuccess, onError, options);    
    };
    
    var onSuccess = function(contacts) {
        clearResults(); 
        if(contacts.length === 0){
            filterResult('No Result');    
        }else{
            
            for(var i=0; i<contacts.length; i++){
                if(contacts[i].displayName && contacts[i].displayName != 'null')
                    filterResult(contacts[i].displayName, contacts[i].id, contacts[i].photos , contacts[i].phoneNumbers);   
            }
        }
    };
    
    var onError = function(contacts) {
        clearResults(); 
        filterResult('Error');
    };
    
    var filterResult = function (data , id , photos , phoneNumbers) {
        
        var phones = [];
        if(phoneNumbers) {
            for(var i=0; i<phoneNumbers.length; i++){
                var phone = {};
                phone.number = phoneNumbers[i].value.replace("(" , "").replace(")" , "").replace(" " , "").replace("-" , "");
                phone.type = phoneNumbers[i].type;
                
                phones.push(phone);
            }
        }
        var clone = resultTemplate.clone();
        clone.attr('id',id);
        clone.data('phoneNumber' , JSON.stringify(phones));
        clone.text(data);
        
        var src ;
        
        if(photos && photos[0]){
            src = photos[0].value;
            
        }else{
            src = "img/photo.jpg";
        }
        clone.append('<img src="' +src + ' " height="100%"   />');   
        search.append(clone);
    };
    
    var clearResults = function () {
        search.html('');  
    };
    
    $('body').on('getAllContacts',function (){
        initialize(); 
    });
    
    var addContactSuccess = function (contact) {
        $('body').trigger('showRemainders',[contact.id , contact.displayName]);
    };
    
    var addContactError = function (error) {
        
        clearResults();
        filterResult('Error creating');
    };
    
    $('body').on('toUser',function (){
        var name = $('#contact').val();
        var id = $('#contact').data('contactID');
        var photo = $('#contact').data('photo');
        var phoneNumber = $('#contact').data('phoneNumber');
        
        if(!photo){
            photo = 'img/photo.jpg';
        }
        
        var contact = {
            'id' : id,
            'displayName' : name,
            'photo' : photo,
            'phoneNumber' : phoneNumber
        };
        
        techoStorage.addContact(addContactSuccess,addContactError,[contact]);
        
    });
    
});

$('body').on('ContactsReady',function (event){
    contacts();
    $( this ).off( event ); // similar jquery one event
});