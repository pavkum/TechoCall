var settings = (function (){
    
    var elem = $('#workarea');

    var loadTemplate = function (def) {
        var promise = moduleLoader.loadModule('settings');
        
        promise.done(function (data){
            elem.html(data);
            def.resolve();
        });
        
        return def.promise();
    };
    
    var getSettingsSuccess = function (data) {
        
        if(data){
            for(var i=0; i<data.length; i++){
                var config = data[i];
                
                var name = config.name;
                var value = config.value;
                var id = config.id;
                
                console.log(id + ":" + name + ":" + value);
                
                if(value == 0){
                    elem.find('#' + id).find('input').removeAttr('checked');    
                }else{
                    elem.find('#' + id).find('input').attr('checked' , 'checked');    
                }
                
            }
        }
    };
    
    var getSettingsError = function (error) {
        notification('Error while loading settings');
    };
    
    $('body').on('settings' , function (){
        
        var def = new $.Deferred();
        
        $('body').trigger('addToHistory',['showTechoContacts']);
        
        $('body').trigger('headerMiddle',['Settings']);
        $('body').trigger('headerRight',['<img src="img/settings.png" />']);// local event
        
        
        loadTemplate(def);
        
        def.done(function (){
            techoStorage.getSettings(getSettingsSuccess , getSettingsError , []);
        });
        
    });
    
    var updateSuccess = function (data) {
        notification('Updated...');
    };
    
    var updateError = function (error) {
        notification('Error updating preferences');
    };
    
    var handleConfiguration = function (id , target) {
        // when clicked always getting opposite value, should investigate
        var val = target.find('input').attr('checked') ? 0 : 1;
        
        var setting = {
            id : id,
            value : val
        };
        
        techoStorage.updateSettings(updateSuccess , updateError , [setting]);
    };
    
    var handleAbout = function () {
        var data = "<div>Version 1.0.0</div>" + 
                    "<div>techocall@gmail.com</div>";
        var title = "About Next Time";
        
        alert(data , title);
    };
    
    $('body').on(configuartion.events.userselect , '.item' , function (event){
        var target = $(event.currentTarget);
        
        var id = target.attr('id');
        
        switch(id) {
                
            case 'about' : handleAbout();
                break;
            
            default : handleConfiguration(id , target)
                break;
                
            /*case 'autodelete' :
                break;
                
            case 'anonymoususage'
                break;*/
                
        }
    });
    
})();