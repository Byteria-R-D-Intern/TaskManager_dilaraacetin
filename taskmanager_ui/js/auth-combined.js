// Panel geçişleri ve login/register akışı
window.addEventListener('DOMContentLoaded', () => {
  const container = document.getElementById('container');
  const signUpButton = document.getElementById('signUp');
  const signInButton = document.getElementById('signIn');

  signUpButton?.addEventListener('click', (e) => { e.preventDefault(); container?.classList.add("right-panel-active"); });
  signInButton?.addEventListener('click', (e) => { e.preventDefault(); container?.classList.remove("right-panel-active"); });

  // ---- Login ----
  const loginForm = document.getElementById("login-form");
  loginForm?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const email = document.getElementById("email-login").value.trim();
    const password = document.getElementById("password-login").value;
    hideMsg("login-error");

    try {
      const res = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password })
      });
      if (!res.ok) { const txt = await res.text(); showMsg("login-error", txt || "Giriş başarısız."); return; }

      const data = await res.json();
      localStorage.setItem("jwtToken", data.token);
      window.location.href = "menu.html"; // menüye yönlendir
    } catch (err) {
      showMsg("login-error", "Bağlantı hatası. Tekrar deneyin."); console.error(err);
    }
  });

  // ---- Register ----
  const registerForm = document.getElementById("register-form");
  registerForm?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const username = document.getElementById("username-register").value.trim();
    const email = document.getElementById("email-register").value.trim();
    const password = document.getElementById("password-register").value;

    hideMsg("register-error"); hideMsg("register-ok");

    try {
      const res = await fetch("http://localhost:8080/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, email, password })
      });
      if (!res.ok) { const txt = await res.text(); showMsg("register-error", txt || "Kayıt başarısız."); return; }

      showMsg("register-ok", "Kayıt başarılı! Giriş paneline dönülüyor…");
      setTimeout(() => container?.classList.remove("right-panel-active"), 900);
    } catch (err) {
      showMsg("register-error", "Bağlantı hatası. Tekrar deneyin."); console.error(err);
    }
  });

  function showMsg(id, text){ const el = document.getElementById(id); if (!el) return; el.textContent = text; el.style.display = 'block'; }
  function hideMsg(id){ const el = document.getElementById(id); if (!el) return; el.style.display = 'none'; }
});
