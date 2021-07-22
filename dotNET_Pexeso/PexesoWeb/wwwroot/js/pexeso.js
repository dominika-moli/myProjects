function showHiddenElm(){
    document.getElementById('settings').classList.remove("hiddenElm");
    document.getElementById('buttonNewGame').classList.add("hiddenElm");
    document.getElementById('buttonNewGame').classList.remove("centerik");
}


function hideElm(){
    document.getElementById('settings').classList.add("hiddenElm");
    document.getElementById('buttonNewGame').classList.remove("hiddenElm");
    document.getElementById('buttonNewGame').classList.add("centerik");
}

function showCom(){
    document.getElementById('commentiky').classList.remove("hiddenElm");
    document.getElementById('buttonAddComment').classList.add("hiddenElm");
    document.getElementById('buttonAddComment').classList.remove("centerik");
}


function hideCom(){
    document.getElementById('commentiky').classList.add("hiddenElm");
    document.getElementById('buttonAddComment').classList.remove("hiddenElm");
    document.getElementById('buttonAddComment').classList.add("centerik");
}
function showRat(){
    document.getElementById('rejting').classList.remove("hiddenElm");
    document.getElementById('buttonAddRating').classList.add("hiddenElm");
    document.getElementById('buttonAddRating').classList.remove("centerik");
}


function hideRat(){
    document.getElementById('rejting').classList.add("hiddenElm");
    document.getElementById('buttonAddRating').classList.remove("hiddenElm");
    document.getElementById('buttonAddRating').classList.add("centerik");
}

