const toggleButtons = document.querySelectorAll('.button.toggle');

// Event listener to catch clicks on the toggle buttons to switch display state.
document.querySelectorAll('.button.toggle').forEach(toggle => {
    toggle.addEventListener('click', (event) => {
        // Check if the clicked button already has the active class on it. If it does, do nothing.
        if (!event.target.classList.contains('active')) {
            // Search the other toggle buttons for the active class and remove it.
            toggleButtons.forEach(btn => {
                if (btn.classList.contains('active')) btn.classList.remove('active');

                // Add the hidden class to the related element.
                const viewID = btn.getAttribute('data-toggle');
                document.getElementById(viewID).classList.add('hidden');
            });

            // Add the active class to this button.
            event.target.classList.add('active');

            // Remove the hidden class from the div related to this button.
            const activeViewID = event.target.getAttribute('data-toggle');
            document.getElementById(activeViewID).classList.remove('hidden');
        }
    });
});