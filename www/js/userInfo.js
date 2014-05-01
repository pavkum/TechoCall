var userInfo = (function (){
    
    var userImage;
    var userName;
    var userNumbers = [];
    
    var elem = $('#content');
    
    var loadTemplate = function (def) {
        if(elem.length === 0) elem = $('#content');
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
    
    $('body').on('userInfo',function (event,user){
        var def = new $.Deferred();
        loadTemplate(def);

        // sample data
        
        user = {};
        user.name = "Pavan Kumar L";
        user.numbers = [7760968318];
        user.img = "https://scontent-b-cdg.xx.fbcdn.net/hphotos-prn2/t1.0-9/1458566_768090913201444_94017057_n.jpg";
        
        def.done(function (){
            adjust(user);   
        });
        
    });
    
})();

