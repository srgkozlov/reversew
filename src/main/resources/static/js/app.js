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
    var stompCli = Stomp.over(new SockJS('/stomp-over-ws'));
    stompCli.connect({}, function (frame) {
//Регистрация обработчика списка слов
        stompCli.subscribe('/words',
            function (wordsList) {
                populateTable($('#words-list'), 
                    JSON.parse(wordsList.body));
        });
//Загрузка уже имеющихся слов (не первая сессия)
        stompCli.send('/get/words', {}, {});
    });
    return stompCli;
};

$(function() {
    var stompCli = connect();
    $("#input-word").keyup(function (e) {
        if (e.keyCode == 13) {
            var w = $(this).val().trim();
//Отправляем новое
            if (w) stompCli.send('/word', {}, w);
            $(this).val('');
        }
    });
});