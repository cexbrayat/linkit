var fav = function(id) {
    if($('#star-'+id).hasClass('fav')){
        removeFromFavs(id);
    } else {
        addToFavs(id);
    }
};

var removeFromFavs = function(id) {
    $('#star-'+id).removeClass('fav');
    $.get(favAction({talkId: id, fav: true}), function(data) {
        updateCounts(id,data);
    });
};
var addToFavs = function(id) {
    $('#star-'+id).addClass('fav');
    $.get(favAction({talkId: id, fav: false}), function(data) {
        updateCounts(id,data);
    });
};
var updateCounts = function(id,count) {
    var text = $('#favs-'+id).text().split(' ');
    $('#favs-'+id).html(count + " " + text[1]);
}
