var registeredUser = (function (){
    
    var elem = $('#workarea');

    var id , displayName,photo = undefined;
    
    var loadTemplate = function (def, args) {
        var promise = moduleLoader.loadModule('registeredUser');
        
        promise.done(function (data){
            elem.empty();
            elem.html(data);
            //elem.css('visibility','hidden');
            def.resolve(args);
            
        });
        
        return def.promise();
    };
    
    var adjust = function () {
        /*var pos = elem.position();
        
        var width = elem.width();
        var height = elem.height();
        
        var sidebar = $('.sidebar');
        sidebar.css("top",pos.top);
        sidebar.css("left",pos.left);
        sidebar.width(width * 0.15);
        sidebar.height(height);
        
        $('.sidebar table').width(width * 0.15);
        
        $('.sidebarElem').height(height*0.1);*/
        
        //$('.content').height(height);
    };
    
    var addSidebarEvents = function () {
        $('#userInfo').on(configuartion.events.userselect , function (){
            $('body').trigger('userInfo');// Pass data later
        });
        
        $('#remainder').on(configuartion.events.userselect , function (event, userID){
            
            $('body').trigger('showRemainders', [userID]);
        });
    };
    
    $('body').on('showUser',function (event, userID, name,UserPhoto){
        
        id = userID;
        displayName = name;
        photo = UserPhoto;
        
        $('body').trigger('addToHistory',['showTechoContacts']);
        
        var def = new $.Deferred();
        
        $('body').trigger('headerMiddle' , [displayName]);
        $('body').trigger('headerRight' , ['<img src="' + photo+ '" height="100%" >']);
        
        loadTemplate(def , userID);
        def.done(function (userID){
            adjust();
            addSidebarEvents();
            //elem.css('visibility','visible');
            
            $('#remainder').trigger('click' , [userID]); // testing
        });
    });
    
});

$('body').on('RegisteredUserReady',function(event){
    registeredUser();
    
    //$('body').trigger('showUser');
    
    $( this ).off( event ); // similar jquery one event
});