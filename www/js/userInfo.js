var userInfo = (function (){
    
    var userImage;
    var userName;
    var userNumbers = [];
    
    var elem = $('#workarea');
    
    var loadTemplate = function (def) {
        if(elem.length === 0) elem = $('#workarea');
        var promise = moduleLoader.loadModule('userInfo');
        
        promise.done(function (data){
            elem.html(data);
            def.resolve();
        });
    };

    var adjust = function (user) {
        
        var name = elem.find('#name');
        name.css('line-height',name.height() + 'px');
        name.text(user.name);
        
        var img = elem.find('img'); // img has 2% margin
        img.attr('src',user.img);
        
        img.load(function () {
            var width = img.width();
            var parentWidth = img.parent().width();
            img.css('margin-left',(parentWidth - width)/2);
        });
    };
    
    $('body').on('userInfo',function (event, id, name , photo , phoneNumber){
        var def = new $.Deferred();
        loadTemplate(def);

        // sample data
        
        user = {};
        user.name = name;
        user.numbers = phoneNumber;
        user.img = photo;
        
        def.done(function (){
            adjust(user);   
        });
        
    });
    
})();

