// Handles the navbar collapse/expansion.
document.addEventListener('DOMContentLoaded', () => {
    const navBar = document.querySelector('aside');
    const toggleBtn = document.getElementById('nav-toggle-btn');

    toggleBtn.addEventListener('click', () => {
        navBar.classList.toggle('collapsed');
        toggleBtn.classList.toggle('rotated');
    });
});

/* UTILITY FUNCTIONS */

/**
 * Makes sure the 'hidden' class is applied to an element.
 * @param element The element to hide.
 * @author BML
 * @since 20261107
 */
function hideElement(element) {
    console.log(element);
    if (!element.classList.contains('hidden')) element.classList.add('hidden');
}

/**
 * Makes sure the 'hidden' class isn't applied to an element.
 * @param element The element to unhide.
 * @author BML
 * @since 20261107
 */
function unhideElement(element) {
    if (element.classList.contains('hidden')) element.classList.remove('hidden');
}

/**
 * Makes sure the 'invalid' class is applied to an element.
 * @param element The element to flag as invalid.
 * @author BML
 * @since 20261107
 */
function applyInvalid(element) {
    if (!element.classList.contains('invalid')) element.classList.add('invalid');
}

/**
 * Makes sure the 'invalid' class isn't applied to an element.
 * @param element The element to remove the invalid class from.
 * @author BML
 * @since 20261107
 */
function removeInvalid(element) {
    if (element.classList.contains('invalid')) element.classList.remove('invalid');
}