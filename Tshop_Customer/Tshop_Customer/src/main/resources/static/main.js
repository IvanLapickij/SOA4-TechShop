document.addEventListener("DOMContentLoaded", () => {
  // Fetch from the combined endpoint (adjust path if needed)
  fetch("/customers/all-with-orders")
    .then(response => {
      if (!response.ok) {
        throw new Error("Error fetching data");
      }
      return response.json();
    })
    .then(customerResponses => {
      populateTable(customerResponses);
    })
    .catch(error => console.error("Fetch Error:", error));
});

function populateTable(customerResponses) {
  const tableBody = document.getElementById("customerTable");
  tableBody.innerHTML = ""; // clear old rows

  customerResponses.forEach(cr => {
    // 'cr' is your CustomerResponse with both customer info and an 'orders' object
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${cr.custId}</td>
      <td>${cr.custName}</td>
      <td>${cr.custBod}</td>
      <td>${cr.custPhone}</td>
      <td>${cr.orders.orderId}</td>
      <td>${cr.orders.items}</td>
      <td>${cr.orders.price}</td>
    `;
    tableBody.appendChild(row);
  });
}

// Load table data on page load
document.addEventListener("DOMContentLoaded", () => {
  fetchCustomerOrders(); // fetches combined customer+order data
});

// GET All Customers + Orders
function fetchCustomerOrders() {
  fetch("/customers/all-with-orders") // your combined endpoint
    .then(response => {
      if (!response.ok) {
        throw new Error("Error fetching data");
      }
      return response.json();
    })
    .then(customerResponses => {
      populateTable(customerResponses);
    })
    .catch(error => console.error("Fetch Error:", error));
}

function populateTable(customerResponses) {
  const tableBody = document.getElementById("customerTable");
  tableBody.innerHTML = "";

  customerResponses.forEach(cr => {
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${cr.custId}</td>
      <td>${cr.custName}</td>
      <td>${cr.custBod}</td>
      <td>${cr.custPhone}</td>
      <td>${cr.orders.orderId}</td>
      <td>${cr.orders.items}</td>
      <td>${cr.orders.price}</td>
    `;
    tableBody.appendChild(row);
  });
}

// POST: Add a New Student
function addStudent() {
  const name = document.getElementById("newStudentName").value;
  const birthDate = document.getElementById("newStudentBirthDate").value;

  // Build a JSON object matching your "Customers" entity
  const newStudent = {
    custId: 0,           // or auto-gen in DB
    custName: name,
    custBod: birthDate,
    custPhone: "0000000", // or a real phone from a form if needed
    orderId: 11          // if relevant
  };

  fetch("/customers", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(newStudent)
  })
    .then(response => {
      if (!response.ok) {
        throw new Error("Failed to add student");
      }
      document.getElementById("addStatus").textContent = "(Status: " + response.status + ")";
      // Refresh the table to show the newly added student
      return fetchCustomerOrders();
    })
    .catch(error => {
      document.getElementById("addStatus").textContent = "(Error adding student)";
      console.error(error);
    });
}

// DELETE: Remove a Student by ID
function deleteStudent() {
  const studentId = document.getElementById("deleteStudentId").value;

  if (!studentId) {
    alert("Please enter an ID to delete!");
    return;
  }

  fetch("/customers/" + studentId, {
    method: "DELETE"
  })
    .then(response => {
      if (!response.ok) {
        throw new Error("Delete failed");
      }
      document.getElementById("deleteStatus").textContent = "(Status: " + response.status + ")";
      // Refresh table after delete
      return fetchCustomerOrders();
    })
    .catch(error => {
      document.getElementById("deleteStatus").textContent = "(Error deleting student)";
      console.error(error);
    });
}
function editStudent() {
  const id = document.getElementById("editStudentId").value;
  const name = document.getElementById("editStudentName").value;
  const birth = document.getElementById("editStudentBirthDate").value; // now retrieves correct date input

  const updatedStudent = {
    custId: parseInt(id),
    custName: name,
    custBod: birth,
    custPhone: "0000000", // or read from a separate input if needed
    orderId: 11          // or from another input if relevant
  };

  fetch("/customers/" + id, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(updatedStudent)
  })
    .then(response => {
      if (!response.ok) {
        throw new Error("Failed to update student: " + response.status);
      }
      return response.json();
    })
    .then(updated => {
      console.log("Updated student:", updated);
      // Refresh the table
      fetchCustomerOrders();
    })
    .catch(error => console.error(error));
}

