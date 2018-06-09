function populateTable (table, wordsList) {
    var tb = $('<tbody>');
    wordsList.forEach(function(el) {
        var tr = $('<tr>');
        tr.append($('<td>').append(el.word));
        tr.append($('<td>').append(el.count));
        tb.append(tr)
    });
    table.find('tbody').replaceWith(tb);
};

function connect() {
    var ws = new WebSocket("ws://localhost:8080/word");
    ws.onmessage = function(m) {
        console.log(m);
        populateTable($('#words-list'), 
        JSON.parse(m.data));
    };
    ws.onopen = function(m) {
        this.send('');
    }
    return ws;
};

$(function() {
    var ws = connect();
    $("#input-word").keyup(function (e) {
        if (e.keyCode == 13) {
            var w = $(this).val().trim();
            if (w) ws.send(w);
            $(this).val('');
        }
    });
});