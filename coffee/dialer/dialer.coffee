do ($ = jQuery) ->
    
    initialize = ->
        elem = $('#dialer')
        elem.text('Hello man');
        
    $(document).on 'deviceready' , ->
        initialize()