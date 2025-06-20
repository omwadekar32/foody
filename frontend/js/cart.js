// Script for cart.html

document.addEventListener('DOMContentLoaded', () => {
    console.log('Cart page loaded');
    loadCartItems();
});

function loadCartItems() {
    // Placeholder: In a real app, fetch cart items from localStorage or backend API
    const cartItemsContainer = document.getElementById('cart-items-container');
    const cartTotalElement = document.getElementById('cart-total');
    if (!cartItemsContainer || !cartTotalElement) return;

    // Mock cart data (replace with actual data source)
    const cartItems = [
        { id: 1, dishName: 'Pizza Margherita', restaurantName: 'Restaurant Alpha', quantity: 1, price: 12.99 },
        { id: 2, dishName: 'Chicken Burger', restaurantName: 'Restaurant Beta', quantity: 2, price: 10.50 }
    ];

    cartItemsContainer.innerHTML = ''; // Clear placeholder items
    let total = 0;

    if (cartItems.length === 0) {
        cartItemsContainer.innerHTML = '<p>Your cart is empty.</p>';
        cartTotalElement.textContent = '0.00';
        return;
    }

    cartItems.forEach(item => {
        const itemTotal = item.quantity * item.price;
        total += itemTotal;

        const card = document.createElement('div');
        card.className = 'card mb-3';
        card.innerHTML = `
            <div class="card-body">
                <div class="d-flex justify-content-between">
                    <div>
                        <h5 class="card-title">${item.dishName}</h5>
                        <p class="card-text">Restaurant: ${item.restaurantName}</p>
                    </div>
                    <div>
                        <p>Quantity: <input type="number" value="${item.quantity}" min="1" class="form-control form-control-sm d-inline-block" style="width: 70px;" data-item-id="${item.id}" onchange="updateQuantity(${item.id}, this.value)"></p>
                        <p>Price: $${item.price.toFixed(2)}</p>
                        <p>Subtotal: $${itemTotal.toFixed(2)}</p>
                        <button class="btn btn-danger btn-sm" onclick="removeFromCart(${item.id})">Remove</button>
                    </div>
                </div>
            </div>
        `;
        cartItemsContainer.appendChild(card);
    });

    cartTotalElement.textContent = total.toFixed(2);
}

function updateQuantity(itemId, newQuantity) {
    // Placeholder: Update quantity in localStorage or backend
    console.log(`Updating quantity for item ID ${itemId} to ${newQuantity}`);
    // For now, just reload cart to reflect changes (mock behavior)
    // In a real app, you'd update the specific item and recalculate total more efficiently
    loadCartItems(); // This will re-render based on the "updated" mock data
}

function removeFromCart(itemId) {
    // Placeholder: Remove item from localStorage or backend
    console.log(`Removing item ID ${itemId} from cart`);
    // For now, just reload cart to reflect changes (mock behavior)
    // In a real app, you'd filter out the item and then call loadCartItems or update the DOM directly.
    alert(`Item ${itemId} would be removed. Reloading cart for demo.`);
    // This is a simplified mock. A real implementation would modify the cartItems array and re-render.
    loadCartItems();
}

// Example of how cart data might be stored (e.g., in localStorage)
// function getCartFromStorage() {
//     return JSON.parse(localStorage.getItem('cart')) || [];
// }

// function saveCartToStorage(cart) {
//     localStorage.setItem('cart', JSON.stringify(cart));
// }