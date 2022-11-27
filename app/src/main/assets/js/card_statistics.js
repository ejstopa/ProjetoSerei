window.addEventListener("load", function(){

    let studyingCardJson = CardStatisticsActivity.GetStudyingCardJson();
    let studyingCard = JSON.parse(studyingCardJson);

    updateCardStatics(studyingCard)


});

function updateCardStatics(StudyingCard){

    document.querySelector("#txt_card_name").innerHTML = StudyingCard.Name;
    document.querySelector("#txt_add_when").innerHTML = convertTimestampToShortDate(StudyingCard.AddedWhen);
    document.querySelector("#txt_studies_quantity").innerHTML = getStudyingCardCompletedPlays(StudyingCard)
    document.querySelector("#txt_questions_answered").innerHTML = getStudyingCardAnsweredQuestions(StudyingCard);
    document.querySelector("#txt_questions_right").innerHTML = getStudyingCardRightQuestions(StudyingCard);
    document.querySelector("#txt_questions_wrong").innerHTML = getStudyingCardWrongQuestions(StudyingCard);

    let cardPlaysDatesList = createCardPlaysDatesList(StudyingCard);
    let cardPlayRightAnswersList = createCardPlaysRightAnswersRateList(StudyingCard);

    createCardPlaysCharts(cardPlaysDatesList, cardPlayRightAnswersList);

}

function convertTimestampToShortDate(timeStamp){

     let date = new Date(timeStamp.seconds * 1000);
     let shortDate = date.toLocaleDateString();

     return shortDate;
}

function getStudyingCardCompletedPlays(StudyingCard){

    let studyingCardCompletedPlays = 0;
    let studyingCardPlaysList = StudyingCard.StudyingCardPlaysList;
    let currentStudyingCardPlay = studyingCardPlaysList[studyingCardPlaysList.length - 1];

    if (currentStudyingCardPlay.Finished){
        studyingCardCompletedPlays = studyingCardPlaysList.length;
    }
    else{
        studyingCardCompletedPlays = studyingCardPlaysList.length - 1;
    }

    return studyingCardCompletedPlays;
}

function getStudyingCardAnsweredQuestions(StudyingCard){

    let answeredQuestions = 0;
    let studyingCardPlaysList = StudyingCard.StudyingCardPlaysList;

    studyingCardPlaysList.forEach(function(item){
        answeredQuestions += item.RightAnswers.length;
        answeredQuestions += item.WrongAnswers.length;
    });

    return answeredQuestions;
}

function getStudyingCardRightQuestions(StudyingCard){

    let rightQuestions = 0;
    let studyingCardPlaysList = StudyingCard.StudyingCardPlaysList;

    studyingCardPlaysList.forEach(function(item){
        rightQuestions += item.RightAnswers.length;
    });

    return rightQuestions;
}

function getStudyingCardWrongQuestions(StudyingCard){

    let wrongQuestions = 0;
    let studyingCardPlaysList = StudyingCard.StudyingCardPlaysList;

    studyingCardPlaysList.forEach(function(item){
        wrongQuestions += item.WrongAnswers.length;
    });

    return wrongQuestions;
}

function createCardPlaysDatesList(StudyingCard){

    let studyingCardPlaysDatesList = [];
    let studyingCardPlaysList = StudyingCard.StudyingCardPlaysList;

    studyingCardPlaysList.forEach(function(item){

        if (item.Finished){

            studyingCardPlaysDatesList.push(convertTimestampToShortDate(item.FinishedWhen))
        }
    });

    return studyingCardPlaysDatesList;
}

function createCardPlaysRightAnswersRateList(StudyingCard){

    let cardPlaysRightAnswersList = [];
    let studyingCardPlaysList = StudyingCard.StudyingCardPlaysList;

    studyingCardPlaysList.forEach(function(item){
        if (item.Finished){

            let rightAnswers = item.RightAnswers.length;
            let wrongAnswers = item.WrongAnswers.length;
            let questionQuantity = rightAnswers + wrongAnswers;
            let rightAnswersRate = (rightAnswers / questionQuantity) * 100;

            cardPlaysRightAnswersList.push(rightAnswersRate);
        }

    });

    return cardPlaysRightAnswersList;
}

function createCardPlaysCharts(cardPlaysDatesList, cardPlayRightAnswersList){


    new Chart("card_plays_chart", {
      type: "line",
      data: {
        labels: cardPlaysDatesList,

        datasets: [{
          label: "Respostas corretas em %",
          data: cardPlayRightAnswersList,
          borderColor: "green",
          fill: false,
          tension: 0.1,
          pointRadius: 10
        }]
      },
      options: {
        maintainAspectRatio: false,
        legend: {display: true},
        clip: false,
        scales: {
            y: {
                beginAtZero: true,
                min: 0,
                max: 100,

                ticks: {
                    stepSize: 10,
                    autoSkip: false
                }
            },

            x: {

                ticks: {
    //                display: false,
                    minRotation: 45,
                    maxRotation: 90,
                    autoSkip: true
                }
            }

        }
      }
    });

}


