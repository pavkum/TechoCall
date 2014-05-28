var userInfo = (function (){
    
    var userImage;
    var userName;
    var userNumbers = [];
    
    var user = {};
    
    var elem = $('#workarea');
    
    var template = $('<div class="number">' + 
                     '<div class="msisdn"> </div>' +
                     '<div class="type"> </div>' + 
                     '<div class="clearfix"> </div>' + 
                     '</div>');
    
    var loadTemplate = function (def) {
        if(elem.length === 0) elem = $('#workarea');
        var promise = moduleLoader.loadModule('userInfo');
        
        promise.done(function (data){
            elem.html(data);
            def.resolve();
        });
    };

    var adjust = function () {
        
        var name = elem.find('#name');
        name.css('line-height',name.height() + 'px');
        name.text(user.name);
        
        var img = elem.find('img'); // img has 2% margin
        img.attr('src',user.photo);
        
        img.load(function () {
            var width = img.width();
            var parentWidth = img.parent().width();
            img.css('margin-left',(parentWidth - width)/2);
        });
        
        var numbers = elem.find('#numbers');
        
        for(var i=0; i< user.phoneNumber.length; i++){
            
            var clone = template.clone();
            
            var phoneNumber = user.phoneNumber[i];
            
            clone.find('.msisdn').text(phoneNumber.number);
            clone.find('.type').text(phoneNumber.type);
            
            numbers.append(clone);
            
        }
    };
    
    $('body').on('userInfo',function (event, userObj){
        var def = new $.Deferred();
        loadTemplate(def);

        user = JSON.parse(userObj);
        
        // sample data
        
        def.done(function (){
            adjust();   
        });
        
    });
    
})();

