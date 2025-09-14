const API_BASE = "http://localhost:8080/api";

(function init() {
  const token = localStorage.getItem("jwtToken");
  console.log("[menu] token var mı?", !!token, "uzunluk:", token?.length || 0);
  if (!token) { window.location.href = "auth.html"; return; }

  const role = getRoleFromJWT(token) || "ROLE_USER";
  console.log("[menu] role:", role);

  const roleBadge = document.getElementById("role-badge");
  roleBadge && (roleBadge.textContent = role);

  fetch(`${API_BASE}/users`, { headers: { "Authorization": `Bearer ${token}` } })
    .then(async res => {
      console.log("[/users] status:", res.status);
      if (!res.ok) throw new Error(await res.text());
      return res.json();
    })
    .then(user => {
      const welcome = document.getElementById("welcome");
      if (welcome) welcome.textContent = `Hoş geldin, ${user.username}`;
    })
    .catch(err => console.warn("Kullanıcı bilgisi alınamadı:", err));

  toggleRoleMenu(role);
  wireLinks();

  document.getElementById("logout-btn")?.addEventListener("click", () => {
    localStorage.removeItem("jwtToken");
    window.location.href = "auth.html";
  });
})();

function wireLinks() {
  const go = (url) => { window.location.href = url; };

  document.getElementById("link-tasks")?.addEventListener("click", (e) => { e.preventDefault(); go("my-tasks.html"); });
  document.getElementById("link-create")?.addEventListener("click", (e) => { e.preventDefault(); go("create-task.html"); });
  document.getElementById("link-admin-create")?.addEventListener("click", (e)=>{ e.preventDefault(); window.location.href = "admin-create-task.html"; });
  document.getElementById("link-all")?.addEventListener("click", (e) => { e.preventDefault(); go("all-tasks.html"); });
  document.getElementById("link-stats")?.addEventListener("click", (e) => { e.preventDefault(); go("stats.html"); });
  document.getElementById("link-users")?.addEventListener("click", (e) => { e.preventDefault(); go("users-admin.html"); });
  document.getElementById("link-logs")?.addEventListener("click", (e) => { e.preventDefault(); go("logs.html"); });
  document.getElementById("link-my-logs")?.addEventListener("click", (e)=>{ e.preventDefault(); window.location.href = "notifications.html"; });
  document.getElementById("link-home")?.addEventListener("click", (e) => { e.preventDefault(); window.scrollTo({ top: 0, behavior: "smooth" }); });
  document.getElementById("link-contact")?.addEventListener("click", (e) => { e.preventDefault(); alert("İletişim: support@example.com"); });
}

function toggleRoleMenu(role) {
  const mmItems = document.querySelectorAll(".role-mm");
  const adminItems = document.querySelectorAll(".role-admin");
  const isManager = role === "ROLE_MANAGER" || role === "ROLE_ADMIN";
  const isAdmin = role === "ROLE_ADMIN";
  mmItems.forEach(li => li.style.display = isManager ? "" : "none");
  adminItems.forEach(li => li.style.display = isAdmin ? "" : "none");
}

function getRoleFromJWT(token) {
  try {
    const payload = token.split(".")[1];
    const json = JSON.parse(atob(payload.replace(/-/g, "+").replace(/_/g, "/")));
    return json.role;
  } catch (_) {
    return null;
  }
}