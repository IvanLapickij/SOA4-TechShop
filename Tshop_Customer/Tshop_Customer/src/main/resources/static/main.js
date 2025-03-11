function fetchCustomerInfo() {
    const url = '/customers'; // Make sure your Spring Boot controller maps this route

    fetch(url, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Error getting data from the server.");
        }
        return response.json();
    })
    .then(data => {
        if (data) {
            populateTable(data);
        }
    })
    .catch(error => console.error('Error fetching customer info:', error));
}

// Function to populate the table with data
function populateTable(customers) {
    const tableBody = document.getElementById('customerTable');
    tableBody.innerHTML = ''; // Clear previous data

    customers.forEach(customer => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${customer.custId}</td>
            <td>${customer.custName}</td>
            <td>${customer.custBod}</td>
            <td>${customer.custPhone}</td>
        `;
        tableBody.appendChild(row);
    });
}

// When the DOM is fully loaded, fetch customer data
document.addEventListener("DOMContentLoaded", () => {
    fetchCustomerInfo();
});
