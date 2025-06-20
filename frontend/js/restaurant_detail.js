// Script for restaurant_detail.html

document.addEventListener('DOMContentLoaded', () => {
    console.log('Restaurant detail page loaded');
    const params = new URLSearchParams(window.location.search);
    const restaurantId = params.get('id');

    if (restaurantId) {
        fetchRestaurantDetails(restaurantId);
        fetchMenuItems(restaurantId);
    } else {
        // Handle case where no ID is provided, maybe redirect or show an error
        console.error('No restaurant ID found in URL');
        const container = document.querySelector('.container.mt-4');
        if (container) {
            container.innerHTML = '<p class="text-danger">Error: Restaurant ID not provided.</p>';
        }
    }
});

function fetchRestaurantDetails(id) {
    // Placeholder: In a real app, fetch from API: GET /restaurants/{id}
    console.log(`Fetching details for restaurant ID: ${id}`);
    // Mock data
    const restaurantData = {
        name: `Awesome Restaurant ${id}`,
        location: '123 Main St, Food City',
        cuisine: 'Mixed Cuisine',
        rating: '4.7',
        image_url: 'https://via.placeholder.com/800x300'
    };

    document.getElementById('restaurant-name').textContent = restaurantData.name;
    document.getElementById('restaurant-location').textContent = `Location: ${restaurantData.location}`;
    document.getElementById('restaurant-cuisine').textContent = `Cuisine: ${restaurantData.cuisine}`;
    document.getElementById('restaurant-rating').textContent = `Rating: ${restaurantData.rating}`;
    document.getElementById('restaurant-image').src = restaurantData.image_url;
    document.getElementById('restaurant-image').alt = restaurantData.name;
}

function fetchMenuItems(restaurantId) {
    // Placeholder: In a real app, fetch from API: GET /dishes?restaurantId={id}
    console.log(`Fetching menu items for restaurant ID: ${restaurantId}`);
    const menuItemsList = document.getElementById('menu-items-list');
    if (!menuItemsList) return;

    // Mock data
    const menuItems = [
        { id: 1, name: 'Pizza Margherita', description: 'Classic cheese and tomato pizza.', price: 12.99, image_url: 'https://via.placeholder.com/150' },
        { id: 2, name: 'Chicken Burger', description: 'Grilled chicken patty with lettuce and cheese.', price: 10.50, image_url: 'https://via.placeholder.com/150' },
        { id: 3, name: 'Pasta Carbonara', description: 'Creamy pasta with bacon and egg.', price: 14.00, image_url: 'https://via.placeholder.com/150' },
        { id: 4, name: 'Caesar Salad', description: 'Fresh salad with Caesar dressing.', price: 8.75, image_url: 'https://via.placeholder.com/150' }
    ];

    menuItemsList.innerHTML = ''; // Clear placeholder items

    menuItems.forEach(item => {
        const col = document.createElement('div');
        col.className = 'col-md-6 mb-4';

        const card = document.createElement('div');
        card.className = 'card';

        const row = document.createElement('div');
        row.className = 'row g-0';

        const imgCol = document.createElement('div');
        imgCol.className = 'col-md-4';
        const img = document.createElement('img');
        img.src = item.image_url;
        img.className = 'img-fluid rounded-start';
        img.alt = item.name;
        imgCol.appendChild(img);

        const bodyCol = document.createElement('div');
        bodyCol.className = 'col-md-8';

        const cardBody = document.createElement('div');
        cardBody.className = 'card-body';

        const title = document.createElement('h5');
        title.className = 'card-title';
        title.textContent = item.name;

        const description = document.createElement('p');
        description.className = 'card-text';
        description.textContent = item.description;

        const price = document.createElement('p');
        price.className = 'card-text';
        price.innerHTML = `<strong>Price: $${item.price.toFixed(2)}</strong>`;

        const addButton = document.createElement('button');
        addButton.className = 'btn btn-success';
        addButton.textContent = 'Add to Cart';
        addButton.onclick = () => addToCart(item); // Example function call

        cardBody.appendChild(title);
        cardBody.appendChild(description);
        cardBody.appendChild(price);
        cardBody.appendChild(addButton);

        bodyCol.appendChild(cardBody);
        row.appendChild(imgCol);
        row.appendChild(bodyCol);
        card.appendChild(row);
        col.appendChild(card);
        menuItemsList.appendChild(col);
    });
}

function addToCart(item) {
    // Placeholder for Add to Cart functionality
    // In a real app, this would interact with a cart state (e.g., localStorage or backend API POST /cart)
    console.log('Added to cart:', item);
    alert(`${item.name} added to cart!`);
}