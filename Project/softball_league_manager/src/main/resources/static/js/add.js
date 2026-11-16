const homeTeamInput = document.getElementById('inputHomeTeam');
const roadTeamInput = document.getElementById('inputRoadTeam');

const forfeitSwitch = document.getElementById('switchForfeit');
const teamAtFaultSelect = document.getElementById('teamAtFault');

const inningsContainerInner = document.getElementById('inningsContainerInner');
const addInningButton = document.getElementById('btnAddInning');

function updateTeamAtFaultSelect() {
    teamAtFaultSelect.innerHTML = "";

    const fragment = document.createDocumentFragment();

    // Add a blank option.
    fragment.appendChild(new Option("", ""));

    const teamNames = [homeTeamInput.value.trim(), roadTeamInput.value.trim()];

    if (teamNames[0] === teamNames[1]) teamNames.pop()

    teamNames.forEach(option => {
        fragment.appendChild(new Option(option, option));
    });

    teamAtFaultSelect.appendChild(fragment);
}

/* EVENT LISTENERS */

homeTeamInput.addEventListener('change', updateTeamAtFaultSelect);
roadTeamInput.addEventListener('change', updateTeamAtFaultSelect);

// Listen to forfeit checkbox to enable/disable other inputs.
forfeitSwitch.addEventListener('change', (event) => {
    if (event.target.checked) {
        // If checked, disable innings and enable team at fault select.
        setInningsState(true);
        setForfeitState(false);
    } else {
        // If unchecked, disable team at fault select and enable innings.
        setInningsState(false);
        setForfeitState(true);
    }
});

function setInningsState(disabled) {
    const inningInputs = inningsContainerInner.querySelectorAll('.inning-row input[type="number"]');
    const buttons = inningsContainerInner.querySelectorAll('.remove-button');

    inningInputs.forEach(input => {
        input.disabled = disabled;
    });

    buttons.forEach(button => button.disabled = disabled);

    addInningButton.disabled = disabled;
}

function setForfeitState(disabled) {
    // If unchecked, disable and clear team at fault select and enable innings.
    teamAtFaultSelect.disabled = disabled;
    if (!disabled && teamAtFaultSelect.children.length === 0) updateTeamAtFaultSelect();
}

// Listen for a click to add a new inning row.
addInningButton.addEventListener('click', () => {
    console.log("Clicked add!");

    const templateInning = document.getElementById('templateInningRow');

    // Get a deep copy of the template inning.
    const newInning = templateInning.content.cloneNode(true);

    // Append the inning element to the inner innings' container.
    inningsContainerInner.appendChild(newInning);

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
    const innings = inningsContainerInner.querySelectorAll('.inning-row');

    innings.forEach((inning, index) => {
        // Set the inning number in the readonly text field. + 1 to account for zero-indexing.
        const inningNum = inning.querySelector('p[data-field="inningNumber"]');
        inningNum.textContent = (index + 1).toString();

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