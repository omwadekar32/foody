// Script for admin.html

document.addEventListener('DOMContentLoaded', () => {
    console.log('Admin dashboard script loaded.');

    // Sidebar toggle
    const menuToggle = document.getElementById('menu-toggle');
    if (menuToggle) {
        menuToggle.addEventListener('click', (e) => {
            e.preventDefault();
            document.getElementById('wrapper').classList.toggle('toggled');
        });
    }

    // Tab navigation persistence (optional, using localStorage)
    const sidebarLinks = document.querySelectorAll('#sidebar-wrapper .list-group-item[data-bs-toggle="tab"]');
    sidebarLinks.forEach(link => {
        link.addEventListener('shown.bs.tab', event => {
            localStorage.setItem('lastAdminTab', event.target.getAttribute('href'));
        });
    });

    const lastTab = localStorage.getItem('lastAdminTab');
    if (lastTab) {
        const lastTabLink = document.querySelector(`#sidebar-wrapper .list-group-item[href="${lastTab}"]`);
        if (lastTabLink) {
            new bootstrap.Tab(lastTabLink).show();
        }
    }


    // --- Mock Data & Functions ---
    // In a real application, these would be API calls.

    // Restaurants
    const mockRestaurants = [
        { id: 1, name: 'Pizza Place', location: 'Downtown', cuisine: 'Italian', rating: 4.5, image_url: 'https://via.placeholder.com/100' },
        { id: 2, name: 'Taco Town', location: 'Uptown', cuisine: 'Mexican', rating: 4.2, image_url: 'https://via.placeholder.com/100' },
        { id: 3, name: 'Sushi Spot', location: 'Midtown', cuisine: 'Japanese', rating: 4.8, image_url: 'https://via.placeholder.com/100' }
    ];

    function renderRestaurants() {
        const listElement = document.getElementById('admin-restaurant-list');
        const selectForMenu = document.getElementById('restaurant-select-for-menu');
        if (!listElement) return;
        listElement.innerHTML = '';
        if (selectForMenu) selectForMenu.innerHTML = '<option selected disabled>Select a restaurant</option>';

        mockRestaurants.forEach(r => {
            const row = `<tr>
                <td>${r.id}</td>
                <td>${r.name}</td>
                <td>${r.location}</td>
                <td>${r.cuisine}</td>
                <td>${r.rating}</td>
                <td>
                    <button class="btn btn-sm btn-warning" data-bs-toggle="modal" data-bs-target="#editRestaurantModal" onclick="populateEditRestaurantForm(${r.id})"><i class="bi bi-pencil-square"></i></button>
                    <button class="btn btn-sm btn-danger" onclick="deleteRestaurant(${r.id})"><i class="bi bi-trash"></i></button>
                </td>
            </tr>`;
            listElement.insertAdjacentHTML('beforeend', row);
            if (selectForMenu) {
                selectForMenu.insertAdjacentHTML('beforeend', `<option value="${r.id}">${r.name}</option>`);
            }
        });
        updateDashboardStats();
    }

    window.populateEditRestaurantForm = (id) => {
        const restaurant = mockRestaurants.find(r => r.id === id);
        if (restaurant) {
            document.getElementById('edit-restaurant-id').value = restaurant.id;
            document.getElementById('edit-restaurant-name').value = restaurant.name;
            document.getElementById('edit-restaurant-location').value = restaurant.location;
            document.getElementById('edit-restaurant-cuisine').value = restaurant.cuisine;
            document.getElementById('edit-restaurant-image_url').value = restaurant.image_url || '';
            document.getElementById('edit-restaurant-rating').value = restaurant.rating || '';
        }
    };

    const addRestaurantForm = document.getElementById('add-restaurant-form');
    if (addRestaurantForm) {
        addRestaurantForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const newRestaurant = {
                id: mockRestaurants.length > 0 ? Math.max(...mockRestaurants.map(r => r.id)) + 1 : 1,
                name: document.getElementById('add-restaurant-name').value,
                location: document.getElementById('add-restaurant-location').value,
                cuisine: document.getElementById('add-restaurant-cuisine').value,
                image_url: document.getElementById('add-restaurant-image_url').value,
                rating: parseFloat(document.getElementById('add-restaurant-rating').value)
            };
            mockRestaurants.push(newRestaurant);
            renderRestaurants();
            bootstrap.Modal.getInstance(document.getElementById('addRestaurantModal')).hide();
            addRestaurantForm.reset();
            console.log('Added restaurant:', newRestaurant);
        });
    }

    const editRestaurantForm = document.getElementById('edit-restaurant-form');
    if (editRestaurantForm) {
        editRestaurantForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const id = parseInt(document.getElementById('edit-restaurant-id').value);
            const index = mockRestaurants.findIndex(r => r.id === id);
            if (index > -1) {
                mockRestaurants[index] = {
                    id: id,
                    name: document.getElementById('edit-restaurant-name').value,
                    location: document.getElementById('edit-restaurant-location').value,
                    cuisine: document.getElementById('edit-restaurant-cuisine').value,
                    image_url: document.getElementById('edit-restaurant-image_url').value,
                    rating: parseFloat(document.getElementById('edit-restaurant-rating').value)
                };
                renderRestaurants();
                bootstrap.Modal.getInstance(document.getElementById('editRestaurantModal')).hide();
                console.log('Edited restaurant:', mockRestaurants[index]);
            }
        });
    }

    window.deleteRestaurant = (id) => {
        if (confirm(`Are you sure you want to delete restaurant ID ${id}? This might affect its menu items.`)) {
            const index = mockRestaurants.findIndex(r => r.id === id);
            if (index > -1) {
                mockRestaurants.splice(index, 1);
                // Also delete associated menu items (mock behavior)
                mockMenuItems = mockMenuItems.filter(item => item.restaurantId !== id);
                renderRestaurants();
                renderMenuItems(); // Re-render menu items as some might be deleted
                console.log('Deleted restaurant ID:', id);
            }
        }
    };

    // Menu Items
    let mockMenuItems = [
        { id: 101, restaurantId: 1, name: 'Margherita Pizza', price: 12.99, description: 'Classic cheese and tomato', category: 'Pizza', image_url: 'https://via.placeholder.com/100' },
        { id: 102, restaurantId: 1, name: 'Pepperoni Pizza', price: 14.50, description: 'Cheese, tomato, and pepperoni', category: 'Pizza', image_url: 'https://via.placeholder.com/100' },
        { id: 201, restaurantId: 2, name: 'Chicken Taco', price: 3.50, description: 'Grilled chicken in a soft tortilla', category: 'Tacos', image_url: 'https://via.placeholder.com/100' },
    ];

    function renderMenuItems(restaurantId = null) {
        const listElement = document.getElementById('admin-menu-item-list');
        if (!listElement) return;
        listElement.innerHTML = '';
        const selectedRestaurantId = restaurantId || parseInt(document.getElementById('restaurant-select-for-menu')?.value);

        const itemsToShow = selectedRestaurantId ? mockMenuItems.filter(item => item.restaurantId === selectedRestaurantId) : [];

        itemsToShow.forEach(item => {
            const row = `<tr>
                <td>${item.id}</td>
                <td>${item.name}</td>
                <td>$${item.price.toFixed(2)}</td>
                <td>${item.description}</td>
                <td>${item.category}</td>
                <td>
                    <button class="btn btn-sm btn-warning" data-bs-toggle="modal" data-bs-target="#editMenuItemModal" onclick="populateEditMenuItemForm(${item.id})"><i class="bi bi-pencil-square"></i></button>
                    <button class="btn btn-sm btn-danger" onclick="deleteMenuItem(${item.id})"><i class="bi bi-trash"></i></button>
                </td>
            </tr>`;
            listElement.insertAdjacentHTML('beforeend', row);
        });
    }
    
    const restaurantSelectForMenu = document.getElementById('restaurant-select-for-menu');
    if(restaurantSelectForMenu) {
        restaurantSelectForMenu.addEventListener('change', (e) => {
            renderMenuItems(parseInt(e.target.value));
        });
    }


    window.populateEditMenuItemForm = (id) => {
        const item = mockMenuItems.find(m => m.id === id);
        if (item) {
            document.getElementById('edit-menu-item-id').value = item.id;
            document.getElementById('edit-menu-item-restaurant-id').value = item.restaurantId; // Keep track of its restaurant
            document.getElementById('edit-menu-item-name').value = item.name;
            document.getElementById('edit-menu-item-price').value = item.price;
            document.getElementById('edit-menu-item-description').value = item.description || '';
            document.getElementById('edit-menu-item-category').value = item.category || '';
            document.getElementById('edit-menu-item-image_url').value = item.image_url || '';
        }
    };
    
    const addMenuItemForm = document.getElementById('add-menu-item-form');
    if(addMenuItemForm){
        addMenuItemForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const selectedRestaurantId = parseInt(document.getElementById('restaurant-select-for-menu').value);
            if(!selectedRestaurantId || isNaN(selectedRestaurantId)){
                alert("Please select a restaurant first.");
                return;
            }
            const newItem = {
                id: mockMenuItems.length > 0 ? Math.max(...mockMenuItems.map(item => item.id)) + 1 : 1,
                restaurantId: selectedRestaurantId,
                name: document.getElementById('add-menu-item-name').value,
                price: parseFloat(document.getElementById('add-menu-item-price').value),
                description: document.getElementById('add-menu-item-description').value,
                category: document.getElementById('add-menu-item-category').value,
                image_url: document.getElementById('add-menu-item-image_url').value
            };
            mockMenuItems.push(newItem);
            renderMenuItems(selectedRestaurantId);
            bootstrap.Modal.getInstance(document.getElementById('addMenuItemModal')).hide();
            addMenuItemForm.reset();
            console.log('Added menu item:', newItem);
        });
    }

    const editMenuItemForm = document.getElementById('edit-menu-item-form');
    if(editMenuItemForm){
        editMenuItemForm.addEventListener('submit', (e) => {
            e.preventDefault();
            const id = parseInt(document.getElementById('edit-menu-item-id').value);
            const restaurantId = parseInt(document.getElementById('edit-menu-item-restaurant-id').value);
            const index = mockMenuItems.findIndex(item => item.id === id);
            if(index > -1){
                mockMenuItems[index] = {
                    id: id,
                    restaurantId: restaurantId, // Preserve original restaurant ID
                    name: document.getElementById('edit-menu-item-name').value,
                    price: parseFloat(document.getElementById('edit-menu-item-price').value),
                    description: document.getElementById('edit-menu-item-description').value,
                    category: document.getElementById('edit-menu-item-category').value,
                    image_url: document.getElementById('edit-menu-item-image_url').value
                };
                renderMenuItems(restaurantId); // Re-render for the correct restaurant
                bootstrap.Modal.getInstance(document.getElementById('editMenuItemModal')).hide();
                console.log('Edited menu item:', mockMenuItems[index]);
            }
        });
    }


    window.deleteMenuItem = (id) => {
        if (confirm(`Are you sure you want to delete menu item ID ${id}?`)) {
            const index = mockMenuItems.findIndex(item => item.id === id);
            if (index > -1) {
                const restaurantId = mockMenuItems[index].restaurantId;
                mockMenuItems.splice(index, 1);
                renderMenuItems(restaurantId); // Re-render for the correct restaurant
                console.log('Deleted menu item ID:', id);
            }
        }
    };


    // Orders
    const mockOrders = [
        { orderId: 'ORD001', userId: 'USR101', customerName: 'John Doe', shippingAddress: '123 Oak St, Anytown', orderDate: '2025-06-20', status: 'Preparing', totalPrice: 25.50, items: [{name: 'Pizza', qty: 1, price: 15.00}, {name: 'Coke', qty: 2, price: 5.25}] },
        { orderId: 'ORD002', userId: 'USR102', customerName: 'Jane Smith', shippingAddress: '456 Pine St, Otherville', orderDate: '2025-06-19', status: 'Delivered', totalPrice: 18.75, items: [{name: 'Burger', qty: 1, price: 12.00}, {name: 'Fries', qty: 1, price: 6.75}] },
    ];

    function renderOrders() {
        const listElement = document.getElementById('admin-order-list');
        if (!listElement) return;
        listElement.innerHTML = '';
        mockOrders.forEach(order => {
            const statusBadge = getStatusBadge(order.status);
            const row = `<tr>
                <td>${order.orderId}</td>
                <td>${order.userId}</td>
                <td>${order.orderDate}</td>
                <td>${statusBadge}</td>
                <td>$${order.totalPrice.toFixed(2)}</td>
                <td>
                    <button class="btn btn-sm btn-info" data-bs-toggle="modal" data-bs-target="#viewOrderModal" onclick="populateViewOrderModal('${order.orderId}')"><i class="bi bi-eye"></i></button>
                    <select class="form-select form-select-sm d-inline-block w-auto ms-2" onchange="updateOrderStatus('${order.orderId}', this.value)">
                        <option value="Pending" ${order.status === 'Pending' ? 'selected' : ''}>Pending</option>
                        <option value="Preparing" ${order.status === 'Preparing' ? 'selected' : ''}>Preparing</option>
                        <option value="Out for Delivery" ${order.status === 'Out for Delivery' ? 'selected' : ''}>Out for Delivery</option>
                        <option value="Delivered" ${order.status === 'Delivered' ? 'selected' : ''}>Delivered</option>
                        <option value="Cancelled" ${order.status === 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                    </select>
                </td>
            </tr>`;
            listElement.insertAdjacentHTML('beforeend', row);
        });
        updateDashboardStats();
    }
    
    window.populateViewOrderModal = (orderId) => {
        const order = mockOrders.find(o => o.orderId === orderId);
        if(order){
            document.getElementById('modal-order-id').textContent = order.orderId;
            document.getElementById('modal-customer-name').textContent = order.customerName || 'N/A';
            document.getElementById('modal-shipping-address').textContent = order.shippingAddress || 'N/A';
            document.getElementById('modal-order-date').textContent = order.orderDate;
            document.getElementById('modal-order-status').innerHTML = getStatusBadge(order.status);
            document.getElementById('modal-order-total').textContent = `$${order.totalPrice.toFixed(2)}`;
            
            const itemsList = document.getElementById('modal-order-items-list');
            itemsList.innerHTML = '';
            order.items.forEach(item => {
                itemsList.insertAdjacentHTML('beforeend', `<li>${item.name} (x${item.qty}) - $${(item.price * item.qty).toFixed(2)}</li>`);
            });
        }
    };

    window.updateOrderStatus = (orderId, newStatus) => {
        const order = mockOrders.find(o => o.orderId === orderId);
        if (order) {
            order.status = newStatus;
            renderOrders(); // Re-render to show updated status
            console.log(`Order ${orderId} status updated to ${newStatus}`);
        }
    };

    function getStatusBadge(status) {
        let badgeClass = 'bg-secondary';
        switch (status) {
            case 'Pending': badgeClass = 'bg-light text-dark border'; break;
            case 'Preparing': badgeClass = 'bg-warning text-dark'; break;
            case 'Out for Delivery': badgeClass = 'bg-info text-dark'; break;
            case 'Delivered': badgeClass = 'bg-success'; break;
            case 'Cancelled': badgeClass = 'bg-danger'; break;
        }
        return `<span class="badge ${badgeClass}">${status}</span>`;
    }

    // Users
    const mockUsers = [
        { userId: 'USR101', name: 'John Doe', email: 'john.doe@example.com', role: 'Customer' },
        { userId: 'USR102', name: 'Jane Smith', email: 'jane.smith@example.com', role: 'Customer' },
        { userId: 'ADM001', name: 'Admin User', email: 'admin@example.com', role: 'Admin' },
    ];

    function renderUsers() {
        const listElement = document.getElementById('admin-user-list');
        if (!listElement) return;
        listElement.innerHTML = '';
        mockUsers.forEach(user => {
            const row = `<tr>
                <td>${user.userId}</td>
                <td>${user.name}</td>
                <td>${user.email}</td>
                <td><span class="badge ${user.role === 'Admin' ? 'bg-primary' : 'bg-secondary'}">${user.role}</span></td>
                <td>
                    ${user.role !== 'Admin' ? `<button class="btn btn-sm btn-danger" onclick="deleteUser('${user.userId}')"><i class="bi bi-trash"></i></button>` : ''}
                </td>
            </tr>`;
            listElement.insertAdjacentHTML('beforeend', row);
        });
        updateDashboardStats();
    }

    window.deleteUser = (userId) => {
        if (confirm(`Are you sure you want to delete user ${userId}?`)) {
             const index = mockUsers.findIndex(u => u.userId === userId);
            if (index > -1 && mockUsers[index].role !== 'Admin') { // Prevent deleting admin for demo
                mockUsers.splice(index, 1);
                renderUsers();
                console.log('Deleted user ID:', userId);
            } else if (mockUsers[index].role === 'Admin'){
                alert("Admin users cannot be deleted through this interface.");
            }
        }
    };
    
    // Dashboard Stats
    function updateDashboardStats() {
        const totalRestaurantsEl = document.getElementById('total-restaurants');
        const totalOrdersEl = document.getElementById('total-orders');
        const totalUsersEl = document.getElementById('total-users');

        if(totalRestaurantsEl) totalRestaurantsEl.textContent = mockRestaurants.length;
        if(totalOrdersEl) totalOrdersEl.textContent = mockOrders.length;
        if(totalUsersEl) totalUsersEl.textContent = mockUsers.length;
    }


    // Initial renders
    renderRestaurants();
    renderMenuItems(); // Initially might be empty or show for first restaurant if one is pre-selected
    renderOrders();
    renderUsers();
    updateDashboardStats(); // Initial call for dashboard stats
});