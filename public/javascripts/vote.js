function vote(id) {
    var value = $("#vote-" + id).html() === '-1';
    $.get(voteAction({talk:id, value:value}), function (data) {
        if (data == -1) {
            alert("Erreur pendant le vote");
        }
        else {
            $("#counter-" + id).html(data);
            $("#vote-" + id).toggleClass("disabled danger primary");
            $("#vote-" + id).html((value)?'+1':'-1');
        }
    })
}