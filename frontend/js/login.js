// Script for login.html

document.addEventListener('DOMContentLoaded', () => {
    console.log('Login page loaded');
    const loginForm = document.getElementById('login-form');

    if (loginForm) {
        loginForm.addEventListener('submit', function(event) {
            event.preventDefault();
            console.log('Login form submitted');

            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            const rememberMe = document.getElementById('rememberMe').checked;

            // Placeholder: In a real app, send this to the backend (POST /login)
            console.log('Login attempt:', { email, password, rememberMe });

            // Mock login logic
            // Replace with actual API call and response handling
            if (email === "admin@example.com" && password === "admin123") {
                alert('Admin login successful! (Mock)');
                // Store token/session info (mock)
                localStorage.setItem('userToken', 'fakeAdminToken');
                localStorage.setItem('userRole', 'admin');
                window.location.href = 'admin.html'; // Redirect to admin dashboard
            } else if (email === "user@example.com" && password === "user123") {
                alert('Customer login successful! (Mock)');
                // Store token/session info (mock)
                localStorage.setItem('userToken', 'fakeUserToken');
                localStorage.setItem('userRole', 'customer');
                window.location.href = 'index.html'; // Redirect to home page
            } else {
                alert('Invalid email or password. (Mock)');
            }
        });
    }
});