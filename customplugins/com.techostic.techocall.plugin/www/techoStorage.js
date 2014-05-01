var exec = require('cordova/exec');
var techoStorage = {
    addContact : function (successcallback, errorcallback,args) {
        try{
            exec(successcallback , errorcallback , 'techoStorage' , 'addContact' , args);    
        }catch(error){
            errorcallback('Please try again later');
        }
    },
    
    getAllContacts : function (successcallback, errorcallback) {
        try{
            exec(successcallback , errorcallback , 'techoStorage' , 'getAllContacts' , []);    
        }catch(error){
            errorcallback('Please try again later');
        }
    },
    
    addRemainder : function (successcallback, errorcallback, args){
        try{
            exec(successcallback ,  errorcallback , 'techoStorage' , 'addRemainder' , args);
        }catch(error){
            errorcallback('Please try again later');   
        }
    },
    
    updateRemainder : function (successcallback, errorcallback, args){
        try{
            exec(successcallback ,  errorcallback , 'techoStorage' , 'updateRemainder' , args);
        }catch(error){
            errorcallback('Please try again later');   
        }
    },
    
    getAllRemaindersById : function (successcallback, errorcallback, args){
        try{
            exec(successcallback ,  errorcallback , 'techoStorage' , 'getAllRemaindersByID' , args);
        }catch(error){
            errorcallback('Please try again later');   
        }
    },
    
    deleteRemainder : function (sucesscallback, errorcallback, args) {
        try {
            exec(successcallback ,  errorcallback , 'techoStorage' , 'deleteRemainder' , args);
        }catch(error){
            errorcallback('Please try again later');   
        }
    }
};

module.exports = techoStorage;
