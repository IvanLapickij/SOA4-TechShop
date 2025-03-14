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
