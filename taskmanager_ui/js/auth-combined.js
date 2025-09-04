const API_BASE = "http://localhost:8080/api";

window.addEventListener("DOMContentLoaded", () => {
  const container = document.getElementById("container");
  const signUpButton = document.getElementById("signUp");
  const signInButton = document.getElementById("signIn");

  signUpButton?.addEventListener("click", (e) => {
    e.preventDefault();
    container?.classList.add("right-panel-active");
  });
  signInButton?.addEventListener("click", (e) => {
    e.preventDefault();
    container?.classList.remove("right-panel-active");
  });

  const loginForm = document.getElementById("login-form");
  loginForm?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const email = document.getElementById("email-login").value.trim();
    const password = document.getElementById("password-login").value;
    hideMsg("login-error");

    try {
      const res = await fetch(`${API_BASE}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password })
      });

      const ct = (res.headers.get("content-type") || "").toLowerCase();
      if (!res.ok) {
        const txt = await res.text().catch(() => "");
        console.error("[login] status:", res.status, "body:", txt);
        showMsg("login-error", txt || "Giriş başarısız.");
        return;
      }

      let token = "";
      if (ct.includes("application/json")) {
        const data = await res.json().catch(() => ({}));
        if (typeof data === "string") {
          token = data.trim();
        } else {
          token = (data.token || data.access_token || data.jwt || "").toString().trim();
        }
      } else {
        const txt = (await res.text()).toString().trim();
        const m = txt.match(/[A-Za-z0-9\-_]+\.[A-Za-z0-9\-_]+\.[A-Za-z0-9\-_]+/);
        token = (m && m[0]) ? m[0] : txt.replace(/^"|"$/g, "");
      }

      if (!token || token.split(".").length !== 3) {
        console.warn("[login] geçersiz token:", token?.slice?.(0, 30) || "(boş)");
        showMsg("login-error", "Sunucu geçerli bir token döndürmedi.");
        return;
      }

      localStorage.setItem("jwtToken", token);
      console.log("[login] token kaydedildi, menüye gidiliyor");
      window.location.replace("menu.html"); 
    } catch (err) {
      console.error("[login] fetch error:", err);
      showMsg("login-error", "Bağlantı hatası. Tekrar deneyin.");
    }
  });

  const registerForm = document.getElementById("register-form");
  registerForm?.addEventListener("submit", async (e) => {
    e.preventDefault();
    const username = document.getElementById("username-register").value.trim();
    const email = document.getElementById("email-register").value.trim();
    const password = document.getElementById("password-register").value;

    hideMsg("register-error");
    hideMsg("register-ok");

    try {
      const res = await fetch(`${API_BASE}/auth/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, email, password })
      });
      if (!res.ok) {
        const txt = await res.text();
        showMsg("register-error", txt || "Kayıt başarısız.");
        return;
      }

      showMsg("register-ok", "Kayıt başarılı! Giriş paneline dönülüyor…");
      setTimeout(() => container?.classList.remove("right-panel-active"), 900);
    } catch (err) {
      console.error("[register] fetch error:", err);
      showMsg("register-error", "Bağlantı hatası. Tekrar deneyin.");
    }
  });

  function showMsg(id, text) {
    const el = document.getElementById(id);
    if (!el) return;
    el.textContent = text;
    el.style.display = "block";
  }
  function hideMsg(id) {
    const el = document.getElementById(id);
    if (!el) return;
    el.style.display = "none";
  }
});
