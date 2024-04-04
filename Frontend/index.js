document
  .getElementById("registerButton")
  .addEventListener("click", async function () {
    const form = document.getElementById("registrationForm");
    const formData = new FormData(form);

    try {
      const response = await fetch("http://localhost:8080/registration", {
        method: "POST",
        body: JSON.stringify(Object.fromEntries(formData)),
        headers: {
          "Content-Type": "application/json",
        },
      });

      const responseData = await response.json();

      if (!response.ok) {
        throw new Error(responseData.message || "Registration failed");
      }

      alert("Registration successful!");
      window.location.href="http://127.0.0.1:5500/login.html";
      // Redirect or perform other actions upon successful registration
    } catch (error) {
      alert("Registration failed: " + error.message);
    }
  });
