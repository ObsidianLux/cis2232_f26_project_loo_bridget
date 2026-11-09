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

    if ((homeTeam !== "" && roadTeam !== "") && homeTeam === roadTeam) {
        setErrorStyling();
        return false;
    } else {
        setDefaultStyling();
        return true;
    }

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

/**
 * Checks whether the game was forfeited or not and that all necessary information has been provided.
 * @returns {boolean} True if all info provided, otherwise false.
 */
function validateForfeit() {
    const forfeited = forfeitSwitch.checked;

    if (forfeited) {
        // If forfeited is checked, a team at fault must be selected.
        return teamAtFaultSelect.value !== "";
    } else {
        const errorMissingInfo = document.getElementById('invalidInningsMissingInfo');
        const errorTie = document.getElementById('invalidInningsTie');
        const errorMercy = document.getElementById('invalidInningsMercy');

        // If forfeited is not checked, there must be at least 5 valid innings that do not end in a tie.
        const innings = inningsContainerInner.querySelectorAll('.inning-row');

        if (innings.length < 5) return false;
        else {
            let totalHomeRuns = 0;
            let totalRoadRuns = 0;

            innings.forEach(inning => {
                const homeRuns = inning.querySelector('[data-field="homeRuns"]').valueAsNumber;
                const roadRuns = inning.querySelector('[data-field="roadRuns"]').valueAsNumber;

                if (Number.isNaN(homeRuns) || Number.isNaN(roadRuns)) {
                    unhideElement(errorMissingInfo);

                    return false;
                } else {
                    totalHomeRuns += homeRuns;
                    totalRoadRuns += roadRuns;
                }
            });

            hideElement(errorMissingInfo);

            if (totalHomeRuns === totalRoadRuns) {
                unhideElement(errorTie);
                return false;
            } else {
                hideElement(errorTie);

                if (innings.length === 5) {
                    // If there are 5 valid innings, the score differential must be at least 10.
                    if (Math.abs(totalRoadRuns - totalRoadRuns) < 10) {
                        unhideElement(errorMercy);
                        return false;
                    } else hideElement(errorMercy);
                } else hideElement(errorMercy);
            }

            return true;
        }
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

// Event listener for btnSubmit button click to handle validation and determine whether to submit or not.
document.getElementById('btnSubmit').addEventListener('click', event => {
    // Prevent the default submit behaviour to allow for validation.
    event.preventDefault();

    // Make the sure the names are valid.
    const validNames = validateTeamNames();

    if (validNames) {
        // Don't need to check that date isn't empty because it is a required field on the form.

        // Check whether the date is in the past or not.
        const gamePlayed = gameHasBeenPlayed();

        if (!gamePlayed) {
            // The game hasn't happened yet, so submit the form.
            form.submit();
        } else {
            const forfeited = validateForfeit();

            if (forfeited) form.submit();
        }
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