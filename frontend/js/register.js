// Script for register.html

document.addEventListener('DOMContentLoaded', () => {
    console.log('Register page loaded');
    const registerForm = document.getElementById('register-form');

    if (registerForm) {
        registerForm.addEventListener('submit', function(event) {
            event.preventDefault();
            console.log('Register form submitted');

            const fullName = document.getElementById('fullName').value;
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;

            if (password !== confirmPassword) {
                alert('Passwords do not match!');
                return;
            }

            // Placeholder: In a real app, send this to the backend (POST /register)
            const registrationData = {
                fullName,
                email,
                password,
                role: 'customer' // Default role for new registrations
            };
            console.log('Registration attempt:', registrationData);

            // Mock registration logic
            // Replace with actual API call and response handling
            // For demo, assume registration is successful
            alert('Registration successful! (Mock)\nPlease login with your new account.');
            // Store user data (mock, in real app backend handles this)
            // localStorage.setItem('newUser', JSON.stringify(registrationData)); // Example
            window.location.href = 'login.html'; // Redirect to login page
        });
    }
});