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
    
    deleteContact : function (sucesscallback, errorcallback, args) {
        try {
            exec(successcallback ,  errorcallback , 'techoStorage' , 'deleteContact' , args);
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
    },
    
    // settings
    
    updateSettings : function (successcallback, errorcallback, args) {
        try{
            exec(successcallback , errorcallback , 'techoStorage' , 'updateSettings' , args)
        }catch(error) {
            errorcallback('Failed to update preferences');   
        }
    },
    
    getSettings : function (successcallback, errorcallback, args) {
        try{
            exec(successcallback , errorcallback , 'techoStorage' , 'getSettings' , args)
        }catch(error) {
            errorcallback('Failed to obtain preferences');   
        }
    },
};

module.exports = techoStorage;
