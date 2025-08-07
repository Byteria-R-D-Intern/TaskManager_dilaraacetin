const API_BASE = "http://localhost:8080/api";
const token = localStorage.getItem("jwtToken");

if (!token) {
  window.location.href = "login.html";
}

async function loadTasks() {
  const res = await fetch(`${API_BASE}/tasks`, {
    headers: { "Authorization": `Bearer ${token}` }
  });

  const tasks = await res.json();
  const list = document.getElementById("task-list");
  list.innerHTML = "";

  tasks.forEach(task => {
    const li = document.createElement("li");
    li.innerHTML = `
      <strong>${task.title}</strong> - ${task.status} - ${task.priority}
      <br>${task.description}
      <br><button onclick="viewComments(${task.id})">View Comments</button>
    `;
    list.appendChild(li);
  });
}

document.getElementById("task-form").addEventListener("submit", async function (e) {
  e.preventDefault();

  const task = {
    title: document.getElementById("title").value,
    description: document.getElementById("description").value,
    status: document.getElementById("status").value,
    priority: document.getElementById("priority").value,
    dueDate: document.getElementById("dueDate").value
  };

  const res = await fetch(`${API_BASE}/tasks`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${token}`
    },
    body: JSON.stringify(task)
  });

  if (res.ok) {
    alert("Task added!");
    loadTasks();
    e.target.reset();
  } else {
    const error = await res.text();
    alert("Error: " + error);
  }
});

function logout() {
  localStorage.removeItem("jwtToken");
  window.location.href = "login.html";
}

function viewComments(taskId) {
  localStorage.setItem("selectedTaskId", taskId);
  window.location.href = "task-details.html";
}

loadTasks();
