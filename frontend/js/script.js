// Basic script file for frontend logic

document.addEventListener('DOMContentLoaded', () => {
    console.log('DOM fully loaded and parsed');

    // Example: Fetch restaurants (replace with actual API call)
    // fetchRestaurants();
});

// function fetchRestaurants() {
//     // This is a placeholder. In a real application, you would fetch data from your backend API.
//     const restaurantList = document.getElementById('restaurant-list');
//     if (restaurantList) {
//         // Clear existing content (e.g., placeholder cards)
//         // restaurantList.innerHTML = ''; // Uncomment if you want to clear static cards from HTML

//         // Example restaurant data
//         const restaurants = [
//             { id: 1, name: 'Restaurant Alpha', cuisine: 'Italian', location: 'Downtown', rating: 4.5, image_url: 'https://via.placeholder.com/300x200' },
//             { id: 2, name: 'Restaurant Beta', cuisine: 'Mexican', location: 'Uptown', rating: 4.2, image_url: 'https://via.placeholder.com/300x200' },
//             { id: 3, name: 'Restaurant Gamma', cuisine: 'Indian', location: 'Midtown', rating: 4.7, image_url: 'https://via.placeholder.com/300x200' }
//         ];

//         restaurants.forEach(restaurant => {
//             const col = document.createElement('div');
//             col.className = 'col-md-4 mb-4';

//             const card = document.createElement('div');
//             card.className = 'card';

//             const img = document.createElement('img');
//             img.src = restaurant.image_url;
//             img.className = 'card-img-top';
//             img.alt = restaurant.name;

//             const cardBody = document.createElement('div');
//             cardBody.className = 'card-body';

//             const title = document.createElement('h5');
//             title.className = 'card-title';
//             title.textContent = restaurant.name;

//             const cuisine = document.createElement('p');
//             cuisine.className = 'card-text';
//             cuisine.textContent = `Cuisine: ${restaurant.cuisine}`;

//             const location = document.createElement('p');
//             location.className = 'card-text';
//             location.textContent = `Location: ${restaurant.location}`;

//             const rating = document.createElement('p');
//             rating.className = 'card-text';
//             rating.textContent = `Rating: ${restaurant.rating}`;

//             const link = document.createElement('a');
//             link.href = `restaurant_detail.html?id=${restaurant.id}`;
//             link.className = 'btn btn-primary';
//             link.textContent = 'View Menu';

//             cardBody.appendChild(title);
//             cardBody.appendChild(cuisine);
//             cardBody.appendChild(location);
//             cardBody.appendChild(rating);
//             cardBody.appendChild(link);

//             card.appendChild(img);
//             card.appendChild(cardBody);

//             col.appendChild(card);
//             // restaurantList.appendChild(col); // Uncomment if you want to dynamically add cards
//         });
//     }
// }