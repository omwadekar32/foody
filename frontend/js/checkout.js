// Script for checkout.html

document.addEventListener('DOMContentLoaded', () => {
    console.log('Checkout page loaded');
    loadOrderSummary();
    setupFormSubmission();
});

function loadOrderSummary() {
    // Placeholder: In a real app, get cart items from localStorage or a global state
    const orderSummaryContainer = document.getElementById('order-summary-container');
    const checkoutTotalElement = document.getElementById('checkout-total');

    if (!orderSummaryContainer || !checkoutTotalElement) return;

    // Mock cart data (same as cart.js for consistency in this example)
    const cartItems = [
        { id: 1, dishName: 'Pizza Margherita', restaurantName: 'Restaurant Alpha', quantity: 1, price: 12.99 },
        { id: 2, dishName: 'Chicken Burger', restaurantName: 'Restaurant Beta', quantity: 2, price: 10.50 }
    ];

    orderSummaryContainer.innerHTML = ''; // Clear placeholder items
    let total = 0;

    if (cartItems.length === 0) {
        orderSummaryContainer.innerHTML = '<p>Your cart is empty. Cannot proceed to checkout.</p>';
        checkoutTotalElement.textContent = '0.00';
        // Optionally disable the form or place order button
        const placeOrderButton = document.querySelector('#checkout-form button[type="submit"]');
        if(placeOrderButton) placeOrderButton.disabled = true;
        return;
    }

    cartItems.forEach(item => {
        const itemTotal = item.quantity * item.price;
        total += itemTotal;

        const itemDiv = document.createElement('div');
        itemDiv.className = 'd-flex justify-content-between py-2 border-bottom';
        itemDiv.innerHTML = `
            <span>${item.dishName} (x${item.quantity})</span>
            <span>$${itemTotal.toFixed(2)}</span>
        `;
        orderSummaryContainer.appendChild(itemDiv);
    });

    checkoutTotalElement.textContent = `$${total.toFixed(2)}`;
}

function setupFormSubmission() {
    const checkoutForm = document.getElementById('checkout-form');
    if (checkoutForm) {
        checkoutForm.addEventListener('submit', function(event) {
            event.preventDefault();
            console.log('Checkout form submitted');

            // Collect form data
            const formData = {
                fullName: document.getElementById('fullName').value,
                address: document.getElementById('address').value,
                city: document.getElementById('city').value,
                postalCode: document.getElementById('postalCode').value,
                phone: document.getElementById('phone').value,
                // Mock payment details
                cardNumber: document.getElementById('cardNumber').value,
                expiryDate: document.getElementById('expiryDate').value,
                cvv: document.getElementById('cvv').value,
            };

            // Placeholder: In a real app, send this data to the backend (POST /orders)
            console.log('Order details:', formData);
            // Simulate API call
            alert('Order placed successfully! (Mock)');

            // Clear cart (mock)
            // localStorage.removeItem('cart'); // If using localStorage for cart

            // Redirect to order confirmation page
            window.location.href = 'order_confirmation.html';
        });
    }
}