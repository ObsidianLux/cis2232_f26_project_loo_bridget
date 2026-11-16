const toggleButtons = Array.from(document.getElementById('scheduleButtons').children);
const toggleViews = document.getElementById('schedule').querySelectorAll('article');

document.addEventListener('DOMContentLoaded', () => {
    const savedView = localStorage.getItem('activeViewId') || 'scheduleUpcomingGames';
    activateView(savedView);
});

// Event listener to catch clicks on the toggle buttons to switch display state.
toggleButtons.forEach(toggle => {
    toggle.addEventListener('click', () => activateView(toggle.dataset.target));
});

function activateView(targetID) {
    // Update the buttons.
    toggleButtons.forEach(btn => btn.classList.toggle('active', btn.dataset.target === targetID));

    // Activate the correct view and hide the rest.
    toggleViews.forEach(view => view.classList.toggle('hidden', view.id !== targetID));

    // Save the current view in the browser's local storage.
    localStorage.setItem('activeViewId', targetID);
}