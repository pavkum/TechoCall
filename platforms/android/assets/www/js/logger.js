    var Logger = function(){
      
        var status = true;
        
        this.log = function (level, tag, msg){
            
            if(status === true) {
                
                var logData = {
                    'level' : level,
                     'tag' : tag,
                    'msg' : msg,
                };
                
                $.ajax({
                    url : 'http://192.168.0.100:32000/log',
                    method : 'POST',
                    data : logData,
                    
                    success : function (data) {
                        if(data != 'OK'){
                            console.log('Error Logging');
                        }
                    },
                       
                    error : function (xhr,textStatus,errorThrown) {
                        console.log('Error logging'+errorThrown);
                    }
                });
            }
        };
    };

    Logger.prototype.off = function () {
        status = false;
    };

    Logger.prototype.on = function () {
        status = true;
    };

    Logger.prototype.trace = function (tag,msg) {
        this.log('trace',tag,msg);
    };
    
    Logger.prototype.info = function (tag,msg) {
        this.log('info',tag,msg);
    };
    
    Logger.prototype.warn = function (tag,msg) {
        this.log('warn',tag,msg);
    };
    
    Logger.prototype.error = function (tag,msg) {
        
        this.log('error',tag,msg);
    };


$(document).ready(function (){
     window.Logger = new Logger();
});