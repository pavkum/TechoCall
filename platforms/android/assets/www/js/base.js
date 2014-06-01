window.defaultAlert = alert;
window.defaultConfirm = confirm;

window.alert = function (data , title){
    
    var modal = $('#modal');
    var overlay = $('#overlay');
    
    var def = new $.Deferred ();
    
    var promise = moduleLoader.loadModule('alert');
        
    promise.done(function (data){
        modal.html(data);
        def.resolve();
    });
    
    def.promise().done(function (){
        if(title){
            modal.find('#modalTitle').html(title);   
        }
        
        modal.find('#modalBody').html(data);
        
        overlay.show();
        modal.show(500);
        
        $('#modalClose').on(configuartion.events.userselect , function (event){
            modal.hide(500);
            overlay.hide();

            $(this).off(event);
        });
        
    });
    
};

window.notification = function (msg) {
    
    if(msg){
        var notification = $('#notification');
        
        notification.text(msg);
        
        notification.show(250);
        
        setTimeout(function (){
            notification.hide(250);
        },2500);
    }
    
    
};