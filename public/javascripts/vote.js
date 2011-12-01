function vote(id, name) {
    var value = $("#vote-" + id).hasClass("inactive");
    $.get(voteAction({talk:id, member:name, value:value}), function (data) {
        if (data == -1) {
            alert("Erreur pendant le vote")
        }
        else {
            $("#counter-" + id).html(data);
            $("#vote-" + id).toggleClass("active");
            $("#vote-" + id).toggleClass("inactive");
        }
    })
}