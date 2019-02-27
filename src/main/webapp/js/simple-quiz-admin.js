function addQuestions() {
   var questionsString = $('#data').val();
   $.ajax({
       type: "POST",
       url: "rest/admin/addQuestions",
       data: questionsString,
       contentType: 'text/plain'
   })
}

function addPlayers() {
   var playersData = $('#playersData').val();
   $.ajax({
       type: "POST",
       url: "rest/admin/addPlayers",
       data: playersData,
       contentType: 'text/plain'
   })
}