// Handles the navbar collapse/expansion.
document.addEventListener('DOMContentLoaded', () => {
    const navBar = document.querySelector('aside');
    const toggleBtn = document.getElementById('nav-toggle-btn');

    toggleBtn.addEventListener('click', () => {
        navBar.classList.toggle('collapsed');
        toggleBtn.classList.toggle('rotated');
    });
});