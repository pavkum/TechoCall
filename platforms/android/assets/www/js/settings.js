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
        if(data && data.config){
            for(var i=0; i<data.config.length; i++){
                var config = data.config[i];
                
                var name = config.name;
                var value = config.value;
                
                elem.find('#' + name).find('input').val(value);
            }
        }
    };
    
    var getSettingsError = function (error) {
        // do nothing
    };
    
    $('body').on('settings' , function (){
        
        var def = new $.Deferred();
        
        $('body').trigger('addToHistory',['settings']);
        
        $('body').trigger('headerMiddle',['Settings']);
        $('body').trigger('headerRight',['<img src="img/settings.png" />']);// local event
        
        
        loadTemplate(def);
        
        def.done(function (){
            //techoStorage.getSettings(getSettingsSuccess , getSettingsError , []);
        });
        
    });
    
    var updateSuccess = function (data) {
        // do nothing
    };
    
    var updateError = function (error) {
        alert(error);
        // do nothing
    };
    
    var handleConfiguration = function (id , target) {
        var val = target.find('input').val();
        
        var setting = {
            option : id,
            value : val
        };
        
        //techoStorage.updateSettings(updateSuccess , updateError , [setting]);
    };
    
    var handleAbout = function () {
        
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