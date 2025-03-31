// main.js

// Global variable to store the last known ETag
let cachedEtag = "";

// -----------------------------
// Refresh/GET Customers with Orders
// -----------------------------
function refreshCustomers() {
  fetch("/customers/techshop", {
    headers: {
      "If-None-Match": cachedEtag
    }
  })
    .then(response => {
      // Update status code and ETag display
      document.getElementById("statusCode").textContent = response.status;
      const newEtag = response.headers.get("ETag");
      document.getElementById("etagValue").textContent = newEtag || "-";

      // If the response is 304 (Not Modified), notify the user and stop processing
      if (response.status === 304) {
        toastr.info('No new data available (304 Not Modified).', 'Refresh');
        return null;
      }
      if (newEtag) {
        cachedEtag = newEtag;
      }
      toastr.success('Data refreshed successfully.', 'Refresh');
      return response.json();
    })
    .then(data => {
      if (data) populateTable(data);
    })
    .catch(error => {
      toastr.error('Error refreshing data.', 'Refresh');
      console.error("Error:", error);
    });
}

function populateTable(customerResponses) {
  const tableBody = document.getElementById("customerTable");
  tableBody.innerHTML = "";
  customerResponses.forEach(cr => {
    if (cr.orders && cr.orders.length > 0) {
      // Loop over each order for this customer and add a row
      cr.orders.forEach(order => {
        const row = document.createElement("tr");
        row.innerHTML = `
          <td>${cr.custId}</td>
          <td>${cr.custName}</td>
          <td>${cr.custBod}</td>
          <td>${cr.custPhone}</td>
          <td>${order.orderId}</td>
          <td>${order.items}</td>
          <td>${order.price}</td>
        `;
        tableBody.appendChild(row);
      });
    } else {
      // If the customer has no orders, add a single row with placeholder data.
      const row = document.createElement("tr");
      row.innerHTML = `
          <td>${cr.custId}</td>
          <td>${cr.custName}</td>
          <td>${cr.custBod}</td>
          <td>${cr.custPhone}</td>
          <td>-</td>
          <td>No Orders</td>
          <td>-</td>
      `;
      tableBody.appendChild(row);
    }
  });
}

// -----------------------------
// 2) POST: Add a New Customer
// -----------------------------
function addCustomer() {
  const name = document.getElementById("newCustomerName").value;
  const birthDate = document.getElementById("newCustomerBirthDate").value;
  const phone = document.getElementById("newCustomerPhone").value;

  const newCustomer = {
    custName: name,
    custBod: birthDate,
    custPhone: phone
  };

  fetch("/customers", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(newCustomer)
  })
    .then(response => {
      if (!response.ok) {
        throw new Error("Failed to add customer. Status: " + response.status);
      }
      document.getElementById("addStatus").textContent = `(Status: ${response.status})`;
      toastr.success('Customer added successfully!', 'Add');
      refreshCustomers();
    })
    .catch(error => {
      document.getElementById("addStatus").textContent = "(Error adding customer)";
      toastr.error('Error adding customer.', 'Add');
      console.error(error);
    });
}

// -----------------------------
// 3) PUT: Edit a Customer
// -----------------------------
function editCustomer() {
  const id = document.getElementById("editCustomerId").value;
  const name = document.getElementById("editCustomerName").value;
  const birth = document.getElementById("editCustomerBirthDate").value;
  const phone = document.getElementById("editCustomerPhone").value;

  if (!id) {
    alert("Please enter a valid Customer ID to edit!");
    return;
  }

  const updatedCustomer = {
    custName: name,
    custBod: birth,
    custPhone: phone
  };

  fetch(`/customers/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(updatedCustomer)
  })
    .then(response => {
      if (!response.ok) {
        throw new Error("Failed to update customer. Status: " + response.status);
      }
      document.getElementById("editStatus").textContent = `(Status: ${response.status})`;
      toastr.success('Customer updated successfully!', 'Edit');
      return response.json();
    })
    .then(data => {
      console.log("Updated customer:", data);
      refreshCustomers();
    })
    .catch(error => {
      document.getElementById("editStatus").textContent = "(Error editing customer)";
      toastr.error('Error editing customer.', 'Edit');
      console.error(error);
    });
}

// -----------------------------
// 4) DELETE: Remove a Customer
// -----------------------------
function deleteCustomer() {
  const id = document.getElementById("deleteCustomerId").value;
  if (!id) {
    alert("Please enter a valid Customer ID to delete!");
    return;
  }

  fetch(`/customers/${id}`, { method: "DELETE" })
    .then(response => {
      if (!response.ok) {
        throw new Error("Failed to delete customer. Status: " + response.status);
      }
      document.getElementById("deleteStatus").textContent = `(Status: ${response.status})`;
      toastr.success('Customer deleted successfully!', 'Delete');
      refreshCustomers();
    })
    .catch(error => {
      document.getElementById("deleteStatus").textContent = "(Error deleting customer)";
      toastr.error('Error deleting customer.', 'Delete');
      console.error(error);
    });
}

document.addEventListener("DOMContentLoaded", refreshCustomers);
