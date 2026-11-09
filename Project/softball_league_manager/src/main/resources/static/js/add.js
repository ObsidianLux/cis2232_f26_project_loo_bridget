const form = document.getElementById('formAdd');

const homeTeamInput = document.getElementById('inputHomeTeam');
const roadTeamInput = document.getElementById('inputRoadTeam');
const gameDayInput = document.getElementById('inputGameDay');

const forfeitSwitch = document.getElementById('switchForfeit');
const teamAtFaultSelect = document.getElementById('teamAtFault');

const inningsContainerInner = document.getElementById('inningsContainerInner');
const additionalInnings = document.getElementById('inningsAdditional');
const addInningButton = document.getElementById('btnAddInning');

/* SUBMIT VALIDATION METHODS */

// Team names must not be blank and must not be the same.

/**
 * Checks whether the values of the two team name fields are identical.
 * Applies styling to the team name inputs and displays a message to indicate to the user what the issue is if they are invalid.
 * @returns {boolean} True if the team names are unique and not empty, otherwise false.
 * @author BML
 * @since 20261107
 */
function validateTeamNames() {
    const teamsErrorElement = document.getElementById('teamsDuplicateError');

    // Get the trimmed home team name.
    const homeTeam = homeTeamInput.value.trim();

    // Get the trimmed road team name.
    const roadTeam = roadTeamInput.value.trim();

    /*
    Check whether both inputs have values and that they are the exact same.
    If either is empty or if they aren't identical, keep the default styling.
    The form's own validation will prevent submission if the fields are empty.
     */

    // Unless the names are valid and not empty, forfeit checkbox is disabled.
    if (homeTeam !== "" && roadTeam !== "") {
        if (homeTeam === roadTeam) {
            setErrorStyling();
            forfeitSwitch.disabled = true;
            return false;
        } else forfeitSwitch.disabled = false;
    }

    setDefaultStyling();
    return true;

    function setDefaultStyling() {
        // Remove error styling.
        removeInvalid(homeTeamInput);
        removeInvalid(roadTeamInput);

        // Hide the error element.
        hideElement(teamsErrorElement);
    }

    function setErrorStyling() {
        // Apply error styling.
        applyInvalid(homeTeamInput);
        applyInvalid(roadTeamInput);

        // Unhide the error element.
        unhideElement(teamsErrorElement);
    }
}

/**
 * Checks whether the given date is in the past or not and either enables or disables other inputs based on the result.
 * @returns {boolean} True if the date is in the past, false otherwise.
 */
function gameHasBeenPlayed() {
    const date = gameDayInput.valueAsNumber;

    const inputs = inningsContainerInner.querySelectorAll('.inning-row input[type="number"]');

    if (date >= Date.now()) {
        // Disable inputs for forfeit and innings.
        forfeitSwitch.disabled = true;

        inputs.forEach(input => input.disabled = true);
        addInningButton.disabled = true;

        return false;
    } else {
        // Enable inputs for forfeit and innings.
        forfeitSwitch.disabled = false;

        inputs.forEach(input => input.disabled = false);
        addInningButton.disabled = false;

        return true;
    }
}

/* EVENT LISTENERS */

// Event listeners on the home and road team inputs to apply styling if they match.
homeTeamInput.addEventListener('change', validateTeamNames);
roadTeamInput.addEventListener('change', validateTeamNames);

// Event listener on the game date input to control the rest of the form inputs.
gameDayInput.addEventListener('change', gameHasBeenPlayed);

// Listen to forfeit checkbox to enable/disable other inputs.
forfeitSwitch.addEventListener('change', (event) => {
    const inningInputs = inningsContainerInner.querySelectorAll('.inning-row input[type="number"]');

    if (event.target.checked) {
        // If checked, disable and clear innings and enable team at fault select.
        inningInputs.forEach(input => {
            input.disabled = true;
            input.value = "0";
        });

        // Populate the select element.
        const fragment = document.createDocumentFragment();
        fragment.appendChild(new Option("", ""));

        [homeTeamInput.value.trim(), roadTeamInput.value.trim()].forEach(option => {
            fragment.appendChild(new Option(option, option));
        });

        teamAtFaultSelect.appendChild(fragment);

        // Enable the select element.
        teamAtFaultSelect.disabled = false;
    } else {
        // If unchecked, disable and clear team at fault select and enable innings.
        teamAtFaultSelect.disabled = true;
        teamAtFaultSelect.innerHTML = '';

        inningInputs.forEach(input => {
            input.disabled = false;
        });
    }
});

// Listen for a click to add a new inning row.
addInningButton.addEventListener('click', event => {
    console.log("Clicked add!");

    const templateInning = document.getElementById('templateInningRow');

    // Get a deep copy of the template inning.
    const newInning = templateInning.content.cloneNode(true);

    // Append the inning element to the inner innings' container.
    additionalInnings.appendChild(newInning);

    // Recalculate numbering and set attributes.
    updateInningNumbers();
});

/* FUNCTIONS */

function removeInning(element) {
    // Remove this row.
    element.parentElement.remove();

    // Fix inning numbering.
    updateInningNumbers();
}

/**
 * Ensure the inning numbering is in a proper sequence.
 */
function updateInningNumbers() {
    // Get the inning rows.
    const innings = additionalInnings.querySelectorAll('.inning-row');

    innings.forEach((inning, index) => {
        // Set the inning number in the readonly text field. + 6 because there will always be 5 in another element and the display index should start at 1.
        inning.querySelector('input[data-field="inningNumber"]').value = (index + 6).toString();

        // Set the Spring MVC name and id attributes for each field that should map to an Inning object.
        const inputs = inning.querySelectorAll('input[data-field]');
        inputs.forEach(input => {
            const fieldName = input.getAttribute('data-field');

            input.name = `innings[${index}].${fieldName}`;
            input.id = `innings[${index}].${fieldName}`;
        });

        // TODO Associate labels.
    });
}