
var updateData = function (data , openClosed) {


    var template = $('<div class="item" style="display:none">' + 
                   '<input class="checkbox" type="checkbox" id="id" />' +
                   '<label for="id">Talk about completion</label>' +
                   '</div>') ; 
    
    
    $('#close').on('touchstart' , function (){
        Android.finish();
    });

    
    $('#all').on('touchstart' , function (){
        var selectedElements = $('.item');
        
        var remainders = [];
        
        for(var i=0; i<selectedElements.length; i++){
            
            var remainder = {};
            
            var selectedElement = selectedElements.eq(i);
            
            remainder.id = selectedElement.attr('id');
            remainder.message = selectedElement.find('label').text();
            
            remainders.push(remainder);   
        }
        console.log(remainders);
        Android.markSelectedRead(JSON.stringify(remainders));
    });
    
    $('#selected').on('touchstart' , function (){
        var selectedElements = $('.item[selected=selected]');
        
        var remainders = [];
        
        for(var i=0; i<selectedElements.length; i++){
            
            var remainder = {};
            
            var selectedElement = selectedElements.eq(i);
            
            remainder.id = selectedElement.attr('id');
            remainder.message = selectedElement.find('label').text();
            
            remainders.push(remainder);   
        }
        console.log(remainders);
        Android.markSelectedRead(JSON.stringify(remainders));
    });
    
    data = JSON.parse(data);
    
    var index = 0;
    $('.name').text(data.name);

    var message = $('.message');
    
    for(var i=0; i<data.remainders.length; i++){
        var clone = template.clone();
        
        clone.attr('id' , data.remainders[i].id);
        clone.attr('index' , i);
        
        var label = clone.find('label');
        
        label.attr('for' , data.remainders[i].id);
        label.html(data.remainders[i].message);
        
        message.append(clone);
        // dont show now
    }

    var visible ;
    
    if(data.remainders && data.remainders.length > 1){
        $('.left').show();
        $('.right').show();    
        
        /*$('.message').text(data.message[index]);*/
        
        visible = $('.item[index=' + index + ']');
        visible.show();
        
        $('.right').on('click' , function (){
            if((index + 1) < data.remainders.length) {
                index = index + 1;
                
                visible.hide(); // hide previous
                visible = $('.item[index=' + index + ']');
                visible.show();
                
                if((index + 1) === data.remainders.length){
                    $('.right').addClass('disabled');   
                }
                
                if($('.left').hasClass('disabled')){
                    $('.left').removeClass('disabled');
                }
            }
        });
        
        $('.left').on('click' , function (){
            if((index - 1) >= 0){
                index = index - 1; 
                
                visible.hide(); // hide previous
                visible = $('.item[index=' + index + ']');
                visible.show();
                
                if(index === 0){
                    $('.left').addClass('disabled');   
                }
                if($('.right').hasClass('disabled')){
                    $('.right').removeClass('disabled');
                }
            }
        });
        
        $('.left').addClass('disabled');   
        
    }else if(data.remainders && data.remainders.length === 1) {
        visible = $('.item[index=' + index + ']');
        visible.show();
    }else{
        // shouldn't come here
        $('.message').text('No remainder');
    }
    
    $(":checkbox").click(function (event) {
    //$('.checkbox').on('click' , function (event){
        var target = $(event.currentTarget);
        
        var thisVar = $(this);
        var isChecked = thisVar.is(':checked');
        
        var parent = target.parent();
        
       
        
        if(isChecked){
            parent.attr('selected' , 'selected');   
        }else{
            parent.removeAttr('selected');
        }
        
        
       
        
    });
    
    
}
