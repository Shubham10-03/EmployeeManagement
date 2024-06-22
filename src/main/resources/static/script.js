// Function to fetch and display content based on user interaction
async function fetchAndDisplayContent(action) {
    try {
        let url = 'http://localhost:1080/employees';
        let method = 'GET';
        let body = null;
        const contentDiv = document.querySelector('.content');

        switch (action) {
            case 'list':
                method = 'GET';
                break;

            case 'view':
                const employeeId = prompt('Please enter Employee ID:');
                if (!employeeId) {
                    alert('Employee ID is required.');
                    return;
                }
                url += `/${employeeId}`;
                method = 'GET';
                break;

            case 'add':
                contentDiv.innerHTML = `
                    <form id="add-employee-form">
                        <label for="add-employee-id">Employee ID:</label>
                        <input type="number" id="add-employee-id" placeholder = "Employee Id must be in range of 1000 to 999999" name="employeeId" required><br><br>
                        <label for="add-employee-firstname">First Name:</label>
                        <input type="text" id="add-employee-firstname" placeholder = "Employee's First Name" name="employeeFirstName" required><br><br>
                        <label for="add-employee-lastname">Last Name:</label>
                        <input type="text" id="add-employee-lastname" name="employeeLastName" placeholder = "Employee's Last Name" required><br><br>
                        <label for="add-employee-age">Age:</label>
                        <input type="number" id="add-employee-age" name="employeeAge" placeholder = "Employee's Age" required><br><br>
                        <label for="add-employee-project">Project:</label>
                        <input type="text" id="add-employee-project" name="employeeProject" placeholder = "Employee's Project" required><br><br>
                        <button type="submit">Add Employee</button>
                    </form>
                `;
                document.getElementById('add-employee-form').addEventListener('submit', async function(event) {
                    event.preventDefault();
                    await validateAndSubmitForm(event, 'POST');
                });
                return;

            case 'update':
                const updateEmployeeId = prompt('Please enter Employee ID for update:');
                if (!updateEmployeeId) {
                    alert('Employee ID is required.');
                    return;
                }
                const employeeToUpdate = await getEmployeeDetails(updateEmployeeId);
                if (!employeeToUpdate) {
                    alert('Employee not found.');
                    return;
                }
                contentDiv.innerHTML = `
                    <form id="update-employee-form">
                        <label for="update-employee-id">Employee ID:</label>
                        <input type="text" id="update-employee-id" name="employeeId" placeholder="Number ranging from 1000 to 999999" value="${employeeToUpdate.employeeId}" readonly><br><br>
                        <label for="update-employee-firstname">First Name:</label>
                        <input type="text" id="update-employee-firstname" name="employeeFirstName" placeholder="First Name" value="${employeeToUpdate.employeeFirstName}" required><br><br>
                        <label for="update-employee-lastname">Last Name:</label>
                        <input type="text" id="update-employee-lastname" name="employeeLastName" placeholder = "Last Name" value="${employeeToUpdate.employeeLastName}" required><br><br>
                        <label for="update-employee-age">Age:</label>
                        <input type="number" id="update-employee-age" name="employeeAge" placeholder = "Employee Age" value="${employeeToUpdate.employeeAge}" required><br><br>
                        <label for="update-employee-project">Project:</label>
                        <input type="text" id="update-employee-project" name="employeeProject" placeholder="Project Assigned" value="${employeeToUpdate.employeeProject}" required><br><br>
                        <button type="submit">Update Employee</button>
                    </form>
                `;
                document.getElementById('update-employee-form').addEventListener('submit', async function(event) {
                    event.preventDefault();
                    await validateAndSubmitForm(event, 'PUT');
                });
                return;

            case 'delete':
                const deleteEmployeeId = prompt('Please enter Employee ID for deletion:');
                if (!deleteEmployeeId) {
                    alert('Employee ID is required.');
                    return;
                }
                url += `/${deleteEmployeeId}`;
                method = 'DELETE';
                break;

            default:
                console.error('Invalid action');
                return;
        }

        const options = {
            method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: body ? JSON.stringify(body) : null
        };

        const response = await fetch(url, options);
        
        if (!response.ok) {
            throw new Error(`Error fetching data: ${response.status}`);
        }
        
        // Handle DELETE action which returns no content
        if (action === 'delete') {
            contentDiv.innerHTML = `<p>Employee deleted successfully!</p>`;
            return;
        }

        const data = await response.json();

        switch (action) {
            case 'list':
                const table = `
                    <table>
                        <thead>
                            <tr>
                                <th>Employee ID</th>
                                <th>First Name</th>
                                <th>Last Name</th>
                                <th>Age</th>
                                <th>Project</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${data.map(employee => `
                                <tr>
                                    <td>${employee.employeeId}</td>
                                    <td>${employee.employeeFirstName}</td>
                                    <td>${employee.employeeLastName}</td>
                                    <td>${employee.employeeAge}</td>
                                    <td>${employee.employeeProject}</td>
                                </tr>
                            `).join('')}
                        </tbody>
                    </table>
                `;
                contentDiv.innerHTML = table;
                break;

            case 'view':
                const employeeDetails = `
                    <h2>Employee Details</h2>
                    <p><strong>Employee ID:</strong> ${data.employeeId}</p>
                    <p><strong>First Name:</strong> ${data.employeeFirstName}</p>
                    <p><strong>Last Name:</strong> ${data.employeeLastName}</p>
                    <p><strong>Age:</strong> ${data.employeeAge}</p>
                    <p><strong>Project:</strong> ${data.employeeProject}</p>
                `;
                contentDiv.innerHTML = employeeDetails;
                break;

            default:
                console.error('Invalid action');
        }

    } catch (error) {
        console.error(`Error fetching data: ${error.message}`);
        document.querySelector('.content').innerHTML = `<p>Error fetching data: ${error.message}</p>`;
    }
}

// Function to handle form submissions with validation
async function validateAndSubmitForm(event, method) {
    event.preventDefault();
    const form = event.target;
    const url = 'http://localhost:1080/employees';
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());

    // Validation
    if (method === 'PUT' || method === 'POST') {
        const employeeId = data.employeeId;
        const employeeAge = parseInt(data.employeeAge, 10);

        if (isNaN(employeeId)) {
            alert('Employee ID must be a number.');
            form.reset(); // Reset the form
            return;
        }

        if (employeeAge < 18 || employeeAge > 60) {
            alert('Employee age must be between 18 and 60.');
            form.reset(); // Reset the form
            return;
        }
    }

    try {
        const response = await fetch(url, {
            method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.status === 201) {
            alert('Employee added successfully!');
        } else if (response.status === 202) {
            alert('Employee updated successfully!');
        } else if (response.status === 204) {
            alert('Employee deleted successfully!');
        } else if (response.status === 400) {
            throw new Error('Bad request. Please check the data and try again.');
        } else if (response.status === 404) {
            throw new Error('Employee not found.');
        } else {
            throw new Error(`Unexpected response: ${response.status}`);
        }

        fetchAndDisplayContent('list');
    } catch (error) {
        alert(`Error: ${error.message}`);
    }
}

// Function to get employee details by ID
async function getEmployeeDetails(employeeId) {
    try {
        const response = await fetch(`http://localhost:1080/employees/${employeeId}`);

        if (!response.ok) {
            throw new Error(`Error fetching employee details: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        alert(`Error: ${error.message}`);
        return null;
    }
}

// Event listeners for navigation links and form submissions
document.addEventListener('DOMContentLoaded', () => {
    const links = document.querySelectorAll('.nav-links a');

    links.forEach(link => {
        link.addEventListener('click', async function(event) {
            event.preventDefault();
            const action = this.getAttribute('data-action');
            await fetchAndDisplayContent(action);
        });
    });
});
