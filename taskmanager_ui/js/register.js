document.getElementById("register-form").addEventListener("submit", async function (e) {
  e.preventDefault();

  const username = document.getElementById("username").value;
  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  try {
    const res = await fetch("http://localhost:8080/api/auth/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ username, email, password })
    });

    if (res.ok) {
      alert("Registration successful! Redirecting to login...");
      window.location.href = "login.html";
    } else {
      const error = await res.text();
      document.getElementById("error-msg").innerText = error;
    }
  } catch (err) {
    document.getElementById("error-msg").innerText = "Registration failed";
  }
});
