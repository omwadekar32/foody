// Script for order_confirmation.html

document.addEventListener('DOMContentLoaded', () => {
    console.log('Order confirmation page loaded');

    // Placeholder: In a real app, you might get the order ID from URL parameters or session storage
    const orderIdElement = document.getElementById('order-id');
    if (orderIdElement) {
        // Example: const params = new URLSearchParams(window.location.search);
        // const actualOrderId = params.get('orderId');
        // orderIdElement.textContent = actualOrderId || '#XYZ789ABC'; // Display actual or a default
        orderIdElement.textContent = `#${generateMockOrderId()}`;
    }

    const viewOrderHistoryLink = document.getElementById('view-order-history-link');
    if (viewOrderHistoryLink) {
        viewOrderHistoryLink.addEventListener('click', (event) => {
            event.preventDefault();
            // Placeholder: Redirect to user's order history page
            // This page is not yet created, so for now it can be a console log or alert.
            console.log('Redirecting to order history page (not implemented yet).');
            alert('Order history page is not yet implemented.');
            // window.location.href = 'order_history.html'; // Uncomment when page exists
        });
    }
});

function generateMockOrderId() {
    // Generates a simple mock order ID
    const prefix = "ORD";
    const randomNumber = Math.floor(Math.random() * 1000000);
    const timestamp = Date.now().toString().slice(-4);
    return `${prefix}${randomNumber}${timestamp}`;
}