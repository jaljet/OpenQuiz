function getAnswers() {
    var Answers = $.ajax({
        type: "GET",
        url: "rest/admin/check/answers",
        async: false,
    }).responseText;
    return Answers.split(';');
}

function getSortedByAnswers() {
    var Answers = $.ajax({
        type: "GET",
        url: "rest/admin/check/sortedbyanswers",
        async: false,
    }).responseText;
    return Answers.split(';');
}

function checkAnswer(id, correctCode, correctText) {
    $.ajax({
        type: "POST",
        url: "rest/admin/check/answer",
        data: id + "|" + correctCode,
        contentType: 'text/plain',
        success: function () {
            if (id.toString().startsWith('0')) {
                id = parseInt(id.toString().substring(1));
            }
            $('#' + id).text(correctText);
            $('#' + id).attr('correctCode', correctCode);
            $('#' + 0 + id).text(correctText);
            $('#' + 0 + id).attr('correctCode', correctCode);
        }
    }).responseText;
}