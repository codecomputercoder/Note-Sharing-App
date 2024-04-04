function login(name,pass){

    fetch('http://localhost:8080/genToken', {
        method: 'POST', // Change method to POST
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          "userName": name,
          "password": pass
        })
      })
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        console.log(response);
        return response.json(); // Expecting a text response, not JSON
      })
      .then(token => {
        console.log('Bearer Token:', token);

        if(token.role=="admin"){
            window.location.href="http://127.0.0.1:5500/admin.html";
        }
        if(token.role=="user"){
            window.location.href="http://127.0.0.1:5500/user.html";
        }
        localStorage.setItem("Token", token.token);
        localStorage.setItem("Role", token.role);
      })
      .catch(error => {
        console.error('Error:', error);
      });

    console.log("hello");
      
}





let loginForm = document.getElementById("loginForm");

loginForm.addEventListener("submit", (e) => {
  e.preventDefault();

  let username = document.getElementById("username");
  let password = document.getElementById("password");

  if (username.value == "" || password.value == "") {
    //alert("Ensure you input a value in both fields!");
  } else {
    // perform operation with form input
    //alert("This form has been successfully submitted!");
    console.log(
      `This form has a username of ${username.value} and password of ${password.value}`
    );

    login(username.value,password.value);

    username.value = "";
    password.value = "";
  }
});