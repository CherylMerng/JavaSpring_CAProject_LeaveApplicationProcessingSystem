window.onload = function(){
	var btns = document.getElementsByClassName('btn-light');

    for(var i = 0; i < btns.length; i++){
        let btn = btns[i];

        btn.addEventListener('click', OnClick);
    }
}

function OnClick(event){
    let elem = event.currentTarget;

    if(elem.value == "+"){
        elem.value = "-";
    }
    else if(elem.value == "-"){
        elem.value = "+";
    }
}

function validateForm() {
    var x = document.forms["pageform"]["page"].value;
    if (x == "") {
      return false;
    }
}