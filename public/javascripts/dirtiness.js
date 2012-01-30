
$(document).ready(function() {
    // Activate modal box
    $('#dirtyDialog').modal({
                  keyboard: true,
                  backdrop: 'static'
    });
    $.DirtyForms.dialog = {

        // Selector is a selector string for dialog content. Used to determin if event targets are inside a dialog
        selector : '#dirtyDialog .content',

        // Fire starts the dialog
        fire : function(message, title){
//                    var content = '<h1>' + title + '</h1><p>' + message + '</p><p><a href="#" class="ignoredirty button medium red continue">Continue</a><a href="#" class="ignoredirty button medium cancel">Stop</a>';
//                    $.facebox(content);
                $('#dirtyDialog').modal('show');
        },

        // Bind binds the continue and cancel functions to the correct links
        bind : function(){
                var close = function(decision) {
                        return function(e) {
                                e.preventDefault();
                                // $(document).trigger('close.facebox');
                                $('#dirtyDialog').modal('hide');
                                decision(e);
                        };
                };
                $('#dirtyDialog .cancel, #dirtyDialog .close').click(close(decidingCancel));
                $('#dirtyDialog .continue').click(close(decidingContinue));
        },

        // Refire handles closing an existing dialog AND fires a new one
        refire : function(content, ev){
                $('#dirtyDialog').modal('hide');
                $('#dirtyDialog').modal('show');
        },

        // Stash returns the current contents of a dialog to be refired after the confirmation
        // Use to store the current dialog, when it's about to be replaced with the confirmation dialog. This function can return false if you don't wish to stash anything.
        stash : function(){
//                    var fb = $('#facebox');
//                    return ($.trim(fb.html()) == '' || fb.css('display') != 'block') ?
//                       false :
//                       $('#facebox .content').clone(true);
                  return false;
        }
    };
    // Enable dirty form managing
    $('form').not('#formTopbarSearch').dirtyForms();
});
