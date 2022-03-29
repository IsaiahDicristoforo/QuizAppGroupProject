const requiredClassName = "required-input";

function checkForm() {
    if (isRequiredComplete()) {
        let formDOM =  $('.submit-form');
        let formCompleteDOM = $('.form-complete')
        formCompleteDOM.val(true);
        formDOM.submit();

    }
    else if (isRequiredComplete !== true) {
        alert('Please correct all the highlighted fields.');
        let requiredDOM = $('.' + requiredClassName);

        requiredDOM.each(function () {
            removeErrorMessage($(this));
            addErrorMessage($(this), false);
        });
    }
}

function isRequiredComplete() {
    let requiredDOM = $('.' + requiredClassName);
    let passed;
    requiredDOM.each(function () {
        if ($(this).val() === '') {
            return passed = false;
        }
        else if ($(this).attr('type') === 'radio' || $(this).attr('type') === 'checkbox') {
            if ($(this).checked() === false) {
                if (multiGroupRadioValidation($(this)) === false) {
                    return passed = false;
                }
            }
        }
        else if ($(this).attr('type') === 'email') {
            if (emailValidation($(this)) === false) {
                return passed = false;
            }
        }
        else if ($(this).attr('type') === 'password' && $(this).hasClass('password-confirm') === false) {
            if (passwordValidation($(this)) === false) {
                return passed = false;
            }
        }
        else if ($(this).attr('type') === 'password' && (passwordConfirmValidation($(this)) === false || passwordValidation($(this)) === false) && $(this).hasClass('password-confirm') === true) {
            return passed = false;
        }
        return passed = true;
    });
    return passed;
}


function passwordValidation(e) {
    //checks if a password is between 6 to 20 characters as well as contains at least one numeric digit, one uppercase and one lowercase letter -- could use -> to add a special char check /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\s).{8,20}$/
    if (/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$/.test(e.val())) {
        return true;
    }
    return false;
}

function passwordConfirmValidation(e) {
    if (e.hasClass('password-confirm')) {
        let passwordDOM = $('.password-confirm');
        if (passwordDOM[0].val() === passwordDOM[1].val()) {
            return true;
        }
        return false;
    }
    return false;
}

//regrex check if a valid email is used
function emailValidation(e) {
    if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(e.val())) {
        return true;
    }
    return false;
}

//checks if at least one radio/checkbox is selected from a shared id
function multiGroupRadioValidation(e) {
    let groupRadioDOM = document.querySelectorAll('#' + e.attr('id'));
    let checkIfFalse = [];

    for (let x = 0; groupRadioDOM.length > x; x++) {
        checkIfFalse.push(groupRadioDOM[x].checked);
    }

    const allEqual = arr => arr.every(v => v === arr[0])
    if (allEqual(checkIfFalse) && checkIfFalse[0] == false) {
        return false;
    }

}

function displayErrorMessage(errorMessageText, targetElement) {
    let errorMessage = document.createElement('span');
    let divDOM = document.createElement('div');
    errorMessage.id = targetElement.attr('id') + 'errorMessage';
    errorMessage.innerHTML = errorMessageText.italics()
    errorMessage.style.color = 'black';
    errorMessage.style.float = 'right';
    errorMessage.style.marginTop = '10px'


    if (targetElement.attr('type') === 'textarea') {
        errorMessage.style.float = 'left';
    }

    if (targetElement.attr('type') !== 'radio' && targetElement.attr('type') !== 'checkbox' && targetElement.attr('id') !== 'fileUpload') {
        targetElement.addClass('inlineError');
        targetElement.parent().append(errorMessage);
    }
    else {
        if (targetElement.hasClass('radio-inline-validation') === true) {
            errorMessage.innerHTML = '&nbsp;&nbsp;&nbsp;' + errorMessageText.italics();
            targetElement.parent().parent().append(errorMessage.clone());
        }
        else {
            divDOM.classList.add('row');
            errorMessage.innerHTML = '&nbsp;&nbsp;&nbsp;' + errorMessageText.italics();
            targetElement.parent().parent().append(divDOM);
            divDOM.appendChild(errorMessage.clone());
        }
    }

}

function addErrorMessage(e, isInline) {
    //display inline message for empty fields
    if ((e.val() === '' && e.attr('type') === 'textarea') || (e.val() === '' && e.attr('type') === 'text') ||  (e.val() === '' && e.val() === 'email') || (e.val() === '' && e.attr('type') === 'password')) {
        displayErrorMessage('This field is required.', e);
    }

    // Radio/checkbox inline message --This should have been done with an id not class
    else if ((e.attr('type') === 'radio' || e.attr('type') === 'checkbox') && e.hasClass('uniqueclass') === false) {
        let groupDOM = document.querySelectorAll('#' + e.id)
        let checkRadio = [];

        for (let i = 0; groupDOM.length > i; i++) {
            checkRadio.push(groupDOM[i].checked)
        }

        for (let x = 0; groupDOM.length - 1 > x; x++) {
            groupDOM[x].classList.add('uniqueclass')
        }

        if (checkRadio.includes(true)) {

        }
        else {
            displayErrorMessage('This field is required.', groupDOM[checkRadio.length - 1]);
        }
    }

    // display message if the email is invalid
    else if (e.attr('type') === 'email' && emailValidation(e) === false) {
        displayErrorMessage('Please enter a valid email.', e)
    }
    //If password doesn't match regex
    else if (e.attr('type') === 'password' && passwordValidation(e) === false) {
        displayErrorMessage('Please enter a valid password.', e)
    }
    //If the passwords do not match
    else if (e.attr('type') === 'password' && (passwordConfirmValidation(e) === false || passwordValidation(e) === false) && e.hasClass('password-confirm') === true && isInline === false) {
        displayErrorMessage('Passwords need to match.', e)
    }
}

function removeErrorMessage(e) {
    let errorMessageDOM = $('#' + e.attr('id') + 'errorMessage');

    if (errorMessageDOM.length > 0) {
        errorMessageDOM.remove();
        e.removeClass('inlineError')
    }
}

function initInlineValidation() {
    let requiredDOM = $('.' + requiredClassName);
    requiredDOM.each(function () {
        $(this).on('focus', function () {
            removeErrorMessage($(this));
        });

        $(this).on('blur', function () {
            addErrorMessage($(this), true);
        });
    })
}

initInlineValidation();