const API_BASE = "http://localhost:8080/api";
const token = localStorage.getItem("jwtToken");
const taskId = localStorage.getItem("selectedTaskId");

if (!token || !taskId) {
  window.location.href = "tasks.html";
}

async function loadComments() {
  const res = await fetch(`${API_BASE}/tasks/${taskId}/comments`, {
    headers: { "Authorization": `Bearer ${token}` }
  });

  const comments = await res.json();
  const list = document.getElementById("comment-list");
  list.innerHTML = "";

  comments.forEach(comment => {
    const li = document.createElement("li");
    li.innerHTML = `
      <strong>User ${comment.userId}</strong>:
      ${comment.content}
      <br><small>${new Date(comment.timestamp).toLocaleString()}</small>
    `;
    list.appendChild(li);
  });
}

document.getElementById("comment-form").addEventListener("submit", async function (e) {
  e.preventDefault();
  const content = document.getElementById("comment-content").value;

  const res = await fetch(`${API_BASE}/tasks/${taskId}/comments`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Authorization": `Bearer ${token}`
    },
    body: JSON.stringify({ content })
  });

  if (res.ok) {
    document.getElementById("comment-form").reset();
    loadComments();
  } else {
    const error = await res.text();
    alert("Error: " + error);
  }
});

function goBack() {
  window.location.href = "tasks.html";
}

loadComments();
